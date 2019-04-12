package cn.farwalker.ravv.service.sys.message.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderInfoMessageVo {
    List<OrderInfoMessage> orderInfoMessageList;
    boolean listIsNull;
}
