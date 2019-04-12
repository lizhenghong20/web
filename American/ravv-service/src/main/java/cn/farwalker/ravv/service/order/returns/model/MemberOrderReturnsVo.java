package cn.farwalker.ravv.service.order.returns.model;

import java.util.List;

import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuVo;

/**
 * 用户退货单
 * 
 */

public class MemberOrderReturnsVo extends OrderReturnsBo {

	private static final long serialVersionUID = -2275829168487250857L;

	/**
	 * 总退货数量
	 */
	private Integer returnCount;
	
	/**
	 * 退货sku数量
	 */
	private Integer returnDetailNum;
	
	/**
	 * 订单商品
	 */
	private List<GoodsSkuVo> goodSkuInfo;

	public Integer getReturnCount() {
		return returnCount;
	}

	public void setReturnCount(Integer returnCount) {
		this.returnCount = returnCount;
	}

	public Integer getReturnDetailNum() {
		return returnDetailNum;
	}

	public void setReturnDetailNum(Integer returnDetailNum) {
		this.returnDetailNum = returnDetailNum;
	}

	public List<GoodsSkuVo> getGoodSkuInfo() {
		return goodSkuInfo;
	}

	public void setGoodSkuInfo(List<GoodsSkuVo> goodSkuInfo) {
		this.goodSkuInfo = goodSkuInfo;
	}

}