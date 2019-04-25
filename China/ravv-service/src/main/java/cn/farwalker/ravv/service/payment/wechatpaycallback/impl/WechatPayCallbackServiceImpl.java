package cn.farwalker.ravv.service.payment.wechatpaycallback.impl;

import cn.farwalker.ravv.service.order.constants.PayStatusEnum;
import cn.farwalker.ravv.service.order.constants.PaymentModeEnum;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoBiz;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderPaymentService;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.order.paymemt.biz.IOrderPaymemtBiz;
import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;
import cn.farwalker.ravv.service.payment.paymentlog.biz.IMemberPaymentLogBiz;
import cn.farwalker.ravv.service.payment.paymentlog.model.MemberPaymentLogBo;
import cn.farwalker.ravv.service.payment.wechatpaycallback.WechatPayCallbackService;
import cn.farwalker.waka.core.WakaException;
import com.baomidou.mybatisplus.mapper.Condition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class WechatPayCallbackServiceImpl implements WechatPayCallbackService {

    @Autowired
    private IOrderPaymemtBiz orderPaymemtBiz;

    @Autowired
    private IMemberPaymentLogBiz memberPaymentLogBiz;

    @Autowired
    private IOrderPaymentService orderPaymentService;

    @Autowired
    private IOrderInfoBiz orderInfoBiz;

    @Override
    public void doSuccess(Map<String, String> map) {
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMddHHmmss");
        Long orderId = Long.parseLong(map.get("out_trade_no"));
        String total_fee = map.get("total_fee");
        String transactionId = map.get("transaction_id");
        String openId = map.get("openid");
        String time_end = map.get("time_end");
        Date payedTime = null;
        try {
            payedTime = sdf.parse(time_end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        BigDecimal fee = new BigDecimal(total_fee).multiply(new BigDecimal(0.01)).setScale(2, BigDecimal.ROUND_DOWN);

        //根据orderId查询orderPaymentBo
        OrderPaymemtBo query = orderPaymemtBiz.selectOne(Condition.create().eq(OrderPaymemtBo.Key.orderId.toString(),orderId)
                .eq(OrderPaymemtBo.Key.payStatus.toString(), PayStatusEnum.UNPAY.toString())
                .eq(OrderPaymemtBo.Key.payMode.toString(), PaymentModeEnum.ONLINE.toString()));
        if(query == null){
            log.info("微信可能重复回调:" + orderId);
            OrderPaymemtBo orderPaymemtBo = orderPaymemtBiz.selectOne(Condition.create().eq(OrderPaymemtBo.Key.orderId.toString(),orderId)
                    .eq(OrderPaymemtBo.Key.payStatus.toString(), PayStatusEnum.PAID.toString())
                    .eq(OrderPaymemtBo.Key.payMode.toString(), PaymentModeEnum.ONLINE.toString()));
            if(orderPaymemtBo == null){
                throw new WakaException("未找到该订单:" + orderId);
            }
            return;
        }
        //根据订单id查出memberId
        OrderInfoBo orderInfoBo = orderInfoBiz.selectById(query.getOrderId());
        if(orderInfoBo == null){
            throw new WakaException("订单不存在");
        }

        MemberPaymentLogBo memberPaymentLogBo =  new MemberPaymentLogBo();
        memberPaymentLogBo.setPayType(query.getBuyerPaymentType());
        memberPaymentLogBo.setStatus(PayStatusEnum.PAID);
        memberPaymentLogBo.setOrderId(orderId);
        memberPaymentLogBo.setTotalAmount(fee);
        memberPaymentLogBo.setSubtotal(query.getGoodsTotalFee());
        memberPaymentLogBo.setShipping(query.getPostFee());
        memberPaymentLogBo.setMemberId(orderInfoBo.getBuyerId());
        memberPaymentLogBo.setPayerId(openId);
        memberPaymentLogBo.setPaymentId(transactionId);
        memberPaymentLogBo.setPayedTime(payedTime);

        if(!memberPaymentLogBiz.insert(memberPaymentLogBo)){
            throw new WakaException("memberPaymentLogBo插入出错");
        }
        orderPaymentService.updateOrderAfterPay(orderId, query.getBuyerPaymentType(), memberPaymentLogBo);
    }
}
