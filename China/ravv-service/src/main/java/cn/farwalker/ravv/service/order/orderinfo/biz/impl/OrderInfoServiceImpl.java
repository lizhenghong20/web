package cn.farwalker.ravv.service.order.orderinfo.biz.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import cn.farwalker.ravv.service.order.constants.PayStatusEnum;
import cn.farwalker.ravv.service.order.constants.PaymentPlatformEnum;
import cn.farwalker.ravv.service.payment.paymentlog.biz.IMemberPaymentLogBiz;
import cn.farwalker.ravv.service.payment.paymentlog.model.MemberPaymentLogBo;
import cn.farwalker.ravv.service.quartz.JobSchedulerFactory;
import cn.farwalker.ravv.service.quartz.UpdateOrderStatusTaskJob;
import cn.farwalker.ravv.service.shipstation.biz.IShipStationService;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import com.baomidou.mybatisplus.mapper.Condition;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

import cn.farwalker.ravv.service.order.operationlog.biz.IOrderLogService;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoBiz;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoService;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInventoryService;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderStatusEnum;
import cn.farwalker.ravv.service.order.orderinfo.dao.IOrderInfoDao;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.order.orderlogistics.biz.IOrderLogisticsBiz;
import cn.farwalker.ravv.service.order.orderlogistics.contants.LogisticsStatusEnum;
import cn.farwalker.ravv.service.order.orderlogistics.model.OrderLogisticsBo;
import cn.farwalker.ravv.service.order.paymemt.biz.IOrderPaymemtBiz;
import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;
import cn.farwalker.ravv.service.sys.message.biz.ISystemMessageService;
import cn.farwalker.ravv.service.sys.user.model.SysUserBo;
import cn.farwalker.waka.util.Tools;

/**
 * 订单信息<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
@Service
public class OrderInfoServiceImpl implements IOrderInfoService {
	private static final Logger log = LoggerFactory.getLogger(OrderInfoServiceImpl.class);

	@Resource
	private IOrderInfoBiz orderInfoBiz;
	@Resource
	private IOrderLogisticsBiz orderLogisticsBiz;

	@Resource
	private IOrderLogService orderLogService;

	@Resource
	private IOrderInventoryService orderInventoryService;

	@Resource
	private IOrderInfoDao orderInfoDao;

	@Resource
	private ISystemMessageService systemMessageService;

	@Resource
	private IOrderPaymemtBiz orderPaymemtBiz;

	@Autowired
	private IShipStationService iShipStationService;

	@Autowired
	private IMemberPaymentLogBiz iMemberPaymentLogBiz;

	@Override
	public OrderInfoBo updateCancelOrder(Long orderId) {
		OrderInfoBo bo = orderInfoBiz.selectById(orderId);
		OrderStatusEnum status = bo.getOrderStatus();
		if (status == OrderStatusEnum.REVIEWADOPT_UNPAID) {// || status == OrderStatusEnum.CREATED_UNREVIEW){
			orderLogService.createLog(orderId, "取消订单", "用户自己", "订单原状态:" + status.getLabel());
			orderInventoryService.updateOrderUnfreeze(orderId);

			bo.setOrderStatus(OrderStatusEnum.CANCEL);
			return bo;
		} else {
			throw new WakaException(status.getLabel() + "的状态不能取消订单");
		}
	}

	@Override
	public List<OrderInfoBo> getMyOrderList(Long buyerId, List<OrderStatusEnum> orderStatus, String search,
			Integer lastMonth, Boolean waitReview, Boolean afterSale, List<String> sortfield, Integer start,
			Integer size) {
		// OrderStatusEnum.CREATED_UNREVIEW,
		List<OrderStatusEnum> unpaidStatus = Arrays.asList(OrderStatusEnum.REVIEWADOPT_UNPAID);
		List<OrderStatusEnum> paidStatus = new ArrayList<>();
		for (OrderStatusEnum s : OrderStatusEnum.values()) {
			if (!unpaidStatus.contains(s)) {
				paidStatus.add(s);
			}
		}
		if (Tools.collection.isEmpty(orderStatus)) {
			orderStatus = null;
		}

		Date lastDate = null;
		int lastMoth = Tools.number.nullIf(lastMonth, 0);
		if (lastMoth > 0) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, 0 - lastMoth);
			lastDate = c.getTime();
		}
		if (Tools.collection.isEmpty(sortfield)) {
			sortfield = null;
		}
		if (Tools.number.nullIf(start, 0) <= 0) {
			start = Integer.valueOf(0);
		}
		if (Tools.number.nullIf(size, 0) == 0) {
			size = Integer.valueOf(20);
		}
		if (Tools.string.isEmpty(search)) {
			search = null;
		} else if (search.indexOf('%') == -1) { // 没有%符号
			search = "%" + search + "%";
		}

		if (waitReview == null) {
			waitReview = Boolean.FALSE;
		}
		if (afterSale == null) {
			afterSale = Boolean.FALSE;
		}

		List<OrderInfoBo> rds = orderInfoDao.getMyOrder(unpaidStatus, paidStatus, buyerId, lastDate, orderStatus,
				search, waitReview, afterSale, sortfield, start, size);
		return rds;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public OrderInfoBo updateOrderReceiver(Long orderId) {
		OrderStatusEnum type = OrderStatusEnum.SING_GOODS;
		OrderInfoBo bo = orderInfoBiz.selectById(orderId);
		if(bo == null){
			throw new WakaException("订单号不存在:" + orderId);
		}
		bo.setOrderStatus(type);
		orderInfoBiz.updateById(bo);

		Wrapper<OrderLogisticsBo> wp = new EntityWrapper<>();
		wp.eq(OrderLogisticsBo.Key.orderId.toString(), orderId);
		wp.last("limit 1");
		OrderLogisticsBo lgbo = orderLogisticsBiz.selectOne(wp);
		lgbo.setReceiverTime(new Date());
		orderLogisticsBiz.updateById(lgbo);

		orderLogService.createLog(orderId, "用户操作", "订单已收货", "用户操作了订单收货功能");

		Long memberId = bo.getBuyerId();
		// List<Long> ids = Collections.singletonList(orderId);
		systemMessageService.addOrderMessage(memberId, orderId, null, type, new Date());
		//使用taskjob30天后订单自动关闭
		callUpdateOrderStatus(orderId);
		return bo;
	}

	public void callUpdateOrderStatus(Long orderId){
		//目前写死30天
		long delay = 30 * 24 * 60 * 60 * 1000L;
		log.info("delay:{}", delay);
		JobDataMap paramMap = new JobDataMap();
		paramMap.put("orderId", String.valueOf(orderId));
		JobSchedulerFactory.callTaskJob(
				delay,
				UpdateOrderStatusTaskJob.class,
				"UpdateOrderStatusTaskJob",
				paramMap);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Boolean updateForWearhouseSendgoods(OrderLogisticsBo logisbo, Long userId, SysUserBo user) {
		// 仅仓管发货用
		logisbo.setLogisticsStatus(LogisticsStatusEnum.SendGoods);
		Boolean rs = orderLogisticsBiz.updateById(logisbo);
		if (!rs) {
			return rs;
		}
		//更新shipstation发货状态
//		if(!iShipStationService.orderShipped(logisbo.getOrderId()))
//			throw new WakaException(RavvExceptionEnum.INSERT_ERROR + "shipstation订单更新失败");
		Long logisticsId = logisbo.getId();
		// 拆单后会默认生成对应物流信息
		OrderLogisticsBo logistic = orderLogisticsBiz.selectById(logisticsId);
		if (logistic != null) {
			OrderStatusEnum type = OrderStatusEnum.SENDGOODS_UNCONFIRM;
			OrderInfoBo orderInfo = new OrderInfoBo();
			// 更新对应订单状态
			final Long orderId = logistic.getOrderId();
			orderInfo.setId(orderId);
			orderInfo.setOrderStatus(type);
			orderInfoBiz.updateById(orderInfo);
			// 添加订单消息
			OrderInfoBo orderVo = orderInfoBiz.selectById(orderId);
			Long memberId = orderVo.getBuyerId();
			// List<Long> orderIds = Collections.singletonList(orderId);
			systemMessageService.addOrderMessage(memberId, orderId, logisbo.getLogisticsNo(), type, new Date());
			// 操作日志
			String userName = "";
			if (user != null) {
				userName = user.getName();
			}
			orderLogService.createLog(logisbo.getOrderId(), userId, userName,
					OrderStatusEnum.SENDGOODS_UNCONFIRM.getLabel(), "订单发货。", true);
		}
		return rs;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Boolean updatePayment(OrderPaymemtBo paybo, Long userId, SysUserBo user) {
		OrderPaymemtBo bo = orderPaymemtBiz.selectById(paybo.getId());
		// 修改调整金额后的一系列改动
		bo.setAdjustFee(paybo.getAdjustFee());
		bo.setShouldPayTotalFee(OrderPaymemtBo.getPayTotalFee(bo));

		Boolean rs = orderPaymemtBiz.updateById(bo);
		if (rs) {
			// 操作日志
			String userName = "";
			if (user != null) {
				userName = user.getName();
			}
			orderLogService.createLog(bo.getOrderId(), userId, userName, "修改", "修改订单支付信息。", true);
		}
		return rs;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Boolean updateOrderLogistics(OrderLogisticsBo logisbo, Long userId, SysUserBo user) {
		Long logisticsId = logisbo.getId();
		// 拆单后会默认生成对应物流信息
		OrderLogisticsBo oldLogistic = orderLogisticsBiz.selectById(logisticsId);
		if (oldLogistic != null && logisbo.getLogisticsStatus() == LogisticsStatusEnum.SendGoods) {
			OrderStatusEnum type = OrderStatusEnum.SENDGOODS_UNCONFIRM;
			OrderInfoBo orderInfo = new OrderInfoBo();
			// 更新对应订单状态
			Long orderId = oldLogistic.getOrderId();
			orderInfo.setId(orderId);
			orderInfo.setOrderStatus(type);
			// TODO 发货操作需要写订单操作日志
			orderInfoBiz.updateById(orderInfo);

			// 添加订单消息
			OrderInfoBo orderVo = orderInfoBiz.selectById(orderId);
			Long memberId = orderVo.getBuyerId();
			// List<Long> orderIds = Collections.singletonList(orderId);
			systemMessageService.addOrderMessage(memberId, orderId, logisbo.getLogisticsNo(), type, new Date());
		}
		Boolean rs = orderLogisticsBiz.updateById(logisbo);
		// 操作日志
		String userName = "";
		if (user != null) {
			userName = user.getName();
		}
		orderLogService.createLog(logisbo.getOrderId(), userId, userName, "修改物流信息", "修改订单发货的物流信息。", true);
		return rs;
	}


	/**
	 * 是否支付成功
	 * @param session
	 * @param orderId
	 * @return
	 */
	@Override
	public Boolean isPaySuccess(HttpSession session,Long orderId){
		Long memberId;
		if (session.getAttribute("memberId") == null) {
			throw new WakaException(RavvExceptionEnum.TOKEN_PARSE_MEMBER_ID_FAILED);
		}
		memberId = (Long)session.getAttribute("memberId");
		MemberPaymentLogBo query =  iMemberPaymentLogBiz.selectOne(Condition.create().eq(MemberPaymentLogBo.Key.memberId.toString(),memberId)
				.eq(MemberPaymentLogBo.Key.orderId.toString(),orderId)
				.eq(MemberPaymentLogBo.Key.status.toString(), PayStatusEnum.PAID.toString()));
		return query != null;
	}


	/**
	 * 从订单获取支付类型，是paypal 支付，还是wallet支付 null代表查不到信息
	 * @return
	 */
	public PaymentPlatformEnum getPayType(Long orderId){
		if(orderId == null || orderId == 0){
			return null;
		}
		MemberPaymentLogBo query =  iMemberPaymentLogBiz.selectOne(Condition.create().eq(MemberPaymentLogBo.Key.orderId.toString(),orderId));
		if(query != null){
			return query.getPayType();
		}
		return null;
	}





}