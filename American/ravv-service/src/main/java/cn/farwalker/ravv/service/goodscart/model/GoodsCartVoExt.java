package cn.farwalker.ravv.service.goodscart.model;

import cn.farwalker.ravv.service.goods.base.model.ParseSkuTemp;
import cn.farwalker.ravv.service.goods.base.model.PropertyLineVo;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuDefBo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by asus on 2018/12/29.
 */
public class GoodsCartVoExt {
    BigDecimal sum;
    String goodsImgUrl;

    String goodsName;

    //当前goods_id 的所有的属性值
    List<PropertyLineVo> allPropertyValues;

    //当前good_id 的所有允许的sku列表
    List<GoodsSkuDefBo> skuList;

    //当前goods_id和 当前 sku_id下所选中的属性值
    List<PropertyLineVo> selectedPropertyValues;

    //当前goods_id和当前sku_id的商品是不是在限时购活动中
    boolean isInFlashSale;

    //当前选中的sku的属性
    ParseSkuTemp parseSkuTemp;

    //活动开始时间，若处于冻结状态则倒计时这个时间。
    Date startTime;

    //活动结束时间，若处于开始状态则倒计时这个时间。
    Date endTime;
}
