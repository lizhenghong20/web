package cn.farwalker.ravv.service.goodsext.comment.model;

import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import lombok.Data;

import java.util.Date;

@Data
public class CommentOrderVo extends OrderGoodsBo {
    private Long commentId;
    private Date commentTime;
}
