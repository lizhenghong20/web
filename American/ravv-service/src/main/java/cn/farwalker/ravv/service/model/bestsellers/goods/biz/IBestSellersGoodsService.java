package cn.farwalker.ravv.service.model.bestsellers.goods.biz;

import cn.farwalker.ravv.service.model.bestsellers.goods.model.BestSellersGoodsBo;

import java.util.List;

public interface IBestSellersGoodsService {
    List<BestSellersGoodsBo> getGoods(int currentPage, int pageSize);
}
