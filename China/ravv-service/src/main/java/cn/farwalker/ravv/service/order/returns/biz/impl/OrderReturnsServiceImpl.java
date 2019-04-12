package cn.farwalker.ravv.service.order.returns.biz.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import cn.farwalker.ravv.service.payment.pay.IRefundService;
import cn.farwalker.ravv.service.payment.paymentlog.biz.IMemberPaymentLogBiz;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.ravv.service.quartz.JobSchedulerFactory;
import cn.farwalker.ravv.service.quartz.UpdateReturnsOrderTaskJob;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cangwu.frame.orm.core.annotation.LoadJoinValueImpl;

import cn.farwalker.ravv.service.base.area.biz.IBaseAreaBiz;
import cn.farwalker.ravv.service.base.area.model.AreaFullVo;
import cn.farwalker.ravv.service.base.storehouse.biz.IStorehouseBiz;
import cn.farwalker.ravv.service.base.storehouse.model.StorehouseBo;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsBiz;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.goodssku.skudef.biz.IGoodsSkuDefBiz;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuDefBo;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuVo;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.order.operationlog.biz.IOrderLogService;
import cn.farwalker.ravv.service.order.ordergoods.biz.IOrderGoodsBiz;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoBiz;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderStatusEnum;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderTypeEnum;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderReturnSkuVo;
import cn.farwalker.ravv.service.order.orderlogistics.biz.IOrderLogisticsBiz;
import cn.farwalker.ravv.service.order.orderlogistics.model.OrderLogisticsBo;
import cn.farwalker.ravv.service.order.paymemt.biz.IOrderPaymemtBiz;
import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnLogBiz;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnLogService;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsBiz;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsDetailBiz;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsService;
import cn.farwalker.ravv.service.order.returns.constants.DamagedSourceEnum;
import cn.farwalker.ravv.service.order.returns.constants.OperatorTypeEnum;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsGoodsStatusEnum;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsTypeEnum;
import cn.farwalker.ravv.service.order.returns.dao.IOrderReturnsDao;
import cn.farwalker.ravv.service.order.returns.dao.IOrderReturnsDetailDao;
import cn.farwalker.ravv.service.order.returns.model.MemberOrderReturnsVo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnLogBo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnLogVo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsBo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsDetailBo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsDetailVo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsVo;
import cn.farwalker.ravv.service.order.time.OrderTaskTimer;
import cn.farwalker.ravv.service.paypal.RefundForm;
import cn.farwalker.ravv.service.shipment.biz.IShipmentBiz;
import cn.farwalker.ravv.service.shipment.model.ShipmentBo;
import cn.farwalker.ravv.service.sys.message.biz.ISystemMessageService;
import cn.farwalker.ravv.service.sys.user.biz.ISysUserBiz;
import cn.farwalker.ravv.service.sys.user.model.SysUserBo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.base.controller.ControllerUtils;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;

/**
 * 订单退货<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
@Service
public class OrderReturnsServiceImpl implements IOrderReturnsService {
	private static final Logger log = LoggerFactory.getLogger(OrderReturnsServiceImpl.class);
	@Resource
	private IOrderReturnLogService orderReturnLogService;

	@Resource
	private IOrderLogService orderLogService;

	@Resource
	private IOrderReturnLogBiz orderReturnLogBiz;

	@Resource
	private IOrderReturnsDao orderReturnsDao;

	@Resource
	private IOrderReturnsDetailBiz orderReturnsDetailBiz;

	@Resource
	private IOrderReturnsDetailDao orderReturnsDetailDao;

	@Resource
	private IOrderReturnsBiz orderReturnsBiz;

	@Resource
	private IOrderInfoBiz orderInfoBiz;

	@Resource
	private IOrderGoodsBiz orderGoodsBiz;

	@Resource
	private IStorehouseBiz storeHouseBiz;
	@Resource
	private IBaseAreaBiz areaBiz;
	@Resource
	private IGoodsBiz goodsBiz;
	
	@Resource
	private IGoodsSkuDefBiz goodsSkuDefBiz;
	@Resource
	private IOrderLogisticsBiz orderLogisticsBiz;

	@Resource
	private ISystemMessageService systemMessageService;

	@Resource
	private IOrderPaymemtBiz orderPaymentBiz;

	@Resource
	private IShipmentBiz shipmentBiz;
	
	@Resource
	private OrderTaskTimer orderTaskTimer;

	@Autowired
	private IMemberPaymentLogBiz iMemberPaymentLogBiz;

	@Autowired
	@Qualifier("refundServiceImpl")
	IRefundService iRefundService;

	private class ReturnSkuVo extends OrderReturnSkuVo {
		private int amt;

		/** 退款额 */
		public int getAmt() {
			return amt;
		}

		/** 退款额 */
		public void setAmt(int amt) {
			this.amt = amt;
		}

		public int getPrice() {
			int quan = Tools.number.nullIf(getQuan(), 0);
			if (quan <= 0) {
				return 0;
			} else {
				return amt / quan;
			}
		}
	}

	@Resource
	private ISysUserBiz sysUserBiz;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public JsonResult<Boolean> saveOperateOrderReturnsAndLog(Long userId, OperatorTypeEnum operatorType,
			OrderReturnsVo vo, final ReturnsGoodsStatusEnum status) {
		// ReturnsGoodsStatusEnum status = vo.getStatus();
		// 对应退货详情
		OrderReturnsDetailBo query = new OrderReturnsDetailBo();
		query.setReturnsId(vo.getId());
		List<OrderReturnsDetailBo> orderReturnsDetailList = orderReturnsDetailBiz
				.selectList(new EntityWrapper<OrderReturnsDetailBo>(query));
		if (Tools.collection.isEmpty(orderReturnsDetailList)) {
			return JsonResult.newFail("该条记录没有对应的退货详情");
		}
		Long orderId = orderReturnsDetailList.get(0).getOrderId();
		// 检查数量,若数量无误则对应订单减去退货数量
		List<ReturnsGoodsStatusEnum> effectiveStatus = getEffectiveStatus();
		List<ReturnsGoodsStatusEnum> effectiveDetailStatus = getEffectiveDetailsStatus();
		if (effectiveStatus.contains(status)) {
			for (OrderReturnsDetailBo ord : orderReturnsDetailList) {
				checkReturnQuan(ord.getOrderId(), ord.getSkuId(), ord.getRefundQty(), ord.getId());
				// 只有在允许退/换货和发货成功后才改变退货详情对应状态
				if (effectiveDetailStatus.contains(status)) {
					// ord.setStatus(status); detail表不添加商品状态
					orderReturnsDetailBiz.updateBatchById(orderReturnsDetailList);
				}
			}
		}
		String content = operatorType.getLabel() + "將退货状态改为了‘" + status.getLabel() + "’";
		String remark = "";
		if (status == ReturnsGoodsStatusEnum.allowExchange || status == ReturnsGoodsStatusEnum.allowRefund) {
			content += "；买家是否承担换货运费：" + (vo.getBuyerBearPostage() ? "是" : "否");
		} else if (status == ReturnsGoodsStatusEnum.refuseExchange || status == ReturnsGoodsStatusEnum.refuseRefund) {
			content += "；退品是否退还买家" + (vo.getIsReturnGoods() ? "是" : "否");
			if (vo.getIsReturnGoods()) {
				content += "；买家是否承担退货运费：" + (vo.getBuyerBearPostage() ? "是" : "否");
			}
			remark = vo.getRefuseReason();
			//修改订单对应备注
			this.saveOrderInfoRemark(orderId, false, vo.getReturnsType());
		} else if (status == ReturnsGoodsStatusEnum.exchangeAuditFail
				|| status == ReturnsGoodsStatusEnum.refundAuditFail) {
			remark = vo.getRefuseReason();
			//修改订单对应备注
			this.saveOrderInfoRemark(orderId, false, vo.getReturnsType());
		}
		SysUserBo user = sysUserBiz.selectById(userId);
		// String userName = "";
		// if (user != null) {
		// userName = "[" + user.getName() + "]";
		// }
		Boolean success = true;
		// 退款与其他状态分开处理
		if (status == ReturnsGoodsStatusEnum.refundSucess) { // 执行退款（调用第3方，原路退回）
			success = updateRefundPay(vo, orderReturnsDetailList, user);
			// 退款失败 记录订单日志 直接返回false 不再往下执行
			// if (success) {
			// orderLogService.createLog(orderId, userId, operatorType.getLabel() +
			// userName, "退款操作", "退款失败", false);
			// } else {
			// orderLogService.createLog(orderId, userId, operatorType.getLabel() +
			// userName, "退款操作", "退款成功", true);
			// }
			return JsonResult.newSuccess(success);
		}
		Boolean rs = saveChangeAndLog(orderId, user, operatorType, status, content, vo, remark);
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 修改退单状态并添加日志
	 * 
	 * @param orderId
	 * @param user
	 * @param operatorType
	 * @param status
	 * @param content
	 * @param vo
	 * @param remark
	 * @return
	 */
	private Boolean saveChangeAndLog(Long orderId, SysUserBo user, OperatorTypeEnum operatorType,
			ReturnsGoodsStatusEnum status, String content, OrderReturnsVo vo, String remark) {
		orderReturnLogService.createLog(orderId, user.getId(), operatorType.getLabel() + user.getName(),
				status.getLabel(), content, true, vo.getId(), status, remark);
		// 添加消息
		this.addReturnGoodsMessage(user.getId(), vo);
		// 审核时间
		if (ReturnsGoodsStatusEnum.isChecked(status)) {
			vo.setCheckTime(new Date());
		}
		// 改变退货单对应状态 若拒绝则 交易失败，成功 则交易完成
		ReturnsGoodsStatusEnum currentStatus = ReturnsGoodsStatusEnum.checkAndReturnStatus(status);
		vo.setStatus(currentStatus);
		if (currentStatus == ReturnsGoodsStatusEnum.failed || currentStatus == ReturnsGoodsStatusEnum.finish) {
			content = currentStatus.getLabel();
			// 保存成功或者失败的日志
			orderReturnLogService.createLog(vo.getOrderId(), user.getId(), operatorType.getLabel() + user.getName(),
					content, content, true, vo.getId(), currentStatus, "");
			// 若进入此判断则代表退单未完成状态
			vo.setFinishTime(new Date());
		}
		// 如果换货时 不允许换货允许退款需改变退货类型
		if (status == ReturnsGoodsStatusEnum.allowRefund && vo.getReturnsType() == ReturnsTypeEnum.ChangeGoods) {
			vo.setReturnsType(ReturnsTypeEnum.ReGoods);
		}
		vo.setGmtModified(new Date());
		Boolean rs = orderReturnsBiz.updateById(vo);
		return rs;
	}

	/**
	 * 执行退款（调用第3方，原路退回）

	 */
	private boolean updateRefundPay(OrderReturnsVo returnsVo, List<OrderReturnsDetailBo> detailBos, SysUserBo user) {
		BigDecimal adjustFee = returnsVo.getAdjustFee();
		//实退金额不能大于订单支付总额
		OrderPaymemtBo paymentBo = orderPaymentBiz.orderPayByOrderId(returnsVo.getOrderId());
		if(paymentBo == null) {
			throw new WakaException("订单没有支付记录，不允许退款");
		}
		BigDecimal refundFee = paymentBo.getShouldPayTotalFee();
		if (returnsVo.getAdjustFee() == null || returnsVo.getAdjustFee().doubleValue() == 0 || refundFee == null) {
			throw new WakaException("实际退款金额不能为0");
		}
		adjustFee = adjustFee.setScale(2, RoundingMode.HALF_UP);
		refundFee = refundFee.setScale(2, RoundingMode.HALF_UP);
		if (adjustFee.doubleValue() > refundFee.doubleValue()) {
			throw new WakaException("实际退款金额(" + adjustFee + ")不能大于订单付款总额(" + refundFee + ")");
		}
		final Long orderId = returnsVo.getOrderId();// 订单ID
		final Long returnOrderId = returnsVo.getId();// 退货单id
		final Long memberId = returnsVo.getBuyerId(); //

		/*
		 * refundSubAmount：===================退款的商品总价 refundTax:=============要退的税
		 * refundShipping:===========要退的运费 memberId:====================用于测试，生产环境下后台自己取
		 */
		BigDecimal refundSuccessAmt;
		{// 已经退款成功的金额，不能大于订单金额
			Wrapper<OrderReturnsBo> query = new EntityWrapper<>();

			query.setSqlSelect(OrderReturnsBo.Key.adjustFee.toString());
			query.ne(OrderReturnsBo.Key.id.toString(), returnOrderId);// 排除当前记录
			query.eq(OrderReturnsBo.Key.orderId.toString(), orderId);
			query.in(OrderReturnsBo.Key.returnsType.toString(),
					Arrays.asList(ReturnsTypeEnum.ReGoods, ReturnsTypeEnum.Refund));
			query.in(OrderReturnsBo.Key.status.toString(),
					Arrays.asList(ReturnsGoodsStatusEnum.refundSucess, ReturnsGoodsStatusEnum.finish));

			List<OrderReturnsBo> rds = orderReturnsBiz.selectList(query);
			BigDecimal total = adjustFee;
			for (OrderReturnsBo vo : rds) {
				total = Tools.bigDecimal.add(total, vo.getAdjustFee());
			}
			refundSuccessAmt = total.setScale(2, RoundingMode.HALF_UP);
		}
		if (refundSuccessAmt.compareTo(paymentBo.getShouldPayTotalFee()) > 0) {
			throw new WakaException(
					"退款总额(" + refundSuccessAmt + ")不能大于付款总金额(" + paymentBo.getShouldPayTotalFee() + ")");
		}
		//////// 使用线程调用第三方退款，不要在事物里调用第三方接口////////////////////////
		final RefundForm formVo = new RefundForm();
		formVo.setMemberId(memberId);
		// TODO 获取总单id
		OrderInfoBo orderInfo = orderInfoBiz.selectById(orderId);
		if (orderInfo == null) {
			throw new WakaException("找不到对应主订单");
		} else {
			if (orderInfo.getOrderType() == OrderTypeEnum.CHILD) {
				formVo.setOrderId(orderInfo.getPid());
			} else {
				formVo.setOrderId(orderId);
			}
		}
		formVo.setRefundTotalAmount(adjustFee);
		formVo.setReturnOrderId(returnOrderId);

		Tools.thread.runSingle(new Runnable() {
			@Override
			public void run() {
				try {
					log.info("执行退款支付:" + Tools.json.toJson(formVo));
					iRefundService.refund(formVo);
					orderLogService.createLog(orderId, user.getId(),
							OperatorTypeEnum.server.getLabel() + user.getName(), "退款操作", "退款成功", true);
					saveChangeAndLog(orderId, user, OperatorTypeEnum.server, ReturnsGoodsStatusEnum.refundSucess,
							"客服操作退款", returnsVo, "");
					//订单对应备注需要更改
					OrderInfoBo orderbo = orderInfoBiz.selectById(returnsVo.getOrderId());
					if(orderbo != null) {
						//判断并更新对应订单状态
						orderTaskTimer.closeOrderForReturn(Arrays.asList(orderbo));
					}else {
						log.error("更新订单失败，订单数据被删除了！");
					}
			
					 
				} catch (Exception e) {
					returnsVo.setRemark("退款支付失败:" + e.getMessage());
					// 改回退单状态
					if (returnsVo.getReturnsType() == ReturnsTypeEnum.ChangeGoods) {
						returnsVo.setStatus(ReturnsGoodsStatusEnum.refundAuditSucess);
					} else if (returnsVo.getReturnsType() == ReturnsTypeEnum.ReGoods) {
						returnsVo.setStatus(ReturnsGoodsStatusEnum.allowRefund);
					}
					orderReturnsBiz.updateById(returnsVo);
					orderLogService.createLog(orderId, user.getId(),
							OperatorTypeEnum.server.getLabel() + user.getName(), "退款操作", "退款失败", false);
					log.error("执行退款支付失败:" + Tools.json.toJson(formVo), e);
				}
			}
		}, "doRefundPay");
		return true;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public JsonResult<Boolean> updateOrderReturnsAndCreateLog(Long userId, OperatorTypeEnum operatorType,
			OrderReturnsBo bo) {
		// 改变退货单对应状态
		Boolean rs = orderReturnsBiz.updateById(bo);
		// 记录操作日志
		SysUserBo user = sysUserBiz.selectById(userId);
		String userName = "";
		if (user != null) {
			userName = user.getName();
		}
		orderLogService.createLog(bo.getOrderId(), userId, userName, "修改", "修改退货信息。", true);

		OrderReturnsVo vo = Tools.bean.cloneBean(bo, new OrderReturnsVo());
		// 添加消息
		this.addReturnGoodsMessage(userId, vo);
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 新增退货消息
	 * 
	 * @param userId
	 * @param vo
	 */
	private void addReturnGoodsMessage(Long userId, OrderReturnsVo vo) {
		OrderReturnsBo bo = orderReturnsBiz.selectById(vo.getId());
		List<ReturnsGoodsStatusEnum> needSendMessageStatusList = Arrays.asList(
				ReturnsGoodsStatusEnum.exchangeAuditSucess, ReturnsGoodsStatusEnum.exchangeAuditFail,
				ReturnsGoodsStatusEnum.refuseExchange, ReturnsGoodsStatusEnum.buyerWaitReceived,
				ReturnsGoodsStatusEnum.exchangeSucess, ReturnsGoodsStatusEnum.refundAuditSucess,
				ReturnsGoodsStatusEnum.refundAuditFail, ReturnsGoodsStatusEnum.refuseRefund,
				ReturnsGoodsStatusEnum.refundSucess);
		if (needSendMessageStatusList.contains(vo.getStatus())) {
			systemMessageService.addReturnGoodsMessage(userId, bo.getId(), bo.getReturnsType(), vo.getStatus(),
					vo.getLogisticsNo(), vo.getRefuseReason(), bo.getGmtCreate());
		}
	}

	// 验收
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void saveCheckAndAcceptanceGoods(Long userId, OperatorTypeEnum operatorType, OrderReturnsVo vo) {
		String validesc = "包装是否损坏：" + (vo.getPackingDamaged() ? "是" : "否") + "。";
		log.info("============================验收:{}", validesc);
		if (vo.getPackingDamaged() && Tools.collection.isNotEmpty(vo.getDamagedSource())) {
			String damagedSourceStr = "";
			for (DamagedSourceEnum ds : vo.getDamagedSource()) {
				if (Tools.string.isEmpty(damagedSourceStr)) {
					damagedSourceStr = ds.getLabel();
				} else {
					damagedSourceStr += "、" + ds.getLabel();
				}
			}
			validesc += "包装损坏原因：'" + damagedSourceStr + "'。";
		}
		if (vo.getDamagedCondition() != null) {
			validesc += "退品损坏程度：'" + vo.getDamagedCondition().getLabel() + "'。";
		}
		// if (vo.getContent() != null) {
		// validesc += "备注：" + vo.getContent() + "。";
		// }

		// 验收退货详情对应退品
		OrderReturnsDetailBo orderReturnsDetail = new OrderReturnsDetailBo();
		orderReturnsDetail.setId(vo.getOrderReturnsDetailId());
		orderReturnsDetail.setRemark(vo.getContent());
		orderReturnsDetail.setActualRefundQty(vo.getActualRefundQty());
		orderReturnsDetail.setValidesc(validesc);
		orderReturnsDetailBiz.updateById(orderReturnsDetail);
		// 记录操作日志
		SysUserBo user = sysUserBiz.selectById(userId);
		String userName = "";
		if (user != null) {
			userName = "[" + user.getName() + "]";
		}
		String content = operatorType.getLabel() + "验收了退货详情[" + vo.getOrderReturnsDetailId() + "]对应的退品。";
		orderLogService.createLog(vo.getOrderId(), userId, operatorType.getLabel() + userName, "仓管验收退品", content, true);

		// 添加消息
		this.addReturnGoodsMessage(userId, vo);
	}

	@Override
	public List<Map<String, String>> getReturnsStatusByStatusForWarehouse(ReturnsGoodsStatusEnum status) {
		if (status == null) {
			return null;
		}
		List<ReturnsGoodsStatusEnum> returnsStatusList = new ArrayList<>();
		switch (status) {
		/** 未退货/退款 **/
		case normal:
			break;
		/** 退货申请中 */
		case exchangeApply:
			returnsStatusList.add(ReturnsGoodsStatusEnum.exchangeApply);
			break;

		// /** 退货审核通过 */
		case exchangeAuditSucess:
			returnsStatusList.add(ReturnsGoodsStatusEnum.exchangeAuditSucess);
			break;
		/** 退货审核不通过 */
		case exchangeAuditFail:
			returnsStatusList.add(ReturnsGoodsStatusEnum.exchangeAuditFail);
			break;

		/** 待卖方收货 */
		case exchangeWaitReceived:
			returnsStatusList.add(ReturnsGoodsStatusEnum.exchangeWaitReceived);
			//
			returnsStatusList.add(ReturnsGoodsStatusEnum.exchangeAcceptanceReceived);
			break;

		/** 验收换货退品 */
		case exchangeAcceptanceReceived:
			returnsStatusList.add(ReturnsGoodsStatusEnum.exchangeAcceptanceReceived);
			break;
		/** 允许 */
		case allowExchange:
			returnsStatusList.add(ReturnsGoodsStatusEnum.allowExchange);
			break;
		/** 拒绝 */
		case refuseExchange:
			returnsStatusList.add(ReturnsGoodsStatusEnum.refuseExchange);
			break;
		/** 待买家收取退品 */
		case buyerWaitReceived:
			returnsStatusList.add(ReturnsGoodsStatusEnum.buyerWaitReceived);
			break;
		/** 换货成功 */
		case exchangeSucess:
			returnsStatusList.add(ReturnsGoodsStatusEnum.exchangeSucess);
			break;

		/** 退款申请中 */
		case refundApply:
			returnsStatusList.add(ReturnsGoodsStatusEnum.refundApply);
			break;

		/** 退款审核通过 */
		case refundAuditSucess:
			returnsStatusList.add(ReturnsGoodsStatusEnum.refundAuditSucess);
			break;

		/** 退款审核不通过 */
		case refundAuditFail:
			returnsStatusList.add(ReturnsGoodsStatusEnum.refundAuditFail);
			break;

		/** 待卖方收货 */
		case refundWaitReceived:
			returnsStatusList.add(ReturnsGoodsStatusEnum.refundWaitReceived);
			//
			returnsStatusList.add(ReturnsGoodsStatusEnum.refundAcceptanceReceived);
			break;

		/** 收到退品/未退款 */
		case refundAcceptanceReceived:
			returnsStatusList.add(ReturnsGoodsStatusEnum.refundAcceptanceReceived);
			break;
		/** 退货失败 */
		/** 允许 */
		case allowRefund:
			returnsStatusList.add(ReturnsGoodsStatusEnum.allowRefund);
			break;
		/** 拒绝 */
		case refuseRefund:
			returnsStatusList.add(ReturnsGoodsStatusEnum.refuseRefund);
			break;
		/** 退款成功 */
		case refundSucess:
			returnsStatusList.add(ReturnsGoodsStatusEnum.refundSucess);
			break;
		case failed:
			returnsStatusList.add(ReturnsGoodsStatusEnum.failed);
			break;
		case finish:
			returnsStatusList.add(ReturnsGoodsStatusEnum.finish);
			break;
		default:
			break;
		}
		List<Map<String, String>> returnsMapList = new ArrayList<>();
		for (ReturnsGoodsStatusEnum rstatus : returnsStatusList) {
			Map<String, String> map = new HashMap<>();
			map.put("key", rstatus.getKey());
			map.put("label", rstatus.getLabel());
			returnsMapList.add(map);
		}
		return returnsMapList;
	}

	@Override
	public List<Map<String, String>> getReturnsStatusByStatusForService(ReturnsGoodsStatusEnum status) {
		if (status == null) {
			return null;
		}
		List<ReturnsGoodsStatusEnum> returnsStatusList = new ArrayList<>();
		switch (status) {
		/** 未退货/退款 **/
		case normal:
			break;
		/** 退货申请中 */
		case exchangeApply:
			returnsStatusList.add(ReturnsGoodsStatusEnum.exchangeApply);
			//
			returnsStatusList.add(ReturnsGoodsStatusEnum.exchangeAuditSucess);
			returnsStatusList.add(ReturnsGoodsStatusEnum.exchangeAuditFail);
			break;

		/** 退货审核通过 */
		case exchangeAuditSucess:
			returnsStatusList.add(ReturnsGoodsStatusEnum.exchangeAuditSucess);
			break;

		/** 退货审核不通过 */
		case exchangeAuditFail:
			returnsStatusList.add(ReturnsGoodsStatusEnum.exchangeAuditFail);
			break;

		/** 待卖方收货 */
		case exchangeWaitReceived:
			returnsStatusList.add(ReturnsGoodsStatusEnum.exchangeWaitReceived);

			break;

		/** 收到退品/未发货 */
		case exchangeAcceptanceReceived:
			returnsStatusList.add(ReturnsGoodsStatusEnum.exchangeAcceptanceReceived);
			returnsStatusList.add(ReturnsGoodsStatusEnum.allowExchange);
			returnsStatusList.add(ReturnsGoodsStatusEnum.refuseExchange);
			returnsStatusList.add(ReturnsGoodsStatusEnum.allowRefund);
			break;
		/** 允许 */
		case allowExchange:
			returnsStatusList.add(ReturnsGoodsStatusEnum.allowExchange);
			break;
		/** 拒绝 */
		case refuseExchange:
			returnsStatusList.add(ReturnsGoodsStatusEnum.refuseExchange);
			break;
		/** 待买家收取退品 */
		case buyerWaitReceived:
			returnsStatusList.add(ReturnsGoodsStatusEnum.buyerWaitReceived);
			break;

		/** 换货成功 */
		case exchangeSucess:
			returnsStatusList.add(ReturnsGoodsStatusEnum.exchangeSucess);
			break;

		/** 退款申请中 */
		case refundApply:
			returnsStatusList.add(ReturnsGoodsStatusEnum.refundApply);
			//
			returnsStatusList.add(ReturnsGoodsStatusEnum.refundAuditSucess);
			returnsStatusList.add(ReturnsGoodsStatusEnum.refundAuditFail);

			break;

		/** 退款审核通过 */
		case refundAuditSucess:
			returnsStatusList.add(ReturnsGoodsStatusEnum.refundAuditSucess);
			break;

		/** 退款审核不通过 */
		case refundAuditFail:
			returnsStatusList.add(ReturnsGoodsStatusEnum.refundAuditFail);
			break;

		/** 待卖方收货 */
		case refundWaitReceived:
			returnsStatusList.add(ReturnsGoodsStatusEnum.refundWaitReceived);

			break;

		/** 收到退品/未退款 */
		case refundAcceptanceReceived:
			returnsStatusList.add(ReturnsGoodsStatusEnum.refundAcceptanceReceived);
			returnsStatusList.add(ReturnsGoodsStatusEnum.allowRefund);
			returnsStatusList.add(ReturnsGoodsStatusEnum.refuseRefund);
			break;
		/** 允许 */
		case allowRefund:
			returnsStatusList.add(ReturnsGoodsStatusEnum.allowRefund);
			// returnsStatusList.add(ReturnsGoodsStatusEnum.refundSucess);
			break;
		/** 拒绝退货 */
		case refuseRefund:
			returnsStatusList.add(ReturnsGoodsStatusEnum.refuseRefund);
			break;
		/** 退款成功 */
		case refundSucess:
			returnsStatusList.add(ReturnsGoodsStatusEnum.refundSucess);
			break;
		case failed:
			returnsStatusList.add(ReturnsGoodsStatusEnum.failed);
			break;
		case finish:
			returnsStatusList.add(ReturnsGoodsStatusEnum.finish);
			break;
		default:
			break;
		}
		List<Map<String, String>> returnsMapList = new ArrayList<>();
		for (ReturnsGoodsStatusEnum rstatus : returnsStatusList) {
			Map<String, String> map = new HashMap<>();
			map.put("key", rstatus.getKey());
			map.put("label", rstatus.getLabel());
			returnsMapList.add(map);
		}
		return returnsMapList;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public OrderReturnsBo createOrderReturns(Long orderId, List<OrderReturnSkuVo> skus, ReturnsTypeEnum returnsType, MemberBo member) {
		// 判断订单状态是否允许退换货
		OrderInfoBo order = orderInfoBiz.selectById(orderId);
		if (order != null) {
			List<OrderStatusEnum> canRefundOrderStatusList = this.getCanRefundOrderStatus();
			if (!canRefundOrderStatusList.contains(order.getOrderStatus())) {
				throw new WakaException("该订单不符合退换要求");
			}
		} else {
			throw new WakaException("找不到对应订单,请联系管理员");
		}

		List<ReturnSkuVo> returnSkuVo = createOrderReturnsCheck(orderId, skus);
		OrderReturnsBo bo = this.createOrderReturnSub(orderId, returnSkuVo, returnsType);
		// 创建申请日志
		// 获取用户相关信息 并记录对应日志
		String memberName = member.getFirstname() + " " + member.getLastname();
		String content = "用户[" + memberName + "]";
		// 退货状态
		ReturnsGoodsStatusEnum status = null;
		if (returnsType == ReturnsTypeEnum.ChangeGoods) {
			status = ReturnsGoodsStatusEnum.exchangeApply;
			content += "发起换货申请";
		} else if (returnsType == ReturnsTypeEnum.ReGoods) {
			status = ReturnsGoodsStatusEnum.refundApply;
			content += "发起退货申请";
		} else if (returnsType == ReturnsTypeEnum.Refund) {
			status = ReturnsGoodsStatusEnum.refundApply;
			content += "发起退款申请";
			//TODO 发起退款需要修改备注， 取消退款的时候需要对应修改
		}
		this.saveOrderInfoRemark(orderId, true, returnsType);
		// 记录申请日志
		orderReturnLogService.createLog(bo.getOrderId(), member.getId(), memberName, status.getLabel(), content, true,
				bo.getId(), status, "");

		return bo;
	}
	
	private void saveOrderInfoRemark(Long orderId, Boolean isReturnApply, ReturnsTypeEnum returnsType) {
		OrderInfoBo orderBo = new OrderInfoBo();
		orderBo.setId(orderId);
		if(isReturnApply) {
			orderBo.setRemark("该订单正在[" + returnsType.getLabel() + "]操作");
		}else {
			orderBo.setRemark(" ");
		}
		
		orderInfoBiz.updateById(orderBo);
	}
	
	
	private List<OrderStatusEnum> getCanRefundOrderStatus() {
		return Arrays.asList(OrderStatusEnum.PAID_UNSENDGOODS, OrderStatusEnum.SENDGOODS_UNCONFIRM,
				OrderStatusEnum.SING_GOODS);
	}

	@Override
	public OrderReturnsBo createOrderRefund(Long orderId, String reasonType, String reason, MemberBo member) {
		// 退款每个原因都一样
		OrderGoodsBo query = new OrderGoodsBo();
		query.setOrderId(orderId);
		List<OrderGoodsBo> orderGoodsList = orderGoodsBiz.selectList(new EntityWrapper<OrderGoodsBo>(query));
		if (Tools.collection.isEmpty(orderGoodsList)) {
			throw new WakaException("找不到对应商品,请联系管理员");
		}
		List<OrderReturnSkuVo> skus = new ArrayList<OrderReturnSkuVo>();
		for (OrderGoodsBo bo : orderGoodsList) {
			OrderReturnSkuVo skuvo = new OrderReturnSkuVo();
			skuvo.setGoodsId(bo.getGoodsId());
			skuvo.setQuan(bo.getQuantity());
			skuvo.setSkuId(bo.getSkuId());
			skuvo.setReason(reason);
			skuvo.setReasonType(reasonType);
			skus.add(skuvo);
		}
		ReturnsTypeEnum returnsType = ReturnsTypeEnum.Refund;
		return this.createOrderReturns(orderId, skus, returnsType, member);
	}

	private OrderReturnsBo createOrderReturnSub(Long orderId, List<ReturnSkuVo> returnSkuVos,
			ReturnsTypeEnum returnsType) {
		List<OrderReturnsDetailBo> detailBos = new ArrayList<>(returnSkuVos.size());
		int refundFee = 0;
		for (ReturnSkuVo vo : returnSkuVos) {
			refundFee += vo.getAmt();

			OrderReturnsDetailBo bo = new OrderReturnsDetailBo();
			bo.setActualRefundQty(vo.getQuan());
			if (vo.getGoodsId() != null) {
				GoodsBo goods = goodsBiz.selectById(vo.getGoodsId());
				if (goods == null) {
					throw new WakaException("找不到对应商品商品");
				}
				bo.setGoodsName(goods.getGoodsName());
			} else {
				throw new WakaException("商品id不能为空");
			}
			bo.setGoodsId(vo.getGoodsId());
			// bo.setGoodsName(goodsName);
			bo.setOrderId(orderId);
			bo.setReason(vo.getReason());
			bo.setRefundPrice(new BigDecimal(vo.getPrice() / 100.0d));
			if (Tools.collection.isNotEmpty(vo.getReturnGoodsImgList())) {
				bo.setImglistJson(Tools.json.toJson(vo.getReturnGoodsImgList()));
			}
			bo.setRefundQty(vo.getQuan());
			bo.setSkuId(vo.getSkuId());
			// detail 不需要退货类型和状态
			// bo.setReturnsType(vo.getReturnsType());
			// if (vo.getReturnsType() == ReturnsTypeEnum.ChangeGoods) { // 换货
			// bo.setStatus(ReturnsGoodsStatusEnum.exchangeApply);
			// } else {
			// bo.setStatus(ReturnsGoodsStatusEnum.refundApply);
			// }
			bo.setReasonType(vo.getReasonType());
			detailBos.add(bo);
		}
		LoadJoinValueImpl.load(orderGoodsBiz, detailBos);// 冗余商品名称

		OrderInfoBo orderBo = orderInfoBiz.selectById(orderId);
		OrderReturnsBo bo = new OrderReturnsBo();
		// bo.setAdjustFee(adjustFee);
		bo.setBuyerId(orderBo.getBuyerId());
		bo.setBuyerNick(orderBo.getBuyerNick());
		bo.setOrderCode(orderBo.getOrderCode());
		bo.setOrderId(orderId);
		bo.setRefundFee(new BigDecimal(refundFee / 100d));

		StorehouseBo storeBo = storeHouseBiz.selectById(orderBo.getStorehouseId());
		AreaFullVo addressVo = AreaFullVo.getAreaFullVo(storeBo.getAreaid(), areaBiz);
		bo.setSellerAddress(addressVo.getFullAddress() + storeBo.getAddress());
		bo.setSellerPhone(storeBo.getPhone());
		// 退货状态
		bo.setReturnsType(returnsType);
		if (returnsType == ReturnsTypeEnum.ChangeGoods) {
			bo.setStatus(ReturnsGoodsStatusEnum.exchangeApply);
		} else if (returnsType == ReturnsTypeEnum.ReGoods) {
			bo.setStatus(ReturnsGoodsStatusEnum.refundApply);
		} else if (returnsType == ReturnsTypeEnum.Refund) {
			bo.setStatus(ReturnsGoodsStatusEnum.refundApply);
		}
		orderReturnsBiz.insert(bo);
		Long returnId = bo.getId();
		for (OrderReturnsDetailBo dv : detailBos) {
			dv.setReturnsId(returnId);
		}
		orderReturnsDetailBiz.insertBatch(detailBos);
		return bo;
	}

	/**
	 * 检查退货数量(退货修改状态时使用)
	 * 
	 * @param orderid
	 * @param skuId
	 * @param quan
	 *            退货数量
	 * @param detailsId
	 *            数据表{@link OrderReturnsDetailBo#TABLE_NAME}.id ，有id时，统计数量会减去这个数量
	 * @return
	 */
	private List<ReturnSkuVo> checkReturnQuan(Long orderid, Long skuId, Integer quan, Long detailsId) {
		OrderReturnSkuVo vo = new OrderReturnSkuVo();
		// vo.setGoodsId(goodsId);
		vo.setSkuId(skuId);
		vo.setQuan(quan);
		List<OrderReturnSkuVo> skus = Arrays.asList(vo);
		return createOrderReturnsGoodsCheck(orderid, skus, detailsId);
	}

	/** 检查退货数量及状态(退货订单创建前使用) */
	private List<ReturnSkuVo> createOrderReturnsCheck(Long orderId, List<OrderReturnSkuVo> skus) {
		// 一个订单仅能存在一个进行中的退单
		List<MemberOrderReturnsVo> ongoingReturns = this.getMyReturns(null, null, null, null, null, null, false,
				orderId);
		if (Tools.collection.isNotEmpty(ongoingReturns)) {
			throw new WakaException("该订单存在未完成退单，不能进行退货相关操作");
		}
		return createOrderReturnsGoodsCheck(orderId, skus, null);
	}

	/**
	 * 检查退货数量及状态
	 * 
	 * @param orderId
	 * @param skus
	 * @param detailsId
	 *            数据表id，有id时，统计数量会减去这个数量
	 * @return
	 */
	private List<ReturnSkuVo> createOrderReturnsGoodsCheck(Long orderId, List<OrderReturnSkuVo> skus, Long detailsId) {
		// 合计已退货的数量
		Map<Long, Integer> skuQuan = new HashMap<>(skus.size());
		List<Long> skuIdList = new ArrayList<>(skus.size());
		for (OrderReturnSkuVo s : skus) {
			if (s.getQuan() <= 0) {
				// 添加数量限制
				throw new WakaException("退货数量不能小于等于0");
			}
			skuIdList.add(s.getSkuId());
			skuQuan.put(s.getSkuId(), s.getQuan());
			// 取出图片相对路径
			if (Tools.collection.isNotEmpty(s.getReturnGoodsImgList())) {
				List<String> imgList = new ArrayList<>();
				for (String str : s.getReturnGoodsImgList()) {
					imgList.add(QiniuUtil.getRelativePath(str));
				}
				s.setReturnGoodsImgList(imgList);
			}
		}

		// 这个退货有效的状态
		List<ReturnsGoodsStatusEnum> statusList = getEffectiveStatus();

		// Wrapper<OrderReturnsDetailBo> wp = new EntityWrapper<>();
		// wp.eq(OrderReturnsDetailBo.Key.orderId.toString(), orderid);
		// wp.in(OrderReturnsDetailBo.Key.skuId.toString(), skuIds);
		// wp.in(OrderReturnsDetailBo.Key.status.toString(), status);
		if (Tools.collection.isEmpty(skuIdList)) {
			skuIdList = null;
		}
		if (Tools.collection.isEmpty(statusList)) {
			statusList = null;
		}
		List<OrderReturnsDetailBo> rds = orderReturnsDetailDao.getEffectiveReturnsDetail(orderId, skuIdList,
				statusList);

		// 合计已退货的数量
		for (OrderReturnsDetailBo d : rds) {
			if (detailsId != null && detailsId.equals(d.getId())) {
				;// 不统计
			} else {
				int quan = Tools.number.nullIf(skuQuan.get(d.getSkuId()), 0);
				int refund = Tools.number.nullIf(d.getActualRefundQty(), 0);
				int qty = quan + refund;
				skuQuan.put(d.getSkuId(), Integer.valueOf(qty));
			}
		}

		Wrapper<OrderGoodsBo> wporder = new EntityWrapper<>();
		wporder.eq(OrderGoodsBo.Key.orderId.toString(), orderId);
		wporder.in(OrderGoodsBo.Key.skuId.toString(), skuIdList);
		List<OrderGoodsBo> orderGoodsBos = orderGoodsBiz.selectList(wporder);

		if (orderGoodsBos.size() != skuQuan.size()) {
			throw new WakaException("退货商品与订单商品不匹配,orderId=" + orderId);
		}

		///////////////////////
		List<ReturnSkuVo> result = new ArrayList<>(skus.size());
		for (OrderGoodsBo vo : orderGoodsBos) {
			// 退货的数量
			Long skuId = vo.getSkuId();
			int returnQuan = Tools.number.nullIf(skuQuan.get(skuId), 0);//当前+历史退货数量
			int quan = Tools.number.nullIf(vo.getQuantity(), 0);// 订单数量
			if (returnQuan > quan) {
				throw new WakaException("退货数量(" + returnQuan + ")不能大于订单数量(" + quan + "),orderId=" + orderId);
			}

			OrderReturnSkuVo skuvo = Tools.collection.getBo(skus, skuId, "skuId");
			ReturnSkuVo e = Tools.bean.cloneBean(skuvo, new ReturnSkuVo());
			int returnQuan2 = Tools.number.nullIf(e.getQuan(), 0);
			if (returnQuan2 == quan) { // 全部一起退货
				double amt = vo.getGoodsfee().doubleValue() * 100;
				e.setAmt((int) amt);
			} else {// 部分退货
				double prc = Tools.number.nullIf(vo.getPrice(), 0d) * 100;
				//当前退货数量为 skuvo.getQuan()
				e.setAmt((int) (prc * skuvo.getQuan()));
			}
			result.add(e);
		}

		return result;
	}

	/** 有效的退货状态 */
	private List<ReturnsGoodsStatusEnum> getEffectiveStatus() {
		List<ReturnsGoodsStatusEnum> status = Arrays.asList(
				/** 退货申请中 */
				ReturnsGoodsStatusEnum.exchangeApply,
				/** 退货审核通过 */
				ReturnsGoodsStatusEnum.exchangeAuditSucess,

				/** 退货审核不通过 */
				// exchangeAuditFail("exchangeAuditFail", "换货审核不通过"),

				/** 待卖方收货 */
				ReturnsGoodsStatusEnum.exchangeWaitReceived,

				/** 验收换货退品 */
				ReturnsGoodsStatusEnum.exchangeAcceptanceReceived,

				/** 允许换货 */
				ReturnsGoodsStatusEnum.allowExchange,
				/** 拒绝换货 */
				// ReturnsGoodsStatusEnum.refuseExchange("refuseExchange", "拒绝换货"),
				/** 待买家收货(退货成功) */
				ReturnsGoodsStatusEnum.buyerWaitReceived,

				/** 换货成功 */
				ReturnsGoodsStatusEnum.exchangeSucess,

				/** 退款申请中 */
				ReturnsGoodsStatusEnum.refundApply,

				/** 退款审核通过 */
				ReturnsGoodsStatusEnum.refundAuditSucess,

				/** 退款审核不通过 */
				// ReturnsGoodsStatusEnum.refundAuditFail("refundAuditFail", "退款审核不通过"),

				/** 待卖方收货 */
				ReturnsGoodsStatusEnum.refundWaitReceived,

				/** 验收退货退品 */
				ReturnsGoodsStatusEnum.refundAcceptanceReceived,
				/** 允许退货 */
				ReturnsGoodsStatusEnum.allowRefund,
				/** 拒绝退货 */
				// refuseRefund("refuseRefund", "拒绝退货退款"),
				/** 退款成功 */
				ReturnsGoodsStatusEnum.refundSucess,
				/** 交易完成 */
				ReturnsGoodsStatusEnum.finish);
		return status;
	}

	/** 有效的退货详情状态 */
	private List<ReturnsGoodsStatusEnum> getEffectiveDetailsStatus() {
		List<ReturnsGoodsStatusEnum> status = Arrays.asList(
				/** 允许换货 */
				ReturnsGoodsStatusEnum.allowExchange,
				/** 允许退货 */
				ReturnsGoodsStatusEnum.allowRefund,
				/** 待买家收货 */
				ReturnsGoodsStatusEnum.buyerWaitReceived,
				/** 退货退款成功 */
				ReturnsGoodsStatusEnum.refundSucess);
		return status;
	}

	@Override
	public void updateReturnsStatus(Long returnsdetailId, ReturnsGoodsStatusEnum status, String validesc) {
		if (returnsdetailId == null || status == null) {
			throw new WakaException("退货单明细id或状态不能为空");
		}
		OrderReturnsDetailBo bo = new OrderReturnsDetailBo();
		bo.setId(returnsdetailId);
		// if (status != null) {
		// bo.setStatus(status);
		// }
		if (validesc != null) {
			bo.setValidesc(validesc);
		}
		this.updateReturnsStatus(Collections.singletonList(bo));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void updateReturnsStatus(List<OrderReturnsDetailBo> detailBos) {
		if (Tools.collection.isEmpty(detailBos)) {
			throw new WakaException("退货单明细id或状态不能为空");
		}

		////////////////////////////////////////
		List<Long> ids = new ArrayList<>();
		List<OrderReturnsDetailBo> bos = new ArrayList<>();// orderReturnsDetailBiz.selectBatchIds(status.keySet());
		for (OrderReturnsDetailBo e : detailBos) {
			OrderReturnsDetailBo bo = new OrderReturnsDetailBo();
			bo.setId(e.getId());

			// bo.setStatus(e.getStatus());
			bo.setValidesc(e.getValidesc());// 验收描述(null不更新)
			bos.add(bo);

			ids.add(bo.getId());
		}
		orderReturnsDetailBiz.updateBatchById(bos);
		// 是否需要更新主表
		// updateReturnsStatusMaster(ids);
	}

	/**
	 * 是否需要更新主表
	 * 
	 * @param detailIds
	 */
	private void updateReturnsStatusMaster(List<Long> detailIds) {
		List<OrderReturnsDetailBo> detailState = orderReturnsDetailDao.getReturnsDetailState(detailIds);
		Map<Long, List<ReturnsGoodsStatusEnum>> returnState = new HashMap<>();
		for (OrderReturnsDetailBo s : detailState) {
			Long returnId = s.getReturnsId();

			List<ReturnsGoodsStatusEnum> rds = returnState.get(returnId);
			if (rds == null) {
				rds = new ArrayList<>();
				returnState.put(returnId, rds);
			}
			// ReturnsGoodsStatusEnum st = s.getStatus();
			// if (!rds.contains(st)) {
			// rds.add(st);
			// }
		}
		for (Entry<Long, List<ReturnsGoodsStatusEnum>> rs : returnState.entrySet()) {
			if (rs.getValue().size() == 1) {
				OrderReturnsBo rb = new OrderReturnsBo();
				rb.setId(rs.getKey());
				rb.setStatus(rs.getValue().get(0));
				orderReturnsBiz.updateById(rb);
			}
		}
	}

	@Override
	public Map<Long, Integer> getReturnQuanByOrder(Collection<Long> orderIds, List<ReturnsGoodsStatusEnum> status) {
		List<OrderReturnsDetailBo> bos = this.getReturnQuanByGoods(orderIds, status);

		// 合计以订单为单位
		Map<Long, Integer> rds = new HashMap<>(bos.size());
		for (OrderReturnsDetailBo bo : bos) {
			Long orderId = bo.getOrderId();
			int mq = Tools.number.nullIf(rds.get(orderId), 0);
			int bq = Tools.number.nullIf(bo.getRefundQty(), 0);
			rds.put(orderId, Integer.valueOf(mq + bq));
		}
		return rds;
	}

	@Override
	public List<OrderReturnsDetailBo> getReturnQuanByGoods(Collection<Long> orderIds,
			List<ReturnsGoodsStatusEnum> status) {
		if (Tools.collection.isEmpty(orderIds)) {
			throw new WakaException("订单id及状态不能为空");
		}
		List<OrderReturnsDetailBo> bos = orderReturnsDetailDao.getEffectiveReturnCount(orderIds, status);
		return bos;
	}

	@Override
	public List<OrderReturnLogVo> getOrderReturnLogList(Long returnId) {
		OrderReturnLogBo query = new OrderReturnLogBo();
		query.setReturnId(returnId);
		EntityWrapper<OrderReturnLogBo> wp = new EntityWrapper<OrderReturnLogBo>(query);
		wp.orderBy(OrderReturnLogBo.Key.operationTime.toString());
		wp.orderBy(OrderReturnLogBo.Key.id.toString());
		List<OrderReturnLogBo> boList = orderReturnLogBiz.selectList(wp);
		return ControllerUtils.convertList(boList, OrderReturnLogVo.class);
	}

	@Override
	public List<MemberOrderReturnsVo> getMyReturns(Long memberId, List<ReturnsTypeEnum> returnsTypeList,
			List<ReturnsGoodsStatusEnum> statusList, List<String> orderFieldList, Integer start, Integer size,
			Boolean isReturnFinish, Long orderId) {
		if (Tools.collection.isEmpty(returnsTypeList)) {
			returnsTypeList = null;
		}
		if (Tools.collection.isEmpty(statusList)) {
			statusList = null;
		}
		if (Tools.collection.isEmpty(orderFieldList)) {
			orderFieldList = null;
		}
		if (Tools.number.nullIf(start, 0) <= 0) {
			start = Integer.valueOf(0);
		}
		if (Tools.number.nullIf(size, 0) == 0) {
			size = Integer.valueOf(20);
		}
		if (Tools.number.isEmpty(orderId)) {
			orderId = null;
		}
		List<ReturnsGoodsStatusEnum> closeStatusList = Arrays.asList(ReturnsGoodsStatusEnum.finish,
				ReturnsGoodsStatusEnum.failed);
		return orderReturnsDao.getMyReturns(memberId, returnsTypeList, statusList, orderFieldList, start, size,
				closeStatusList, isReturnFinish, orderId);
	}

	@Override
	public List<GoodsSkuVo> getSkuInfoByReturnsId(Long returnsId) {
		return orderReturnsDao.getSkuInfoByReturnsId(returnsId);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Boolean updateStatusAndSendGoods(OrderLogisticsBo logisbo, Long userId, Long returnsId) {
		// 通过shipid找到对应的运费模板
		ShipmentBo shipment = shipmentBiz.selectById(logisbo.getShipmentId());
		if (shipment != null) {
			// 设置物流名称
			logisbo.setLogisticsCompany(shipment.getName());
		}
		Boolean rs = orderLogisticsBiz.insert(logisbo);
		if (!rs) {
			return rs;
		}
		// 更新退货信息状态状态
		OrderReturnsBo returns = new OrderReturnsBo();
		returns.setId(returnsId);
		returns.setStatus(ReturnsGoodsStatusEnum.buyerWaitReceived);
		orderReturnsBiz.updateById(returns);
		// 记录操作日志
		SysUserBo user = sysUserBiz.selectById(userId);
		String userName = "";
		if (user != null) {
			userName = user.getName();
		}
		orderReturnLogService.createLog(logisbo.getOrderId(), userId,
				OperatorTypeEnum.wearhouse.getLabel()
						+ (Tools.string.isNotEmpty(userName) ? ("[" + userName + "]") : ""),
				ReturnsGoodsStatusEnum.buyerWaitReceived.getLabel(), "仓库管理员向买家发货。", true, returnsId,
				ReturnsGoodsStatusEnum.buyerWaitReceived, "");
		// 添加消息
		OrderReturnsVo vo = Tools.bean.cloneBean(returns, new OrderReturnsVo());
		this.addReturnGoodsMessage(userId, vo);
		//使用taskjob执行定时任务
		callUpdateReturnsOrderStatus(returnsId);
		return rs;
	}

	private void callUpdateReturnsOrderStatus(Long returnsId){
		//目前写死30天
		long delay = 30 * 24 * 60 * 60 * 1000;
		JobDataMap paramMap = new JobDataMap();
		paramMap.put("orderId", String.valueOf(returnsId));
		JobSchedulerFactory.callTaskJob(
				delay,
				UpdateReturnsOrderTaskJob.class,
				"UpdateReturnsOrderTaskJob",
				paramMap);
	}

	@Override
	public List<OrderReturnsDetailVo> getOrderReturnsDetailList(Long returnsId) {
		OrderReturnsDetailBo query = new OrderReturnsDetailBo();
		query.setReturnsId(returnsId);
		List<OrderReturnsDetailBo> orderDetailList = orderReturnsDetailBiz
				.selectList(new EntityWrapper<OrderReturnsDetailBo>(query));
		List<OrderReturnsDetailVo> rs = ControllerUtils.convertList(orderDetailList, OrderReturnsDetailVo.class);
		if (Tools.collection.isNotEmpty(rs)) {
			for (OrderReturnsDetailVo vo : rs) {
				// 处理图片为全路径
				if (Tools.string.isNotEmpty(vo.getImglistJson())) {
					List<String> imgList = Tools.json.toList(vo.getImglistJson(), String.class);
					if (Tools.collection.isNotEmpty(imgList)) {
						List<String> fullPathImgList = new ArrayList<>();
						for (String img : imgList) {
							fullPathImgList.add(QiniuUtil.getFullPath(img));
						}
						vo.setImgList(fullPathImgList);
					}
				}
				GoodsBo goods = goodsBiz.selectById(vo.getGoodsId());
				if (goods != null) {
					vo.setGoods(goods);
				}
				GoodsSkuDefBo skuVo = goodsSkuDefBiz.selectById(vo.getSkuId());
				if (skuVo != null) {
					skuVo.setImageUrl(QiniuUtil.getFullPath(skuVo.getImageUrl()));
					vo.setGoodsSkuDef(skuVo);
				}
			}
		}
		return rs;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Boolean updateForReturnShipment(MemberBo member, Long returnsId, String logisticsNo, String shipmentId) {
		OrderReturnsBo bo = orderReturnsBiz.selectById(returnsId);
		bo.setId(returnsId);
		bo.setLogisticsNo(logisticsNo);
		// 设置物流名称
		ShipmentBo shipment = shipmentBiz.selectById(shipmentId);
		if (shipment != null) {
			bo.setShipmentId(shipment.getId());
			bo.setLogisticsCompany(shipment.getName());
		}
		if (bo.getReturnsType() == ReturnsTypeEnum.ChangeGoods) {
			bo.setStatus(ReturnsGoodsStatusEnum.exchangeWaitReceived);
		} else if (bo.getReturnsType() == ReturnsTypeEnum.ReGoods) {
			bo.setStatus(ReturnsGoodsStatusEnum.refundWaitReceived);
		} else {
			throw new WakaException("退款不需要操作退货步骤");
		}

		Boolean rs = orderReturnsBiz.updateById(bo);
		if (rs) {
			String memberName = member.getFirstname() + " " + member.getLastname();
			String content = "用户[" + memberName + "]发送退品到仓库";
			// 记录申请日志
			orderReturnLogService.createLog(bo.getOrderId(), member.getId(), memberName, bo.getStatus().getLabel(),
					content, true, bo.getId(), bo.getStatus(), "");
		}
		return rs;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Boolean updateForConfirmReceipt(MemberBo member, Long returnsId) {
		OrderReturnsBo orderReturns = new OrderReturnsBo();
		orderReturns.setId(returnsId);
		orderReturns.setStatus(ReturnsGoodsStatusEnum.finish);
		orderReturns.setFinishTime(new Date());
		Boolean rs = orderReturnsBiz.updateById(orderReturns);
		if (rs) {
			OrderReturnsBo bo = orderReturnsBiz.selectById(returnsId);
			// 记录对应日志
			String memberName = member.getFirstname() + " " + member.getLastname();
			String content = "用户[" + memberName + "]确认收货";
			// 记录申请日志
			orderReturnLogService.createLog(bo.getOrderId(), member.getId(), memberName,
					ReturnsGoodsStatusEnum.exchangeSucess.getLabel(), content, true, bo.getId(),
					ReturnsGoodsStatusEnum.exchangeSucess, "");
			orderReturnLogService.createLog(bo.getOrderId(), member.getId(), memberName,
					ReturnsGoodsStatusEnum.finish.getLabel(), "交易完成", true, bo.getId(), ReturnsGoodsStatusEnum.finish,
					"");
		}
		return rs;
	}

	@Override
	public OrderReturnsVo getOrderReturnsInfo(Long returnsId) {
		return orderReturnsDao.getOrderReturnsInfo(returnsId);
	}

}