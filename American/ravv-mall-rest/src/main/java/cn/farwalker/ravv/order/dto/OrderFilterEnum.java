package cn.farwalker.ravv.order.dto;

import cn.farwalker.ravv.service.order.orderinfo.constants.OrderStatusEnum;
import cn.farwalker.waka.orm.EnumManager.IEnumJsons;

/**
 * 订单过滤枚举
 * @author Administrator
 *
 */
public enum OrderFilterEnum implements IEnumJsons{
	WaitReview("WaitReview","等待评论"),AfterSale("AfterSale","售后订单"),
	
	REVIEWADOPT_UNPAID(OrderStatusEnum.REVIEWADOPT_UNPAID), 
	PAID_UNSENDGOODS(OrderStatusEnum.PAID_UNSENDGOODS ),
	SENDGOODS_UNCONFIRM(OrderStatusEnum.SENDGOODS_UNCONFIRM ),
	SING_GOODS(OrderStatusEnum.SING_GOODS ),
	TRADE_CLOSE(OrderStatusEnum.TRADE_CLOSE ), 
	CANCEL(OrderStatusEnum.CANCEL),
	INVALID(OrderStatusEnum.INVALID)
	;
	
	private final String key;
	private final String label;
	
	private final OrderStatusEnum state;
	
	private OrderFilterEnum(OrderStatusEnum state){
		this.state = state;
		this.key = "";
		this.label = "";
	}
	private OrderFilterEnum(String key, String desc){
		this.state = null;
		this.key = key;
		this.label = desc;
	}

	public OrderStatusEnum getOrderStatus(){
		return state;
	}
	
	@Override
	public String getLabel() {
		return (state == null ? label:state.getLabel());
	} 
 
	@Override
	public String getKey() {
		return (state == null ? key:state.getKey());
	}
}
