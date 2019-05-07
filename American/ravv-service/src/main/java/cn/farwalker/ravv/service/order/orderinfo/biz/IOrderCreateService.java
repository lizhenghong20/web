package cn.farwalker.ravv.service.order.orderinfo.biz;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cn.farwalker.ravv.service.goodssku.specification.model.GoodsSpecificationDefBo;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsVo;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderGoodsSkuVo;
import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;
import cn.farwalker.ravv.service.shipment.model.ShipmentBo;

/**
 * 创建订单处理
 * @author Administrator
 *
 */
public interface IOrderCreateService {
	/**
	 * 创建订单
	 * @param goodsSkuVo 选择的规格id,对应{@link GoodsSpecificationDefBo#getId()}
	 * @param shipmentId 运费模板，如果是默认运费，就传-1
	 * @param amt 订单总金额(页面看到的支付金额)
	 * @param quans 数量
	 * @param buyerId 买家id {@link MemberBo#getId()}
	 * @param buyerMessage 买家留言
	 * @param addressId 配送地址id
	 * @return
	 */
	public OrderPaymemtBo createOrder(List<OrderGoodsSkuVo> goodsSkuVo,Long shipmentId, BigDecimal amt,Long buyerId, String buyerMessage,Long addressId);
	
	/**
	 * 创建订单前的信息确认(返回订单，不创建数据库)
	 * @param valueids
	 * @return Map(key=仓库id,订单商品）
	 */
	public Map<Long,List<OrderGoodsVo>> getConfirmOrder(List<OrderGoodsSkuVo> valueids);
	
	/**
	 * 按商品计算默认运费
	 * @param goodBos
	 * @return
	 */
	public ShipmentBo calcFreightGoods(List<OrderGoodsBo> goodBos);

	public BigDecimal calTaxByStore(Long addressId, Long storeId, BigDecimal subTotal);
	
	/**创建订单号*/
	public String getOrderNo();

	public String testQuartz(Long orderId);
}
