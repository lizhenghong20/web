package cn.farwalker.ravv.service.order.returns.biz;
import java.util.List;

import com.baomidou.mybatisplus.service.IService;

import cn.farwalker.ravv.service.order.returns.model.OrderReturnsBo;

/**
 * 订单退货<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IOrderReturnsBiz extends IService<OrderReturnsBo>{
	
	/**
	 * 根据订单id获取退换货订单列表
	 * @param orderId 订单id
	 * @return
	 */
	List<OrderReturnsBo> returnsByOrderId(Long orderId);
}