package cn.farwalker.ravv.service.sys.message.biz.impl;

import cn.farwalker.ravv.common.constants.MessageTypeEnum;
import cn.farwalker.ravv.common.constants.OrderMessageEnum;
import cn.farwalker.ravv.common.constants.ReturnsMessageEnum;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsBiz;
import cn.farwalker.ravv.service.goodsext.consult.biz.IGoodsConsultBiz;
import cn.farwalker.ravv.service.goodsext.consult.model.GoodsConsultBo;
import cn.farwalker.ravv.service.goodssku.skudef.biz.IGoodsSkuDefBiz;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuDefBo;
import cn.farwalker.ravv.service.member.pam.member.biz.IPamMemberBiz;
import cn.farwalker.ravv.service.member.pam.member.model.PamMemberBo;
import cn.farwalker.ravv.service.order.ordergoods.biz.IOrderGoodsBiz;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoBiz;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderStatusEnum;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsBiz;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsDetailBiz;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsGoodsStatusEnum;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsTypeEnum;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsBo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsDetailBo;
import cn.farwalker.ravv.service.sys.message.biz.ISystemMessageBiz;
import cn.farwalker.ravv.service.sys.message.biz.ISystemMessageService;
import cn.farwalker.ravv.service.sys.message.dao.ISystemMessageDao;
import cn.farwalker.ravv.service.sys.message.model.*;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SystemMessageServiceImpl implements ISystemMessageService {

    @Autowired
    private ISystemMessageBiz iSystemMessageBiz;

    @Autowired
    private IGoodsConsultBiz iGoodsConsultBiz;

    @Autowired
    private IOrderGoodsBiz iOrderGoodsBiz;

    @Autowired
    private IGoodsSkuDefBiz iGoodsSkuDefBiz;

    @Autowired
    private IOrderReturnsDetailBiz iOrderReturnsDetailBiz;

    @Autowired
    private IGoodsBiz iGoodsBiz;

    @Autowired
    private IPamMemberBiz iPamMemberBiz;

    @Autowired
    private IOrderInfoBiz iOrderInfoBiz;

    @Autowired
    private ISystemMessageDao iSystemMessageDao;

    @Autowired
    private IOrderReturnsBiz iOrderReturnsBiz;

    /**
     * @description: 获取系统通知
     * @param: memberId, currentPage, pageSize
     * @return list
     * @author Mr.Simple
     * @date 2019/1/10 15:13
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public WebMessageVo updateAndGetSystemNotification(Long memberId, int currentPage, int pageSize) {
        Date now = new Date();
        //查找接收人为memberId，消息类型为系统通知的消息
        WebMessageVo webMessageVo = new WebMessageVo();
        Page<SystemMessageBo> page = new Page<>(currentPage, pageSize);
        EntityWrapper<SystemMessageBo> queryMessage = new EntityWrapper<>();
        queryMessage.eq(SystemMessageBo.Key.recieveMemberId.toString(), memberId);
        queryMessage.eq(SystemMessageBo.Key.type.toString(), MessageTypeEnum.NOTIFICATION.getKey());
        queryMessage.le(SystemMessageBo.Key.publishTime.toString(), now);
        queryMessage.orderBy(SystemMessageBo.Key.publishTime.toString(), false);
        Page<SystemMessageBo> queryMessagePage = iSystemMessageBiz.selectPage(page, queryMessage);
        List<SystemMessageBo> notificationList = queryMessagePage.getRecords();
        if(notificationList.size() != 0){
            notificationList.forEach(item->{
                if(item.getIcon() != null)
                    item.setIcon(Tools.string.assembleImg(item.getIcon()));
            });
            webMessageVo.setNotificationList(notificationList);
            webMessageVo.setListIsNull(false);
            updateReadStatu(notificationList);
        }
        else {
            webMessageVo.setListIsNull(true);
        }

        //查找出其他两项是否未读
        SystemMessageBo consult = getUnReadMessage(memberId, MessageTypeEnum.CONSULT.getKey());
        if(consult != null){
            if(consult.getHaveRead())
                webMessageVo.setUnReadConsult(false);
            else
                webMessageVo.setUnReadConsult(true);
        }


        SystemMessageBo promotion = getUnReadMessage(memberId, MessageTypeEnum.PROMOTION.getKey());
        if(promotion != null){
            if(promotion.getHaveRead())
                webMessageVo.setUnReadPromotion(false);
            else
                webMessageVo.setUnReadPromotion(true);
        }

        SystemMessageBo orderInfo = getUnReadMessage(memberId, MessageTypeEnum.ORDERINFO.getKey());
        if(orderInfo != null){
            if(orderInfo.getHaveRead())
                webMessageVo.setUnReadOrderInfo(false);
            else
                webMessageVo.setUnReadOrderInfo(true);
        }


        return webMessageVo;
    }

    /**
     * @description: 获取所有问答类型消息  规则：QUESTION表示收到推送的问题，ANSWER表示提出的问题有新的回答
     * @param: memberId, currentPage, pageSize
     * @return list
     * @author Mr.Simple
     * @date 2019/1/10 15:16
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ConsultListVo updateAndGetConsult(Long memberId, int currentPage, int pageSize) {
        Date now = new Date();
        //查找接收人为memebrId，消息类型为问答的消息
        ConsultListVo consultListVo = new ConsultListVo();
        List<MessageConsultVo> consultMessageList = new ArrayList<>();
        Page<SystemMessageBo> page = new Page<>(currentPage, pageSize);
        EntityWrapper<SystemMessageBo> queryMessage = new EntityWrapper<>();
        queryMessage.eq(SystemMessageBo.Key.recieveMemberId.toString(), memberId);
        queryMessage.eq(SystemMessageBo.Key.type.toString(), MessageTypeEnum.CONSULT.getKey());
        queryMessage.le(SystemMessageBo.Key.publishTime.toString(), now);
        queryMessage.orderBy(SystemMessageBo.Key.publishTime.toString(), false);
        Page<SystemMessageBo> queryMessagePage = iSystemMessageBiz.selectPage(page, queryMessage);
        List<SystemMessageBo> consultList = queryMessagePage.getRecords();
        if(consultList.size() != 0){
            consultList.forEach(item->{
                MessageConsultVo consult = new MessageConsultVo();
                //如果消息为推送的问题，则只查出问题
                if(item.getQaType().equals(MessageTypeEnum.QUESTION.getKey())){
                    GoodsConsultBo question = new GoodsConsultBo();
                    //查出问题，包括商品图片
                    question = iGoodsConsultBiz.selectById(item.getConsultId());
                    consult.setQuestion(question);
                    consult.setHasQuestion(true);
                }
                //如果消息为推送的回答，则查出问题和最新一条回答
                else if(item.getQaType().equals(MessageTypeEnum.ANSWER.getKey())){
                    GoodsConsultBo question = new GoodsConsultBo();
                    //查出问题，包括商品图片
                    question = iGoodsConsultBiz.selectById(item.getConsultId());
                    consult.setQuestion(question);
                    //根据消息发布者的memberId查出回答
                    GoodsConsultBo answer = new GoodsConsultBo();
                    EntityWrapper<GoodsConsultBo> queryAnswer = new EntityWrapper<>();
                    queryAnswer.eq(GoodsConsultBo.Key.memberId.toString(), item.getPublishMemberId());
                    queryAnswer.eq(GoodsConsultBo.Key.replyId.toString(), item.getConsultId());
                    answer = iGoodsConsultBiz.selectOne(queryAnswer);
                    consult.setAnswer(answer);
                    //查出该问题的回答数
                    EntityWrapper<GoodsConsultBo> queryCount = new EntityWrapper<>();
                    queryCount.eq(GoodsConsultBo.Key.replyId.toString(), item.getConsultId());
                    int answerCount = 0;
                    answerCount = iGoodsConsultBiz.selectCount(queryCount);
                    if(answerCount < 0)
                        throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
                    consult.setAnswerCount(answerCount);
                    consult.setHasQuestion(false);
                }
                if(item.getIcon() != null){
                    consult.setImgUrl(QiniuUtil.getFullPath(item.getIcon()));
                }
                consult.setMessageId(item.getId());
                //设置发布时间
                consult.setPublishTime(item.getPublishTime());
                //设置标题
                consult.setTitle(item.getTitle());
                consultMessageList.add(consult);
            });
            consultListVo.setConsultList(consultMessageList);
            consultListVo.setListIsNull(false);
            updateReadStatu(consultList);
        }else {
            consultListVo.setListIsNull(true);
        }
        return consultListVo;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public SystemMessageVo updateAndGetProductPromotion(Long memberId, int currentPage, int pageSize) {
        Date now = new Date();
        SystemMessageVo systemMessageVo = new SystemMessageVo();
        //查找出接收人为memberId，消息类型为优惠推荐的消息
        List<SystemMessageBo> promotionList = new ArrayList<>();
        Page<SystemMessageBo> page = new Page<>(currentPage, pageSize);
        EntityWrapper<SystemMessageBo> queryMessage = new EntityWrapper<>();
        queryMessage.eq(SystemMessageBo.Key.recieveMemberId.toString(), memberId);
        queryMessage.eq(SystemMessageBo.Key.type.toString(), MessageTypeEnum.PROMOTION.getKey());
        queryMessage.le(SystemMessageBo.Key.publishTime.toString(), now);
        queryMessage.orderBy(SystemMessageBo.Key.publishTime.toString(), false);
        Page<SystemMessageBo> queryMessagePage = iSystemMessageBiz.selectPage(page, queryMessage);
        promotionList = queryMessagePage.getRecords();
        if(promotionList.size() != 0){
            promotionList.forEach(item->{
                if(item.getIcon() != null)
                    item.setIcon(Tools.string.assembleImg(item.getIcon()));
            });
            systemMessageVo.setMessageBoList(promotionList);
            systemMessageVo.setListIsNull(false);
            updateReadStatu(promotionList);
        }
        else {
            systemMessageVo.setListIsNull(true);
        }

        return systemMessageVo;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public OrderInfoMessageVo updateAndGetOrderInfo(Long memberId, int currentPage, int pageSize) {
        Date now = new Date();
        OrderInfoMessageVo orderInfoMessageVo = new OrderInfoMessageVo();
        List<SystemMessageBo> orderInfoList = new ArrayList<>();
        Page<SystemMessageBo> page = new Page<>(currentPage, pageSize);
        EntityWrapper<SystemMessageBo> queryMessage = new EntityWrapper<>();
        queryMessage.eq(SystemMessageBo.Key.recieveMemberId.toString(), memberId);
        queryMessage.eq(SystemMessageBo.Key.type.toString(), MessageTypeEnum.ORDERINFO.getKey());
        queryMessage.le(SystemMessageBo.Key.publishTime.toString(), now);
        queryMessage.orderBy(SystemMessageBo.Key.publishTime.toString(), false);
        Page<SystemMessageBo> queryMessagePage = iSystemMessageBiz.selectPage(page, queryMessage);
        orderInfoList = queryMessagePage.getRecords();
        List<OrderInfoMessage> orderInfoMessageList = new ArrayList<>();
        if(orderInfoList.size() != 0){
            //拼装图片
            orderInfoList.forEach(item->{
                OrderInfoMessage orderInfoMessage = new OrderInfoMessage();

                if(item.getIcon() != null){
                    item.setIcon(Tools.string.assembleImg(item.getIcon()));
                }
                BeanUtils.copyProperties(item, orderInfoMessage);
                if(orderInfoMessage.getOrderId() != null){
                    orderInfoMessage.setHasOrder(true);
                }
                else if(orderInfoMessage.getReturnsOrderId() != null){
                    orderInfoMessage.setHasOrder(false);
                    //根据returnsOrderId判断退货类型
                    OrderReturnsBo orderReturnsBo = iOrderReturnsBiz.selectById(orderInfoMessage.getReturnsOrderId());
                    orderInfoMessage.setReturnsType(orderReturnsBo.getReturnsType().getKey());
                }
                orderInfoMessageList.add(orderInfoMessage);
            });
            orderInfoMessageVo.setOrderInfoMessageList(orderInfoMessageList);
            orderInfoMessageVo.setListIsNull(false);
            updateReadStatu(orderInfoList);
        }
        else {
            orderInfoMessageVo.setListIsNull(true);
        }
        return orderInfoMessageVo;
    }

    /**
     * @description: APP获取所有类型消息的未读情况
     * @param: memberId
     * @return list
     * @author Mr.Simple
     * @date 2019/1/10 18:04
     */
    @Override
    public UnReadMessageVo getUnReadMessageList(Long memberId) {
        //查找出该账号下各类型中最新一条消息
        UnReadMessageVo unReadMessageVo = new UnReadMessageVo();
        //系统通知
        SystemMessageBo notification = getUnReadMessage(memberId, MessageTypeEnum.NOTIFICATION.getKey());
        if(notification != null){
            unReadMessageVo.setNotification(notification);
            unReadMessageVo.setNotificationIsNull(false);
            if(notification.getHaveRead()){
                unReadMessageVo.setUnReadNotification(false);
            }
            else
                unReadMessageVo.setUnReadNotification(true);
        }
        else{
            unReadMessageVo.setNotificationIsNull(true);
        }

        //问答互动
        SystemMessageBo consult = getUnReadMessage(memberId, MessageTypeEnum.CONSULT.getKey());
        if(consult != null){
            unReadMessageVo.setConsult(consult);
            unReadMessageVo.setConsultIsNull(false);
            if(consult.getHaveRead()){
                unReadMessageVo.setUnReadConsult(false);
            }
            else
                unReadMessageVo.setUnReadConsult(true);
        }
        else {
            unReadMessageVo.setConsultIsNull(true);
        }

        //优惠推荐
        SystemMessageBo promotion = getUnReadMessage(memberId, MessageTypeEnum.PROMOTION.getKey());
        if(promotion != null){
            unReadMessageVo.setPromotion(promotion);
            unReadMessageVo.setPromotionIsNull(false);
            if(promotion.getHaveRead()){
                unReadMessageVo.setUnReadPromotion(false);
            }
            else
                unReadMessageVo.setUnReadPromotion(true);
        }
        else {
            unReadMessageVo.setPromotionIsNull(true);
        }

        //订单消息
        SystemMessageBo orderInfo = getUnReadMessage(memberId, MessageTypeEnum.ORDERINFO.getKey());
        if(orderInfo != null){
            unReadMessageVo.setOrderInfo(orderInfo);
            unReadMessageVo.setOrderInfoIsNull(false);
            if(orderInfo.getHaveRead()){
                unReadMessageVo.setUnReadOrderInfo(false);
            }
            else
                unReadMessageVo.setUnReadOrderInfo(true);
        }
        else {
            unReadMessageVo.setOrderInfoIsNull(true);
        }

        return unReadMessageVo;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String deleteMessageByType(Long memberId, String type) {
        EntityWrapper<SystemMessageBo> queryMessage = new EntityWrapper<>();
        queryMessage.eq(SystemMessageBo.Key.recieveMemberId.toString(), memberId);
        queryMessage.eq(SystemMessageBo.Key.type.toString(), type);
        if(!iSystemMessageBiz.delete(queryMessage))
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR + "delete by type");
        return "delete by type successful";
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String deleteMessageById(Long messageId) {
        if(!iSystemMessageBiz.deleteById(messageId))
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR + "delete by id");
        return "delete by id successful";
    }

    @Override
    public String deleteAllMessage(Long memberId) {
        if(!iSystemMessageBiz.delete(Condition.create().eq(SystemMessageBo.Key.recieveMemberId.toString(), memberId))){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR + "删除所有消息失败");
        }
        return "delete all success!";
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String updateToRead(Long memberId) {
        Date now = new Date();
        List<SystemMessageBo> unReadList = new ArrayList<>();
        EntityWrapper<SystemMessageBo> queryUnRead = new EntityWrapper<>();
        queryUnRead.eq(SystemMessageBo.Key.recieveMemberId.toString(), memberId);
        queryUnRead.eq(SystemMessageBo.Key.haveRead.toString(), false);
        unReadList = iSystemMessageBiz.selectList(queryUnRead);
        if(unReadList.size() != 0){
            unReadList.forEach(item->{
                item.setGmtModified(now);
                item.setHaveRead(true);
                if(!iSystemMessageBiz.updateById(item))
                    throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
            });
        }
        else{
            return "no data need to change";
        }
        return "update successful";
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean addOrderMessage(Long memberId, Long orderId, String logisticsNo, OrderStatusEnum type, Date publishTime) {
        if(orderId == null){
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR + "orderIdList.size = 0");
        }

        if(type == null ){
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR + "type error");
        }
        //拆解主单号
        List<Long> orderIdList = new ArrayList<>();
        //根据主单号查出子单号，没有子单就将主单号添加到list
        List<OrderInfoBo> orderList = iOrderInfoBiz.selectList(Condition.create().eq(OrderInfoBo.Key.pid.toString(), orderId));
        if(orderList.size() != 0){
            orderList.forEach(item->{
                orderIdList.add(item.getId());
            });
        }
        else {
            orderIdList.add(orderId);
        }
        OrderStatusEnum orderStatusEnum = type;
        if(orderStatusEnum != null){
            if(orderStatusEnum.equals(OrderStatusEnum.PAID_UNSENDGOODS)){
                if(!addOrderMessageAgain(memberId, orderIdList, OrderMessageEnum.PAID_UNSENDGOODS.getKey(),
                        MessageTypeEnum.ORDERINFO.getKey(), logisticsNo, publishTime))
                    throw new WakaException(RavvExceptionEnum.INSERT_ERROR + "add失败");
            }
            else if(orderStatusEnum.equals(OrderStatusEnum.SENDGOODS_UNCONFIRM)){
                if(!addOrderMessageAgain(memberId, orderIdList, OrderMessageEnum.SENDGOODS_UNCONFIRM.getKey(),
                        MessageTypeEnum.ORDERINFO.getKey(), logisticsNo, publishTime)){
                    throw new WakaException(RavvExceptionEnum.INSERT_ERROR + "add失败");
                }
            }
            else if(orderStatusEnum.equals(OrderStatusEnum.SING_GOODS)){
                if(!addOrderMessageAgain(memberId, orderIdList, OrderMessageEnum.SING_GOODS.getKey(),
                        MessageTypeEnum.ORDERINFO.getKey(), logisticsNo, publishTime)){
                    throw new WakaException(RavvExceptionEnum.INSERT_ERROR + "add失败");
                }
            }
        }else {
            return false;
        }
        return true;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean addReturnGoodsMessage(Long memberId, Long returnsId, ReturnsTypeEnum returnsType,
                      ReturnsGoodsStatusEnum returnsStatus, String logisticsNo, String reason, Date publishTime) {
        String message = null;
        String goodsName = getReturnsDetailList(returnsId).get(0).getGoodsName();
        if(returnsType == null || returnsStatus == null){
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR + "type error");
        }
        //退换货状态
        ReturnsGoodsStatusEnum returnsGoodsStatusEnum = returnsStatus;
        //退换货类型
        ReturnsTypeEnum returnsTypeEnum = returnsType;
        if(returnsGoodsStatusEnum != null){
            //换货审核通过
            if(ReturnsGoodsStatusEnum.exchangeAuditSucess.equals(returnsGoodsStatusEnum)){
                //获取退单明细
                //换货审核通过，将单号设置进消息
                message = "Your exchange audit (\"" + goodsName + " and so on\") have been successful, " +
                        "please waiting for warehouse acceptance goods! ReturnGoodsId:\"" + returnsId +  "\"";
                if(!addReturnsMessageAgain(message, memberId, returnsId, MessageTypeEnum.ORDERINFO.getKey(),
                        ReturnsMessageEnum.CHANGEGOODS.getKey(), logisticsNo, publishTime)){
                    throw new WakaException(RavvExceptionEnum.INSERT_ERROR + returnsTypeEnum.getKey() + "插入消息失败");
                }
            }
            //换货审核不通过
            else if(ReturnsGoodsStatusEnum.exchangeAuditFail.equals(returnsGoodsStatusEnum)){
                message = "Your exchange audit (\"" + goodsName + " and so on\") failed! Because:" + reason;
                if(!addReturnsMessageAgain(message, memberId, returnsId, MessageTypeEnum.ORDERINFO.getKey(),
                        ReturnsMessageEnum.CHANGEGOODS.getKey(), logisticsNo, publishTime)){
                    throw new WakaException(RavvExceptionEnum.INSERT_ERROR + returnsTypeEnum.getKey() + "插入消息失败");
                }
            }
            //拒绝换货
            else if(ReturnsGoodsStatusEnum.refuseExchange.equals(returnsGoodsStatusEnum)){
                message = "The item you returned (\"" + goodsName + " and so on\") did not pass the acceptance!" +
                        " Because:" + reason + "\"";
                if(!addReturnsMessageAgain(message, memberId, returnsId, MessageTypeEnum.ORDERINFO.getKey(),
                        ReturnsMessageEnum.CHANGEGOODS.getKey(), logisticsNo, publishTime)){
                    throw new WakaException(RavvExceptionEnum.INSERT_ERROR + returnsTypeEnum.getKey() + "插入消息失败");
                }
            }
            //待买家收货
            else if(ReturnsGoodsStatusEnum.buyerWaitReceived.equals(returnsGoodsStatusEnum)){
                message = "Your exchange goods (\"" + goodsName + " and so on\") have been shipped!" +
                        "Logistics number:\"" + logisticsNo + "\"";
                if(!addReturnsMessageAgain(message, memberId, returnsId, MessageTypeEnum.ORDERINFO.getKey(),
                        ReturnsMessageEnum.CHANGEGOODS.getKey(), logisticsNo, publishTime)){
                    throw new WakaException(RavvExceptionEnum.INSERT_ERROR + returnsTypeEnum.getKey() + "插入消息失败");
                }
            }
            //换货成功
            else if(ReturnsGoodsStatusEnum.exchangeSucess.equals(returnsGoodsStatusEnum)){
                message = "Your exchange application (\"" + goodsName + " and so on\") has been processed!";
                if(!addReturnsMessageAgain(message, memberId, returnsId, MessageTypeEnum.ORDERINFO.getKey(),
                        ReturnsMessageEnum.CHANGEGOODS.getKey(), logisticsNo, publishTime)){
                    throw new WakaException(RavvExceptionEnum.INSERT_ERROR + returnsTypeEnum.getKey() + "插入消息失败");
                }
            }
            //退款审核通过
            else if(ReturnsGoodsStatusEnum.refundAuditSucess.equals(returnsGoodsStatusEnum)){
                //退款退货
                if(ReturnsTypeEnum.ReGoods.equals(returnsTypeEnum)){
                    message = "Your refund audit (\"" + goodsName + " and so on\") have been successful, " +
                            "please waiting for warehouse acceptance goods! ReturnGoodsId:\"" + returnsId + "\"";
                    if(!addReturnsMessageAgain(message, memberId, returnsId, MessageTypeEnum.ORDERINFO.getKey(),
                            ReturnsMessageEnum.REGOODS.getKey(), logisticsNo, publishTime)){
                        throw new WakaException(RavvExceptionEnum.INSERT_ERROR + returnsTypeEnum.getKey() + "插入消息失败");
                    }
                }
                //仅退款
                else if(ReturnsTypeEnum.Refund.equals(returnsTypeEnum)){
                    message = "Your refund audit (\"" + goodsName + " and so on\") have been successful! " +
                            "ReturnGoodsId:\"" + returnsId + "\"";
                    if(!addReturnsMessageAgain(message, memberId, returnsId, MessageTypeEnum.ORDERINFO.getKey(),
                            ReturnsMessageEnum.REFUND.getKey(), logisticsNo, publishTime)){
                        throw new WakaException(RavvExceptionEnum.INSERT_ERROR + returnsTypeEnum.getKey() + "插入消息失败");
                    }
                }
            }
            //退款审核不通过
            else if(ReturnsGoodsStatusEnum.refundAuditFail.equals(returnsGoodsStatusEnum)){
                message = "Your refund audit (\"" + goodsName + " and so on\") failed! " +
                        "Because:\"" + reason + "\"";
                if(!addReturnsMessageAgain(message, memberId, returnsId, MessageTypeEnum.ORDERINFO.getKey(),
                        ReturnsMessageEnum.REFUND.getKey(), logisticsNo, publishTime)){
                    throw new WakaException(RavvExceptionEnum.INSERT_ERROR + returnsTypeEnum.getKey() + "插入消息失败");
                }
            }
            //拒绝退货退款
            else if(ReturnsGoodsStatusEnum.refuseRefund.equals(returnsGoodsStatusEnum)){
                message = "The item you refund (\"" + goodsName + " and so on\") did not pass the acceptance!" +
                        " Because:" + reason + "\"";
                if(!addReturnsMessageAgain(message, memberId, returnsId, MessageTypeEnum.ORDERINFO.getKey(),
                        ReturnsMessageEnum.REFUND.getKey(), logisticsNo, publishTime)){
                    throw new WakaException(RavvExceptionEnum.INSERT_ERROR + returnsTypeEnum.getKey() + "插入消息失败");
                }
            }
            //退款成功
            else if(ReturnsGoodsStatusEnum.refundSucess.equals(returnsGoodsStatusEnum)){
                message = "Your refund has been returned to the payment account, please check it!";
                if(!addReturnsMessageAgain(message, memberId, returnsId, MessageTypeEnum.ORDERINFO.getKey(),
                        ReturnsMessageEnum.REFUND.getKey(), logisticsNo, publishTime)){
                    throw new WakaException(RavvExceptionEnum.INSERT_ERROR + returnsTypeEnum.getKey() + "插入消息失败");
                }
            }
        }
        else{
            return false;
        }

        return true;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String addPromotionMessage(SystemMessageBo promotionMessage) {
        //给每个用户都插入该消息
        Date now = new Date();
        promotionMessage.setHaveRead(false);
        promotionMessage.setGmtCreate(now);
        promotionMessage.setGmtModified(now);
        //拆除图片路径
        String img = "";
        if(promotionMessage.getIcon() != null){
            img = Tools.string.splitImg(promotionMessage.getIcon());
            promotionMessage.setIcon(img);
        }
        List<SystemMessageBo> promotionList = new ArrayList<>();
        //查出所有用户id
        List<PamMemberBo> allPamMember = new ArrayList<>();
        allPamMember = iPamMemberBiz.selectList(Condition.create()
                                    .isNotNull(PamMemberBo.Key.emailAccount.toString()));
        if(allPamMember.size() != 0){
            allPamMember.forEach(item->{
                SystemMessageBo promotion = new SystemMessageBo();
                BeanUtils.copyProperties(promotionMessage, promotion);
                promotion.setRecieveMemberId(item.getMemberId());
                promotionList.add(promotion);
            });
        }

        if(!iSystemMessageBiz.insertBatch(promotionList)){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR + "system message promotion insert");
        }
        return "successful";
    }


    private SystemMessageBo getUnReadMessage(Long memberId, String type){
        Date now = new Date();
        SystemMessageBo systemMessageBo = null;
        EntityWrapper<SystemMessageBo> queryUnReadMessage = new EntityWrapper<>();
        queryUnReadMessage.orderBy(SystemMessageBo.Key.publishTime.toString(), false);
        queryUnReadMessage.le(SystemMessageBo.Key.publishTime.toString(), now);
        queryUnReadMessage.eq(SystemMessageBo.Key.recieveMemberId.toString(), memberId);
        queryUnReadMessage.eq(SystemMessageBo.Key.type.toString(), type);
        List<SystemMessageBo> list = iSystemMessageBiz.selectList(queryUnReadMessage);
        if(list.size() != 0){
            systemMessageBo = list.get(0);
            if(systemMessageBo.getIcon() != null)
                systemMessageBo.setIcon(QiniuUtil.getFullPath(systemMessageBo.getIcon()));
        }

        return systemMessageBo;
    }

    private void updateReadStatu(List<SystemMessageBo> unReadList){
        unReadList.forEach(item->{
            //将所有未读消息都置为已读
            if(item.getHaveRead().equals(false)){
                SystemMessageBo updateMessage = new SystemMessageBo();
                updateMessage.setHaveRead(true);
                updateMessage.setId(item.getId());
                if(!iSystemMessageBiz.updateById(updateMessage))
                    throw new WakaException(RavvExceptionEnum.UPDATE_ERROR + "systemnotification");
            }
        });
    }



    /**
     * @description: 
     * @param: 
     * @return
     * @author Mr.Simple
     * @date 2019/1/15 10:05 
     */ 
    private boolean addReturnsMessageAgain(String message, Long memberId, Long returnsId, String type, String title,
                                           String logisticsNo, Date publishTime){
        List<Long> skuIdList = getSkuIdListByReturnsId(returnsId);
        String img = skuIdToString(skuIdList);
        if(!addReturnsMessage(message, type, title, memberId, publishTime, img, logisticsNo, returnsId)){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR + title + "插入消息失败");
        }
        return true;
    }

    /**
     * @description: 可重用接口，保存信息   使用订单待发货和已收货状态
     * @param:
     * @return boolean
     * @author Mr.Simple
     * @date 2019/1/14 16:03
     */
    private boolean addOrderMessageAgain(Long memberId, List<Long> orderIdList, String title, String type,
                                         String logisticsNo, Date publishTime){
        orderIdList.forEach(item->{
            //发送付款成功消息
            //查找出订单商品
            String message = null;
            List<Long> skuIdList = new ArrayList<>();
            skuIdList = getSkuIdList(item);
            //查找sku图片

            String img = skuIdToString(skuIdList);
            if(title.equals(OrderMessageEnum.PAID_UNSENDGOODS.getKey())){
                message = "Your order:\"" + item + "\" hava been successfully paid, " +
                        "we will arrange delivery for you as soon as possible.";
            }
            else if(title.equals(OrderMessageEnum.SENDGOODS_UNCONFIRM.getKey())){
                message = "Your order:\"" + item + "\" hava been dispatched! Logistics number:\"" + logisticsNo + "\"";
            }
            else if (title.equals(OrderMessageEnum.SING_GOODS.getKey())) {
                //查找出订单商品第一条信息
                List<OrderGoodsBo> orderGoodsList = getOrderGoodsList(item);
                if(orderGoodsList.size() == 0)
                    throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR + "该订单号未查出任何信息");
                String goodsName = orderGoodsList.get(0).getGoodsName();
                message = "Your order\"" + goodsName + " and so on \" has been signed";
            }
            if(!addMessage(message, type, title, memberId, publishTime, img ,logisticsNo, item))
                throw new WakaException(RavvExceptionEnum.INSERT_ERROR + type + " 消息添加失败");

        });
        return true;
    }

    /**
     * @description: 获取订单商品
     * @param: orderId
     * @return list
     * @author Mr.Simple
     * @date 2019/1/14 16:42
     */
    private List<OrderGoodsBo> getOrderGoodsList(Long orderId){
        List<OrderGoodsBo> orderGoodsList = iOrderGoodsBiz.selectList(Condition.create()
                .eq(OrderGoodsBo.Key.orderId.toString(), orderId));
        if(orderGoodsList.size() == 0){
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR + "orderGoodsList为空");
        }
        return orderGoodsList;
    }

    /**
     * @description: 获取退换单商品
     * @param: returnsId
     * @return list
     * @author Mr.Simple
     * @date 2019/1/14 16:42
     */
    private List<OrderReturnsDetailBo> getReturnsDetailList(Long returnsId){
        List<OrderReturnsDetailBo> returnsDetailList = iOrderReturnsDetailBiz.selectList(Condition.create()
                .eq(OrderReturnsDetailBo.Key.returnsId.toString(), returnsId));
        if(returnsDetailList.size() == 0){
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR + "returnsDetailList为空");
        }
        return returnsDetailList;
    }

    /**
     * @description: 根据orderId获取skuIdList
     * @param: orderId
     * @return list
     * @author Mr.Simple
     * @date 2019/1/14 16:42
     */
    private List<Long> getSkuIdList(Long orderId){
        List<OrderGoodsBo> orderGoodsList = getOrderGoodsList(orderId);
        List<Long> skuIdList = new ArrayList<>();
        if(orderGoodsList.size() > 4){
            for(int i = 0; i < 4;i++){
                skuIdList.add(orderGoodsList.get(i).getSkuId());
            }
        }
        else {
            orderGoodsList.forEach(order->{
                skuIdList.add(order.getSkuId());
            });
        }
        return skuIdList;
    }

    /**
     * @description: 根据returnsId获取skuIdList
     * @param: returnsId
     * @return list
     * @author Mr.Simple
     * @date 2019/1/14 16:42
     */
    private List<Long> getSkuIdListByReturnsId(Long returnsId){
        List<OrderReturnsDetailBo> returnsDetailList = getReturnsDetailList(returnsId);

        List<Long> skuIdList = new ArrayList<>();
        if(returnsDetailList.size() > 4){
            for(int i = 0; i < 4;i++){
                skuIdList.add(returnsDetailList.get(i).getSkuId());
            }
        }
        else {
            returnsDetailList.forEach(returnsDetail->{
                skuIdList.add(returnsDetail.getSkuId());
            });
        }
        return skuIdList;
    }

    /**
     * @description: 添加消息
     * @param:
     * @return boolean
     * @author Mr.Simple
     * @date 2019/1/14 16:43
     */
    private boolean addMessage(String message, String type, String title, Long memberId, Date publishTime,
                               String img, String logisticsNo, Long orderId){
        Date now = new Date();
        SystemMessageBo orderMessage = new SystemMessageBo();
        orderMessage.setHaveRead(false);

        if(Tools.string.isNotEmpty(img)){
            orderMessage.setIcon(img);
        }
        orderMessage.setContent(message);
        orderMessage.setType(type);
        orderMessage.setTitle(title);
        orderMessage.setOrderId(orderId);
        if(Tools.string.isNotEmpty(logisticsNo)){
            orderMessage.setLogisticsNo(logisticsNo);
        }
        orderMessage.setRecieveMemberId(memberId);
        orderMessage.setGmtModified(now);
        orderMessage.setGmtCreate(now);
        orderMessage.setPublishTime(publishTime);
        if(!iSystemMessageBiz.insert(orderMessage))
            return false;
        return true;
    }

    private boolean addReturnsMessage(String message, String type, String title, Long memberId, Date publishTime,
                               String img, String logisticsNo, Long returnsOrderId){
        Date now = new Date();
        SystemMessageBo orderMessage = new SystemMessageBo();
        orderMessage.setHaveRead(false);

        if(Tools.string.isNotEmpty(img)){
            orderMessage.setIcon(img);
        }
        orderMessage.setContent(message);
        orderMessage.setType(type);
        orderMessage.setTitle(title);
        orderMessage.setReturnsOrderId(returnsOrderId);
        if(Tools.string.isNotEmpty(logisticsNo)){
            orderMessage.setLogisticsNo(logisticsNo);
        }
        orderMessage.setRecieveMemberId(memberId);
        orderMessage.setGmtModified(now);
        orderMessage.setGmtCreate(now);
        orderMessage.setPublishTime(publishTime);
        if(!iSystemMessageBiz.insert(orderMessage))
            return false;
        return true;
    }

    /**
     * @description: 获取sku图片并拼接
     * @param:
     * @return string
     * @author Mr.Simple
     * @date 2019/1/14 16:44
     */
    private String skuIdToString(List<Long> skuIdList){
        List<String> imgList = new ArrayList<>();
        String imgUrl = null;
        skuIdList.forEach(skuId->{
            GoodsSkuDefBo skuDefBo = new GoodsSkuDefBo();
            skuDefBo = iGoodsSkuDefBiz.selectById(skuId);
            if(skuDefBo.getImageUrl() != null)
                imgList.add(skuDefBo.getImageUrl());
        });
        StringBuffer imgBuffer = new StringBuffer("");
        if(imgList.size() != 0){
            imgList.forEach(item->{
                imgBuffer.append(item).append(",");
            });
            imgUrl = imgBuffer.substring(0,imgBuffer.length() - 1);
        }


        return imgUrl;
    }

}
