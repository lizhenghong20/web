package cn.farwalker.ravv.service.goodsext.comment.biz;

import cn.farwalker.ravv.service.goodsext.comment.model.*;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IGoodsCommentService {
    public boolean addComment(GoodsCommentVo goodsCommentVo);

    public String addCommentByBatch(List<GoodsCommentVo> commentVoList);

    public String addAppendComment(GoodsCommentBo goodsCommentBo);

    public String addShopReply(GoodsCommentBo goodsCommentBo);

    public GoodsCommentDetailVo getCommentDetail(Long commentId);

    public GoodsCommentDetailVo getCommentDetailFromOrder(Long memberId, Long orderGoodsId);

    public List<CommentOrderVo> publishedReviews(Long memberId, int currentPage, int pageSize);

    public List<OrderGoodsBo> awaitingReviews(Long memberId, int currentPage, int pageSize);

    public GoodsCommentPointVo getCommentWithPoint(Long goodsId);

    public List<GoodsCommentDetailVo> getCommentWithFilter(Long goodsId, int goodsPoint, boolean picture, boolean addition, int currentPage, int pageSize);

    public Map<BigDecimal, Map<String, BigDecimal>> calculation(Long goodsId);


}
