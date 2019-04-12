package cn.farwalker.ravv.service.order.returns.model;

import java.util.List;

import cn.farwalker.ravv.service.base.storehouse.model.StorehouseBo;
import cn.farwalker.ravv.service.order.orderlogistics.model.OrderLogisticsBo;
import cn.farwalker.ravv.service.order.returns.constants.DamagedConditionEnum;
import cn.farwalker.ravv.service.order.returns.constants.DamagedSourceEnum;

/**
 * 订单退货
 * 
 */

public class OrderReturnsVo extends OrderReturnsBo {

	private static final long serialVersionUID = -2275829168487250857L;

	/**
	 * 退货状态流程列表，最后一条为当前的状态（高亮显示）
	 */
	private List<OrderReturnLogVo> returnsLogList;

	/**
	 * 损坏程度(后台验收用)
	 */
	private DamagedConditionEnum damagedCondition;

	/**
	 * 备注(后台验收用)
	 */
	private String content;

	/**
	 * 订单退货详情id，验收时的对应详情的id(后台验收用)
	 */
	private Long orderReturnsDetailId;

	/**
	 * 包装是否损坏(后台验收用)
	 */
	private Boolean packingDamaged;

	/**
	 * 损坏原因(后台验收用)
	 */
	private List<DamagedSourceEnum> damagedSource;

	/**
	 * 是否退还买家(后台验收用)
	 */
	private Boolean isReturnGoods;
	/**
	 * 买家是否承担运费(后台验收用)
	 */
	private Boolean buyerBearPostage;
	/**
	 * 实际退回数量(后台验收用)
	 */
	private Integer actualRefundQty;

	/**
	 * 审核不通过，或者拒绝退货换过原因
	 */
	private String refuseReason;

	/**
	 * 仓库
	 */
	private StorehouseBo storehouse;
	
	/**
	 * 退货物流信息
	 */
	private OrderLogisticsBo logisticsBo;
	
	/**
	 * 退货商品总数量
	 */
	private Integer reGoodsTotal;

	public DamagedConditionEnum getDamagedCondition() {
		return damagedCondition;
	}

	public void setDamagedCondition(DamagedConditionEnum damagedCondition) {
		this.damagedCondition = damagedCondition;
	}

	public List<DamagedSourceEnum> getDamagedSource() {
		return damagedSource;
	}

	public void setDamagedSource(List<DamagedSourceEnum> damagedSource) {
		this.damagedSource = damagedSource;
	}

	public Boolean getIsReturnGoods() {
		return isReturnGoods;
	}

	public void setIsReturnGoods(Boolean isReturnGoods) {
		this.isReturnGoods = isReturnGoods;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Boolean getBuyerBearPostage() {
		return buyerBearPostage;
	}

	public void setBuyerBearPostage(Boolean buyerBearPostage) {
		this.buyerBearPostage = buyerBearPostage;
	}

	public Boolean getPackingDamaged() {
		return packingDamaged;
	}

	public void setPackingDamaged(Boolean packingDamaged) {
		this.packingDamaged = packingDamaged;
	}

	public Long getOrderReturnsDetailId() {
		return orderReturnsDetailId;
	}

	public void setOrderReturnsDetailId(Long orderReturnsDetailId) {
		this.orderReturnsDetailId = orderReturnsDetailId;
	}

	public Integer getActualRefundQty() {
		return actualRefundQty;
	}

	public void setActualRefundQty(Integer actualRefundQty) {
		this.actualRefundQty = actualRefundQty;
	}

	public List<OrderReturnLogVo> getReturnsLogList() {
		return returnsLogList;
	}

	public void setReturnsLogList(List<OrderReturnLogVo> returnsLogList) {
		this.returnsLogList = returnsLogList;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

	public StorehouseBo getStorehouse() {
		return storehouse;
	}

	public void setStorehouse(StorehouseBo storehouse) {
		this.storehouse = storehouse;
	}

	public OrderLogisticsBo getLogisticsBo() {
		return logisticsBo;
	}

	public void setLogisticsBo(OrderLogisticsBo logisticsBo) {
		this.logisticsBo = logisticsBo;
	}

	public Integer getReGoodsTotal() {
		return reGoodsTotal;
	}

	public void setReGoodsTotal(Integer reGoodsTotal) {
		this.reGoodsTotal = reGoodsTotal;
	}

}