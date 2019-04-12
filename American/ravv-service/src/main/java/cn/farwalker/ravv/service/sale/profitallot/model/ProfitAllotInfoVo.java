package cn.farwalker.ravv.service.sale.profitallot.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 分销列表信息
 * @author chensl
 *
 */
public class ProfitAllotInfoVo {

	/** 会员id  */
	private String id;
	
	/** 会员头像  */
	private String avator;
	
	/** 会员名称  */
	private String firstname;
	
	/** 会员姓氏  */
	private String lastname;
	
	/** 分销金额  */
	private BigDecimal amt;
	
	/** 到账时间  */
	private String returnedTime;
	
	/** 实际到账时间  */
	private Date gmtModified;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	public String getReturnedTime() {
		return returnedTime;
	}

	public void setReturnedTime(String returnedTime) {
		this.returnedTime = returnedTime;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}
	
}
