package cn.farwalker.ravv.service.goodsext.favorite.model;

import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GoodsFavoriteVo extends GoodsFavoriteBo {

    String imgUrl;
    GoodsBo goodsBo;

    BigDecimal price;
    //差价
    BigDecimal spreadPrice;
    //商品状态，是否下架，有无库存
    String goodsStatus;

    //添加菜单id
    List<Long> menuIdList;

    //评价数量
    int reviewCount = 0;
}
