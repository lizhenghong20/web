package cn.farwalker.ravv.flash;

import java.util.List;

import cn.farwalker.ravv.admin.flash.AdminFlashService;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.util.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;

import cn.farwalker.ravv.service.flash.goods.model.FlashGoodsVo;
import cn.farwalker.ravv.service.flash.sku.model.FlashGoodsSkuBo;
import cn.farwalker.ravv.service.goodssku.skudef.model.SkuPriceInventoryVo;
import cn.farwalker.waka.core.JsonResult;

/**
 * 限时购
 * 
 * @author chensl
 *
 */
@RestController
@RequestMapping("/flash")
public class FlashController{

	private final static Logger log = LoggerFactory.getLogger(FlashController.class);

	@Autowired
	private AdminFlashService adminFlashService;

	/**
	 * 获取单条限时购记录的商品列表
	 * 
	 * @param flashSaleId
	 * @return
	 */
	@RequestMapping("/list")
	public JsonResult<Page<FlashGoodsVo>> getFlashGoodsList(Integer start, Integer size,
			String sortfield, Long flashSaleId) {
		try{

			if(Tools.number.isEmpty(flashSaleId)){
				throw new WakaException("限时购id不能为空");
			}
			return JsonResult.newSuccess(adminFlashService.getFlashGoodsList(start, size, sortfield, flashSaleId));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}
		

	}

	/**
	 * 添加商品到限时购
	 * @param flashSaleId
	 * @param flashGoodsIdList
	 * @return
	 */
	@RequestMapping("/select")
	public JsonResult<Boolean> selectFlashGoods(@RequestBody List<Long> flashGoodsIdList, Long flashSaleId) {
		try{
			if (flashSaleId == null) {
				throw new WakaException("限时购id不能为空");
			}
			if (CollectionUtils.isEmpty(flashGoodsIdList)) {
				throw new WakaException("未选择商品");
			}
			return JsonResult.newSuccess(adminFlashService.selectFlashGoods(flashGoodsIdList, flashSaleId));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}

	}
	
	/**
	 * 修改限时购商品的排序
	 * @param flashGoodsId 限时购商品关联表id
	 * @param sequence 排序
	 * @return
	 */
	@RequestMapping("/change_sequence")
	public JsonResult<Boolean> changeFlashGoodsSeq(@RequestParam Long flashGoodsId, Integer sequence) {
		try{
			if(flashGoodsId == 0 || flashGoodsId == null){
				throw new WakaException("flashGoodsId为空");
			}
			return JsonResult.newSuccess(adminFlashService.changeFlashGoodsSeq(flashGoodsId, sequence));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}

	}

	/**
	 * 移除选中的商品
	 * @param flashSaleId
	 * @param flashGoodsIdList
	 * @return
	 */
	@RequestMapping("/remove")
	@Transactional
	public JsonResult<Boolean> removeFlashGoods(@RequestBody List<Long> flashGoodsIdList, Long flashSaleId) {
		try{
			if (flashSaleId == null) {
				throw new WakaException("限时购id不能为空");
			}
			if (CollectionUtils.isEmpty(flashGoodsIdList)) {
				throw new WakaException("未选择商品");
			}
			return JsonResult.newSuccess(adminFlashService.removeFlashGoods(flashGoodsIdList, flashSaleId));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}

	}

	/**
	 * 保存限时购商品的sku
	 * 
	 * @param goodsSkuList
	 * @return
	 */
	@RequestMapping("save_sku")
	public JsonResult<Boolean> saveFlashGoodsSku(@RequestBody List<SkuPriceInventoryVo> goodsSkuList,
												 Long flashSaleId, Long goodsId) {
		try{
			if (flashSaleId == null) {
				throw new WakaException("限时购id不能为空");
			}
			if (goodsId == null) {
				throw new WakaException("商品id不能为空");
			}
			return JsonResult.newSuccess(adminFlashService.saveFlashGoodsSku(goodsSkuList, flashSaleId, goodsId));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}

	}
	
	/**
	 * 获取限时购商品保存的sku
	 * @param flashSaleId
	 * @param goodsId
	 * @return
	 */
	@RequestMapping("flash_goods_sku")
	public JsonResult<List<FlashGoodsSkuBo>> getFlashGoodsSku(Long flashSaleId, Long goodsId){
		try{
//			if (flashSaleId == null) {
//				throw new WakaException("限时购id不能为空");
//			}
//			if (goodsId == null) {
//				throw new WakaException("商品id不能为空");
//			}
			return JsonResult.newSuccess(adminFlashService.getFlashGoodsSku(flashSaleId, goodsId));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}

	}

}
