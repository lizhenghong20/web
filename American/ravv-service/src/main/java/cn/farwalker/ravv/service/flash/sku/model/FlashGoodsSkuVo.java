package cn.farwalker.ravv.service.flash.sku.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Created by asus on 2018/11/29.
 */
@Data
public class FlashGoodsSkuVo extends FlashGoodsSkuBo {
    //特定sku下的图片
    String goodsImageUrl;

    //商品主图
    String goodsImageUrlMajor;
    boolean isFrozen;
    BigDecimal originPrice;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date currentTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endtime;

    String goodsName;

    String keyWords;
}
