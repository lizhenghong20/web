package cn.farwalker.ravv.service.member.basememeber.model;

/**
 * 会员简略信息，用户客户端分销关系展示
 * @author chensl
 *
 */
public class MemberSimpleInfoVo {
	
	/** 会员id  */
	private String id;
	
	/** 会员头像  */
	private String avator;
	
	/** 会员名称  */
	private String firstname;
	
	/** 会员姓氏  */
	private String lastname;
	
	/** 会员推荐码  */
	private String referralCode;
	
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReferralCode() {
		return referralCode;
	}

	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}
	
}
