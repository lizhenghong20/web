package cn.farwalker.ravv.admin.order;

import cn.farwalker.ravv.service.order.operationlog.model.OrderOperationLogBo;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.order.orderlogistics.model.OrderLogisticsBo;
import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;
import cn.farwalker.ravv.service.order.returns.model.*;
import cn.farwalker.waka.core.JsonResult;
import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.web.crud.QueryFilter;

import java.util.List;
import java.util.Map;

public interface AdminOrderService {
    Boolean createOrderGoods(OrderGoodsBo vo);

    Boolean deleteOrderGoods(Long id);

    OrderGoodsBo getOneOrderGoods(Long id);

    Page<OrderGoodsBo> getListOrderGoods(List<QueryFilter> query, Integer start, Integer size,
                               String sortfield);

    Boolean updateOrderGoods(OrderGoodsBo vo);



    Boolean createOrderPayment(OrderPaymemtBo vo);

    Boolean deleteOrderPayment(Long id);

    OrderPaymemtBo getOneOrderPayment(Long id);

    Page<OrderPaymemtBo> getListOrderPayment(List<QueryFilter> query, Integer start, Integer size,
                                         String sortfield);



    OrderReturnsBo createReturnGoods(Long returnsid, Boolean state);

    Boolean deleteReturnGoods(Long id);

    OrderReturnsBo getOneReturnGoods(Long id);

    Page<OrderReturnsBo> getListReturnGoods(List<QueryFilter> query, Integer start, Integer size,
                                         String sortfield);

    JsonResult<Boolean> warehouseOperateRefund(OrderReturnsVo vo, Long userId);

    Boolean checkAndAcceptanceGoods(OrderReturnsVo vo, Long userId);

    List<Map<String, String>> getReturnsGoodsStatus(Long returnsid);

    OrderLogisticsBo getReturnGoodsLogisticsInfo(Long returnsId, Long orderId);

    Boolean doReturnGoodsSendGoods(OrderLogisticsBo logisbo, Long returnsId, Long userId);

    List<OrderReturnsDetailVo> getReturnsDetail(Long returnsId);

    Page<OrderReturnsBo> getMerchantReturns(Long merchantId, List<QueryFilter> query,
                                             Integer start, Integer size, String sortfield);


    Boolean createReturnLog(OrderReturnLogBo vo);

    Boolean deleteReturnLog(Long id);

    OrderReturnLogBo getOneReturnLog(Long id);

    Page<OrderReturnLogBo> getListReturnLog(List<QueryFilter> query, Integer start, Integer size,
                                         String sortfield, Long returnsId);

    Boolean updateReturnLog(OrderReturnLogBo vo);



    Boolean createReturnDetail(OrderReturnsDetailBo vo);

    Boolean deleteReturnDetail(Long id);

    OrderReturnsDetailBo getOneReturnDetail(Long id);

    Page<OrderReturnsDetailBo> getListReturnDetail(List<QueryFilter> query, Integer start, Integer size,
                                        String sortfield);

    Boolean updateReturnDetail(OrderReturnsDetailBo vo);



    JsonResult<Boolean> doReundPay(OrderReturnsBo returnsBo, Long userId);

    JsonResult<Boolean> serviceOperateRefund(OrderReturnsVo vo, Long userId);

    List<Map<String, String>> getReturnsStatus(Long returnsid);



    Boolean deleteOrderInfo(Long id);

    OrderInfoBo getOneOrderInfo(Long id);

    Page<OrderInfoBo> getListOrderInfo(Long userid, List<QueryFilter> query, Integer start, Integer size,
                                                   String sortfield);

    OrderLogisticsBo getOrderInfoLogisticsInfo(Long orderId);

    OrderLogisticsBo getlogisticsByReturnsId(Long returnsId);

    Boolean doOrderInfoSendGoods(OrderLogisticsBo logisbo, Long userId);

    Boolean updateOrderLogistics(OrderLogisticsBo logisbo, Long userId);

    Boolean wearhouseSendgoods(OrderLogisticsBo logisbo, Long userId);

    Boolean updateOrderInfo(OrderInfoBo vo, Long userId);

    List<OrderGoodsBo> getOrderGoods(Long orderId);

    Boolean doUpdateGoods(OrderGoodsBo goods);

    OrderPaymemtBo getPayment(Long orderid);

    Boolean doUpdatePayment(OrderPaymemtBo paybo, Long userId);

    Boolean doUpdateOrderGoods(Long orderId, List<OrderGoodsBo> orderGoods,
                                Long userId);

    Page<OrderInfoBo> getMerchantOrderInfo(Long merchantId, List<QueryFilter> query,
                                            Integer start, Integer size, String sortfield);



    Boolean createOrderOperationLog(OrderOperationLogBo vo);

    Boolean deleteOrderOperationLog(Long id);

    OrderOperationLogBo getOneOrderOperationLog(Long id);

    Page<OrderOperationLogBo> getListOrderOperationLog(List<QueryFilter> query, Integer start, Integer size,
                                                   String sortfield, Long orderId);

    Boolean updateOrderOperationLog(OrderOperationLogBo vo);

}
