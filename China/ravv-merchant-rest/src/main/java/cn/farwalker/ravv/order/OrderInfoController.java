package cn.farwalker.ravv.order;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.web.crud.QueryFilter;

import cn.farwalker.ravv.service.base.storehouse.biz.IStorehouseService;
import cn.farwalker.ravv.service.order.operationlog.biz.IOrderLogService;
import cn.farwalker.ravv.service.order.operationlog.biz.IOrderOperationLogBiz;
import cn.farwalker.ravv.service.order.ordergoods.biz.IOrderGoodsBiz;
import cn.farwalker.ravv.service.order.ordergoods.biz.IOrderGoodsService;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoBiz;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoService;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderStatusEnum;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.order.orderlogistics.biz.IOrderLogisticsBiz;
import cn.farwalker.ravv.service.order.orderlogistics.model.OrderLogisticsBo;
import cn.farwalker.ravv.service.order.paymemt.biz.IOrderPaymemtBiz;
import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;
import cn.farwalker.ravv.service.sys.message.biz.ISystemMessageService;
import cn.farwalker.ravv.service.sys.user.biz.ISysUserBiz;
import cn.farwalker.ravv.service.sys.user.model.SysUserBo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.base.controller.ControllerUtils;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;

/**
 * 订单信息<br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
@RestController
@RequestMapping("/order/orderinfo")
public class OrderInfoController {
	private final static Logger log = LoggerFactory.getLogger(OrderInfoController.class);
	@Resource
	private IStorehouseService storeHouseService;

	@Resource
	private IOrderPaymemtBiz orderPaymemtBiz;

	@Resource
	private IOrderGoodsBiz orderGoodsBiz;

	@Resource
	private IOrderGoodsService orderGoodsService;

	@Resource
	private IOrderInfoBiz orderInfoBiz;

	@Resource
	private IOrderInfoService orderInfoService;

	@Resource
	private IOrderLogisticsBiz orderLogisticsBiz;

	@Resource
	private IOrderOperationLogBiz orderOperationLogBiz;

	@Resource
	private IOrderLogService orderLogService;

	@Resource
	private ISysUserBiz sysUserBiz;

	@Resource
	private ISystemMessageService systemMessageService;

	protected IOrderInfoBiz getBiz() {
		return orderInfoBiz;
	}

	/**
	 * 删除记录
	 * 
	 * @param id
	 *            null<br/>
	 */
	@RequestMapping("/delete")
	public JsonResult<Boolean> doDelete(@RequestParam Long id) {
		// createMethodSinge创建方法
		if (id == null) {
			return JsonResult.newFail("id不能为空");
		}
		Boolean rs = getBiz().deleteById(id);
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 取得单条记录
	 * 
	 * @param id
	 *            null<br/>
	 */
	@RequestMapping("/get")
	public JsonResult<OrderInfoBo> doGet(@RequestParam Long id) {
		// createMethodSinge创建方法
		if (id == null) {
			return JsonResult.newFail("id不能为空");
		}
		OrderInfoBo rs = getBiz().selectById(id);
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 列表记录
	 * 
	 * @param userid 当前用户id<br/>
	 * @param query 查询条件<br/>
	 * @param start 开始行号<br/>
	 * @param size 记录数<br/>
	 * @param sortfield 排序(+字段1,-字段名2)<br/>
	 */
	@RequestMapping("/list")
	public JsonResult<Page<OrderInfoBo>> doList(Long userid, @RequestBody List<QueryFilter> query, Integer start,
			Integer size, String sortfield) {
		if (userid == null) {
			return JsonResult.newFail("当前用户id不能为空");
		}

		Page<OrderInfoBo> page = ControllerUtils.getPage(start, size, sortfield);
		Wrapper<OrderInfoBo> wrap = ControllerUtils.getWrapper(query);
		if (wrap == null) {
			wrap = new EntityWrapper<>();
		}
		
		String sql = "id in (select o.id from order_info o inner join order_paymemt p on o.id = p.order_id where (p.pay_status ='PAID' or p.pay_status ='REFUND') "
				+ "and exists (select og.id from order_goods og inner join goods g on g.id = og.goods_id where o.id = og.order_id  and g.merchant_id = '"
				+ userid + "'))";
		
		wrap.where(sql);
		
		wrap.orderBy(OrderInfoBo.Key.gmtModified.toString(), false);
		Page<OrderInfoBo> rs = getBiz().selectPage(page, wrap);
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 订单发货详情
	 */
	@RequestMapping("/getlogisticsinfo")
	public JsonResult<OrderLogisticsBo> getLogisticsInfo(@RequestParam Long orderId) {
		// createMethodSinge创建方法
		if (orderId == null) {
			return JsonResult.newFail("订单id不能为空");
		}
		OrderLogisticsBo query = new OrderLogisticsBo();
		query.setOrderId(orderId);
		// 退货id，默认值为0，如果不是0表示退货
		query.setReturnsId(0l);
		OrderLogisticsBo rs = orderLogisticsBiz.selectOne(new EntityWrapper<OrderLogisticsBo>(query));
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 退单发货详情
	 */
	@RequestMapping("/returnslogisticsinfo")
	public JsonResult<OrderLogisticsBo> getlogisticsByReturnsId(@RequestParam Long returnsId) {
		// createMethodSinge创建方法
		if (returnsId == null) {
			return JsonResult.newFail("退单id不能为空");
		}
		OrderLogisticsBo query = new OrderLogisticsBo();
		query.setReturnsId(returnsId);
		OrderLogisticsBo rs = orderLogisticsBiz.selectOne(new EntityWrapper<OrderLogisticsBo>(query));
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 发货（非仓库管理员操作发货）
	 * 
	 * @param logisbo
	 * @return
	 */
	@RequestMapping("/sendgoods")
	@Transactional
	public JsonResult<Object> doSendGoods(@RequestBody OrderLogisticsBo logisbo, Long userId) {
		Long orderId = logisbo.getOrderId();
		if (logisbo == null || logisbo.getOrderId() == null) {
			return JsonResult.newFail("订单id不能为空");
		}
		OrderStatusEnum type = OrderStatusEnum.SENDGOODS_UNCONFIRM;
		Object rs = orderLogisticsBiz.insert(logisbo);

		// 更新订单状态
		OrderInfoBo order = new OrderInfoBo();
		order.setId(orderId);
		order.setOrderStatus(type);
		this.getBiz().updateById(order);

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
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 修改物流信息
	 * 
	 * @param orderid
	 *            订单id<br/>
	 */
	@RequestMapping("/updatelogistics")
	@Transactional
	public JsonResult<Object> updateOrderLogistics(@RequestBody OrderLogisticsBo logisbo, Long userId) {
		// createMethodSinge创建方法

		if (logisbo == null || logisbo.getId() == null) {
			return JsonResult.newFail("物流单id不能为空");
		}
		// TODO 物流信息不能随便修改
		SysUserBo user = sysUserBiz.selectById(userId);
		Boolean rs = orderInfoService.updateOrderLogistics(logisbo, userId, user);
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 仓管发货
	 */
	@RequestMapping("/wearhouse_sendgoods")
	@Transactional
	public JsonResult<Object> wearhouseSendgoods(@RequestBody OrderLogisticsBo logisbo, Long userId) {
		// createMethodSinge创建方法
		if (logisbo == null || logisbo.getId() == null) {
			return JsonResult.newFail("物流单id不能为空");
		}
		SysUserBo user = sysUserBiz.selectById(userId);
		Boolean rs = orderInfoService.updateForWearhouseSendgoods(logisbo, userId, user);
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 修改记录
	 * 
	 * @param vo
	 *            订单信息<br/>
	 */
	@RequestMapping("/update")
	@Transactional
	public JsonResult<?> doUpdate(@RequestBody OrderInfoBo vo, Long userId) {
		// createMethodSinge创建方法
		if (vo == null) {
			return JsonResult.newFail("vo不能为空");
		}
		OrderInfoBo v2 = getBiz().selectById(vo.getId());
		getBiz().updateById(v2);
		Object rs = getBiz().updateById(vo);
		// 操作日志
		SysUserBo user = sysUserBiz.selectById(userId);
		String userName = "";
		if (user != null) {
			userName = user.getName();
		}
		orderLogService.createLog(vo.getId(), userId, userName, "修改", "修改订单信息。", true);
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 获取订单商品
	 */
	@RequestMapping("/ordergoods")
	public JsonResult<List<OrderGoodsBo>> getOrderGoods(Long orderId) {
		// createMethodSinge创建方法
		if (orderId == null) {
			return JsonResult.newFail("订单id不能为空");
		}
		OrderGoodsBo query = new OrderGoodsBo();
		query.setOrderId(orderId);
		List<OrderGoodsBo> rs = orderGoodsBiz.selectList(new EntityWrapper<OrderGoodsBo>(query));

		for (OrderGoodsBo bo : rs) {
			// 去图片相对路径
			bo.setImgMajor(QiniuUtil.getFullPath(bo.getImgMajor()));
		}
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 修改订单金额信息
	 * 
	 * @param goods
	 *            订单金额信息<br/>
	 */
	@RequestMapping("/updategoods")
	public JsonResult<Boolean> doUpdateGoods(OrderGoodsBo goods) {
		// createMethodSinge创建方法
		if (goods == null) {
			return JsonResult.newFail("订单金额信息不能为空");
		}
		Boolean rs = null;
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 取得订单金额信息
	 * 
	 * @param orderid
	 *            订单id<br/>
	 */
	@RequestMapping("/payment")
	public JsonResult<OrderPaymemtBo> getPayment(@RequestParam Long orderid) {
		// createMethodSinge创建方法
		if (orderid == null) {
			return JsonResult.newFail("订单id不能为空");
		}
		OrderPaymemtBo query = new OrderPaymemtBo();
		query.setOrderId(orderid);
		EntityWrapper<OrderPaymemtBo> wp = new EntityWrapper<OrderPaymemtBo>(query);
		wp.last("LIMIT 1");
		OrderPaymemtBo rs = orderPaymemtBiz.selectOne(wp);
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 修改订单金额信息
	 * 
	 * @param paybo
	 *            订单金额信息<br/>
	 */
	@RequestMapping("/updatepayment")
	@Transactional
	public JsonResult<Boolean> doUpdatePayment(@RequestBody OrderPaymemtBo paybo, Long userId) {
		// createMethodSinge创建方法
		if (paybo == null) {
			return JsonResult.newFail("订单金额信息不能为空");
		}
		if (paybo.getId() == null) {
			return JsonResult.newFail("订单金额信息id不能为空");
		}
		SysUserBo user = sysUserBiz.selectById(userId);
		Boolean rs = orderInfoService.updatePayment(paybo, userId, user);
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 修改订单商品
	 * 
	 * @param orderId
	 *            商品ID<br/>
	 * @param prices
	 *            库存及单价信息<br/>
	 */
	@RequestMapping("/update_ordergoods")
	@Transactional
	public JsonResult<Boolean> doUpdateOrderGoods(Long orderId, @RequestBody List<OrderGoodsBo> orderGoods,
			Long userId) {
		// createMethodSinge创建方法
		if (orderId == null) {
			return JsonResult.newFail("商品ID不能为空");
		}
		if (Tools.collection.isEmpty(orderGoods)) {
			return JsonResult.newFail("库存及单价信息不能为空");
		}
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
		return JsonResult.newSuccess(true);
	}

	/**
	 * 获取订单操作日志
	 */
	@RequestMapping("/order_operationLog")
	public JsonResult<Boolean> orderOperationLogList(Long orderId) {
		if (orderId == null) {
			return JsonResult.newFail("商品ID不能为空");
		}
		return JsonResult.newSuccess(true);
	}

}