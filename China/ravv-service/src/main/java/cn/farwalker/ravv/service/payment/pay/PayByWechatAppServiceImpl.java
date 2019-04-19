package cn.farwalker.ravv.service.payment.pay;

import cn.farwalker.ravv.service.order.constants.PayStatusEnum;
import cn.farwalker.ravv.service.order.constants.PaymentModeEnum;
import cn.farwalker.ravv.service.order.constants.PaymentPlatformEnum;
import cn.farwalker.ravv.service.order.paymemt.biz.IOrderPaymemtBiz;
import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;
import cn.farwalker.ravv.service.payment.payconfig.biz.IPayConfigBiz;
import cn.farwalker.ravv.service.payment.payconfig.model.PayConfigBo;
import cn.farwalker.ravv.service.paypal.PaymentForm;
import cn.farwalker.ravv.service.paypal.PaymentResultVo;
import cn.farwalker.ravv.service.paypal.WechatPayForm;
import cn.farwalker.waka.components.wechatpay.mp.api.WxMpInMemoryConfigStorage;
import cn.farwalker.waka.components.wechatpay.mp.api.WxMpService;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.Condition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service("payByWechatAppServiceImpl")
public class PayByWechatAppServiceImpl implements IPayService {

    @Autowired
    private IOrderPaymemtBiz orderPaymemtBiz;

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private IPayConfigBiz payConfigBiz;

    @Override
    public JsonResult<PaymentResultVo> payByAll(HttpServletRequest request, PaymentForm paymentEx) throws Exception {
        return null;
    }

    @Override
    public JsonResult<PaymentResultVo> payByOrder(HttpServletRequest request, Long orderId, String payPassword) throws Exception {
        //根据订单id查询支付日志
        if(orderId == null || orderId == 0)
            throw new WakaException("invalid orderId");
        OrderPaymemtBo query = orderPaymemtBiz.selectOne(Condition.create().eq(OrderPaymemtBo.Key.orderId.toString(),orderId)
                .eq(OrderPaymemtBo.Key.payStatus.toString(), PayStatusEnum.UNPAY.toString())
                .eq(OrderPaymemtBo.Key.payMode.toString(), PaymentModeEnum.ONLINE.toString()));
        if(query == null)
            throw new WakaException("can not find valid order");
        //更新用户选择的支付方式
        query.setBuyerPaymentType(PaymentPlatformEnum.WechatApp);
        if(!orderPaymemtBiz.updateById(query)){
            throw new WakaException("支付方式更新失败");
        }
        String ip = Tools.clientIP.getRemoteHost(request);
        WechatPayForm wechatPayForm = getWechatPayInfo(query, ip);
        PaymentResultVo resultVo = new PaymentResultVo();
        resultVo.setPayType(PaymentPlatformEnum.WechatApp.getKey());
        resultVo.setOrderId(query.getOrderId());
        resultVo.setWechatPayForm(wechatPayForm);

        return JsonResult.newSuccess(resultVo);
    }

    private WechatPayForm getWechatPayInfo(OrderPaymemtBo query, String ip){
        //查出app支付所需要的信息
        PayConfigBo payConfigBo = payConfigBiz.selectOne(Condition.create()
                .eq(PayConfigBo.Key.payType.toString(), "APP"));
        if(payConfigBo == null){
            throw new WakaException("支付配置查询出错");
        }
        //订单总金额单位为分(乘上100)
        BigDecimal totalFee = query.getShouldPayTotalFee().multiply(new BigDecimal(100))
                                                .setScale(0, BigDecimal.ROUND_HALF_UP);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("out_trade_no", String .valueOf(query.getOrderId()));
        parameters.put("body", "商品");
        parameters.put("spbill_create_ip", ip);
        parameters.put("notify_url", payConfigBo.getNotifyUrl());
        parameters.put("trade_type", "APP");
        parameters.put("total_fee", String.valueOf(totalFee));

        // 设置微信支付参数
        WxMpInMemoryConfigStorage wxConfig = new WxMpInMemoryConfigStorage();
        wxConfig.setAppId(payConfigBo.getAppId());
        wxConfig.setSecret(payConfigBo.getAppSecret());
        wxConfig.setPartnerId(payConfigBo.getMchId());
        wxConfig.setPartnerKey(payConfigBo.getApiKey());
        wxMpService.setWxMpConfigStorage(wxConfig);

        Map<String, String> result = wxMpService.getJSSDKPayInfo(parameters);

        WechatPayForm wechatPayForm = new WechatPayForm();
        wechatPayForm.setPartnerid(result.get("partnerid"));
        wechatPayForm.setPartnerid(result.get("prepayid"));
        wechatPayForm.setNoncestr(result.get("noncestr"));
        wechatPayForm.setTimestamp(result.get("timestamp"));
        wechatPayForm.setSign(result.get("sign"));
        return wechatPayForm;
    }
}
