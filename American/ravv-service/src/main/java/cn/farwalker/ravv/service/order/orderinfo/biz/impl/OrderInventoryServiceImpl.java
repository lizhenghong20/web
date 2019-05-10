package cn.farwalker.ravv.service.order.orderinfo.biz.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.farwalker.ravv.service.flash.sale.biz.IFlashSaleBiz;
import cn.farwalker.ravv.service.flash.sale.model.FlashSaleBo;
import cn.farwalker.ravv.service.flash.sku.biz.IFlashGoodsSkuBiz;
import cn.farwalker.ravv.service.flash.sku.biz.IFlashSkuService;
import cn.farwalker.ravv.service.flash.sku.biz.impl.FlashSkuServiceImpl;
import cn.farwalker.ravv.service.flash.sku.model.FlashGoodsSkuBo;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsService;
import cn.farwalker.ravv.service.goods.inventory.biz.IGoodsInventoryBiz;
import cn.farwalker.ravv.service.goods.inventory.model.GoodsInventoryBo;
import cn.farwalker.ravv.service.order.ordergoods.biz.IOrderGoodsService;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoBiz;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInventoryService;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderStatusEnum;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.order.paymemt.biz.IOrderPaymemtBiz;
import cn.farwalker.ravv.service.order.time.OrderTaskTimer;
import cn.farwalker.waka.cache.CacheManager;
import cn.farwalker.waka.util.DateUtil.FORMAT;
import cn.farwalker.waka.util.Tools;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cangwu.frame.orm.core.BaseBo;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderInventoryServiceImpl implements IOrderInventoryService{
	private static final Logger log = LoggerFactory.getLogger(OrderInventoryServiceImpl.class);
	@Resource
	private IOrderInfoBiz orderInfoBiz;
	
	@Resource
	private IOrderGoodsService orderGoodsService;
	@Resource
	private IFlashSaleBiz flashSaleBiz;
	
	@Resource
	private IGoodsInventoryBiz goodsInventoryBiz; 
	@Resource
	private IFlashGoodsSkuBiz flashGoodsSkuBiz;
	@Resource
	private IFlashSkuService flashSkuBiz;
	
	@Resource
	private IOrderPaymemtBiz orderPaymemtBiz;
	
	/**解除冻结定时器*/
	@Resource
	private OrderTaskTimer orderTaskTimer;
	@Resource
	private IGoodsService goodsService;
	
	/**下单成功，更新库存*/
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Boolean updateBuySuccessGoodsInventory(Long... orderIds){
		if(orderIds.length==0){
			throw new WakaException("订单不能为空");
		}
		int size;
		List<OrderInfoBo> orderBos;{
			List<Long> ids = Arrays.asList(orderIds);
			Wrapper<OrderInfoBo> wporder = new EntityWrapper<>();
			wporder.in(OrderInfoBo.Key.id.toString(), ids);
			wporder.or().in(OrderInfoBo.Key.pid.toString(), ids);
			orderBos = orderInfoBiz.selectList(wporder);
			
			size = orderBos.size();
			if(size ==0){
				throw new WakaException("订单号不存在:" + orderIds);
			}
		}
		if(size ==1){
			OrderInfoBo singeOrderBo = orderBos.get(0);
			List<OrderGoodsBo> goodsBos = orderGoodsService.getOrderGoodsBos(singeOrderBo.getId());
			updateGoodsSuccessSub(singeOrderBo, goodsBos);
		}
		else{
			OrderInfoBo masterOrderBo = null;
			List<Long> childIds = new ArrayList<>();
			for(OrderInfoBo bo:orderBos){
				//OrderInfoBo bo = orderBos.get(i);
				if(Tools.number.nullIf(bo.getPid() ,0) ==0){
					masterOrderBo = bo;
					//orderBos.remove(i); //删除顶级订单号
				}
				else{
					childIds.add(bo.getId());
				}
			}
			Long[] ids = childIds.toArray(new Long[childIds.size()]);
			List<OrderGoodsBo> goodsBos = orderGoodsService.getOrderGoodsBos(ids);
			updateGoodsSuccessSub(masterOrderBo, goodsBos);
		}
		return true;
	}
	
	/*
	private List<OrderInfoBo> getOrderInfo(List<OrderGoodsBo> goodsBos){
		List<Long> orderIds = new ArrayList<>();
		for(OrderGoodsBo g: goodsBos){
			Long id = g.getOrderId();
			if(!orderIds.contains(id)){
				orderIds.add(id);
			}
		}
		List<OrderInfoBo> bos = orderInfoBiz.selectBatchIds(orderIds);
		return bos;
	}*/
	private <T  extends BaseBo> T getBo(List<T> bos,Long id){
		T bo = null;
		for(T b : bos){
			if(id.equals(b.getId())){
				bo = b;
				break;
			}
		}
		return bo;
	}
	
	/**冻结*/
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer updateOrderFreeze(List<OrderInfoBo> orderBos,List<OrderGoodsBo> goodsBos) {
		int size=  goodsBos.size();
		List<GoodsInventoryBo> invBos = new ArrayList<>(size);
		List<FlashGoodsSkuBo> saleInvBos = new ArrayList<>(size);
		for(OrderGoodsBo g:goodsBos){
			GoodsInventoryBo invBo = freezeGoodsInventory(g,true);
			if(invBo!=null){
				invBos.add(invBo);
			}
			OrderInfoBo orderBo = getBo(orderBos, g.getOrderId());
			List<String> saleIds = getCacheSaleEnableIds(orderBo.getGmtCreate());
			FlashGoodsSkuBo saleInvbo = freezeSaleInventory(saleIds,g,true);
			if(saleInvbo!=null){
				saleInvBos.add(saleInvbo);
			}
		}
		if(Tools.collection.isNotEmpty(invBos)){
			goodsInventoryBiz.updateBatchById(invBos);
		}
		if(Tools.collection.isNotEmpty(saleInvBos)){
			flashGoodsSkuBiz.updateBatchById(saleInvBos);
		}
		return Integer.valueOf(size);
	}
	/**解除冻结(不更新状态)*/
	private void unfreezeOrder(List<OrderInfoBo> orderBos,List<OrderGoodsBo> goodsBos) {
		int size=  goodsBos.size();
		List<GoodsInventoryBo> invBos = new ArrayList<>(size);
		List<FlashGoodsSkuBo> saleInvBos = new ArrayList<>(size);
		for(OrderGoodsBo g:goodsBos){
			GoodsInventoryBo invBo = freezeGoodsInventory(g,false);
			if(invBo!=null){
				invBos.add(invBo);
			}
			OrderInfoBo orderBo = getBo(orderBos, g.getOrderId());
			List<String> saleIds = getCacheSaleEnableIds(orderBo.getGmtCreate());
			FlashGoodsSkuBo saleInvbo = freezeSaleInventory(saleIds,g,false);
			if(saleInvbo!=null){
				saleInvBos.add(saleInvbo);
			}
			//g.setOrderGoodsStatus(orderGoodsStatus);
			
		}
		if(Tools.collection.isNotEmpty(invBos)){
			goodsInventoryBiz.updateBatchById(invBos);
		}
		if(Tools.collection.isNotEmpty(saleInvBos)){
			flashGoodsSkuBiz.updateBatchById(saleInvBos);
		}
	}
 

	/**
	 * 更新库存冻结数
	 * @param goodsBos
	 * @param isFreeze true:冻结/false:解除
	 * @return
	 */
	private GoodsInventoryBo freezeGoodsInventory(OrderGoodsBo goodsBos,boolean isFreeze) {
		//goodsService.getGoodsSkuPriceVo(goodsBos.getSkuId());
		Wrapper<GoodsInventoryBo> invQuery = new EntityWrapper<>();
		invQuery.eq(GoodsInventoryBo.Key.skuId.toString(), goodsBos.getSkuId());
		GoodsInventoryBo invBo = goodsInventoryBiz.selectOne(invQuery);
		int quan = Tools.number.nullIf(goodsBos.getQuantity(),0);
		
		if(invBo!=null){
			//int stock = Tools.number.nullIf(invBo.getSaleStockNum(),0);
			int freeze = Tools.number.nullIf(invBo.getFreeze(),0);
			if(isFreeze){//冻结
				//stock -= quan; //库存减少
				freeze += quan;//冻结增加
			}
			else{
				//stock += quan; //库存增加
				freeze -= quan;//冻结减少
			}
			
			if(freeze<0){
				log.error("冻结数量小于零,sku=" + goodsBos.getSkuId());
				freeze =0;//可能有小于零的情况，忽略
			}
			
			//invBo.setSaleStockNum(Integer.valueOf(stock));
			invBo.setFreeze(Integer.valueOf(freeze));
		}
		return invBo;
	}
	
 
	/**
	 * 更新促销冻结数<br/>
	 * 获取数据的语句要参考 创建订单时的语句,促销是按flashSaleId排序<br/>
	 * {@link OrderCreateServiceImpl#createOrder(List, Long, java.math.BigDecimal, Long, String, Long)}<br/>
	 * {@link FlashSkuServiceImpl#getGoodsSkuBo(Long, Long)}
	 * @param saleIds 订单下单时间的有效促销
	 * @param goodsBos
	 * @param isFreeze true:冻结/false:解除
	 * @return
	 */
	private FlashGoodsSkuBo freezeSaleInventory(List<String> saleIds, OrderGoodsBo goodsBos,boolean isFreeze) {
		//goodsService.getGoodsSkuPriceVo(goodsBos.getSkuId());
		Wrapper<FlashGoodsSkuBo> invQuery = new EntityWrapper<>();
		invQuery.in(FlashGoodsSkuBo.Key.flashSaleId.toString(), saleIds);
		invQuery.eq(FlashGoodsSkuBo.Key.goodsSkuDefId.toString(), goodsBos.getSkuId());
		invQuery.orderBy(FlashGoodsSkuBo.Key.flashSaleId.toString());//保持每次加载都是相同的顺序
		invQuery.last("limit 1");
		
		FlashGoodsSkuBo invBo = flashGoodsSkuBiz.selectOne(invQuery);
		int quan = Tools.number.nullIf(goodsBos.getQuantity(),0);
		if(invBo!=null){
			int freeze = Tools.number.nullIf(invBo.getFreezeCount(),0);
			int inv = Tools.number.nullIf(invBo.getInventory(),0);
			if(isFreeze){
				freeze += quan;//冻结
			}
			else{
				freeze -= quan;//解除冻结
			}
			if(freeze <0){
				log.error("冻结数量小于零,sku=" + goodsBos.getSkuId() + ",FlashGoodsSkuBo=" +invBo.getId());
				freeze =0;
			}
			else if(freeze > inv){
				log.error("冻结数量大于库存,sku=" + goodsBos.getSkuId()+ ",FlashGoodsSkuBo=" +invBo.getId());
				freeze = inv;
			}
			invBo.setFreezeCount(Integer.valueOf(freeze));
		}
		return invBo;
	}
 
	
	/**
	 * 取得未付款的订单(拆单时，处理了全部子单)
	 * @param orderIds
	 * @return
	 */
	private List<OrderInfoBo> getFreezeOrderUnpaid(Long... orderIds){
		if(orderIds==null || orderIds.length==0){
			throw new WakaException("订单号不能为空");
		}
		List<OrderStatusEnum> states =Arrays.asList(OrderStatusEnum.REVIEWADOPT_UNPAID);//,OrderStatusEnum.CREATED_UNREVIEW);
		List<Long> ids=Arrays.asList(orderIds);
		Wrapper<OrderInfoBo> query = new EntityWrapper<>();
		query.in(OrderInfoBo.Key.id.toString(), ids);
		query.in(OrderInfoBo.Key.orderStatus.toString(), states);
		query.or().in(OrderInfoBo.Key.pid.toString(), ids)
			.in(OrderInfoBo.Key.orderStatus.toString(), states);
		
		//query.setSqlSelect(OrderInfoBo.Key.id.toString());
		
		List<OrderInfoBo> orderBos = orderInfoBiz.selectList(query);
		if(Tools.collection.isEmpty(orderBos)){
			log.info("订单已支付，不做处理");
//			throw new WakaException("只能是为支付的订单才能冻结:" + ids);
		}
		return orderBos;
	}
	
	/**下单成功，更新库存*/
	private void updateGoodsSuccessSub(OrderInfoBo masterOrderBo,List<OrderGoodsBo> goodsBos){
		List<FlashGoodsSkuBo> flashSkuBos = new ArrayList<>(goodsBos.size());
		List<GoodsInventoryBo> goodsInvBos = new ArrayList<>(goodsBos.size());
		
		Date orderDate = masterOrderBo.getGmtCreate();
		if(orderDate == null){
			orderDate = new Date();
			//Date today = new Date();	
		}
		List<String> saleIds = getCacheSaleEnableIds(orderDate);
		
		for(OrderGoodsBo go : goodsBos){
			int quan = Tools.number.nullIf(go.getQuantity(), 0);
			
			//更新促销数量
			FlashGoodsSkuBo saleSku = getFlashSaleGoodsSkuVo(saleIds, go.getSkuId());
			if(saleSku!=null){
				int saleCount = Tools.number.nullIf( saleSku.getSaleCount(),0) + quan;
				saleSku.setSaleCount(Integer.valueOf(saleCount));
				
				//促销减少冻结
				int freezeSale = Tools.number.nullIf(saleSku.getFreezeCount(), 0) - quan;
				saleSku.setFreezeCount(Integer.valueOf(freezeSale>0 ? freezeSale :0));
				flashSkuBos.add(saleSku);
				//flashSkuBiz.updateById(saleSku);
			}
			//更新库存
			GoodsInventoryBo skuInvBo = getGoodsInventoryBo(go.getSkuId());
			int stockNum = Tools.number.nullIf( skuInvBo.getSaleStockNum(),0) - quan;
			skuInvBo.setSaleStockNum(Integer.valueOf(stockNum));
			
			//库存减少冻结
			int freezeGoods = Tools.number.nullIf(skuInvBo.getFreeze(),0) - quan;
			skuInvBo.setFreeze(Integer.valueOf(freezeGoods >0 ? freezeGoods: 0)); 
			goodsInvBos.add(skuInvBo);
		}
		goodsInventoryBiz.updateBatchById(goodsInvBos);//更新库存数量
		if(Tools.collection.isNotEmpty(flashSkuBos)){
			flashGoodsSkuBiz.updateBatchById(flashSkuBos);//更新促销数量
		}
	}
	
	/**
	 * 取得下单时的促销活动(有效的记录)
	 * @return
	 */
	private List<String> getCacheSaleEnableIds(Date orderDate){
		final String saleKey;{//每分钟缓存
			Calendar cd = Calendar.getInstance();
			cd.setTime(orderDate);
			StringBuilder saleDate = Tools.date.formatDate( cd, FORMAT.YYYYMMDDHHMMSS);
			saleKey ="order_sale_" + saleDate.substring(0, 12);
		}
		String rds = CacheManager.cache.get(saleKey);
		if(Tools.string.isNotEmpty(rds)){
			String[] ids = rds.split(",");
			return Arrays.asList(ids);
		}
		
		//Date nows = new Date();
		Wrapper<FlashSaleBo> wpsale = new EntityWrapper<>();
		wpsale.le(FlashSaleBo.Key.starttime.toString(), orderDate);
		wpsale.ge(FlashSaleBo.Key.endtime.toString(), orderDate);
		wpsale.orderBy(FlashSaleBo.Key.id.toString());//保持每次加载都是相同的顺序
		wpsale.setSqlSelect(FlashSaleBo.Key.id.toString());
		
		List<FlashSaleBo> saleBos =flashSaleBiz.selectList(wpsale);
		StringBuilder saleIds = new StringBuilder();
		List<String> result = new ArrayList<>(saleBos.size());
		for(FlashSaleBo b:saleBos){
			String e =String.valueOf(b.getId());
			result.add(e);
			saleIds.append(',').append(e);
		}
		if(saleIds.length()>0){
			saleIds.deleteCharAt(0);
			CacheManager.cache.put(saleKey,saleIds.toString());
		}
		
		return result;
	}
	/**促销数量*/
	private FlashGoodsSkuBo getFlashSaleGoodsSkuVo(List<String> saleIds,Long goodsSkuId){
		final String SaleID=FlashGoodsSkuBo.Key.flashSaleId.toString();
		Wrapper<FlashGoodsSkuBo> wpfsku = new EntityWrapper<>();
		wpfsku.in(SaleID, saleIds);
		wpfsku.eq(FlashGoodsSkuBo.Key.goodsSkuDefId.toString(), goodsSkuId);
		wpfsku.orderBy(SaleID);//按id排序，保证每次取得数据一致
		wpfsku.last("limit 1");
		FlashGoodsSkuBo flashSkuBos = flashGoodsSkuBiz.selectOne(wpfsku);
		return flashSkuBos;
	}
	
	/**商品库存*/
	private GoodsInventoryBo getGoodsInventoryBo(Long goodsSkuId){
		Wrapper<GoodsInventoryBo> wpinv = new EntityWrapper<>();
		wpinv.eq(GoodsInventoryBo.Key.skuId.toString(), goodsSkuId);
		wpinv.last("limit 1");
		GoodsInventoryBo flashSkuBos = goodsInventoryBiz.selectOne(wpinv);
		return flashSkuBos;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Long[] updateOrderUnfreeze(Long... orderIds) {
		List<OrderInfoBo> orderBos= getFreezeOrderUnpaid(orderIds);
		if(Tools.collection.isNotEmpty(orderBos)){
			log.info("======================================执行取消订单任务，取消个数:{},第一个订单号:{}",
																			orderBos.size(), orderBos.get(0).getId());
			Long[] loadIds = new Long[orderBos.size()];
			for(int i =0 ;i < loadIds.length;i++){
				loadIds[i] = orderBos.get(i).getId();
			}
			List<OrderGoodsBo> goodsBos = orderGoodsService.getOrderGoodsBos(loadIds);
			unfreezeOrder(orderBos, goodsBos);

			for(OrderInfoBo bo : orderBos){
				bo.setOrderStatus(OrderStatusEnum.CANCEL);
			}

			if(!orderInfoBiz.updateBatchById(orderBos)){
				throw new WakaException(RavvExceptionEnum.UPDATE_ERROR + "取消订单执行失败,orderId=" + orderIds);
			}


			/////////////////////////////////
 		/*
 		Wrapper<OrderPaymemtBo> wpPay = new EntityWrapper<>();
 		wpPay.in(OrderPaymemtBo.Key.orderId.toString(), Arrays.asList(loadIds));
 		OrderPaymemtBo payVo = new OrderPaymemtBo();
 		orderPaymemtBiz.update(payVo, wpPay);
 		*/

 		/*
 		for(OrderGoodsBo g : goodsBos){
 			g.setOrderGoodsStatus(OrderGoodsStatusEnum.);
 		}*/

			return loadIds;
		}else{
			log.info("订单已支付，不做操作");
		}
		return null;
	}
}
