package cn.farwalker.ravv.service.order.returns.biz.impl;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsBiz;
import cn.farwalker.ravv.service.order.returns.dao.IOrderReturnsDao;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsBo;

/**
 * 订单退货<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class OrderReturnsBizImpl extends ServiceImpl<IOrderReturnsDao,OrderReturnsBo> implements IOrderReturnsBiz{

	@Resource
	private IOrderReturnsBiz orderReturnsBiz;
	
	@Override
	public List<OrderReturnsBo> returnsByOrderId(Long orderId) {
		Wrapper<OrderReturnsBo> wrapper = new EntityWrapper<>();
		wrapper.eq(OrderReturnsBo.Key.orderId.toString(), orderId);
		
		return orderReturnsBiz.selectList(wrapper);
	}
}