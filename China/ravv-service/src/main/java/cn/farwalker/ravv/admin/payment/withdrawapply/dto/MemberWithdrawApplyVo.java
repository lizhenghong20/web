package cn.farwalker.ravv.admin.payment.withdrawapply.dto;

import com.cangwu.frame.orm.core.annotation.LoadJoinValue;

import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.payment.withdrawapply.model.MemberWithdrawApplyBo;

public class MemberWithdrawApplyVo extends MemberWithdrawApplyBo{

	private static final long serialVersionUID = 1L;
	
	/** 会员名称 */
	private String firstname;
	
	/** 会员姓氏 */
	private String lastname;

	public String getFirstname() {
		return firstname;
	}

	@LoadJoinValue(by="memberId",table=MemberBo.TABLE_NAME,joinfield="id")
	public void setFirstname(MemberBo memberBo) {
		this.firstname = memberBo.getFirstname();
		this.lastname = memberBo.getLastname();
	}

	public String getLastname() {
		return lastname;
	}

//	public void setLastname(String lastname) {
//		this.lastname = lastname;
//	}
	
}
