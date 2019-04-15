package cn.farwalker.ravv.service.base.area.constant;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;
import com.alibaba.fastjson.annotation.JSONType;

/**
 * 国家编码
 * @author chensl
 *
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = CountryCodeEnumDeser.class)
public enum CountryCodeEnum implements IEnumJsons{
	/**
	 * 中国
	 */
	CHINA("CHN", "China"),

	/**
	 * 美国
	 */
	USA("USA", "USA"),

	/**
	 * 日本
	 */
	JAPAN("JPN", "Japan");

	private String key;
	private String desc;

	CountryCodeEnum(String key, String desc) {
		this.key = key;
		this.desc = desc;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getLabel() {
		return desc;
	}
	/** @deprecated */ 
	public String getDesc() {
		return getLabel();
	}
	public static boolean validateOrderType(String key) {
		CountryCodeEnum[] arr = CountryCodeEnum.values();
		for (CountryCodeEnum tmp : arr) {
			if (tmp.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}

	public static CountryCodeEnum value(String key){
		if("USA".equals(key)){
			return USA;
		} else if("CHN".equals(key)){
			return CHINA;
		}
		return null;
	}
}
