package cn.farwalker.ravv.service.goods.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;

public enum SupportTradeEnum  implements IEnumJsons{
	ALL("all", "全部"),
	COD("cod", "货到付款"),
	ALIPAY("alipay", "支付宝"), 
	WXPAY("wxpay", "微信支付"),;
	
	private String key;
	private String desc;
	
	SupportTradeEnum(String key, String desc){
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

	@Override
	public String toString() {
		return super.toString();// EnumManager.toString(this);
	}
}
