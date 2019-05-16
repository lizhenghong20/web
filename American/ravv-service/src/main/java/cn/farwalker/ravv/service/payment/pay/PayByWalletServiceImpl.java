package cn.farwalker.ravv.service.payment.pay;

import cn.farwalker.ravv.service.order.constants.PayStatusEnum;
import cn.farwalker.ravv.service.order.constants.PaymentModeEnum;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderPaymentService;
import cn.farwalker.ravv.service.order.paymemt.biz.IOrderPaymemtBiz;
import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;
import cn.farwalker.ravv.service.payment.paymentlog.biz.IMemberPaymentLogBiz;
import cn.farwalker.ravv.service.paypal.PaymentForm;
import cn.farwalker.ravv.service.paypal.PaymentResultVo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.Condition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * Created by asus on 2019/3/5.
 */
@Slf4j
@Service("payByWalletServiceImpl")
public class PayByWalletServiceImpl implements IPayService {

    @Autowired
    IOrderPaymentService orderPaymentService;

    @Autowired
    private IOrderPaymemtBiz iOrderPaymemtBiz;

    @Autowired
    IMemberPaymentLogBiz iMemberPaymentLogBiz;



    @Autowired
    private IOrderPaymentService iOrderPaymentService;

    /**
     * 订单支付，参数中有金额信息，正常进行支付的接口
     * @param
     * @return
     */
    @Override
    public JsonResult<PaymentResultVo> payByAll(HttpServletRequest request, PaymentForm paymentEx){
        log.info("==============订单总价total:{}============", paymentEx.getOrderTotal());
        log.info("==============商品总价subtotal:{}============", paymentEx.getSubTotal());
        log.info("==============税金:{}============", paymentEx.getTax());
        log.info("==============运费:{}============", paymentEx.getShipping());
        if (null == paymentEx || null == paymentEx.getOrderId() || Tools.string.isEmpty( paymentEx.getPayPassword())) {
            log.error("======================invalid parameter error==================");
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        }
        Long memberId;
        if (request.getSession().getAttribute("memberId") == null) {
            throw new WakaException(RavvExceptionEnum.TOKEN_PARSE_MEMBER_ID_FAILED);
        }

        if(!iOrderPaymentService.isOrderValidForPay(paymentEx.getOrderId())){
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        }

        memberId = (Long) request.getSession().getAttribute("memberId");
        paymentEx.setMemberId(memberId);
        BigDecimal total =  Tools.bigDecimal.add(paymentEx.getSubTotal(),paymentEx.getShipping(),paymentEx.getTax()).setScale(2, BigDecimal.ROUND_HALF_UP);
        if(!total.equals(paymentEx.getOrderTotal().setScale(2,BigDecimal.ROUND_HALF_UP)))
            throw new WakaException("生成支付订单的金额不合法");
       return orderPaymentService.updatePayFromAdvance(paymentEx);
    }

    /**
     * 订单支付,参数只有orderID,应用场景在于对取消的订单中的商品做批量的购买
     * @param
     * @return
     */
    @Override
    public JsonResult<PaymentResultVo> payByOrder(HttpServletRequest request, Long orderId,String payPassword){
        if(orderId == null || orderId == 0 || Tools.string.isEmpty(payPassword) )
            throw new WakaException("invalid orderId");
//        if(Tools.string.isEmpty(payPassword) ){
//            throw new WakaException("invalid password");
//        }
        OrderPaymemtBo query = iOrderPaymemtBiz.selectOne(Condition.create().eq(OrderPaymemtBo.Key.orderId.toString(),orderId)
                .eq(OrderPaymemtBo.Key.payStatus.toString(), PayStatusEnum.UNPAY.toString())
                .eq(OrderPaymemtBo.Key.payMode.toString(), PaymentModeEnum.ONLINE.toString()));
        if(query == null)
            throw new WakaException("can not find valid order");
        PaymentForm  paymentForm = new PaymentForm();
        paymentForm.setOrderId(orderId);
        paymentForm.setOrderTotal(query.getShouldPayTotalFee() == null ? BigDecimal.ZERO : query.getShouldPayTotalFee());
        paymentForm.setSubTotal(query.getGoodsTotalFee() == null ? BigDecimal.ZERO : query.getGoodsTotalFee());
        paymentForm.setShipping(query.getPostFee() == null ? BigDecimal.ZERO : query.getPostFee());
        paymentForm.setTax(query.getTaxFee() == null ? BigDecimal.ZERO : query.getTaxFee());
        paymentForm.setPayPassword(payPassword);
        return orderPaymentService.updatePayFromAdvance(paymentForm);
    }






}
