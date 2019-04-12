package cn.farwalker.ravv.service.category.property.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;
/**
 *
 * 属性类别(SKU属性、普通属性...)
 * @author Administrator
 *
 */
public enum PropertyTypeEnum implements IEnumJsons{
	/**普通属性*/
	NORMAL("normal", "普通类目属性"),
	/**SKU属性*/
	STANDARD("standard", "规格属性");

	PropertyTypeEnum(String key, String desc) {
		this.key = key;
		this.desc = desc;
	}

	private String key;
	private String desc;

	@Override
	public String getKey() {
		return key;
	}
	@Override
	public String getLabel() {
		return desc;
	}

	public boolean compare(String key) {
		return this.key.equals(key);
	}
	
	public static PropertyTypeEnum getEnumByKey(String key) {
		PropertyTypeEnum[] all = PropertyTypeEnum.values();
		for(PropertyTypeEnum a : all) {
			if(a.getKey().equals(key)) {
				return a;
			}
		}
		return null;
	}
}