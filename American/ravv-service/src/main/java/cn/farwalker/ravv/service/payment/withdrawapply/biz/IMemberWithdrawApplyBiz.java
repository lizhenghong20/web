package cn.farwalker.ravv.service.payment.withdrawapply.biz;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import cn.farwalker.ravv.service.payment.withdrawapply.model.MemberWithdrawApplyBo;
import cn.farwalker.ravv.service.payment.withdrawapply.model.WithdrawPayCostVo;

/**
 * 提现申请<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IMemberWithdrawApplyBiz extends IService<MemberWithdrawApplyBo>{
	
	/**
	 * 计算提现金额手续费
	 * @param memberId 会员id
	 * @param money 提现金额
	 * @return
	 */
	WithdrawPayCostVo getpaycost(Long memberId, BigDecimal money);
	
	/**
	 * 会员提现申请
	 * @param bankCardId 银行卡id
	 * @param money 提现金额
	 * @param paycost 提现手续费
	 * @return
	 */
	Boolean withdrawApply(Long memberId ,Long bankCardId, BigDecimal money, BigDecimal paycost);
	
	/**
	 * 获取会员提现记录
	 * @param memberId 会员id
	 * @return
	 */
	Page<MemberWithdrawApplyBo> getWithdrawApplyPage(Long memberId, Integer start, Integer size);
	
	/**
	 * 审核会员提现记录
	 * @param WithdrawApply 提现记录
	 * @param sysUserId 管理员id
	 * @return
	 */
	Boolean updateWithdrawApply(MemberWithdrawApplyBo Withdraw, Long sysUserId);
	
}