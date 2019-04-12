package cn.farwalker.ravv.service.shipstation.model;

import lombok.Data;

@Data
public class ShippedOrder {
    int orderId;
    String carrierCode;
    String shipDate;
    String trackingNumber;
    boolean notifyCustomer;
    boolean notifySalesChannel;
}
