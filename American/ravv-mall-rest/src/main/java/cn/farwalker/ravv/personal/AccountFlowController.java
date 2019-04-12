package cn.farwalker.ravv.personal;

import cn.farwalker.ravv.service.member.accountflow.biz.IMemberAccountFlowService;
import cn.farwalker.ravv.service.member.accountflow.model.MemberAccountVo;
import cn.farwalker.ravv.service.member.accountflow.model.MemberTransactionListVo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.util.Tools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/wallet")
@Slf4j
public class AccountFlowController {

    @Autowired
    private IMemberAccountFlowService iMemberAccountFlowService;

    @RequestMapping("/advance")
    public JsonResult<MemberAccountVo> getAdvance(HttpSession session) {
        try {
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iMemberAccountFlowService.getAccountAdvance(memberId));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("/all_transaction")
    public JsonResult<MemberTransactionListVo> getAllTransaction(HttpSession session, int currentPage, int pageSize) {
        try {
            currentPage++;
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iMemberAccountFlowService.getAllTransaction(memberId, currentPage, pageSize));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("/transaction_by_type")
    public JsonResult<MemberTransactionListVo> getTransactionByType(HttpSession session, int type, int currentPage, int pageSize) {
        try {
            currentPage++;
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iMemberAccountFlowService.getTransactionByChargeSource(memberId, type, currentPage, pageSize));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("/verified_old_password")
    public JsonResult<String> verifiedOldPayPassword(HttpSession session, String payPassword) {
        try {
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            if(Tools.string.isEmpty(payPassword))
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return JsonResult.newSuccess(iMemberAccountFlowService.verifiedOldPassword(memberId, payPassword));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("/update_password")
    public JsonResult<String> updateWithActivationCode(HttpSession session, String email, String activationCode, String payPassword) {
        try {
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            if(Tools.string.isEmpty(payPassword))
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            if(Tools.string.isEmpty(activationCode))
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            if(Tools.string.isEmpty(email))
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return JsonResult.newSuccess(iMemberAccountFlowService.updatePayPasswordWithActivationCode(memberId, email, payPassword, activationCode));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("/add_payment_password")
    public JsonResult<String> addPayPassword(HttpSession session, String payPassword) {
        try {
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            if(Tools.string.isEmpty(payPassword))
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            if(iMemberAccountFlowService.updatePayPassword(memberId, payPassword))
                return JsonResult.newSuccess("add/update successful");
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
        return JsonResult.newFail("fail");
    }


}
