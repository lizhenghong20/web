package cn.farwalker.ravv.service.goodsext.viewlog.model;

import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsViewLogVo extends GoodsViewLogBo {
    String imgUrl;
    GoodsBo goodsBo;
    BigDecimal minPrice;
}
