package cn.farwalker.ravv.service.goodscart.model;

import lombok.Data;

/**
 * Created by asus on 2019/1/17.
 */
@Data
public class RecoverToCartForm {
    Long goodsId;
    Long skuId;
    Integer quantity;
}
