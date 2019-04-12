package cn.farwalker.ravv.service.member.basememeber.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;

public enum ApplyStatusEnum implements IEnumJsons {
	ADUITING("aduiting", "审核中"),
	ADUITED("aduited", "审核通过"),
	FAIL("fail", "审核不通过");

	ApplyStatusEnum(String key, String desc) {
		this.key = key;
		this.desc = desc;
	}

	private String key = "unpay";
	private String desc;

	@Override
	public String getKey() {
		return key;
	}
 
	public boolean compare(String key_) {
		return this.key.equals(key_);
	}

	@Override
	public String getLabel() { 
		return desc;
	} 
}
