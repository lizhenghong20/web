package cn.farwalker.ravv.service.shipstation.model;

import lombok.Data;

import java.util.List;

@Data
public class AdvancedOptions {
    private Long warehouseId;
    private boolean nonMachinable;
    private boolean saturdayDelivery;
    private boolean containsAlcohol;
    private Long storeId;
    private String customField1;
    private String customField2;
    private String customField3;
    private String source;
    private boolean mergedOrSplit;
    private List<Long> mergedIds;
    private Long parentId;
    private String billToParty;
    private String billToAccount;
    private String billToPostalCode;
    private String billToCountryCode;
    private String billToMyOtherAccount;
}
