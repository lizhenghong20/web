package cn.farwalker.ravv.service.payment.pay;

import cn.farwalker.ravv.service.order.constants.PayStatusEnum;
import cn.farwalker.ravv.service.order.constants.PaymentModeEnum;
import cn.farwalker.ravv.service.order.constants.PaymentPlatformEnum;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderPaymentService;
import cn.farwalker.ravv.service.order.paymemt.biz.IOrderPaymemtBiz;
import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;
import cn.farwalker.ravv.service.paypal.PaymentForm;
import cn.farwalker.ravv.service.paypal.PaymentResultVo;
import cn.farwalker.ravv.service.paypal.URLUtils;
import cn.farwalker.ravv.service.paypal.service.IPaypalService;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import com.baomidou.mybatisplus.mapper.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * Created by asus on 2019/3/5.
 */
@Service("payByPaypalServiceImpl")
public class PayByPaypalServiceImpl implements IPayService {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private IPaypalService paypalService;

    @Autowired
    private IOrderPaymemtBiz iOrderPaymemtBiz;

    @Autowired
    private IOrderPaymentService iOrderPaymentService;


    //支付订单下单成功的回调地址，即本地服务器执行支付的地址，相对路径
    private static final String   RETURN_MAP = "/paypal_callback/pay_return";
    //在支付订单下单成功前，取消支付订单的回调地址，相对路径
    private static final String   CANCEL_MAP = "/paypal_callback/pay_cancel";
    /**
     * 订单支付，参数中有金额信息，正常进行支付的接口
     * @param
     * @return
     */
    @Override
    public JsonResult<PaymentResultVo> payByAll(HttpServletRequest request, PaymentForm paymentEx) throws Exception{
        log.info("==============订单总价total:{}============", paymentEx.getOrderTotal());
        log.info("==============商品总价subtotal:{}============", paymentEx.getSubTotal());
        log.info("==============税金:{}============", paymentEx.getTax());
        log.info("==============运费:{}============", paymentEx.getShipping());
        if (null == paymentEx || null == paymentEx.getOrderId()) {
            log.error("======================invalid parameter error==================");
            return JsonResult.newFail("invalid");
        }
        Long memberId;
        if (request.getSession().getAttribute("memberId") == null) {
            throw new WakaException(RavvExceptionEnum.TOKEN_PARSE_MEMBER_ID_FAILED);
        }
        if(!iOrderPaymentService.isOrderValidForPay(paymentEx.getOrderId())){
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        }
        memberId = (Long) request.getSession().getAttribute("memberId");
        paymentEx.setMemberId(memberId);
        // 支付订单下单成功的回调地址，即本地服务器执行支付的地址,需要在创建订单时传给PayPal的服务器
        String returnURL = URLUtils.getBaseURl(request) + RETURN_MAP;
        // 在支付订单下单成功前取消支付订单的回调地址，需要在创建订单时传给PayPal的服务器
        String cancelUrl = URLUtils.getBaseURl(request) + CANCEL_MAP;
        PaymentResultVo result = paypalService.getPaypalUrl(paymentEx, returnURL, cancelUrl);
        result.setPayType(PaymentPlatformEnum.PayPal.getKey());
        if (request != null)
            return JsonResult.newSuccess(result);
        else {
            return JsonResult.newFail(null);
        }
    }

    /**
     * 订单支付,参数只有orderID,应用场景在于对取消的订单中的商品做批量的购买
     * @param
     * @return
     */
    @Override
    public JsonResult<PaymentResultVo> payByOrder(HttpServletRequest request, Long orderId,String payPassword) throws Exception{
        if(orderId == null || orderId == 0)
            throw new WakaException("orderId 不合法");
        OrderPaymemtBo query = iOrderPaymemtBiz.selectOne(Condition.create().eq(OrderPaymemtBo.Key.orderId.toString(),orderId)
                .eq(OrderPaymemtBo.Key.payStatus.toString(), PayStatusEnum.UNPAY.toString())
                .eq(OrderPaymemtBo.Key.payMode.toString(), PaymentModeEnum.ONLINE.toString()));
        if(query == null)
            throw new WakaException("找不到符合条件的订单");
        PaymentForm  paymentForm = new PaymentForm();
        paymentForm.setOrderId(orderId);
        paymentForm.setOrderTotal(query.getShouldPayTotalFee() == null ? BigDecimal.ZERO : query.getShouldPayTotalFee());
        paymentForm.setSubTotal(query.getGoodsTotalFee() == null ? BigDecimal.ZERO : query.getGoodsTotalFee());
        paymentForm.setShipping(query.getPostFee() == null ? BigDecimal.ZERO : query.getPostFee());
        paymentForm.setTax(query.getTaxFee() == null ? BigDecimal.ZERO : query.getTaxFee());
        return payByAll(request,paymentForm);
    }






}
