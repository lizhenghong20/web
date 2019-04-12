package cn.farwalker.ravv.goods;
import java.util.List;

import cn.farwalker.ravv.admin.goods.AdminGoodsService;
import cn.farwalker.waka.core.WakaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.farwalker.ravv.admin.goods.dto.GoodsPropertyVo;
import cn.farwalker.ravv.admin.goods.dto.GoodsSpecVo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.util.Tools;

/**
 * 商品属性管理<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@RestController
@RequestMapping("/goods/goodsproperty")
public class GoodsPropertyController{
    private final static  Logger log =LoggerFactory.getLogger(GoodsPropertyController.class);

    @Autowired
	private AdminGoodsService adminGoodsService;

    /**按分类取得属性
     * @param goodsid 分类<br/>
     */
    @RequestMapping("/propertys")
    public JsonResult<List<GoodsPropertyVo>> getPropertys(@RequestParam Long goodsid){
		try {
			if(goodsid == 0 || goodsid == null){
				throw new WakaException("商品id不能为空");
			}
			return adminGoodsService.getPropertys(goodsid);
		} catch (WakaException e) {
			log.error("删除记录", e);
			return JsonResult.newFail(e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());
		}

    }
 
    /**
     * 创建商品的属性值及SKU
     * @param goodsid 商品ID<br/>
     * @param values SKU属性值<br/>
     * @param skus sku组合,每组以分号分隔,组内用斜杠"/"分隔 )<br/>
     */
    @RequestMapping("/createsku")
    public JsonResult<Boolean> doCreateSku(Long goodsid,@RequestBody List<GoodsSpecVo> values, String skus){
		try {
			//createMethodSinge创建方法
			if(goodsid==null){
				throw new WakaException("商品ID不能为空");
			}
			if(Tools.string.isEmpty(skus)){
				throw new WakaException("sku定义,每组以分号分隔,组内用逗号分隔(12,34;12,45)不能为空");
			}
			return JsonResult.newSuccess(adminGoodsService.createSku(goodsid, values, skus));
		} catch (WakaException e) {
			log.error("删除记录", e);
			return JsonResult.newFail(e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());
		}

    }

}