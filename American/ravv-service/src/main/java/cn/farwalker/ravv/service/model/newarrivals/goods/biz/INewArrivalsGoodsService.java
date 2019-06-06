package cn.farwalker.ravv.service.model.newarrivals.goods.biz;

import cn.farwalker.ravv.service.model.newarrivals.goods.model.NewArrivalsGoodsBo;

import java.util.List;

public interface INewArrivalsGoodsService {
    List<NewArrivalsGoodsBo> getGoods(int currentPage, int pageSize);
}
