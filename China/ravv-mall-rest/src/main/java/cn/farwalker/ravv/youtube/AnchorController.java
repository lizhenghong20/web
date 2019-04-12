package cn.farwalker.ravv.youtube;

import cn.farwalker.ravv.service.youtube.liveanchor.biz.IYoutubeLiveAnchorBiz;
import cn.farwalker.ravv.service.youtube.liveanchor.model.YoutubeLiveAnchorBo;
import cn.farwalker.ravv.service.youtube.liveanchor.model.YoutubeLiveAnchorVo;
import cn.farwalker.ravv.service.youtube.livegoods.model.AddGoodsToVideoForm;
import cn.farwalker.ravv.service.youtube.livevideo.model.YoutubeLiveVideoVo;
import cn.farwalker.ravv.service.youtube.livevideo.model.YoutubeRelatedGoodsVo;
import cn.farwalker.ravv.service.youtube.service.IYoutubeService;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.Condition;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Created by asus on 2019/1/3.
 *主播行为，包括注册，将名下YouTube 的视屏关联到平台商品,
 */
@Slf4j
@RestController
@RequestMapping("/youtube_live/anchor")
public class AnchorController {

    @Autowired
    private IYoutubeService iYoutubeService;

    @Autowired
    private IYoutubeLiveAnchorBiz iYoutubeLiveAnchorBiz;

    //主播注册
    @RequestMapping("/add_channel_id")
    public JsonResult<String> addChannelId(HttpSession httpSession,String channelId) {
        try {
            Long memberId;
            if ((memberId = (Long) httpSession.getAttribute("memberId")) == 0) {
                throw new WakaException(RavvExceptionEnum.TOKEN_PARSE_MEMBER_ID_FAILED);
            }
            if ( Tools.string.isEmpty(channelId)) {
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            }
            return iYoutubeService.addChannelId(memberId,channelId);
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    //主播获取名下youtube视频
    @RequestMapping("/get_youtube_video")
    public JsonResult<List<SearchResult>> getYoutubeVideo(HttpSession httpSession,
                                                          @RequestParam(value = "queryItem",required = false) String queryItem,
                                                          @RequestParam(value = "channelId",required = false)String channelId,
                                                          @RequestParam(value = "order",required = false,defaultValue = "date")String order,
                                                          @RequestParam(value = "videoDefinition",required = false,defaultValue = "any")String videoDefinition,
                                                          @RequestParam(value = "videoEmbeddable",required = false,defaultValue = "any")String videoEmbeddable
                                                          ){
        try {
            Long anchorId;
            if ((anchorId = (Long) httpSession.getAttribute("memberId")) == 0) {
                throw new WakaException(RavvExceptionEnum.TOKEN_PARSE_MEMBER_ID_FAILED);
            }
            YoutubeLiveAnchorBo queryAnchor =   iYoutubeLiveAnchorBiz.selectOne(Condition.create().eq(YoutubeLiveAnchorBo.Key.anchorMemberId.toString(),anchorId));
            if(queryAnchor == null|| Tools.string.isEmpty(queryAnchor.getYoutubeChannelId())){
               throw new WakaException(RavvExceptionEnum.ANCHOR_NOT_REGISTER);
            }

            if(iYoutubeService.isAnchorFrozen(anchorId))
                throw new WakaException(RavvExceptionEnum.ANCHOR_IS_FROZEN);

            Properties properties = new Properties();
            InputStream in = AnchorController.class.getResourceAsStream("/youtube.properties");
            properties.load(in);
            // This object is used to make YouTube Data API requests. The last
            // argument is required, but since we don't need anything
            // initialized when the HttpRequest is initialized, we override
            // the interface and provide a no-op function.
            YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("youtube-cmdline-search-sample").build();

            // Define the API request for retrieving search results.
            YouTube.Search.List search = youtube.search().list("id,snippet");

            // Set your developer key from the {{ Google Cloud Console }} for
            // non-authenticated requests. See:
            // {{ https://cloud.google.com/console }}
            String apiKey = properties.getProperty("youtube.apikey");
            search.setKey(apiKey);
            if(queryItem != null)
                search.setQ(queryItem);

            // Restrict the search results to only include videos. See:
            // https://developers.google.com/youtube/v3/docs/search/list#type
            search.setType("video");
            if(channelId == null)
                search.setChannelId(queryAnchor.getYoutubeChannelId());
            else
                search.setChannelId(channelId);

//            search.setEventType(form.getEventType());
//            search.setVideoEmbeddable(form.getVideoEmbeddable());
            search.setPart("snippet");

            //            可接受的值是：
//            date - 资源根据创建日期按反向时间顺序排序。
//            rating - 资源从最高评级到最低评级。
//            relevance - 资源根据其与搜索查询的相关性进行排序。这是此参数的默认值。
//            title - 资源按标题按字母顺序排序。
//            videoCount - 频道按其上传视频数量的降序排序。
//            viewCount - 资源按从最高到最低的视图数排序。对于直播，视频在广播正在进行时按照并发观众的数量进行排序。

            search.setOrder(order);

            // To increase efficiency, only retrieve the fields that the
            // application uses.
//            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails)");
            search.setMaxResults(50L);

//            该videoDefinition参数允许您将搜索限制为仅包括高清（HD）或标准清晰（SD）视频。
//            高清视频可以播放至少720p，但也可以使用更高的分辨率，如1080p。
//            如果为此参数指定值，则还必须将type参数的值设置为video。
//            可接受的值是：
//            any - 返回所有视频，无论其分辨率如何。
//            high - 仅检索高清视频。
//            standard - 仅检索标准清晰度的视频。
            search.setVideoDefinition(videoDefinition);
//            通过该videoEmbeddable参数，您可以将搜索范围限制为仅可嵌入网页的视频。
//            如果为此参数指定值，则还必须将type参数的值设置为video。
//            可接受的值是：
//            any - 返回所有视频，可嵌入或不嵌入。
//            true - 仅检索可嵌入的视频。
            search.setVideoEmbeddable(videoEmbeddable);


            // Call the API and print results.
            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            return JsonResult.newSuccess(searchResultList);
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }

    }


    //主播关联名下的YouTube视频到商品
    @RequestMapping("add_goods_to_video")
    public JsonResult<String> addGoodsToVideo(HttpSession httpSession, AddGoodsToVideoForm addGoodsToVideoForm){
        try {
            Long anchorId;
            if ((anchorId = (Long) httpSession.getAttribute("memberId")) == 0) {
                throw new WakaException(RavvExceptionEnum.TOKEN_PARSE_MEMBER_ID_FAILED);
            }
            if (addGoodsToVideoForm==null
                    ||Tools.string.isEmpty(addGoodsToVideoForm.getYoutubeVideoId())
                    ||Tools.string.isEmpty(addGoodsToVideoForm.getGoodIds())){
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            }
            return iYoutubeService.addGoodsToVideo(anchorId,addGoodsToVideoForm);
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }

    }

    /*****************************************主播获取自己的信息****************************************/
    //主播获取自己的直播视频,包括历史视频，正在直播的视频，和未来的视频
    @RequestMapping("get_live_video_myself")
    public JsonResult<List<YoutubeLiveVideoVo>> getLiveVideo(HttpSession httpSession,Integer currentPage,Integer pageSize,String videoType){
        try {
            currentPage++;
            Long anchorId;
            if (httpSession.getAttribute("memberId") == null) {
                throw new WakaException(RavvExceptionEnum.TOKEN_PARSE_MEMBER_ID_FAILED);
            }
            anchorId = (Long) httpSession.getAttribute("memberId");
            if(anchorId == 0)
                throw new WakaException(RavvExceptionEnum.TOKEN_PARSE_MEMBER_ID_FAILED);
            if(!iYoutubeService.isAnchorRegister(anchorId))
                throw new WakaException(RavvExceptionEnum.ANCHOR_NOT_REGISTER);

            return iYoutubeService.getAnchorLiveVideoByType(currentPage,pageSize,anchorId,videoType);
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }

    }
    //主播获取自己的直播视频相关联的商品,新品
    @RequestMapping("get_goods_related_live_new_myself")
    public JsonResult<List<YoutubeRelatedGoodsVo>> getGoodsRelatedLiveNew(HttpSession httpSession){
        try {
            Long anchorId;
            if (httpSession.getAttribute("memberId") == null) {
                throw new WakaException(RavvExceptionEnum.TOKEN_PARSE_MEMBER_ID_FAILED);
            }
            anchorId = (Long) httpSession.getAttribute("memberId");
            if(anchorId == 0)
                throw new WakaException(RavvExceptionEnum.TOKEN_PARSE_MEMBER_ID_FAILED);
            if(!iYoutubeService.isAnchorRegister(anchorId))
                throw new WakaException(RavvExceptionEnum.ANCHOR_NOT_REGISTER);
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

    //主播获取自己的直播视频相关联的商品,全部商品
    @RequestMapping("get_goods_related_live_all_myself")
    public JsonResult<List<YoutubeRelatedGoodsVo>> getGoodsRelatedLiveAll(HttpSession httpSession,Integer currentPage, Integer pageSize){
        try {
            currentPage++;
            Long anchorId;
            if (httpSession.getAttribute("memberId") == null) {
                throw new WakaException(RavvExceptionEnum.TOKEN_PARSE_MEMBER_ID_FAILED);
            }
            anchorId = (Long) httpSession.getAttribute("memberId");
            if(anchorId == 0)
                throw new WakaException(RavvExceptionEnum.TOKEN_PARSE_MEMBER_ID_FAILED);
            if(!iYoutubeService.isAnchorRegister(anchorId))
                throw new WakaException(RavvExceptionEnum.ANCHOR_NOT_REGISTER);
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

    //主播获取自己的信息
    @RequestMapping("get_my_info")
    public JsonResult<YoutubeLiveAnchorVo> getMyInfo(HttpSession httpSession){
        try {
            Long anchorId;
            if (httpSession.getAttribute("memberId") == null) {
                throw new WakaException(RavvExceptionEnum.TOKEN_PARSE_MEMBER_ID_FAILED);
            }
            anchorId = (Long) httpSession.getAttribute("memberId");
            if(anchorId == 0)
                throw new WakaException(RavvExceptionEnum.TOKEN_PARSE_MEMBER_ID_FAILED);
            if(!iYoutubeService.isAnchorRegister(anchorId))
                throw new WakaException(RavvExceptionEnum.ANCHOR_NOT_REGISTER);
            if (anchorId == 0) {
                throw new WakaException(RavvExceptionEnum.TOKEN_PARSE_MEMBER_ID_FAILED);
            }

            return iYoutubeService.getOneStreamer(anchorId,anchorId);
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }
    }
//
//    //获取主播的首页
//    @RequestMapping("/get_home_page")
//    public JsonResult<> getHomePage(){
//
//    }
//
//
//











}
