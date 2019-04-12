package cn.farwalker.ravv.admin.payment.withdrawapply;


import cn.farwalker.ravv.admin.payment.withdrawapply.dto.MemberWithdrawApplyVo;
import cn.farwalker.ravv.service.payment.withdrawapply.biz.IMemberWithdrawApplyBiz;
import cn.farwalker.ravv.service.payment.withdrawapply.model.MemberWithdrawApplyBo;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.core.base.controller.ControllerUtils;
import cn.farwalker.waka.core.RavvExceptionEnum;
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
public class AdminWithdrawApplyServiceImpl implements AdminWithdrawApplyService {
    @Resource
    private IMemberWithdrawApplyBiz memberWithdrawApplyBiz;
    protected IMemberWithdrawApplyBiz getBiz(){
        return memberWithdrawApplyBiz;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean create(MemberWithdrawApplyBo vo) {
        Boolean rs = getBiz().insert(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        Boolean rs =getBiz().deleteById(id);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        return rs;
    }

    @Override
    public MemberWithdrawApplyVo getOne(Long id) {
        MemberWithdrawApplyBo withdrawApply =getBiz().selectById(id);

        //返回MemberWithdrawApplyVo的数据
        MemberWithdrawApplyVo withdrawApplyVo = new MemberWithdrawApplyVo();
        Tools.bean.cloneBean(withdrawApply, withdrawApplyVo);

        LoadJoinValueImpl.load(memberWithdrawApplyBiz, withdrawApplyVo);

        return withdrawApplyVo;
    }

    @Override
    public Page<MemberWithdrawApplyVo> getList(List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        Page<MemberWithdrawApplyBo> page = ControllerUtils.getPage(start,size,sortfield);
        Wrapper<MemberWithdrawApplyBo> wrap =ControllerUtils.getWrapper(query);
        Page<MemberWithdrawApplyBo> withdrawApplyPage =getBiz().selectPage(page,wrap);

        Page<MemberWithdrawApplyVo> withdrawApplyVoPage = ControllerUtils.convertPageRecord(withdrawApplyPage, MemberWithdrawApplyVo.class);

        LoadJoinValueImpl.load(memberWithdrawApplyBiz, withdrawApplyVoPage.getRecords());

        return withdrawApplyVoPage;
    }
}
