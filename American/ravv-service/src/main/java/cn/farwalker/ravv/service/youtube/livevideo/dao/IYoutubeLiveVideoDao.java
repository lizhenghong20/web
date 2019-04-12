package cn.farwalker.ravv.service.youtube.livevideo.dao;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.farwalker.ravv.service.youtube.livevideo.model.YoutubeLiveVideoBo;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 直播视频<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IYoutubeLiveVideoDao extends BaseMapper<YoutubeLiveVideoBo>{
    List<YoutubeLiveVideoBo> selectAllVideoByType(Page page,@Param(value = "videoType")String videoType
                                                            ,@Param(value = "currentTime")Date currentTime);
}