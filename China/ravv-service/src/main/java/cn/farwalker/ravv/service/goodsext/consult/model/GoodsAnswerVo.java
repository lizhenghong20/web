package cn.farwalker.ravv.service.goodsext.consult.model;

import lombok.Data;

/**
 * @description: 我的回答
 * @author Mr.Simple
 * @date 2018/12/27 15:40
 */
@Data
public class GoodsAnswerVo {
    GoodsConsultBo question;
    GoodsConsultBo answer;
    String imgUrl;
}
