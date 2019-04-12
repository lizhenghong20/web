package cn.farwalker.ravv.service.order.returns.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;
import com.alibaba.fastjson.annotation.JSONType;

/**
 * 退货类型
 * 
 * @author Administrator
 *
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = ReturnsTypeEnumDeser.class)
public enum ReturnsTypeEnum implements IEnumJsons {
	ChangeGoods("ChangeGoods", "换货"), ReGoods("ReGoods", "退款又退货"), Refund("refund", "仅退款");
	private final String label, key;

	private ReturnsTypeEnum(String key, String label) {
		this.key = key;
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getKey() {
		return key;
	}

	public static ReturnsTypeEnum value(String key){
		if("ChangeGoods".equals(key)){
			return ChangeGoods;
		} else if("ReGoods".equals(key)){
			return ReGoods;
		} else if("refund".equals(key)){
			return Refund;
		}
		return null;
	}
}
