package cn.farwalker.ravv.service.order.orderlogistics.contants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;

/**物流方式*/
public enum LogisticsTypeEnum implements IEnumJsons{ 
	AIRLIFT("AIRLIFT","空运"),LandCarriage("LandCarriage","陆运");
	private final String key;
	private final String label;

	private LogisticsTypeEnum(String key, String label) {
		this.key = key;
		this.label = label;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return getKey();
	}

}
