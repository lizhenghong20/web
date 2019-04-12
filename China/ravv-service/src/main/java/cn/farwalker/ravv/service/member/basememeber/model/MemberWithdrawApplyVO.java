package cn.farwalker.ravv.service.member.basememeber.model;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;

public class MemberWithdrawApplyVO {
	/**
     * 提现申请ID
     */
    private Long id;
    /**
     * 会员ID
     */
    private Long memberId;
    /**
     * 银行卡ID
     */
    private Long bankId;
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
     * 申请状态
     */
    private String applyStatus;
    
    /**
     * 申请编号
     */
    private String applyNo;
    /**
     * 申请提现金额
     */
    private BigDecimal applyFee;
    /**
     * 手续费
     */
    private BigDecimal paycost;
    /**
     * 手续费百分比
     */
    private BigDecimal percentage;
    /**
     * 申请时间
     */
    private Date applyTime;
    /**
     * 审核时间
     */
    private Date checkTime;
    
    /**
     * 完成提现时间
     */
    private Date withdrawTime;

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

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
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

	public String getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}

	public String getApplyNo() {
		return applyNo;
	}

	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}

	public BigDecimal getApplyFee() {
		return applyFee;
	}

	public void setApplyFee(BigDecimal applyFee) {
		this.applyFee = applyFee;
	}

	public BigDecimal getPaycost() {
		return paycost;
	}

	public void setPaycost(BigDecimal paycost) {
		this.paycost = paycost;
	}

	public BigDecimal getPercentage() {
		return percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public Date getWithdrawTime() {
		return withdrawTime;
	}

	public void setWithdrawTime(Date withdrawTime) {
		this.withdrawTime = withdrawTime;
	}
    
}
