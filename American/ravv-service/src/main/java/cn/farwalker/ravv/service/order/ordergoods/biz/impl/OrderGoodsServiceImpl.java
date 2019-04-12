package cn.farwalker.ravv.service.order.ordergoods.biz.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

import cn.farwalker.ravv.service.goods.price.model.GoodsPriceBo;
import cn.farwalker.ravv.service.order.ordergoods.biz.IOrderGoodsBiz;
import cn.farwalker.ravv.service.order.ordergoods.biz.IOrderGoodsService;
import cn.farwalker.ravv.service.order.ordergoods.dao.IOrderGoodsDao;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsSimpleVo;
import cn.farwalker.waka.util.Tools;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单商品
 * 
 * @author Lijj
 *
 */
@Service
public class OrderGoodsServiceImpl implements IOrderGoodsService {
	@Resource
	private IOrderGoodsBiz orderGoodsBiz;
	
	@Resource
	private IOrderGoodsDao orderGoodsDao;

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void updateOrderGoods(Long orderId, List<OrderGoodsBo> orderGoods) {
		List<GoodsPriceBo> orderGoodsList = new ArrayList<>(orderGoods.size());

		for (OrderGoodsBo vo : orderGoods) {
			// "id": e.id,
			// "quantity": e.quantity,
			// "price": e.price,
			// "goodsfee": e.goodsfee,
			// "remark": e.remark,
			// "imgMajor": e.imgMajor
			orderGoodsBiz.updateById(vo);

		}
	}

	@Override
	public List<OrderGoodsBo> getOrderGoodsBos(Long... orderIds) {
		if(orderIds == null || orderIds.length==0){
			return Collections.emptyList();
		}
		
		List<Long> ids = Arrays.asList(orderIds);
		Wrapper<OrderGoodsBo> wrap = new EntityWrapper<>();
		wrap.in(OrderGoodsBo.Key.orderId.toString(), ids);
		List<OrderGoodsBo> rs = orderGoodsBiz.selectList(wrap);
		return rs;
	}

	@Override
	public Integer getOrderGoodsTotal(Long orderId) {
		Integer orderGoodsTotal = 0;
		if(orderId == null) {
			return orderGoodsTotal;
		}
		
		Wrapper<OrderGoodsBo> wrapper = new EntityWrapper<>();
		wrapper.eq(OrderGoodsBo.Key.orderId.toString(), orderId);
		
		List<OrderGoodsBo> orderGoodsList = orderGoodsBiz.selectList(wrapper);
		if(Tools.collection.isEmpty(orderGoodsList)) {
			return orderGoodsTotal;
		}
		
		for(OrderGoodsBo orderGoods : orderGoodsList) {
			orderGoodsTotal += orderGoods.getQuantity();
		}
		
		return orderGoodsTotal;
	}

	@Override
	public List<OrderGoodsSimpleVo> getOrderGoodsByMerchant(Long merchantId, Date startdate, Date enddate) {
		return orderGoodsDao.getOrderGoodsByMerchant(merchantId, startdate, enddate);
	}
}