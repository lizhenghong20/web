package cn.farwalker.ravv.service.goodsext.comment.model;

import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.goodsext.point.model.GoodsPointBo;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import lombok.Data;

@Data
public class GoodsCommentDetailVo {
    private GoodsCommentBo originalComment;
    private GoodsCommentBo appendComment;
    private GoodsCommentBo shopReply;
    private GoodsPointBo productPoint;
    private OrderGoodsBo orderInfo;
    private GoodsBo goodsInfo;
    private MemberBo commentUserInfo;
}
