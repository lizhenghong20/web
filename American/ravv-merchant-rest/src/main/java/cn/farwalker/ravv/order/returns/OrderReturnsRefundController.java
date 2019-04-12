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

import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsBiz;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsService;
import cn.farwalker.ravv.service.order.returns.constants.OperatorTypeEnum;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsGoodsStatusEnum;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsBo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsVo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.util.Tools;

/**
 * 财务订单退款<br/>
 * 财务订单退款，有发货时，仓库收到退货后才能退款，没有发货时，直接退款<br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
@RestController
@RequestMapping("/order/returns/refund")
public class OrderReturnsRefundController extends OrderReturnGoodsController {
	private final static Logger log = LoggerFactory.getLogger(OrderReturnsRefundController.class);
	@Resource
	private IOrderReturnsBiz orderReturnsBiz;

	@Resource
	private IOrderReturnsService orderReturnsService;
	/*
	 * @Resource private IOrderReturnsBiz orderReturnsBiz; protected
	 * IOrderReturnsBiz getBiz(){ return orderReturnsBiz; }
	 */

	/**
	 * 订单退款
	 * 
	 * @param returnsid 退货单id<br/>
	 */
	@RequestMapping("/reundpay")
	public JsonResult<Boolean> doReundPay(@RequestBody OrderReturnsBo returnsBo, Long userId) {
		// createMethodSinge创建方法
		if(returnsBo == null) {
			return JsonResult.newFail("退货单不能为空");
		}
		if (returnsBo.getId() == null) {
			return JsonResult.newFail("退货单id不能为空");
		}
		OrderReturnsBo bo = orderReturnsBiz.selectById(returnsBo.getId());
		if (bo == null) {
			return JsonResult.newFail("对应退货单不存在");
		}
		bo.setAdjustFee(returnsBo.getAdjustFee());//实退金额
		OrderReturnsVo vo = Tools.bean.cloneBean(bo, new OrderReturnsVo());
		//vo.setStatus(ReturnsGoodsStatusEnum.refundSucess);
		// 操作修改对应消息并添加操作记录
		return orderReturnsService.saveOperateOrderReturnsAndLog(userId, OperatorTypeEnum.server, vo,ReturnsGoodsStatusEnum.refundSucess);
	}

	/**
	 * 客服操作退货/换货记录
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping("/service_operate_refund")
	@Transactional
	public JsonResult<Boolean> serviceOperateRefund(@RequestBody OrderReturnsVo vo, Long userId) {
		// createMethodSinge创建方法
		if (vo == null || vo.getId() == null || vo.getStatus() == null) {
			return JsonResult.newFail("退货单id不能为空");
		}
		OrderReturnsBo bo = orderReturnsBiz.selectById(vo.getId());
		if (bo == null) {
			return JsonResult.newFail("对应退货单不存在");
		}
		vo.setOrderId(bo.getOrderId());
		vo.setReturnsType(bo.getReturnsType());
		// 操作修改对应消息并添加操作记录
		return orderReturnsService.saveOperateOrderReturnsAndLog(userId, OperatorTypeEnum.server, vo,vo.getStatus());
	}

	/**
	 * 获取当前可选状态
	 * 
	 * @param returnsid
	 * @return
	 */
	@RequestMapping("/get_return_status_enum")
	public JsonResult<List<Map<String, String>>> getReturnsStatus(Long returnsid) {
		// createMethodSinge创建方法
		if (returnsid == null) {
			return JsonResult.newFail("退货单id不能为空");
		}
		OrderReturnsBo bo = orderReturnsBiz.selectById(returnsid);
		List<Map<String, String>> rs = null;
		if (bo != null) {
			rs = orderReturnsService.getReturnsStatusByStatusForService(bo.getStatus());
			return JsonResult.newSuccess(rs);
		}
		return JsonResult.newSuccess(rs);
	}
}