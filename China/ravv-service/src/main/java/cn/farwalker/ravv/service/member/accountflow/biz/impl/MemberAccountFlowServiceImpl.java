package cn.farwalker.ravv.service.member.accountflow.biz.impl;

import java.util.List;

import javax.annotation.Resource;

import cn.farwalker.waka.core.WakaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;

import cn.farwalker.ravv.service.email.IEmailService;
import cn.farwalker.ravv.service.member.accountflow.biz.IMemberAccountFlowBiz;
import cn.farwalker.ravv.service.member.accountflow.biz.IMemberAccountFlowService;
import cn.farwalker.ravv.service.member.accountflow.constants.ChargeTypeEnum;
import cn.farwalker.ravv.service.member.accountflow.model.MemberAccountFlowBo;
import cn.farwalker.ravv.service.member.accountflow.model.MemberAccountVo;
import cn.farwalker.ravv.service.member.accountflow.model.MemberTransactionListVo;
import cn.farwalker.ravv.service.member.basememeber.biz.IMemberBiz;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.member.pam.member.biz.IPamMemberBiz;
import cn.farwalker.ravv.service.member.pam.member.model.PamMemberBo;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.util.Tools;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberAccountFlowServiceImpl implements IMemberAccountFlowService {

    @Autowired
    private IMemberAccountFlowBiz iMemberAccountFlowBiz;

    @Autowired
    private IMemberBiz iMemberBiz;

    @Autowired
    private IPamMemberBiz iPamMemberBiz;

    @Autowired
    private IEmailService iEmailService;
    
    @Resource
    private IMemberBiz memberBiz;

    @Override
    public MemberAccountVo getAccountAdvance(Long memberId) {
        MemberAccountVo memberAccountVo = new MemberAccountVo();
        MemberBo memberBo = iMemberBiz.selectById(memberId);
        if(Tools.string.isEmpty(memberBo.getPayPassword())){
            memberAccountVo.setPaymentPassword(false);
        }
        else {
            memberAccountVo.setPaymentPassword(true);
        }
        memberAccountVo.setAdvance(memberBo.getAdvance());
        return memberAccountVo;
    }

    @Override
    public MemberTransactionListVo getAllTransaction(Long memberId, int currentPage, int pageSize) {
        MemberTransactionListVo transactionListVo = new MemberTransactionListVo();
        Page<MemberAccountFlowBo> page = new Page<>(currentPage, pageSize);
        EntityWrapper<MemberAccountFlowBo> queryTransaction = new EntityWrapper<>();
        queryTransaction.eq(MemberAccountFlowBo.Key.memberId.toString(), memberId);
        queryTransaction.orderBy(MemberAccountFlowBo.Key.gmtCreate.toString(), false);
        Page<MemberAccountFlowBo> accountPage = iMemberAccountFlowBiz.selectPage(page, queryTransaction);
        List<MemberAccountFlowBo> accountFlowBoList = accountPage.getRecords();
        if(accountFlowBoList.size() != 0){
            transactionListVo.setTransacntionList(accountFlowBoList);
            transactionListVo.setListIsNull(false);
        }
        else{
            transactionListVo.setListIsNull(true);
        }
        return transactionListVo;
    }

    @Override
    public MemberTransactionListVo getTransactionByChargeSource(Long memberId, int type, int currentPage, int pageSize) {
        MemberTransactionListVo transactionListVo = new MemberTransactionListVo();
        Page<MemberAccountFlowBo> page = new Page<>(currentPage, pageSize);
        EntityWrapper<MemberAccountFlowBo> queryTransaction = new EntityWrapper<>();
        queryTransaction.eq(MemberAccountFlowBo.Key.memberId.toString(), memberId);
        queryTransaction.eq(MemberAccountFlowBo.Key.chargesource.toString(), type);
        queryTransaction.orderBy(MemberAccountFlowBo.Key.gmtCreate.toString(), false);
        Page<MemberAccountFlowBo> accountPage = iMemberAccountFlowBiz.selectPage(page, queryTransaction);
        List<MemberAccountFlowBo> accountTypeList = accountPage.getRecords();
        if(accountTypeList.size() != 0){
            transactionListVo.setTransacntionList(accountTypeList);
            transactionListVo.setListIsNull(false);
        }
        else{
            transactionListVo.setListIsNull(true);
        }
        return transactionListVo;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean updatePayPassword(Long memberId, String payPassword) {
        String pwd = encodePassword(memberId, payPassword);
        MemberBo memberBo = new MemberBo();
        memberBo.setId(memberId);
        memberBo.setPayPassword(pwd);
        if(!iMemberBiz.updateById(memberBo))
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        return true;
    }

    /**
     * @description: 验证旧密码是否正确
     * @param: memberId, payPassword
     * @return string
     * @author Mr.Simple
     * @date 2019/1/12 11:46
     */
    @Override
    public String verifiedOldPassword(Long memberId, String payPassword) {
        //将传入的密码加密，再从数据库获取已存密码进行对比
        String oldPassword = encodePassword(memberId, payPassword);
        MemberBo memberBo = iMemberBiz.selectById(memberId);
        if(!memberBo.getPayPassword().equals(oldPassword))
            throw new WakaException(RavvExceptionEnum.USER_ACCOUNT_OR_PASSWORD_INCORRECT);
        return "verified success";
    }

    @Override
    public String updatePayPasswordWithActivationCode(Long memberId, String email, String payPassword, String activationCode) {
        //判断验证是否成功
        if(!iEmailService.validator(email, activationCode))
            throw new WakaException(RavvExceptionEnum.USER_EMAIL_VERIFICATION_FAILED);
        if(!updatePayPassword(memberId, payPassword))
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR + "forget password update error");
        return "update password successful";
    }


    private String encodePassword(Long memberId, String payPassword){
        //获取加密盐，对密码进行加密
        PamMemberBo pamMemberBo = iPamMemberBiz.selectOne(Condition.create()
                .eq(PamMemberBo.Key.memberId.toString(), memberId));
        if(pamMemberBo == null)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        String salt = pamMemberBo.getSalt();
        //对密码进行加密
        String passSalt = payPassword + "@" + salt;
        String pwd = Tools.md5.md5Hex(passSalt);
        return pwd;
    }

	@Override
	public Page<MemberAccountFlowBo> getTransactionByChargeType(Long memberId, ChargeTypeEnum type, Integer start, Integer size) {
		//获取会员信息
    	MemberBo member = memberBiz.selectById(memberId);
    	if(null == member) {
    		throw new WakaException("找不到该会员信息");
    	}
		
		if (Tools.number.nullIf(start, 0) <= 0) {
			start = Integer.valueOf(0);
		}
		if (Tools.number.nullIf(size, 0) == 0) {
			size = Integer.valueOf(10);
		}
		
		Page<MemberAccountFlowBo> page = new Page<>(start,size);
		Wrapper<MemberAccountFlowBo> wrapper = new EntityWrapper<>();
		wrapper.eq(MemberAccountFlowBo.Key.memberId.toString(), memberId);
		if(null != type) {
			wrapper.eq(MemberAccountFlowBo.Key.chargetype.toString(), type);
		}
		
		return iMemberAccountFlowBiz.selectPage(page, wrapper);
	}


}
