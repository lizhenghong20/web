package cn.farwalker.ravv.service.goodsext.comment.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsCommentVo extends GoodsCommentBo {
    private BigDecimal productPoint;

    private BigDecimal shipmentPoint;

    private String goodsImage;

    private String goodsName;

}
