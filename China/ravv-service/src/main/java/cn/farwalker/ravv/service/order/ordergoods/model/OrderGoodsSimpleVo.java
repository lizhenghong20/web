package cn.farwalker.ravv.service.order.ordergoods.model;

import java.math.BigDecimal;

public class OrderGoodsSimpleVo {
	
	/** 订单商品id */
	private Long id;
	
	/** 订单id */
	private Long orderId;
	
	/** 商品id */
	private Long goodsId;
	
	/** 商品数量 */
	private Integer quantity;
	
	/** 商品单价 */
	private BigDecimal price;
	
	/** 创建时间（年月） */
	private String gmtCreate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	
}
