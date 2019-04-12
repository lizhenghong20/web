package cn.farwalker.ravv.service.member.basememeber.model;

import com.cangwu.frame.orm.core.annotation.LoadJoinValue;

import cn.farwalker.ravv.service.member.level.model.MemberLevelBo;
import cn.farwalker.ravv.service.member.pam.member.model.PamMemberBo;

/**import more*/
/**
 * member表
 * @author generateModel.java
 */
public class MemberVo extends MemberBo {

	private static final long serialVersionUID = 3798903713445497418L;
	
	/**
	 * 会员等级名称
	 */
	private String buyerLevelName;
	
	/**
	 * 会员账号
	 */
	private String emailAccount;

	public String getBuyerLevelName() {
		return buyerLevelName;
	}

	@LoadJoinValue(by = "buyerLevelId", table = MemberLevelBo.TABLE_NAME, joinfield = "id", returnfield = "name")
	public void setBuyerLevelName(String buyerLevelName) {
		this.buyerLevelName = buyerLevelName;
	}

	public String getEmailAccount() {
		return emailAccount;
	}

	@LoadJoinValue(by = "id", table = PamMemberBo.TABLE_NAME, joinfield = "memberId", returnfield = "emailAccount")
	public void setEmailAccount(String emailAccount) {
		this.emailAccount = emailAccount;
	}
	 
 
}
