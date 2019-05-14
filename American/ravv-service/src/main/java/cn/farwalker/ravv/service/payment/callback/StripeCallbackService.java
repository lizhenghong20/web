package cn.farwalker.ravv.service.payment.callback;

public interface StripeCallbackService {

    //记录intentid到数据库，在stripe退款时用到
    public void doSuccess(Long orderId, String paymentIntentId);
}
