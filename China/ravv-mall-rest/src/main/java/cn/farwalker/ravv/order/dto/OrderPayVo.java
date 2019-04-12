package cn.farwalker.ravv.order.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;
import lombok.Data;

/**PC端支付前的信息*/
@Data
public class OrderPayVo extends OrderPaymemtBo{
	private static final long serialVersionUID = -1917523856954799266L;

	/**订单商品*/
	private List<OrderGoodsBo> goodsBos;
	
	/**失效时间(创建+过期分钟)*/
	private Date timeout;

	/**系统当前时间*/
	private Date systime;

	private BigDecimal advance;

}
