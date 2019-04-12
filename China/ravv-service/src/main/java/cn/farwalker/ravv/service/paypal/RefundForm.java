package cn.farwalker.ravv.service.paypal;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by asus on 2019/1/14.
 */
@Data
public class RefundForm {
    /**
     * 订单ID
     */
   private Long  orderId;
    /**
     * 退货单的ID
     */
    private Long returnOrderId;
    /**
     * 退款要退的商品的金额
     */
    private BigDecimal refundTotalAmount = BigDecimal.ZERO;

    private Long memberId;
}
