package cn.farwalker.ravv.service.order.orderinfo.biz.impl;

import org.springframework.stereotype.Component;

import cn.farwalker.ravv.service.order.orderinfo.biz.ISendGoodsBiz;

@Component
public class SendGoodsBizImpl implements ISendGoodsBiz {

//    @Resource
//    private AliSendGoodsConfirmClient aliSendGoodsConfirmClient;
//
//    @Resource
//    private IOrderBiz orderBiz;
//
//    @Override
//    @Transactional
//    public DefaultResult<String> submitSendGoods(BaseAliSendGoodsDO aliSendGoodsDO) {
//        DefaultResult<String> result = new DefaultResult<String>();
//        OrderInfoDO orderInfo = orderBiz.queryOrderInfoByOrderId(aliSendGoodsDO.getOrderId());
//        checkParAliSendGoodsDO(aliSendGoodsDO, orderInfo, result);
//        if (result.isFailure()) {
//            return result;
//        }
//        BaseAliSendGoodsConfirmClientDO aliSendGoodsConfirmClientDO = new BaseAliSendGoodsConfirmClientDO();
//        aliSendGoodsConfirmClientDO.setTrade_no(StringUtils.getNotNullString(orderInfo.getOutTradeNo()));
//        aliSendGoodsConfirmClientDO.setInvoice_no(aliSendGoodsDO.getInvoice_no());
//        aliSendGoodsConfirmClientDO.setLogistics_name(aliSendGoodsDO.getLogistics_name());
//        aliSendGoodsConfirmClientDO.setTransport_type(aliSendGoodsDO.getTransport_type().getAlipayType());
////        Map<String, Object> map = aliSendGoodsConfirmClient.submitSendGoodsConfirm(aliSendGoodsConfirmClientDO);
////
////        if (!map.get("is_success").equals("T")) {
////            result.addErrorMsg("SUBMIT_SEND_GOODS_ERROR", "确认发货失败");
////            return result;
////        }
//
//        SubOrderResult<OrderInfoDO> operationResult = orderBiz.orderConfirmSendGoodsOperation(aliSendGoodsDO
//                .getOrderId(), null, aliSendGoodsDO.getTransport_type()
//                .getType(), aliSendGoodsDO.getLogistics_name(), aliSendGoodsDO.getInvoice_no(),aliSendGoodsDO.getRemark());
//        result.addErrors(operationResult.getErrorMessagesList());
//
//        result.setModule(operationResult.isSuccess() ? "true" : "false");
//
//        return result;
//    }
//
//    /**
//     * 检查参数合法性
//     * 
//     * @param aliSendGoodsDO
//     * @param orderInfo
//     * @param result
//     */
//    private void checkParAliSendGoodsDO(BaseAliSendGoodsDO aliSendGoodsDO, OrderInfoDO orderInfo,
//            DefaultResult<String> result) {
//        if (orderInfo == null) {
//            result.addErrorMsg("ORDER_NULL", "不存在此交易订单");
//        } else {
//            if (orderInfo.getOrderStatus() != OrderStatusEnum.PAID_UNSENDGOODS.getStatus().intValue()) {
//                result.addErrorMsg("ORDER_STATUS_ERROR", "亲~此订单已发货！");
//            }
//        }
//        if (aliSendGoodsDO.getTransport_type() == null) {
//            result.addErrorMsg("ORDER_LOGISTICS_TYPE_ERROR", "物流方式不能为空");
//        }
//        if (StringUtils.isEmptyWithTrim(aliSendGoodsDO.getLogistics_name())) {
//            result.addErrorMsg("ORDER_LOGISTICS_NAME_ERROR", "物流公司不能为空");
//        }
//        if (!LogisticsTypeEnum.DIRECT.equals(aliSendGoodsDO.getTransport_type())
//                && StringUtils.isEmptyWithTrim(aliSendGoodsDO.getInvoice_no())) {
//            result.addErrorMsg("ORDER_LOGISTICS_INVOICE_NO_NULL", "物流运单号不能为空");
//        }
//    }

}
