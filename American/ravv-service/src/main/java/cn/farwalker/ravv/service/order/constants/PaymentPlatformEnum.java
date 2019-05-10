package cn.farwalker.ravv.service.order.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;
import com.alibaba.fastjson.annotation.JSONType;

/**
 * 支付平台类型(微信、支付宝)
 * @author Administrator
 *
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = PaymentPlatformEnumDeser.class)
public enum PaymentPlatformEnum implements IEnumJsons{
	/** 微信 * /
	WEIXIN("WEIXIN", "微信"),
	/ **支付宝* /
	ALIPAY("ALIPAY", "支付宝"),
	/ **预存款* /
	DEPOSIT("deposit","预存款"),
	/ **银联支付* /
	UNIONPAY("unionpay", "银联支付")*/
	PayPal("PayPal","PayPal"),
	Stripe("Stripe", "Stripe"),
	Advance("Advance","账户余额"),
	;
	private final String key,label;
	PaymentPlatformEnum(String status, String name) {
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

	public static PaymentPlatformEnum getEnumByKey(String key) {
		PaymentPlatformEnum[] all = PaymentPlatformEnum.values();
		for(PaymentPlatformEnum a : all) {
			if(a.getKey().equals(key)) {
				return a;
			}
		}
		return null;
	}

	public static PaymentPlatformEnum value(String key){
		if("PayPal".equals(key)){
			return PayPal;
		} else if("Advance".equals(key)){
			return Advance;
		} else if("Stripe".equals(key)){
			return Stripe;
		}
		return null;
	}

}
