package cn.farwalker.ravv.service.paypal;

import lombok.Data;

@Data
public class WechatPayForm {
//    private String appid;
    private String partnerid;
    private String prepayid;
//    private String signPackage;
    private String noncestr;
    private String timestamp;
    private String sign;
}
