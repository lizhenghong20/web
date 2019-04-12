package cn.farwalker.ravv.service.paypal;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by asus on 2018/12/12.
 */
@Data
public class PaymentForm {
    private Long orderId;
    private BigDecimal orderTotal = BigDecimal.ZERO;//订单总额
    private BigDecimal subTotal = BigDecimal.ZERO;//商品总额
    private BigDecimal tax = BigDecimal.ZERO;//税
    private BigDecimal shipping = BigDecimal.ZERO;//运费
    private Long memberId;
    private String token;
    private String paymentModel;
    private String payType;
    private String payPassword;
}
