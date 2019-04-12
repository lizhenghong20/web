package cn.farwalker.ravv.service.order.ordergoods.biz;

import java.util.Date;
import java.util.List;

import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsSimpleVo;

/**
 * 订单商品
 * 
 * @author Lijj
 */
public interface IOrderGoodsService {

	/**
	 * 更新订单商品
	 * @param orderId
	 * @param orderGoods
	 */
	void updateOrderGoods(Long orderId, List<OrderGoodsBo> orderGoods);
	
	/**
	 * 取得订单商品
	 * @param orderId
	 * @return
	 */
	public List<OrderGoodsBo> getOrderGoodsBos(Long... orderIds);
	
	/**
	 * 统计单个订单所有商品数量总和
	 * @param orderId
	 * @return
	 */
	public Integer getOrderGoodsTotal(Long orderId);
	
	/**
	 * 获取供应商某时间范围内的订单商品
	 * @param merchantId 供应商id
	 * @param startdate 开始时间
	 * @param enddate 结束时间
	 * @return
	 */
	public List<OrderGoodsSimpleVo> getOrderGoodsByMerchant(Long merchantId, Date startdate, Date enddate);
}