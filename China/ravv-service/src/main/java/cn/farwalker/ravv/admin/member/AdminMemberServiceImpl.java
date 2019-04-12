package cn.farwalker.ravv.admin.member;


import cn.farwalker.ravv.service.member.basememeber.biz.IMemberBiz;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.member.basememeber.model.MemberVo;
import cn.farwalker.ravv.service.member.level.biz.IMemberLevelBiz;
import cn.farwalker.ravv.service.member.level.model.MemberLevelBo;
import cn.farwalker.ravv.service.sys.syslogs.biz.ISysLogsBiz;
import cn.farwalker.ravv.service.sys.syslogs.model.SysLogsBo;
import cn.farwalker.ravv.service.sys.user.biz.ISysUserBiz;
import cn.farwalker.ravv.service.sys.user.model.SysUserBo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.core.base.controller.ControllerUtils;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.SerialNumber;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.orm.core.annotation.LoadJoinValueImpl;
import com.cangwu.frame.web.crud.QueryFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class AdminMemberServiceImpl implements AdminMemberService {

    @Resource
    private IMemberBiz memberBiz;

    @Resource
    private IMemberLevelBiz memberLevelBiz;

    protected IMemberBiz getBiz() {
        return memberBiz;
    }

    protected IMemberLevelBiz getLevelBiz() {
        return memberLevelBiz;
    }

    @Resource
    private ISysLogsBiz sysLogsBiz;

    @Resource
    private ISysUserBiz sysUserBiz;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean createMember(MemberBo vo) {
        vo.setAvator(QiniuUtil.getRelativePath(vo.getAvator()));
        Boolean rs = getBiz().insert(vo);

        if (rs) {
            // 存入会员专属推荐码
            vo.setReferralCode(SerialNumber.idToCode(vo.getId()));

            getBiz().updateById(vo);
        }else {
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }

        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean deleteMemebr(Long id) {
        Boolean rs = getBiz().deleteById(id);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        return rs;
    }

    @Override
    public MemberVo getMember(Long id) {
        // createMethodSinge创建方法
        MemberBo bo = getBiz().selectById(id);
        MemberVo rs = Tools.bean.cloneBean(bo, new MemberVo());
        LoadJoinValueImpl.load(getBiz(), rs);
        rs.setAvator(QiniuUtil.getFullPath(rs.getAvator()));
        return rs;
    }

    @Override
    public Page<MemberVo> getMemberList(List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        String queryName = "";
        String queryAccount = "";
        if (Tools.collection.isNotEmpty(query)) {
            Integer queryLen = query.size() - 1;
            for (Integer i = queryLen; i >= 0; i--) {
                QueryFilter q = query.get(i);
                if (q.getField().equals("name")) {
                    queryName = q.getStartValue();
                    query.remove(q);
                } else if (q.getField().equals("emailAccount")) {
                    queryAccount = q.getStartValue();
                    query.remove(q);
                }
            }
        }
        // createMethodSinge创建方法
        Page<MemberBo> page = ControllerUtils.getPage(start, size, sortfield);

        Wrapper<MemberBo> wrap = ControllerUtils.getWrapper(query);
        if(Tools.string.isNotEmpty(queryName)) {
            wrap.like("concat(" + MemberBo.Key.firstname.toString() + ",' ',"+ MemberBo.Key.lastname +")", queryName);
        }
        if(Tools.string.isNotEmpty(queryAccount)) {
            wrap.and("id in (select member_id from pam_member where email_account like concat('%',{0},'%'))", queryAccount);
        }
        Page<MemberBo> memberPage = getBiz().selectPage(page, wrap);
        // 联表获取对应id的名称
        Page<MemberVo> rs = ControllerUtils.convertPageRecord(memberPage, MemberVo.class);
        List<MemberVo> memberVoList = rs.getRecords();
        LoadJoinValueImpl.load(getBiz(), memberVoList);
        //
        Integer length = memberVoList.size();
        for (Integer i = 0; i < length; i++) {
            MemberVo vo = memberVoList.get(i);
            vo.setName(vo.getFirstname() + " " + vo.getLastname());
            vo.setAvator(QiniuUtil.getFullPath(vo.getAvator()));
        }
        // 处理图片全路径
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean updateMember(MemberBo vo, Long userId) {
        vo.setAvator(QiniuUtil.getRelativePath(vo.getAvator()));

        // 获取管理员信息
        SysUserBo sysUserBo = sysUserBiz.selectById(userId);
        if (null == sysUserBo) {
            throw new WakaException("没有对应管理员信息");
        }

        // 判断推荐人推荐码
        MemberBo referalMember = getBiz().memberByReferral(vo.getReferrerReferalCode());
        if (null == referalMember && vo.getReferrerReferalCode().equals("RAVV")) {// RAVV系统名称
            throw new WakaException("推荐人邀请码不存在");
        }

        // 判断推荐人推荐码修改
        MemberBo memberBo = getBiz().selectById(vo.getId());
        if (null == memberBo) {
            throw new WakaException("没有对应会员信息");
        }
        Boolean flag = false;
        String reReferalCode = memberBo.getReferrerReferalCode();
        if (Tools.string.isNotEmpty(reReferalCode)  && !reReferalCode.equals(vo.getReferrerReferalCode())) {
            flag = true;
        }

        // 若原本会员没有推荐码，重新存入会员专属推荐码
        if (Tools.string.isEmpty(memberBo.getReferralCode())) {
            vo.setReferralCode(SerialNumber.idToCode(memberBo.getId()));
        }

        Boolean rs = getBiz().updateById(vo);

        // 存储推荐人修改记录
        if (rs && flag) {
            if (Tools.string.isNotEmpty(vo.getReferrerReferalCode())) {
                SysLogsBo logsBo = new SysLogsBo();
                logsBo.setSourcetable("member");
                logsBo.setSourcefield("referrer_referal_code");
                logsBo.setSourceid(String.valueOf(memberBo.getId()));
                logsBo.setContent(memberBo.getName() + "的推荐人推荐码" + memberBo.getReferrerReferalCode() + "已被管理员"
                        + sysUserBo.getName() + "修改");

                sysLogsBiz.insert(logsBo);
            }
        }

        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean createMemberLevel(MemberLevelBo vo) {
        // 检查数据表中的会员默认等级memberLevelBo
        MemberLevelBo memberLevel = memberLevelBiz.getSameByDefaultLevel();

        if (null != memberLevel) {
            if (vo.getDefaultLevel()) {
                throw new WakaException("添加失败，默认会员等级已经存在");
            }
        }

        Boolean rs = getLevelBiz().insert(vo);
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean deleteMemberLevel(Long id) {
        Boolean rs = getLevelBiz().deleteById(id);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        return rs;
    }

    @Override
    public MemberLevelBo getMemberLevel(Long id) {
        MemberLevelBo rs = getLevelBiz().selectById(id);
        if(rs == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        return rs;
    }

    @Override
    public Page<MemberLevelBo> getMemberLevelList(List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        // createMethodSinge创建方法
        Page<MemberLevelBo> page = ControllerUtils.getPage(start, size, sortfield);
        Wrapper<MemberLevelBo> wrap = ControllerUtils.getWrapper(query);
        Page<MemberLevelBo> rs = getLevelBiz().selectPage(page, wrap);

        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Object updateMemberLevel(MemberLevelBo vo) {
        // 检查数据表中的会员默认等级memberLevelBo
        MemberLevelBo memberLevel = memberLevelBiz.getSameByDefaultLevel();

        if (null != memberLevel) {
            if (vo.getDefaultLevel()) {
                //判断检查相同属性名下，是否有相同的属性值
                if(memberLevel.getId().equals(vo.getId() )){

                    Object rs = getLevelBiz().updateAllColumnById(vo);
                    return JsonResult.newSuccess(rs);
                }
                return JsonResult.newFail("添加失败，默认会员等级已经存在");
            }

        }
        Object rs = getLevelBiz().updateAllColumnById(vo);
        return rs;
    }
}
