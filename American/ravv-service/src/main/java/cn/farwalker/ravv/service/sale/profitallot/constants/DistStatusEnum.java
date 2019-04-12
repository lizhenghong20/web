package cn.farwalker.ravv.service.sale.profitallot.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;
/**
 *
 * 分销状态
 * @author Chensl
 *
 */
public enum DistStatusEnum implements IEnumJsons{
	/**待返现*/
	Pending_Return("pending_return", "待返现"),
	/**已返现*/
	Returned("returned", "已返现"),
	/**已取消*/
	Cancel("cancel", "已取消");

	DistStatusEnum(String key, String lable) {
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
	
	public static DistStatusEnum getEnumByKey(String key) {
		DistStatusEnum[] all = DistStatusEnum.values();
		for(DistStatusEnum a : all) {
			if(a.getKey().equals(key)) {
				return a;
			}
		}
		return null;
	}
}