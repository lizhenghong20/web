package cn.farwalker.ravv.service.payment.pay;

import cn.farwalker.ravv.service.paypal.PaymentForm;
import cn.farwalker.ravv.service.paypal.PaymentResultVo;
import cn.farwalker.waka.core.JsonResult;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by asus on 2019/3/5.
 */
public interface IPayService {

    /**
     * 订单支付，参数中有金额信息，正常进行支付的接口
     * @param
     * @return
     */
    JsonResult<PaymentResultVo> payByAll(HttpServletRequest request, PaymentForm paymentEx) throws Exception;

    /**
     * 订单支付,参数只有orderID,应用场景在于对取消的订单中的商品做批量的购买
     * @param
     * @return
     */
   JsonResult<PaymentResultVo> payByOrder(HttpServletRequest request, Long orderId,String payPassword) throws Exception;









}
