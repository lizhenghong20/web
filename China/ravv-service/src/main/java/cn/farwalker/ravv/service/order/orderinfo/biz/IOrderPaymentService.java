package cn.farwalker.ravv.service.order.orderinfo.biz;

import cn.farwalker.ravv.service.order.constants.PaymentPlatformEnum;
import cn.farwalker.ravv.service.payment.paymentlog.model.MemberPaymentLogBo;
import cn.farwalker.ravv.service.paypal.PaymentForm;
import cn.farwalker.ravv.service.paypal.PaymentResultVo;
import cn.farwalker.waka.core.JsonResult;

import java.math.BigDecimal;


/**
 * 订单支付处理
 * @author Administrator
 *
 */
public interface IOrderPaymentService {

	/**
	 * 支付回调处理(支付成功时，需要更新库存)
	 * @param paymentNo 支付交易号
	 * @param success 
	 * @return 支付成功状态
	 */ 
	//public Boolean updateOrderAfterPay(String paymentNo,Boolean success);
	
	/**
	 * 支付回调处理(支付成功时，需要更新库存)
	 * @param orderId
	 * @param platform 支付方式
	 * @param paylogBo
	 * @return 处理成功
	 */
	public Boolean updateOrderAfterPay(Long orderId, PaymentPlatformEnum platform, MemberPaymentLogBo paylogBo);

	/**
	 * 执行支付处理，扣余额，写日志(不涉及第3方支付时，不需要回调，所以这个方法相当于支付成功后的回调）
	 * 取得当前用户余额
	 * @param orderId 订单号
	 * @param amt 支付金额
	 * @return
	 */
	public JsonResult<PaymentResultVo> updatePayFromAdvance(PaymentForm paymentForm);


	/**
	 * 判断当前订单是否能够被支付
	 * @param orderId
	 * @return
     */
	Boolean isOrderValidForPay(Long orderId);


}
