package cn.farwalker.ravv.model;

import cn.farwalker.ravv.service.goods.base.model.ParseSkuExtVo;
import cn.farwalker.ravv.service.model.bestsellers.activity.biz.IBestSellersActivityService;
import cn.farwalker.ravv.service.model.bestsellers.activity.model.BestSellersActivityBo;
import cn.farwalker.ravv.service.model.bestsellers.goods.biz.IBestSellersGoodsService;
import cn.farwalker.ravv.service.model.bestsellers.goods.model.BestSellersGoodsBo;
import cn.farwalker.ravv.service.model.newarrivals.activity.biz.INewArrivalsActivityService;
import cn.farwalker.ravv.service.model.newarrivals.activity.model.NewArrivalsActivityBo;
import cn.farwalker.ravv.service.model.newarrivals.goods.biz.INewArrivalsGoodsService;
import cn.farwalker.ravv.service.model.newarrivals.goods.model.NewArrivalsGoodsBo;
import cn.farwalker.ravv.service.web.webmodel.biz.IWebModelService;
import cn.farwalker.ravv.service.web.webmodel.model.WebModelBo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.util.Tools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {

    @Autowired
    private INewArrivalsActivityService arrivalsActivityService;

    @Autowired
    private INewArrivalsGoodsService arrivalsGoodsService;

    @Autowired
    private IBestSellersActivityService bestSellersActivityService;

    @Autowired
    private IBestSellersGoodsService bestSellersGoodsService;

    @Autowired
    private IWebModelService webModelService;

    @RequestMapping("/new_arrivals_activity")
    public JsonResult<List<NewArrivalsActivityBo>> getNewArrivalsActivity() {
        try {

            return JsonResult.newSuccess(arrivalsActivityService.getActivity());
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    @RequestMapping("/new_arrivals_goods")
    public JsonResult<List<NewArrivalsGoodsBo>> getNewArrivalsGoods(Integer currentPage, Integer pageSize) {
        try {
            currentPage++;
            return JsonResult.newSuccess(arrivalsGoodsService.getGoods(currentPage, pageSize));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    @RequestMapping("/best_sellers_activity")
    public JsonResult<List<BestSellersActivityBo>> getBestSellersActivity() {
        try {

            return JsonResult.newSuccess(bestSellersActivityService.getActivity());
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    @RequestMapping("/best_sellers_goods")
    public JsonResult<List<BestSellersGoodsBo>> getBestSellersGoods(Integer currentPage, Integer pageSize) {
        try {
            currentPage++;
            return JsonResult.newSuccess(bestSellersGoodsService.getGoods(currentPage, pageSize));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    @RequestMapping("/model")
    public JsonResult<List<WebModelBo>> getModel() {
        try {
            return JsonResult.newSuccess(webModelService.getAllModel());
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }
    }

}
