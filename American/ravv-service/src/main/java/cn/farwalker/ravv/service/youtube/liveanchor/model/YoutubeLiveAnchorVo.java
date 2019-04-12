package cn.farwalker.ravv.service.youtube.liveanchor.model;

import cn.farwalker.ravv.service.youtube.livevideo.model.YoutubeLiveVideoBo;
import cn.farwalker.ravv.service.youtube.livevideo.model.YoutubeLiveVideoVo;
import cn.farwalker.ravv.service.youtube.livevideo.model.YoutubeRelatedGoodsVo;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import lombok.Data;

import java.util.List;

/**
 * Created by asus on 2019/1/7.
 */
@Data
public class YoutubeLiveAnchorVo extends YoutubeLiveAnchorBo {
    String firstName;
    String lastName;
    String headShotUrl;

    //是否被当前登录用户关注，如果用户没登录则没有此字段
    boolean isFollowed = false;

    //有多少人关注此主播
    Integer fansNumber = 0;
    //每个
    List<YoutubeLiveVideoVo> videoList;

    public void setHeadShotUrl(String url){
        this.headShotUrl =  QiniuUtil.covertToRealPath(url);
    }


}
