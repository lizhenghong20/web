package cn.farwalker.ravv.merchant.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单统计
 * @author Administrator
 * 供应商名称，订单数（含本供应商商品的订单数量），
 * 发货数量（按件计算），
 * 完成订单数（含本供应商商品的订单数量，且无退换货情况），
 * 取消订单数量（没有发货，取消的订单），
 * 退货件数量，
 * 换货件数量，
 * 订单总收入（有效订单合计）
 */
public class OrderStatsVo {
	private Date startdate,enddate;
	private Long merchantId;
	private String merchantName;
	
	/**订单数(有支付的都有效)*/
	private Integer orderCount;
	/**订单商品数量(有支付的都有效)*/
	private Integer goodsCount;
	
	/**完成订单数量（退一件都不统计）*/
	private Integer orderFinish;
	
	/**取消订单数量（没有发货，取消的订单）*/
	private Integer orderCancel;
	
	/**退货数量*/
	private Integer goodsReturn;
	/**换货数量*/
	private Integer goodsChange;
	
	/**订单关闭后的有效金额*/
	private BigDecimal totalAmt;

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	/**订单数(有支付的都有效)*/
	public Integer getOrderCount() {
		return orderCount;
	}
	/**订单数(有支付的都有效)*/
	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}
	/**订单商品数量(有支付的都有效)*/
	public Integer getGoodsCount() {
		return goodsCount;
	}
	/**订单商品数量(有支付的都有效)*/
	public void setGoodsCount(Integer goodsCount) {
		this.goodsCount = goodsCount;
	}
	/**完成订单数量（退一件都不统计）*/
	public Integer getOrderFinish() {
		return orderFinish;
	}
	/**完成订单数量（退一件都不统计）*/
	public void setOrderFinish(Integer orderFinish) {
		this.orderFinish = orderFinish;
	}
	/**取消订单数量（没有发货，取消的订单）*/
	public Integer getOrderCancel() {
		return orderCancel;
	}
	/**取消订单数量（没有发货，取消的订单）*/
	public void setOrderCancel(Integer orderCancel) {
		this.orderCancel = orderCancel;
	}
	/**退货数量*/
	public Integer getGoodsReturn() {
		return goodsReturn;
	}
	/**退货数量*/
	public void setGoodsReturn(Integer goodsReturn) {
		this.goodsReturn = goodsReturn;
	}
	/**换货数量*/
	public Integer getGoodsChange() {
		return goodsChange;
	}
	/**换货数量*/
	public void setGoodsChange(Integer goodsChange) {
		this.goodsChange = goodsChange;
	}
	/**订单关闭后的有效金额*/
	public BigDecimal getTotalAmt() {
		return totalAmt;
	}
	/**订单关闭后的有效金额*/
	public void setTotalAmt(BigDecimal totalAmt) {
		this.totalAmt = totalAmt;
	}
}
