package cn.farwalker.ravv.service.goodsext.consult.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description: 提问详情
 * @author Mr.Simple
 * @date 2018/12/27 15:38
 */
@Data
public class GoodsQaVo {
    GoodsConsultBo question;
    List<GoodsConsultBo> answer;
    String imgUrl;
    String goodsName;
    BigDecimal price;
    boolean isMyQuestion = false;
}
