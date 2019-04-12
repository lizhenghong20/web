package cn.farwalker.ravv.service.goodsext.comment.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class GoodsCommentPointVo {
    List<GoodsCommentDetailVo> commentDetailVoList;
    BigDecimal avgPoint;
    Map<String, BigDecimal> proportion;
    List<GoodsCommentFilter> filterList;
    int commentCount;
}
