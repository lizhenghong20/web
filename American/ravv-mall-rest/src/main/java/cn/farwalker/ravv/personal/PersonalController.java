package cn.farwalker.ravv.personal;

import cn.farwalker.ravv.service.goodsext.comment.biz.IGoodsCommentService;
import cn.farwalker.ravv.service.goodsext.comment.model.CommentOrderVo;
import cn.farwalker.ravv.service.goodsext.consult.biz.IGoodsConsultService;
import cn.farwalker.ravv.service.goodsext.consult.model.GoodsAnswerVo;
import cn.farwalker.ravv.service.goodsext.consult.model.GoodsQuestionVo;
import cn.farwalker.ravv.service.goodsext.favorite.biz.IGoodsFavoriteService;
import cn.farwalker.ravv.service.goodsext.favorite.model.GoodsFavoriteVo;
import cn.farwalker.ravv.service.goodsext.viewlog.biz.IGoodsViewLogService;
import cn.farwalker.ravv.service.goodsext.viewlog.model.GoodsViewLogVo;
import cn.farwalker.ravv.service.member.basememeber.biz.IMemberService;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.member.basememeber.model.MemberExVo;
import cn.farwalker.ravv.service.member.pam.constants.LoginTypeEnum;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.youtube.liveanchor.model.YoutubeLiveAnchorVo;
import cn.farwalker.ravv.service.youtube.service.IYoutubeService;
import cn.farwalker.waka.core.JsonResult;

import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/personal")
@Slf4j
public class PersonalController {

    @Autowired
    private IGoodsViewLogService iGoodsViewLogService;

    @Autowired
    private IGoodsFavoriteService iGoodsFavoriteService;

    @Autowired
    private IGoodsConsultService iGoodsConsultService;

    @Autowired
    private IGoodsCommentService iGoodsCommentService;

    @Autowired
    private IMemberService iMemberService;

    @Autowired
    private IYoutubeService iYoutubeService;

    /**
     * @description: 获取用户基础信息
     * @param: memberId
     * @return memberBo
     * @author Mr.Simple
     * @date 2019/1/7 18:11
     */
    @RequestMapping("/basic_info")
    public JsonResult<MemberExVo> getBasicInfo(HttpSession session, String loginType) {
        try {
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iMemberService.getBasicInfo(memberId, loginType ));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("/add_person_info")
    public JsonResult<MemberExVo> addPersonInfo(HttpSession session, MemberBo memberInfo, String loginType) {
        try {
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            if(memberInfo == null)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return JsonResult.newSuccess(iMemberService.addBasicInfo(memberId, memberInfo, loginType));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("/youtube_follow")
    public JsonResult<List<YoutubeLiveAnchorVo>> youtubeFollow(HttpSession session, int currentPage, int pageSize) {
        try {
            currentPage++;
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iYoutubeService.getAllFollow(memberId, currentPage, pageSize));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }

    }

    /**
     * @description: 获取推荐主播
     * @param:
     * @return
     * @author Mr.Simple
     * @date 2019/1/23 10:55
     */
    @RequestMapping("get_suggested_streamers")
    public JsonResult<List<YoutubeLiveAnchorVo>> getSuggestedStreamers(HttpSession httpSession, Integer currentPage,Integer pageSize,Integer videoNum){
        try {
            currentPage++;
            Long memberId = 0L;
            if (httpSession.getAttribute("memberId") != null) {
                memberId = (Long) httpSession.getAttribute("memberId");
            }
            if(currentPage < 0||pageSize < 0||videoNum < 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return iYoutubeService.getSuggestedStreamers(memberId,currentPage,pageSize,videoNum);
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    /**
     * @description: 查看浏览记录
     * @param: memberId
     * @return list
     * @author Mr.Simple
     * @date 2018/12/25 17:23
     */
    @RequestMapping("/view_log")
    public JsonResult<List<GoodsViewLogVo>> viewLog(HttpSession session, Integer currentPage, Integer pageSize) {
        try {
            currentPage++;
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iGoodsViewLogService.updateAndGetAllViewLog(memberId, currentPage, pageSize));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }

    }

    /**
     * @description: 查看收藏列表
     * @param: memberId
     * @return list
     * @author Mr.Simple
     * @date 2018/12/25 17:23
     */
    @RequestMapping("/favorite_list")
    public JsonResult<List<GoodsFavoriteVo>> favorite(HttpSession session,
                                                      @RequestParam(value = "isReduction", required = false, defaultValue = "false") boolean isReduction) {
        try {
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iGoodsFavoriteService.getAllFavorite(memberId, isReduction));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }

    }


    @RequestMapping("/my_question")
    public JsonResult<List<GoodsQuestionVo>> myQuestion(HttpSession session) {
        try {
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iGoodsConsultService.myQuestion(memberId));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }

    }

    @RequestMapping("/my_answer")
    public JsonResult<List<GoodsAnswerVo>> myAnswer(HttpSession session) {
        try {
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iGoodsConsultService.myAnswer(memberId));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }

    }

    /**
     * @description: 查找出所有已评价订单
     * @param: memberId
     * @return json
     * @author Mr.Simple
     * @date 2018/12/28 14:32
     */
    @RequestMapping("/published_reviews")
    public JsonResult<List<CommentOrderVo>> publishedReviews(HttpSession session, int currentPage, int pageSize) {
        try {
            currentPage++;
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iGoodsCommentService.publishedReviews(memberId, currentPage, pageSize));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }

    }

    /**
     * @description: 查找出所有未评价订单
     * @param: memberId
     * @return json
     * @author Mr.Simple
     * @date 2018/12/28 14:32
     */
    @RequestMapping("/awaiting_reviews")
    public JsonResult<List<OrderGoodsBo>> awaitingReviews(HttpSession session, int currentPage, int pageSize) {
        try {
            currentPage++;
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iGoodsCommentService.awaitingReviews(memberId, currentPage, pageSize));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }

    }

}
