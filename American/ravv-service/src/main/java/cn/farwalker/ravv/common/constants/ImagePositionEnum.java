package cn.farwalker.ravv.common.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;

public enum ImagePositionEnum implements IEnumJsons{
	TITLE("title", "标题轮播图片"),
	DETAIL("detail", "详情图"),
	MAJOR("major", "主图");

	private final String key;
	private final String desc;
	ImagePositionEnum(String key, String desc) {
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

	public boolean compare(String key) {
		return this.key.equals(key);
	}
	
	public static ImagePositionEnum getEnumByKey(String key) {
		ImagePositionEnum[] all = ImagePositionEnum.values();
		for(ImagePositionEnum a : all) {
			if(a.getKey().equals(key)) {
				return a;
			}
		}
		return null;
	}
}
