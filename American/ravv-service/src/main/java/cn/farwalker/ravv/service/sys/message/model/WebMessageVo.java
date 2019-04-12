package cn.farwalker.ravv.service.sys.message.model;

import lombok.Data;

import java.util.List;

@Data
public class WebMessageVo{
    List<SystemMessageBo> notificationList;
    boolean unReadConsult;
    boolean unReadPromotion;
    boolean listIsNull;
    boolean unReadOrderInfo;
}
