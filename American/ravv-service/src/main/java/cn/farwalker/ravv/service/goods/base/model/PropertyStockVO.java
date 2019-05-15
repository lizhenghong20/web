package cn.farwalker.ravv.service.goods.base.model;

import lombok.Data;

/**
 * @Author Mr.Simple
 * @Description sku匹配库存
 * @Date 14:59 2019/5/14
 * @Param
 * @return
 **/
@Data
public class PropertyStockVO {
    private Long propertyId;
    private int skuStockNum;
}
