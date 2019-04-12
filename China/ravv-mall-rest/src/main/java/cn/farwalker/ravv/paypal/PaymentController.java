package cn.farwalker.ravv.paypal;

import cn.farwalker.ravv.service.order.constants.PaymentPlatformEnum;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoService;
import cn.farwalker.ravv.service.payment.pay.IPayService;
import cn.farwalker.ravv.service.payment.paymentlog.biz.IMemberPaymentLogBiz;
import cn.farwalker.ravv.service.paypal.PaymentForm;
import cn.farwalker.ravv.service.paypal.PaymentResultVo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.WakaException;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**负责支付和退款
 * Created by asus on 2019/3/5.
 */
@Controller
@RequestMapping("/pay")
public class PaymentController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("payByPaypalServiceImpl")
    private IPayService payByPaypal;

    @Autowired
    @Qualifier("payByWalletServiceImpl")
    private IPayService payByWallet;

    @Autowired
    IMemberPaymentLogBiz iMemberPaymentLogBiz;

    @Autowired
    IOrderInfoService iOrderInfoService;



    /**
     * 订单支付，参数中有金额信息，正常进行支付的接口
     * @param
     * @return
     */

    @RequestMapping(method = RequestMethod.POST, value = "/pay_by_all")
    @ResponseBody
    public JsonResult<PaymentResultVo> payByAll(HttpServletRequest request, PaymentForm paymentForm){
        try {
            return getPayService(paymentForm.getPayType()).payByAll(request,paymentForm);

        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
            return JsonResult.newFail(e.getMessage());
        }catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    /**
     * 订单支付,参数只有orderID,某些情况下前端只能拿到订单id
     * @param
     * @return
     */

    @RequestMapping(method = RequestMethod.POST, value = "/pay_by_order")
    @ResponseBody
    public JsonResult<PaymentResultVo> payByOrder(HttpServletRequest request,@RequestParam("orderId")Long orderId, @RequestParam("payType") String payType,
                                                  @RequestParam(required = false) String payPassword ){
        try {
            return getPayService(payType).payByOrder(request,orderId,payPassword);

        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
            return JsonResult.newFail(e.getMessage());
        }catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }
    }



    /**
     * 是否支付成功
     * @param session
     * @param orderId
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/is_pay_success")
    @ResponseBody
    public JsonResult<Boolean> isPaySuccess(HttpSession session,Long orderId){
    try{

            return JsonResult.newSuccess(iOrderInfoService.isPaySuccess(session,orderId));

        }catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    /**
     * 获取支付策略
     * @param payType
     * @return
     */
    private IPayService getPayService(String payType){
        if(PaymentPlatformEnum.getEnumByKey(payType)==null){
            throw new WakaException("未指定支付方式");
        }
        if(PaymentPlatformEnum.PayPal.getKey().equals(payType)){
            return payByPaypal;
        }
        else if(PaymentPlatformEnum.Advance.getKey().equals(payType)){
            return payByWallet;
        }else{
            throw new WakaException("支付方式指定错误");
        }

    }























}
