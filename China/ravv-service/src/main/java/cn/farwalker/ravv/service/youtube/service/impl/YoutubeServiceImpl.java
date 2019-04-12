package cn.farwalker.ravv.service.youtube.service.impl;

import cn.farwalker.ravv.common.constants.YoutubeVideoTypeEnum;
import cn.farwalker.ravv.service.youtube.liveanchor.biz.IYoutubeLiveAnchorBiz;
import cn.farwalker.ravv.service.youtube.liveanchor.dao.IYoutubeLiveAnchorDao;
import cn.farwalker.ravv.service.youtube.liveanchor.model.YoutubeLiveAnchorBo;
import cn.farwalker.ravv.service.youtube.liveanchor.model.YoutubeLiveAnchorVo;
import cn.farwalker.ravv.service.youtube.livefollow.biz.IYoutubeLiveFollowBiz;
import cn.farwalker.ravv.service.youtube.livefollow.model.YoutubeLiveFollowBo;
import cn.farwalker.ravv.service.youtube.livegoods.biz.IYoutubeLiveGoodsBiz;
import cn.farwalker.ravv.service.youtube.livegoods.dao.IYoutubeLiveGoodsDao;
import cn.farwalker.ravv.service.youtube.livegoods.model.AddGoodsToVideoForm;
import cn.farwalker.ravv.service.youtube.livegoods.model.YoutubeLiveGoodsBo;
import cn.farwalker.ravv.service.youtube.livevideo.biz.IYoutubeLiveVideoBiz;
import cn.farwalker.ravv.service.youtube.livevideo.dao.IYoutubeLiveVideoDao;
import cn.farwalker.ravv.service.youtube.livevideo.model.YoutubeLiveVideoBo;
import cn.farwalker.ravv.service.youtube.livevideo.model.YoutubeLiveVideoVo;
import cn.farwalker.ravv.service.youtube.livevideo.model.YoutubeRelatedGoodsVo;
import cn.farwalker.ravv.service.youtube.service.IYoutubeService;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * Created by asus on 2019/1/3.
 */
@Service
public class YoutubeServiceImpl  implements IYoutubeService{
    @Autowired
    private IYoutubeLiveFollowBiz iYoutubeLiveFollowBiz;

    @Autowired
    private IYoutubeLiveGoodsBiz iYoutubeLiveGoodsBiz;

    @Autowired
    private IYoutubeLiveVideoBiz iYoutubeLiveVideoBiz;

    @Autowired
    private IYoutubeLiveAnchorBiz iYoutubeLiveAnchorBiz;

    @Autowired
    private IYoutubeLiveGoodsDao iYoutubeLiveGoodsDao;

    @Autowired
    private IYoutubeLiveVideoDao iYoutubeLiveVideoDao;

    @Autowired
    private IYoutubeLiveAnchorDao iYoutubeLiveAnchorDao;
    
    @Resource
    private IYoutubeLiveFollowBiz youtubeLiveFollowBiz;
    
    @Resource
    private IYoutubeLiveVideoBiz youtubeLiveVideoBiz;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public JsonResult<String> addChannelId(Long memberId,String channelId){
        YoutubeLiveAnchorBo query =   iYoutubeLiveAnchorBiz.selectOne(Condition.create().eq(YoutubeLiveAnchorBo.Key.anchorMemberId.toString(),memberId));
        if(query == null){
            YoutubeLiveAnchorBo insert = new YoutubeLiveAnchorBo();
            insert.setAnchorMemberId(memberId);
            insert.setYoutubeChannelId(channelId);
            //使冻结时间非空
            insert.setFrozenStart(Tools.date.parseDate("1900/01/01 00:00:00").getTime());
            insert.setFrozenEnd(Tools.date.parseDate("1900/01/01 00:00:00").getTime());
            if(!iYoutubeLiveAnchorBiz.insert(insert))
                throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
            return JsonResult.newSuccess("youtube channelId has been bound to your account");
        }else{
            if(isAnchorFrozen(query.getAnchorMemberId())){
                return JsonResult.newSuccess("your account is frozen");
            }else{
                query.setAnchorMemberId(memberId);
                query.setYoutubeChannelId(channelId);
                //使冻结时间非空
                query.setFrozenStart(Tools.date.parseDate("1900/01/01 00:00:00").getTime());
                query.setFrozenEnd(Tools.date.parseDate("1900/01/01 00:00:00").getTime());
                if(!iYoutubeLiveAnchorBiz.updateById(query))
                    throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
                return JsonResult.newSuccess("your account has been updated");
            }
        }

    }

    //判断主播有没有冻结
    @Override
    public boolean isAnchorFrozen(Long anchorMemberId){
        YoutubeLiveAnchorBo query =   iYoutubeLiveAnchorBiz.selectOne(Condition.create().eq(YoutubeLiveAnchorBo.Key.anchorMemberId.toString(),anchorMemberId));
        if(query == null)
            return false;
        Date currentTime = new Date();
        if(query.getFrozenEnd()==null||query.getFrozenStart()==null)
            return false;
        if(query.getFrozenStart().getTime() <= currentTime.getTime()&&query.getFrozenEnd().getTime() >= currentTime.getTime())
            return true;
        return false;

    }
    //判断组播是否注册
    @Override
    public boolean isAnchorRegister(Long memberId){
        YoutubeLiveAnchorBo queryAnchor =   iYoutubeLiveAnchorBiz.selectOne(Condition.create().eq(YoutubeLiveAnchorBo.Key.anchorMemberId.toString(),memberId));
        return queryAnchor == null?false:true;
    }


    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public JsonResult<String> addGoodsToVideo(Long anchorMemberId, AddGoodsToVideoForm addGoodsToVideoForm){
        YoutubeLiveAnchorBo queryAnchor =   iYoutubeLiveAnchorBiz.selectOne(Condition.create().eq(YoutubeLiveAnchorBo.Key.anchorMemberId.toString(),anchorMemberId));
        if(queryAnchor == null){
           throw new WakaException(RavvExceptionEnum.ANCHOR_NOT_REGISTER);
        }
        if(isAnchorFrozen(queryAnchor.getAnchorMemberId()))
            return JsonResult.newSuccess("your account is frozen");

            YoutubeLiveVideoBo queryVideo = iYoutubeLiveVideoBiz.selectOne(Condition.create().eq(YoutubeLiveVideoBo.Key.youtubeVideoId.toString(),addGoodsToVideoForm.getYoutubeVideoId())
        .eq(YoutubeLiveVideoBo.Key.anchorMemberId.toString(),anchorMemberId));
        //如果视频链接没有被记录过，即此视频没有关联商品的历史，则插入，否则更新
        Long videoId = 0L;
        if(queryVideo==null){
            YoutubeLiveVideoBo insertVideo = new YoutubeLiveVideoBo();
            insertVideo.setTitle(addGoodsToVideoForm.getTitle());
            insertVideo.setAnchorMemberId(anchorMemberId);
            insertVideo.setVideoUrl("https://www.youtube.com/embed/"+addGoodsToVideoForm.getYoutubeVideoId());
            insertVideo.setPreviewDefaultUrl(addGoodsToVideoForm.getPreviewDefaultUrl());
            insertVideo.setPreviewMediumUrl(addGoodsToVideoForm.getPreviewMediumUrl());
            insertVideo.setPreviewHighUrl(addGoodsToVideoForm.getPreviewHighUrl());
            insertVideo.setYoutubeVideoId(addGoodsToVideoForm.getYoutubeVideoId());
            insertVideo.setPublishTime(addGoodsToVideoForm.getPublishTime());
            insertVideo.setStartTime(addGoodsToVideoForm.getStartTime());
            insertVideo.setEndTime(addGoodsToVideoForm.getEndTime());
            insertVideo.setDescription(addGoodsToVideoForm.getDescription());

            if(!iYoutubeLiveVideoBiz.insert(insertVideo)){
             throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
            }
            videoId = insertVideo.getId();


        }else{//只能更新某些信息
            queryVideo.setTitle(addGoodsToVideoForm.getTitle());
            queryVideo.setPreviewDefaultUrl(addGoodsToVideoForm.getPreviewDefaultUrl());
            queryVideo.setPreviewMediumUrl(addGoodsToVideoForm.getPreviewMediumUrl());
            queryVideo.setPreviewHighUrl(addGoodsToVideoForm.getPreviewHighUrl());
            queryVideo.setPublishTime(addGoodsToVideoForm.getPublishTime());
            queryVideo.setStartTime(addGoodsToVideoForm.getStartTime());
            queryVideo.setEndTime(addGoodsToVideoForm.getEndTime());
            queryVideo.setDescription(addGoodsToVideoForm.getDescription());
            if(!iYoutubeLiveVideoBiz.updateById(queryVideo)){
                throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
            }
            videoId = queryVideo.getId();
        }

        String goodsIds = addGoodsToVideoForm.getGoodIds();
        String [] goodsIdstr = goodsIds.split(",");
        List<Long> goodsIdList = new ArrayList<>();
        for(int i = 0; i < goodsIdstr.length; i++){
            goodsIdList.add(Long.parseLong(goodsIdstr[i]));
        }

        for(Long item : goodsIdList){
            //如果此商品已经关联此视频，跳过
            if( iYoutubeLiveGoodsBiz.selectOne(Condition.create().eq(YoutubeLiveGoodsBo.Key.goodsId.toString(),item)
                    .eq(YoutubeLiveGoodsBo.Key.videoId.toString(),videoId))!=null )
                continue;
            YoutubeLiveGoodsBo insertGoodsBo = new YoutubeLiveGoodsBo();
            insertGoodsBo.setVideoId(videoId);
            insertGoodsBo.setGoodsId(item);
            if(!iYoutubeLiveGoodsBiz.insert(insertGoodsBo))
                throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        return JsonResult.newSuccess("goods have been related to your video");
    }


    public JsonResult<List<YoutubeLiveVideoVo>> getAnchorLiveVideoByType(Integer currentPage,Integer pageSize,Long anchorMemberId, String videoType){
        YoutubeLiveAnchorBo queryAnchor = iYoutubeLiveAnchorBiz.selectOne(Condition.create().eq(YoutubeLiveAnchorBo.Key.anchorMemberId.toString(),anchorMemberId));
        if(queryAnchor == null||currentPage < 0||pageSize < 0)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        if(isAnchorFrozen(queryAnchor.getAnchorMemberId()))
            throw new WakaException(RavvExceptionEnum.ANCHOR_IS_FROZEN);

        Page<YoutubeLiveVideoBo> videoPage = new Page<>(currentPage,pageSize);
        Date currentTime = new Date();
        //查出各个时间段的视频
        if(YoutubeVideoTypeEnum.COMPLETED.getKey().equals(videoType)){
            Page<YoutubeLiveVideoBo> queryPage =  iYoutubeLiveVideoBiz.selectPage(videoPage,Condition.create().eq(YoutubeLiveVideoBo.Key.anchorMemberId.toString(),anchorMemberId)
            .lt(YoutubeLiveVideoBo.Key.endTime.toString(),currentTime));
            return JsonResult.newSuccess(addGoodsToVideoVo(createVideoVo(queryPage.getRecords())));

        }else if(YoutubeVideoTypeEnum.LIVE.getKey().equals(videoType)){
            Page<YoutubeLiveVideoBo> queryPage =  iYoutubeLiveVideoBiz.selectPage(videoPage,Condition.create().eq(YoutubeLiveVideoBo.Key.anchorMemberId.toString(),anchorMemberId)
                    .lt(YoutubeLiveVideoBo.Key.startTime.toString(),currentTime)
                    .gt(YoutubeLiveVideoBo.Key.endTime.toString(),currentTime));
            return JsonResult.newSuccess(createVideoVo(queryPage.getRecords()));

        }else if(YoutubeVideoTypeEnum.UPCOMING.getKey().equals(videoType)){
            Page<YoutubeLiveVideoBo> queryPage =  iYoutubeLiveVideoBiz.selectPage(videoPage,Condition.create().eq(YoutubeLiveVideoBo.Key.anchorMemberId.toString(),anchorMemberId)
                    .gt(YoutubeLiveVideoBo.Key.startTime.toString(),currentTime));
            return JsonResult.newSuccess(createVideoVo(queryPage.getRecords()));
        }else{
            Page<YoutubeLiveVideoBo> queryPage =  iYoutubeLiveVideoBiz.selectPage(videoPage,Condition.create().eq(YoutubeLiveVideoBo.Key.anchorMemberId.toString(),anchorMemberId));
            List<YoutubeLiveVideoVo>  resultList = addGoodsToVideoVo(createVideoVo(queryPage.getRecords()));
            //减少返回前端的字段
            resultList.forEach(item -> {
                if(!item.getVideoType().equals(YoutubeVideoTypeEnum.COMPLETED.getKey())){
                    item.setGoodsList(null);
                }
            });
            return JsonResult.newSuccess(resultList);
        }

    }

    /**
     * 为视频列表的每一项添加关联商品
     * @param videoVoList
     * @return
     */
    private List<YoutubeLiveVideoVo> addGoodsToVideoVo(List<YoutubeLiveVideoVo> videoVoList){
        if(videoVoList == null || videoVoList.size() == 0)
            return videoVoList;
        for(YoutubeLiveVideoVo item:videoVoList){
            List<YoutubeRelatedGoodsVo> goodsRelatedList =  iYoutubeLiveGoodsDao.selectGoodsByVideoIdList(Arrays.asList(item.getId()));
            for(YoutubeRelatedGoodsVo goodItem : goodsRelatedList){
                goodItem.setGoodsMajorUrl(QiniuUtil.getFullPath(goodItem.getGoodsMajorUrl()));
            }
            item.setGoodsList(goodsRelatedList);
        }
        return videoVoList;
    }







    @Override
    public JsonResult<List<YoutubeRelatedGoodsVo>>  getGoodsRelatedLiveNew(Long anchorMemberId){
        YoutubeLiveAnchorBo queryAnchor =   iYoutubeLiveAnchorBiz.selectOne(Condition.create().eq(YoutubeLiveAnchorBo.Key.anchorMemberId.toString(),anchorMemberId));
        if(queryAnchor == null|| Tools.string.isEmpty(queryAnchor.getYoutubeChannelId())){
            new WakaException(RavvExceptionEnum.ANCHOR_NOT_REGISTER);
        }
        if(isAnchorFrozen(anchorMemberId))
            throw new WakaException(RavvExceptionEnum.ANCHOR_IS_FROZEN);

        List<YoutubeRelatedGoodsVo> goodsRelatedList = new ArrayList<>();
        //取最近两次已完成的和正在进行的直播所关联的商品
        Page<YoutubeLiveVideoBo> videoPage = new Page<>(0,2);
        Date currentTime = new Date();
        Page<YoutubeLiveVideoBo> queryPage =  iYoutubeLiveVideoBiz.selectPage(videoPage,Condition.create().eq(YoutubeLiveVideoBo.Key.anchorMemberId.toString(),anchorMemberId)
                .lt(YoutubeLiveVideoBo.Key.startTime.toString(),currentTime)
                .orderBy(YoutubeLiveVideoBo.Key.startTime.toString(),false)
                .groupBy(YoutubeLiveVideoBo.Key.youtubeVideoId.toString()));

        List<YoutubeLiveVideoBo> queryVideoList = queryPage.getRecords();
        if(queryVideoList.size() == 0)
            return JsonResult.newSuccess(goodsRelatedList);

        goodsRelatedList=  iYoutubeLiveGoodsDao.selectGoodsByVideoIdList(queryVideoList.stream().map(s->s.getId()).collect(Collectors.toList()));
        for(YoutubeRelatedGoodsVo goodsItem : goodsRelatedList )
            goodsItem.setGoodsMajorUrl(QiniuUtil.getFullPath(goodsItem.getGoodsMajorUrl()));

       return JsonResult.newSuccess(goodsRelatedList);

    }

    @Override
   public JsonResult<List<YoutubeRelatedGoodsVo>>  getGoodsRelatedLiveAll(Long anchorMemberId, Integer currentPage, Integer pageSize){
       YoutubeLiveAnchorBo queryAnchor =   iYoutubeLiveAnchorBiz.selectOne(Condition.create().eq(YoutubeLiveAnchorBo.Key.anchorMemberId.toString(),anchorMemberId));
       if(queryAnchor == null|| Tools.string.isEmpty(queryAnchor.getYoutubeChannelId())){
           new WakaException(RavvExceptionEnum.ANCHOR_NOT_REGISTER);
       }
        if(isAnchorFrozen(anchorMemberId))
            throw new WakaException(RavvExceptionEnum.ANCHOR_IS_FROZEN);
       List<YoutubeRelatedGoodsVo> resultList = new ArrayList<>();
       //取所有已完成的和正在进行的直播所关联的商品
       Date currentTime = new Date();
       List<YoutubeLiveVideoBo> queryVideoList =  iYoutubeLiveVideoBiz.selectList(Condition.create().eq(YoutubeLiveVideoBo.Key.anchorMemberId.toString(),anchorMemberId)
               .lt(YoutubeLiveVideoBo.Key.startTime.toString(),currentTime)
               .orderBy(YoutubeLiveVideoBo.Key.startTime.toString(),false)
               .groupBy(YoutubeLiveVideoBo.Key.youtubeVideoId.toString()));

       if(queryVideoList.size() == 0)
           return JsonResult.newSuccess(resultList);

       Page<YoutubeRelatedGoodsVo> page = new Page<>(currentPage,pageSize);
       List<YoutubeRelatedGoodsVo> goodsRelatedList=  iYoutubeLiveGoodsDao.selectGoodsByVideoIdList(page,queryVideoList.stream().map(s->s.getId()).collect(Collectors.toList()));
       for(YoutubeRelatedGoodsVo goodsItem : goodsRelatedList )
           goodsItem.setGoodsMajorUrl(QiniuUtil.getFullPath(goodsItem.getGoodsMajorUrl()));
       return JsonResult.newSuccess(goodsRelatedList);

    }

    public JsonResult<List<YoutubeLiveVideoVo>> getAllVideoByType(Integer currentPage,Integer pageSize,String videoType){

        Page<YoutubeLiveVideoBo> videoPage = new Page<>(currentPage,pageSize);
        Date currentTime = new Date();
        List<YoutubeLiveVideoBo> videoList;
        List<YoutubeLiveVideoVo> resultList = new ArrayList<>();
        videoList = iYoutubeLiveVideoDao.selectAllVideoByType(videoPage,videoType,currentTime);
        if(videoList.size() == 0)
            return JsonResult.newSuccess(resultList);
        //如果是历史视频，则需要关联商品
        if(YoutubeVideoTypeEnum.COMPLETED.getKey().equals(videoType)){
            for(YoutubeLiveVideoBo item:videoList){
                List<YoutubeRelatedGoodsVo> goodsRelatedList =  iYoutubeLiveGoodsDao.selectGoodsByVideoIdList(Arrays.asList(item.getId()));
                for(YoutubeRelatedGoodsVo goodsItem : goodsRelatedList )
                    goodsItem.setGoodsMajorUrl(QiniuUtil.getFullPath(goodsItem.getGoodsMajorUrl()));
                YoutubeLiveVideoVo temp = new YoutubeLiveVideoVo();
                Tools.bean.copyProperties(item,temp);
                temp.setGoodsList(goodsRelatedList);
                resultList.add(temp);
            }
        }else{
            for(YoutubeLiveVideoBo item:videoList){
                YoutubeLiveVideoVo temp = new YoutubeLiveVideoVo();
                Tools.bean.copyProperties(item,temp);
                resultList.add(temp);
            }
        }

        //更新主播的信息
        for(YoutubeLiveVideoVo item:resultList){
            Page<YoutubeLiveAnchorVo> page = new Page<>(0,1);
            List<YoutubeLiveAnchorVo> anchorList =  iYoutubeLiveAnchorDao.selectAnchorList(page,currentTime,Arrays.asList(item.getAnchorMemberId()));
            if(anchorList.size() == 0){
                throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
            }
            item.setAnchor(anchorList.get(0));
            item.setVideoType(videoType);
        }
        return JsonResult.newSuccess(resultList);
    }

    @Override
    public JsonResult<List<YoutubeLiveVideoVo>> getWebMainPage(Integer displayNum){
        //web前端中直播模块的首页，需要六个展示的视屏
        final int display = displayNum;
        JsonResult<List<YoutubeLiveVideoVo>> liveResult = getAllVideoByType(1,display,YoutubeVideoTypeEnum.LIVE.getKey());
        JsonResult<List<YoutubeLiveVideoVo>> previewResult = getAllVideoByType(1,display,YoutubeVideoTypeEnum.LIVE.getKey());
        List<YoutubeLiveVideoVo> combineList = new ArrayList<>();
        if(!liveResult.isSuccess() && !previewResult.isSuccess()){
            return JsonResult.newFail("get video failed");
        }

        if(liveResult.isSuccess()){
            combineList.addAll(liveResult.getData());
        }

        if(previewResult.isSuccess()){
            combineList.addAll(previewResult.getData());
        }

        List<YoutubeLiveVideoVo> resultList = combineList.subList(0,combineList.size() < display ? combineList.size() : display);
        return JsonResult.newSuccess(resultList);

    }

    @Override
    public JsonResult<String> updateFollow(Long fansMemberId,long anchorMemberId,boolean unFollow){
        if(isAnchorFrozen(anchorMemberId))
            throw new WakaException(RavvExceptionEnum.ANCHOR_IS_FROZEN);

        YoutubeLiveFollowBo  queryFollow = iYoutubeLiveFollowBiz.selectOne(Condition.create().eq(YoutubeLiveFollowBo.Key.anchorMemberId.toString(),anchorMemberId)
                                                                .eq(YoutubeLiveFollowBo.Key.fansMemberId.toString(),fansMemberId));
        if(unFollow){
            if(queryFollow == null)
                return JsonResult.newSuccess( "already unfollowed");
            else{
                if(!iYoutubeLiveFollowBiz.deleteById(queryFollow.getId()))
                    throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
                return JsonResult.newSuccess( "unfollowed");
            }
        }else{
            if(queryFollow != null)
                return JsonResult.newSuccess( "already followed");
            else{
                YoutubeLiveFollowBo newOne = new YoutubeLiveFollowBo();
                newOne.setAnchorMemberId(anchorMemberId);
                newOne.setFansMemberId(fansMemberId);

                if(!iYoutubeLiveFollowBiz.insert(newOne))
                    throw new WakaException(RavvExceptionEnum.INSERT_ERROR);

                return JsonResult.newSuccess( "followed");
            }
        }
    }


    @Override
    public JsonResult<List<YoutubeLiveAnchorVo>> getSuggestedStreamers(Long userMemberId,Integer currentPage, Integer pageSize, Integer videoNum){
        Page<YoutubeLiveAnchorVo> page = new Page<>(currentPage,pageSize);
        Date currentTime = new Date();
        List<YoutubeLiveAnchorVo> resultList =   iYoutubeLiveAnchorDao.selectAnchorList(page,currentTime,null);
        for(YoutubeLiveAnchorVo item : resultList){
            //更新是否被关注
            item.setFollowed(isFollowed(userMemberId,item.getAnchorMemberId()));
            if(item.getHeadShotUrl()!=null)
                item.setHeadShotUrl(QiniuUtil.getFullPath(item.getHeadShotUrl()));
            item.setFansNumber(liveFollowNumByAnchor(item.getAnchorMemberId()));
            //获取主播名下的视频

            Page<YoutubeLiveVideoBo> videoPage = new Page<>(0,videoNum);
            List<YoutubeLiveVideoBo> queryVideoList =  iYoutubeLiveVideoBiz.selectPage(videoPage,Condition.create().eq(YoutubeLiveVideoBo.Key.anchorMemberId.toString(),item.getAnchorMemberId())
                    .lt(YoutubeLiveVideoBo.Key.startTime.toString(),currentTime)
                    .orderBy(YoutubeLiveVideoBo.Key.startTime.toString(),false)
                    .groupBy(YoutubeLiveVideoBo.Key.youtubeVideoId.toString())).getRecords();

            item.setVideoList(createVideoVo(queryVideoList));
        }
        return JsonResult.newSuccess(resultList);
    }

    @Override
    public JsonResult<YoutubeLiveAnchorVo> getOneStreamer(Long fansMemberId,Long anchorMemberId){
        if(isAnchorFrozen(anchorMemberId))
            throw new WakaException(RavvExceptionEnum.ANCHOR_IS_FROZEN);

        Page<YoutubeLiveAnchorVo> page = new Page<>(0,1);
        Date currentTime = new Date();
        List<YoutubeLiveAnchorVo> resultList =   iYoutubeLiveAnchorDao.selectAnchorList(page,currentTime,Arrays.asList(anchorMemberId));
        if(resultList.size()==0)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);

        YoutubeLiveAnchorVo item = resultList.get(0);
        item.setFollowed(isFollowed(fansMemberId,anchorMemberId));
        item.setFansNumber(liveFollowNumByAnchor(anchorMemberId));
        if(item.getHeadShotUrl()!=null)
            item.setHeadShotUrl(QiniuUtil.getFullPath(item.getHeadShotUrl()));
        return JsonResult.newSuccess(item);
    }

    /**
     * anchorMemberId 是否被 fansMemberId 关注
     * @param fansMemberId
     * @param anchorMemberId
     * @return
     */
    boolean isFollowed(Long fansMemberId,Long anchorMemberId){
        if(fansMemberId != 0L){
            YoutubeLiveFollowBo query = iYoutubeLiveFollowBiz.selectOne(Condition.create().eq(YoutubeLiveFollowBo.Key.fansMemberId.toString(),fansMemberId)
                    .eq(YoutubeLiveFollowBo.Key.anchorMemberId.toString(),anchorMemberId));
            if(query != null){
                return true;
            }
        }
        return false;
    }

    @Override
   public JsonResult<YoutubeLiveVideoVo> videoPlayInfo(Long memberId,Long videoId){
        YoutubeLiveVideoBo query = iYoutubeLiveVideoBiz.selectById(videoId);
        if(query == null)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        YoutubeLiveVideoVo result = new YoutubeLiveVideoVo();
        Tools.bean.copyProperties(query,result);
        //获得主播信息
        result.setAnchor(getOneStreamer(query.getAnchorMemberId(),query.getAnchorMemberId()).getData());
        //更新主播是否被关注
        result.getAnchor().setFollowed(isFollowed(memberId,query.getAnchorMemberId()));
        //获得商品信息
        List<YoutubeRelatedGoodsVo> goodsList = iYoutubeLiveGoodsDao.selectGoodsByVideoIdList(Arrays.asList(videoId));
        for(YoutubeRelatedGoodsVo item : goodsList){
            item.setGoodsMajorUrl(QiniuUtil.getFullPath(item.getGoodsMajorUrl()));
        }
        result.setGoodsList(goodsList);
        return JsonResult.newSuccess(result);
    }

	@Override
	public Integer liveFollowNumByAnchor(Long anchorMemberId) {
//        if(isAnchorFrozen(anchorMemberId))
//            throw new WakaException(RavvExceptionEnum.ANCHOR_IS_FROZEN);
		Integer liveFollowNum = 0;
		if(null == anchorMemberId) {
			return liveFollowNum;
		}
		Wrapper<YoutubeLiveFollowBo> wrapper = new EntityWrapper<>();
    	wrapper.eq(YoutubeLiveFollowBo.Key.anchorMemberId.toString(), anchorMemberId);
		liveFollowNum = youtubeLiveFollowBiz.selectCount(wrapper);
		
		return liveFollowNum;
	}

    @Override
    public Integer followedNumByFans(Long fansMemberId) {
        Integer liveFollowNum = 0;
        if(null == fansMemberId) {
            return liveFollowNum;
        }
        Wrapper<YoutubeLiveFollowBo> wrapper = new EntityWrapper<>();
        wrapper.eq(YoutubeLiveFollowBo.Key.fansMemberId.toString(), fansMemberId);
        liveFollowNum = youtubeLiveFollowBiz.selectCount(wrapper);

        return liveFollowNum;
    }

    @Override
    public Integer followedUnfrozenNumByFans(Long fansMemberId) {
        int liveFollowNum = 0;
        if(null == fansMemberId) {
            return liveFollowNum;
        }
        Wrapper<YoutubeLiveFollowBo> wrapper = new EntityWrapper<>();
        wrapper.eq(YoutubeLiveFollowBo.Key.fansMemberId.toString(), fansMemberId);
        List<YoutubeLiveFollowBo> list = youtubeLiveFollowBiz.selectList(wrapper);
        liveFollowNum =  (int)(list.size() - list.stream().filter(s->isAnchorFrozen(s.getAnchorMemberId())).count());
        return liveFollowNum;
    }
    
    @Override
	public Integer getMonthLiveDuration(Long anchorMemberId, Date month) {
//        if(isAnchorFrozen(anchorMemberId))
//            throw new WakaException(RavvExceptionEnum.ANCHOR_IS_FROZEN);
		// 获取所选月份最后一天
    	Calendar calendar = Calendar.getInstance();
		calendar.setTime(month);
		// 获取所选月最大天数
		int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		// 设置日历中月份的最大天数
		calendar.set(Calendar.DAY_OF_MONTH, lastDay);
		// 所选月份最后一天0点增加24小时
		calendar.set(Calendar.HOUR_OF_DAY, 24);
		
		// 获取所选月份，主播已播直播视频列表
		Wrapper<YoutubeLiveVideoBo> wrapper = new EntityWrapper<>();
		wrapper.le(YoutubeLiveVideoBo.Key.endTime.toString(), new Date());
		wrapper.between(YoutubeLiveVideoBo.Key.endTime.toString(), month, calendar.getTime());
		wrapper.eq(YoutubeLiveVideoBo.Key.anchorMemberId.toString(), anchorMemberId);
		
		List<YoutubeLiveVideoBo> liveVideoList = youtubeLiveVideoBiz.selectList(wrapper);
		// 计算所选月份，主播直播视频累计时长(分钟)
		Long playtimeTotal = 0L;
		if(Tools.collection.isEmpty(liveVideoList)) {
			return Integer.valueOf(playtimeTotal.toString());
		}
		
		for(YoutubeLiveVideoBo liveVideo : liveVideoList) {
			if(liveVideo.getStartTime() != null && liveVideo.getEndTime() != null){
				 playtimeTotal += (liveVideo.getEndTime().getTime() - liveVideo.getStartTime().getTime()) / 1000 / 60;
			}
		}
		
		return Integer.valueOf(playtimeTotal.toString());
	}

    @Override
    public List<YoutubeLiveAnchorVo> getAllFollow(Long memberId, int currentPage, int pageSize) {
        Page<YoutubeLiveAnchorVo> page = new Page<>(currentPage, pageSize);
        Date now = new Date();
        List<YoutubeLiveAnchorVo> anchorVoList = new ArrayList<>();
        List<YoutubeLiveFollowBo> followBoList = new ArrayList<>();
        followBoList = iYoutubeLiveFollowBiz.selectList(Condition.create()
                .eq(YoutubeLiveFollowBo.Key.fansMemberId.toString(), memberId));
        if(followBoList.size() != 0){
            //获取所有主播id，插入list
            List<Long> anchorIdList = new ArrayList<>();
            followBoList.forEach(item->{
                anchorIdList.add(item.getAnchorMemberId());
            });
            if(anchorIdList.size() == 0){
                throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
            }
            anchorVoList = iYoutubeLiveAnchorDao.selectAnchorList(page, now, anchorIdList);
            anchorVoList.forEach(item->{
                item.setFollowed(true);
                //插入粉丝数
                item.setFansNumber(liveFollowNumByAnchor(item.getAnchorMemberId()));
            });
        }
        return anchorVoList;
    }

    /**
     * 创建vo，并增加视频的videoType
     * @param videoList
     * @return
     */
    private List<YoutubeLiveVideoVo> createVideoVo(List<YoutubeLiveVideoBo> videoList){
        List<YoutubeLiveVideoVo> resultList = new ArrayList<>();
        if(videoList == null || videoList.size() == 0)
            return resultList;
        for(YoutubeLiveVideoBo item:videoList){
            YoutubeLiveVideoVo temp = new YoutubeLiveVideoVo();
            Tools.bean.copyProperties(item,temp);
            updateVideoType(temp);
            resultList.add(temp);
        }
        return resultList;
    }

    /**
     * 更新视频的直播时间类型
     */
    private void updateVideoType(YoutubeLiveVideoVo item){
        if(item != null){
            Date  currentTime  =   new Date();
            if(currentTime.getTime() < item.getStartTime().getTime()){
                item.setVideoType(YoutubeVideoTypeEnum.UPCOMING.getKey());
            }else if( currentTime.getTime() > item.getStartTime().getTime() && currentTime.getTime() < item.getEndTime().getTime()){
                item.setVideoType(YoutubeVideoTypeEnum.LIVE.getKey());
            }else{
                item.setVideoType(YoutubeVideoTypeEnum.COMPLETED.getKey());
            }
        }

    }


}
