package cn.farwalker.ravv.goods;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import cn.farwalker.ravv.admin.goods.AdminGoodsService;
import cn.farwalker.waka.core.WakaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cn.farwalker.ravv.service.goodssku.skudef.biz.IGoodsSkuService;

import cn.farwalker.ravv.service.goodssku.skudef.model.SkuPriceInventoryVo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.util.Tools;


/**
 * SKU单价及库存设置<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@RestController
@RequestMapping("/goods/goodsskuprice")
public class GoodsSkuPriceController{
    private final static  Logger log =LoggerFactory.getLogger(GoodsSkuPriceController.class);

    @Resource
    private IGoodsSkuService goodsSkuService;

    @Autowired
    private AdminGoodsService adminGoodsService;
    
    /**删除记录
     * @param skuid sku表id<br/>
     */
    @RequestMapping("/delete")
    public JsonResult<Boolean> doDelete(@RequestParam Long skuid){
        try {
            if(skuid == 0 || skuid == null){
                throw new WakaException("skuid不能为空");
            }
            Integer rs = goodsSkuService.deleteSku(skuid);
            return JsonResult.newSuccess(rs.intValue()!=0);
        } catch (WakaException e) {
            log.error("删除记录", e);
            return JsonResult.newFail(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }

    }
    /**
     * 列表记录
     * @param goodsid 商品ID<br/>
     */
    @RequestMapping("/skulist")
    public JsonResult<List<SkuPriceInventoryVo>> getSkulist(Long goodsid){
        try {
            if(goodsid == 0 || goodsid == null){
                throw new WakaException("skuid不能为空");
            }
            return JsonResult.newSuccess(adminGoodsService.getSkulist(goodsid));
        } catch (WakaException e) {
            log.error("删除记录", e);
            return JsonResult.newFail(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }

    }

    /**修改记录
     * @param goodsid 商品ID<br/>
     * @param prices 库存及单价信息<br/>
     */
    @RequestMapping("/update")
    public JsonResult<Integer> doUpdate(Long goodsid,@RequestBody List<SkuPriceInventoryVo> prices){
        try {
            if(goodsid == 0 || goodsid == null){
                throw new WakaException("skuid不能为空");
            }
            if(Tools.collection.isEmpty(prices)){
                throw new WakaException("库存及单价信息不能为空");
            }

            return JsonResult.newSuccess(adminGoodsService.updateInventory(goodsid, prices));
        } catch (WakaException e) {
            log.error("删除记录", e);
            return JsonResult.newFail(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }

    }
}