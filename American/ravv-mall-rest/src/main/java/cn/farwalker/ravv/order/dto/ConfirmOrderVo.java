package cn.farwalker.ravv.order.dto;

import java.math.BigDecimal;
import java.util.List;

import com.cangwu.frame.orm.core.annotation.LoadJoinValue;

import cn.farwalker.ravv.service.base.storehouse.model.StorehouseBo;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsVo;
import cn.farwalker.waka.util.Tools;

/**
 * 确认订单信息
 * @author Administrator
 *
 */
public class ConfirmOrderVo{
	private StorehouseBo storeHouseBo;
	private List<OrderGoodsVo> goodsBos;
	/**优惠金额(正数)*/
	private BigDecimal coupon;
	/**商品合计(不包括物流)*/
	private BigDecimal subtotal;
	/**运费*/
	private BigDecimal shipping;
	
	public ConfirmOrderVo(){
		coupon = BigDecimal.ZERO;
		subtotal = BigDecimal.ZERO;
		shipping = BigDecimal.ZERO;
	}

	
	/** 仓库*/
	public StorehouseBo getStoreHouseBo(){
		return storeHouseBo;
	}
	/** 仓库*/
	public void setStoreHouseBo(StorehouseBo storeHouseBo){
		this.storeHouseBo = storeHouseBo;
	}
	
	/** 按仓库分单后的商品*/
	public List<OrderGoodsVo> getGoodsBos(){
		return goodsBos;
	}
	/** 按仓库分单后的商品*/
	public void setGoodsBos(List<OrderGoodsVo> goodsBos){
		this.goodsBos = goodsBos;
		
		BigDecimal coupon = BigDecimal.ZERO;
		BigDecimal total = BigDecimal.ZERO;
		for(OrderGoodsVo vo : goodsBos){ 
			BigDecimal quan = BigDecimal.valueOf(Tools.number.nullIf( vo.getQuantity(),0));
			BigDecimal amt = Tools.bigDecimal.mul(vo.getPrice() ,quan);
			total = Tools.bigDecimal.add(total ,amt);
			
			BigDecimal orgiPrc = vo.getOrgiPrice();
			if(orgiPrc!=null){
				BigDecimal camt2 = Tools.bigDecimal.mul(vo.getPrice() ,quan);
				BigDecimal c =Tools.bigDecimal.sub(camt2 , amt);
				coupon = Tools.bigDecimal.add(c , coupon);
			} 
		}
		 
		this.coupon = coupon;
		this.subtotal = total;
	}
	
	/** 优惠金额(正数)*/
	public BigDecimal getCoupon(){
		return coupon;
	}
	/**优惠金额(正数)
	public void setCoupon(BigDecimal coupon) {
		//this.coupon = coupon;
	}*/
	
	/** 商品合计(不包括物流)*/
	public BigDecimal getSubtotal(){
		return subtotal;
	}
	
	/** 运费*/
	public BigDecimal getShipping(){
		return shipping;
	}
	
	/** 运费*/
	public void setShipping(BigDecimal shipping){
		this.shipping = shipping;
	}
	
}
