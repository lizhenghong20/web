package cn.farwalker.ravv.order.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.farwalker.ravv.service.base.storehouse.model.StorehouseBo;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.order.orderlogistics.model.OrderLogisticsBo;
import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsTypeEnum;
import cn.farwalker.ravv.service.shipment.model.ShipmentBo;
import cn.farwalker.waka.util.Tools;

import com.cangwu.frame.orm.core.annotation.LoadJoinValue;

/**
 * 订单 + 订单商品
 * @author Administrator
 *
 */
public class OrderAllInfoVo extends OrderInfoBo{
	private static final long serialVersionUID = 3660755127131171483L;
	/**订单商品*/
	private List<OrderGoodsBo> goodsBos;
	
	/**退货数量(5种商品，各退3个，等于15)*/
	private Integer returnCount;
	
	/**取第一个商品的退货状态*/
	private ReturnsTypeEnum returnType;
	
	/**订单支付信息*/
	private OrderPaymemtBo paymentBo;
	
	/**配送地址信息*/
	private OrderLogisticsBo logisticsBo;
	/**物流模板*/
	private ShipmentBo shipmentBo;
	
	/**子订单信息*/
	private List<OrderAllInfoVo> children;
	
	/**仓库名称*/
	private String storeName;
	
	/**订单评论次数*/
	private Integer commentCount;
	
	/**系统时间*/
	private Date systime;
	
	/**订单商品*/
	public List<OrderGoodsBo> getGoodsBos() {
		return goodsBos;
	}
	/**订单商品*/
	@LoadJoinValue(by="id",table=OrderGoodsBo.TABLE_NAME,joinfield="orderId")
	public void setGoodsBos(List<OrderGoodsBo> goodsBos) {
		this.goodsBos = goodsBos;
		/*new ArrayList<>();
		if(Tools.collection.isNotEmpty(goodsBos)){
			for(OrderGoodsBo bo :goodsBos){
				OrderGoodsReturnVo vo = Tools.bean.cloneBean(bo, new OrderGoodsReturnVo());
				this.goodsBos.add(vo);
			}
		}
		*/
	}

	/**订单商品 OrderGoodsReturnVo */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setGoodsVos(List<OrderGoodsReturnVo> goodsVo){
		List c = goodsVo;
		this.goodsBos = c;
	}
	
	/**订单支付信息*/
	public OrderPaymemtBo getPaymentBo() {
		return paymentBo;
	}
	/**订单支付信息*/
	@LoadJoinValue(by="id",table=OrderPaymemtBo.TABLE_NAME,joinfield="orderId")
	public void setPaymentBo(OrderPaymemtBo paymentBo) {
		this.paymentBo = paymentBo;
	}
	

	/**子订单信息*/
	public List<OrderAllInfoVo> getChildren() {
		return children;
	}
	
	/**子订单信息*/
	@LoadJoinValue(by="id",table=OrderInfoBo.TABLE_NAME,joinfield="pid")
	public void setChildren(List<OrderInfoBo> children) {
		if(children == null){
			return;
		}
		List<OrderAllInfoVo> rs = new ArrayList<>();
		for(OrderInfoBo bo :children){
			OrderAllInfoVo vo = Tools.bean.cloneBean(bo, new OrderAllInfoVo());
			rs.add(vo);
		}
		this.children = rs;
	}
	
	/**配送地址信息*/
	public OrderLogisticsBo getLogisticsBo() {
		return logisticsBo;
	}
	/**配送地址信息*/
	@LoadJoinValue(by="id",table=OrderLogisticsBo.TABLE_NAME,joinfield="orderId")
	public void setLogisticsBo(OrderLogisticsBo logisticsBo) {
		this.logisticsBo = logisticsBo;
	}
	
	/**退货数量(5种商品，各退3个，等于15)，用于列表显示*/
	public Integer getReturnCount() {
		return returnCount;
	}
	
	/**退货数量(5种商品，各退3个，等于15)，用于列表显示*/
	public void setReturnCount(Integer returnCount) {
		this.returnCount = returnCount;
	}
	/**仓库名称*/
	public String getStoreName() {
		return storeName;
	}
	/**仓库名称*/
	@LoadJoinValue(by="storehouseId",table=StorehouseBo.TABLE_NAME,joinfield="id",returnfield="storename")
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	/**订单评论次数*/
	public Integer getCommentCount() {
		return commentCount;
	}
	/**订单评论次数*/
	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}
	/**系统时间*/
	public Date getSystime() {
		return systime;
	}
	/**系统时间*/
	public void setSystime(Date systime) {
		this.systime = systime;
	}
	/**物流模板*/
	public ShipmentBo getShipmentBo() {
		return shipmentBo;
	}
	/**物流模板*/
	public void setShipmentBo(ShipmentBo shipmentBo) {
		this.shipmentBo = shipmentBo;
	}
	/**取第一个商品的退货状态*/
	public ReturnsTypeEnum getReturnType() {
		return returnType;
	}
	/**取第一个商品的退货状态*/
	public void setReturnType(ReturnsTypeEnum returnType) {
		this.returnType = returnType;
	}
}
