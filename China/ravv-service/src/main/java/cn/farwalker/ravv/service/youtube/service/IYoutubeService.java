package cn.farwalker.ravv.service.youtube.service;

import java.util.Date;
import java.util.List;

import cn.farwalker.ravv.service.youtube.liveanchor.model.YoutubeLiveAnchorVo;
import cn.farwalker.ravv.service.youtube.livegoods.model.AddGoodsToVideoForm;
import cn.farwalker.ravv.service.youtube.livevideo.model.YoutubeLiveVideoVo;
import cn.farwalker.ravv.service.youtube.livevideo.model.YoutubeRelatedGoodsVo;
import cn.farwalker.waka.core.JsonResult;

/**
 * Created by asus on 2019/1/3.
 */
public interface IYoutubeService {
    /**
     * 主播关联channeld
     * @param memberId
     * @param channelId
     * @return
     */
    JsonResult<String>  addChannelId(Long memberId,String channelId);

    /**
     * 视频关联商品
     * @param memberId
     * @param addGoodsToVideoForm
     * @return
     */
    JsonResult<String> addGoodsToVideo(Long memberId, AddGoodsToVideoForm addGoodsToVideoForm);

    /**
     * 获取主播的视频列表
     * @param currentPage
     * @param pageSize
     * @param anchorId
     * @param videoType 有三总类型；upcoming 代表未来的视频，completed 代表已经直播过得视频，live 代表正在直播的视频。必须是小写。传空或传错，则返回改主播名下的所有视频
     * @return
     */
    JsonResult<List<YoutubeLiveVideoVo>> getAnchorLiveVideoByType(Integer currentPage, Integer pageSize, Long anchorId, String videoType);

    /**
     * 获取组播所有视频关联的商品
     * @param memberId
     * @param currentPage
     * @param pageSize
     * @return
     */
    JsonResult<List<YoutubeRelatedGoodsVo>>  getGoodsRelatedLiveAll(Long memberId, Integer currentPage, Integer pageSize);

    /**
     * 获取主播近期视频关联的商品
     * @param memberId
     * @return
     */
    JsonResult<List<YoutubeRelatedGoodsVo>>  getGoodsRelatedLiveNew(Long memberId);

    /**
     * 首页获取视频
     * @param currentPage
     * @param pageSize
     * @param type
     * @return
     */
    JsonResult<List<YoutubeLiveVideoVo>> getAllVideoByType(Integer currentPage,Integer pageSize,String type);

    /**
     * web端获取直播首页，只获取正在直播的和已经直播的视频
     * @param currentPage
     * @param pageSize
     * @return
     */
    JsonResult<List<YoutubeLiveVideoVo>> getWebMainPage(Integer displayNum);

    /**
     * 关注和取消关注
     * @param fansMemberId
     * @param anchorMemberId
     * @param unFollow
     * @return
     */
    JsonResult<String> updateFollow(Long fansMemberId,long anchorMemberId,boolean unFollow);

    /**
     * 获得推荐主播
     * @param userMemberId
     * @param currentPage
     * @param pageSize
     * @param videoNum
     * @return
     */
    JsonResult<List<YoutubeLiveAnchorVo>> getSuggestedStreamers(Long userMemberId,Integer currentPage,Integer pageSize,Integer videoNum);

    /**
     * 获得某个主播的信息
     * @param userMemberId
     * @param anchorMemberId
     * @return
     */
    JsonResult<YoutubeLiveAnchorVo> getOneStreamer(Long userMemberId,Long anchorMemberId);

    /**
     * 组播是否冻结
     * @param anchorMemberId
     * @return
     */
    boolean isAnchorFrozen(Long anchorMemberId);

    /**
     * 主播是否注册
     * @param anchorMemberId
     * @return
     */
    boolean isAnchorRegister(Long anchorMemberId);

    JsonResult<YoutubeLiveVideoVo> videoPlayInfo(Long memberId,Long videoId);


    
	/**
	 * 统计主播粉丝数量
	 * @param anchorMemberId 主播id
	 * @return
	 */
	Integer liveFollowNumByAnchor(Long anchorMemberId);
    /**
     * 每个粉丝关注了多少个主播
     * @param fansMemberId 粉丝id
     * @return
     */
    public Integer followedNumByFans(Long fansMemberId);


    /**
     * 每个粉丝关注了多少个主播,取出冻结的主播
     * @param fansMemberId
     * @return
     */
    public Integer followedUnfrozenNumByFans(Long fansMemberId);
    
    /**
	 * 统计主播某月直播时长
	 * @param anchorMemberId 主播会员id
	 * @param month 查看的月份
	 * @return
	 */
	Integer getMonthLiveDuration(Long anchorMemberId, Date month);

    List<YoutubeLiveAnchorVo> getAllFollow(Long memberId, int currentPage, int pageSize);
}
