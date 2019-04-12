package cn.farwalker.ravv.service.paypal.service;

import cn.farwalker.ravv.service.paypal.PaymentForm;
import cn.farwalker.ravv.service.paypal.PaymentResultVo;
import cn.farwalker.ravv.service.paypal.RefundForm;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

/**
 * Created by asus on 2018/12/12.
 */

public interface IPaypalService {

    /**
     * 开始支付
     * @param paymentEx
     * @return
     * @throws Exception
     */
    PaymentResultVo getPaypalUrl(PaymentForm paymentEx, String returnURL, String cancelUrl) throws Exception;

    /**
     * 创建支付订单
     * @param paymentEx
     * @param successUrl
     * @param cancelUrl
     * @return
     * @throws Exception
     */
    public Payment createPayment(PaymentForm paymentEx, String successUrl, String cancelUrl) throws Exception;

    /**
     * 执行支付
     * @param paymentId
     * @param payerId
     * @return
     * @throws PayPalRESTException
     */
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;

    /**
     * 退款调用
     * @param refundForm
     * @return
     * @throws Exception
     */

    public String  refund(RefundForm refundForm) throws Exception;


    /**
     * 支付成功后更新订单信息
     * @param payment
     */
    public  void updatePayment(Payment payment);

    /**
     * 判断当前订单有没有支付成功
     * @param memberId
     * @param orderId
     */
    public boolean isPaySuccess(Long memberId,Long orderId);

}
