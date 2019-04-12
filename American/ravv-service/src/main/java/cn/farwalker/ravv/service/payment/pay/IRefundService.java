package cn.farwalker.ravv.service.payment.pay;

import cn.farwalker.ravv.service.paypal.RefundForm;

/**
 * Created by asus on 2019/3/13.
 */
public interface IRefundService {
    /**
     * 退款相关接口
     * @param
     * @param refundForm
     * @return
     */
    public String refund(RefundForm refundForm) throws Exception;
}
