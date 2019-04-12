package cn.farwalker.ravv.service.member.basememeber.biz;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import cn.farwalker.ravv.service.member.basememeber.constants.MemberSubordinate;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.member.basememeber.model.MemberSimpleInfoVo;

/**
 * member表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IMemberBiz extends IService<MemberBo>{
	
	/**
	 * 根据会员推荐码获取会员信息
	 * @param referralCode
	 * @return
	 */
	MemberBo memberByReferral(String referralCode);
	
	/**
	 * 将分销金额添加到账户余额和累计分销金额
	 * @param memberId 会员id
	 * @param orderId 来源订单id
	 * @param money 变动金额
	 * @return
	 */
	Boolean addProfitToAdvance(Long memberId, Long orderId, BigDecimal money);
	
	/**
	 * 获取会员上级推荐人信息
	 * @param memberId 会员id
	 * @return
	 */
	MemberSimpleInfoVo getMemberSupurior(Long memberId);
	
	/**
	 * 获取某一等级下级推荐人
	 * @param referrerReferalCode 推荐人推荐码
	 * @param subordinate 下级推荐人等级
	 * @param start
	 * @param size
	 * @return
	 */
	Page<MemberSimpleInfoVo> subordinateList(String referrerReferalCode, MemberSubordinate subordinate, Integer start, Integer size);

	/**
	 * 修改会员账户余额
	 * @param memberId 会员id
	 * @param changeMoney 加减的金额
	 * @return
	 */
	Boolean updateMemberAdvance(Long memberId, BigDecimal changeMoney);
}