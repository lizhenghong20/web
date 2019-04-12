package cn.farwalker.ravv.service.order.orderinfo.model;

import java.util.List;
import java.util.Map;

import cn.farwalker.ravv.service.order.constants.PaymentPlatformEnum;

import com.google.common.collect.Table;
 

/**
 * 生成订单的对象(购买对象)
 * 
 * @author k2
 *
 */
public class OrderInfoBuyBo extends OrderInfoBo {

	/**
	 * 商品id
	 */
	private Long goodsId;

	/**
	 * 地址id
	 */
	private Long addressId;

	/**
	 * 支付方式
	 */
	private PaymentPlatformEnum payMethod;

	/**
	 * goodsId:skuId:数量
	 */
	private String skuIdQuantity;

	/**
	 * sku参数
	 */
	private Table<Long, Long, Integer> skuMap;

	/**
	 * 是否来自购物车
	 */
	private boolean isFromCart;

	/**
	 * 优惠券id
	 */
	private long couponId;

	/**
	 * 时区？
	 */
	private int timeZone;

	/**
	 * 商品id及对应的直接卖家 (goodsId:sellerId,goodsId:sellerId)
	 */
	private String goodsIdAndSellerIdStr;
	/**
	 * 商品id及对应的直接卖家
	 */
	private List<Map<Long, Long>> goodsIdAndSellerIdMapList;

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public Table<Long, Long, Integer> getSkuMap() {
		return skuMap;
	}

	public void setSkuMap(Table<Long, Long, Integer> skuMap) {
		this.skuMap = skuMap;
	}

	public boolean isFromCart() {
		return isFromCart;
	}

	public void setFromCart(boolean isFromCart) {
		this.isFromCart = isFromCart;
	}

	public long getCouponId() {
		return couponId;
	}

	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}

	public int getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(int timeZone) {
		this.timeZone = timeZone;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getSkuIdQuantity() {
		return skuIdQuantity;
	}

	public void setSkuIdQuantity(String skuIdQuantity) {
		this.skuIdQuantity = skuIdQuantity;
	}

	public String getGoodsIdAndSellerIdStr() {
		return goodsIdAndSellerIdStr;
	}

	public void setGoodsIdAndSellerIdStr(String goodsIdAndSellerIdStr) {
		this.goodsIdAndSellerIdStr = goodsIdAndSellerIdStr;
	}

	public List<Map<Long, Long>> getGoodsIdAndSellerIdMapList() {
		return goodsIdAndSellerIdMapList;
	}

	public void setGoodsIdAndSellerIdMapList(List<Map<Long, Long>> goodsIdAndSellerIdMapList) {
		this.goodsIdAndSellerIdMapList = goodsIdAndSellerIdMapList;
	}

	public PaymentPlatformEnum getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(PaymentPlatformEnum payMethod) {
		this.payMethod = payMethod;
	}

}
