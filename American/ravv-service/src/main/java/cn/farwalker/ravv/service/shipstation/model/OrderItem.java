package cn.farwalker.ravv.service.shipstation.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderItem {
    private int orderItemId;
    private String lineItemKey;
    private String sku;
    private String name;
    private String imageUrl;
    private Weight weight;
    private int quantity;
    private double unitPrice;
    private double taxAmount;
    private double shippingAmount;
    private String warehouseLocation;
    private List<ItemOption> options;
    private int productId;
    private String fulfillmentSku;
    private boolean adjustment;
    private String upc;
    private String createDate;
    private String modifyDate;

}
