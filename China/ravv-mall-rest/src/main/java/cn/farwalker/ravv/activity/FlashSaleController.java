package cn.farwalker.ravv.activity;

import cn.farwalker.ravv.service.flash.sale.biz.IFlashSaleService;
import cn.farwalker.ravv.service.flash.sale.model.FlashSaleCategoryVo;
import cn.farwalker.ravv.service.flash.sku.model.FlashGoodsSkuVo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by asus on 2018/11/29.
 */
@Slf4j
@RestController
@RequestMapping("/flash_sale")
public class FlashSaleController {
    @Autowired
    private IFlashSaleService iFlashSaleService;

    @RequestMapping("get_category")
    public JsonResult<List<FlashSaleCategoryVo>> getCategory(){
        try {
            return JsonResult.newSuccess(iFlashSaleService.getCategory());

        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }
    @RequestMapping("get_goods")
    public JsonResult<List<FlashGoodsSkuVo>> getGoods(Long flashSaleId,Integer currentPage, Integer pageSize){
        try {
            currentPage++;
            if(flashSaleId == null||currentPage < 0||pageSize < 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            List<FlashGoodsSkuVo> rs =iFlashSaleService.getGoods(flashSaleId,currentPage,pageSize);
            return JsonResult.newSuccess(rs);
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    /**
     * 限时购首页显示商品
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping("get_goods_in_home")
    public JsonResult<List<FlashGoodsSkuVo>> getGoodsInHome(Integer currentPage, Integer pageSize){
        try {
            currentPage++;
            return JsonResult.newSuccess(iFlashSaleService.getGoodsInHome(currentPage,pageSize));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }













}
