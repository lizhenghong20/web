package cn.farwalker.ravv.service.order.paymemt.biz;

import com.baomidou.mybatisplus.service.IService;

import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;

/**
 * 订单支付信息<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IOrderPaymemtBiz extends IService<OrderPaymemtBo>{
		
	/**
	 * 根据订单id获取订单支付信息
	 * @param orderId 订单id
	 * @return
	 */
	OrderPaymemtBo orderPayByOrderId(Long orderId);
}