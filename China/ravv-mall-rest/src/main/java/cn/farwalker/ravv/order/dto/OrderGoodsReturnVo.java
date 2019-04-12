package cn.farwalker.ravv.order.dto;

import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsBo;
/**
 * 添加退货信息
 * @author Administrator
 *
 */
public class OrderGoodsReturnVo extends OrderGoodsBo{
	private static final long serialVersionUID = 3374941564170850840L;
	private Integer returnIng,returnFinish;
	
	/**正在退货中的退货主表*/
	private OrderReturnsBo returnIngBo; 
	
	/**处理中的售后数量*/
	public Integer getReturnIng() {
		return returnIng;
	}
	/**处理中的售后数量*/
	public void setReturnIng(Integer returnIng) {
		this.returnIng = returnIng;
	}
	/**完成的售后数量*/
	public Integer getReturnFinish() {
		return returnFinish;
	}
	/**完成的售后数量*/
	public void setReturnFinish(Integer returnFinish) {
		this.returnFinish = returnFinish;
	}
	/**正在退货中的退货主表*/
	public OrderReturnsBo getReturnIngBo() {
		return returnIngBo;
	}
	/**正在退货中的退货主表*/
	public void setReturnIngBo(OrderReturnsBo returnIngBo) {
		this.returnIngBo = returnIngBo;
	}
	 
}
