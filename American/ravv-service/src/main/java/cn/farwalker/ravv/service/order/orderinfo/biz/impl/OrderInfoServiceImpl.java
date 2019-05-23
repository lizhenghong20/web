package cn.farwalker.ravv.service.order.orderinfo.biz.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import cn.farwalker.ravv.service.goods.base.biz.IGoodsBiz;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.goods.constants.GoodsStatusEnum;
import cn.farwalker.ravv.service.goodssku.skudef.biz.IGoodsSkuDefBiz;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuDefBo;
import cn.farwalker.ravv.service.member.address.biz.IMemberAddressBiz;
import cn.farwalker.ravv.service.member.address.model.MemberAddressBo;
import cn.farwalker.ravv.service.order.constants.PayStatusEnum;
import cn.farwalker.ravv.service.order.constants.PaymentPlatformEnum;
import cn.farwalker.ravv.service.order.ordergoods.biz.IOrderGoodsBiz;
import cn.farwalker.ravv.service.order.ordergoods.dao.IOrderGoodsDao;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderCreateService;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderTypeEnum;
import cn.farwalker.ravv.service.order.orderinfo.model.*;
import cn.farwalker.ravv.service.payment.paymentlog.biz.IMemberPaymentLogBiz;
import cn.farwalker.ravv.service.payment.paymentlog.model.MemberPaymentLogBo;
import cn.farwalker.ravv.service.quartz.JobSchedulerFactory;
import cn.farwalker.ravv.service.quartz.UpdateOrderStatusTaskJob;
import cn.farwalker.ravv.service.shipstation.biz.IShipStationService;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.plugins.Page;
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

	@Autowired
	private IOrderGoodsBiz iOrderGoodsBiz;

	@Autowired
	private IMemberAddressBiz iMemberAddressBiz;

	@Autowired
	private IOrderCreateService iOrderCreateService;

	@Autowired
	private IGoodsBiz iGoodsBiz;

	@Autowired
	private IGoodsSkuDefBiz iGoodsSkuDefBiz;

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
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Page<OrderInfoBo> getMyOrderList(Long buyerId, List<OrderStatusEnum> orderStatus, String search,
			Integer lastMonth, Boolean waitReview, Boolean afterSale, List<String> sortfield, Integer start,
			Integer size) {
		Page<OrderInfoBo> page = new Page<>(start, size);
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

		List<OrderInfoBo> rds = orderInfoDao.getMyOrder(page,unpaidStatus, paidStatus, buyerId, lastDate, orderStatus,
				search, waitReview, afterSale, sortfield);

		//对未支付订单做失效判断（计算出的应付价格等与数据库存储不符即为失效）
		rds = doInvalidOrder(rds);

		page.setRecords(rds);
		return page;
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

	/**
	 * @Author Mr.Simple
	 * @Description 判断订单中是否存在失效订单并更改状态
	 * @Date 13:48 2019/5/23
	 * @Param
	 * @return
	 **/
	private List<OrderInfoBo> doInvalidOrder(List<OrderInfoBo> orderInfoBoList){
		Long memberId = orderInfoBoList.get(0).getBuyerId();
		List<OrderInfoBo> allList = new ArrayList<>();
		//判断订单是否支付
		for (OrderInfoBo item : orderInfoBoList) {
			//如果未支付，判断该订单是否失效
			if(OrderStatusEnum.REVIEWADOPT_UNPAID.getKey().equals(item.getOrderStatus().getKey())){
				//如果订单未失效，根据orderId查出订单下的所有商品，重新执行一遍confirm接口计算金额
				//判断该订单有没有子单，分别查出OrderGoodsSkuVo属性值
				List<Long> orderIdList = new ArrayList<>();
				if(OrderTypeEnum.SINGLE.getKey().equals(item.getOrderType().getKey())){
					orderIdList.add(item.getId());
				} else if(OrderTypeEnum.MASTER.getKey().equals(item.getOrderType().getKey())){
					//根据主单id查询出所有子单
					List<OrderInfoBo> orderList = orderInfoBiz.selectList(Condition.create()
							.eq(OrderInfoBo.Key.pid.toString(), item.getId()));
					List<Long> childOrderList = orderList.stream().map(OrderInfoBo::getId).collect(Collectors.toList());
					orderIdList.addAll(childOrderList);
				}
				//根据orderId查出订单所有商品
				List<OrderGoodsSkuVo> valueids = getValueidsByOrderId(orderIdList);
				//判断valueids是否为空，为空说明商品失效
				if(valueids == null){
					//执行订单失效逻辑
					allList.add(changeInvalidStatus(item.getId()));
					continue;
				}

				ConfirmVo confirmVo = getAddressId(item.getId(), memberId);
				if(confirmVo == null){
					//confirmVo为空说明收货地址改变，执行订单失效逻辑
					allList.add(changeInvalidStatus(item.getId()));
					continue;
				}

				//不为空则重新计算所有金额
				JsonResult<List<ConfirmOrderVo>> result =
						iOrderCreateService.calTotal(valueids, confirmVo.getAddressId(), confirmVo.getShipmentId());
				//与数据库存储的金额对比（orderpayment表）
				if(!compareOrderPament(result, item.getId())){
					//执行订单失效逻辑
					allList.add(changeInvalidStatus(item.getId()));
					continue;
				}
			}
			allList.add(item);
		}
		return allList;
	}

	/**
	 * @Author Mr.Simple
	 * @Description 获得订单所有商品
	 * @Date 13:48 2019/5/23
	 * @Param
	 * @return
	 **/
	private List<OrderGoodsSkuVo> getValueidsByOrderId(List<Long> orderIdList){
		List<OrderGoodsSkuVo> valueids = new ArrayList<>();
		for (Long item : orderIdList) {
			List<OrderGoodsBo> orderGoodsBos = iOrderGoodsBiz.selectList(Condition.create()
					.eq(OrderGoodsBo.Key.orderId.toString(), item));
			if(orderGoodsBos == null){
				throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
			}
			for (OrderGoodsBo list : orderGoodsBos) {
				//查看goodsid,skuid是否存在,不存在直接return null
				if(!checkGoods(list)){
					return null;
				}
				OrderGoodsSkuVo orderGoodsSkuVo = new OrderReturnSkuVo();
				orderGoodsSkuVo.setGoodsId(list.getGoodsId());
				orderGoodsSkuVo.setSkuId(list.getSkuId());
				orderGoodsSkuVo.setQuan(list.getQuantity());
				valueids.add(orderGoodsSkuVo);
			}
		}

		return valueids;
	}

	private boolean checkGoods(OrderGoodsBo orderGoodsBo){
		//验证商品是否失效
		GoodsBo goodsBo = iGoodsBiz.selectById(orderGoodsBo.getGoodsId());
		if(goodsBo == null || !GoodsStatusEnum.ONLINE.getKey().equals(goodsBo.getGoodsStatus().getKey())){
			return false;
		}
		//再查找sku
		GoodsSkuDefBo skuDefBo = iGoodsSkuDefBiz.selectById(orderGoodsBo.getSkuId());
		if(skuDefBo == null){
			return false;
		}
		return true;
	}

	/**
	 * @Author Mr.Simple
	 * @Description 获取该订单下单时的地址id和shipmentId
	 * @Date 13:48 2019/5/23
	 * @Param
	 * @return
	 **/
	private ConfirmVo getAddressId(Long orderId, Long memberId){
		ConfirmVo confirmVo = new ConfirmVo();
		//通过orderId查询出该订单填写的物流地址（查询order_logistics表）
		OrderLogisticsBo orderLogisticsBo = orderLogisticsBiz.selectOne(Condition.create()
													.eq(OrderLogisticsBo.Key.orderId.toString(), orderId));
		if(orderLogisticsBo == null){
			throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
		}
		List<MemberAddressBo> addressList = iMemberAddressBiz.selectList(Condition.create()
									.eq(MemberAddressBo.Key.areaId.toString(), orderLogisticsBo.getReceiverAreaId())
									.eq(MemberAddressBo.Key.address.toString(), orderLogisticsBo.getReceiverDetailAddress())
									.eq(MemberAddressBo.Key.memberId.toString(), memberId));
		if(addressList.size() == 0){
			return null;
		}
		confirmVo.setAddressId(addressList.get(0).getId());
		confirmVo.setShipmentId(orderLogisticsBo.getShipmentId());
		return confirmVo;
	}

	/**
	 * @Author Mr.Simple
	 * @Description 比较当前计算出的价格与数据库存储价格
	 * @Date 13:54 2019/5/23
	 * @Param
	 * @return
	 **/
	private boolean compareOrderPament(JsonResult<List<ConfirmOrderVo>> result, Long orderId){
		BigDecimal total = (BigDecimal) result.get("total");
		BigDecimal tax = (BigDecimal) result.get("tax");
		BigDecimal ship = (BigDecimal) result.get("ship");
		BigDecimal amount = Tools.bigDecimal.add(BigDecimal.ZERO, total, tax, ship);
		//根据orderId查询orderpayment查出订单应付金额

		OrderPaymemtBo paymemtBo = orderPaymemtBiz.selectOne(Condition.create()
											.eq(OrderPaymemtBo.Key.orderId.toString(), orderId));
		if(paymemtBo == null){
			throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
		}

		if(amount.compareTo(paymemtBo.getShouldPayTotalFee()) != 0){
			log.info("-----------------amount:{}", amount);
			log.info("-----------------paymemtBo.getShouldPayTotalFee():{}", paymemtBo.getShouldPayTotalFee());
			return false;
		}
		return true;
	}

	/**
	 * @Author Mr.Simple
	 * @Description 订单失效逻辑
	 * @Date 15:07 2019/5/23
	 * @Param
	 * @return
	 **/
	private OrderInfoBo changeInvalidStatus(Long orderId){
		//改变订单和子单状态
		List<Long> orderIdList = new ArrayList<>();
		orderIdList.add(orderId);
		//查询是否有子单
		List<OrderInfoBo> orderList = orderInfoBiz.selectList(Condition.create()
				.eq(OrderInfoBo.Key.pid.toString(), orderId));
		if(orderList.size() != 0){
			orderList.forEach(item->{
				orderIdList.add(item.getId());
			});
		}
		OrderInfoBo updateBo = new OrderInfoBo();
		updateBo.setOrderStatus(OrderStatusEnum.INVALID);
		if(!orderInfoBiz.update(updateBo, Condition.create().in(OrderInfoBo.Key.id.toString(), orderIdList))){
			throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
		}
		OrderInfoBo orderInfoBo = orderInfoBiz.selectById(orderId);
		if(orderInfoBo == null){
			throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
		}
		return orderInfoBo;
	}




}