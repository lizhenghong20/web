package cn.farwalker.ravv.service.goodssku.specification.model;

import com.fasterxml.jackson.databind.PropertyName;
import lombok.Data;

/**
 * Created by asus on 2018/12/3.
 */
@Data
public class GoodsSpecificationDefVo extends GoodsSpecificationDefBo {
    String valueName;
    Long propertyId;
}
