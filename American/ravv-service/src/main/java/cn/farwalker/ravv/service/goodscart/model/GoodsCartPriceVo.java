package cn.farwalker.ravv.service.goodscart.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsCartPriceVo {
    BigDecimal sum;
    String goodsCartId;
}
