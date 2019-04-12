package cn.farwalker.ravv.personal.dto;

import java.math.BigDecimal;
import java.util.Date;

import cn.farwalker.ravv.service.payment.withdrawapply.constants.WithdrawStatusEnum;

/**
 * 提现记录列表
 * @author chensl
 *
 */
public class WithdrawApplyVo {
	
	/** 提现记录id */
	private Long id;
	
	/** 提现金额 */
	private BigDecimal applyFee;
	
	/** 申请提现时间  */
	private Date applyTime;
	
	/** 申请提现状态  */
	private WithdrawStatusEnum withdrawStatus;
	
	/** 申请提现编号  */
	private WithdrawStatusEnum applyNo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getApplyFee() {
		return applyFee;
	}

	public void setApplyFee(BigDecimal applyFee) {
		this.applyFee = applyFee;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public WithdrawStatusEnum getWithdrawStatus() {
		return withdrawStatus;
	}

	public void setWithdrawStatus(WithdrawStatusEnum withdrawStatus) {
		this.withdrawStatus = withdrawStatus;
	}

	public WithdrawStatusEnum getApplyNo() {
		return applyNo;
	}

	public void setApplyNo(WithdrawStatusEnum applyNo) {
		this.applyNo = applyNo;
	}
	
}
