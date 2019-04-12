package cn.farwalker.ravv.service.goods.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;

public enum GoodsSkuDefTypeEnum  implements IEnumJsons{
	SPEC("spec", "按规格定义"),
	CUSTOM("custom", "自定义");

	GoodsSkuDefTypeEnum(String key, String desc) {
		this.key = key;
		this.desc = desc;
	}

	private String key = "unpay";
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
	
	public static GoodsSkuDefTypeEnum getEnumByKey(String key) {
		GoodsSkuDefTypeEnum[] all = GoodsSkuDefTypeEnum.values();
		for(GoodsSkuDefTypeEnum a : all) {
			if(a.getKey().equals(key)) {
				return a;
			}
		}
		return null;
	}
}
