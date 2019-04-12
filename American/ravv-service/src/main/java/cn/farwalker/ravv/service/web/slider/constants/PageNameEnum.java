package cn.farwalker.ravv.service.web.slider.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;

/**
 * 轮播的页面枚举
 * 
 * @author Administrator
 */
public enum PageNameEnum implements IEnumJsons {
	HOME("home", "首页"), CATEGORY("category", "商品分类"), LIVE("live", "直播");

	private final String key;
	private final String label;

	private PageNameEnum(String key, String label) {
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

	public static PageNameEnum getEnumByKey(String key) {
		PageNameEnum[] all = PageNameEnum.values();
		for(PageNameEnum a : all) {
			if(a.getKey().equals(key)) {
				return a;
			}
		}
		return null;
	}
}
