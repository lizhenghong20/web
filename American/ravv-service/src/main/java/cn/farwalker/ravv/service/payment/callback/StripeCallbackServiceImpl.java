package cn.farwalker.ravv.service.payment.callback;

import cn.farwalker.ravv.service.order.constants.PayStatusEnum;
import cn.farwalker.ravv.service.order.constants.PaymentPlatformEnum;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoBiz;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderPaymentService;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.order.paymemt.biz.IOrderPaymemtBiz;
import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;
import cn.farwalker.ravv.service.payment.paymentlog.biz.IMemberPaymentLogBiz;
import cn.farwalker.ravv.service.payment.paymentlog.biz.impl.MemberPaymentLogBizImpl;
import cn.farwalker.ravv.service.payment.paymentlog.model.MemberPaymentLogBo;
import cn.farwalker.waka.core.HttpKit;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import com.baomidou.mybatisplus.mapper.Condition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@Service
public class StripeCallbackServiceImpl implements StripeCallbackService {

    @Autowired
    private IOrderPaymemtBiz orderPaymemtBiz;

    @Autowired
    private IMemberPaymentLogBiz memberPaymentLogBiz;

    @Autowired
    private IOrderInfoBiz orderInfoBiz;

    @Autowired
    private IOrderPaymentService orderPaymentService;

    @Override
    public void doSuccess(Long orderId, String paymentIntentId) {
        HttpSession sin = HttpKit.getRequest().getSession();
        //根据orderId查出订单详细信息
        OrderPaymemtBo orderPaymemtBo = orderPaymemtBiz.selectOne(Condition.create()
                                                .eq(OrderPaymemtBo.Key.orderId.toString(), orderId));
        OrderInfoBo orderInfoBo = orderInfoBiz.selectById(orderId);
        MemberPaymentLogBo payLogBo = new MemberPaymentLogBo();
        payLogBo.setStripePaymentId(paymentIntentId);
        payLogBo.setMemberId(orderInfoBo.getBuyerId());
        payLogBo.setOrderId(orderId);
        payLogBo.setStatus(PayStatusEnum.PAID);
        payLogBo.setPayType(PaymentPlatformEnum.Stripe);
        payLogBo.setIp(HttpKit.getIp());
        payLogBo.setTotalAmount(orderPaymemtBo.getShouldPayTotalFee());
        payLogBo.setTax(orderPaymemtBo.getTaxFee());
        payLogBo.setSubtotal(orderPaymemtBo.getGoodsTotalFee());
        payLogBo.setShipping(orderPaymemtBo.getPostFee());
        payLogBo.setGmtCreate(new Date());
        payLogBo.setGmtModified(new Date());
        payLogBo.setPayedTime(new Date());
        //插入到paylog表中
        if(!memberPaymentLogBiz.insert(payLogBo)){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        //执行系统支付回调
        if(!orderPaymentService.updatePaymentCallback(orderId, PaymentPlatformEnum.Stripe, payLogBo))
            throw new WakaException("Callback fail");
    }
}
