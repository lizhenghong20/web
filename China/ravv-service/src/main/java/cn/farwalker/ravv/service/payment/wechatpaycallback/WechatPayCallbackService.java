package cn.farwalker.ravv.service.payment.wechatpaycallback;

import java.math.BigDecimal;
import java.util.Map;

public interface WechatPayCallbackService {
    public void doSuccess(Map<String, String> map) throws Exception;
}
