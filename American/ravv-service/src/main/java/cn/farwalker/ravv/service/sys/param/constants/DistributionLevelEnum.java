package cn.farwalker.ravv.service.sys.param.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;
/**
 *
 * 分销等级
 * @author Administrator
 *
 */
public enum DistributionLevelEnum implements IEnumJsons{
	/**一级分销*/
	First_Distribution("first_distribution", "一级分销"),
	/**二级分销*/
	Second_Distribution("second_distribution", "二级分销"),
	/**三级分销*/
	Third_Distribution("third_distribution", "三级分销");
//	/**四级分销*/
//	fourth_Distribution("fourth_distribution", "四级分销");

	DistributionLevelEnum(String key, String lable) {
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
	
	public static DistributionLevelEnum getEnumByKey(String key) {
		DistributionLevelEnum[] all = DistributionLevelEnum.values();
		for(DistributionLevelEnum a : all) {
			if(a.getKey().equals(key)) {
				return a;
			}
		}
		return null;
	}
}