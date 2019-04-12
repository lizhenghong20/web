package cn.farwalker.ravv.service.order.operationlog.contants;

public enum BehaviorEnum {
	/**
	 * 创建订单
	 */
	CREATEORDER(1, "创建订单"),

	/**
	 * 取消订单
	 */
	DISTRIBUTION(2, "取消订单"),

	/**
	 * 退货
	 */
	REFUNDGOODS(3, "退货");

	private Integer key;
	private String desc;

	BehaviorEnum(Integer key, String desc) {
		this.key = key;
		this.desc = desc;
	}

	public Integer getKey() {
		return key;
	}

	public String getDesc() {
		return desc;
	}

	public static boolean validateOrderType(Integer key) {
		BehaviorEnum[] arr = BehaviorEnum.values();
		for (BehaviorEnum tmp : arr) {
			if (tmp.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}
}
