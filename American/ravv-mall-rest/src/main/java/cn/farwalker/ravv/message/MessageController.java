package cn.farwalker.ravv.message;


import cn.farwalker.ravv.service.sys.message.biz.ISystemMessageService;
import cn.farwalker.ravv.service.sys.message.model.*;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/message")
@Slf4j
public class MessageController {

    @Autowired
    private ISystemMessageService iSystemMessageService;

    /**
     * @description: web端进入消息中心默认请求该接口，该接口包括所有系统通知，问答模块、优惠推荐是否有未读信息
     * @param: memberId, currentPage, pageSize
     * @return webmessageVo
     * @author Mr.Simple
     * @date 2019/1/10 10:07
     */
    @RequestMapping("/system_notification")
    public JsonResult<WebMessageVo> getSystemNotification(HttpSession session, int currentPage, int pageSize) {
        try {
            currentPage++;
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iSystemMessageService.updateAndGetSystemNotification(memberId, currentPage, pageSize));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    /**
     * @description: web端进入消息中心默认请求该接口，该接口包括所有系统通知，问答模块、优惠推荐是否有未读信息
     * @param:
     * @return list
     * @author Mr.Simple
     * @date 2019/1/10 10:07
     */
    @RequestMapping("/consult")
    public JsonResult<ConsultListVo> getConsult(HttpSession session, int currentPage, int pageSize) {
        try {
            currentPage++;
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iSystemMessageService.updateAndGetConsult(memberId, currentPage, pageSize));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("/product_promotion")
    public JsonResult<SystemMessageVo> getProductPromotion(HttpSession session, int currentPage, int pageSize) {
        try {
            currentPage++;
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iSystemMessageService.updateAndGetProductPromotion(memberId, currentPage, pageSize));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("/order_info")
    public JsonResult<OrderInfoMessageVo> getOrderInfo(HttpSession session, int currentPage, int pageSize) {
        try {
            currentPage++;
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iSystemMessageService.updateAndGetOrderInfo(memberId, currentPage, pageSize));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("/unread_message_for_app")
    public JsonResult<UnReadMessageVo> unReadMessage(HttpSession session) {
        try {
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iSystemMessageService.getUnReadMessageList(memberId));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("/delete_by_type")
    public JsonResult<String> deleteByType(HttpSession session, String type) {
        try {
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iSystemMessageService.deleteMessageByType(memberId, type));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("/delete_by_Id")
    public JsonResult<String> deleteById(HttpSession session, Long messageId) {
        try {
            if(messageId == 0){
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            }
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iSystemMessageService.deleteMessageById(messageId));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("/delete_all")
    public JsonResult<String> deleteAll(HttpSession session) {
        try {

            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iSystemMessageService.deleteAllMessage(memberId));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("/change_to_read")
    public JsonResult<String> changeToRead(HttpSession session) {
        try {

            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iSystemMessageService.updateToRead(memberId));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("/send_product_promotion")
    public JsonResult<String> addPromotionMessage(HttpSession session, SystemMessageBo promotionMessage) {
        try {

            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            promotionMessage.setPublishMemberId(memberId);
            return JsonResult.newSuccess(iSystemMessageService.addPromotionMessage(promotionMessage));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }


}
