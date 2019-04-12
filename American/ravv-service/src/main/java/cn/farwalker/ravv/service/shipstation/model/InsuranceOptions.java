package cn.farwalker.ravv.service.shipstation.model;

import lombok.Data;

@Data
public class InsuranceOptions {
    private String provider;
    private boolean insureShipment;
    private double insureValue;
}

