package cn.farwalker.ravv.service.order.orderlogistics.model;

import lombok.Data;

@Data
public class LogisticsTraceVo {
    // 时间
    private String acceptTime;
    // 事件描述
    private String acceptStation;
    // 备注
    private String remark;
}
