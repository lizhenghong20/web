package cn.farwalker.ravv.service.youtube.livegoods.dao;
import cn.farwalker.ravv.service.youtube.livevideo.model.YoutubeLiveVideoBo;
import cn.farwalker.ravv.service.youtube.livevideo.model.YoutubeRelatedGoodsVo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.farwalker.ravv.service.youtube.livegoods.model.YoutubeLiveGoodsBo;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 直播商品<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IYoutubeLiveGoodsDao extends BaseMapper<YoutubeLiveGoodsBo>{
    List<YoutubeRelatedGoodsVo> selectGoodsByVideoIdList(@Param(value = "videoIdList")List<Long> videoIdList);
    List<YoutubeRelatedGoodsVo> selectGoodsByVideoIdList(Page page, @Param(value = "videoIdList")List<Long> videoIdList);
}