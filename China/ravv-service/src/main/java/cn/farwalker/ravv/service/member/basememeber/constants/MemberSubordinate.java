package cn.farwalker.ravv.service.member.basememeber.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;
/**
 *
 * 会员推荐下级等级
 * @author Chensl
 *
 */
public enum MemberSubordinate implements IEnumJsons{
	/**第一级下级推荐人*/
	Frist_Subordinate("frist_subordinate", "第一级下级推荐人"),
	/**第二级下级推荐人*/
	Second_Subordinate("second_subordinate", "第二级下级推荐人"),
	/**第三级下级推荐人*/
	Third_Subordinate("third_subordinate", "第三级下级推荐人");

	MemberSubordinate(String key, String lable) {
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
	
	public static MemberSubordinate getEnumByKey(String key) {
		MemberSubordinate[] all = MemberSubordinate.values();
		for(MemberSubordinate a : all) {
			if(a.getKey().equals(key)) {
				return a;
			}
		}
		return null;
	}
}