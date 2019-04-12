package cn.farwalker.ravv.service.goodsext.consult.model;

import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import lombok.Data;

import java.util.List;

@Data
public class GoodsConsultVo {
    GoodsConsultBo question;
    List<GoodsConsultBo> answer;
//    GoodsBo goodsBo;
}
