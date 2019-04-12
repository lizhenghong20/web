package cn.farwalker.ravv.service.order.paymemt.biz.impl;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.farwalker.ravv.service.order.paymemt.biz.IOrderPaymemtBiz;
import cn.farwalker.ravv.service.order.paymemt.dao.IOrderPaymemtDao;
import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;

/**
 * 订单支付信息<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class OrderPaymemtBizImpl extends ServiceImpl<IOrderPaymemtDao,OrderPaymemtBo> implements IOrderPaymemtBiz{

	@Resource
	private IOrderPaymemtBiz orderPaymemtBiz;
	
	@Override
	public OrderPaymemtBo orderPayByOrderId(Long orderId) {
		Wrapper<OrderPaymemtBo> wrapper = new EntityWrapper<>();
		wrapper.eq(OrderPaymemtBo.Key.orderId.toString(), orderId);
		
		return orderPaymemtBiz.selectOne(wrapper);
	}
}