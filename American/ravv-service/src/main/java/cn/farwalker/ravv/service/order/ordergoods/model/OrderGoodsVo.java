package cn.farwalker.ravv.service.order.ordergoods.model;

import java.math.BigDecimal;

/**
 * 订单商品扩展
 * @author Administrator
 */
public class OrderGoodsVo extends OrderGoodsBo{
	private static final long serialVersionUID = 106511906788948754L;
	/**原价(没有促销时的价格)*/
	private BigDecimal orgiPrice;

	/**原价(没有促销时的价格)*/
	public BigDecimal getOrgiPrice() {
		return orgiPrice;
	}

	/**原价(没有促销时的价格)*/
	public void setOrgiPrice(BigDecimal orgiPrice) {
		this.orgiPrice = orgiPrice;
	}
}
