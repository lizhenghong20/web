package cn.farwalker.ravv.service.order.returns.biz.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnLogBiz;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnLogService;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsGoodsStatusEnum;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnLogBo;
import cn.farwalker.ravv.service.sys.message.biz.ISystemMessageService;
import cn.farwalker.waka.util.Tools;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderReturnLogServiceImpl implements IOrderReturnLogService {
	@Resource
	private IOrderReturnLogBiz logBiz;
	
	@Resource
	private ISystemMessageService systemMessageService;

	/**
	 * @param orderId
	 * @param operatorId
	 *            操作人ID（可以null）
	 * @param operatorName
	 *            操作人姓名
	 * @param behavior
	 *            操作行为（例如：订单支付、订单审核、订单发货... 太多的行为就不适用枚举了）
	 * @param content
	 *            操作内容
	 * @param success
	 *            是否成功
	 * @return
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public OrderReturnLogBo createLog(Long orderId, Long operatorId, String operatorName, String behavior,
			String content, Boolean success, Long returnsId, ReturnsGoodsStatusEnum returnStatus, String remark) {
		return this.createOrderLog(orderId, operatorId, operatorName, behavior, content, success, returnsId,
				returnStatus, remark);
	}

	private OrderReturnLogBo createOrderLog(Long orderId, Long operatorId, String operatorName, String behavior,
			String content, Boolean success, Long returnsId, ReturnsGoodsStatusEnum returnStatus, String remark) {
		OrderReturnLogBo bo = new OrderReturnLogBo();
		bo.setBehavior(behavior);
		bo.setLogText(content);
		bo.setOperationTime(new Date());
		bo.setOperatorId(operatorId);
		bo.setOperatorName(operatorName);
		bo.setOrderId(orderId);
		bo.setReturnId(returnsId);
		bo.setReturnStatus(returnStatus);
		bo.setRemark(remark);
		String result = "";
		if (success == null) {
			result = "";
		} else if (success.booleanValue()) {
			result = "成功";
		} else {
			result = "失败";
		}
		bo.setResult(result);
		Tools.thread.runSingle(new Runnable() {
			@Override
			public void run() {
				logBiz.insert(bo);
			}
		}, "OrderReturnLogBo");
		return bo;
	}
}
