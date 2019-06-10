package cn.farwalker.ravv.service.web.webmodel.biz.impl;

import cn.farwalker.ravv.service.model.bestsellers.activity.biz.IBestSellersActivityBiz;
import cn.farwalker.ravv.service.model.bestsellers.activity.model.BestSellersActivityBo;
import cn.farwalker.ravv.service.model.bestsellers.goods.biz.IBestSellersGoodsBiz;
import cn.farwalker.ravv.service.model.bestsellers.goods.model.BestSellersGoodsBo;
import cn.farwalker.ravv.service.model.newarrivals.activity.biz.INewArrivalsActivityBiz;
import cn.farwalker.ravv.service.model.newarrivals.activity.model.NewArrivalsActivityBo;
import cn.farwalker.ravv.service.model.newarrivals.goods.biz.INewArrivalsGoodsBiz;
import cn.farwalker.ravv.service.model.newarrivals.goods.biz.INewArrivalsGoodsService;
import cn.farwalker.ravv.service.model.newarrivals.goods.model.NewArrivalsGoodsBo;
import cn.farwalker.ravv.service.web.webmodel.biz.IWebModelBiz;
import cn.farwalker.ravv.service.web.webmodel.biz.IWebModelService;
import cn.farwalker.ravv.service.web.webmodel.model.WebModelBo;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import com.baomidou.mybatisplus.mapper.Condition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class IWebModelServiceImpl implements IWebModelService {

    @Autowired
    private IWebModelBiz webModelBiz;

    @Autowired
    private IBestSellersGoodsBiz bestSellersGoodsBiz;

    @Autowired
    private IBestSellersActivityBiz bestSellersActivityBiz;

    @Autowired
    private INewArrivalsGoodsBiz newArrivalsGoodsBiz;

    @Autowired
    private INewArrivalsActivityBiz newArrivalsActivityBiz;

    @Override
    public List<WebModelBo> getAllModel() {
        List<WebModelBo> allModel = webModelBiz.selectList(Condition.create()
                                    .orderBy(WebModelBo.Key.sequence.toString(), true));
        return allModel;
    }

    @Override
    public String insertData() {
        //插入bestSellersActivity
//        insertSellersActivity();
        //插入bestSellersGoods
        insertSellersGoods();
        //插入newArrivalsActivity
//        insertArrivalsActivity();
        //插入newArrivalsGoods
        insertArrivalsGoods();
        return "success";
    }

    private void insertSellersActivity(){
        List<BestSellersActivityBo> sellersActivityBoList = new ArrayList<>();
        for(int i = 0; i < 5;i++){
            int j = i;
            BestSellersActivityBo activityBo = new BestSellersActivityBo();
            activityBo.setContent("测试数据");
            activityBo.setTitle("测试标题");
            activityBo.setGmtCreate(new Date());
            activityBo.setGmtModified(new Date());
            activityBo.setGoodsId(1073102169818165250L);
            activityBo.setSkuId(1073102205847236610L);
            activityBo.setSequence(++j);
            activityBo.setImgUrl("goods_sku_def/image_url/18CDE2757XB50001/6.jpg");
            activityBo.setJumpTo("47.107.236.246/admin");
            sellersActivityBoList.add(activityBo);
        }
        if(!bestSellersActivityBiz.insertBatch(sellersActivityBoList))
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
    }

    private void insertSellersGoods(){
        List<BestSellersGoodsBo> sellersGoodsBoList = new ArrayList<>();
        for(int i = 0;i < 15;i++){
            int j = i;
            BestSellersGoodsBo  goodsBo = new BestSellersGoodsBo();
            goodsBo.setDisplay(1);
            goodsBo.setGoodsId(1073102169818165250L);
            goodsBo.setSkuId(1073102205847236610L);
            goodsBo.setImgUrl("goods_sku_def/image_url/18CDE2757XB50001/6.jpg");
            goodsBo.setPrice(new BigDecimal("15.9"));
            goodsBo.setGmtCreate(new Date());
            goodsBo.setGmtModified(new Date());
            goodsBo.setSequence(++j);
            sellersGoodsBoList.add(goodsBo);
        }
        if(!bestSellersGoodsBiz.insertBatch(sellersGoodsBoList))
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
    }

    private void insertArrivalsActivity(){
        List<NewArrivalsActivityBo> arrivalsActivityBoList = new ArrayList<>();
        for(int i = 0; i < 5;i++){
            int j = i;
            NewArrivalsActivityBo activityBo = new NewArrivalsActivityBo();
            activityBo.setContent("测试数据");
            activityBo.setTitle("测试标题");
            activityBo.setGmtCreate(new Date());
            activityBo.setGmtModified(new Date());
            activityBo.setGoodsId(1073102169818165250L);
            activityBo.setSkuId(1073102205847236610L);
            activityBo.setSequence(++j);
            activityBo.setImgUrl("goods_sku_def/image_url/18CDE2757XB50001/6.jpg");
            activityBo.setJumpTo("47.107.236.246/admin");
            arrivalsActivityBoList.add(activityBo);
        }
        if(!newArrivalsActivityBiz.insertBatch(arrivalsActivityBoList))
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
    }

    private void insertArrivalsGoods(){
        List<NewArrivalsGoodsBo> arrivalsGoodsBoList = new ArrayList<>();
        for(int i = 0;i < 15;i++){
            int j = i;
            NewArrivalsGoodsBo  goodsBo = new NewArrivalsGoodsBo();
            goodsBo.setDisplay(1);
            goodsBo.setGoodsId(1073102169818165250L);
            goodsBo.setSkuId(1073102205847236610L);
            goodsBo.setImgUrl("goods_sku_def/image_url/18CDE2757XB50001/6.jpg");
            goodsBo.setPrice(new BigDecimal("15.9"));
            goodsBo.setGmtCreate(new Date());
            goodsBo.setGmtModified(new Date());
            goodsBo.setSequence(++j);
            arrivalsGoodsBoList.add(goodsBo);
        }
        if(!newArrivalsGoodsBiz.insertBatch(arrivalsGoodsBoList))
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
    }
}
