package cn.farwalker.ravv.service.paypal;

import lombok.Data;

/**
 * Created by asus on 2019/1/25.
 */
@Data
public class PaymentResultVo {
    String url;
    Long orderId;
    String payType;
    WechatPayForm wechatPayForm;
}
