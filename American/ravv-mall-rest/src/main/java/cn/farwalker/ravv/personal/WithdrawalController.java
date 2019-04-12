package cn.farwalker.ravv.personal;
import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import cn.farwalker.waka.core.HttpKit;
import cn.farwalker.waka.core.WakaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;

import cn.farwalker.ravv.order.CreateOrderController;
import cn.farwalker.ravv.personal.dto.WithdrawApplyVo;
import cn.farwalker.ravv.service.member.basememeber.biz.IMemberBiz;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.payment.withdrawapply.biz.IMemberWithdrawApplyBiz;
import cn.farwalker.ravv.service.payment.withdrawapply.model.MemberWithdrawApplyBo;
import cn.farwalker.ravv.service.payment.withdrawapply.model.WithdrawPayCostVo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.base.controller.ControllerUtils;

/**
 * 会员提现接口<br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@RestController
@RequestMapping("/withdrawal")
public class WithdrawalController{
    private final static  Logger log =LoggerFactory.getLogger(WithdrawalController.class);
    @Resource
    private IMemberWithdrawApplyBiz memberWithdrawApplyBiz;
    protected IMemberWithdrawApplyBiz getBiz(){
        return memberWithdrawApplyBiz;
    }
    
    @Resource
    private IMemberBiz memberBiz;
    
    /**
     * 获取用户会员账户总金额
     * @return
     */
    @RequestMapping("/get_member_advance")
    public JsonResult<BigDecimal> getMemberAdvance(){
    	HttpSession sin = HttpKit.getRequest().getSession();
    	Long memberId = (Long)sin.getAttribute(CreateOrderController.K_MemberId);
    	
    	//获取会员信息
    	MemberBo member = memberBiz.selectById(memberId);
    	if(null == member) {
    		return JsonResult.newFail("找不到该会员信息，确认是否登录");
    	}
    	
    	BigDecimal MemberAdvance = member.getAdvance();
    	
    	return JsonResult.newSuccess(MemberAdvance);
    }
    
    /**
     * 计算提现金额所需手续费和实际提现金额
     * @param money 提现金额
     * @return
     */
    @RequestMapping("/getpaycost")
    public JsonResult<WithdrawPayCostVo> gePaycost(BigDecimal money){
        try{
        	HttpSession sin =HttpKit.getRequest().getSession();
        	Long memberId = (Long)sin.getAttribute(CreateOrderController.K_MemberId);
        	
        	if(null == money) {
        		return JsonResult.newFail("请输入提现金额");
        	}
        	
        	Integer compareResult = money.compareTo(BigDecimal.ZERO);
        	if(compareResult == 0 || compareResult == -1) {
        		return JsonResult.newFail("请输入大于0.00的金额");
        	}
        	
        	WithdrawPayCostVo withdrawPayCostVo = memberWithdrawApplyBiz.getpaycost(memberId,money);
        	
        	return JsonResult.newSuccess(withdrawPayCostVo);

        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());

        }
    }
    
    /**
     * 申请提现接口
     * @param bankCardId 银行卡id
     * @param money 申请提现金额
     * @param paycost 提现手续费
     * @return
     */
    @RequestMapping("/withdraw")
    public JsonResult<Boolean> withdraw(Long bankCardId, BigDecimal money, BigDecimal paycost){
        try{
            if(bankCardId == null){
                return JsonResult.newFail("请选择银行卡");
            }
            if(money == null){
                return JsonResult.newFail("请填写提现金额");
            }
            if(paycost == null){
                return JsonResult.newFail("手续费不能为空");
            }
            
        	HttpSession sin =HttpKit.getRequest().getSession();
        	Long memberId = (Long)sin.getAttribute(CreateOrderController.K_MemberId);
            
            Boolean rs =getBiz().withdrawApply(memberId, bankCardId, money, paycost);
            
            return JsonResult.newSuccess(rs);
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());

        }
    }
    
    /**
     * 获取会员提现列表
     * @param start
     * @param size
     * @return
     */
    @RequestMapping("/withdraw_apply_list")
    public JsonResult<Page<WithdrawApplyVo>> getWithdrawApplyPage(Integer start, Integer size){
    	HttpSession sin =HttpKit.getRequest().getSession();
    	Long memberId = (Long)sin.getAttribute(CreateOrderController.K_MemberId);
    	
    	//获取会员信息
    	MemberBo member = memberBiz.selectById(memberId);
    	if(null == member) {
    		return JsonResult.newFail("找不到该会员信息，确认是否登录");
    	}
    	
    	Page<MemberWithdrawApplyBo> applyPage = memberWithdrawApplyBiz.getWithdrawApplyPage(memberId, start, size);
    	
    	Page<WithdrawApplyVo> withdrawApplyVoPage = ControllerUtils.convertPageRecord(applyPage, WithdrawApplyVo.class);
    	
    	return JsonResult.newSuccess(withdrawApplyVoPage);
    }
    
}