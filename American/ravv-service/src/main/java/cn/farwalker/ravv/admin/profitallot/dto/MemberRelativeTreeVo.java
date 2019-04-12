package cn.farwalker.ravv.admin.profitallot.dto;

import com.baomidou.mybatisplus.plugins.Page;

import cn.farwalker.ravv.service.member.basememeber.model.MemberSimpleInfoVo;

/**
 * 会员上下级推荐人
 * @author chensl
 *
 */
public class MemberRelativeTreeVo {
	
	/** 上级推荐人 */
	private MemberSimpleInfoVo supurior;
	
	/** 第一级下级推荐人 */
	private Page<MemberSimpleInfoVo> fristSubordinate;
	
	/** 第二级下级推荐人 */
	private Page<MemberSimpleInfoVo> secondSubordinate;
	
	/** 第三级下级推荐人 */
	private Page<MemberSimpleInfoVo> thirdSubordinate;
	
	public MemberSimpleInfoVo getSupurior() {
		return supurior;
	}

	public void setSupurior(MemberSimpleInfoVo supurior) {
		this.supurior = supurior;
	}

	public Page<MemberSimpleInfoVo> getFristSubordinate() {
		return fristSubordinate;
	}

	public void setFristSubordinate(Page<MemberSimpleInfoVo> fristSubordinate) {
		this.fristSubordinate = fristSubordinate;
	}

	public Page<MemberSimpleInfoVo> getSecondSubordinate() {
		return secondSubordinate;
	}

	public void setSecondSubordinate(Page<MemberSimpleInfoVo> secondSubordinate) {
		this.secondSubordinate = secondSubordinate;
	}

	public Page<MemberSimpleInfoVo> getThirdSubordinate() {
		return thirdSubordinate;
	}

	public void setThirdSubordinate(Page<MemberSimpleInfoVo> thirdSubordinate) {
		this.thirdSubordinate = thirdSubordinate;
	}

}
