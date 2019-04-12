package cn.farwalker.ravv.member;

import cn.farwalker.waka.core.WakaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;

import cn.farwalker.ravv.service.member.accountflow.biz.IMemberAccountFlowService;
import cn.farwalker.ravv.service.member.accountflow.constants.ChargeTypeEnum;
import cn.farwalker.ravv.service.member.accountflow.model.MemberAccountFlowBo;
import cn.farwalker.waka.core.JsonResult;
import lombok.extern.slf4j.Slf4j;

/**
 * 会员钱包
 * @author chensl
 */
@RestController
@RequestMapping("/wallet")
@Slf4j
public class AccountFlowController {

    @Autowired
    private IMemberAccountFlowService iMemberAccountFlowService;

    /**
     * 根据交易流水类型获取交易记录
     * @param memberId 会员id
     * @param type 交易流水类型
     * @param start 页码
     * @param size 每页行数
     * @return
     */
    @RequestMapping("/transaction")
    public JsonResult<Page<MemberAccountFlowBo>> getTransactionByType(Long memberId, ChargeTypeEnum type, Integer start, Integer size) {
        try {
        	
        	Page<MemberAccountFlowBo> transactionPage = iMemberAccountFlowService.getTransactionByChargeType(memberId, type, start, size);
        	if(null == transactionPage) {
        		Page<MemberAccountFlowBo> transaction = new Page<>();
        		return JsonResult.newSuccess(transaction);
        	}
        	
        	return JsonResult.newSuccess(transactionPage);
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

}
