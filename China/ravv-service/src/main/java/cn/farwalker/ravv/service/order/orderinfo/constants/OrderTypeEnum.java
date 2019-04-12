package cn.farwalker.ravv.service.order.orderinfo.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsonn;

/** 
 * 订单类型
 * @author Administrator
 *
 */
public enum OrderTypeEnum implements IEnumJsonn{
	/**
	 * 有拆单的主单
	 */
	MASTER(0, "有拆单的主单"),

	/**
	 * 有拆单的子单
	 */
	CHILD(1, "有拆单的子单"),

	/**
	 * 没有拆单的订单
	 */
	SINGLE(2, "没有拆单");

	private Integer key;
	private String desc;

	OrderTypeEnum(int key, String desc) {
		this.key = Integer.valueOf(key);
		this.desc = desc;
	}

	@Override
	public Integer getKey() {
		return key;
	}
	@Override
	public String getLabel() {
		return desc;
	}

	public static boolean validateOrderType(Integer key) {
		OrderTypeEnum[] arr = OrderTypeEnum.values();
		for (OrderTypeEnum tmp : arr) {
			if (tmp.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}
}
