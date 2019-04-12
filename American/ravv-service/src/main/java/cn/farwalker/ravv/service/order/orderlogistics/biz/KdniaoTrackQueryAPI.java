package cn.farwalker.ravv.service.order.orderlogistics.biz;

/**
 * @description: 快递鸟
 * @author Mr.Simple
 * @date 2019/4/2 9:28
 */
public interface KdniaoTrackQueryAPI {

    String getOrderTracesByJson(String expCode, String expNo) throws Exception;

}
