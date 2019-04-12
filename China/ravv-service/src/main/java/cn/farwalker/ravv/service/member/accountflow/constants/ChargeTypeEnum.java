package cn.farwalker.ravv.service.member.accountflow.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsonn;

/**
 *
 * 交易流水类型
 * @author Chensl
 *
 */
public enum ChargeTypeEnum implements IEnumJsonn{
	/**现金(支付宝或银联等)*/
	Cash(1, "现金(支付宝或银联等)"),
	/**现金券(总额)*/
	Cash_Ticket(2, "现金券(总额)"),
	/**余额*/
	Advance(3, "余额"),	
	/**返现(总额)*/
	Cashback(4, "返现(总额)"),	
	/**提现*/
	Withdraw(5, "提现"),
	/**分销*/
	Distribution(6, "分销");

	private Integer key;
	private String lable;
	
	ChargeTypeEnum(int key, String lable) {
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

	public static boolean validateOrderType(Integer key) {
		ChargeTypeEnum[] arr = ChargeTypeEnum.values();
		for (ChargeTypeEnum tmp : arr) {
			if (tmp.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}
}