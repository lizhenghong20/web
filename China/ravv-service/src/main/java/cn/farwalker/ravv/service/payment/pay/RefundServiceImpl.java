package cn.farwalker.ravv.service.payment.pay;

import cn.farwalker.ravv.service.order.constants.PaymentPlatformEnum;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoService;
import cn.farwalker.ravv.service.paypal.RefundForm;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by asus on 2019/3/13.
 */
@Service("refundServiceImpl")
public class RefundServiceImpl implements IRefundService {

    @Autowired
    @Qualifier("refundFromPayPalServiceImpl")
    IRefundService iRefundFromPayPal;

    @Autowired
    @Qualifier("refundFromWalletServiceImpl")
    IRefundService iRefundFromWallet;

    @Autowired
    @Qualifier("refundFromWechatServiceImpl")
    IRefundService iRefundFromWechat;

    @Autowired
    IOrderInfoService iOrderInfoService;

    @Override
    public String refund(RefundForm refundForm) throws Exception{

        PaymentPlatformEnum  payType = iOrderInfoService.getPayType(refundForm.getOrderId());
        if(PaymentPlatformEnum.PayPal.equals(payType)){
            return iRefundFromPayPal.refund(refundForm);
        }

        if(PaymentPlatformEnum.Advance.equals(payType)){
            return iRefundFromWallet.refund(refundForm);
        }

        if(PaymentPlatformEnum.WECHATAPP.equals(payType)){
            return iRefundFromWechat.refund(refundForm);
        }

        throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
    }
}
