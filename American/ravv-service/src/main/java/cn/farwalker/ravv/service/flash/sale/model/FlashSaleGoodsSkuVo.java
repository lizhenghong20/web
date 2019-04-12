package cn.farwalker.ravv.service.flash.sale.model;


import cn.farwalker.ravv.service.flash.sku.model.FlashGoodsSkuBo;

/**
 * 闪购的信息及商品的SKU信息
 *
 */
public class FlashSaleGoodsSkuVo extends FlashSaleVo {
	private static final long serialVersionUID = 7738128977787279909L;
	private FlashGoodsSkuBo goodsSkuBo;
	public FlashGoodsSkuBo getGoodsSkuVo() {
		return goodsSkuBo;
	}
	public void setGoodsSkuVo(FlashGoodsSkuBo goodsSkuVo) {
		this.goodsSkuBo = goodsSkuVo;
	}
}
