package cn.farwalker.ravv.service.payment.withdrawapply.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;
/**
 *
 * 提现状态
 * @author Chensl
 *
 */
public enum WithdrawStatusEnum implements IEnumJsons{
	/**待审核*/
	Pending_Audit("pending_audit", "待审核"),
	/**提现成功*/
	Withdraw_Succeed("withdraw_succeed", "提现成功"),
	/**提现失败*/
	Withdraw_Fail("withdraw_fail", "提现失败");

	WithdrawStatusEnum(String key, String lable) {
		this.key = key;
		this.lable = lable;
	}

	private String key;
	private String lable;

	@Override
	public String getKey() {
		return key;
	}
	@Override
	public String getLabel() {
		return lable;
	}

	public boolean compare(String key) {
		return this.key.equals(key);
	}
	
	public static WithdrawStatusEnum getEnumByKey(String key) {
		WithdrawStatusEnum[] all = WithdrawStatusEnum.values();
		for(WithdrawStatusEnum a : all) {
			if(a.getKey().equals(key)) {
				return a;
			}
		}
		return null;
	}
}