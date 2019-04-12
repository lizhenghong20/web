package cn.farwalker.ravv.service.order.operationlog.biz.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.farwalker.ravv.service.order.operationlog.biz.IOrderLogService;
import cn.farwalker.ravv.service.order.operationlog.biz.IOrderOperationLogBiz;
import cn.farwalker.ravv.service.order.operationlog.model.OrderOperationLogBo;
import cn.farwalker.waka.util.Tools;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderLogServiceImpl implements IOrderLogService{
	@Resource
	private IOrderOperationLogBiz logBiz;
	
	/**
	 * @param orderId  
	 * @param operatorId 操作人ID（可以null）
	 * @param operatorName 操作人姓名 
	 * @param behavior 操作行为（例如：订单支付、订单审核、订单发货... 太多的行为就不适用枚举了）
	 * @param content 操作内容
	 * @param success 是否成功
	 * @return
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public OrderOperationLogBo createLog(Long orderId,Long operatorId,String operatorName ,String behavior,String content,Boolean success) {
		return this.createOrderLog(orderId, operatorId, operatorName, behavior, content, success);
	}
	/**
	 * 订单日志
	 * @param orderId 订单id
	 * @param operatorName 操作人姓名
	 * @param behavior 操作行为（例如：订单支付、订单审核、订单发货... 太多的行为就不适用枚举了）
	 * @param content createLog
	 * @return
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public OrderOperationLogBo createLog(Long orderId, String operatorName ,String behavior,String content) {
		return this.createOrderLog(orderId, null, operatorName, behavior, content, null);
	}

	private OrderOperationLogBo createOrderLog(Long orderId,Long operatorId,String operatorName ,String behavior,String content,Boolean success) {
		OrderOperationLogBo bo = new OrderOperationLogBo();
		bo.setBehavior(behavior);
		bo.setLogText(content);
		bo.setOperationTime(new Date());
		bo.setOperatorId(operatorId);
		bo.setOperatorName(operatorName);
		bo.setOrderId(orderId);
		
		String result = "";
		if(success == null){
			result ="";
		}
		else if(success.booleanValue()){
			result="成功";
		}
		else{
			result="失败";
		}
		bo.setResult(result);
		Tools.thread.runSingle(new Runnable() { 
			@Override
			public void run() {
				logBiz.insert(bo);
			}
		}, "OrderOperationLogBo");
		return bo;
	}
}
