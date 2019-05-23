package cn.farwalker.ravv.service.order.orderinfo.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;

/**
 * 订单状态
 * @author Administrator
 *
 */
public enum OrderStatusEnum implements IEnumJsons{
	/**
	 * 已创建，待审核
	 */
	//CREATED_UNREVIEW("created_unreview", "已创建，待审核"),

	/** 
	 * 审核通过，待付款 
	 */
	REVIEWADOPT_UNPAID("reviewadopt_unpaid", "审核通过，待付款"),

	/**
	 * 已付款，待发货
	 */
	PAID_UNSENDGOODS("paid_unsendgoods", "待发货"),

	/**
	 * 发货中
	 */
	//GOODS_SENDING("goods_sending", "发货中"),

	/**
	 * 已发货，待确认收货
	 */
	SENDGOODS_UNCONFIRM("sendgoods_unconfirm", "已发货"),

	/**已收货*/
	SING_GOODS("sing_goods", "已收货"),
	
	/**
	 * 交易完成
	 */
	//TRADE_COMPLETE("trade_complete", "交易成功"),

	/**
	 * 交易关闭(交易成功后，才能转为关闭，交易不成功，就是取消)
	 */
	TRADE_CLOSE("trade_close", "交易关闭"),
	/**
	 * 交易取消(没有支付、已退款：退款完成后，需要变为交易取消)
	 */
	CANCEL("cancel", "交易取消"),
	/**
	 * 订单失效（获取订单列表时判断是否失效）
	 */
	INVALID("invalid", "订单失效");
	
	/**
	 * 审核不通过
	 */
	//REVIEW_REJECT("review_reject", "审核不通过");
	
	private String key;
	private String desc;
	
	OrderStatusEnum(String key, String desc){
		this.key = key;
		this.desc = desc;
	}
	
	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getLabel() {
		return desc;
	}

	public static boolean validateOrderStatus(String status) {
        OrderStatusEnum[] arr = OrderStatusEnum.values();
        for (OrderStatusEnum tmp : arr) {
            if (tmp.getKey().equals(status)) {
                return true;
            }
        }
        return false;
    }
	/**
	 * 通用：有效的状态
	 * @param appClose 关闭的状态要不要(true:要)
	 * @return
	 */
	public static List<OrderStatusEnum> getValid(boolean appClose){
		ArrayList<OrderStatusEnum> rds = new ArrayList<>();
		List<OrderStatusEnum> invalids = getInvalid();
		for(OrderStatusEnum e : OrderStatusEnum.values()){
			if(invalids.contains(e)){
				continue;
			}
			
			if(e == TRADE_CLOSE){
				if(appClose){
					rds.add(e);
				}
			}
			else{
				rds.add(e);
			}
		}
		return rds;
	}
	/**通用：无效的状态*/
	public static List<OrderStatusEnum> getInvalid(){
		return Arrays.asList(REVIEWADOPT_UNPAID,CANCEL);
	}
}
