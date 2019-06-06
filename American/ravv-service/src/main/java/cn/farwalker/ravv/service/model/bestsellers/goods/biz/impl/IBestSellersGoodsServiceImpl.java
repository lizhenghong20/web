package cn.farwalker.ravv.service.model.bestsellers.goods.biz.impl;

import cn.farwalker.ravv.service.goods.base.model.GoodsDetailsVo;
import cn.farwalker.ravv.service.model.bestsellers.goods.biz.IBestSellersGoodsBiz;
import cn.farwalker.ravv.service.model.bestsellers.goods.biz.IBestSellersGoodsService;
import cn.farwalker.ravv.service.model.bestsellers.goods.dao.IBestSellersGoodsDao;
import cn.farwalker.ravv.service.model.bestsellers.goods.model.BestSellersGoodsBo;
import cn.farwalker.ravv.service.model.newarrivals.goods.model.NewArrivalsGoodsBo;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.plugins.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class IBestSellersGoodsServiceImpl implements IBestSellersGoodsService {

    @Autowired
    private IBestSellersGoodsBiz goodsBiz;

    @Autowired
    private IBestSellersGoodsDao iBestSellersGoodsDao;

    @Override
    public List<GoodsDetailsVo> getGoods(int currentPage, int pageSize) {
        Page page = new Page(currentPage, pageSize);
        List<GoodsDetailsVo> queryList = iBestSellersGoodsDao.getGoods(page);
        return queryList;
    }
}
