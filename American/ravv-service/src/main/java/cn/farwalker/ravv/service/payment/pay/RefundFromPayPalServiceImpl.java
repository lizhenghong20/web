package cn.farwalker.ravv.service.payment.pay;

import cn.farwalker.ravv.service.paypal.RefundForm;
import cn.farwalker.ravv.service.paypal.service.IPaypalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by asus on 2019/3/13.
 */
@Slf4j
@Service("refundFromPayPalServiceImpl")
public class RefundFromPayPalServiceImpl implements IRefundService {

    @Autowired
    IPaypalService paypalService;

    @Override
    public String refund(RefundForm refundForm) throws Exception{
        log.info("=========退款总金额==========：{}",refundForm.getRefundTotalAmount());
        log.info("=========订单号==========：{}",refundForm.getOrderId());
        log.info("=========退款单号==========：{}",refundForm.getReturnOrderId());
        log.info("=========退款人的MemberId==========：{}",refundForm.getMemberId());
        return paypalService.refund(refundForm);
    }
}
