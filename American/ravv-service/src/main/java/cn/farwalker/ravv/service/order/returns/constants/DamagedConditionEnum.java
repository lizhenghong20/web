package cn.farwalker.ravv.service.order.returns.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;

/**
 * 损坏程度
 *
 */
public enum DamagedConditionEnum implements IEnumJsons {
	/**
	 * 代销
	 */
	undamaged("undamaged", "未损坏"),

	/**
	 * 分销
	 */
	partialDamage ("partialDamage", "部分损坏"),

	/**
	 * 普通订单
	 */
	completeDamage("completeDamage", "完全损坏");

	private String key;
	private String label;

	DamagedConditionEnum(String key, String label) {
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
		DamagedConditionEnum[] arr = DamagedConditionEnum.values();
		for (DamagedConditionEnum tmp : arr) {
			if (tmp.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}
}
