package cn.farwalker.ravv.youtube;

import cn.farwalker.ravv.common.constants.YoutubeVideoTypeEnum;
import cn.farwalker.ravv.service.youtube.liveanchor.model.YoutubeLiveAnchorVo;
import cn.farwalker.ravv.service.youtube.livevideo.model.YoutubeLiveVideoVo;
import cn.farwalker.ravv.service.youtube.livevideo.model.YoutubeRelatedGoodsVo;
import cn.farwalker.ravv.service.youtube.service.IYoutubeService;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by asus on 2019/1/3.
 */
@Slf4j
@RestController
@RequestMapping("/youtube_live/main_page")
public class MainPageController {
    @Autowired
    private IYoutubeService iYoutubeService;

//    //获取首页
//    @RequestMapping("/get_home_page")
//    public JsonResult<> getHomePage(){
//
//    }
//
    //直播首页获取正在直播
    @RequestMapping("get_live_current")
    public JsonResult<List<YoutubeLiveVideoVo>> getLiveCurrent(Integer currentPage,Integer pageSize){
        try {
            currentPage++;
            if(currentPage < 0||pageSize < 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return iYoutubeService.getAllVideoByType(currentPage,pageSize, YoutubeVideoTypeEnum.LIVE.getKey());
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    //直播首页获取未来直播
    @RequestMapping("get_live_preview")
    public JsonResult<List<YoutubeLiveVideoVo>> getLivePreview(Integer currentPage,Integer pageSize){
        try {
            currentPage++;
            if(currentPage < 0||pageSize < 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return iYoutubeService.getAllVideoByType(currentPage,pageSize,YoutubeVideoTypeEnum.UPCOMING.getKey());
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    //直播首页获取直播回放
    @RequestMapping("get_live_replay")
    public JsonResult<List<YoutubeLiveVideoVo>> getLiveReplay(Integer currentPage,Integer pageSize){
        try {
            currentPage++;
            if(currentPage < 0||pageSize < 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return iYoutubeService.getAllVideoByType(currentPage,pageSize,YoutubeVideoTypeEnum.COMPLETED.getKey());
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    //直播首页获取直播回放
    @RequestMapping("get_web_main_page")
    public JsonResult<List<YoutubeLiveVideoVo>> getWebMainPage(Integer displayNum){
        try {
            if(displayNum == null||displayNum < 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return iYoutubeService.getWebMainPage(displayNum);
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }
    }



    //获取推荐的主播，,可登录也可不登录
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

    //获取某个主播的信息,可登录也可不登录
    @RequestMapping("get_one_streamer")
    public JsonResult<YoutubeLiveAnchorVo> getOneStreamer(HttpSession httpSession, Long anchorId){
        try {
            Long memberId = 0L;
            if (httpSession.getAttribute("memberId") != null) {
                memberId = (Long) httpSession.getAttribute("memberId");
            }
            if(anchorId == null||anchorId == 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return iYoutubeService.getOneStreamer(memberId,anchorId);
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }
    }


    //关注主播,或取消关注,一定要登录
    @RequestMapping("follow")
    public JsonResult<String> updateFollow(HttpSession httpSession,Long anchorMemberId,boolean unFollow){
        try {
            Long memberId;
            if ((memberId = (Long) httpSession.getAttribute("memberId")) == 0) {
                throw new WakaException(RavvExceptionEnum.TOKEN_PARSE_MEMBER_ID_FAILED);
            }
            if (anchorMemberId == 0){
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            }
            return iYoutubeService.updateFollow(memberId,anchorMemberId,unFollow);
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }


    }

    /*****************************************用户获取单个主播的信息****************************************/
    //用户获取主播的直播视频,包括历史视频，正在直播的视频，和未来的视频
    @RequestMapping("get_live_video")
    public JsonResult<List<YoutubeLiveVideoVo>> getLiveVideo(Integer currentPage,Integer pageSize,Long anchorId,String videoType){
        try {
            currentPage++;
            if(anchorId == null || anchorId == 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);

            return iYoutubeService.getAnchorLiveVideoByType(currentPage,pageSize,anchorId,videoType);
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }

    }
    //获取主播直播视频相关联的商品,新品
    @RequestMapping("get_goods_related_live_new")
    public JsonResult<List<YoutubeRelatedGoodsVo>> getGoodsRelatedLiveNew(Long anchorId){
        try {

            if (anchorId == 0) {
                throw new WakaException(RavvExceptionEnum.TOKEN_PARSE_MEMBER_ID_FAILED);
            }
            return iYoutubeService.getGoodsRelatedLiveNew(anchorId);
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }

    }

    //获取主播直播视频相关联的商品,全部商品
    @RequestMapping("get_goods_related_live_all")
    public JsonResult<List<YoutubeRelatedGoodsVo>> getGoodsRelatedLiveAll(Long anchorId, Integer currentPage, Integer pageSize){
        try {
            currentPage++;
            if (anchorId == 0) {
                throw new WakaException(RavvExceptionEnum.TOKEN_PARSE_MEMBER_ID_FAILED);
            }

            if(currentPage < 0||pageSize < 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return iYoutubeService.getGoodsRelatedLiveAll(anchorId,currentPage,pageSize);
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }

    }

    //点击播放某个视频
    @RequestMapping("video_play_info")
    public JsonResult<YoutubeLiveVideoVo> videoPlayInfo(HttpSession httpSession, Long videoId){
        try {
            if (videoId == 0) {
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            }
            Long memberId = 0L;
            if (httpSession.getAttribute("memberId") != null) {
                memberId = (Long) httpSession.getAttribute("memberId");
            }
            return iYoutubeService.videoPlayInfo(memberId,videoId);
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }

    }



}
