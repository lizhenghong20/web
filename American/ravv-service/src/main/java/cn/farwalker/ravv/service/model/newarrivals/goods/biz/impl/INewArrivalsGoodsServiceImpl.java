package cn.farwalker.ravv.service.model.newarrivals.goods.biz.impl;

import cn.farwalker.ravv.service.model.bestsellers.goods.model.BestSellersGoodsBo;
import cn.farwalker.ravv.service.model.newarrivals.goods.biz.INewArrivalsGoodsBiz;
import cn.farwalker.ravv.service.model.newarrivals.goods.biz.INewArrivalsGoodsService;
import cn.farwalker.ravv.service.model.newarrivals.goods.model.NewArrivalsGoodsBo;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.plugins.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class INewArrivalsGoodsServiceImpl implements INewArrivalsGoodsService {

    @Autowired
    private INewArrivalsGoodsBiz goodsBiz;

    @Override
    public List<NewArrivalsGoodsBo> getGoods(int currentPage, int pageSize) {
        Page page = new Page(currentPage, pageSize);
        Page<NewArrivalsGoodsBo> arrivalsGoodsBoPage = goodsBiz.selectPage(page, Condition.create()
                .eq(NewArrivalsGoodsBo.Key.display.toString(), 1)
                .orderBy(NewArrivalsGoodsBo.Key.sequence.toString(), true));
        return arrivalsGoodsBoPage.getRecords();
    }
}
