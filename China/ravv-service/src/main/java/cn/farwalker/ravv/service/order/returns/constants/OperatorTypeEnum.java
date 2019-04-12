package cn.farwalker.ravv.service.order.returns.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;

/**
 * 损坏程度
 *
 */
public enum OperatorTypeEnum implements IEnumJsons {
	/**
	 * 客服
	 */
	server("server", "客服"),

	/**
	 * 仓库管理员
	 */
	wearhouse("wearhouse", "仓库管理员"),
	
	
	user("user", "会员");

	private String key;
	private String label;

	OperatorTypeEnum(String key, String label) {
		this.key = key;
		this.label = label;
	}

	public String getKey() {
		return key;
	}

	public String getLabel() {
		return label;
	}

}
