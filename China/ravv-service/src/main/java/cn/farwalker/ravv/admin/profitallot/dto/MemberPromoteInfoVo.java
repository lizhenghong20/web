package cn.farwalker.ravv.admin.profitallot.dto;

import java.math.BigDecimal;

/**
 * 会员分润信息
 * @author chensl
 *
 */
public class MemberPromoteInfoVo {
	
	/** 会员头像  */
	private String avator;
	
	/** 会员名称  */
	private String firstname;
	
	/** 会员姓氏  */
	private String lastname;

	/** 会员推荐码  */
	private String referralCode;
	
	/** 会员账户余额  */
	private BigDecimal advance;
	
	/** 分销累计金额  */
	private BigDecimal distributionTotal;
	
	/** 分销待返现金额 */
	private BigDecimal awaitingAmount;
	
	public String getAvator() {
		return avator;
	}

	public void setAvator(String avator) {
		this.avator = avator;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getReferralCode() {
		return referralCode;
	}

	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}

	public BigDecimal getDistributionTotal() {
		return distributionTotal;
	}

	public void setDistributionTotal(BigDecimal distributionTotal) {
		this.distributionTotal = distributionTotal;
	}

	public BigDecimal getAwaitingAmount() {
		return awaitingAmount;
	}

	public void setAwaitingAmount(BigDecimal awaitingAmount) {
		this.awaitingAmount = awaitingAmount;
	}

	public BigDecimal getAdvance() {
		return advance;
	}

	public void setAdvance(BigDecimal advance) {
		this.advance = advance;
	}
	
}
