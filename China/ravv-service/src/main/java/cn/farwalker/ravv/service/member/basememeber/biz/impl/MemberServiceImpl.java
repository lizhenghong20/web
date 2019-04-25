package cn.farwalker.ravv.service.member.basememeber.biz.impl;

import cn.farwalker.ravv.service.goodsext.favorite.biz.IGoodsFavoriteBiz;
import cn.farwalker.ravv.service.goodsext.favorite.model.GoodsFavoriteBo;
import cn.farwalker.ravv.service.goodsext.viewlog.biz.IGoodsViewLogBiz;
import cn.farwalker.ravv.service.goodsext.viewlog.model.GoodsViewLogBo;
import cn.farwalker.ravv.service.member.basememeber.biz.IMemberBiz;
import cn.farwalker.ravv.service.member.basememeber.biz.IMemberService;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.member.basememeber.model.MemberExVo;
import cn.farwalker.ravv.service.member.pam.member.biz.IPamMemberBiz;
import cn.farwalker.ravv.service.member.pam.member.model.PamMemberBo;
import cn.farwalker.ravv.service.youtube.liveanchor.biz.IYoutubeLiveAnchorBiz;
import cn.farwalker.ravv.service.youtube.liveanchor.model.YoutubeLiveAnchorBo;
import cn.farwalker.ravv.service.youtube.service.IYoutubeService;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by asus on 2018/11/8.
 */

@Slf4j
@Service
public class MemberServiceImpl implements IMemberService {

    @Autowired
    private IMemberBiz iMemberBiz;

    @Autowired
    private IGoodsFavoriteBiz iGoodsFavoriteBiz;

    @Autowired
    private IGoodsViewLogBiz iGoodsViewLogBiz;

    @Override
    public MemberExVo getBasicInfo(Long memberId) {
        MemberExVo memberInfoVo = new MemberExVo();
        MemberBo memberBo = iMemberBiz.selectById(memberId);
        BeanUtils.copyProperties(memberBo, memberInfoVo);
        if(memberBo.getBirthday() == null)
            memberInfoVo.setBirth(false);
        else
            memberInfoVo.setBirth(true);
        if(memberInfoVo.getAvator() != null)
            memberInfoVo.setAvator(QiniuUtil.getFullPath(memberInfoVo.getAvator()));
        //查出收藏，关注，最近浏览量
        int favoriteCount = 0;
        favoriteCount = iGoodsFavoriteBiz.selectCount(Condition.create().eq(GoodsFavoriteBo.Key.memberId.toString(), memberId));
        if(favoriteCount != 0)
            memberInfoVo.setFavoriteCount(favoriteCount);
        int viewCount = 0;
        viewCount = iGoodsViewLogBiz.selectCount(Condition.create().eq(GoodsViewLogBo.Key.memberId.toString(), memberId));
        if(viewCount != 0)
            memberInfoVo.setViewCount(viewCount);

        return memberInfoVo;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public MemberExVo addBasicInfo(Long memberId, MemberBo memberInfo) {
        //如果有图片，拆出路径
        if(memberInfo.getAvator() != null)
            memberInfo.setAvator(QiniuUtil.getRelativePath(memberInfo.getAvator()));
        Date now = new Date();
        memberInfo.setGmtModified(now);
        EntityWrapper<MemberBo> queryMember = new EntityWrapper<>();
        queryMember.eq(MemberBo.Key.id.toString(), memberId);
        if(!iMemberBiz.update(memberInfo, queryMember)){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return getBasicInfo(memberId);
    }

}
