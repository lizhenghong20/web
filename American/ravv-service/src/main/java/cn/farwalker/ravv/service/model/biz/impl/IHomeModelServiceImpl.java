package cn.farwalker.ravv.service.model.biz.impl;

import cn.farwalker.ravv.service.model.VO.HomeModelVo;
import cn.farwalker.ravv.service.model.bestsellers.activity.biz.IBestSellersActivityService;
import cn.farwalker.ravv.service.model.bestsellers.goods.biz.IBestSellersGoodsService;
import cn.farwalker.ravv.service.model.biz.IHomeModelService;
import cn.farwalker.ravv.service.model.newarrivals.activity.biz.INewArrivalsActivityService;
import cn.farwalker.ravv.service.model.newarrivals.goods.biz.INewArrivalsGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IHomeModelServiceImpl implements IHomeModelService {

    @Autowired
    private INewArrivalsGoodsService newArrivalsGoodsService;

    @Autowired
    private INewArrivalsActivityService newArrivalsActivityService;

    @Autowired
    private IBestSellersGoodsService bestSellersGoodsService;

    @Autowired
    private IBestSellersActivityService bestSellersActivityService;

    @Override
    public HomeModelVo getHomeModel() {
        HomeModelVo homeModelVo = new HomeModelVo();
        homeModelVo.setBestSellersActivityList(bestSellersActivityService.getActivity());
        homeModelVo.setBestSellersGoodsList(bestSellersGoodsService.getGoods());
        homeModelVo.setNewArrivalsActivityList(newArrivalsActivityService.getActivity());
        homeModelVo.setNewArrivalsGoodsList(newArrivalsGoodsService.getGoods());
        return homeModelVo;
    }
}
