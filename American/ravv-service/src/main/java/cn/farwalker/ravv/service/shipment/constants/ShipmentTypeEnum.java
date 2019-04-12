package cn.farwalker.ravv.service.shipment.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;

/**
 * 运费模板类型
 * @author Administrator
 *
 */
public enum ShipmentTypeEnum implements IEnumJsons{
	General("general","通用"),Category("category","商品类目"),Goods("goods","指定商品"), Default("default", "默认");
	private final String key;
	private final String label;

	private ShipmentTypeEnum(String key, String label) {
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
