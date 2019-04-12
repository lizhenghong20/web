package cn.farwalker.ravv.service.goods.base.model;

import cn.farwalker.ravv.common.constants.FlashSaleStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
public class ParseSkuTemp {
    //非活动价
    BigDecimal price;

    //活动价
    BigDecimal activityPrice;

    //库存
    int saleStockNum;

    //常规的售卖件数
    int saleCount;

    //拿出多少件商品来做限时购活动
    int flashActivityReserve;

    //限时购活动的售卖件数
    int flashSaleCount;

    //属性名:属性值
    String valueNames;


    //商品的sku图片
    String goodsImageUrl;
    Long skuId;

    //当前goods_id和当前sku_id的商品是不是在限时购活动中
    boolean isInFlashSale;

    //若在限时购活动中则判断其状态，状态有二，冻结中或进行中。
    boolean isFrozen;
    boolean isUnderWay;

    //活动title
    String title;

    //活动开始时间，若处于冻结状态则倒计时这个时间。
    Date startTime;

    //活动结束时间，若处于开始状态则倒计时这个时间。
    Date endTime;

    Date currentTime;

}
