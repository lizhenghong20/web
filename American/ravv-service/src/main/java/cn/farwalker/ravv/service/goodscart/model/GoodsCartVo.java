package cn.farwalker.ravv.service.goodscart.model;

import cn.farwalker.ravv.common.constants.FlashSaleStatusEnum;
import cn.farwalker.ravv.service.category.value.model.BaseCategoryPropertyValueBo;
import cn.farwalker.ravv.service.goods.base.model.ParseSkuExtVo;
import cn.farwalker.ravv.service.goods.base.model.ParseSkuTemp;
import cn.farwalker.ravv.service.goods.base.model.ParseSkuTemp;
import cn.farwalker.ravv.service.goods.base.model.PropertyLineVo;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuDefBo;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuVo;
import cn.farwalker.ravv.service.goodssku.specification.model.GoodsSpecificationDefVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by asus on 2018/11/27.
 */
@Data
public class GoodsCartVo extends  GoodsCartBo {

//    BigDecimal price;
//    Integer saleStockNum;
//    String goodsImgUrl;

    //总价
    BigDecimal sum;
    String goodsName;
    String goodsStatus;

    //当前goods_id 的所有的属性值
    List<PropertyLineVo> allPropertyValues;

    //当前good_id 的所有允许的sku列表
    List<GoodsSkuDefBo> skuList;

    //当前goods_id和 当前 sku_id下所选中的属性值
    List<PropertyLineVo> selectedPropertyValues;

    //当前选中的sku的属性
    ParseSkuExtVo parseSkuExtVo;

    //活动开始时间，若处于冻结状态则倒计时这个时间。
    Date startTime;

    //活动结束时间，若处于开始状态则倒计时这个时间。
    Date endTime;

}
