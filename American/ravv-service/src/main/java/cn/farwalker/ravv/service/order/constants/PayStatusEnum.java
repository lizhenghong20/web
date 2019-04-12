package cn.farwalker.ravv.service.order.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;
import com.alibaba.fastjson.annotation.JSONType;

/**
 * 订单支付状态
 * @author Administrator
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = PayStatusEnumDeser.class)
public enum PayStatusEnum implements IEnumJsons{
	
	/** 未支付 */
	UNPAY("UNPAY", "未支付"),
	/** 已支付*/
	PAID("PAID", "已支付"),
	//已退款
	REFUND("REFUND","已退款"),
	;
	PayStatusEnum(String status, String name) {
		this.key = status;
		this.label = name;
	}

	private final String key;
	private final String label;

	@Override
	public String getKey() {
		return key;
	}


	@Override
	public String getLabel() {
		return label;
	}

	public static PayStatusEnum value(String key){
		if("UNPAY".equals(key)){
			return UNPAY;
		} else if("PAID".equals(key)){
			return PAID;
		} else if("REFUND".equals(key)){
			return REFUND;
		}
		return null;
	}
}