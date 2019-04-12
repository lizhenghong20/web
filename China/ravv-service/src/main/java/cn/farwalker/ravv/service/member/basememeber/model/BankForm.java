package cn.farwalker.ravv.service.member.basememeber.model;

import java.util.Date;

public class BankForm {
	/**
	 * 银行卡ID
	 */
	private Long id;
	/**
	 * 会员ID
	 */
	private Long memberId;
	
	/**
	 * 户主姓名
	 */
	private String memberName;
	/**
	 * 银行名称
	 */
	private String bankName;
	/**
	 * 银行卡类型
	 */
	private Integer bankType;
	/**
	 * 银行卡号
	 */
	private String cardNumber;
	/**
	 * 绑定时间
	 */
	private Date bindTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Integer getBankType() {
		return bankType;
	}

	public void setBankType(Integer bankType) {
		this.bankType = bankType;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Date getBindTime() {
		return bindTime;
	}

	public void setBindTime(Date bindTime) {
		this.bindTime = bindTime;
	}

}
