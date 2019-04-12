package cn.farwalker.ravv.service.order.orderinfo.biz;

import java.util.List;

public interface IOrderReturnBiz {

	/**
	 * 通过订单号查询主订单对象
	 * @param id
	 * @return
	 */
	//public OrderSalesReturnsDO getOrderByOrderId(Long id);
	
	/**
	 * 通过主订单id查询商品订单信息列表
	 * @param id
	 * @return
	 */
	//public List<OrderSalesReturnsDetailDO> getOrderGoodsInfoListByOrderId(Long id);
	
	/**
	 * 更新退货单
	 * @param DO
	 * @return
	 */
	//public BooleanResult updateOrderReturn(OrderSalesReturnsDO DO);
	
	/**
	 * 确认退货单
	 * @param ids
	 * @return
	 */
 	//public BooleanResult refundConfirmByOrder(List<OrderSalesReturnsDO> orders);
	/**
	 * 确认退货明细
	 * @param ids
	 * @return
	 */
	//public BooleanResult refundConfirmByOrderDetail(List<OrderSalesReturnsDetailDO> orderDetials);
	/**
	 * 查询退货单列表
	 * @param query
	 * @return
	 */
	//public List<OrderSalesReturnsDO> queryOrderReturnsList(QueryOrderReturnDO query);
	/**
	 * 查询退货清单列表
	 * @param query
	 * @return
	 */
	//public List<OrderSalesReturnsDetailDO> queryOrderDetailList(QueryOrderReturnDO DO);
	
	//public boolean checkOrderStatus(OrderSalesReturnsDetailDO DO);

	/**
	 * 通过用户id获取退货订单
	 * @param returnDo
	 * @return
	 */

	//PageDO<OrderSalesReturnsDetailDO> geOrderReturnList(OrderSalesReturnsDetailDO returnDo);
}
