package cn.farwalker.ravv.service.shipstation.model;

import lombok.Data;


@Data
public class Weight {
    private double number;
    private String units; // allowed units: pounds , ounces , grams
    private double weightUnits;
}
