package cn.farwalker.ravv.service.sys.message.model;

import lombok.Data;

/**
 * @description: 包含各类消息是否为空字段
 * @param:
 * @return
 * @author Mr.Simple
 * @date 2019/1/12 16:38
 */
@Data
public class UnReadMessageVo {
    SystemMessageBo notification;
    SystemMessageBo consult;
    SystemMessageBo promotion;
    SystemMessageBo orderInfo;

    boolean unReadNotification;
    boolean unReadConsult;
    boolean unReadPromotion;
    boolean unReadOrderInfo;

    boolean notificationIsNull;
    boolean consultIsNull;
    boolean promotionIsNull;
    boolean orderInfoIsNull;
}
