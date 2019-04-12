package cn.farwalker.ravv.order.returns;

import java.util.List;
import java.util.Map;

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

import cn.farwalker.ravv.service.base.area.biz.IBaseAreaBiz;
import cn.farwalker.ravv.service.base.area.model.BaseAreaBo;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsBiz;
import cn.farwalker.ravv.service.goodssku.skudef.biz.IGoodsSkuDefBiz;
import cn.farwalker.ravv.service.goodssku.skudef.biz.IGoodsSkuService;
import cn.farwalker.ravv.service.order.operationlog.biz.IOrderLogService;
import cn.farwalker.ravv.service.order.orderlogistics.biz.IOrderLogisticsBiz;
import cn.farwalker.ravv.service.order.orderlogistics.contants.LogisticsPaymentEnum;
import cn.farwalker.ravv.service.order.orderlogistics.contants.LogisticsStatusEnum;
import cn.farwalker.ravv.service.order.orderlogistics.contants.LogisticsTypeEnum;
import cn.farwalker.ravv.service.order.orderlogistics.model.OrderLogisticsBo;
import cn.farwalker.ravv.service.order.orderlogistics.model.OrderLogisticsVo;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnLogService;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsBiz;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsDetailBiz;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsService;
import cn.farwalker.ravv.service.order.returns.constants.OperatorTypeEnum;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsGoodsStatusEnum;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsBo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsDetailBo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsDetailVo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsVo;
import cn.farwalker.ravv.service.sys.user.biz.ISysUserBiz;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.base.controller.ControllerUtils;
import cn.farwalker.waka.util.Tools;

/**
 * 仓库订单退货<br/>
 * 仓库订单退货,只显示有发货的退货订单，没有发货时，直接退款了，跟仓库没有关系了<br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
@RestController
@RequestMapping("/order/returns/goods")
public class OrderReturnGoodsController {
	private final static Logger log = LoggerFactory.getLogger(OrderReturnGoodsController.class);
	@Resource
	private IOrderReturnsBiz orderReturnsBiz;

	@Resource
	private IOrderReturnsDetailBiz orderReturnsDetailBiz;

	@Resource
	private IOrderReturnsService orderReturnsService;

	@Resource
	private IOrderLogisticsBiz orderLogisticsBiz;


	@Resource
	private IBaseAreaBiz baseAreaBiz;

	protected IOrderReturnsBiz getBiz() {
		return orderReturnsBiz;
	}

	/**
	 * 退货申请审核
	 * 
	 * @param returnsid
	 *            退货单id<br/>
	 * @param state
	 *            审核状态<br/>
	 */
	@RequestMapping("/audi")
	public JsonResult<OrderReturnsBo> doAudi(Long returnsid, Boolean state) {
		// createMethodSinge创建方法
		if (returnsid == null) {
			return JsonResult.newFail("退货单id不能为空");
		}
		OrderReturnsBo rs = null;
		if (state == null) {
			return JsonResult.newFail("审核状态不能为空");
		}
		return JsonResult.newSuccess(rs);
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
	 * @param id
	 */
	@RequestMapping("/get")
	public JsonResult<OrderReturnsBo> doGet(@RequestParam Long id) {
		// createMethodSinge创建方法
		if (id == null) {
			return JsonResult.newFail("id不能为空");
		}
		OrderReturnsBo rs = getBiz().selectById(id);
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 列表记录
	 * 
	 * @param query 查询条件<br/>
	 * @param start 开始行号<br/>
	 * @param size 记录数<br/>
	 * @param sortfield 排序(+字段1,-字段名2)<br/>
	 */
	@RequestMapping("/list")
	public JsonResult<Page<OrderReturnsBo>> doList(Long userId, @RequestBody List<QueryFilter> query, Integer start, Integer size,
			String sortfield) {
		if (userId == null) {
			return JsonResult.newFail("当前用户id不能为空");
		}
		
		Page<OrderReturnsBo> page = ControllerUtils.getPage(start, size, sortfield);
		Wrapper<OrderReturnsBo> wrap = ControllerUtils.getWrapper(query);
		wrap.orderBy(OrderReturnsBo.Key.gmtCreate.toString(), false);
		
		String sql = "order_id in (SELECT order_id FROM order_goods WHERE goods_id in "
				+ "(SELECT id FROM goods WHERE merchant_id = '" + userId + "'))";
		
		wrap.where(sql);
		
		Page<OrderReturnsBo> rs = getBiz().selectPage(page, wrap);
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 修改记录
	 * @param vo
	 */
	@RequestMapping("/update")
	public JsonResult<?> doUpdate(@RequestBody OrderReturnsBo vo, Long userId) {
		// createMethodSinge创建方法
		if (vo == null) {
			return JsonResult.newFail("vo不能为空");
		}
		return orderReturnsService.updateOrderReturnsAndCreateLog(userId, OperatorTypeEnum.server, vo);
	}

	/**
	 * 仓库操作退货/换货记录
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping("/warehouse_operate_refund")
	public JsonResult<Boolean> warehouseOperateRefund(@RequestBody OrderReturnsVo vo, Long userId) {
		// createMethodSinge创建方法
		if (vo == null || vo.getId() == null || vo.getStatus() == null) {
			return JsonResult.newFail("退货单id不能为空");
		}
		OrderReturnsBo bo = orderReturnsBiz.selectById(vo.getId());
		if (bo == null) {
			return JsonResult.newFail("对应退货单不存在");
		}
		// 判断对应商品的是否已验收
		if(vo.getStatus() == ReturnsGoodsStatusEnum.exchangeAcceptanceReceived){
			OrderReturnsDetailBo query = new OrderReturnsDetailBo();
			query.setReturnsId(vo.getId());
			EntityWrapper<OrderReturnsDetailBo> wp = new EntityWrapper<OrderReturnsDetailBo>(query);
			wp.isNull(OrderReturnsDetailBo.Key.validesc.toString()).or(OrderReturnsDetailBo.Key.validesc+" ={0}", "");
			Integer unCheckCount = orderReturnsDetailBiz.selectCount(wp);
			if (!Tools.number.isEmpty(unCheckCount)) {
				return JsonResult.newFail("存在未验收退品");
			}
		}
		// 操作修改对应消息并添加操作记录
		vo.setOrderId(bo.getOrderId());
		return orderReturnsService.saveOperateOrderReturnsAndLog(userId, OperatorTypeEnum.wearhouse, vo,
				vo.getStatus());
	}

	/**
	 * 仓库验收退品
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping("/acceptance_returns_goods")
	@Transactional
	public JsonResult<Boolean> checkAndAcceptanceGoods(@RequestBody OrderReturnsVo vo, Long userId) {
		// createMethodSinge创建方法
		if (vo == null || vo.getOrderReturnsDetailId() == null) {
			return JsonResult.newFail("退详情id不能为空");
		}
		OrderReturnsBo bo = orderReturnsBiz.selectById(vo.getId());
		if (bo == null) {
			return JsonResult.newFail("对应退货单不存在");
		}
		// 操作修改对应消息并添加操作记录
		vo.setOrderId(bo.getOrderId());
		orderReturnsService.saveCheckAndAcceptanceGoods(userId, OperatorTypeEnum.wearhouse, vo);
		return JsonResult.newSuccess(true);
	}

	/**
	 * 获取可选状态列表
	 * 
	 * @param returnsid
	 * @return
	 */
	@RequestMapping("/get_warehouse_return_status")
	public JsonResult<List<Map<String, String>>> getReturnsGoodsStatus(Long returnsid) {
		// createMethodSinge创建方法
		if (returnsid == null) {
			return JsonResult.newFail("退货单id不能为空");
		}
		OrderReturnsBo bo = orderReturnsBiz.selectById(returnsid);
		List<Map<String, String>> rs = null;
		if (bo != null) {
			rs = orderReturnsService.getReturnsStatusByStatusForWarehouse(bo.getStatus());
			return JsonResult.newSuccess(rs);
		}
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 获取换货物流信息
	 * 
	 * @param returnsId
	 * @return
	 */
	@RequestMapping("/getlogisticsinfo")
	public JsonResult<OrderLogisticsBo> getLogisticsInfo(Long returnsId, Long orderId) {
		// createMethodSinge创建方法
		if (returnsId == null) {
			return JsonResult.newSuccess(null);
		}
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
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 订单发货
	 * 
	 * @param logisbo
	 * @param userId
	 * @return
	 */
	@RequestMapping("/sendgoods")
	@Transactional
	public JsonResult<Object> doSendGoods(@RequestBody OrderLogisticsBo logisbo, Long userId) {
		Long returnsId = logisbo.getReturnsId();
		if (logisbo == null || returnsId == null) {
			return JsonResult.newFail("退货id不能为空");
		} else if (logisbo.getShipmentId() == null) {
			return JsonResult.newFail("运费模板不能为空");
		}
		Boolean rs = orderReturnsService.updateStatusAndSendGoods(logisbo, userId, returnsId);
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 取得单条退货记录对应的退货记录
	 * 
	 * @param returnsId
	 * @return
	 */
	@RequestMapping("/returns_detail")
	public JsonResult<List<OrderReturnsDetailVo>> getReturnsDetail(@RequestParam Long returnsId) {
		// createMethodSinge创建方法
		if (returnsId == null) {
			return JsonResult.newFail("退货id不能为空");
		}
		List<OrderReturnsDetailVo> rs = orderReturnsService.getOrderReturnsDetailList(returnsId);
		return JsonResult.newSuccess(rs);
	}

}