package cn.farwalker.ravv.service.flash.sale.biz;


import cn.farwalker.ravv.service.flash.sale.model.FlashSaleCategoryVo;
import cn.farwalker.ravv.service.flash.sku.model.FlashGoodsSkuVo;
import cn.farwalker.ravv.service.flash.sale.model.FlashSaleBo;
import java.util.List;

import cn.farwalker.ravv.service.flash.sku.model.FlashGoodsSkuBo;

public interface IFlashSaleService {
	
	/**
	 * 获取限时购商品保存的sku
	 * @param flashSaleId 限时购id
	 * @param goodsId 商品id
	 * @return
	 */
	List<FlashGoodsSkuBo> flashGoodsSkuList(Long flashSaleId, Long goodsId);

	/**
	 * 获得限时购的目录
	 * @return
     */
    public List<FlashSaleCategoryVo> getCategory();

	/**
	 * 根据限时购目录获取商品
	 * @param flashSaleId
	 * @param currentPage
	 * @param pageSize
     * @return
     */
    public List<FlashGoodsSkuVo> getGoods(Long flashSaleId, Integer currentPage, Integer pageSize);

	/**
	 * 首页获取限时购的商品
	 * @param currentPage
	 * @param pageSize
     * @return
     */
    public List<FlashGoodsSkuVo> getGoodsInHome(Integer currentPage, Integer pageSize);
}
