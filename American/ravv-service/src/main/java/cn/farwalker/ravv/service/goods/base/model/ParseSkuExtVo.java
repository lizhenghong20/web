package cn.farwalker.ravv.service.goods.base.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by asus on 2018/12/29.
 */
@Data
public class ParseSkuExtVo {
    //原价，即非活动价，如果不在活动中则，originPrice==price
    BigDecimal originPrice;
    //价格
    BigDecimal price;
    //库存
    int saleStockNum;
    //售卖件数
    int saleCount;
    //属性名:属性值
    String valueNames;
    //商品的sku图片
    String goodsImageUrl;
    Long skuId;

    //当前goods_id和当前sku_id的商品是不是在限时购活动中
    boolean isInFlashSale;
    ActivityField activityField;

    Date currentTime;

    List<PropertyStockVO> propertyStockVOList;
}


