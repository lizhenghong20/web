package cn.farwalker.ravv.service.order.orderlogistics.biz;

import cn.farwalker.ravv.service.order.orderlogistics.model.LogisticsTraceVo;

import java.util.List;

public interface IOrderLogisticsService {

    public List<LogisticsTraceVo> getLogisticsTrace(Long orderId) throws Exception;

    public List<LogisticsTraceVo> test(String expCode, String expNo) throws Exception;
}
