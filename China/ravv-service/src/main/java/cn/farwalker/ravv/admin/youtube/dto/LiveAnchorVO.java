package cn.farwalker.ravv.admin.youtube.dto;

import com.cangwu.frame.orm.core.annotation.LoadJoinValue;

import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.youtube.liveanchor.model.YoutubeLiveAnchorBo;

public class LiveAnchorVO extends YoutubeLiveAnchorBo{
	
	private static final long serialVersionUID = 1L;
	
	/**会员名称*/
	private String anchorMemberName;
	
	/**会员名字*/
	private String firstname;
	
	/**会员姓氏*/
	private String lastname;
	
	/**粉丝数量**/
	private Integer liveFollowNum;

	public String getAnchorMemberName() {
		return anchorMemberName;
	}

	@LoadJoinValue(by="anchorMemberId",table=MemberBo.TABLE_NAME,joinfield="id")
	public void setAnchorMemberName(MemberBo memberBo) {
		if(null != memberBo) {
			this.anchorMemberName = memberBo.getName();
			this.firstname = memberBo.getFirstname();
			this.lastname = memberBo.getLastname();
		}
	}

	public Integer getLiveFollowNum() {
		return liveFollowNum;
	}

	public void setLiveFollowNum(Integer liveFollowNum) {
		this.liveFollowNum = liveFollowNum;
	}

	public String getFirstname() {
		return firstname;
	}

//	public void setFirstname(String firstname) {
//		this.firstname = firstname;
//	}

	public String getLastname() {
		return lastname;
	}

//	public void setLastname(String lastname) {
//		this.lastname = lastname;
//	}

}
