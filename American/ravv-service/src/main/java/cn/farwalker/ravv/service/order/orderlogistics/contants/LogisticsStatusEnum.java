package cn.farwalker.ravv.service.order.orderlogistics.contants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;

/**
 * 物流状态
 * @author Administrator
 *
 */
public enum LogisticsStatusEnum  implements IEnumJsons{
	NONE("NONE","未发货"),SendGoods("SendGoods","发货"),TranSport("TranSport","运输中"),Sign("Sign","签收");
	
	//,ReturnGoods("ReturnGoods","退货");
	private final String key;
	private final String label;

	private LogisticsStatusEnum(String key, String label) {
		this.key = key;
		this.label = label;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return getKey();
	}
}
