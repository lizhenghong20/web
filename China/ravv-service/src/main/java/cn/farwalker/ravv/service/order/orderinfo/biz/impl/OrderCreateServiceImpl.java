package cn.farwalker.ravv.service.order.orderinfo.biz.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.farwalker.waka.core.WakaException;
import cn.farwalker.ravv.service.quartz.JobSchedulerFactory;
import cn.farwalker.ravv.service.quartz.UpdateOrderUnfreezeTaskJob;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.cangwu.frame.orm.core.BaseBo;
import com.cangwu.frame.orm.core.annotation.LoadJoinValueImpl;

import cn.farwalker.ravv.common.constants.FlashSaleStatusEnum;
import cn.farwalker.ravv.service.base.area.biz.IBaseAreaBiz;
import cn.farwalker.ravv.service.base.area.constant.CountryCodeEnum;
import cn.farwalker.ravv.service.base.area.model.AreaFullVo;
import cn.farwalker.ravv.service.base.storehouse.biz.IStorehouseBiz;
import cn.farwalker.ravv.service.base.storehouse.model.StorehouseBo;
import cn.farwalker.ravv.service.category.property.biz.IBaseCategoryPropertyBiz;
import cn.farwalker.ravv.service.category.property.model.BaseCategoryPropertyBo;
import cn.farwalker.ravv.service.category.value.biz.IBaseCategoryPropertyValueBiz;
import cn.farwalker.ravv.service.category.value.model.BaseCategoryPropertyValueBo;
import cn.farwalker.ravv.service.flash.sale.model.FlashSaleGoodsSkuVo;
import cn.farwalker.ravv.service.flash.sku.biz.IFlashSkuService;
import cn.farwalker.ravv.service.flash.sku.model.FlashGoodsSkuBo;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsBiz;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsService;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.goods.base.model.GoodsVo;
import cn.farwalker.ravv.service.goods.constants.GoodsStatusEnum;
import cn.farwalker.ravv.service.goods.image.biz.IGoodsImageBiz;
import cn.farwalker.ravv.service.goods.price.biz.IGoodsPriceBiz;
import cn.farwalker.ravv.service.goods.price.model.GoodsPriceBo;
import cn.farwalker.ravv.service.goods.utils.GoodsUtil;
import cn.farwalker.ravv.service.goodscart.biz.IGoodsCartService;
import cn.farwalker.ravv.service.goodssku.skudef.biz.IGoodsSkuDefBiz;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuDefBo;
import cn.farwalker.ravv.service.goodssku.skudef.model.SkuPriceInventoryVo;
import cn.farwalker.ravv.service.goodssku.specification.biz.IGoodsSpecificationDefBiz;
import cn.farwalker.ravv.service.goodssku.specification.model.GoodsSpecificationDefBo;
import cn.farwalker.ravv.service.member.address.biz.IMemberAddressBiz;
import cn.farwalker.ravv.service.member.address.model.MemberAddressBo;
import cn.farwalker.ravv.service.member.basememeber.biz.IMemberBiz;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.order.constants.PayStatusEnum;
import cn.farwalker.ravv.service.order.constants.PaymentModeEnum;
import cn.farwalker.ravv.service.order.operationlog.biz.IOrderLogService;
import cn.farwalker.ravv.service.order.ordergoods.biz.IOrderGoodsBiz;
import cn.farwalker.ravv.service.order.ordergoods.constants.OrderGoodsStatusEnum;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsVo;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderCreateService;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoBiz;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInventoryService;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderStatusEnum;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderTypeEnum;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderGoodsSkuVo;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.order.orderlogistics.biz.IOrderLogisticsBiz;
import cn.farwalker.ravv.service.order.orderlogistics.contants.LogisticsPaymentEnum;
import cn.farwalker.ravv.service.order.orderlogistics.contants.LogisticsStatusEnum;
import cn.farwalker.ravv.service.order.orderlogistics.contants.LogisticsTypeEnum;
import cn.farwalker.ravv.service.order.orderlogistics.model.OrderLogisticsBo;
import cn.farwalker.ravv.service.order.paymemt.biz.IOrderPaymemtBiz;
import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;
import cn.farwalker.ravv.service.order.time.OrderTaskTimer;
import cn.farwalker.ravv.service.sale.profitallot.biz.ISaleProfitAllotBiz;
import cn.farwalker.ravv.service.shipment.biz.IShipmentBiz;
import cn.farwalker.ravv.service.shipment.constants.ShipmentTypeEnum;
import cn.farwalker.ravv.service.shipment.model.ShipmentBo;
import cn.farwalker.ravv.service.sys.param.biz.ISysParamService;
import cn.farwalker.ravv.service.sys.param.biz.SysParamCache;
import cn.farwalker.waka.cache.CacheManager;
import cn.farwalker.waka.components.sequence.SequenceManager;
import cn.farwalker.waka.util.DateUtil.FORMAT;
import cn.farwalker.waka.util.Tools;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单处理
 * @author Administrator
 */
@Service
public class OrderCreateServiceImpl implements IOrderCreateService{
	private static final Logger log = LoggerFactory.getLogger(OrderCreateServiceImpl.class);
	
	@Resource
	private IFlashSkuService flashSkuBiz;
	@Resource
	private IGoodsSkuDefBiz goodsSkuDefBiz;
	
	@Resource
	private IGoodsBiz goodsBiz;
	
	@Resource
	private IGoodsService goodsService;
	
	@Resource
	private IOrderInfoBiz orderInfoBiz;
	
	@Resource
	private IOrderGoodsBiz orderGoodsBiz;
	
	@Resource
	private IOrderPaymemtBiz orderPaymemtBiz;
	
	@Resource
	private IGoodsImageBiz goodsImageBiz;
	
	@Resource
	private IGoodsSpecificationDefBiz goodsSpecificationDefBiz;
	@Resource
	private IBaseCategoryPropertyBiz categoryPropertyBiz;
	@Resource
	private IBaseCategoryPropertyValueBiz categoryPropertyValueBiz;
	
	@Resource
	private IMemberBiz memberBiz;
	
	@Resource
	private IOrderLogService orderLogService;
	
	@Resource
	private IShipmentBiz shipmentBiz;
	
	@Resource
	private IGoodsPriceBiz goodsPriceBiz; 
	
	@Resource
	private IMemberAddressBiz memberAddressBiz; 
	@Resource
	private IBaseAreaBiz baseAreaBiz ; 
	
	@Resource
	private IOrderLogisticsBiz orderLogisticsBiz; 
	
	@Resource
	private ISaleProfitAllotBiz saleProfitAllotBiz;
	
	@Resource
	private IStorehouseBiz storehouseBiz;
	
	/**解除冻结定时器*/
	@Resource
	private OrderTaskTimer orderTaskTimer;
	@Resource
	private ISysParamService sysparamService;
	@Resource
	private IOrderInventoryService orderInfoService;
	
	private class OrderAllObject{
		OrderInfoBo orderBo;
		List<OrderGoodsBo> goodsBos;
		OrderPaymemtBo paymentBo;
		OrderLogisticsBo logistiscBo;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public OrderPaymemtBo createOrder(List<OrderGoodsSkuVo> goodsSkuVo,Long shipmentId, BigDecimal pageTotalAmt,
			Long buyerId, String buyerMessage,Long addressId) {
		if(shipmentId==null){
			throw new WakaException("运费id不能为空,如果默认运费，请传-1");
		}
		if(addressId==null){
			throw new WakaException("订单配送id不能为空");
		}
		if(buyerId==null){
			throw new WakaException("买家不能为空");
		}

		//检查通过
		List<SkuPriceInVo> skuVos = checkGoodsInventory(goodsSkuVo);
		//int totalAmt = (int)(amt.doubleValue() * 100);
		
		List<OrderAllObject> orderBos = createOrders(pageTotalAmt,shipmentId,skuVos,buyerId, buyerMessage); 
		this.checkOrderTotalAmt(orderBos, pageTotalAmt);
		
		this.createOrderLogisticsBo(orderBos,addressId,  shipmentId);
		//calcTaxOrders(orderBos);//不需要算税了
		
		OrderAllObject masterBo = orderBos.get(0);//第一个是主单
		OrderInfoBo ob = masterBo.orderBo;
		String txt = "订单金额:" +  pageTotalAmt;
		orderLogService.createLog(ob.getId(),buyerId,ob.getBuyerNick(), "订单创建", txt ,Boolean.TRUE);
		
		freezeInventory(orderBos);
		
		//创建分润记录
		if(CollectionUtils.isNotEmpty(orderBos)) {
			List<OrderPaymemtBo> orderPayList = new ArrayList<>();
			for(OrderAllObject order : orderBos) {
				if(!order.orderBo.getOrderType().equals(OrderTypeEnum.MASTER)) {
					orderPayList.add(order.paymentBo);
				}
			}
			saleProfitAllotBiz.setProfitAllot(orderPayList, buyerId);
		}
		
		deleteCart(buyerId,goodsSkuVo); //删除购物车的记录
		return masterBo.paymentBo;
	}
	
	/**
	 * 冻结库存
	 * @param orderBos
	 */
	private void freezeInventory(List<OrderAllObject> orderBos){
		List<OrderInfoBo> bos = new ArrayList<>(orderBos.size());
		List<OrderGoodsBo> goodsBos = new ArrayList<>();
		for(OrderAllObject a :orderBos){
			bos.add(a.orderBo);
			goodsBos.addAll(a.goodsBos);
		}
		orderInfoService.updateOrderFreeze(bos, goodsBos);
		OrderInfoBo masterBo = orderBos.get(0).orderBo;////第一个是主单
//		orderTaskTimer.addOrderUnfreeze(masterBo.getId());
		//启用定时任务更新
		callUpdateOrderUnfreeze(masterBo.getId());
	}



	/**
	 * @description: 使用Quartz执行定时任务
	 * @param: orderId
	 * @return void
	 * @author Mr.Simple
	 * @date 2019/3/21 9:43
	 */
	private void callUpdateOrderUnfreeze(Long orderId){
		long delay = 15 * 60 * 1000;
		JobDataMap paramMap = new JobDataMap();
		paramMap.put("orderId", String.valueOf(orderId));
		JobSchedulerFactory.callTaskJob(
				delay,
				UpdateOrderUnfreezeTaskJob.class,
				"UpdateOrderUnfreezeTaskJob",
				paramMap);
	}
 
	/**
	 * 取下单时的金额、库存、促销价格、促销冻结(不检查)
	 * @param goodsSkuVo
	 * @return
	 */
	private List<SkuPriceInVo> getGoodsInventory(List<OrderGoodsSkuVo> goodsSkuVo){
		List<SkuPriceInVo> skuVos = new ArrayList<>(); 
		for(OrderGoodsSkuVo vo:goodsSkuVo){
			Long goodsId = vo.getGoodsId();
			GoodsBo goodsBo = goodsBiz.selectById(goodsId);
			if(goodsBo == null){
				throw new WakaException("商品已下架:" + goodsId);
			}
			if(goodsBo.getGoodsStatus() != GoodsStatusEnum.ONLINE){
				String s =goodsBo.getGoodsName() + "商品已下架";
				log.error(s);
				throw new WakaException(s);
			}
			
			//List<Long> pvIds = vo.getValueids() ;//Arrays.asList(specValieIds.get(i));
			//FlashSaleGoodsSkuVo skuBo = flashSkuBiz.getGoodsSkuBo(goodsId, pvIds);
			Long skuId = vo.getSkuId();
			FlashSaleGoodsSkuVo skuBo = flashSkuBiz.getGoodsSkuBo(goodsId, skuId);
			SkuPriceInVo priceVo ;
			if(skuBo == null){ //不在促销控制
				SkuPriceInventoryVo skuVo = goodsService.getGoodsSkuPriceVo(skuId);//goodsService.getGoodsSkuPriceVo(goodsId, pvIds);
				priceVo = new SkuPriceInVo(skuVo,goodsBo);
			}
			else if(skuBo.getStatus() == FlashSaleStatusEnum.FROZEN){
				String s = goodsBo.getGoodsName() + "正在冻结期，不能购买";
				log.error(s + ",skuid=" + skuBo.getGoodsSkuVo().getId());
				throw new WakaException(s);//冻结期，不能购买
			}
			else{//促销
				priceVo = new SkuPriceInVo(skuBo.getGoodsSkuVo(),goodsBo);
			}
			
			int quan = Tools.number.nullIf(vo.getQuan(),0);
			skuVos.add(priceVo);
			priceVo.setQuan(quan);
		}
		return skuVos;
	}
	
	/**
	 * 检查下单时的金额、库存、促销价格、促销冻结(检查)
	 * @param goodsSkuVo 
	 * @return
	 */ 
	private List<SkuPriceInVo> checkGoodsInventory(List<OrderGoodsSkuVo> goodsSkuVo ){
		List<SkuPriceInVo> skuVos = getGoodsInventory(goodsSkuVo);
		BigDecimal totalAmt = BigDecimal.valueOf(0);
		for(SkuPriceInVo vo:skuVos){ 
			int quan = vo.getQuan();
			totalAmt = Tools.bigDecimal.add( totalAmt ,vo.getAmt());
			
			int inv = vo.getInventory();
			if(inv<quan){
				String s = vo.getGoodsBo().getGoodsName() + "库存不足";
				log.error(s + ",skuid=" + vo.getSkuId());
				String s2 = vo.getGoodsBo().getGoodsName() + "库存不足" 
						+ quan + "/" + inv;
				log.error("skuid=" +  vo.getSkuId() + "," + s2);
				throw new WakaException(s2);
			}
		}
		/**
		int amt2 =  (int)(pageTotalAmt.doubleValue() * 100);
		if(amt2 != totalAmt){
			throw new WakaException("下单金额不正确" + Tools.number.formatAmt(totalAmt, 100) + "/" +Tools.number.formatAmt(amt2, 100));
		}*/
		return skuVos;
	}
	@Resource
    private IGoodsCartService goodsCartService; 
	/**
	 * 用户生成订单后批量删除购物车商品
	 * 使用线程，不影响主业务
	 */
	private void deleteCart(final Long buyerId, final List<OrderGoodsSkuVo> goodsSkuVo){
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				List<Long> skuIds = new ArrayList<>(goodsSkuVo.size());
				for(OrderGoodsSkuVo vo : goodsSkuVo){
					skuIds.add(vo.getSkuId());
				}
				goodsCartService.deleteByGoodsIdList(buyerId, skuIds);		
			}
		};
		Tools.thread.runSingle(task, "deleteCart");
	}

	/**创建订单号*/
	@Override
	public String getOrderNo(){
		String df = Tools.date.formatDate(FORMAT.YYYYMMDD).toString();
		SequenceManager sm = SequenceManager.getInstance(OrderInfoBo.TABLE_NAME + df);
		long code = sm.nextValue();
		String orderCode = df + Tools.number.formatNumber(code, 4);
		return orderCode;
	}

	@Override
	public String testQuartz(Long orderId) {
		callUpdateOrderUnfreeze(orderId);

		return "success";
	}

	/**
	 * 
	 * @param totalAmt 页面看到的支付金额
	 * @param shipmentId
	 * @param skuVos
	 * @param buyerId
	 * @param buyerMessage
	 * @return 第一个是主单
	 */
	private List<OrderAllObject> createOrders(BigDecimal totalAmt,Long shipmentId, List<SkuPriceInVo> skuVos,Long buyerId,String buyerMessage){
		Map<Long,List<SkuPriceInVo>> storeOrder = doDecompose(skuVos);
		final int storeCount  = storeOrder.size();
		if(storeCount ==1){ //不需分单
			//BigDecimal amt = new BigDecimal(totalAmt / 100.0d);
			OrderAllObject bo = createOrderChildren(totalAmt,buyerId,  buyerMessage,null,skuVos,shipmentId);
			return Arrays.asList(bo);
		}
		
		///////分单////////////////////////////// 
		OrderAllObject masterBo = createOrderMaster( buyerId, buyerMessage );
		List<OrderAllObject> rds = new ArrayList<>(storeOrder.size()+1);
		rds.add(masterBo); //第一个是主单

		final Long orderMasterId = masterBo.orderBo.getId();
		int index =0;
		BigDecimal postFee = BigDecimal.ZERO,goodsTotalFee = BigDecimal.ZERO;
		BigDecimal total2 = totalAmt;
		
		for(List<SkuPriceInVo> skus : storeOrder.values()){
			BigDecimal childAmt;
			index ++;
			if(index == storeCount){ //最后一个子订单
				childAmt = total2;//new BigDecimal(total2 / 100.0d);
			}
			else{
				BigDecimal childrenAmt = BigDecimal.ZERO;
				for(SkuPriceInVo v : skus){
					childrenAmt = Tools.bigDecimal.add(childrenAmt,  v.getAmt());
				}
				total2 =Tools.bigDecimal.sub(total2 , childrenAmt);
				childAmt = childrenAmt;
			}
			
			OrderAllObject cbo = createOrderChildren(childAmt,buyerId,  buyerMessage,orderMasterId,skus,shipmentId);
			rds.add(cbo);
			
			postFee = Tools.bigDecimal.add(postFee , cbo.paymentBo.getPostFee());
			goodsTotalFee = Tools.bigDecimal.add(goodsTotalFee , cbo.paymentBo.getGoodsTotalFee());
		}
		
		//////////主单的运费信息，并且保存///////////////////////
		masterBo.paymentBo = createOrderPaymentBo(orderMasterId, goodsTotalFee, postFee); 
		return rds;
	}
	/**
	 * 检查页面总金额
	 * @param orderBos
	 * @param pageTotalAmt (单位:元)
	 * @return
	 */
	private boolean checkOrderTotalAmt(List<OrderAllObject> orderBos,BigDecimal pageTotalAmt){
		OrderPaymemtBo paymentBo = orderBos.get(0).paymentBo;
		BigDecimal orderAmt = Tools.bigDecimal.add(paymentBo.getGoodsTotalFee(), paymentBo.getPostFee())
				.setScale(2,RoundingMode.HALF_UP);
		
		//orderAmt.setScale(2,RoundingMode.HALF_UP);
		//int orderAmt2 = Tools.bigDecimal.mul(orderAmt, new BigDecimal(100)).intValue();
		//int pageTotalAmt2 = Tools.bigDecimal.mul(pageTotalAmt, new BigDecimal(100)).intValue();
		
		if(orderAmt.doubleValue() == pageTotalAmt.setScale(2,RoundingMode.HALF_UP).doubleValue()){
			return true;
		}
		else{
			String s1="计算后的订单金额(" +  orderAmt  ;
			String s2 =  ")与页面看到的金额(" +  pageTotalAmt + ")不相等";
			throw new WakaException(s1+s2);
		}
	}
	
	/**
	 * 创建主单记录(paymentBo没有保存的，需要调用的地方保存)
	 * (运费需要从明细记录计算运费，所以不能保存)
	 * @param buyerId
	 * @param buyerMessage
	 * @return
	 */
	private OrderAllObject createOrderMaster(Long buyerId,String buyerMessage ){
		OrderInfoBo orderBo = new OrderInfoBo();
		orderBo.setBuyerId(buyerId);
		orderBo.setBuyerMessage(buyerMessage);
		//orderBo.setPid(Long.valueOf(0 - childCount));
		orderBo.setOrderType(OrderTypeEnum.MASTER);
		
		MemberBo mbo = getMemberBo(buyerId);
		orderBo.setBuyerNick(mbo.getFirstname() + " " + mbo.getLastname());
		String orderCode = getOrderNo();
		orderBo.setOrderCode(orderCode);
		
		orderBo.setOrderStatus(OrderStatusEnum.REVIEWADOPT_UNPAID);
		Calendar timeout = getOrderTimeout();
	    orderBo.setTimeoutDate(timeout.getTime());
		orderInfoBiz.insert(orderBo);

		
		OrderAllObject rs = new OrderAllObject();
		rs.goodsBos = Collections.emptyList();
		rs.orderBo = orderBo;
		//rs.paymentBo = paymentBo;
		return rs;
	}
	/**
	 * 
	 * @return
	 */
	private OrderPaymemtBo createOrderPaymentBo(Long orderId, BigDecimal goodsTotalFee,BigDecimal postFee){
		OrderPaymemtBo paymentBo = new OrderPaymemtBo();
		paymentBo.setPayMode(PaymentModeEnum.ONLINE);
		paymentBo.setPayStatus(PayStatusEnum.UNPAY);

		paymentBo.setOrderId(orderId);
		paymentBo.setGoodsTotalFee(goodsTotalFee);
		paymentBo.setPostFee(postFee);
		paymentBo.setShouldPayTotalFee(OrderPaymemtBo.getPayTotalFee(paymentBo));

		orderPaymemtBiz.insert(paymentBo);
		return paymentBo;
	}
	
	private MemberBo getMemberBo(Long memberId){
		String key = "member_" + memberId;
		MemberBo rd= CacheManager.cache.getObject(key);
		if(rd ==null){
			rd= memberBiz.selectById(memberId);
			CacheManager.cache.putObject(key, rd);
		}
		return rd;
	}
	
	private OrderAllObject createOrderChildren(BigDecimal amt,Long buyerId,String buyerMessage 
			,Long orderMasterId,List<SkuPriceInVo> goodsBos,Long shipmentId){
		OrderInfoBo orderBo = new OrderInfoBo();
		orderBo.setPid( orderMasterId);
		if(Tools.number.nullIf(orderMasterId, 0) ==0){
			orderBo.setOrderType(OrderTypeEnum.SINGLE);
		}
		else{
			orderBo.setOrderType(OrderTypeEnum.CHILD);
		}
		orderBo.setBuyerId(buyerId);
		orderBo.setBuyerMessage(buyerMessage);
		MemberBo mbo = getMemberBo(buyerId);
		orderBo.setBuyerNick(mbo.getFirstname() + " " + mbo.getLastname());
		
		String orderCode = getOrderNo();
		orderBo.setOrderCode(orderCode);
		Calendar timeout = getOrderTimeout();
	    orderBo.setTimeoutDate(timeout.getTime());
	    
		orderBo.setOrderStatus(OrderStatusEnum.REVIEWADOPT_UNPAID);
		Long storeId  = goodsBos.get(0).getGoodsBo().getStorehouseId();
		orderBo.setStorehouseId(storeId);
		
		orderInfoBiz.insert(orderBo);
		Long orderId = orderBo.getId();

		//////////////////////////////////////////
		List<OrderGoodsBo> orderGoodsBos = getOrderGoodsBos(orderId,goodsBos);
		BigDecimal goodsTotalFee =BigDecimal.ZERO;
		for(OrderGoodsBo g :orderGoodsBos){
			int quan =Tools.number.nullIf(g.getQuantity(),0);
			BigDecimal goodsFee = Tools.bigDecimal.mul(g.getPrice(), BigDecimal.valueOf(quan));

			goodsTotalFee = Tools.bigDecimal.add(goodsFee,goodsTotalFee);
		}
		orderGoodsBiz.insertBatch(orderGoodsBos);
		
		BigDecimal postFee ;
		if(shipmentId.longValue()==-1){//默认运费
			ShipmentBo bo = this.calcFreightGoods(orderGoodsBos);
			postFee = (bo == null ? null : bo.getFee());
		}
		else{
			ShipmentBo sbo = shipmentBiz.selectById(shipmentId);
			if(sbo==null){
				throw new WakaException("运费id不存在:" + shipmentId);
			}
			postFee = sbo.getFee();
		}

		//////////////////////////////////////////
		OrderPaymemtBo paymentBo = new OrderPaymemtBo();
		paymentBo.setGoodsTotalFee(goodsTotalFee);
		paymentBo.setPayMode(PaymentModeEnum.ONLINE);
		paymentBo.setPayStatus(PayStatusEnum.UNPAY);
		paymentBo.setShouldPayTotalFee(amt);
		paymentBo.setOrderId(orderId);
		
		paymentBo.setPostFee(postFee);//物流不需要收税的
		orderPaymemtBiz.insert(paymentBo); 
		
		OrderAllObject allobj = new OrderAllObject();
		allobj.goodsBos = orderGoodsBos;
		allobj.paymentBo = paymentBo;
		allobj.orderBo = orderBo;
				
		return allobj;
	}
	
	/**
	 * 创建订单物流(主单、拆单都各有一份)
	 * @param orderBos
	 * @return
	 */
	private List<OrderLogisticsBo> createOrderLogisticsBo(List<OrderAllObject> orderBos,Long addressId,Long shipmentId){
		//return Collections.emptyList();
		MemberAddressBo addreBo = memberAddressBiz.selectById(addressId);
		AreaFullVo areaVo  = AreaFullVo.getAreaFullVo(addreBo.getAreaId(), baseAreaBiz);
		List<OrderLogisticsBo> rds = new ArrayList<>(orderBos.size());
		
		for(OrderAllObject bo: orderBos){
			OrderLogisticsBo vo = new OrderLogisticsBo();
			bo.logistiscBo = vo;
			vo.setOrderId(bo.orderBo.getId());
			vo.setReturnsId(Long.valueOf(0));
			
			
			OrderPaymemtBo payBo = bo.paymentBo;
			if(Tools.number.nullIf(payBo.getPostFee(),0) ==0){
				vo.setLogisticsPayment(LogisticsPaymentEnum.SELLER);
			}
			else{
				vo.setLogisticsPayment(LogisticsPaymentEnum.COD);//TODO 只有两个支付状态了 不知道会不会报错  把buyer 改为了cod
			}
			vo.setLogisticsStatus(LogisticsStatusEnum.NONE);
			vo.setLogisticsType(LogisticsTypeEnum.LandCarriage);
			
			if(areaVo.getAreaBo()!=null){
				vo.setReceiverArea(areaVo.getAreaBo().getName());
			}
			if(areaVo.getCityBo()!=null){
				vo.setReceiverCity(areaVo.getCityBo().getName());
			}
			if(areaVo.getProvinceBo()!=null){ //省、国家
				vo.setCountryCode(areaVo.getProvinceBo().getCountryCode());
				vo.setReceiverProvince(areaVo.getProvinceBo().getName());
			}
			vo.setReceiverAreaId(addreBo.getAreaId());
			
			//vo.setReceiverFullname(addreBo.getFirstname() + "." + addreBo.getLastname());
			vo.setReceiverFullname(addreBo.getName());
			vo.setReceiverDetailAddress(addreBo.getAddress());
			vo.setReceiverMobile(addreBo.getMobile());
			vo.setReceiverPostCode(addreBo.getZip());
			
			//物流模板
			if(shipmentId!=null && shipmentId.longValue() >0){
				ShipmentBo shipment = shipmentBiz.selectById(shipmentId);
				if(shipment != null) {
					vo.setLogisticsCompany(shipment.getName());
				}
				vo.setShipmentId(shipmentId);
			}
			rds.add(vo);
		}
		orderLogisticsBiz.insertBatch(rds);
		return rds;
	}
	
	
	/**
	 * 取得订单商品
	 * @param goodsBos
	 * @return
	 */
	private List<OrderGoodsBo> getOrderGoodsBos(Long orderId, List<SkuPriceInVo> goodsBos){
		Map<Long,String> skuImage = getSkuImage(goodsBos);
		List<OrderGoodsBo> orderGoodsBos = new ArrayList<>();
		for(SkuPriceInVo g : goodsBos){
			GoodsVo gv = Tools.bean.cloneBean( g.getGoodsBo() , new GoodsVo());
			LoadJoinValueImpl.load(goodsImageBiz, gv);
			
			OrderGoodsBo gbo = new OrderGoodsBo();
			gbo.setGoodsBrandName(gv.getBrandName());
			gbo.setGoodsCode(gv.getGoodsCode());
			
			//BigDecimal fee = new BigDecimal(g.getAmt() /100.0d);
			BigDecimal fee = g.getAmt();
			gbo.setGoodsfee(fee);
			gbo.setGoodsId(gv.getId());
			gbo.setGoodsModelNum(gv.getGoodsModelNum());
			gbo.setGoodsName(gv.getGoodsName());
			gbo.setSkuId(g.getSkuId());
			
			gbo.setImgDesc(GoodsUtil.getCdnRelativePath(gv.getImageDetails()));
			gbo.setImgMajor(GoodsUtil.getCdnRelativePath(gv.getImageMajor()));
			gbo.setImgTitle(GoodsUtil.getCdnRelativePath(gv.getImageTitles()));
			String skuImg = skuImage.get(gbo.getSkuId());
			gbo.setImgSku(GoodsUtil.getCdnRelativePath(skuImg));
			
			gbo.setOrderGoodsStatus(OrderGoodsStatusEnum.SALE);
			gbo.setOrderId(orderId);
			//gbo.setPrice(new BigDecimal(g.getPrice() /100.0d));
			gbo.setPrice(g.getPrice());
			gbo.setQuantity(Integer.valueOf(g.getQuan()));
			
			if(g.getSale()){
				String valueIds =g.getSaleVo().getPropertyValueIds();
				StringBuilder names = getPropertyDefName(gbo.getGoodsId(), valueIds);
				gbo.setPropertyDef(valueIds);
				gbo.setPropertyDefDesc(names.toString());
			}
			else{
				String valueIds =g.getGoodsInvo().getPropertyValueIds();
				String names = g.getGoodsInvo().getValueNames();
				gbo.setPropertyDef(valueIds);
				gbo.setPropertyDefDesc(names);
			}
			
			orderGoodsBos.add(gbo);
		}
		return orderGoodsBos;
	}
	/**
	 * 取得SKU的图片
	 * @param goodsBos
	 * @return key-skuid,value-图片路径
	 */
	private Map<Long,String> getSkuImage(List<SkuPriceInVo> goodsBos){
		int size = goodsBos.size();
		if(size ==0){
			return Collections.emptyMap();
		}
				
		List<Long> skuIds = new ArrayList<>(size);
		for(SkuPriceInVo g : goodsBos){
			skuIds.add(g.getSkuId());
		}
		final String SKUID=GoodsSkuDefBo.Key.id.toString(),URL=GoodsSkuDefBo.Key.imageUrl.toString();
		Wrapper<GoodsSkuDefBo> wp = new EntityWrapper<>();
		wp.in(SKUID, skuIds);
		wp.setSqlSelect(SKUID,URL);
		List<Map<String, Object>> rds = goodsSkuDefBiz.selectMaps(wp);
		
		Map<Long,String> result = new HashMap<>();
		for(Map<String, Object> m : rds){
			Number n = (Number)m.get(SKUID);
			Long id ;
			if(n instanceof Long){
				id = (Long)n;
			}
			else{
				id = new Long(n.longValue());
			}
			String url = (String)m.get(URL);
			result.put(id, url);
		}
		return result;
	}
	
	/**取订单的过期时间*/
	private Calendar getOrderTimeout(){
		int delay = SysParamCache.getUnfreezeCacheDelay(sysparamService);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, delay);
		return c;
	}
	
	/**
	 * @param propertyValueIds
	 * @return 属性及属性值，{@link GoodsSkuDefBo#getValueNames()}
	 */
	private StringBuilder getPropertyDefName(Long goodsId, String propertyValueIds){
		String[] ids = propertyValueIds.split("\\(");
		List<String> valueIds = new ArrayList<>();
		for(String s : ids){
			s = s.replace(')', ' ').trim();
			if(Tools.string.isNotEmpty(s)){
				valueIds.add(s);
			}
		}
		Wrapper<GoodsSpecificationDefBo> wp = new EntityWrapper<>();
		wp.eq(GoodsSpecificationDefBo.Key.goodsId.toString(), goodsId);
		wp.in(GoodsSpecificationDefBo.Key.propertyValueId.toString(), valueIds);
		List<GoodsSpecificationDefBo> specRds = goodsSpecificationDefBiz.selectList(wp);
		
		Wrapper<BaseCategoryPropertyValueBo> wpv = new EntityWrapper<>();
		wpv.in(BaseCategoryPropertyValueBo.Key.id.toString(), valueIds);
		List<BaseCategoryPropertyValueBo> valueRds = categoryPropertyValueBiz.selectList(wpv);
		
		List<Long> proids =new ArrayList<>();
		for(BaseCategoryPropertyValueBo pv : valueRds){
			proids.add(pv.getPropertyId());
		}
		
		Wrapper<BaseCategoryPropertyBo> wpb = new EntityWrapper<>();
		wpb.in(BaseCategoryPropertyBo.Key.id.toString(), proids);
		List<BaseCategoryPropertyBo> propertyBos = categoryPropertyBiz.selectList(wpb);
		
		StringBuilder result = new StringBuilder();
		for(GoodsSpecificationDefBo bo : specRds){
			BaseCategoryPropertyValueBo vbo = getBo(valueRds, bo.getPropertyValueId());
			BaseCategoryPropertyBo pbo = getBo(propertyBos,vbo.getPropertyId());
			
			String valueName = Tools.string.nullif(bo.getCustomValueName(), vbo.getValueName());
			result.append(pbo.getPropertyName()).append(':').append(valueName).append(';');
		}
		if(result.length()>0){
			result.deleteCharAt(result.length()-1);
		}
		return result;
	}
	private <T extends BaseBo> T getBo(List<T> rds,Long id){
		T rs = null;
		for(T t : rds ){
			if(t.getId().equals(id)){
				rs = t;
				break;
			}
		}
		return rs;
	}
	
	/**
	 * 按仓库分单
	 * @param skuVos
	 * @return
	 */
	private Map<Long,List<SkuPriceInVo>> doDecompose(List<SkuPriceInVo> skuVos){
		Map<Long,List<SkuPriceInVo>> rs = new HashMap<>();
		for(SkuPriceInVo vo : skuVos){
			Long storeId = vo.getGoodsBo().getStorehouseId();
			List<SkuPriceInVo> skus = rs.get(storeId);
			if(skus==null){
				skus = new ArrayList<>();
				rs.put(storeId, skus);
			}
			skus.add(vo);
		}
		return rs;
	}


	@Override
	public Map<Long,List<OrderGoodsVo>> getConfirmOrder(List<OrderGoodsSkuVo> valueids) {
		List<SkuPriceInVo> skuBos = getGoodsInventory(valueids);
		Map<Long,List<SkuPriceInVo>> storeGoods = this.doDecompose(skuBos);
		Map<Long,List<OrderGoodsVo>>  result = new HashMap<>();
		
		for(Map.Entry<Long,List<SkuPriceInVo>> store : storeGoods.entrySet()){
			List<SkuPriceInVo> storeGoodsSkuBos = store.getValue();
			List<OrderGoodsBo> goodsBos = this.getOrderGoodsBos(null, storeGoodsSkuBos);
			List<OrderGoodsVo> goodsVos = new ArrayList<>(goodsBos.size()); 
			//查找促销
			for(OrderGoodsBo bo :goodsBos){
				OrderGoodsVo vo = Tools.bean.cloneBean(bo, new OrderGoodsVo());
				GoodsPriceBo prcBo = getGoodsPriceBo(vo, storeGoodsSkuBos);
				if(prcBo != null){
					vo.setOrgiPrice(prcBo.getPrice());
				}
				goodsVos.add(vo);
			}
						
			result.put(store.getKey(), goodsVos);
		}
		return result;
	}
	
	/**
	 * 如果商品是促销活动，则取原来的库存价格，否则返回null
	 * @param vo
	 * @param storeGoodsSkuBos
	 * @return
	 */
	private GoodsPriceBo getGoodsPriceBo(OrderGoodsVo vo ,List<SkuPriceInVo> storeGoodsSkuBos){
		Long goodsId = vo.getGoodsId(),skuId = vo.getSkuId();
		SkuPriceInVo skuVo = null;
		for(SkuPriceInVo so :storeGoodsSkuBos){
			if(so.getSkuId().equals(skuId) && so.getGoodsBo().getId().equals(goodsId)){
				skuVo = so;
				break;
			}
		}
		GoodsPriceBo prcBo = null;
		if(skuVo !=null && skuVo.getSale()){
			Wrapper<GoodsPriceBo> wp = new EntityWrapper<>();
			wp.eq(GoodsPriceBo.Key.goodsId.toString(), goodsId);
			wp.eq(GoodsPriceBo.Key.skuId.toString(), skuId);
			wp.last("limit 1");
			prcBo = goodsPriceBiz.selectOne(wp);
		}
		return prcBo;
	}
	
	/**
	 * 按商品计算运费
	 * @param goodBos
	 * @return
	 */
	@Override
	public ShipmentBo calcFreightGoods(List<OrderGoodsBo> goodBos){
		if(Tools.collection.isEmpty(goodBos)){
			return null;
		}
		Map<Long,ShipmentBo> goodsShipmentBo = getGoodsShipmentBo(goodBos);
		if(goodsShipmentBo.size() < goodBos.size()){
			Map<Long,ShipmentBo> categoryShipmentBo = getCategoryShipmentBo(goodBos,goodsShipmentBo);
			goodsShipmentBo.putAll(categoryShipmentBo);
		}
		if(goodsShipmentBo.isEmpty()){
			return null;
		}
		List<ShipmentBo> shipBos = new ArrayList<>(goodsShipmentBo.values());
		log.info("===============查找运费");
		//找出最大的运费
		ShipmentBo rs = null;
		BigDecimal fee = BigDecimal.ZERO;
		for(ShipmentBo bo : shipBos){
			BigDecimal f = bo.getFee();
			if(f!=null && f.compareTo(fee)>0){
				fee = f;
				rs = bo;
			}
		}
		log.info("rs.getFee():{}", rs.getFee());
		log.info("===============查找运费");
		return rs;
	}
	
	/**
	 * 按商品分类返回每个商品的运费
	 * @param orderGoodBos
	 * @param goodsShipmentBo 排除已有运费的
	 * @return 返回Map(GoodsId,ShipmentBo)
	 */
	private Map<Long,ShipmentBo> getCategoryShipmentBo(List<OrderGoodsBo> orderGoodBos,Map<Long,ShipmentBo> goodsShipmentBo){
		List<Long> goodsIds = new ArrayList<>();
		for(OrderGoodsBo bo : orderGoodBos){
			Long goodsId = bo.getGoodsId();
			if(!goodsShipmentBo.containsKey(goodsId)){
				goodsIds.add(bo.getGoodsId());	//排除已有运费的
			}
		}
		
		Wrapper<ShipmentBo> sp = new EntityWrapper<>();
		sp.eq(ShipmentBo.Key.shipmentType.toString(), ShipmentTypeEnum.Category);
		//sp.isNull(ShipmentBo.Key.goodsId.toString());
		List<ShipmentBo> goodShipBos = shipmentBiz.selectList(sp);
		
		//按分类的长度排序(倒排)
		Collections.sort(goodShipBos, new Comparator<ShipmentBo>() {
			@Override
			public int compare(ShipmentBo o1, ShipmentBo o2) {
				String firsts=Tools.string.nullif(o1.getCategoryPaths(),""),lasts = Tools.string.nullif(o2.getCategoryPaths(),"");
				int first =  firsts.length();
				int last =  lasts.length();
				int r = (last -  first);
				if (r==0){
					r = lasts.compareToIgnoreCase(firsts);
				}
				return r;
			}
		});
		
		Wrapper<GoodsBo> gp = new EntityWrapper<>();
		gp.in(GoodsBo.Key.id.toString(), goodsIds);
		List<GoodsBo> goodsBos = goodsBiz.selectList(gp); 
		Map<Long,ShipmentBo> rs = new HashMap<>();
		
		for(GoodsBo bo : goodsBos){
			String category = bo.getCategoryPath();
			if(Tools.string.isEmpty(category)){
				continue;
			}
			for(ShipmentBo so :goodShipBos){
				String shipCate = so.getCategoryPaths();
				if(category.indexOf(shipCate)==0 && !rs.containsKey(bo.getId())){
					//匹配(前面已经按分类的长度排序了，使用第一个是最精确的)
					rs.put(bo.getId(), so);
					break;
				}
			}
		}
		return rs;
	}
	
	/**
	 * 按商品id返回每个商品的运费
	 * @param goodBos
	 * @return 返回Map(GoodsId,ShipmentBo)
	 */
	private Map<Long,ShipmentBo> getGoodsShipmentBo(List<OrderGoodsBo> goodBos){
		List<Long> goodsIds = new ArrayList<>();
		for(OrderGoodsBo bo : goodBos){
			goodsIds.add(bo.getGoodsId());
		}
		Wrapper<ShipmentBo> sp = new EntityWrapper<>();
		sp.eq(ShipmentBo.Key.shipmentType.toString(), ShipmentTypeEnum.Goods);
		sp.in(ShipmentBo.Key.goodsId.toString(), goodsIds);
		List<ShipmentBo> goodShipBos = shipmentBiz.selectList(sp);
		
		Map<Long,ShipmentBo> rs = new HashMap<>();
		for(OrderGoodsBo bo : goodBos){
			//goodsId.add(bo.getGoodsId());
			Long goodsId = bo.getGoodsId();
			for(ShipmentBo so :goodShipBos){
				if(so.getGoodsId().equals(goodsId)){
					rs.put(goodsId, so);
					break;
				}
			}
		}
		return rs;
	}
}

/**商品库存信息*/
class SkuPriceInVo{
	private final FlashGoodsSkuBo saleVo;
	private final SkuPriceInventoryVo goodsVo;
	private int quan;
	private final GoodsBo goodsBo;
	private final Long skuId;
	public SkuPriceInVo(FlashGoodsSkuBo vo,GoodsBo goodsBo){
		saleVo = vo;
		this.goodsBo = goodsBo;
		skuId = vo.getGoodsSkuDefId();
		goodsVo = null;
	}
	public SkuPriceInVo(SkuPriceInventoryVo vo,GoodsBo goodsBo){
		goodsVo = vo;
		this.goodsBo = goodsBo;
		skuId = vo.getSkuId();
		saleVo = null;
	}
	
	/** 可以销售的库存数量(已经处理了冻结数量)*/
    public int getInventory(){
    	int inv =0;
    	if(goodsVo!=null){
    		int freeze = Tools.number.nullIf(goodsVo.getFreeze(),0);
    		inv = Tools.number.nullIf(goodsVo.getSaleStockNum(),0) - freeze;
    	}
    	else if(saleVo!=null){
    		int freeze = Tools.number.nullIf(saleVo.getFreezeCount(),0);
    		inv = Tools.number.nullIf( saleVo.getInventory() ,0) - Tools.number.nullIf( saleVo.getSaleCount() ,0);
    		inv = inv -freeze;
    	}
    	return inv;
    }
    /** 冻结的数量*/
    public int getFreeze(){
    	int inv =0;
    	if(goodsVo!=null){
    		inv = Tools.number.nullIf(goodsVo.getFreeze(),0);
    	}
    	else if(saleVo!=null){
    		inv = Tools.number.nullIf( saleVo.getFreezeCount() ,0) ;
    	}
    	return inv;
    }
    
    /**销售价(元)*/
    public BigDecimal getPrice(){
    	BigDecimal prc = null ;
    	if(goodsVo!=null){
    		//double p = Tools.number.nullIf(goodsVo.getPrice(),0.0d);
    		//prc = (int)(p  * 100);
    		prc = goodsVo.getPrice();
    	}
    	else if(saleVo!=null){
    		//double p = Tools.number.nullIf(saleVo.getPrice(),0.0d);
    		//prc = (int)(p  * 100);
    		prc = saleVo.getPrice();
    	}
    	return prc;
    }
    /**是否促销 */
    public boolean getSale(){
    	return (saleVo !=null);
    }
	public FlashGoodsSkuBo getSaleVo() {
		return saleVo;
	}
	public SkuPriceInventoryVo getGoodsInvo() {
		return goodsVo;
	}
	/**订单数量*/
	public int getQuan() {
		return quan;
	}
	public void setQuan(int quan) {
		this.quan = quan;
	}
	
	public BigDecimal getAmt(){
		BigDecimal rs = Tools.bigDecimal.mul(getPrice(), BigDecimal.valueOf(getQuan()));
		return rs;
	}
	public GoodsBo getGoodsBo() {
		return goodsBo;
	}
	/** SKUID {@link GoodsSkuDefBo#getId()}*/
	public Long getSkuId() {
		return skuId;
	}
}
