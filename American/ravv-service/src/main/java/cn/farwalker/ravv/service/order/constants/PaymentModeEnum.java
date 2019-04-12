package cn.farwalker.ravv.service.order.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;
import com.alibaba.fastjson.annotation.JSONType;

/**
 * 支付方式(在线支付、货到付款)
 * @author Administrator
 *
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = PaymentModeEnumDeser.class)
public enum PaymentModeEnum implements IEnumJsons{
	/** 微信 */
	ONLINE("ONLINE", "在线支付"),
	/**货到付款*/
	COD("COD", "货到付款");

	private final String key,label;
	PaymentModeEnum(String status, String name) {
		this.key = status;
		this.label = name;
	}
	@Override
	public String getKey() {
		return key;
	}
	@Override
	public String getLabel() {
		return label;
	}

	public static PaymentModeEnum value(String key){
		if("ONLINE".equals(key)){
			return ONLINE;
		} else if("COD".equals(key)){
			return COD;
		}
		return null;
	}

}
