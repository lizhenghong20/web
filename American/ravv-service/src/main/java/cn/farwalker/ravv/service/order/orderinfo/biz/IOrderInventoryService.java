package cn.farwalker.ravv.service.order.orderinfo.biz;

import java.util.List;

import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderStatusEnum;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;

/**
 * 订单库存处理(下单成功更新库存、冻结、解除冻结)
 * @author Administrator
 *
 */
public interface IOrderInventoryService {
	/**
	 * 订单支付前冻结库存(只处理冻结，其他的状态不处理
	 * @param orderIds 订单号(拆单的主表、没拆的订单号)
	 * @return  
	 */
	public Integer updateOrderFreeze(List<OrderInfoBo> orderBos,List<OrderGoodsBo> goodsBos) ;

	/**
	 * 订单支付前冻结库存 
	 * @param autoFree 是否自动接除冻结
	 * @param goodsBos 自带商品
	 * @return
	 */
	//public Integer updateOrderFreeze(List<OrderGoodsBo> goodsBos) ;
	
	/**
	 * 解除冻结，并且更新订单为取消状态<br/>
	 * 只有为支付的订单才能解除冻结:{@link OrderStatusEnum#CREATED_UNREVIEW},{@link OrderStatusEnum#REVIEWADOPT_UNPAID} 
	 * @param orderIds 订单号,如果是拆单，就要是主单
	 * @return 处理的订单号
	 */
	public Long[] updateOrderUnfreeze(Long... orderIds) ;
	
	/**
	 * 单纯的按商品增加库存、减少冻结数量，不处理订单状态(也不检查)
	 * @param goodsBos
	 * @return
	 */
	//public Integer updateOrderUnfreeze(List<OrderGoodsBo> goodsBos) ;
	
	/**
	 * 订单下单成功（支付成功后），更新订单商品的库存信息(冻结信息)
	 * 只更新商品库存及促销已购买数量，订单的其他信息不更新
	 * @param orderIds 如果是拆单主单号，就会更新全部订单
	 * @return
	 */
	public Boolean updateBuySuccessGoodsInventory(Long... orderIds);
}
