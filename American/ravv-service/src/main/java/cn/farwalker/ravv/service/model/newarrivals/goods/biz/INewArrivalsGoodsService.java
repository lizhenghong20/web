package cn.farwalker.ravv.service.model.newarrivals.goods.biz;

import cn.farwalker.ravv.service.goods.base.model.GoodsDetailsVo;
import cn.farwalker.ravv.service.model.newarrivals.goods.model.NewArrivalsGoodsBo;

import java.util.List;

public interface INewArrivalsGoodsService {
    List<GoodsDetailsVo> getGoods();
}
