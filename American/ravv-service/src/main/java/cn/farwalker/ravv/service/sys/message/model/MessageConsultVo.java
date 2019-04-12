package cn.farwalker.ravv.service.sys.message.model;

import cn.farwalker.ravv.service.goodsext.consult.model.GoodsConsultBo;
import lombok.Data;

import java.util.Date;

@Data
public class MessageConsultVo {
    GoodsConsultBo question;
    GoodsConsultBo answer;
    int answerCount;
    String imgUrl;
    Date publishTime;
    String title;
    boolean hasQuestion;
    Long messageId;
}
