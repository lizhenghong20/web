package cn.farwalker.ravv.service.sys.message.biz;


import cn.farwalker.ravv.service.order.orderinfo.constants.OrderStatusEnum;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsGoodsStatusEnum;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsTypeEnum;
import cn.farwalker.ravv.service.sys.message.model.*;
import cn.farwalker.waka.orm.EnumManager;

import java.util.Date;
import java.util.List;

import cn.farwalker.ravv.service.sys.message.model.ConsultListVo;
import cn.farwalker.ravv.service.sys.message.model.SystemMessageVo;
import cn.farwalker.ravv.service.sys.message.model.UnReadMessageVo;
import cn.farwalker.ravv.service.sys.message.model.WebMessageVo;

public interface ISystemMessageService {

    public WebMessageVo updateAndGetSystemNotification(Long memberId, int currentPage, int pageSize);

    public ConsultListVo updateAndGetConsult(Long memberId, int currentPage, int pageSize);

    public SystemMessageVo updateAndGetProductPromotion(Long memberId, int currentPage, int pageSize);

    public OrderInfoMessageVo updateAndGetOrderInfo(Long memberId, int currentPage, int pageSize);

    public UnReadMessageVo getUnReadMessageList(Long memberId);

    public String deleteMessageByType(Long memberId, String type);

    public String deleteMessageById(Long messageId);

    public String deleteAllMessage(Long memberId);

    public String updateToRead(Long memberId);

    /**
     * 描述：添加订单消息
     * 说明：当OrderStatusEnum类型为 PAID_UNSENDGOODS、SENDGOODS_UNCONFIRM、SING_GOODS 时调用此接口
     * @param memberId 下单人id
     * @param orderId 主单号id
     * @param logisticsNo 物流单号，有值就传值，没有值就传null
     * @param type orderStatusEnum类型，传值见说明，例：OrderStatusEnum.PAID_UNSENDGOODS
     * @param publishTime 消息发布时间，目前传值为调用此接口的时间（后续可能会传指定的时间）
     * @return
     */
    public boolean addOrderMessage(Long memberId, Long orderId, String logisticsNo, OrderStatusEnum type, Date publishTime);

    public boolean addReturnGoodsMessage(Long memberId, Long returnsId, ReturnsTypeEnum returnsType,
                         ReturnsGoodsStatusEnum returnsStatus, String logisticsNo, String reason, Date publishTime);

    public String addPromotionMessage(SystemMessageBo promotionMessage);

}
