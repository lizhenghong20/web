package cn.farwalker.ravv.service.order.operationlog.contants;

public enum ResultEnum {
	/**
	 * 创建订单
	 */
	CREATEORDER(1, "创建订单成功"),

	/**
	 * 取消订单
	 */
	DISTRIBUTION(2, "取消订单成功"),

	/**
	 * 退货
	 */
	REFUNDGOODS(3, "退货成功");

	private Integer key;
	private String desc;

	ResultEnum(Integer key, String desc) {
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
		ResultEnum[] arr = ResultEnum.values();
		for (ResultEnum tmp : arr) {
			if (tmp.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}
}
