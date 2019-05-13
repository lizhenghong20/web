package cn.farwalker.ravv.service.payment.pay;

import cn.farwalker.ravv.service.order.constants.PayStatusEnum;
import cn.farwalker.ravv.service.order.constants.PaymentModeEnum;
import cn.farwalker.ravv.service.order.constants.PaymentPlatformEnum;
import cn.farwalker.ravv.service.order.paymemt.biz.IOrderPaymemtBiz;
import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;
import cn.farwalker.ravv.service.paypal.PaymentForm;
import cn.farwalker.ravv.service.paypal.PaymentResultVo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.WakaException;
import com.baomidou.mybatisplus.mapper.Condition;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service("payByStripeServiceImpl")
public class PayByStripeServiceImpl implements IPayService {

    @Autowired
    private IOrderPaymemtBiz iOrderPaymemtBiz;


    @Override
    public JsonResult<PaymentResultVo> payByAll(HttpServletRequest request, PaymentForm paymentEx) throws Exception {
        return null;
    }

    @Override
    public JsonResult<PaymentResultVo> payByOrder(HttpServletRequest request, Long orderId, String payPassword) throws Exception {
        if(orderId == null || orderId == 0)
            throw new WakaException("orderId 不合法");
        OrderPaymemtBo query = iOrderPaymemtBiz.selectOne(Condition.create().eq(OrderPaymemtBo.Key.orderId.toString(),orderId)
                .eq(OrderPaymemtBo.Key.payStatus.toString(), PayStatusEnum.UNPAY.toString())
                .eq(OrderPaymemtBo.Key.payMode.toString(), PaymentModeEnum.ONLINE.toString()));
        if(query == null)
            throw new WakaException("找不到符合条件的订单");
        //查出金额等信息发送到stripe
        Stripe.apiKey = "sk_test_aAcqEpgzPmQYqplFVNErNS3U004xRfetnl";
        Map<String, Object> paymentIntentParams = new HashMap<String, Object>();
        //总价字段使用美分计算
        BigDecimal amount = query.getShouldPayTotalFee().multiply(new BigDecimal(100))
                .setScale(0, BigDecimal.ROUND_HALF_UP);
        paymentIntentParams.put("amount", amount);
        paymentIntentParams.put("currency", "usd");
        ArrayList<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");
        paymentIntentParams.put("payment_method_types", paymentMethodTypes);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("orderId", orderId.toString());
        paymentIntentParams.put("metadata", metadata);
        log.info("=====================before paymentintent");
        PaymentIntent intent = PaymentIntent.create(paymentIntentParams);
        log.info("=====================after paymentintent");
        log.info("=====================this intent id:{}", intent.getId());
        PaymentResultVo paymentResultVo = new PaymentResultVo();
        paymentResultVo.setClientSecret(intent.getClientSecret());
        paymentResultVo.setAmount(query.getShouldPayTotalFee());
        paymentResultVo.setOrderId(orderId);
        paymentResultVo.setPayType(PaymentPlatformEnum.Stripe.getKey());
        return JsonResult.newSuccess(paymentResultVo);
    }
}
