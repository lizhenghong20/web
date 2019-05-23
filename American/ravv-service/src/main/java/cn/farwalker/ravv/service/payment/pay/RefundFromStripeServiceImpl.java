package cn.farwalker.ravv.service.payment.pay;

import cn.farwalker.ravv.service.order.constants.PayStatusEnum;
import cn.farwalker.ravv.service.order.constants.PaymentPlatformEnum;
import cn.farwalker.ravv.service.payment.paymentlog.biz.IMemberPaymentLogBiz;
import cn.farwalker.ravv.service.payment.paymentlog.model.MemberPaymentLogBo;
import cn.farwalker.ravv.service.paypal.RefundForm;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.Condition;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("refundFromStripeServiceImpl")
public class RefundFromStripeServiceImpl implements IRefundService{

    @Autowired
    private IMemberPaymentLogBiz iMemberPaymentLogBiz;

    @Override
    public String refund(RefundForm refundForm) throws Exception {
        if(refundForm.getRefundTotalAmount().doubleValue() < 0 || refundForm.getOrderId() == null
                || refundForm.getMemberId()==null)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        MemberPaymentLogBo queryLogBo = iMemberPaymentLogBiz.selectOne(Condition.create()
                .eq(MemberPaymentLogBo.Key.orderId.toString(),refundForm.getOrderId())
                .eq(MemberPaymentLogBo.Key.memberId.toString(),refundForm.getMemberId())
                .eq(MemberPaymentLogBo.Key.status.toString(), PayStatusEnum.PAID.toString())
                .eq(MemberPaymentLogBo.Key.payType.toString(), PaymentPlatformEnum.PayPal.getKey()));
        if(queryLogBo == null)
            throw new WakaException("当前参数查询不到支付信息");

        if(refundForm.getRefundTotalAmount().doubleValue() != 0)
            if( refundForm.getRefundTotalAmount().doubleValue() >  queryLogBo.getTotalAmount().doubleValue())
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);

        //使用payment_intent_id重新获取paymentIntent
        PaymentIntent intent = PaymentIntent.retrieve(queryLogBo.getStripePaymentId());
        String chargeId = intent.getCharges().getData().get(0).getId();
        //退款金额使用美分
        BigDecimal refundAmount = refundForm.getRefundTotalAmount().multiply(new BigDecimal(100))
                .setScale(0, BigDecimal.ROUND_HALF_UP);
        Map<String, Object> params = new HashMap<>();
        params.put("amount", refundAmount);
        params.put("charge", chargeId);
        Refund refund = Refund.create(params);
        if(!"succeeded".equals(refund.getStatus())){
            throw new WakaException("退款失败");
        }
        MemberPaymentLogBo insertBo = new MemberPaymentLogBo();
        Tools.bean.copyProperties(queryLogBo,insertBo);
        insertBo.setStatus(PayStatusEnum.REFUND);
        insertBo.setReturnOrderId(refundForm.getReturnOrderId());
        insertBo.setRefundTime(new Date());
        insertBo.setId(null);
        insertBo.setGmtModified(new Date());
        insertBo.setGmtCreate(new Date());
        insertBo.setPayedTime(null);
        insertBo.setRefundTotalAmount(refundForm.getRefundTotalAmount());
        if(!iMemberPaymentLogBiz.insert(insertBo)){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        return "refund success!";
    }
}
