package cn.farwalker.ravv.service.order.ordergoods.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;
import com.alibaba.fastjson.annotation.JSONType;

/**
 * 订单的商品状态
 * @author Administrator 
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = OrderGoodsStatusEnumDeser.class)
public enum OrderGoodsStatusEnum implements IEnumJsons{
	SALE("sale", "销售状态"),
	REFUND("refund", "已退款"),
	REGOODS("regoods", "退货"),
	
	REFUND_PAR("refund_par", "部分退款"),
	REGOODS_PAR("regoods_par", "部分退货");

	OrderGoodsStatusEnum(String key, String desc) {
		this.key = key;
		this.desc = desc;
	}

	private final String key ;
	private final String desc;

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getLabel() {
		return desc;
	}

	public boolean compare(String key_) {
		return this.key.equals(key_);
	}

	public static OrderGoodsStatusEnum value(String key){
		if("sale".equals(key)){
			return SALE;
		}
		return null;
	}
}
