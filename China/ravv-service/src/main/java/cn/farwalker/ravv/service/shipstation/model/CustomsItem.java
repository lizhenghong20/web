package cn.farwalker.ravv.service.shipstation.model;

import lombok.Data;

@Data
public class CustomsItem {
    private String customsItemId;
    private String description;
    private double quantity;
    private double value;
    private String harmonizedTariffCode;
    private String countryOfOrigin;

}
