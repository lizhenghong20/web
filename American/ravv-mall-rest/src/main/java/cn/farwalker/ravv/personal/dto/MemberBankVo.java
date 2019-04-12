package cn.farwalker.ravv.personal.dto;

/**
 * 银行卡信息
 * @author chensl
 *
 */
public class MemberBankVo {
	
	/** 银行卡id */
	private Long id;
	
	/** 银行名称 */
	private String bankName;
	
	/** 银行卡类型 */
	private String bankType;
	
	/** 银行卡卡号 */
	private String cardNumber;
	
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
