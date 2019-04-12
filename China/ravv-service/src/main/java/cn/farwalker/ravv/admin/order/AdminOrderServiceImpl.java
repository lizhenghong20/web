package cn.farwalker.ravv.admin.order;

import cn.farwalker.ravv.service.base.area.biz.IBaseAreaBiz;
import cn.farwalker.ravv.service.base.area.model.BaseAreaBo;
import cn.farwalker.ravv.service.base.storehouse.biz.IStorehouseService;
import cn.farwalker.ravv.service.order.operationlog.biz.IOrderLogService;
import cn.farwalker.ravv.service.order.operationlog.biz.IOrderOperationLogBiz;
import cn.farwalker.ravv.service.order.operationlog.model.OrderOperationLogBo;
import cn.farwalker.ravv.service.order.ordergoods.biz.IOrderGoodsBiz;
import cn.farwalker.ravv.service.order.ordergoods.biz.IOrderGoodsService;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoBiz;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoService;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderStatusEnum;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.order.orderlogistics.biz.IOrderLogisticsBiz;
import cn.farwalker.ravv.service.order.orderlogistics.contants.LogisticsPaymentEnum;
import cn.farwalker.ravv.service.order.orderlogistics.contants.LogisticsStatusEnum;
import cn.farwalker.ravv.service.order.orderlogistics.contants.LogisticsTypeEnum;
import cn.farwalker.ravv.service.order.orderlogistics.model.OrderLogisticsBo;
import cn.farwalker.ravv.service.order.orderlogistics.model.OrderLogisticsVo;
import cn.farwalker.ravv.service.order.paymemt.biz.IOrderPaymemtBiz;
import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnLogBiz;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsBiz;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsDetailBiz;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsService;
import cn.farwalker.ravv.service.order.returns.constants.OperatorTypeEnum;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsGoodsStatusEnum;
import cn.farwalker.ravv.service.order.returns.model.*;
import cn.farwalker.ravv.service.quartz.JobSchedulerFactory;
import cn.farwalker.ravv.service.quartz.UpdateOrderStatusTaskJob;
import cn.farwalker.ravv.service.sys.message.biz.ISystemMessageService;
import cn.farwalker.ravv.service.sys.user.biz.ISysUserBiz;
import cn.farwalker.ravv.service.sys.user.model.SysUserBo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.core.base.controller.ControllerUtils;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.web.crud.QueryFilter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AdminOrderServiceImpl implements AdminOrderService {

    @Resource
    private IOrderGoodsBiz orderGoodsBiz;

    protected IOrderGoodsBiz getBiz(){
        return orderGoodsBiz;
    }

    @Resource
    private IOrderOperationLogBiz orderOperationLogBiz;

    protected IOrderOperationLogBiz getOrderOperationLogBiz(){
        return orderOperationLogBiz;
    }

    @Resource
    private IStorehouseService storeHouseService;

    @Resource
    private IOrderPaymemtBiz orderPaymemtBiz;

    @Resource
    private IOrderGoodsService orderGoodsService;

    @Resource
    private IOrderInfoBiz orderInfoBiz;

    @Resource
    private IOrderInfoService orderInfoService;

    @Resource
    private IOrderLogisticsBiz orderLogisticsBiz;

    @Resource
    private IOrderLogService orderLogService;

    @Resource
    private ISysUserBiz sysUserBiz;

    @Resource
    private IOrderReturnsBiz orderReturnsBiz;

    @Resource
    private IOrderReturnsService orderReturnsService;

    @Resource
    private IOrderReturnsDetailBiz orderReturnsDetailBiz;

    protected IOrderPaymemtBiz getOrderPaymemtBiz(){
        return orderPaymemtBiz;
    }

    @Resource
    private IBaseAreaBiz baseAreaBiz;

    protected IOrderReturnsBiz getOrderReturnsBiz() {
        return orderReturnsBiz;
    }

    @Resource
    private ISystemMessageService systemMessageService;

    protected IOrderInfoBiz getOrderInfoBiz() {
        return orderInfoBiz;
    }

    protected IOrderReturnsDetailBiz getReturnsDetailBiz(){
        return orderReturnsDetailBiz;
    }

    @Resource
    private IOrderReturnLogBiz orderReturnLogBiz;

    protected IOrderReturnLogBiz getReturnLogBiz(){
        return orderReturnLogBiz;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean createOrderGoods(OrderGoodsBo vo) {
        Boolean rs = getBiz().insert(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean deleteOrderGoods(Long id) {
        Boolean rs = getBiz().deleteById(id);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        return rs;
    }

    @Override
    public OrderGoodsBo getOneOrderGoods(Long id) {
        OrderGoodsBo rs = getBiz().selectById(id);
        if(rs == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        return rs;
    }

    @Override
    public Page<OrderGoodsBo> getListOrderGoods(List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        Page<OrderGoodsBo> page = ControllerUtils.getPage(start,size,sortfield);
        Wrapper<OrderGoodsBo> wrap = ControllerUtils.getWrapper(query);
        Page<OrderGoodsBo> rs = getBiz().selectPage(page,wrap);
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean updateOrderGoods(OrderGoodsBo vo) {
        Boolean rs = getBiz().updateById(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean createOrderPayment(OrderPaymemtBo vo) {
        Boolean rs = getOrderPaymemtBiz().insert(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean deleteOrderPayment(Long id) {
        Boolean rs = getOrderPaymemtBiz().deleteById(id);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        return rs;
    }

    @Override
    public OrderPaymemtBo getOneOrderPayment(Long id) {
        OrderPaymemtBo rs = getOrderPaymemtBiz().selectById(id);
        if(rs == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        return rs;
    }

    @Override
    public Page<OrderPaymemtBo> getListOrderPayment(List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        Page<OrderPaymemtBo> page = ControllerUtils.getPage(start,size,sortfield);
        Wrapper<OrderPaymemtBo> wrap = ControllerUtils.getWrapper(query);
        Page<OrderPaymemtBo> rs = getOrderPaymemtBiz().selectPage(page,wrap);
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public OrderReturnsBo createReturnGoods(Long returnsid, Boolean state) {
        OrderReturnsBo rs = null;
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean deleteReturnGoods(Long id) {
        Boolean rs = getOrderReturnsBiz().deleteById(id);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        return rs;
    }

    @Override
    public OrderReturnsBo getOneReturnGoods(Long id) {
        OrderReturnsBo rs = getOrderReturnsBiz().selectById(id);
        if(rs == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        return rs;
    }

    @Override
    public Page<OrderReturnsBo> getListReturnGoods(List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        Page<OrderReturnsBo> page = ControllerUtils.getPage(start, size, sortfield);
        Wrapper<OrderReturnsBo> wrap = ControllerUtils.getWrapper(query);
        wrap.orderBy(OrderReturnsBo.Key.gmtCreate.toString(), false);
        Page<OrderReturnsBo> rs = getOrderReturnsBiz().selectPage(page, wrap);
        return rs;
    }

    @Override
    public JsonResult<Boolean> warehouseOperateRefund(OrderReturnsVo vo, Long userId) {
        OrderReturnsBo bo = orderReturnsBiz.selectById(vo.getId());
        if (bo == null) {
            throw new WakaException("对应退货单不存在");
        }
        // 判断对应商品的是否已验收
        if(vo.getStatus() == ReturnsGoodsStatusEnum.exchangeAcceptanceReceived){
            OrderReturnsDetailBo query = new OrderReturnsDetailBo();
            query.setReturnsId(vo.getId());
            EntityWrapper<OrderReturnsDetailBo> wp = new EntityWrapper<OrderReturnsDetailBo>(query);
            wp.isNull(OrderReturnsDetailBo.Key.validesc.toString()).or(OrderReturnsDetailBo.Key.validesc+" ={0}", "");
            Integer unCheckCount = orderReturnsDetailBiz.selectCount(wp);
            if (!Tools.number.isEmpty(unCheckCount)) {
                throw new WakaException("存在未验收退品");
            }
        }
        // 操作修改对应消息并添加操作记录
        vo.setOrderId(bo.getOrderId());
        return orderReturnsService.saveOperateOrderReturnsAndLog(userId, OperatorTypeEnum.wearhouse, vo,
                vo.getStatus());
    }

    @Override
    public Boolean checkAndAcceptanceGoods(OrderReturnsVo vo, Long userId) {
        OrderReturnsBo bo = orderReturnsBiz.selectById(vo.getId());
        if (bo == null) {
            throw new WakaException("对应退货单不存在");
        }
        // 操作修改对应消息并添加操作记录
        vo.setOrderId(bo.getOrderId());
        orderReturnsService.saveCheckAndAcceptanceGoods(userId, OperatorTypeEnum.wearhouse, vo);
        return true;
    }

    @Override
    public List<Map<String, String>> getReturnsGoodsStatus(Long returnsid) {
        OrderReturnsBo bo = orderReturnsBiz.selectById(returnsid);
        List<Map<String, String>> rs = null;
        if (bo != null) {
            rs = orderReturnsService.getReturnsStatusByStatusForWarehouse(bo.getStatus());
            return rs;
        }
        return rs;
    }

    @Override
    public OrderLogisticsBo getReturnGoodsLogisticsInfo(Long returnsId, Long orderId) {
        OrderLogisticsBo query = new OrderLogisticsBo();
        query.setReturnsId(returnsId);
        OrderLogisticsBo bo = orderLogisticsBiz.selectOne(new EntityWrapper<OrderLogisticsBo>(query));
        if (bo == null) {
            query.setReturnsId(0l);
            query.setOrderId(orderId);
            OrderLogisticsBo orderRL = orderLogisticsBiz.selectOne(new EntityWrapper<OrderLogisticsBo>(query));
            if (orderRL != null) {
                bo = new OrderLogisticsBo();
                bo.setOrderId(orderId);
                bo.setReturnsId(returnsId);
                bo.setLogisticsPayment(LogisticsPaymentEnum.SELLER);
                bo.setLogisticsStatus(LogisticsStatusEnum.NONE);
                bo.setLogisticsType(LogisticsTypeEnum.LandCarriage);
                bo.setReceiverArea(orderRL.getReceiverArea());
                bo.setReceiverCity(orderRL.getReceiverCity());
                bo.setReceiverProvince(orderRL.getReceiverProvince());
                bo.setReceiverFullname(orderRL.getReceiverFullname());
                bo.setReceiverDetailAddress(orderRL.getReceiverDetailAddress());
                bo.setReceiverMobile(orderRL.getReceiverMobile());
                bo.setReceiverPostCode(orderRL.getReceiverPostCode());
                bo.setCountryCode(orderRL.getCountryCode());
                bo.setReceiverAreaId(orderRL.getReceiverAreaId());
            }
        }
        OrderLogisticsVo rs = Tools.bean.cloneBean(bo, new OrderLogisticsVo());
        // 通过子地址areaId获取地址全路径(广东id/珠海id)
        if (rs.getReceiverAreaId() != null) {
            Long currentAreaId = rs.getReceiverAreaId();
            String areaFullPath = currentAreaId.toString();
            while (true) {
                BaseAreaBo area = baseAreaBiz.selectById(currentAreaId);
                if (area != null && !Tools.number.isEmpty(area.getPid())) {
                    currentAreaId = area.getPid();
                    areaFullPath = area.getPid().toString() + "/" + areaFullPath;
                } else {
                    break;
                }
            }
            rs.setAreaFullPath(areaFullPath);
        }
        return rs;
    }

    @Override
    public Boolean doReturnGoodsSendGoods(OrderLogisticsBo logisbo, Long returnsId, Long userId) {
        Boolean rs = orderReturnsService.updateStatusAndSendGoods(logisbo, userId, returnsId);
        return rs;
    }

    @Override
    public List<OrderReturnsDetailVo> getReturnsDetail(Long returnsId) {
        List<OrderReturnsDetailVo> rs = orderReturnsService.getOrderReturnsDetailList(returnsId);
        return rs;
    }

    @Override
    public Page<OrderReturnsBo> getMerchantReturns(Long merchantId, List<QueryFilter> query, Integer start,
                                                   Integer size, String sortfield) {
        Page<OrderReturnsBo> page = ControllerUtils.getPage(start, size, sortfield);
        Wrapper<OrderReturnsBo> wrap = ControllerUtils.getWrapper(query);
        wrap.orderBy(OrderReturnsBo.Key.gmtCreate.toString(), false);

        String sql = "order_id in (SELECT order_id FROM order_goods WHERE goods_id in "
                + "(SELECT id FROM goods WHERE merchant_id = '" + merchantId + "'))";

        wrap.where(sql);

        Page<OrderReturnsBo> rs = getOrderReturnsBiz().selectPage(page, wrap);
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean createReturnLog(OrderReturnLogBo vo) {
        Boolean rs = getReturnLogBiz().insert(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean deleteReturnLog(Long id) {
        Boolean rs = getReturnLogBiz().deleteById(id);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        return rs;
    }

    @Override
    public OrderReturnLogBo getOneReturnLog(Long id) {
        OrderReturnLogBo rs = getReturnLogBiz().selectById(id);
        if(rs == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        return rs;
    }

    @Override
    public Page<OrderReturnLogBo> getListReturnLog(List<QueryFilter> query, Integer start, Integer size,
                                                   String sortfield, Long returnsId) {
        Page<OrderReturnLogBo> page =ControllerUtils.getPage(start,size,sortfield);
        Wrapper<OrderReturnLogBo> wrap =ControllerUtils.getWrapper(query);
        wrap.eq(OrderReturnLogBo.Key.returnId.toString(), returnsId);
        wrap.orderBy(OrderReturnLogBo.Key.operationTime.toString(), false);
        wrap.orderBy(OrderReturnLogBo.Key.id.toString(), false);
        Page<OrderReturnLogBo> rs = getReturnLogBiz().selectPage(page,wrap);
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean updateReturnLog(OrderReturnLogBo vo) {
        Boolean rs = getReturnLogBiz().updateById(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean createReturnDetail(OrderReturnsDetailBo vo) {
        Boolean rs = getReturnsDetailBiz().insert(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean deleteReturnDetail(Long id) {
        Boolean rs = getReturnsDetailBiz().deleteById(id);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        return rs;
    }

    @Override
    public OrderReturnsDetailBo getOneReturnDetail(Long id) {
        OrderReturnsDetailBo rs = getReturnsDetailBiz().selectById(id);
        //需返回对应sku商品信息
        if(rs == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        return rs;
    }

    @Override
    public Page<OrderReturnsDetailBo> getListReturnDetail(List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        Page<OrderReturnsDetailBo> page =ControllerUtils.getPage(start,size,sortfield);
        Wrapper<OrderReturnsDetailBo> wrap =ControllerUtils.getWrapper(query);
        Page<OrderReturnsDetailBo> rs = getReturnsDetailBiz().selectPage(page,wrap);
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean updateReturnDetail(OrderReturnsDetailBo vo) {
        Boolean rs = getReturnsDetailBiz().updateById(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }

    @Override
    public JsonResult<Boolean> doReundPay(OrderReturnsBo returnsBo, Long userId) {
        OrderReturnsBo bo = orderReturnsBiz.selectById(returnsBo.getId());
        if (bo == null) {
            throw new WakaException("对应退货单不存在");
        }
        bo.setAdjustFee(returnsBo.getAdjustFee());//实退金额
        OrderReturnsVo vo = Tools.bean.cloneBean(bo, new OrderReturnsVo());
        // 操作修改对应消息并添加操作记录
        return orderReturnsService.saveOperateOrderReturnsAndLog(userId, OperatorTypeEnum.server, vo,
                ReturnsGoodsStatusEnum.refundSucess);
    }

    @Override
    public JsonResult<Boolean> serviceOperateRefund(OrderReturnsVo vo, Long userId) {
        OrderReturnsBo bo = orderReturnsBiz.selectById(vo.getId());
        if (bo == null) {
            throw new WakaException("对应退货单不存在");
        }
        vo.setOrderId(bo.getOrderId());
        vo.setReturnsType(bo.getReturnsType());
        // 操作修改对应消息并添加操作记录
        return orderReturnsService.saveOperateOrderReturnsAndLog(userId, OperatorTypeEnum.server, vo,vo.getStatus());
    }

    @Override
    public List<Map<String, String>> getReturnsStatus(Long returnsid) {
        OrderReturnsBo bo = orderReturnsBiz.selectById(returnsid);
        List<Map<String, String>> rs = null;
        if (bo != null) {
            rs = orderReturnsService.getReturnsStatusByStatusForService(bo.getStatus());
            return rs;
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean deleteOrderInfo(Long id) {
        Boolean rs = getOrderInfoBiz().deleteById(id);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        return rs;
    }

    @Override
    public OrderInfoBo getOneOrderInfo(Long id) {
        OrderInfoBo rs = getOrderInfoBiz().selectById(id);
        if(rs == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        return rs;
    }

    @Override
    public Page<OrderInfoBo> getListOrderInfo(Long userid, List<QueryFilter> query, Integer start,
                                              Integer size, String sortfield) {
        List<Long> storeIds = null;
        if (userid.longValue() == -1) {
            // 不过滤
        } else {
            storeIds = storeHouseService.getStoreIds(userid);
            if (Tools.collection.isEmpty(storeIds)) {
                throw new WakaException("当前用户不能操作仓库订单");
            }
        }

        // createMethodSinge创建方法
        Page<OrderInfoBo> page = ControllerUtils.getPage(start, size, sortfield);
        Wrapper<OrderInfoBo> wrap = ControllerUtils.getWrapper(query);
        if (wrap == null) {
            wrap = new EntityWrapper<>();
        }

        if (userid.longValue() > 0 && Tools.collection.isNotEmpty(storeIds)) {
            wrap.in(OrderInfoBo.Key.storehouseId.toString(), storeIds);
        }
        wrap.orderBy(OrderInfoBo.Key.gmtModified.toString(), false);
        Page<OrderInfoBo> rs = getOrderInfoBiz().selectPage(page, wrap);
        return rs;
    }

    @Override
    public OrderLogisticsBo getOrderInfoLogisticsInfo(Long orderId) {
        OrderLogisticsBo query = new OrderLogisticsBo();
        query.setOrderId(orderId);
        // 退货id，默认值为0，如果不是0表示退货
        query.setReturnsId(0l);
        OrderLogisticsBo rs = orderLogisticsBiz.selectOne(new EntityWrapper<OrderLogisticsBo>(query));
        return rs;
    }

    @Override
    public OrderLogisticsBo getlogisticsByReturnsId(Long returnsId) {
        OrderLogisticsBo query = new OrderLogisticsBo();
        query.setReturnsId(returnsId);
        OrderLogisticsBo rs = orderLogisticsBiz.selectOne(new EntityWrapper<OrderLogisticsBo>(query));
        return rs;
    }

    @Override
    public Boolean doOrderInfoSendGoods(OrderLogisticsBo logisbo, Long userId) {
        Long orderId = logisbo.getOrderId();
        OrderStatusEnum type = OrderStatusEnum.SENDGOODS_UNCONFIRM;
        Boolean rs = orderLogisticsBiz.insert(logisbo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        // 更新订单状态
        OrderInfoBo order = new OrderInfoBo();
        order.setId(orderId);
        order.setOrderStatus(type);
        this.getOrderInfoBiz().updateById(order);

        //使用taskjob30天后订单自动关闭
        orderInfoService.callUpdateOrderStatus(logisbo.getOrderId());

        // 添加订单消息
        OrderInfoBo orderVo = orderInfoBiz.selectById(orderId);
        Long memberId = orderVo.getBuyerId();
        // List<Long> orderIds = Collections.singletonList(orderId);
        systemMessageService.addOrderMessage(memberId, orderId, logisbo.getLogisticsNo(), type, new Date());

        log.debug(order.getOrderStatus() + "orderId:" + order.getId());
        SysUserBo user = sysUserBiz.selectById(userId);
        String userName = "";
        if (user != null) {
            userName = user.getName();
        }
        orderLogService.createLog(logisbo.getOrderId(), userId, userName, "发货", "订单发货。", true);
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean updateOrderLogistics(OrderLogisticsBo logisbo, Long userId) {
        // TODO 物流信息不能随便修改
        SysUserBo user = sysUserBiz.selectById(userId);
        Boolean rs = orderInfoService.updateOrderLogistics(logisbo, userId, user);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }

    @Override
    public Boolean wearhouseSendgoods(OrderLogisticsBo logisbo, Long userId) {
        SysUserBo user = sysUserBiz.selectById(userId);
        Boolean rs = orderInfoService.updateForWearhouseSendgoods(logisbo, userId, user);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean updateOrderInfo(OrderInfoBo vo, Long userId) {
        OrderInfoBo v2 = getOrderInfoBiz().selectById(vo.getId());
        getOrderInfoBiz().updateById(v2);
        Boolean rs = getOrderInfoBiz().updateById(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        // 操作日志
        SysUserBo user = sysUserBiz.selectById(userId);
        if(user == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        String userName = "";
        if (user != null) {
            userName = user.getName();
        }
        orderLogService.createLog(vo.getId(), userId, userName, "修改", "修改订单信息。", true);
        return rs;
    }

    @Override
    public List<OrderGoodsBo> getOrderGoods(Long orderId) {
        OrderGoodsBo query = new OrderGoodsBo();
        query.setOrderId(orderId);
        List<OrderGoodsBo> rs = orderGoodsBiz.selectList(new EntityWrapper<OrderGoodsBo>(query));

        for (OrderGoodsBo bo : rs) {
            // 去图片相对路径
            bo.setImgMajor(QiniuUtil.getFullPath(bo.getImgMajor()));
        }
        return rs;
    }

    @Override
    public Boolean doUpdateGoods(OrderGoodsBo goods) {
        Boolean rs = null;
        return true;
    }

    @Override
    public OrderPaymemtBo getPayment(Long orderid) {
        OrderPaymemtBo query = new OrderPaymemtBo();
        query.setOrderId(orderid);
        EntityWrapper<OrderPaymemtBo> wp = new EntityWrapper<OrderPaymemtBo>(query);
        wp.last("LIMIT 1");
        OrderPaymemtBo rs = orderPaymemtBiz.selectOne(wp);
        if(rs == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        return rs;
    }

    @Override
    public Boolean doUpdatePayment(OrderPaymemtBo paybo, Long userId) {
        SysUserBo user = sysUserBiz.selectById(userId);
        Boolean rs = orderInfoService.updatePayment(paybo, userId, user);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }

    @Override
    public Boolean doUpdateOrderGoods(Long orderId, List<OrderGoodsBo> orderGoods, Long userId) {
        for (OrderGoodsBo bo : orderGoods) {
            // 去图片相对路径
            bo.setImgMajor(QiniuUtil.getRelativePath(bo.getImgMajor()));
        }
        orderGoodsService.updateOrderGoods(orderId, orderGoods);
        // 操作日志
        SysUserBo user = sysUserBiz.selectById(userId);
        String userName = "";
        if (user != null) {
            userName = user.getName();
        }
        orderLogService.createLog(orderId, userId, userName, "修改", "修改商品信息。", true);
        return true;
    }

    @Override
    public Page<OrderInfoBo> getMerchantOrderInfo(Long merchantId, List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        Page<OrderInfoBo> page = ControllerUtils.getPage(start, size, sortfield);
        Wrapper<OrderInfoBo> wrap = ControllerUtils.getWrapper(query);
        if (wrap == null) {
            wrap = new EntityWrapper<>();
        }

        String sql = "id in (select o.id from order_info o inner join order_paymemt p on o.id = p.order_id where (p.pay_status ='PAID' or p.pay_status ='REFUND') "
                + "and exists (select og.id from order_goods og inner join goods g on g.id = og.goods_id where o.id = og.order_id  and g.merchant_id = '"
                + merchantId + "'))";

        wrap.where(sql);

        wrap.orderBy(OrderInfoBo.Key.gmtModified.toString(), false);
        Page<OrderInfoBo> rs = getOrderInfoBiz().selectPage(page, wrap);
        if(rs == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean createOrderOperationLog(OrderOperationLogBo vo) {
        Boolean rs = getOrderOperationLogBiz().insert(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean deleteOrderOperationLog(Long id) {
        Boolean rs = getOrderOperationLogBiz().deleteById(id);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        return rs;
    }

    @Override
    public OrderOperationLogBo getOneOrderOperationLog(Long id) {
        OrderOperationLogBo rs = getOrderOperationLogBiz().selectById(id);
        if(rs == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        return rs;
    }

    @Override
    public Page<OrderOperationLogBo> getListOrderOperationLog(List<QueryFilter> query, Integer start, Integer size, String sortfield, Long orderId) {
        Page<OrderOperationLogBo> page =ControllerUtils.getPage(start,size,sortfield);
        Wrapper<OrderOperationLogBo> wrap =ControllerUtils.getWrapper(query);
        if(orderId != null) {
            wrap.eq(OrderOperationLogBo.Key.orderId.toString(), orderId);
        }
        wrap.orderBy(OrderOperationLogBo.Key.gmtCreate.toString(), false);
        Page<OrderOperationLogBo> rs = getOrderOperationLogBiz().selectPage(page,wrap);
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean updateOrderOperationLog(OrderOperationLogBo vo) {
        Boolean rs = getOrderOperationLogBiz().updateById(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }
}
