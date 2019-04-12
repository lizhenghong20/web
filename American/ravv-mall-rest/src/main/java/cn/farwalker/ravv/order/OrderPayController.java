package cn.farwalker.ravv.order;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.farwalker.ravv.service.member.basememeber.biz.IMemberBiz;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderPaymentService;

/**
 * 订单支付(不涉及第3方支付时，由前端调用,例如余额支付、积分支付)
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/order/pay")
public class OrderPayController{
	 @Resource
	 private IMemberBiz memberBiz;
	 
	 @Resource
	 private IOrderPaymentService orderPaymentService;
	/**
	 * 执行支付处理，扣余额，写日志(不涉及第3方支付时，不需要回调，所以这个方法相当于支付成功后的回调）
	 * 取得当前用户余额 {@link OrderMyController#getAdvance()}
	 * @param orderId 订单号
	 * @param amt 支付金额
	 * @return
	 */
//	@RequestMapping("/paying")
//	public JsonResult<Boolean> doPaying(Long orderId, BigDecimal amt) {
//		return orderPaymentService.payFromAdvance(orderId,amt);
//
//	}
}
