package cn.farwalker.ravv.personal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import cn.farwalker.waka.core.HttpKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.farwalker.ravv.order.CreateOrderController;
import cn.farwalker.ravv.personal.dto.MemberBankVo;
import cn.farwalker.ravv.service.member.basememeber.biz.IMemberBiz;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.payment.bank.biz.IMemberBankBiz;
import cn.farwalker.ravv.service.payment.bank.model.MemberBankBo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.base.controller.ControllerUtils;
import cn.farwalker.waka.util.Tools;

/**
 * 银行卡（卡包）
 * @author generateModel.java
 */
@RestController
@RequestMapping("/bankcard")
public class BankCardController{
    private final static  Logger log =LoggerFactory.getLogger(BankCardController.class);
    @Resource
    private IMemberBankBiz memberBankBiz;
    protected IMemberBankBiz getBiz(){
        return memberBankBiz;
    }
    
    @Resource
    private IMemberBiz memberBiz;
    
    /**
     * 会员添加新银行卡
     * @param memberBank
     * @return
     */
    @RequestMapping("/add_newcard")
    public JsonResult<Boolean> addNewCard(MemberBankBo memberBank){
        if(memberBank == null){
            return JsonResult.newFail("提交内容不能为空");
        }
        if(null == memberBank.getCardHolder()){
            return JsonResult.newFail("持卡人不能为空");
        }
        if(null == memberBank.getCardHolder()){
            return JsonResult.newFail("持卡人不能为空");
        }
        if(null == memberBank.getCardNumber()){
            return JsonResult.newFail("银行卡号不能为空");
        }
        
    	HttpSession sin = HttpKit.getRequest().getSession();
    	Long memberId = (Long)sin.getAttribute(CreateOrderController.K_MemberId);
    	
    	memberBank.setMemberId(memberId);
    	
        Boolean rs =getBiz().insert(memberBank);
        return JsonResult.newSuccess(rs);
    }
    
    /**
     * 获取会员银行卡列表
     * @return
     */
    @RequestMapping("/get_bankcardlist")
    public JsonResult<List<MemberBankVo>> getBankCardList(){
    	HttpSession sin =HttpKit.getRequest().getSession();
    	Long memberId = (Long)sin.getAttribute(CreateOrderController.K_MemberId);
    	
    	List<MemberBankBo> bankBoList = memberBankBiz.getBankCardByMemberId(memberId);
    	
    	if(Tools.collection.isEmpty(bankBoList)) {
    		return JsonResult.newSuccess(null);
    	}
    	
    	//只返回前端部分数据
    	List<MemberBankVo> bankVoList = ControllerUtils.convertList(bankBoList, MemberBankVo.class);
    	
    	return JsonResult.newSuccess(bankVoList);
    }
    
    /**
     * 删除会员银行卡
     * @param memberBank
     * @return
     */
    @RequestMapping("/delete_bankcard")
    public JsonResult<Boolean> deleteBankCard(Long id){
    	HttpSession sin =HttpKit.getRequest().getSession();
    	Long memberId = (Long)sin.getAttribute(CreateOrderController.K_MemberId);
    	
    	//获取会员信息
    	MemberBo member = memberBiz.selectById(memberId);
    	if(null == member) {
    		return JsonResult.newFail("找不到该会员信息，确认是否登录");
    	}
    	
    	Boolean rs =getBiz().deleteById(id);
    	
    	return JsonResult.newSuccess(rs);
    }
}