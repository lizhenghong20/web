package cn.farwalker.ravv.service.goods.base.model;

import cn.farwalker.ravv.service.goodssku.specification.model.GoodsSpecificationDefVo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2018/12/8.
 */
@Data
public class PropertyLineVo {
    Long propertyId;
    String propertyName;
    boolean isImage;
    List<GoodsSpecificationDefVo> propertyValueList;
}
