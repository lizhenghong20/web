package cn.farwalker.ravv.service.goods.base.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import cn.farwalker.ravv.common.constants.ShippingTypeEnum;

/**
 * Created by asus on 2018/11/17.
 */
@Data
public class GoodsDetailsVo extends GoodsVo{
    int answeredQuestions = 76;
    String shippingType = ShippingTypeEnum.FREESHIP.getKey();
    String shippingTo = "";
    List<PropertyLineVo>  allPropertyValues;
    int review = 0;

    //是否收藏
    boolean isFavorite = false;

    BigDecimal highestPrice;
    BigDecimal lowestPrice;

    BigDecimal listHighestPrice;
    BigDecimal listLowestPrice;


    //满12可用2
    String coupon = "2,12";

}
