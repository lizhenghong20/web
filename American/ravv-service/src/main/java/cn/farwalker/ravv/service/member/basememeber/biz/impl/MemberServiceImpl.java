package cn.farwalker.ravv.service.member.basememeber.biz.impl;

import cn.farwalker.ravv.service.goodsext.favorite.biz.IGoodsFavoriteBiz;
import cn.farwalker.ravv.service.goodsext.favorite.model.GoodsFavoriteBo;
import cn.farwalker.ravv.service.goodsext.viewlog.biz.IGoodsViewLogBiz;
import cn.farwalker.ravv.service.goodsext.viewlog.model.GoodsViewLogBo;
import cn.farwalker.ravv.service.member.basememeber.biz.IMemberBiz;
import cn.farwalker.ravv.service.member.basememeber.biz.IMemberService;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.member.basememeber.model.MemberExVo;
import cn.farwalker.ravv.service.member.pam.constants.LoginTypeEnum;
import cn.farwalker.ravv.service.member.pam.member.biz.IPamMemberBiz;
import cn.farwalker.ravv.service.member.pam.member.model.PamMemberBo;
import cn.farwalker.ravv.service.member.thirdpartaccount.biz.IMemberThirdpartAccountBiz;
import cn.farwalker.ravv.service.member.thirdpartaccount.model.MemberThirdpartAccountBo;
import cn.farwalker.ravv.service.youtube.liveanchor.biz.IYoutubeLiveAnchorBiz;
import cn.farwalker.ravv.service.youtube.liveanchor.model.YoutubeLiveAnchorBo;
import cn.farwalker.ravv.service.youtube.service.IYoutubeService;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;
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
    private IPamMemberBiz iPamMemberBiz;

    @Autowired
    private IGoodsFavoriteBiz iGoodsFavoriteBiz;

    @Autowired
    private IGoodsViewLogBiz iGoodsViewLogBiz;

    @Autowired
    private IYoutubeService iYoutubeService;

    @Autowired
    private IYoutubeLiveAnchorBiz iYoutubeLiveAnchorBiz;

    @Autowired
    private IMemberThirdpartAccountBiz memberThirdpartAccountBiz;

    @Override
    public MemberExVo getBasicInfo(Long memberId, String loginType) {
        LoginTypeEnum type = null;
        if(LoginTypeEnum.GOOGLE.getLabel().equals(loginType)){
            type = LoginTypeEnum.GOOGLE;
        } else if(LoginTypeEnum.FACEBOOK.getLabel().equals(loginType)){
            type  = LoginTypeEnum.FACEBOOK;
        }
        MemberExVo memberInfoVo = new MemberExVo();
        MemberBo memberBo = iMemberBiz.selectById(memberId);
        BeanUtils.copyProperties(memberBo, memberInfoVo);
        //邮箱，头像，名称等需要按照登录方式分开查询
        if(LoginTypeEnum.GOOGLE.equals(type) || LoginTypeEnum.FACEBOOK.equals(type)){
            //查出第三方账号信息
            MemberThirdpartAccountBo thirdpartAccountBo = memberThirdpartAccountBiz.selectOne(Condition.create()
                                                    .eq(MemberThirdpartAccountBo.Key.memberId.toString(), memberId)
                                                    .eq(MemberThirdpartAccountBo.Key.accountType.toString(), type.getKey()));
            if(thirdpartAccountBo == null){
                throw new WakaException(RavvExceptionEnum.SELECT_ERROR + "this account is not exits");
            }
            memberInfoVo.setEmail(Tools.string.isEmpty(memberBo.getEmail()) ? thirdpartAccountBo.getEmail() :
                    memberBo.getEmail());
            memberInfoVo.setAvator(Tools.string.isEmpty(memberBo.getAvator()) ? thirdpartAccountBo.getAvator() :
                    memberBo.getAvator());
            memberInfoVo.setFirstname(Tools.string.isEmpty(memberBo.getFirstname()) ? thirdpartAccountBo.getFirstname() :
                    memberBo.getFirstname());
            memberInfoVo.setLastname(Tools.string.isEmpty(memberBo.getLastname()) ? thirdpartAccountBo.getLastname() :
                    memberBo.getLastname());
        } else{
            if(memberInfoVo.getEmail() == null){
                //从pam表里查出email
                PamMemberBo pamMemberBo = iPamMemberBiz.selectOne(Condition.create()
                        .eq(PamMemberBo.Key.memberId.toString(), memberId));
                if(pamMemberBo == null)
                    throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR + "该用户不存在");
                memberInfoVo.setEmail(pamMemberBo.getEmailAccount());
            }
            if(memberInfoVo.getAvator() != null)
                memberInfoVo.setAvator(QiniuUtil.getFullPath(memberInfoVo.getAvator()));
        }
        if(memberBo.getBirthday() == null)
            memberInfoVo.setBirth(false);
        else
            memberInfoVo.setBirth(true);

        //查出收藏，关注，最近浏览量
        int favoriteCount = 0;
        favoriteCount = iGoodsFavoriteBiz.selectCount(Condition.create().eq(GoodsFavoriteBo.Key.memberId.toString(), memberId));
        if(favoriteCount != 0)
            memberInfoVo.setFavoriteCount(favoriteCount);
        int viewCount = 0;
        viewCount = iGoodsViewLogBiz.selectCount(Condition.create().eq(GoodsViewLogBo.Key.memberId.toString(), memberId));
        if(viewCount != 0)
            memberInfoVo.setViewCount(viewCount);
        int followingCount = 0;
        followingCount = iYoutubeService.followedUnfrozenNumByFans(memberId);
        if(followingCount != 0)
            memberInfoVo.setFollowingCount(followingCount);
        //查询是否是主播
        if(iYoutubeService.isAnchorRegister(memberId)){
            memberInfoVo.setAnchor(true);
            //是主播，查询主播信息，查询是否冻结
            YoutubeLiveAnchorBo anchorBo = iYoutubeLiveAnchorBiz.selectOne(Condition.create()
                    .eq(YoutubeLiveAnchorBo.Key.anchorMemberId.toString(), memberId));
            if(anchorBo == null){
                throw new WakaException(RavvExceptionEnum.SELECT_ERROR + "主播不存在");
            }
            memberInfoVo.setAnchorInfo(anchorBo);
            if(iYoutubeService.isAnchorFrozen(memberId)){
                memberInfoVo.setFrozen(true);
            }
            else{
                memberInfoVo.setFrozen(false);
            }
        }
        else {
            memberInfoVo.setAnchor(false);
        }

        return memberInfoVo;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public MemberExVo addBasicInfo(Long memberId, MemberBo memberInfo, String loginType) {
        //如果有图片，拆出路径
        if(memberInfo.getAvator() != null)
            memberInfo.setAvator(QiniuUtil.getRelativePath(memberInfo.getAvator()));
        log.info("{}",memberInfo.getFirstname());
        Date now = new Date();
        memberInfo.setGmtModified(now);
        EntityWrapper<MemberBo> queryMember = new EntityWrapper<>();
        queryMember.eq(MemberBo.Key.id.toString(), memberId);
        if(!iMemberBiz.update(memberInfo, queryMember)){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return getBasicInfo(memberId, loginType);
    }

}
