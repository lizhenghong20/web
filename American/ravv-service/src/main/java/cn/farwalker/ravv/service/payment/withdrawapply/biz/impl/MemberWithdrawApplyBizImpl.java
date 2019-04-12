package cn.farwalker.ravv.service.payment.withdrawapply.biz.impl;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

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
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.payment.bank.biz.IMemberBankBiz;
import cn.farwalker.ravv.service.payment.bank.model.MemberBankBo;
import cn.farwalker.ravv.service.payment.withdrawapply.biz.IMemberWithdrawApplyBiz;
import cn.farwalker.ravv.service.payment.withdrawapply.constants.WithdrawStatusEnum;
import cn.farwalker.ravv.service.payment.withdrawapply.dao.IMemberWithdrawApplyDao;
import cn.farwalker.ravv.service.payment.withdrawapply.model.MemberWithdrawApplyBo;
import cn.farwalker.ravv.service.payment.withdrawapply.model.WithdrawPayCostVo;
import cn.farwalker.ravv.service.sys.param.biz.ISysParamBiz;
import cn.farwalker.ravv.service.sys.param.model.SysParamBo;
import cn.farwalker.ravv.service.sys.syslogs.biz.ISysLogsBiz;
import cn.farwalker.ravv.service.sys.syslogs.model.SysLogsBo;
import cn.farwalker.ravv.service.sys.user.biz.ISysUserBiz;
import cn.farwalker.ravv.service.sys.user.model.SysUserBo;
import cn.farwalker.waka.components.sequence.SequenceManager;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.util.DateUtil.FORMAT;
import cn.farwalker.waka.util.Tools;

/**
 * 提现申请<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class MemberWithdrawApplyBizImpl extends ServiceImpl<IMemberWithdrawApplyDao,MemberWithdrawApplyBo> implements IMemberWithdrawApplyBiz{

    @Resource
    private IMemberWithdrawApplyBiz memberWithdrawApplyBiz;
	
	@Resource
	private ISysParamBiz sysParamBiz;
	
	@Resource
	private IMemberBiz memberBiz;
	
	@Resource
	private IMemberAccountFlowBiz memberAccountFlowBiz;
	
	@Resource
	private IMemberBankBiz memberBankBiz;
	
    @Resource
    private ISysLogsBiz sysLogsBiz;
    
    @Resource
    private ISysUserBiz sysUserBiz;
	
	@Override
	public WithdrawPayCostVo getpaycost(Long memberId, BigDecimal money) {
		if(null == memberId) {
			throw new WakaException("请检查是否登录");
		}
		//获取会员信息
		MemberBo member = memberBiz.selectById(memberId);
		if(null == member) {
			throw new WakaException("无该用户信息");
		}
		
    	Integer compareResult = member.getAdvance().compareTo(money);
    	if(compareResult == -1) {
    		throw new WakaException("输入金额超过零钱余额");
    	}
		
    	WithdrawPayCostVo payCostVo = new WithdrawPayCostVo();
    	
		//获取系统配置的提现手续费费率
		BigDecimal withdrawFeeRate = this.getWithdrawFeeRate();
		
		//计算提现手续费
		BigDecimal paycost = money.multiply(withdrawFeeRate).setScale(2, RoundingMode.HALF_UP);
		
    	if(compareResult == 0) {
    		BigDecimal actualWithdraw = member.getAdvance().subtract(paycost);
    		payCostVo.setActualWithdraw(actualWithdraw);
    	}else {
    		payCostVo.setActualWithdraw(money);
    	}
    	payCostVo.setPaycost(paycost);
		
		return payCostVo;
	}
	
	//获取系统配置的提现手续费费率
	public BigDecimal getWithdrawFeeRate() {
		SysParamBo param = sysParamBiz.getParamByCode("withdraw_fee_rate");
		
		//若系统没有配置提现手续费费率，则默认不收取手续费
		if(null != param && Tools.string.isEmpty(param.getPvalue())) {
			//计算手续费
			BigDecimal withdrawFeeRate = new BigDecimal(param.getPvalue());
			return withdrawFeeRate;
		}else {
			BigDecimal withdrawFeeRate = new BigDecimal(0.00);
			return withdrawFeeRate;
		}
	}

	@Override
	public Boolean withdrawApply(Long memberId, Long bankCardId, BigDecimal money, BigDecimal paycost) {
		if(null == memberId) {
			throw new WakaException("请检查是否登录");
		}
		//获取会员信息
		MemberBo member = memberBiz.selectById(memberId);
		if(null == member) {
			throw new WakaException("无该用户信息");
		}
		
		MemberBankBo bankCard = memberBankBiz.selectById(bankCardId);
		if(null == bankCard) {
			throw new WakaException("该银行卡信息不存在");
		}
		
    	Integer advanceCompare = member.getAdvance().compareTo(money);
    	if(advanceCompare == -1) {
    		throw new WakaException("输入金额超过零钱余额");
    	}
    	
		//获取系统配置的提现手续费费率
		BigDecimal withdrawFeeRate = this.getWithdrawFeeRate();
		
    	Integer paycostCompare = paycost.compareTo(money.multiply(withdrawFeeRate));
    	if(paycostCompare != 0) {
    		throw new WakaException("前端传入的手续费不正确");
    	}
    	
    	MemberWithdrawApplyBo withdrawApply = new MemberWithdrawApplyBo();
    	withdrawApply.setMemberId(memberId);
    	withdrawApply.setBankId(bankCardId);
    	withdrawApply.setBankName(bankCard.getBankName());
    	withdrawApply.setBankType(bankCard.getBankType());
    	withdrawApply.setCardNumber(bankCard.getCardNumber());
    	withdrawApply.setApplyFee(paycost);
    	withdrawApply.setPercentage(withdrawFeeRate);
    	withdrawApply.setApplyTime(new Date());
    	withdrawApply.setWithdrawStatus(WithdrawStatusEnum.Pending_Audit);
    	
    	//生成申请编号
    	String applyNo = this.createApplyNo();
    	withdrawApply.setApplyNo(applyNo);
    	
    	Boolean rs = memberWithdrawApplyBiz.insert(withdrawApply);
		
		return rs;
	}
	
	/**创建提现申请编号*/
	public String createApplyNo(){
		String df = Tools.date.formatDate(FORMAT.YYYYMMDD).toString();
		SequenceManager sm = SequenceManager.getInstance(MemberWithdrawApplyBo.TABLE_NAME + df);
		long code = sm.nextValue();
		String applyNo = df + Tools.number.formatNumber(code, 4);
		return applyNo;
	}
	

	@Override
	public Page<MemberWithdrawApplyBo> getWithdrawApplyPage(Long memberId, Integer start, Integer size) {
		if (Tools.number.nullIf(start, 0) <= 0) {
			start = Integer.valueOf(0);
		}
		if (Tools.number.nullIf(size, 0) == 0) {
			size = Integer.valueOf(10);
		}
		
		Page<MemberWithdrawApplyBo> page = new Page<>(start,size);
		
		Wrapper<MemberWithdrawApplyBo> wrapper = new EntityWrapper<>();
		wrapper.eq(MemberWithdrawApplyBo.Key.memberId.toString(), memberId);
		wrapper.orderBy(MemberWithdrawApplyBo.Key.gmtCreate.toString(), false);
		
		Page<MemberWithdrawApplyBo> applyPage = memberWithdrawApplyBiz.selectPage(page, wrapper);
		
		return applyPage;
	}

	@Override
	public Boolean updateWithdrawApply(MemberWithdrawApplyBo Withdraw, Long sysUserId) {
		// 获取管理员信息
		SysUserBo sysUser = sysUserBiz.selectById(sysUserId);
		if (null == sysUser) {
			throw new WakaException("没有该管理员信息");
		}
		
		//获取提现记录
		MemberWithdrawApplyBo withdrawApply = memberWithdrawApplyBiz.selectById(Withdraw.getId());
		if (null == withdrawApply) {
			throw new WakaException("没有该提现记录");
		}
		if(withdrawApply.getWithdrawStatus() == WithdrawStatusEnum.Withdraw_Succeed) {
			throw new WakaException("该提现记录已成功，不能重复提现");
		}
		
		//获取会员信息
		MemberBo member = memberBiz.selectById(withdrawApply.getMemberId());
		if(null == member) {
			throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR + "用户被删除");
		}
        
		BigDecimal advance = member.getAdvance();//账户余额
    	Integer applyFeeCompare = advance.compareTo(withdrawApply.getApplyFee());
    	if(applyFeeCompare == -1) {
    		throw new WakaException("会员余额为：" + advance + ",低于提现金额");
    	} 
		
		withdrawApply.setCheckTime(new Date());
		withdrawApply.setCheckerId(sysUser.getId());
		withdrawApply.setCheckerName(sysUser.getName());
		withdrawApply.setWithdrawStatus(Withdraw.getWithdrawStatus());
		withdrawApply.setRemark(Withdraw.getRemark());
		
		Boolean rs = memberWithdrawApplyBiz.updateById(withdrawApply);
		
        if(rs) {
        	if(withdrawApply.getWithdrawStatus() == WithdrawStatusEnum.Withdraw_Succeed) {
	        	//提现审核通过，更新会员账户余额
	        	memberBiz.updateMemberAdvance(member.getId(), withdrawApply.getApplyFee().negate());
	        	
	        	//记录会员账户流水
	        	MemberAccountFlowBo accountFlow = new MemberAccountFlowBo();
	        	accountFlow.setMemberId(member.getId());
	        	accountFlow.setAmt(withdrawApply.getApplyFee().negate());
	        	accountFlow.setOperatorId(sysUserId);
	        	accountFlow.setOperator(sysUser.getName());
	        	accountFlow.setChargetype(ChargeTypeEnum.Withdraw);
	        	accountFlow.setChargesource(ChargeSourceEnum.Withdraw);
	        	accountFlow.setRemark(Withdraw.getRemark());
	        	accountFlow.setAuditorId(sysUserId);
	        	accountFlow.setAuditDate(new Date());
	        	
	        	memberAccountFlowBiz.updateById(accountFlow);
	        	
        	}
        	
        	//记录系统操作日志
			SysLogsBo logsBo = new SysLogsBo();
			logsBo.setSourcetable("member_withdraw_apply");
			logsBo.setSourcefield("withdraw_status");
			logsBo.setSourceid(String.valueOf(withdrawApply.getMemberId()));
			logsBo.setContent("管理员" + sysUser.getName() + "将会员(id:" + member.getId() + ")的提现记录（id：" + withdrawApply.getId() + "）的审核状态改为" + withdrawApply.getWithdrawStatus().getLabel());
			
			sysLogsBiz.insert(logsBo);
        }
		return rs;
	}
}