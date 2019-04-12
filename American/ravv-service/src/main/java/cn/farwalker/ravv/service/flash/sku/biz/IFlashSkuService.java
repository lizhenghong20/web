package cn.farwalker.ravv.service.flash.sku.biz;

import java.util.List;

import cn.farwalker.ravv.service.flash.sale.model.FlashSaleGoodsSkuVo;
import cn.farwalker.ravv.service.flash.sku.model.FlashGoodsSkuBo;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuDefBo;

/**
 * 闪购商品的sku<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IFlashSkuService {
	/**
	 * 取得参与闪购商品(有效的促销的商品)
	 * @param goodsId
	 * @param propertyValueIds 属性值
	 * @return
	 */
	
	public FlashSaleGoodsSkuVo getGoodsSkuBo(Long goodsId,List<Long> propertyValueIds);
	/**
	 * 取得参与闪购商品(有效的促销的商品)
	 * @param goodsId
	 * @param skuId 直接的skuid ,{@link GoodsSkuDefBo#getId()}
	 * @return
	 */
	public FlashSaleGoodsSkuVo getGoodsSkuBo(Long goodsId,Long skuId) ;
}