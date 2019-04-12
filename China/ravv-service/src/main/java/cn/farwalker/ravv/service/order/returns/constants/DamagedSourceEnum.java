package cn.farwalker.ravv.service.order.returns.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;

/**
 * 损坏程度
 *
 */
public enum DamagedSourceEnum implements IEnumJsons{
	/**
	 * 代销
	 */
	ourReasons("ourReasons", "我方原因"),

	/**
	 * 分销
	 */
	transportationReasons("transportationReasons", "运输原因"),

	/**
	 * 普通订单
	 */
	buyerReasons("buyerReasons", "买家原因损坏");

	private String key;
	private String label;

	DamagedSourceEnum(String key, String label) {
		this.key = key;
		this.label = label;
	}

	public String getKey() {
		return key;
	}

	public String getLabel() {
		return label;
	}

	public static boolean validateOrderType(String key) {
		DamagedSourceEnum[] arr = DamagedSourceEnum.values();
		for (DamagedSourceEnum tmp : arr) {
			if (tmp.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}
}
