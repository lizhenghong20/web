package cn.farwalker.ravv.service.order.returns.model;

import java.util.List;

import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuDefBo;

/**
 * 订单退货详情
 * 
 * @author generateModel.java
 */
public class OrderReturnsDetailVo extends OrderReturnsDetailBo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4023287068562240130L;

	/**
	 * 商品基础信息
	 */
	private GoodsBo goods;

	/**
	 * sku描述
	 */
	private GoodsSkuDefBo goodsSkuDef;

	/**
	 * 退货图片列表
	 */
	private List<String> imgList;

	public GoodsBo getGoods() {
		return goods;
	}

	public void setGoods(GoodsBo goods) {
		this.goods = goods;
	}

	public GoodsSkuDefBo getGoodsSkuDef() {
		return goodsSkuDef;
	}

	public void setGoodsSkuDef(GoodsSkuDefBo goodsSkuDef) {
		this.goodsSkuDef = goodsSkuDef;
	}

	public List<String> getImgList() {
		return imgList;
	}

	public void setImgList(List<String> imgList) {
		this.imgList = imgList;
	}

}