package cn.farwalker.ravv.service.order.orderinfo.biz;
import java.util.List;

import cn.farwalker.ravv.service.order.constants.PaymentPlatformEnum;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderStatusEnum;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.order.orderlogistics.model.OrderLogisticsBo;
import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;
import cn.farwalker.ravv.service.sys.user.model.SysUserBo;
import cn.farwalker.waka.core.JsonResult;

import javax.servlet.http.HttpSession;

/**
 * 订单信息<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IOrderInfoService {
	/**取消订单*/
	public OrderInfoBo updateCancelOrder(Long orderId);

	/**订单收货*/
	public OrderInfoBo updateOrderReceiver(Long orderId);

	public void callUpdateOrderStatus(Long orderId);
	
	/**
	 * 我的订单列表
	 * @param buyerId 会员id
	 * @param orderStatus 订单状态
	 * @param search 商品关键字
	 * @param lastMonth 最后的几个月(0，或者null不处理)
	 * @param search 商品关键字
	 * @param waitReview 查询未评论的订单
	 * @param afterSale 查询需要售后服务的订单
	 * @param start 开始行
	 * @param size 记录数
	 * @return 不会为null
	 */
	public List<OrderInfoBo> getMyOrderList(Long buyerId
			,List<OrderStatusEnum> orderStatus,String search
			,Integer lastMonth,Boolean waitReview, Boolean afterSale,
			List<String> sortfield,Integer start,Integer size);
	
	/**
	 * 仓库给订单发货
	 * @param logisbo
	 * @param userId
	 * @return
	 */
	public Boolean updateForWearhouseSendgoods(OrderLogisticsBo logisbo, Long userId, SysUserBo user);
	
	/**
	 * 修改订单金额信息
	 * @param paybo
	 * @param userId
	 * @return
	 */
	public Boolean updatePayment(OrderPaymemtBo paybo, Long userId, SysUserBo user);
	
	/**
	 * 修改信息
	 * @param logisbo
	 * @param userId
	 * @return
	 */
	public Boolean updateOrderLogistics(OrderLogisticsBo logisbo, Long userId, SysUserBo user);

	/**
	 * 是否支付成功
	 * @param session
	 * @param orderId
	 * @return
	 */
	Boolean isPaySuccess(HttpSession session, Long orderId);

	/**
	 * 从订单获取支付类型，是paypal 支付，还是wallet支付 null代表没支付
	 * @return
	 */
	PaymentPlatformEnum getPayType(Long orderId);

}