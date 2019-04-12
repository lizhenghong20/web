package cn.farwalker.ravv.service.order.time;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cangwu.frame.orm.core.BaseBo;

import cn.farwalker.ravv.service.order.operationlog.biz.IOrderLogService;
import cn.farwalker.ravv.service.order.ordergoods.biz.IOrderGoodsService;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoBiz;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInventoryService;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderStatusEnum;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderTypeEnum;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.order.orderlogistics.biz.IOrderLogisticsBiz;
import cn.farwalker.ravv.service.order.orderlogistics.model.OrderLogisticsBo;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnLogService;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsBiz;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsDetailBiz;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsGoodsStatusEnum;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsTypeEnum;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsBo;
import cn.farwalker.ravv.service.sale.profitallot.biz.ISaleProfitAllotBiz;
import cn.farwalker.ravv.service.sys.param.biz.ISysParamService;
import cn.farwalker.ravv.service.sys.param.biz.SysParamCache;
import cn.farwalker.waka.core.ITimer;
import cn.farwalker.waka.util.Tools;

@Component
public class OrderTaskTimer implements ITimer {

	private static final Logger log = LoggerFactory.getLogger(OrderTaskTimer.class);
	/** 每天的毫秒 */
	private static final int DAY = 1000 * 60 * 60 * 24;

	private static final String OperatorText = "系统自动关闭";
	private static final int OrderCloseDay = 7;
	@Resource
	private IOrderInfoBiz orderBiz;

	@Resource
	private IOrderGoodsService orderGoodsService;

	@Resource
	private IOrderInventoryService orderInfoService;

	@Resource
	private ISysParamService sysparamService;

	@Resource
	private IOrderReturnsBiz returnsBiz;
	@Resource
	private IOrderReturnsDetailBiz returnsDetailBiz;
	@Resource
	private IOrderLogisticsBiz logisticsBiz;

	@Resource
	private IOrderLogService orderLogService;

	@Resource
	private ISaleProfitAllotBiz saleProfitAllotBiz;

	@Resource
	private IOrderReturnLogService orderReturnLogService;

	// @Scheduled(fixedRate = DAY)
	/**
	 * 每天凌晨2点10分执行一次:更新订单关闭状态
	 */
	@Scheduled(cron = "0 10 2 * * ?")
	public void checkOrderState() {
		try {
			log.info("定时更新订单/退货单状态，每天执行一次:" + Tools.date.formatDate().toString());
			Integer days = sysparamService.getOrderCloseDay();
			int closeDay = Tools.number.nullIf(days, OrderCloseDay);
			Calendar closeDate = Calendar.getInstance();
			closeDate.add(Calendar.DATE, 0 - Math.abs(closeDay));

			// 定时关闭订单
//			this.timeCloseOrder(closeDate, closeDay);

			////////// 更新退货状态////////////
//			this.checkOrderReturns(closeDay);

		} catch (Exception e) {
			log.error("定时更新订单/退货单状态", e);
		}
	}

	/** 更新退货状态 */
	private void checkOrderReturns(int closeDay) {
		Calendar closeDate = Calendar.getInstance();
		closeDate.add(Calendar.DATE, 0 - Math.abs(closeDay));

		// 查找n天前的记录
		Wrapper<OrderReturnsBo> wp = new EntityWrapper<>();
		List<ReturnsGoodsStatusEnum> status = Arrays.asList(
				// ReturnsGoodsStatusEnum.exchangeWaitReceived //待卖方收货1 (手工收货)
				// ,ReturnsGoodsStatusEnum.refundWaitReceived //待卖方收货2 (手工收货)
				ReturnsGoodsStatusEnum.buyerWaitReceived // 待买家收货
		);
		// TODO 更新退货主单状态
		wp.in(OrderReturnsBo.Key.status.toString(), status);
		wp.le(OrderInfoBo.Key.gmtCreate.toString(), closeDate.getTime());
		List<OrderReturnsBo> returnsList = returnsBiz.selectList(wp);
		List<OrderLogisticsBo> logistcsBos = getReturnsLogisticsBos(returnsList);

		List<OrderReturnsBo> changeBos = new ArrayList<>();
		for (OrderReturnsBo bo : returnsList) {
			OrderLogisticsBo logBo = Tools.collection.getBo(logistcsBos, bo.getId(), "returnsId");
			if (logBo != null && logBo.getSendGoodsTime() != null && closeDate.after(logBo.getSendGoodsTime())) {
				OrderReturnsBo e = timerChangeReturnsStatus(bo);
				if (e != null) {
					changeBos.add(e);
				}
			}
		}
		if (Tools.collection.isNotEmpty(changeBos)) {
			returnsBiz.updateBatchById(changeBos);
		}
	}

	/** 定时修改退货单状态 */
	private OrderReturnsBo timerChangeReturnsStatus(OrderReturnsBo bo) {
		ReturnsGoodsStatusEnum status = bo.getStatus();
		if (status == ReturnsGoodsStatusEnum.buyerWaitReceived) {// 待买家收货
			bo.setStatus(ReturnsGoodsStatusEnum.finish);
			bo.setFinishTime(new Date());
			// 添加对应日志
			orderReturnLogService.createLog(bo.getOrderId(), null, "system",
					ReturnsGoodsStatusEnum.buyerWaitReceived.getLabel(), "换货成功", true, bo.getId(),
					ReturnsGoodsStatusEnum.buyerWaitReceived, "");
			orderReturnLogService.createLog(bo.getOrderId(), null, "system", ReturnsGoodsStatusEnum.finish.getLabel(),
					"交易完成", true, bo.getId(), ReturnsGoodsStatusEnum.finish, "");
			return bo;
		} else {
			return null;
		}
	}

	/** 取退货单的物流信息 */
	private List<OrderLogisticsBo> getReturnsLogisticsBos(List<OrderReturnsBo> returnsList) {
		List<Long> returnIds = new ArrayList<>();
		for (OrderReturnsBo e : returnsList) {
			returnIds.add(e.getId());
		}

		Wrapper<OrderLogisticsBo> wp = new EntityWrapper<>();
		wp.in(OrderLogisticsBo.Key.returnsId.toString(), returnIds);
		List<OrderLogisticsBo> rs = logisticsBiz.selectList(wp);
		return rs;
	}

	/**
	 * 判断没有支付的订单
	 * 
	 * @param orderBos
	 * @return true 表示可以关闭
	 */
	private List<OrderInfoBo> getOrderUnpaidState(List<OrderInfoBo> orderBos, int closeDay) {
		List<OrderInfoBo> changeStates = new ArrayList<>(orderBos.size());
		final String logtext = "没有支付的订单(" + closeDay + ")天自动关闭";
		for (OrderInfoBo bo : orderBos) {
			OrderStatusEnum st = bo.getOrderStatus();// st== OrderStatusEnum.CREATED_UNREVIEW ||
			if (st == OrderStatusEnum.REVIEWADOPT_UNPAID) {
				bo.setOrderStatus(OrderStatusEnum.CANCEL);
				bo.setOrderFinishedTime(new Date());
				changeStates.add(bo);// 没有支付的订单
				orderLogService.createLog(bo.getId(), OperatorText, OperatorText, logtext);
			}
		}
		return changeStates;
	}

	/**
	 * 定时关闭订单
	 * 
	 * @param closeDate
	 *            当前时间减去系统设置的订单关闭天数的Calendar
	 * @param closeDay
	 *            系统设置的订单关闭时间
	 */
	private void timeCloseOrder(Calendar closeDate, int closeDay) {
		// 查找系统设置的订单关闭天数前的订单记录
		Wrapper<OrderInfoBo> wporder = new EntityWrapper<>();
		List<OrderStatusEnum> status = Arrays.asList(// OrderStatusEnum.CREATED_UNREVIEW,
				OrderStatusEnum.REVIEWADOPT_UNPAID, OrderStatusEnum.SENDGOODS_UNCONFIRM, OrderStatusEnum.SING_GOODS);
		wporder.in(OrderInfoBo.Key.orderStatus.toString(), status);
		wporder.le(OrderInfoBo.Key.gmtCreate.toString(), closeDate.getTime());

		List<OrderInfoBo> orderBos = orderBiz.selectList(wporder);

		if (Tools.collection.isEmpty(orderBos)) {
			return;
		}

		List<OrderInfoBo> singOrderBos = getOrderSendGoodsState(orderBos, closeDay);
		List<OrderInfoBo> unpaidOrderBos = getOrderUnpaidState(orderBos, closeDay);
		singOrderBos.addAll(unpaidOrderBos);

		if (Tools.collection.isEmpty(singOrderBos)) {
			return;
		}

		// 批量更新订单状态
		Boolean rs = orderBiz.updateBatchById(singOrderBos);

		if (rs) {
			// 非有拆单主单的订单id列表
			List<OrderInfoBo> orderInfoList = new ArrayList<>();

			for (OrderInfoBo bo : singOrderBos) {
				if (!bo.getOrderType().equals(OrderTypeEnum.MASTER)) {
					orderInfoList.add(bo);
				}
			}

			// 订单关闭后更新最终分销金额
			if (Tools.collection.isNotEmpty(orderInfoList)) {
				saleProfitAllotBiz.updateFinalProfit(orderInfoList);
			}
		}
	}

	/**
	 * 已发货处理 判断指定订单，并关闭符合条件的订单 定时器不管仅退款
	 * 
	 * @param orderBos
	 */
	public void closeOrderForReturn(List<OrderInfoBo> orderBos) {
		if (Tools.collection.isEmpty(orderBos)) {
			return;
		}
		// 已发货和已收货的订单
		List<OrderInfoBo> orderInfoList = new ArrayList<>();
		for (OrderInfoBo bo : orderBos) {
			OrderStatusEnum st = bo.getOrderStatus();
			if (st == OrderStatusEnum.SENDGOODS_UNCONFIRM || st == OrderStatusEnum.SING_GOODS
					|| st == OrderStatusEnum.PAID_UNSENDGOODS) {
				orderInfoList.add(bo);
			}
		}
		// 获取传入订单列表中可关闭的订单
		List<OrderInfoBo> closeOrderList = this.closePaidUnsendOrder(orderInfoList);

		// 批量更新订单状态
		if (Tools.collection.isNotEmpty(closeOrderList)) {
			orderBiz.updateBatchById(closeOrderList);
		}
	}

	// 未发货 处理仅退款
	private List<OrderInfoBo> closePaidUnsendOrder(List<OrderInfoBo> orderInfoList) {
		List<OrderInfoBo> closeOrderList = new ArrayList<>();

		for (OrderInfoBo orderInfo : orderInfoList) {
			// 获取指定订单的所有退换货单
			List<OrderReturnsBo> orderReturnsList = returnsBiz.returnsByOrderId(orderInfo.getId());

			if (Tools.collection.isNotEmpty(orderReturnsList)) {
				Boolean isHasReturns = true;
				Integer returnsGoodsNum = 0;// 退换货成功的商品数量初始化
				for (OrderReturnsBo returns : orderReturnsList) {
					if (!ReturnsGoodsStatusEnum.allowOrderClose(returns.getStatus())) {
						isHasReturns = false;
						continue;
					}
					// 获取退换货成功的商品数量(换货商品不计算) 不计算未完成的数量
					if (returns.getReturnsType() != ReturnsTypeEnum.ChangeGoods) {
						Integer succeedReGoodsNum = returnsDetailBiz.succeedReGoodsNum(returns.getId());
						returnsGoodsNum += succeedReGoodsNum;
					}
				}
				if (isHasReturns) {
					// 单个订单所有商品数量总和
					Integer orderGoodsTotal = orderGoodsService.getOrderGoodsTotal(orderInfo.getId());
					// 退换货成功的商品数量等于单个订单所有商品数量总和，则订单状态给为“交易取消”
					if (returnsGoodsNum == orderGoodsTotal) {
						orderInfo.setOrderStatus(OrderStatusEnum.CANCEL);
						orderLogService.createLog(orderInfo.getId(), OperatorText, OperatorText, "订单已全部退款，订单关闭");
						orderInfo.setOrderFinishedTime(new Date());
						closeOrderList.add(orderInfo);
					}
					//如果退货商品不等于订单个数，判断订单是否关闭，如果关闭且所有退换单已执行完毕,执行分润；否则不执行
					else {
						if(OrderStatusEnum.TRADE_CLOSE.equals(orderInfo.getOrderStatus())){
							saleProfitAllotBiz.updateFinalProfit(orderInfoList);
						}
					}
				}
			}
		}
		return closeOrderList;
	}

	/**
	 * 判断已发货的订单（需要检查下面的逻辑 2019-01-04 juno） 关闭逻辑：<br/>
	 * 1、按发货时间7天后关闭，如果有点击收货，就按收货时间7天后关闭
	 * 2、关闭要检查有没有发生退货、换货的操作，如果有就按退换货结束时间计算（退换货有成功、或者拒绝的，所以要详细判断）
	 * 3、如果全部退货后，订单状态要变成cancel
	 * 
	 * @param orderBos
	 * @return true 表示可以关闭
	 */
	private List<OrderInfoBo> getOrderSendGoodsState(List<OrderInfoBo> orderBos, int closeDay) {
		List<Long> orderIds = new ArrayList<>(orderBos.size());
		// 已发货和已收货的订单
		List<OrderInfoBo> orderInfoList = new ArrayList<>();
		for (OrderInfoBo bo : orderBos) {
			OrderStatusEnum st = bo.getOrderStatus();
			if (st == OrderStatusEnum.SENDGOODS_UNCONFIRM || st == OrderStatusEnum.SING_GOODS) {
				orderIds.add(bo.getId());
				orderInfoList.add(bo);
			}
		}

		if (Tools.collection.isEmpty(orderInfoList)) {
			return null;
		}

		// 检查订单有无退货、换货的操作
		List<OrderInfoBo> closeOrderList = new ArrayList<>();
		for (OrderInfoBo orderInfo : orderInfoList) {
			// 获取指定订单的所有退换货单
			List<OrderReturnsBo> orderReturnsList = returnsBiz.returnsByOrderId(orderInfo.getId());
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
								"已发货、签收的订单(" + closeDay + ")天自动取消");
					} else {
						orderInfo.setOrderStatus(OrderStatusEnum.TRADE_CLOSE);
						orderLogService.createLog(orderInfo.getId(), OperatorText, OperatorText,
								"已发货、签收的订单(" + closeDay + ")天自动关闭");
					}
					orderInfo.setOrderFinishedTime(new Date());
					closeOrderList.add(orderInfo);
				}
			} else {
				orderInfo.setOrderStatus(OrderStatusEnum.TRADE_CLOSE);
				closeOrderList.add(orderInfo);
				orderLogService.createLog(orderInfo.getId(), OperatorText, OperatorText,
						"已发货、签收的订单(" + closeDay + ")天自动关闭");
			}
		}

		return closeOrderList;

		// 下方代码为订单物流影响订单关闭实现，目前订单物流只做展示用（chensl）

		// Wrapper<OrderLogisticsBo> wp = new EntityWrapper<>();
		// // 最后时间
		// wp.setSqlSelect(OrderLogisticsBo.Key.orderId.toString() + " as " +
		// OrderLogisticsBo.Key.orderId.name(),
		// "max(" + OrderLogisticsBo.Key.gmtCreate.toString() + ") as " +
		// OrderLogisticsBo.Key.gmtCreate.name());
		// wp.in(OrderLogisticsBo.Key.orderId.toString(), orderIds);
		// wp.groupBy(OrderLogisticsBo.Key.orderId.toString());
		//
		// List<OrderLogisticsBo> logistbos = logisticsBiz.selectList(wp);
		// if (logistbos == null) {
		// return new ArrayList<>();
		// }
		//
		// Calendar closeDate = Calendar.getInstance();
		// closeDate.add(Calendar.DATE, 0 - Math.abs(closeDay));
		//
		// List<OrderInfoBo> rs = new ArrayList<>(logistbos.size());
		// final String logtext = "已发货、签收的订单(" + closeDay + ")天自动关闭";
		//
		// for (OrderLogisticsBo lo : logistbos) {
		// Date createDate = lo.getGmtCreate();
		// if (closeDate.after(createDate)) {
		// OrderInfoBo bo = getBo(orderBos, lo.getOrderId());
		// rs.add(bo);
		//
		// orderLogBiz.createLog(bo.getId(), OperatorText, OperatorText, logtext);
		// }
		// }
		// return rs;
	}

	/**
	 * 添加定时解除冻结库存
	 * 
	 * @param orderId
	 *            如果是拆单，就要是主单
	 */
	public void addOrderUnfreeze(Long orderId) {
		int delay = SysParamCache.getUnfreezeCacheDelay(sysparamService);
		// delay=1;//调试
		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					log.info("解除冻结库存:" + orderId);
					orderInfoService.updateOrderUnfreeze(orderId);
				} catch (Exception e) {
					log.error("解除冻结库存出错:", e);
				}
			}
		};

		Tools.thread.runSingle(task, "orderUnfreeze", delay);
	}

	private <T extends BaseBo> T getBo(List<T> bos, Long id) {
		T rs = null;
		for (T t : bos) {
			if (t.getId().equals(id)) {
				rs = t;
				break;
			}
		}
		return rs;
	}
}