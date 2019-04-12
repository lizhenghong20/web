package cn.farwalker.ravv.service.merchant.biz.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.farwalker.ravv.service.merchant.biz.IMerchantService;
import cn.farwalker.ravv.service.merchant.dao.IMerchantDao;
import cn.farwalker.ravv.service.merchant.model.MerchantOrderVo;
import cn.farwalker.ravv.service.merchant.model.MerchantSalesVo;
import cn.farwalker.ravv.service.order.ordergoods.biz.IOrderGoodsService;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsSimpleVo;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsDetailBiz;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsDetailBo;
import cn.farwalker.waka.util.Tools;


@Service
public class MerchantServiceImpl implements IMerchantService {
	@Resource
	private IMerchantDao merchantDao; 
	
	@Resource
	private IOrderGoodsService orderGoodsService;
	
	@Resource
	private IOrderReturnsDetailBiz orderReturnsDetailBiz;
	
	@Override
	public List<MerchantOrderVo> getMerchantOrderStats(List<Long> merchantIds,
			Date startdate, Date enddate) {
		List<MerchantOrderVo> rds = merchantDao.getGoodsCount(merchantIds, startdate, enddate);
		List<MerchantOrderVo> returnBos = merchantDao.getGoodsReturn(merchantIds, startdate, enddate);
		List<MerchantOrderVo> orderAmts = merchantDao.getTotalOrderAmt(merchantIds, startdate, enddate);
		List<MerchantOrderVo> returnAmts = merchantDao.getTotalReturnAmt(merchantIds, startdate, enddate);
		
		for(MerchantOrderVo vo : rds){
			Long mid = vo.getMerchantId();
			Integer orderCount = merchantDao.getOrderCount(mid, startdate, enddate);
			Integer orderCancel = merchantDao.getOrderCancel(mid, startdate, enddate);
			Integer orderFinish = merchantDao.getOrderFinish(mid, startdate, enddate);
			vo.setOrderCount(orderCount);
			vo.setOrderCancel(orderCancel);
			vo.setOrderFinish(orderFinish);
			
			MerchantOrderVo rb = findMerchantOrderVo(returnBos, mid);
			if(rb!=null){
				vo.setGoodsReturn(rb.getGoodsReturn());
			}
			
			MerchantOrderVo orderBoAmt = findMerchantOrderVo(orderAmts, mid);
			MerchantOrderVo returnBoAmt = findMerchantOrderVo(returnAmts, mid);
			BigDecimal oa = (orderBoAmt == null ? BigDecimal.ZERO :orderBoAmt.getTotalAmt());
			BigDecimal fa = (returnBoAmt == null ? BigDecimal.ZERO :returnBoAmt.getTotalAmt());
			BigDecimal totalAmt = Tools.bigDecimal.sub(oa,fa).setScale(2, BigDecimal.ROUND_HALF_UP);
			vo.setTotalAmt(totalAmt);
			
			vo.setStartdate(startdate);
			vo.setEnddate(enddate);
		}
		return rds;
	}
	
	private MerchantOrderVo findMerchantOrderVo(List<MerchantOrderVo> rds,Long merchantId){
		MerchantOrderVo vo = null;
		for(MerchantOrderVo v : rds){
			if(merchantId.equals(v.getMerchantId())){
				vo = v;
				break;
			}
		}
		return vo;
	}

	@Override
	public MerchantSalesVo getSalesStatistics(Long merchantId, Date startdate, Date enddate) {
		MerchantSalesVo merchantSalesVo = new MerchantSalesVo();
		
		//获取供应商某时间范围内的订单商品
		List<OrderGoodsSimpleVo> orderGoodsList = orderGoodsService.getOrderGoodsByMerchant(merchantId, startdate, enddate);
		if(Tools.collection.isEmpty(orderGoodsList)) {
			return merchantSalesVo;
		}
		
		//计算时间销售量和销售金额（减去退货和退款的数量和金额）
		for(OrderGoodsSimpleVo goodsVo : orderGoodsList) {
			//获取订单退货和退款列表
			List<OrderReturnsDetailBo> returnsDetailList = orderReturnsDetailBiz.getReturnsDetailList(goodsVo.getOrderId(), goodsVo.getGoodsId());
			if(Tools.collection.isNotEmpty(returnsDetailList)) {
				Integer refundQty = 0;//实际退回数量
				for(OrderReturnsDetailBo returnsDetail : returnsDetailList) {
					refundQty += returnsDetail.getActualRefundQty();
				}
				goodsVo.setQuantity(goodsVo.getQuantity() - refundQty);
			}
		}
		
    	//按月份分组
        Map<String,List<OrderGoodsSimpleVo>> orderGoodsSortList = orderGoodsList.stream().collect(Collectors.groupingBy(OrderGoodsSimpleVo::getGmtCreate));
        List<String> saleDateList = new ArrayList<>();
        List<Integer> saleGoodsNumList = new ArrayList<>();
        List<BigDecimal> saleAmountList = new ArrayList<>();
        for(List<OrderGoodsSimpleVo> orderGoodsSort : orderGoodsSortList.values()) {
        	String saleDate = null;//销售月份
        	Integer saleGoods = 0;//月销售量
        	BigDecimal saleAmount = new BigDecimal(0.00);//月销售额
        	
        	for(OrderGoodsSimpleVo goodsSimpleVo : orderGoodsSort) {
        		saleDate = goodsSimpleVo.getGmtCreate();
        		saleGoods += goodsSimpleVo.getQuantity();
        		BigDecimal quantity = new BigDecimal(goodsSimpleVo.getQuantity());
        		saleAmount = saleAmount.add(goodsSimpleVo.getPrice().multiply(quantity));
        	}
        	saleDateList.add(saleDate);
        	saleGoodsNumList.add(saleGoods);
        	saleAmountList.add(saleAmount);
        }
        
        merchantSalesVo.setSaleDateList(saleDateList);
        merchantSalesVo.setSaleGoodsNumList(saleGoodsNumList);
        merchantSalesVo.setSaleAmountList(saleAmountList);
        
		return merchantSalesVo;
	}
	
}
