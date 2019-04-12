package cn.farwalker.ravv.service.goodssku.skudef.biz.impl;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import cn.farwalker.ravv.service.category.value.biz.IBaseCategoryPropertyValueBiz;
import cn.farwalker.ravv.service.category.value.model.BaseCategoryPropertyValueBo;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuDefBo;
import cn.farwalker.ravv.service.goodssku.specification.biz.IGoodsSpecificationDefBiz;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.farwalker.ravv.service.goods.inventory.biz.IGoodsInventoryBiz;
import cn.farwalker.ravv.service.goods.inventory.model.GoodsInventoryBo;
import cn.farwalker.ravv.service.goods.price.biz.IGoodsPriceBiz;
import cn.farwalker.ravv.service.goods.price.model.GoodsPriceBo;
import cn.farwalker.ravv.service.goodssku.skudef.biz.IGoodsSkuDefBiz;
import cn.farwalker.ravv.service.goodssku.skudef.biz.IGoodsSkuService;
import cn.farwalker.ravv.service.goodssku.skudef.model.SkuPriceInventoryVo;
import cn.farwalker.waka.util.Tools;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cangwu.frame.orm.core.BaseBo;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品SKU定义<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class GoodsSkuServiceImpl  implements IGoodsSkuService{
	@Resource
	private IGoodsInventoryBiz inventoryBiz;
	@Resource
	private IGoodsPriceBiz priceBiz;
	@Resource
	private IGoodsSkuDefBiz skudefBiz;

	@Autowired
	private IGoodsSpecificationDefBiz iGoodsSpecificationDefBiz;

	@Autowired
	private IBaseCategoryPropertyValueBiz  iBaseCategoryPropertyValueBiz;
	
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer updateSkuInvent(Long goodsId, List<SkuPriceInventoryVo> prinvBos) { 
		List<GoodsPriceBo> priceBos = new ArrayList<>(prinvBos.size());
		List<GoodsInventoryBo> invBos = new ArrayList<>(prinvBos.size());
		
		for(SkuPriceInventoryVo vo : prinvBos){
			vo.setGoodsId(goodsId);
			GoodsPriceBo prc = Tools.bean.cloneBean(vo,  new GoodsPriceBo());
			prc.setId(vo.getPriceId());
			priceBos.add(prc);
			
			GoodsInventoryBo inv = Tools.bean.cloneBean(vo, new GoodsInventoryBo());
			inv.setId(vo.getInventoryId());
			invBos.add(inv);
			
			skudefBiz.updateById(vo);
		}
		return this.updateSkuInvent(goodsId, priceBos, invBos);
	}

	@Override
	public Integer updateSkuInvent(Long goodsId,List<GoodsPriceBo> priceBos,
			List<GoodsInventoryBo> invBos) {
		int priceSize =(priceBos == null ?0: priceBos.size());
		int invSize =(invBos == null ?0: invBos.size());
		if(priceSize != invSize){
			throw new WakaException("SKU的库存与价格记录数不匹配!");
		}
		//复制并且删除旧记录数据
		int inv = copyDeleteInv(goodsId, invBos);
		int prc = copyDeletePrice(goodsId, priceBos);
		return Integer.valueOf(inv+prc);
	}
	
	/**
	 * 保存新记录(保留相同id的旧数据)并且删除旧记录数据
	 * @param skuId
	 * @param invBos
	 * @return 返回删除的记录数
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	private int copyDeleteInv(Long goodsId,List<GoodsInventoryBo> invBos){
		Wrapper<GoodsInventoryBo> queryInv  = new EntityWrapper<>();
		queryInv.eq(GoodsInventoryBo.Key.goodsId.toString(), goodsId);
		List<GoodsInventoryBo> oldinvBos =inventoryBiz.selectList(queryInv);
	
		for(int i =0,size = invBos.size();i < size;i++){
			GoodsInventoryBo bo = invBos.get(i);
			GoodsInventoryBo oldbo = getBo(bo.getId(), oldinvBos, false);
			if(oldbo!=null){ //保留旧记录的数据
				Tools.bean.copyProperties(bo, oldbo);
				invBos.set(i, oldbo);
			}
		}
		
		//删除旧记录数据
		if(oldinvBos.size()>0){
			List<Long> delIds = new ArrayList<>(oldinvBos.size());
			for(GoodsInventoryBo bo :oldinvBos){
				delIds.add(bo.getId());
			}
			inventoryBiz.deleteBatchIds(delIds);
		}
		
		inventoryBiz.insertBatch(invBos);
		return oldinvBos.size();
	}
	/**
	 * 保存新记录(保留相同id的旧数据)并且删除旧记录数据
	 * @param skuId
	 * @param priceBos
	 * @return 返回删除的记录数
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	private int copyDeletePrice(Long goodsId,List<GoodsPriceBo> priceBos){
		Wrapper<GoodsPriceBo> queryPrice  = new EntityWrapper<>();
		queryPrice.eq(GoodsPriceBo.Key.goodsId.toString(), goodsId);
		List<GoodsPriceBo> oldprcBos =priceBiz.selectList(queryPrice);
	
		for(int i =0,size = priceBos.size();i < size;i++){
			GoodsPriceBo bo = priceBos.get(i);
			GoodsPriceBo oldbo = getBo(bo.getId(), oldprcBos, false);
			if(oldbo!=null){ //保留旧记录的数据
				Tools.bean.copyProperties(bo, oldbo);
				priceBos.set(i, oldbo);
			}
		}
		
		//删除旧记录数据
		if(oldprcBos.size()>0){
			List<Long> delIds = new ArrayList<>(oldprcBos.size());
			for(GoodsPriceBo bo :oldprcBos){
				delIds.add(bo.getId());
			}
			priceBiz.deleteBatchIds(delIds);
		}
		
		priceBiz.insertBatch(priceBos);
		return oldprcBos.size();
	}
	
	private <T extends BaseBo> T getBo(Long id,List<T> rds,boolean remove){
		if(id == null){
			return null;
		}
		T rs = null;
		for(int i = 0 ,size = rds.size();i < size ;i++){
			T t = rds.get(i);
			if(id.equals(t.getId())){
				rs = t;
				
				if(remove){
					rds.remove(i);
				}
				break;
			}
		}
		return rs;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer deleteSku(Long skuId){
		Wrapper<GoodsInventoryBo> queryInv  = new EntityWrapper<>();
		queryInv.eq(GoodsInventoryBo.Key.skuId.toString(), skuId);
		GoodsInventoryBo invBos =inventoryBiz.selectOne(queryInv);
		if(invBos != null && Tools.number.nullIf(invBos.getSaleStockNum(), 0) >0){
			throw new WakaException("SKU还有库存，不能删除");
		}
		inventoryBiz.delete(queryInv);
		
		Wrapper<GoodsPriceBo> queryPrice  = new EntityWrapper<>();
		queryPrice.eq(GoodsPriceBo.Key.skuId.toString(), skuId);
		priceBiz.delete(queryPrice);
		
		skudefBiz.deleteById(skuId);		
		return Integer.valueOf(3);
	}

	public List<BaseCategoryPropertyValueBo> parseSkuId(Long skuId){
		if(skuId == 0)
			throw new  WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
		GoodsSkuDefBo result =  skudefBiz.selectById(skuId);
		if(result == null)
			throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
		String propertyValueIds = result.getPropertyValueIds();
		List<String> propertyValueIdList= Tools.string.convertStringList(propertyValueIds);
		if(propertyValueIdList == null)
			throw new WakaException(RavvExceptionEnum.DATA_PARSE_ERROR);
		//通过propertyValueId批量查询出所有属性和属性值
		List<BaseCategoryPropertyValueBo> categoryPropertyBoList = iBaseCategoryPropertyValueBiz.selectBatchIds(propertyValueIdList);
		return categoryPropertyBoList;
	}
























}