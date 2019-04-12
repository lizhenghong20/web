package cn.farwalker.ravv.service.shipstation.model;

import lombok.Data;

@Data
public class Dimensions {
    private double length;
    private double width;
    private double height;
    private String units; // allowed units: inches , centimeters
}
