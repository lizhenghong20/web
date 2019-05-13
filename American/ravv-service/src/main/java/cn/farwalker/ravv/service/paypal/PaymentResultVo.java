package cn.farwalker.ravv.service.paypal;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by asus on 2019/1/25.
 */
@Data
public class PaymentResultVo {
    String url;
    Long orderId;
    String payType;
    String clientSecret;
    BigDecimal amount;
}
