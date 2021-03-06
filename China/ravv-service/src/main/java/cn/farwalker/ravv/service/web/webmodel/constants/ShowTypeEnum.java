package cn.farwalker.ravv.service.web.webmodel.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;

/** 显示类型(首页显示,某个页面显示,不显示)*/
public enum ShowTypeEnum implements IEnumJsons{
	HOME("home","首页显示"),STAGE("stage","二级页面显示");
	private final String key;
	private final String label;

	private ShowTypeEnum(String key, String label) {
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
