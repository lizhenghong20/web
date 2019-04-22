package cn.farwalker.ravv.service.paypal.service;


import cn.farwalker.ravv.service.order.constants.PayStatusEnum;
import cn.farwalker.ravv.service.order.constants.PaymentPlatformEnum;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderPaymentService;
import cn.farwalker.ravv.service.order.paymemt.biz.IOrderPaymemtBiz;
import cn.farwalker.ravv.service.payment.paymentlog.biz.IMemberPaymentLogBiz;
import cn.farwalker.ravv.service.payment.paymentlog.model.MemberPaymentLogBo;
import cn.farwalker.ravv.service.paypal.*;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.Condition;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by asus on 2018/12/12.
 */
@Slf4j
@Service
public class PaypalServiceImpl implements IPaypalService {

    @Autowired
    private PaypalConfig paypalConfig;

    @Autowired
    private IOrderPaymentService iOrderPaymentService;

    @Autowired
    private IMemberPaymentLogBiz iMemberPaymentLogBiz;

    @Autowired
    private IOrderPaymemtBiz iOrderPaymemtBiz;

    private static final String CURRENCY = "USD";
    private static final String COMPLETED_STATUS = "completed";


    @Override
    public PaymentResultVo getPaypalUrl(PaymentForm paymentEx, String returnURL, String cancelUrl) throws Exception{
        BigDecimal total =  Tools.bigDecimal.add(paymentEx.getSubTotal(),paymentEx.getShipping(),paymentEx.getTax()).setScale(2, BigDecimal.ROUND_HALF_UP);
        if(!total.equals(paymentEx.getOrderTotal().setScale(2,BigDecimal.ROUND_HALF_UP)))
            throw new WakaException("生成支付订单的金额不合法");
        Payment payment = createPayment(paymentEx,returnURL,cancelUrl);
        for(Links links : payment.getLinks()){
            if(links.getRel().equals("approval_url")){
                PaymentResultVo vo = new PaymentResultVo();
                vo.setUrl(links.getHref());
                vo.setOrderId(paymentEx.getOrderId());
                return vo ;//重定向到支付订单验证链接，用户进行PayPal登录，并授权下单，有异常则下单不成功
            }
        }
        return null;
    }


    /**
     * 创建支付订单，在订单创建成功后请求PayPal执行
     * @param cancelUrl
     * @param returnUrl
     * @return
     * @throws PayPalRESTException
     */
    public Payment createPayment(PaymentForm paymentEx, String returnUrl, String cancelUrl) throws Exception {

        log.info("开始创建payment");
        Transaction transaction = new Transaction();

        //将memberId 存在Description中
        if(paymentEx.getMemberId() != null)
            transaction.setDescription(paymentEx.getMemberId().toString());
        // 将我们的订单ID保存到支付信息中，用于后面支付回传
        if (null != paymentEx.getOrderId()) {
            transaction.setCustom(paymentEx.getOrderId().toString());
        }
        /**
         * 订单价格
         */
        Amount amount = new Amount();
        amount.setCurrency(CURRENCY);
        // 支付的总价，paypal会校验 total = subTotal + tax + ...
        amount.setTotal(paymentEx.getOrderTotal().toString());

        // 设置各种费用
        Details details = new Details();
        // 商品总价
        details.setSubtotal(paymentEx.getSubTotal().toString());
        // 税费
        details.setTax(paymentEx.getTax().toString());
        //运费
        details.setShipping(paymentEx.getShipping().toString());

        amount.setDetails(details);
        transaction.setAmount(amount);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        /**
         * 支付信息
         */
        Payer payer = new Payer();
        payer.setPaymentMethod(PaypalPaymentMethod.paypal.toString());

        Payment payment = new Payment();
        payment.setIntent(PaypalPaymentIntent.sale.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        /**
         * 回调地址
         */
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setReturnUrl(returnUrl);
        redirectUrls.setCancelUrl(cancelUrl);
        payment.setRedirectUrls(redirectUrls);
        //为防止token过期,每次请求都重新取accesstoken
        createAPIContext();
        return payment.create(paypalConfig.getApiContext());
    }



    /**
     * 执行支付
     * @param paymentId
     * @param payerId
     * @return
     * @throws PayPalRESTException
     */
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        log.info("开始执行支付");
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(paypalConfig.getApiContext(), paymentExecute);
    }

    public String refund(RefundForm refundForm) throws Exception{
        if(refundForm.getRefundTotalAmount().doubleValue() < 0 || refundForm.getOrderId() == null || refundForm.getMemberId()==null)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        MemberPaymentLogBo  queryLogBo = iMemberPaymentLogBiz.selectOne(Condition.create().eq(MemberPaymentLogBo.Key.orderId.toString(),refundForm.getOrderId())
                                                                .eq(MemberPaymentLogBo.Key.memberId.toString(),refundForm.getMemberId())
                                                                .eq(MemberPaymentLogBo.Key.status.toString(),PayStatusEnum.PAID.toString())
                                                                .eq(MemberPaymentLogBo.Key.payType.toString(),PaymentPlatformEnum.PayPal.getKey()));
        if(queryLogBo == null)
            throw new WakaException("当前参数查询不到支付信息");

        if(refundForm.getRefundTotalAmount().doubleValue() != 0)
            if( refundForm.getRefundTotalAmount().doubleValue() >  queryLogBo.getTotalAmount().doubleValue())
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);

        Sale sale = new Sale();
        sale.setId(queryLogBo.getSaleId());
        Refund refund = new Refund();

        if(refundForm.getRefundTotalAmount().doubleValue() != 0){
            Amount amount = new Amount();
            amount.setCurrency("USD");
            //在paypal的请求中如果不是全额退款，才传amount.setTotal
            amount.setTotal(refundForm.getRefundTotalAmount().setScale(2,BigDecimal.ROUND_HALF_UP).toString());
            refund.setAmount(amount);
        }

        //为防止token过期,每次请求都重新取accesstoken
        createAPIContext();
        log.info("================请求退款===================== returnOrderId:{}",refundForm.getReturnOrderId());
        sale.refund(paypalConfig.getApiContext(), refund);
        log.info("================退款成功===================== returnOrderId:{}",refundForm.getReturnOrderId());
        MemberPaymentLogBo insertBo = new MemberPaymentLogBo();
        Tools.bean.copyProperties(queryLogBo,insertBo);
        insertBo.setStatus(PayStatusEnum.REFUND);
        insertBo.setReturnOrderId(refundForm.getReturnOrderId());
        insertBo.setRefundTime(new Date());
        insertBo.setId(null);
        insertBo.setGmtModified(null);
        insertBo.setGmtCreate(null);
        insertBo.setPayedTime(null);
        //部分退款更新为输入值，全额退款更新为查询值
        if(refundForm.getRefundTotalAmount().doubleValue() != 0){
            insertBo.setRefundTotalAmount(refundForm.getRefundTotalAmount().setScale(2,BigDecimal.ROUND_HALF_UP));
        }else{
            insertBo.setRefundTotalAmount(queryLogBo.getTotalAmount());
        }
        iMemberPaymentLogBiz.insert(insertBo);
        return "refund success!";
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public  void updatePayment(Payment payment){
        log.info("payment:{}", Tools.json.toJson(payment));
        log.info("开始更新支付日志");

        Transaction transaction = payment.getTransactions().get(0);
        if(transaction == null){
            log.info("获取transaction 失败");
            throw new WakaException(RavvExceptionEnum.PAY_LOG_UPDATE_FAILED);

        }
        //支付的时候保留memberId的字段
        Long memberId = null;
        if(! Tools.string.isEmpty( transaction.getDescription()))
            memberId  = Long.parseLong(transaction.getDescription());
        Payer payer = payment.getPayer();
        Payee payee = transaction.getPayee();
        RelatedResources relatedResources = transaction.getRelatedResources().get(0);
        if(relatedResources == null){
            log.info("获取relatedResources 失败");
            throw new WakaException(RavvExceptionEnum.PAY_LOG_UPDATE_FAILED);

        }

        Sale sale = relatedResources.getSale();
        if(sale == null){
            log.info("获取sale 失败");
            throw new WakaException(RavvExceptionEnum.PAY_LOG_UPDATE_FAILED);
        }

        Long orderId = 0L;

        MemberPaymentLogBo memberPaymentLogBo =  new MemberPaymentLogBo();
        memberPaymentLogBo.setPayType(PaymentPlatformEnum.PayPal);
        memberPaymentLogBo.setStatus(PayStatusEnum.PAID);
        //在支付订单中transaction.getCustom() 中存的是orderId,在退货订单中存的是 returnOrderId
        orderId = Long.parseLong(transaction.getCustom());
        memberPaymentLogBo.setOrderId(orderId);
        memberPaymentLogBo.setTotalAmount(new BigDecimal(transaction.getAmount().getTotal()));
        memberPaymentLogBo.setTax(new BigDecimal(transaction.getAmount().getDetails().getTax()));
        memberPaymentLogBo.setSubtotal(new BigDecimal(transaction.getAmount().getDetails().getSubtotal()));
        memberPaymentLogBo.setShipping(new BigDecimal(transaction.getAmount().getDetails().getShipping()));
        memberPaymentLogBo.setCurrencyCode(transaction.getAmount().getCurrency());

        memberPaymentLogBo.setPayerId(payer.getPayerInfo().getPayerId());
        memberPaymentLogBo.setPayerEmail(payer.getPayerInfo().getEmail());
        memberPaymentLogBo.setPayeeMerchantId(payee.getMerchantId());
        memberPaymentLogBo.setPayeeEmail(payee.getEmail());

        memberPaymentLogBo.setPaymentId(payment.getId());
        memberPaymentLogBo.setSaleId(sale.getId());
        memberPaymentLogBo.setMemberId(memberId);
        memberPaymentLogBo.setPayedTime(new Date());

        if(!iMemberPaymentLogBiz.insert(memberPaymentLogBo))
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        iOrderPaymentService.updateOrderAfterPay(orderId,PaymentPlatformEnum.PayPal,memberPaymentLogBo);

    }
    public boolean isPaySuccess(Long memberId,Long orderId){
         MemberPaymentLogBo query =  iMemberPaymentLogBiz.selectOne(Condition.create().eq(MemberPaymentLogBo.Key.memberId.toString(),memberId)
                 .eq(MemberPaymentLogBo.Key.orderId.toString(),orderId)
                .eq(MemberPaymentLogBo.Key.status.toString(),PayStatusEnum.PAID.toString())
                 .eq(MemberPaymentLogBo.Key.payType.toString(),PaymentPlatformEnum.PayPal.getKey()));
        return query != null;
    }

    private void createAPIContext() throws Exception{
        Map<String, String> sdkConfig = new HashMap<>();
        sdkConfig.put("mode", paypalConfig.getMode());
        OAuthTokenCredential oAuthTokenCredential =   new OAuthTokenCredential(paypalConfig.getClientId(), paypalConfig.getClientSecret(), sdkConfig);
        String  token = oAuthTokenCredential.getAccessToken();
        APIContext apiContext = new APIContext(token);
        apiContext.setConfigurationMap(sdkConfig);
        paypalConfig.setApiContext(apiContext);
        return ;
    }



}

