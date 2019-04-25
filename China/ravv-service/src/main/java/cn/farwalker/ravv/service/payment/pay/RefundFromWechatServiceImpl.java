package cn.farwalker.ravv.service.payment.pay;

import cn.farwalker.ravv.service.order.constants.PayStatusEnum;
import cn.farwalker.ravv.service.order.constants.PaymentPlatformEnum;
import cn.farwalker.ravv.service.payment.payconfig.biz.IPayConfigBiz;
import cn.farwalker.ravv.service.payment.payconfig.model.PayConfigBo;
import cn.farwalker.ravv.service.payment.paymentlog.biz.IMemberPaymentLogBiz;
import cn.farwalker.ravv.service.payment.paymentlog.model.MemberPaymentLogBo;
import cn.farwalker.ravv.service.paypal.RefundForm;
import cn.farwalker.waka.components.wechatpay.mp.api.WxMpInMemoryConfigStorage;
import cn.farwalker.waka.components.wechatpay.mp.api.WxMpService;
import cn.farwalker.waka.components.wechatpay.mp.bean.result.WxMpRefundResult;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.Condition;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Date;

@Slf4j
@Service("refundFromWechatServiceImpl")
public class RefundFromWechatServiceImpl implements IRefundService {

    @Autowired
    private IMemberPaymentLogBiz memberPaymentLogBiz;

    @Autowired
    private IPayConfigBiz payConfigBiz;

    @Autowired
    private WxMpService wxMpService;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String refund(RefundForm refundForm) throws Exception {
        if(refundForm.getRefundTotalAmount().doubleValue() < 0 || refundForm.getOrderId() == null
                                                                        || refundForm.getMemberId()==null)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        MemberPaymentLogBo queryLogBo = memberPaymentLogBiz.selectOne(Condition.create()
                .eq(MemberPaymentLogBo.Key.orderId.toString(), refundForm.getOrderId())
                .eq(MemberPaymentLogBo.Key.memberId.toString(),refundForm.getMemberId())
                .eq(MemberPaymentLogBo.Key.status.toString(), PayStatusEnum.PAID.toString())
                .eq(MemberPaymentLogBo.Key.payType.toString(), PaymentPlatformEnum.WechatApp.getKey()));
        if(queryLogBo == null)
            throw new WakaException("当前参数查询不到支付信息");
        if(refundForm.getRefundTotalAmount().doubleValue() != 0)
            if( refundForm.getRefundTotalAmount().doubleValue() >  queryLogBo.getTotalAmount().doubleValue())
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);

        //微信退款
        if(!doWechatRefund(refundForm, queryLogBo)){
            throw new WakaException("退款失败");
        }

        if(!insertMemberPaymentBo(refundForm, queryLogBo)){
            throw new WakaException("更新退款日志失败");
        }

        return "refund success";
    }

    private Boolean doWechatRefund(RefundForm refundForm, MemberPaymentLogBo queryLogBo) throws KeyStoreException,
                                                                IOException, UnrecoverableKeyException,
                                                                NoSuchAlgorithmException, KeyManagementException {
        //设置微信支付参数
        PayConfigBo config = payConfigBiz.selectOne(Condition.create()
                                        .eq(PayConfigBo.Key.payType.toString(), "APP"));
        WxMpInMemoryConfigStorage wxConfig = new WxMpInMemoryConfigStorage();
        wxConfig.setAppId(config.getAppId());
        wxConfig.setSecret(config.getAppSecret());
        wxConfig.setPartnerId(config.getMchId());
        wxConfig.setPartnerKey(config.getApiKey());
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        FileInputStream instream = null;
        try {
            instream = new FileInputStream(new File("/opt/cert/apiclient_cert.p12"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            keyStore.load(instream, config.getMchId().toCharArray());
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            instream.close();
        }
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, config.getMchId().toCharArray())
                .build();

        wxConfig.setSSLContext(sslcontext);
        wxMpService.setWxMpConfigStorage(wxConfig);
        BigDecimal refundFee = refundForm.getRefundTotalAmount().multiply(new BigDecimal(100))
                .setScale(0, BigDecimal.ROUND_HALF_UP);
        BigDecimal totalFee = queryLogBo.getTotalAmount().multiply(new BigDecimal(100))
                .setScale(0, BigDecimal.ROUND_HALF_UP);

        WxMpRefundResult refundResult = wxMpService.getJSSDKRefundResult(queryLogBo.getPaymentId(),
                refundForm.getReturnOrderId().toString(), totalFee, refundFee, "");

        if (!"SUCCESS".equals(refundResult.getReturnCode())) {
            log.error("签名失败");
            throw new WakaException("签名失败");
        }
        if (!"SUCCESS".equals(refundResult.getResultCode())) {
            log.error("提交业务失败");
            throw new WakaException("提交业务失败");
        }
        return true;
    }

    private Boolean insertMemberPaymentBo(RefundForm refundForm, MemberPaymentLogBo queryLogBo){
        MemberPaymentLogBo insertBo = new MemberPaymentLogBo();
        Tools.bean.copyProperties(queryLogBo,insertBo);
        insertBo.setStatus(PayStatusEnum.REFUND);
        insertBo.setReturnOrderId(refundForm.getReturnOrderId());
        insertBo.setRefundTime(new Date());
        insertBo.setId(null);
        insertBo.setGmtModified(new Date());
        insertBo.setGmtCreate(new Date());
        insertBo.setPayedTime(null);
        //部分退款更新为输入值，全额退款更新为查询值
        if(refundForm.getRefundTotalAmount().doubleValue() != 0){
            insertBo.setRefundTotalAmount(refundForm.getRefundTotalAmount().setScale(2,BigDecimal.ROUND_HALF_UP));
        }else{
            insertBo.setRefundTotalAmount(queryLogBo.getTotalAmount());
        }
        return memberPaymentLogBiz.insert(insertBo);
    }
}
