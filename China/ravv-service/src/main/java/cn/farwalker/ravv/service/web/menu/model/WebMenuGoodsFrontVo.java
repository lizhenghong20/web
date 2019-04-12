package cn.farwalker.ravv.service.web.menu.model;

import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WebMenuGoodsFrontVo extends GoodsBo {

    BigDecimal lowestPrice;

    String imgUrl;
}
