package cn.farwalker.ravv.service.flash.sku.biz.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.farwalker.ravv.service.flash.sale.biz.IFlashSaleBiz;
import cn.farwalker.ravv.service.flash.sale.model.FlashSaleBo;
import cn.farwalker.ravv.service.flash.sale.model.FlashSaleGoodsSkuVo;
import cn.farwalker.ravv.service.flash.sku.biz.IFlashGoodsSkuBiz;
import cn.farwalker.ravv.service.flash.sku.biz.IFlashSkuService;
import cn.farwalker.ravv.service.flash.sku.model.FlashGoodsSkuBo;
import cn.farwalker.waka.cache.CacheManager;
import cn.farwalker.waka.util.Tools;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

@Service
public class FlashSkuServiceImpl implements IFlashSkuService{
	@Resource
	private IFlashGoodsSkuBiz flashGoodsSkuBiz;
	@Resource
	private IFlashSaleBiz saleBiz;
	
	@Override
	public FlashSaleGoodsSkuVo getGoodsSkuBo(Long goodsId,
			List<Long> propertyValueIds) {
		
		List<String> saleIds = getCacheSaleIds();
		if(Tools.collection.isEmpty(saleIds)){
			return null;
		}
		
		//找促销商品
		final String vfn = FlashGoodsSkuBo.Key.propertyValueIds.toString(); 
		Wrapper<FlashGoodsSkuBo> query = new EntityWrapper<>();
		query.in(FlashGoodsSkuBo.Key.flashSaleId.toString(), saleIds);
		query.eq(FlashGoodsSkuBo.Key.goodsId.toString(), goodsId);
		for(Long id : propertyValueIds){
			query.like(vfn, "(" + id + ")");
		}
		List<FlashGoodsSkuBo> goodBos= flashGoodsSkuBiz.selectList(query);
		if(Tools.collection.isEmpty(goodBos)){
			return null;
		}
		// 
		FlashGoodsSkuBo bo =  goodBos.get(0);
		FlashSaleBo saleBo = saleBiz.selectById(bo.getFlashSaleId());
		FlashSaleGoodsSkuVo vo = Tools.bean.cloneBean(saleBo, new FlashSaleGoodsSkuVo());
		vo.setGoodsSkuVo(bo);
		return vo;
	}
	@Override
	public FlashSaleGoodsSkuVo getGoodsSkuBo(Long goodsId,Long skuId) {
		List<String> saleIds = getCacheSaleIds();
		if(Tools.collection.isEmpty(saleIds)){
			return null;
		}
		
		//找促销商品 
		Wrapper<FlashGoodsSkuBo> query = new EntityWrapper<>();
		query.in(FlashGoodsSkuBo.Key.flashSaleId.toString(), saleIds);
		query.eq(FlashGoodsSkuBo.Key.goodsId.toString(), goodsId);
		query.eq(FlashGoodsSkuBo.Key.goodsSkuDefId.toString(), skuId);
		query.orderBy(FlashGoodsSkuBo.Key.id.toString());//多个促销按id排序(一定要按id排序，因为涉及到冻结算法)
		
		query.last("limit 1");
		FlashGoodsSkuBo bo = flashGoodsSkuBiz.selectOne(query);
		if(bo == null){
			return null;
		}

		FlashSaleBo saleBo = saleBiz.selectById(bo.getFlashSaleId());
		FlashSaleGoodsSkuVo vo = Tools.bean.cloneBean(saleBo, new FlashSaleGoodsSkuVo());
		vo.setGoodsSkuVo(bo);
		return vo;
	}

	/**(有效的促销的活动)*/
	private List<String> getCacheSaleIds(){
		final String df = Tools.date.formatDate();
		final String key ="sale_" + df;
		String saleIds = CacheManager.cache.get(key); //秒级缓存
		
		if(saleIds == null){ //找正在促销的活动
			Date today = new Date();
			Wrapper<FlashSaleBo> querySale = new EntityWrapper<>();
			querySale.le(FlashSaleBo.Key.freezetime.toString(), today);
			querySale.ge(FlashSaleBo.Key.endtime.toString(), today);
			List<FlashSaleBo> saleBos = saleBiz.selectList(querySale);
			
			StringBuilder ids = new StringBuilder();
			for(FlashSaleBo bo : saleBos){
				ids.append(',').append(bo.getId());
			}
			if(ids.length()>0){
				ids.deleteCharAt(0);
			}
			saleIds = ids.toString();
			CacheManager.cache.put(key, saleIds);
		}
		if(Tools.string.isEmpty(saleIds)){
			return null;//没有促销
		} 
		else{
			return Arrays.asList(saleIds.split(","));
		}
	}
}
