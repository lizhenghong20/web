package cn.farwalker.waka.components.wechatpay.common.bean;

import lombok.Data;

@Data
public class WechatPay {
    private String appId;
    private String secret;
    private String token;
    private String aesKey;
    private String tmpDirFile;
    private String partnerId;
    private String partnerKey;
    private String notifyUrl;
}
