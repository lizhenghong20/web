package cn.farwalker.ravv.service.order.orderlogistics.contants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;

/**
 * 运费支付方式
 * @author Administrator
 *
 */
public enum LogisticsPaymentEnum implements IEnumJsons{
	COD("COD","到付(买家支付)"),SELLER("SELLER","卖家支付");//,BUYER("BUYER","买家支付")
	
	//,ReturnGoods("ReturnGoods","退货");
	private final String key;
	private final String label;

	private LogisticsPaymentEnum(String key, String label) {
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
}
