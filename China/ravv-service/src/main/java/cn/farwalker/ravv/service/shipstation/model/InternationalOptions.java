package cn.farwalker.ravv.service.shipstation.model;

import lombok.Data;

import java.util.List;

@Data
public class InternationalOptions {
    private String contents;
    private List<CustomsItem> customsItems;
    private String nonDelivery;
}
