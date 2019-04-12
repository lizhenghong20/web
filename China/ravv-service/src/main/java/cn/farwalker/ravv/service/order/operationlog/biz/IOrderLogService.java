package cn.farwalker.ravv.service.order.operationlog.biz;

import cn.farwalker.ravv.service.order.operationlog.model.OrderOperationLogBo;

/**
 * 订单操作日志<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IOrderLogService {
	/**
	 * @param orderId  
	 * @param operatorId 操作人ID（可以null）
	 * @param operatorName 操作人姓名 
	 * @param behavior 操作行为（例如：订单支付、订单审核、订单发货... 太多的行为就不适用枚举了）
	 * @param content 操作内容
	 * @param success 是否成功
	 * @return
	 */ 
	public OrderOperationLogBo createLog(Long orderId,Long operatorId,String operatorName ,String behavior,String content,Boolean success) ;
	/**
	 * 订单日志
	 * @param orderId 订单id
	 * @param operatorName 操作人姓名
	 * @param behavior 操作行为（例如：订单支付、订单审核、订单发货... 太多的行为就不适用枚举了）
	 * @param content createLog
	 * @return
	 */ 
	public OrderOperationLogBo createLog(Long orderId, String operatorName ,String behavior,String content);
}