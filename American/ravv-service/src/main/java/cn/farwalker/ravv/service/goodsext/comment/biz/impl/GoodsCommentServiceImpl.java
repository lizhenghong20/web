package cn.farwalker.ravv.service.goodsext.comment.biz.impl;

import cn.farwalker.ravv.common.constants.CommentFilterTypeEnum;
import cn.farwalker.ravv.common.constants.GoodsDisplayEnum;
import cn.farwalker.ravv.common.constants.GoodsPointEnum;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsBiz;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.goods.image.biz.IGoodsImageBiz;
import cn.farwalker.ravv.service.goodsext.comment.biz.IGoodsCommentBiz;
import cn.farwalker.ravv.service.goodsext.comment.biz.IGoodsCommentService;
import cn.farwalker.ravv.service.goodsext.comment.dao.IGoodsCommentDao;
import cn.farwalker.ravv.service.goodsext.comment.model.*;
import cn.farwalker.ravv.service.goodsext.point.biz.IGoodsPointBiz;
import cn.farwalker.ravv.service.goodsext.point.model.GoodsPointBo;
import cn.farwalker.ravv.service.goodssku.skudef.biz.IGoodsSkuDefBiz;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuDefBo;
import cn.farwalker.ravv.service.member.basememeber.biz.IMemberBiz;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.order.ordergoods.biz.IOrderGoodsBiz;
import cn.farwalker.ravv.service.order.ordergoods.dao.IOrderGoodsDao;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoBiz;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderStatusEnum;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class GoodsCommentServiceImpl implements IGoodsCommentService {

    @Autowired
    private IGoodsCommentBiz iGoodsCommentBiz;

    @Autowired
    private IGoodsPointBiz iGoodsPointBiz;

    @Autowired
    private IGoodsCommentService iGoodsCommentService;

    @Autowired
    private IGoodsCommentDao iGoodsCommentDao;

    @Autowired
    private IOrderInfoBiz iOrderInfoBiz;

    @Autowired
    private IGoodsImageBiz iGoodsImageBiz;

    @Autowired
    private IGoodsBiz iGoodsBiz;

    @Autowired
    private IOrderGoodsBiz iOrderGoodsBiz;

    @Autowired
    private IGoodsSkuDefBiz iGoodsSkuDefBiz;

    @Autowired
    private IOrderGoodsDao iOrderGoodsDao;

    @Autowired
    private IMemberBiz iMemberBiz;

    /**
     * @description: 写评价
     * @param: goodsCommentVo
     * @return string
     * @author Mr.Simple
     * @date 2018/12/28 14:27
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean addComment(GoodsCommentVo goodsCommentVo) {
        //查询订单是否已完成
        OrderInfoBo isFinished = new OrderInfoBo();
        isFinished = iOrderInfoBiz.selectById(goodsCommentVo.getOrderId());
        log.info("===============orderStatus:{}",isFinished.getOrderStatus());
        if(!isFinished.getOrderStatus().equals(OrderStatusEnum.TRADE_CLOSE))
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR + "订单未完成");
        //判断该订单商品是否已评论过
        EntityWrapper<GoodsCommentBo> queryComment = new EntityWrapper<>();
        queryComment.eq(GoodsCommentBo.Key.orderId.toString(), goodsCommentVo.getOrderId());
        queryComment.eq(GoodsCommentBo.Key.skuId.toString(), goodsCommentVo.getSkuId());
        int count = iGoodsCommentBiz.selectCount(queryComment);
        if(count == 1)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR + "had comment already");
        //将评价插入表
        GoodsCommentBo commentBo = new GoodsCommentBo();
        BeanUtils.copyProperties(goodsCommentVo, commentBo);
        //设置名字,先查询用户firstname,lastname
        String originalName = "Anonymous User";
        MemberBo memberBo = new MemberBo();
        memberBo = iMemberBiz.selectById(commentBo.getAuthorId());
        if(memberBo.getFirstname() != null && memberBo.getLastname() != null){
            originalName = memberBo.getFirstname() + " " + memberBo.getLastname();
        }
        //如果匿名，处理名称
        if(commentBo.getAuthor().equals("true")){
            originalName = Tools.string.getAnonymous(originalName);
        }
        commentBo.setAuthor(originalName);
        //拆解图片
        if(commentBo.getImages() != null){
            String imgUrl = Tools.string.splitImg(commentBo.getImages());
            commentBo.setImages(imgUrl);
        }
        if(commentBo.getVideos() != null){
            commentBo.setVideos(QiniuUtil.getRelativePath(commentBo.getVideos()));
        }
        Date now = new Date();
        commentBo.setCommentTime(now);
        commentBo.setGmtCreate(now);
        commentBo.setGmtModified(now);
        log.info("{}",Integer.parseInt(GoodsDisplayEnum.DISPLAY.getKey()));
        commentBo.setDisplay(Integer.parseInt(GoodsDisplayEnum.DISPLAY.getKey()));
        if(!iGoodsCommentBiz.insert(commentBo))
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        //插入评价后更新orderGoods表中的comment字段
        EntityWrapper<OrderGoodsBo> queryOrderGoods = new EntityWrapper<>();
        queryOrderGoods.eq(OrderGoodsBo.Key.orderId.toString(), goodsCommentVo.getOrderId());
        queryOrderGoods.eq(OrderGoodsBo.Key.skuId.toString(), goodsCommentVo.getSkuId());
        OrderGoodsBo orderGoodsBo = new OrderGoodsBo();
        orderGoodsBo.setComment(1);
        if(!iOrderGoodsBiz.update(orderGoodsBo, queryOrderGoods))
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR + "addComment,orderGoods");
        //评分
        GoodsPointBo productPoint = new GoodsPointBo();
        GoodsPointBo shipmentPoint = new GoodsPointBo();
        BeanUtils.copyProperties(goodsCommentVo, productPoint);
        //商品评分
        log.info("==================================point{}",goodsCommentVo.getProductPoint());
        productPoint.setGoodsPoint(goodsCommentVo.getProductPoint());
        productPoint.setCommentId(commentBo.getId());
        productPoint.setGmtCreate(now);
        productPoint.setGmtModified(now);
        productPoint.setDisplay(Integer.parseInt(GoodsDisplayEnum.DISPLAY.getKey()));
        productPoint.setMemberId(goodsCommentVo.getAuthorId());
        productPoint.setType(Integer.parseInt(GoodsPointEnum.PRODUCT.getKey()));
        //在插入前复制，id不会复制
        BeanUtils.copyProperties(productPoint, shipmentPoint);
        if(!iGoodsPointBiz.insert(productPoint))
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR + "productPoint");
        //物流评分
        shipmentPoint.setType(Integer.parseInt(GoodsPointEnum.SHIPMENT.getKey()));
        shipmentPoint.setGoodsPoint(goodsCommentVo.getShipmentPoint());
        if(!iGoodsPointBiz.insert(shipmentPoint))
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR + "shipmentPoint");
        //应当在此处重新计算星级并插入goods表
        //获取商品平均星级
        Map<BigDecimal, Map<String, BigDecimal>> point = iGoodsCommentService.calculation(goodsCommentVo.getGoodsId());
        //更新星级
        GoodsBo goodsBo = iGoodsBiz.selectById(goodsCommentVo.getGoodsId());
        if(goodsBo != null){
            if(point != null){
                for(BigDecimal key: point.keySet())
                    goodsBo.setGoodsPoint(key);
                //更新goods的point字段
                if(!iGoodsBiz.updateById(goodsBo))
                    throw new WakaException(RavvExceptionEnum.UPDATE_ERROR + "addComment");
            }
        }
        return true;
    }

    @Override
    public String addCommentByBatch(List<GoodsCommentVo> commentVoList) {
        commentVoList.forEach(item->{
            if(!addComment(item))
                throw new WakaException(RavvExceptionEnum.INSERT_ERROR + "insert comment by batch");
        });
        return "successful";
    }


    /**
     * @description: 追评
     * @param: goodsCommentBo
     * @return string
     * @author Mr.Simple
     * @date 2018/12/28 14:28
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String addAppendComment(GoodsCommentBo goodsCommentBo) {
        //查看该sku是否已评价，是否已追评
        EntityWrapper<GoodsCommentBo> queryComment = new EntityWrapper<>();
        queryComment.eq(GoodsCommentBo.Key.orderId.toString(), goodsCommentBo.getOrderId());
        queryComment.eq(GoodsCommentBo.Key.skuId.toString(), goodsCommentBo.getSkuId());
        GoodsCommentBo originalComment = iGoodsCommentBiz.selectOne(queryComment);
        //是否已评价
        if(originalComment == null)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR + "this sku is not comment");
        //是否已追评
        if(originalComment.getAppendId() != null)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR + "this sku had append already");
        //拆解图片
        if(Tools.string.isNotEmpty(goodsCommentBo.getImages())){
            String imgUrl = Tools.string.splitImg(goodsCommentBo.getImages());
            goodsCommentBo.setImages(imgUrl);
        }
        if(goodsCommentBo.getVideos() != null)
            goodsCommentBo.setVideos(QiniuUtil.getFullPath(goodsCommentBo.getVideos()));
        goodsCommentBo.setForCommentId(originalComment.getId());
        Date now = new Date();
        goodsCommentBo.setAuthor(originalComment.getAuthor());
        goodsCommentBo.setCommentTime(now);
        goodsCommentBo.setGmtCreate(now);
        goodsCommentBo.setGmtModified(now);
        goodsCommentBo.setDisplay(Integer.parseInt(GoodsDisplayEnum.DISPLAY.getKey()));
        if(!iGoodsCommentBiz.insert(goodsCommentBo))
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR + "append comment");
        //更新原评价
        originalComment.setAppendId(goodsCommentBo.getId());
        originalComment.setGmtModified(now);
        if(!iGoodsCommentBiz.updateById(originalComment))
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR + "append comment");
        return "successful";
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String addShopReply(GoodsCommentBo goodsCommentBo) {
        GoodsCommentBo originalComment = iGoodsCommentBiz.selectById(goodsCommentBo.getForCommentId());
        if(originalComment == null)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR + "该评价不存在");
        Date now = new Date();
        goodsCommentBo.setGmtCreate(now);
        goodsCommentBo.setCommentTime(now);
        goodsCommentBo.setGmtModified(now);
        goodsCommentBo.setShopReply(1);
        if(!iGoodsCommentBiz.insert(goodsCommentBo))
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        return "reply successful";
    }

    /**
     * @description: 评价详情
     * @param: commentId
     * @return goodsCommentDetailVo
     * @author Mr.Simple
     * @date 2018/12/28 14:27
     */
    @Override
    public GoodsCommentDetailVo getCommentDetail(Long commentId) {
        //查询出评价，如果有追评，也查出
        GoodsCommentDetailVo commentDetailVo = new GoodsCommentDetailVo();
        GoodsCommentBo originalComment = iGoodsCommentBiz.selectById(commentId);
        if(originalComment == null)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR + "commentId not exists");
        //拼装图片
        if(originalComment.getImages() != null){
            originalComment.setImages(Tools.string.assembleImg(originalComment.getImages()));
        }
        if(originalComment.getVideos() != null){
            originalComment.setVideos(QiniuUtil.getFullPath(originalComment.getVideos()));
        }
        commentDetailVo.setOriginalComment(originalComment);
        //查出订单信息
        OrderGoodsBo order = new OrderGoodsBo();
        EntityWrapper<OrderGoodsBo> queryOrderGoods = new EntityWrapper<>();
        queryOrderGoods.eq(OrderGoodsBo.Key.orderId.toString(), originalComment.getOrderId());
        queryOrderGoods.eq(OrderGoodsBo.Key.skuId.toString(), originalComment.getSkuId());
        order = iOrderGoodsBiz.selectOne(queryOrderGoods);
        if(order.getImgSku() != null)
            order.setImgSku(QiniuUtil.getFullPath(order.getImgSku()));
        commentDetailVo.setOrderInfo(order);
        //追评
        if(originalComment.getAppendId() != null){
            GoodsCommentBo appendComment = new GoodsCommentBo();
            appendComment = iGoodsCommentBiz.selectById(originalComment.getAppendId());
            //拼装图片
            if(appendComment.getImages() != null){
                appendComment.setImages(Tools.string.assembleImg(appendComment.getImages()));
            }
            if(appendComment.getVideos() != null){
                appendComment.setVideos(QiniuUtil.getFullPath(appendComment.getVideos()));
            }
            commentDetailVo.setAppendComment(appendComment);

        }
        //查询出评星
        EntityWrapper<GoodsPointBo> queryPoint = new EntityWrapper<>();
        queryPoint.eq(GoodsPointBo.Key.commentId.toString(), commentId);
        GoodsPointBo productPoint = new GoodsPointBo();
        productPoint = iGoodsPointBiz.selectOne(queryPoint);
        commentDetailVo.setProductPoint(productPoint);
        //查出商家回复
        EntityWrapper<GoodsCommentBo> queryShop = new EntityWrapper<>();
        queryShop.eq(GoodsCommentBo.Key.forCommentId.toString(), commentId);
        queryShop.isNotNull(GoodsCommentBo.Key.shopReply.toString());
        GoodsCommentBo shopReply = iGoodsCommentBiz.selectOne(queryShop);
        if(shopReply != null){
            commentDetailVo.setShopReply(shopReply);
        }

        return commentDetailVo;
    }

    @Override
    public GoodsCommentDetailVo getCommentDetailFromOrder(Long memberId, Long orderGoodsId) {
        //查出商品快照信息
        OrderGoodsBo orderGoodsBo = iOrderGoodsBiz.selectById(orderGoodsId);
        if(orderGoodsBo == null)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        //根据memberId,orderId,skuId查出源评价
        EntityWrapper<GoodsCommentBo> queryComment = new EntityWrapper<>();
        queryComment.eq(GoodsCommentBo.Key.authorId.toString(), memberId);
        queryComment.eq(GoodsCommentBo.Key.orderId.toString(), orderGoodsBo.getOrderId());
        queryComment.eq(GoodsCommentBo.Key.skuId.toString(), orderGoodsBo.getSkuId());
        queryComment.isNull(GoodsCommentBo.Key.forCommentId.toString());
        GoodsCommentBo originalComment = iGoodsCommentBiz.selectOne(queryComment);
        if(originalComment == null)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        return getCommentDetail(originalComment.getId());
    }

    /**
     * @description: 等待评价的订单
     * @param:
     * @return
     * @author Mr.Simple
     * @date 2019/1/3 20:57
     */
    @Override
    public List<OrderGoodsBo> awaitingReviews(Long memberId, int currentPage, int pageSize) {
        //获取该用户的所有已完成订单（总单）
        List<OrderGoodsBo> awaitingList = new ArrayList<>();
        EntityWrapper<OrderInfoBo> queryFinishOrder = new EntityWrapper<>();
        queryFinishOrder.eq(OrderInfoBo.Key.buyerId.toString(), memberId);
        queryFinishOrder.eq(OrderInfoBo.Key.orderStatus.toString(), OrderStatusEnum.TRADE_CLOSE);
        queryFinishOrder.isNull(OrderInfoBo.Key.pid.toString());
        List<OrderInfoBo> allList = iOrderInfoBiz.selectList(queryFinishOrder);
        if(allList.size() == 0)
            return awaitingList;
        //将总单和子单合并
        List<Long> orderList = new ArrayList<>();
        //查询该总单是否有子单，如有，获取子单添加到订单列表；没有，将总单添加到商品列表
        allList.forEach(item->{
            EntityWrapper<OrderInfoBo> querySubOrder = new EntityWrapper<>();
            querySubOrder.eq(OrderInfoBo.Key.pid.toString(), item.getId());
            List<OrderInfoBo> subOrderList = iOrderInfoBiz.selectList(querySubOrder);
            if(subOrderList.size() != 0){
                subOrderList.forEach(sub->{
                    orderList.add(sub.getId());
                });
            }
            else
                orderList.add(item.getId());
        });
        //根据订单号去order_goods里查，将comment为空的信息查出来
        Page<OrderGoodsBo> page = new Page<>(currentPage, pageSize);
        awaitingList = iOrderGoodsDao.getAwaitingOrderList(page, orderList);
        if(awaitingList.size() != 0){
            awaitingList.forEach(item->{
                //根据skuId将sku图片查出来
                GoodsSkuDefBo skuDefBo = new GoodsSkuDefBo();
                skuDefBo = iGoodsSkuDefBiz.selectById(item.getSkuId());
                if(skuDefBo != null && skuDefBo.getImageUrl() != null)
                    item.setImgSku(QiniuUtil.getFullPath(skuDefBo.getImageUrl()));
            });

        }
        return awaitingList;
    }

    /**
     * @description: 查询已发表的评价
     * @param: memberId
     * @return list
     * @author Mr.Simple
     * @date 2018/12/28 14:43
     */
    @Override
    public List<CommentOrderVo> publishedReviews(Long memberId, int currentPage, int pageSize) {
        //修改逻辑
        //根据memberId查出已评价的orderId和skuId，然后去order_goods表里查出订单信息
        List<CommentOrderVo> commentOrderVoList = new ArrayList<>();
        Page<GoodsCommentBo> page = new Page<>(currentPage, pageSize);
        EntityWrapper<GoodsCommentBo> queryGoodsComment = new EntityWrapper<>();
        queryGoodsComment.eq(GoodsCommentBo.Key.authorId.toString(), memberId);
        queryGoodsComment.isNull(GoodsCommentBo.Key.forCommentId.toString());
        queryGoodsComment.isNull(GoodsCommentBo.Key.shopReply.toString());
        queryGoodsComment.orderBy(GoodsCommentBo.Key.gmtCreate.toString(), false);
        Page<GoodsCommentBo> allPublished = iGoodsCommentBiz.selectPage(page, queryGoodsComment);
        if(allPublished.getRecords().size() == 0){
            return commentOrderVoList;
        }
        List<GoodsCommentBo> commentBoList = allPublished.getRecords();
        commentBoList.forEach(item->{
            //拿到orderId和skuId查询订单信息
            CommentOrderVo commentOrderVo = new CommentOrderVo();
            OrderGoodsBo order = new OrderGoodsBo();
            EntityWrapper<OrderGoodsBo> queryOrderGoods = new EntityWrapper<>();
            queryOrderGoods.eq(OrderGoodsBo.Key.orderId.toString(), item.getOrderId());
            queryOrderGoods.eq(OrderGoodsBo.Key.skuId.toString(), item.getSkuId());
            order = iOrderGoodsBiz.selectOne(queryOrderGoods);
            BeanUtils.copyProperties(order, commentOrderVo);
            commentOrderVo.setCommentId(item.getId());
            //根据skuId将sku图片查出来
            GoodsSkuDefBo skuDefBo = new GoodsSkuDefBo();
            skuDefBo = iGoodsSkuDefBiz.selectById(item.getSkuId());
            if(skuDefBo != null && skuDefBo.getImageUrl() != null)
                commentOrderVo.setImgSku(QiniuUtil.getFullPath(skuDefBo.getImageUrl()));
            //添加评价时间
            commentOrderVo.setCommentTime(item.getCommentTime());
            commentOrderVoList.add(commentOrderVo);
        });
        return commentOrderVoList;
    }

    /**
     * @description: 商品详情页获取所有评价和评星
     * @param: goodsId,star,picture,addtion
     * @return goodsCommentPointVo
     * @author Mr.Simple
     * @date 2018/12/28 15:56
     */
    @Override
    public GoodsCommentPointVo getCommentWithPoint(Long goodsId) {
        GoodsCommentPointVo commentPointVo = new GoodsCommentPointVo();
        //根据条件查出原评价
        EntityWrapper<GoodsCommentBo> queryAllComment = new EntityWrapper<>();
        queryAllComment.eq(GoodsCommentBo.Key.goodsId.toString(),goodsId);
        queryAllComment.isNull(GoodsCommentBo.Key.forCommentId.toString());
        queryAllComment.isNull(GoodsCommentBo.Key.shopReply.toString());
        List<GoodsCommentBo> originalCommentList = iGoodsCommentBiz.selectList(queryAllComment);
//        List<GoodsCommentBo> originalCommentList = iGoodsCommentDao.selectByGoodsIdOrderByPoint(goodsId);
        if(originalCommentList.size() == 0){
            commentPointVo.setCommentCount(0);
            return commentPointVo;
        }
        else {
            commentPointVo.setCommentCount(originalCommentList.size());
        }
        //查出分类信息
        commentPointVo.setFilterList(getFilter(originalCommentList));
        Map<BigDecimal, Map<String, BigDecimal>> cal = calculation(goodsId);
        for(BigDecimal key: cal.keySet()){
            commentPointVo.setAvgPoint(key);
            commentPointVo.setProportion(cal.get(key));
        }

        return commentPointVo;
    }

    @Override
    public List<GoodsCommentDetailVo> getCommentWithFilter(Long goodsId, int goodsPoint, boolean picture, boolean addition, int currentPage, int pageSize) {
        List<GoodsCommentDetailVo> detailList = new ArrayList<>();
        Page<GoodsCommentBo> page = new Page<>(currentPage, pageSize);
        List<GoodsCommentBo> originalCommentList = iGoodsCommentDao.selectByCondition(page, goodsId, goodsPoint, picture, addition);
        if(originalCommentList.size() == 0)
            return detailList;
        //根据原评价，查出追评，商家回复，星级
        originalCommentList.forEach(item->{
            GoodsCommentDetailVo commentDetailVo = new GoodsCommentDetailVo();
            //查出用户头像
            MemberBo memberBo = new MemberBo();
            memberBo = iMemberBiz.selectById(item.getAuthorId());
            if(memberBo == null){
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR + "用户被删除");
            }
            else {
                if (memberBo.getAvator() != null){
                    memberBo.setAvator(QiniuUtil.getFullPath(memberBo.getAvator()));
                }
            }
            commentDetailVo.setCommentUserInfo(memberBo);
            //查出订单信息
            OrderGoodsBo order = new OrderGoodsBo();
            EntityWrapper<OrderGoodsBo> queryOrderGoods = new EntityWrapper<>();
            queryOrderGoods.eq(OrderGoodsBo.Key.orderId.toString(), item.getOrderId());
            queryOrderGoods.eq(OrderGoodsBo.Key.skuId.toString(), item.getSkuId());
            order = iOrderGoodsBiz.selectOne(queryOrderGoods);
            commentDetailVo.setOrderInfo(order);
            if(Tools.string.isNotEmpty(item.getImages()))
                item.setImages(Tools.string.assembleImg(item.getImages()));
            if(Tools.string.isNotEmpty(item.getVideos())){
                item.setVideos(QiniuUtil.getFullPath(item.getVideos()));
            }
            commentDetailVo.setOriginalComment(item);
            if(item.getAppendId() != null){
                //查出追评
                GoodsCommentBo appendComment = new GoodsCommentBo();
                appendComment = iGoodsCommentBiz.selectById(item.getAppendId());
                if(Tools.string.isNotEmpty(appendComment.getImages()))
                    appendComment.setImages(Tools.string.assembleImg(appendComment.getImages()));
                if(Tools.string.isNotEmpty(appendComment.getVideos())){
                    appendComment.setVideos(QiniuUtil.getFullPath(appendComment.getVideos()));
                }
                commentDetailVo.setAppendComment(appendComment);
            }
            //查出商家回复
            EntityWrapper<GoodsCommentBo> queryShop = new EntityWrapper<>();
            queryShop.eq(GoodsCommentBo.Key.forCommentId.toString(), item.getId());
            queryShop.isNotNull(GoodsCommentBo.Key.shopReply.toString());
            GoodsCommentBo shopReply = iGoodsCommentBiz.selectOne(queryShop);
            if(shopReply != null){
                commentDetailVo.setShopReply(shopReply);
            }
            //查出评星
            //查出商品评星
            EntityWrapper<GoodsPointBo> queryPoint = new EntityWrapper<>();
            queryPoint.eq(GoodsPointBo.Key.commentId.toString(), item.getId());
            queryPoint.eq(GoodsPointBo.Key.type.toString(), Integer.parseInt(GoodsPointEnum.PRODUCT.getKey()));
            GoodsPointBo productPoint = iGoodsPointBiz.selectOne(queryPoint);
            commentDetailVo.setProductPoint(productPoint);
            //添加到列表
            detailList.add(commentDetailVo);
        });
        return detailList;
    }


    /**
     * @description: 获取所有筛选条件及个数
     * @param: goodsId
     * @return list
     * @author Mr.Simple
     * @date 2019/1/4 14:29
     */
    public List<GoodsCommentFilter> getFilter(List<GoodsCommentBo> originalCommentList){
        List<GoodsCommentFilter> filterList = new ArrayList<>();
        Map<String, Integer> filterMap = new HashMap<>();
        int count = 1;
        //遍历list
        filterMap.put(CommentFilterTypeEnum.ALL.getKey(), originalCommentList.size());
        originalCommentList.forEach(item->{
            //根据评论id查询评分
            GoodsPointBo goodsPointBo = new GoodsPointBo();
            goodsPointBo = iGoodsPointBiz.selectOne(Condition.create().eq(GoodsPointBo.Key.commentId.toString(), item.getId()));
            //先转int再转string,防止出错
            if(filterMap.containsKey(String.valueOf(goodsPointBo.getGoodsPoint().intValue()))){
                filterMap.put(String.valueOf(goodsPointBo.getGoodsPoint().intValue()), filterMap.get(String.valueOf(goodsPointBo.getGoodsPoint().intValue())) + 1);
            }
            else
                filterMap.put(String.valueOf(goodsPointBo.getGoodsPoint().intValue()), count);
            //判断是否有追评
            if(item.getAppendId() != null){
                if(filterMap.containsKey(CommentFilterTypeEnum.APPEND.getKey()))
                    filterMap.put(CommentFilterTypeEnum.APPEND.getKey(), filterMap.get(CommentFilterTypeEnum.APPEND.getKey()));
                else
                    filterMap.put(CommentFilterTypeEnum.APPEND.getKey(), count);
            }
            if(item.getImages() != null || item.getVideos() != null){
                if(filterMap.containsKey(CommentFilterTypeEnum.WITHPIC.getKey()))
                    filterMap.put(CommentFilterTypeEnum.WITHPIC.getKey(), filterMap.get(CommentFilterTypeEnum.WITHPIC.getKey()));
                else
                    filterMap.put(CommentFilterTypeEnum.WITHPIC.getKey(), count);
            }
        });
        //判断为空的字段
        if(!filterMap.containsKey(CommentFilterTypeEnum.FIVEPOINT.getKey())){
            filterMap.put(CommentFilterTypeEnum.FIVEPOINT.getKey(), 0);
        }
        if(!filterMap.containsKey(CommentFilterTypeEnum.FOURPOINT.getKey())){
            filterMap.put(CommentFilterTypeEnum.FOURPOINT.getKey(), 0);
        }
        if(!filterMap.containsKey(CommentFilterTypeEnum.THREEPOINT.getKey())){
            filterMap.put(CommentFilterTypeEnum.THREEPOINT.getKey(), 0);
        }
        if(!filterMap.containsKey(CommentFilterTypeEnum.TWOPOINT.getKey())){
            filterMap.put(CommentFilterTypeEnum.TWOPOINT.getKey(), 0);
        }
        if(!filterMap.containsKey(CommentFilterTypeEnum.ONEPOINT.getKey())){
            filterMap.put(CommentFilterTypeEnum.ONEPOINT.getKey(), 0);
        }
        if(!filterMap.containsKey(CommentFilterTypeEnum.ALL.getKey())){
            filterMap.put(CommentFilterTypeEnum.ALL.getKey(), 0);
        }
        if(!filterMap.containsKey(CommentFilterTypeEnum.APPEND.getKey())){
            filterMap.put(CommentFilterTypeEnum.APPEND.getKey(), 0);
        }
        if(!filterMap.containsKey(CommentFilterTypeEnum.WITHPIC.getKey())){
            filterMap.put(CommentFilterTypeEnum.WITHPIC.getKey(), 0);
        }

        for(String filter: filterMap.keySet()){
            GoodsCommentFilter goodsCommentFilter = new GoodsCommentFilter();
            goodsCommentFilter.setFilterName(filter);
            goodsCommentFilter.setCount(filterMap.get(filter));
            if(filter.equals(CommentFilterTypeEnum.ALL.getKey())){
                goodsCommentFilter.setSequence("1");
            }
            if(filter.equals(CommentFilterTypeEnum.WITHPIC.getKey())){
                goodsCommentFilter.setSequence("2");
            }
            if(filter.equals(CommentFilterTypeEnum.APPEND.getKey())){
                goodsCommentFilter.setSequence("3");
            }
            if(filter.equals(CommentFilterTypeEnum.FIVEPOINT.getKey())){
                goodsCommentFilter.setSequence("4");
            }
            if(filter.equals(CommentFilterTypeEnum.FOURPOINT.getKey())){
                goodsCommentFilter.setSequence("5");
            }
            if(filter.equals(CommentFilterTypeEnum.THREEPOINT.getKey())){
                goodsCommentFilter.setSequence("6");
            }
            if(filter.equals(CommentFilterTypeEnum.TWOPOINT.getKey())){
                goodsCommentFilter.setSequence("7");
            }
            if(filter.equals(CommentFilterTypeEnum.ONEPOINT.getKey())){
                goodsCommentFilter.setSequence("8");
            }
            filterList.add(goodsCommentFilter);
        }
        //重写排序
        Collections.sort(filterList, new Comparator<GoodsCommentFilter>() {
            @Override
            public int compare(GoodsCommentFilter o1, GoodsCommentFilter o2) {
                return o1.getSequence().compareTo(o2.getSequence());
            }
        });
        return filterList;
    }

    /**
     * @description: 计算评星和平均值
     * @param: goodsId
     * @return map
     * @author Mr.Simple
     * @date 2018/12/28 17:04
     */
    public Map<BigDecimal, Map<String, BigDecimal>> calculation(Long goodsId){
        Map<BigDecimal, Map<String, BigDecimal>> map = new HashMap<>();
        Map<String, BigDecimal> proportion = new HashMap<>();
        Map<String, AtomicInteger> internel = new HashMap<>();
        //星级平均值
        BigDecimal avgPoint;
        //一星二星三星四星五星评分个数
        AtomicInteger one = new AtomicInteger(0);
        AtomicInteger two = new AtomicInteger(0);
        AtomicInteger three = new AtomicInteger(0);
        AtomicInteger four = new AtomicInteger(0);
        AtomicInteger five = new AtomicInteger(0);
        internel.put("one",one);
        internel.put("two",two);
        internel.put("three",three);
        internel.put("four",four);
        internel.put("five",five);
        //总评分
        int totalValue = 0;
        EntityWrapper<GoodsCommentBo> queryComment = new EntityWrapper<>();
        queryComment.eq(GoodsCommentBo.Key.goodsId.toString(), goodsId);
        queryComment.isNull(GoodsCommentBo.Key.forCommentId.toString());
        List<GoodsCommentBo> commentBoList = iGoodsCommentBiz.selectList(queryComment);
        for(GoodsCommentBo item : commentBoList){
            //查询该商品的星级
            EntityWrapper<GoodsPointBo> queryPoint = new EntityWrapper<>();
            queryPoint.eq(GoodsPointBo.Key.commentId.toString(), item.getId());
            queryPoint.eq(GoodsPointBo.Key.type.toString(), Integer.parseInt(GoodsPointEnum.PRODUCT.getKey()));
            GoodsPointBo productPoint = iGoodsPointBiz.selectOne(queryPoint);
            log.info("point{}",productPoint.getGoodsPoint());
            totalValue += productPoint.getGoodsPoint().intValue();
            log.info("{}",totalValue);
            if(productPoint.getGoodsPoint().intValue() == 1){
                log.info("{}",one);
                one.getAndIncrement();
                internel.put("one",one);
            }
            if(productPoint.getGoodsPoint().intValue() == 2){
                log.info("{}",two);
                two.getAndIncrement();
                internel.put("two",two);
            }
            if(productPoint.getGoodsPoint().intValue() == 3){
                log.info("{}",three);
                three.getAndIncrement();
                internel.put("three",three);
            }
            if(productPoint.getGoodsPoint().intValue() == 4){
                log.info("{}",four);
                four.getAndIncrement();
                internel.put("four",four);
            }
            if(productPoint.getGoodsPoint().intValue() == 5){
                log.info("{}",five);
                five.getAndIncrement();
                internel.put("five",five);
                log.info("{}",five);
            }
        }
        BigDecimal size;
        if(commentBoList.size() != 0){
            size = new BigDecimal(commentBoList.size());
            log.info("{}",size);
        }
        else
            return null;
        BigDecimal total = new BigDecimal(totalValue);
        log.info("{}",total);
        avgPoint = total.divide(size, 2, BigDecimal.ROUND_HALF_UP);
        log.info("{}",avgPoint);
        BigDecimal starOne = new BigDecimal(internel.get("one").get());
        BigDecimal starTwo = new BigDecimal(internel.get("two").get());
        BigDecimal starThree = new BigDecimal(internel.get("three").get());
        BigDecimal starFour = new BigDecimal(internel.get("four").get());
        BigDecimal starFive = new BigDecimal(internel.get("five").get());
        proportion.put("one", starOne.divide(size, 2, BigDecimal.ROUND_HALF_UP));
        proportion.put("two", starTwo.divide(size, 2, BigDecimal.ROUND_HALF_UP));
        proportion.put("three", starThree.divide(size, 2, BigDecimal.ROUND_HALF_UP));
        proportion.put("four", starFour.divide(size, 2, BigDecimal.ROUND_HALF_UP));
        proportion.put("five", starFive.divide(size, 2, BigDecimal.ROUND_HALF_UP));
        map.put(avgPoint, proportion);
        return map;
    }






}
