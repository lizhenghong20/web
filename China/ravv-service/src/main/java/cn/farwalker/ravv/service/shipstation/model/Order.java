package cn.farwalker.ravv.service.shipstation.model;

import lombok.Data;

import java.util.List;

@Data
public class Order {
//    private int orderId; // 无需设值 shipstation定义的orderId
    private String orderNumber; //required  我们定义的orderId
    private String orderKey; // optional  使用order_code
    private String orderDate; //required
//    private String createDate;
//    private String modifyDate;
    private String paymentDate;
    private String shipByDate;
    private String orderStatus; //required  possible values: awaiting_payment, awaiting_shipment, shipped, on_hold, cancelled
    private Long customerId;
    private String customerUsername;
    private String customerEmail;
    private Address billTo; //required
    private Address shipTo; //required
    private List<OrderItem> items;
    private double orderTotal;
    private double amountPaid;
    private double taxAmount;
    private double shippingAmount;
    private String customerNotes;
    private String internalNotes;
    private boolean gift;
    private String giftMessage;
    private String paymentMethod;
    private String requestedShippingService;
    private String carrierCode;
    private String serviceCode;
    private String packageCode;
    private String confirmation;
    private String shipDate;
    private String holdUntilDate;
    private Weight weight;
    private Dimensions dimensions;
    private InsuranceOptions insuranceOptions;
    private InternationalOptions internationalOptions;
    private AdvancedOptions advancedOptions;
    private List<Long> tagIds;
    private String userId;
    private boolean externallyFulfilled;
    private String externallyFulfilledBy;

}
