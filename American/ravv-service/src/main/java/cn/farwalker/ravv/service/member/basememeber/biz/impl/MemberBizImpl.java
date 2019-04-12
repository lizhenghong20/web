package cn.farwalker.ravv.service.member.basememeber.biz.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import cn.farwalker.waka.core.WakaException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.farwalker.ravv.service.member.accountflow.biz.IMemberAccountFlowBiz;
import cn.farwalker.ravv.service.member.accountflow.constants.ChargeSourceEnum;
import cn.farwalker.ravv.service.member.accountflow.constants.ChargeTypeEnum;
import cn.farwalker.ravv.service.member.accountflow.model.MemberAccountFlowBo;
import cn.farwalker.ravv.service.member.basememeber.biz.IMemberBiz;
import cn.farwalker.ravv.service.member.basememeber.constants.MemberSubordinate;
import cn.farwalker.ravv.service.member.basememeber.dao.IMemberDao;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.member.basememeber.model.MemberSimpleInfoVo;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;
import org.springframework.transaction.annotation.Transactional;

/**
 * member表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
@Service
public class MemberBizImpl extends ServiceImpl<IMemberDao, MemberBo> implements IMemberBiz {

	@Resource
	private IMemberBiz memberBiz;

	@Resource
	private IMemberDao memberDao;

	@Resource
	private IMemberAccountFlowBiz memberAccountFlowBiz;

	@Override
	public MemberBo memberByReferral(String referralCode) {
		Wrapper<MemberBo> wrapper = new EntityWrapper<>();
		wrapper.eq(MemberBo.Key.referralCode.toString(), referralCode);
		return memberBiz.selectOne(wrapper);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Boolean addProfitToAdvance(Long memberId, Long orderId, BigDecimal money) {
		MemberBo member = memberBiz.selectById(memberId);
		if (null == member) {
			return false;
		}

		if (null != member.getAdvance()) {
			BigDecimal advance = member.getAdvance().add(money);
			member.setAdvance(advance);
		} else {
			member.setAdvance(money);
		}

		if (null != member.getDistributionTotal()) {
			BigDecimal distributionTotal = member.getDistributionTotal().add(money);
			member.setDistributionTotal(distributionTotal);
		} else {
			member.setDistributionTotal(money);
		}

		Boolean rs = memberBiz.updateById(member);

		// 增加账户流水记录
		if (rs) {
			MemberAccountFlowBo accountFlow = new MemberAccountFlowBo();
			accountFlow.setMemberId(member.getId());
			accountFlow.setAmt(money);
			accountFlow.setOperator("ravv");// TODO 系统名称未定，暂用ravv
			accountFlow.setChargetype(ChargeTypeEnum.Distribution);
			accountFlow.setChargesource(ChargeSourceEnum.Distribution);
			accountFlow.setSources(String.valueOf(orderId));

			memberAccountFlowBiz.insert(accountFlow);
		}

		return rs;
	}

	@Override
	public Page<MemberSimpleInfoVo> subordinateList(String referrerReferalCode, MemberSubordinate subordinate,
			Integer start, Integer size) {
		if (Tools.number.nullIf(start, 0) <= 0) {
			start = Integer.valueOf(0);
		}
		if (Tools.number.nullIf(size, 0) == 0) {
			size = Integer.valueOf(10);
		}

		Page<MemberSimpleInfoVo> relativePage = new Page<>(start, size);
		List<MemberSimpleInfoVo> subordinateInfoList = new ArrayList<>();

		if (subordinate == MemberSubordinate.Frist_Subordinate) {
			if (subordinate == MemberSubordinate.Frist_Subordinate) {
				// 获取第一级下级推荐人
				subordinateInfoList = memberDao.getFristSubordinate(relativePage, referrerReferalCode);
			} else if (subordinate == MemberSubordinate.Second_Subordinate) {
				// 获取第二级下级推荐人
				subordinateInfoList = memberDao.getSecondSubordinate(relativePage, referrerReferalCode);
			} else if (subordinate == MemberSubordinate.Third_Subordinate) {
				// 获取第三级下级推荐人
				subordinateInfoList = memberDao.getThirdSubordinate(relativePage, referrerReferalCode);
			}
		}
		
		if(Tools.collection.isNotEmpty(subordinateInfoList)) {
			for(MemberSimpleInfoVo subordinateInfo : subordinateInfoList) {
				subordinateInfo.setAvator(QiniuUtil.getFullPath(subordinateInfo.getAvator()));
			}
		}

		return relativePage.setRecords(subordinateInfoList);
	}

	@Override
	public MemberSimpleInfoVo getMemberSupurior(Long memberId) {
    	//获取会员信息
    	MemberBo member = memberBiz.selectById(memberId);
    	if(null == member) {
    		throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR + "用户被删除");
    	}
    	if(null == member.getReferralCode()) {
    		throw new WakaException("无上级推荐码");
    	}
		
		MemberSimpleInfoVo supurior = new MemberSimpleInfoVo();
    	if(null == member.getReferrerReferalCode() || member.getReferrerReferalCode().equals("RAVV")) {
    		supurior.setFirstname("RAVV");
    	}else {
    		MemberBo referrerMember = memberBiz.memberByReferral(member.getReferrerReferalCode());
    		if(null == referrerMember) {
    			supurior.setFirstname("RAVV");
    		}else {
    			supurior.setFirstname(referrerMember.getFirstname());
    			supurior.setLastname(referrerMember.getLastname());
    			supurior.setReferralCode(referrerMember.getReferralCode());
    			supurior.setAvator(QiniuUtil.getFullPath(referrerMember.getAvator()));
    		}
    	}
		return supurior;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Boolean updateMemberAdvance(Long memberId, BigDecimal changeMoney) {
    	//获取会员信息
    	MemberBo member = memberBiz.selectById(memberId);
    	if(null == member) {
    		throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR + "用户被删除");
    	}
    	
    	BigDecimal advance = member.getAdvance().add(changeMoney);
    	member.setAdvance(advance);
    	
    	Boolean rs = memberBiz.updateById(member);
		
		return rs;
	}
}