package cn.farwalker.ravv.admin.youtube;

import cn.farwalker.ravv.admin.youtube.dto.LiveAnchorVO;
import cn.farwalker.ravv.service.sys.menu.biz.ISysMenuBiz;
import cn.farwalker.ravv.service.youtube.liveanchor.biz.IYoutubeLiveAnchorBiz;
import cn.farwalker.ravv.service.youtube.liveanchor.model.YoutubeLiveAnchorBo;
import cn.farwalker.ravv.service.youtube.livevideo.biz.IYoutubeLiveVideoBiz;
import cn.farwalker.ravv.service.youtube.livevideo.model.YoutubeLiveVideoBo;
import cn.farwalker.ravv.service.youtube.service.IYoutubeService;
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
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class AdminYoutubeServiceImpl implements AdminYoutubeService {

    @Resource
    private IYoutubeLiveAnchorBiz youtubeLiveAnchorBiz;

    protected IYoutubeLiveAnchorBiz getBiz() {
        return youtubeLiveAnchorBiz;
    }

    @Resource
    private IYoutubeService youtubeService;

    @Resource
    private ISysMenuBiz sysMenuBiz;

    @Resource
    private IYoutubeLiveVideoBiz youtubeLiveVideoBiz;

    protected IYoutubeLiveVideoBiz getYoutubeLiveVideoBiz(){
        return youtubeLiveVideoBiz;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean createLiveAnchor(YoutubeLiveAnchorBo vo) {
        if (null != vo.getSequence()) {
            // 校验主播推荐和排序是否超出或重复（目前推荐主播数为5个）
            youtubeLiveAnchorBiz.checkRecommend(vo.getId(), vo.getSequence());
        }
        Boolean rs = getBiz().insert(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean deleteLiveAnchor(Long id) {
        //不能删除主播数据
//		Boolean rs = getBiz().deleteById(id);
        return true;
    }

    @Override
    public YoutubeLiveAnchorBo getOneLiveAnchor(Long id) {
        YoutubeLiveAnchorBo rs = getBiz().selectById(id);
        if(rs == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        return rs;
    }

    @Override
    public Page<LiveAnchorVO> getListLiveAnchor(List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        Page<YoutubeLiveAnchorBo> page = ControllerUtils.getPage(start, size, sortfield);
        Wrapper<YoutubeLiveAnchorBo> wrap = ControllerUtils.getWrapper(query);
        Page<YoutubeLiveAnchorBo> liveAnchorPage = getBiz().selectPage(page, wrap);

        Page<LiveAnchorVO> liveAnchorVoPage = ControllerUtils.convertPageRecord(liveAnchorPage, LiveAnchorVO.class);

        LoadJoinValueImpl.load(youtubeLiveAnchorBiz, liveAnchorVoPage.getRecords());
        if (Tools.collection.isNotEmpty(liveAnchorVoPage.getRecords())) {
            for (LiveAnchorVO liveAnchorVO : liveAnchorVoPage.getRecords()) {
                Integer liveFollowNum = youtubeService.liveFollowNumByAnchor(liveAnchorVO.getAnchorMemberId());
                liveAnchorVO.setLiveFollowNum(liveFollowNum);
            }
        }

        return liveAnchorVoPage;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean updateLiveAnchor(YoutubeLiveAnchorBo vo) {
        if (null != vo.getSequence()) {
            // 校验主播推荐和排序是否超出或重复（目前推荐主播数为5个）
            youtubeLiveAnchorBiz.checkRecommend(vo.getId(), vo.getSequence());
        }
        Boolean rs = getBiz().updateAllColumnById(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }

    @Override
    public Integer getMonthLiveDuration(Long anchorMemberId, Date month) {
        Integer playtimeTotal = youtubeService.getMonthLiveDuration(anchorMemberId, month);
        if(playtimeTotal< 0){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        return playtimeTotal;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean freezeAnchor(YoutubeLiveAnchorBo vo) {
        YoutubeLiveAnchorBo anchor = youtubeLiveAnchorBiz.selectById(vo.getId());
        if(null == anchor) {
            throw new WakaException("找不到主播信息");
        }
        if(null != anchor.getFrozenEnd()) {
            Date datetime = new Date();
            if(anchor.getFrozenEnd().getTime() > datetime.getTime()) {
                throw new WakaException("账号处于冻结中，需先解除冻结后才能修改冻结时间");
            }
        }
        Boolean rs = youtubeLiveAnchorBiz.updateById(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean unfreezeAnchor(Long anchorId, Long sysUserId, String unfreezeReason) {
        // 判断管理员是否有权限解封账号
        List<String> roleCodeList = sysMenuBiz.getRoleCodeList(sysUserId);
        if (Tools.collection.isEmpty(roleCodeList)) {
            throw new WakaException("管理员没有权限进行解封操作");
        }
        Boolean flag = false;
        for (String roleCode : roleCodeList) {
            if (roleCode.equals("unfreeze")) {// TODO 解封权限按钮编码未定
                flag = true;
                break;
            }
        }

        if (!flag) {
//			return JsonResult.newFail("没有权限进行解封操作"); // TODO 解封权限按钮编码未定,方便测试暂注释控制代码
        }

        // 获取主播信息
        YoutubeLiveAnchorBo liveAnchor = getBiz().selectById(anchorId);
        if (null == liveAnchor) {
            throw new WakaException("找不到主播信息");
        }

        // 将冻结开始时间和结束时间修改为默认时间，并存储解除冻结原因
        liveAnchor.setFrozenStart(Tools.date.parseDate("1900/01/01 00:00:00").getTime());
        liveAnchor.setFrozenEnd(Tools.date.parseDate("1900/01/01 00:00:00").getTime());
        liveAnchor.setUnfreezeReason(unfreezeReason);

        Boolean rs = getBiz().updateAllColumnById(liveAnchor);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean createLiveVideo(YoutubeLiveVideoBo vo) {
        Boolean rs = getYoutubeLiveVideoBiz().insert(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean deleteLiveVideo(Long id) {
        Boolean rs = getYoutubeLiveVideoBiz().deleteById(id);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        return rs;
    }

    @Override
    public YoutubeLiveVideoBo getOneLiveVideo(Long id) {
        YoutubeLiveVideoBo rs = getYoutubeLiveVideoBiz().selectById(id);
        if(rs == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        return rs;
    }

    @Override
    public Page<YoutubeLiveVideoBo> getListLiveVideo(List<QueryFilter> query, Integer start, Integer size,
                                                     String sortfield , Long anchorMemberId) {
        Page<YoutubeLiveVideoBo> page =ControllerUtils.getPage(start,size,sortfield);
        Wrapper<YoutubeLiveVideoBo> wrap =ControllerUtils.getWrapper(query);

        //目前直播视频入口在主播列表，所以根据主播会员id查找
        if(null == anchorMemberId) {
            throw new WakaException("主播会员id不能空");
        }
        wrap.eq(YoutubeLiveVideoBo.Key.anchorMemberId.toString(), anchorMemberId);

        Page<YoutubeLiveVideoBo> rs = getYoutubeLiveVideoBiz().selectPage(page,wrap);

        //补全图片全路径 TODO是否需要？

        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean updateLiveVideo(YoutubeLiveVideoBo vo) {
        Boolean rs = getYoutubeLiveVideoBiz().updateById(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }
}
