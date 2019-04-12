package cn.farwalker.ravv.service.youtube.livevideo.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by asus on 2019/1/4.
 */
@Data
public class YoutubeRelatedGoodsVo {
    Long GoodsId;
    String goodsName;
    String goodsMajorUrl;
    BigDecimal lowestPrice;
}
