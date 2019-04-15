package cn.farwalker.ravv.service.quartz;

import cn.farwalker.ravv.service.order.operationlog.biz.IOrderLogService;
import cn.farwalker.ravv.service.order.operationlog.biz.IOrderOperationLogBiz;
import cn.farwalker.ravv.service.order.ordergoods.biz.IOrderGoodsService;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoBiz;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderStatusEnum;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderTypeEnum;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsBiz;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsDetailBiz;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsGoodsStatusEnum;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsTypeEnum;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsBo;
import cn.farwalker.ravv.service.sale.profitallot.biz.ISaleProfitAllotBiz;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.util.Tools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class UpdateOrderWihthoutReturns {

    @Autowired
    private IOrderReturnsBiz returnsBiz;

    @Autowired
    private IOrderReturnsDetailBiz returnsDetailBiz;

    @Autowired
    private IOrderLogService orderLogService;

    @Autowired
    private IOrderGoodsService orderGoodsService;

    @Autowired
    private IOrderInfoBiz orderBiz;

    private static final String OperatorText = "系统自动关闭";

    public void updateOrderStatus(Long orderId){
        OrderInfoBo orderInfo = orderBiz.selectById(orderId);
        if(OrderStatusEnum.TRADE_CLOSE.equals(orderInfo.getOrderStatus())){
            log.info("订单已关闭，这里不做操作");
        } else {
            // 获取指定订单的所有退换货单
            List<OrderReturnsBo> orderReturnsList = returnsBiz.returnsByOrderId(orderInfo.getId());
            //这里判断退换货商品条件
            if (Tools.collection.isNotEmpty(orderReturnsList)) {
                Boolean isClose = true;
                Integer returnsGoodsNum = 0;// 退换货成功的商品数量初始化
                for (OrderReturnsBo returns : orderReturnsList) {
                    if (!ReturnsGoodsStatusEnum.allowOrderClose(returns.getStatus())) {
                        isClose = false;
                        break;
                    }
                    // 获取退换货成功的商品数量(换货商品不计算)
                    if (returns.getReturnsType() != ReturnsTypeEnum.ChangeGoods) {
                        Integer succeedReGoodsNum = returnsDetailBiz.succeedReGoodsNum(returns.getId());
                        returnsGoodsNum += succeedReGoodsNum;
                    }
                }
                if (isClose) {
                    // 单个订单所有商品数量总和
                    Integer orderGoodsTotal = orderGoodsService.getOrderGoodsTotal(orderInfo.getId());
                    // 退换货成功的商品数量 == 单个订单所有商品数量总和，则订单状态给为“交易取消”
                    if (returnsGoodsNum == orderGoodsTotal) {
                        orderInfo.setOrderStatus(OrderStatusEnum.CANCEL);
                        orderLogService.createLog(orderInfo.getId(), OperatorText, OperatorText,
                                "已发货、签收的订单(30)天自动取消");
                    } else {
                        orderInfo.setOrderStatus(OrderStatusEnum.TRADE_CLOSE);
                        orderLogService.createLog(orderInfo.getId(), OperatorText, OperatorText,
                                "已发货、签收的订单(30)天自动关闭");
                    }
                    orderInfo.setOrderFinishedTime(new Date());
                    //更新订单状态
                    if (!orderBiz.updateById(orderInfo)) {
                        throw new WakaException(RavvExceptionEnum.UPDATE_ERROR + "关闭/取消订单更新失败");
                    }
                } else {
                    orderInfo.setOrderFinishedTime(new Date());
                    orderInfo.setOrderStatus(OrderStatusEnum.TRADE_CLOSE);
                    orderBiz.updateById(orderInfo);
                }
            } else {
                //这里操作没有换货的商品
                orderInfo.setOrderFinishedTime(new Date());
                orderInfo.setOrderStatus(OrderStatusEnum.TRADE_CLOSE);
                if (!orderBiz.updateById(orderInfo)) {
                    throw new WakaException(RavvExceptionEnum.UPDATE_ERROR + "没有换货操作");
                }
                orderLogService.createLog(orderInfo.getId(), OperatorText, OperatorText,
                        "已发货、签收的订单(30)天自动关闭");
            }
        }

    }

}
