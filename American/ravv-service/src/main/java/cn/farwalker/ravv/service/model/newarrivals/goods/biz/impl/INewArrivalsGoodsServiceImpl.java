package cn.farwalker.ravv.service.model.newarrivals.goods.biz.impl;

import cn.farwalker.ravv.service.goods.base.model.GoodsDetailsVo;
import cn.farwalker.ravv.service.model.bestsellers.goods.dao.IBestSellersGoodsDao;
import cn.farwalker.ravv.service.model.bestsellers.goods.model.BestSellersGoodsBo;
import cn.farwalker.ravv.service.model.newarrivals.goods.biz.INewArrivalsGoodsBiz;
import cn.farwalker.ravv.service.model.newarrivals.goods.biz.INewArrivalsGoodsService;
import cn.farwalker.ravv.service.model.newarrivals.goods.dao.INewArrivalsGoodsDao;
import cn.farwalker.ravv.service.model.newarrivals.goods.model.NewArrivalsGoodsBo;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
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

    @Autowired
    private INewArrivalsGoodsDao iNewArrivalsGoodsDao;

    @Override
    public List<GoodsDetailsVo> getGoods() {
        List<GoodsDetailsVo> queryList = iNewArrivalsGoodsDao.getGoods();
//        if(queryList.size() == 0){
//            return queryList;
//        }
        queryList.forEach(s-> s.setImageMajor(QiniuUtil.getFullPath(s.getImageMajor())));
        return queryList;
    }
}
