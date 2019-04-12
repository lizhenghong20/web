package cn.farwalker.ravv.service.web.searchkeyhistory.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;
/**
 *
 * 搜索类型
 * @author chensl
 *
 */
public enum SearchTypeEnum implements IEnumJsons{
	/**商品*/
	Goods("goods", "商品");

	SearchTypeEnum(String key, String lable) {
		this.key = key;
		this.lable = lable;
	}

	private String key;
	private String lable;

	@Override
	public String getKey() {
		return key;
	}
	@Override
	public String getLabel() {
		return lable;
	}

	public boolean compare(String key) {
		return this.key.equals(key);
	}
	
	public static SearchTypeEnum getEnumByKey(String key) {
		SearchTypeEnum[] all = SearchTypeEnum.values();
		for(SearchTypeEnum a : all) {
			if(a.getKey().equals(key)) {
				return a;
			}
		}
		return null;
	}
}