package cn.farwalker.ravv.service.payment.withdrawapply.model;

import java.math.BigDecimal;

/**
 * 提现手续费和实际可提现金额
 * @author chensl
 *
 */
public class WithdrawPayCostVo {
	
	/** 提现手续费 */
	private BigDecimal actualWithdraw;
	
	/** 实际可提现金额 */
	private BigDecimal paycost;

	public BigDecimal getActualWithdraw() {
		return actualWithdraw;
	}

	public void setActualWithdraw(BigDecimal actualWithdraw) {
		this.actualWithdraw = actualWithdraw;
	}

	public BigDecimal getPaycost() {
		return paycost;
	}

	public void setPaycost(BigDecimal paycost) {
		this.paycost = paycost;
	}
	
}
