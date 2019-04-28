package cn.farwalker.waka.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;
import com.alibaba.fastjson.annotation.JSONType;

@JSONType(serializeEnumAsJavaBean = true, deserializer = SexEnumDeser.class)
public enum SexEnum implements IEnumJsons {
	MALE("M", "男"), FEMALE("F", "女");//

	private final String key;
	private final String desc;

	private SexEnum(String key, String desc) {
		this.key = key;
		this.desc = desc;
	}

	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public String getLabel() {
		return this.desc;
	}

	public static SexEnum getEnumByKey(String key) {
		SexEnum[] sexs = SexEnum.values();
		for (SexEnum sex : sexs) {
			if (sex.key.equals(key)) {
				return sex;
			}
		}
		return null;
	}

	public static String getLabelByKey(String key) {
		if (key == null) {
			return "";
		} else {
			for (SexEnum s : SexEnum.values()) {
				if (s.getKey().equals(key)) {
					return s.getLabel();
				}
			}
			return "";
		}
	}

	public static SexEnum value(String key){
		if("F".equals(key)){
			return FEMALE;
		} else if("M".equals(key)){
			return MALE;
		}
		return null;
	}
}
