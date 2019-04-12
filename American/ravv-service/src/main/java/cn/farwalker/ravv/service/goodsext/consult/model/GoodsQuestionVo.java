package cn.farwalker.ravv.service.goodsext.consult.model;

import lombok.Data;

/**
 * @description: 问题页
 * @author Mr.Simple
 * @date 2018/12/27 15:40
 */
@Data
public class GoodsQuestionVo {
    GoodsConsultBo question;
    int answerCount;
    String imgUrl;
}
