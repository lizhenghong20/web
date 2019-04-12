package cn.farwalker.ravv.service.youtube.livevideo.model;

import cn.farwalker.ravv.common.constants.YoutubeVideoTypeEnum;
import cn.farwalker.ravv.service.youtube.liveanchor.model.YoutubeLiveAnchorVo;
import lombok.Data;

import java.util.List;

/**
 * Created by asus on 2019/1/4.
 */
@Data
public class YoutubeLiveVideoVo extends YoutubeLiveVideoBo {
    List<YoutubeRelatedGoodsVo> goodsList;
    YoutubeLiveAnchorVo anchor;
    String videoType;
}
