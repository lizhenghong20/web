package cn.farwalker.ravv.service.payment.callback;

public interface StripeCallbackService {

    public void doSuccess(Long orderId);
}
