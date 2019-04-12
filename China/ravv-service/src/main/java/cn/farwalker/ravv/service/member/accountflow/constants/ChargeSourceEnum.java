package cn.farwalker.ravv.service.member.accountflow.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsonn;
/**
 *
 * 交易流水来源
 * @author Chensl
 *
 */
public enum ChargeSourceEnum implements IEnumJsonn{
	/**手工充值*/
	Manual(1, "手工充值"),
	/**注册充值*/
	Register(2, "注册充值"),
	/**订单（订单来源的金额为负数）*/
	Order(3, "订单"),	
	/**提现*/
	Withdraw(4, "提现"),
	/**分销*/
	Distribution(5, "分销");

	private Integer key;
	private String lable;
	
	ChargeSourceEnum(int key, String lable) {
		this.key = Integer.valueOf(key);
		this.lable = lable;
	}

	@Override
	public Integer getKey() {
		return key;
	}
	@Override
	public String getLabel() {
		return lable;
	}
	
	public static ChargeSourceEnum getEnumByKey(Integer key) {
		ChargeSourceEnum[] all = ChargeSourceEnum.values();
		for(ChargeSourceEnum a : all) {
			if(a.getKey().equals(key)) {
				return a;
			}
		}
		return null;
	}
}