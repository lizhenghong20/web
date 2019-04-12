package cn.farwalker.ravv.goods;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.farwalker.ravv.service.goods.inventory.biz.IGoodsInventoryBiz;
import cn.farwalker.ravv.service.goods.inventory.model.GoodsInventoryBo;
import cn.farwalker.ravv.service.goods.price.biz.IGoodsPriceBiz;
import cn.farwalker.ravv.service.goods.price.model.GoodsPriceBo;
import cn.farwalker.ravv.service.goodssku.skudef.biz.IGoodsSkuDefBiz;
import cn.farwalker.ravv.service.goodssku.skudef.biz.IGoodsSkuService;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuDefBo;
import cn.farwalker.ravv.service.goodssku.skudef.model.SkuPriceInventoryVo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * SKU单价及库存设置<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@RestController
@RequestMapping("/goods/goodsskuprice")
public class GoodsSkuPriceController{
    private final static  Logger log =LoggerFactory.getLogger(GoodsSkuPriceController.class);
    @Resource
    private IGoodsPriceBiz goodsPriceBiz;
    
    @Resource
    private IGoodsSkuDefBiz goodsSkudefBiz;
    
    @Resource
    private IGoodsSkuService goodsSkuService;
    
    @Resource
    private IGoodsInventoryBiz goodsInventoryBiz;
    
    
    /**删除记录
     * @param id sku表id<br/>
     */
    @RequestMapping("/delete")
    public JsonResult<Boolean> doDelete(@RequestParam Long skuid){
        //createMethodSinge创建方法
        if(skuid==null){
            return JsonResult.newFail("sku表id不能为空");
        }
        Integer rs = goodsSkuService.deleteSku(skuid);
        return JsonResult.newSuccess(rs.intValue()!=0);
    }
    /**
     * 列表记录
     * @param goodsid 商品ID<br/>
     */
    @RequestMapping("/skulist")
    public JsonResult<List<SkuPriceInventoryVo>> getSkulist(Long goodsid){
        //createMethodSinge创建方法
    	Wrapper<GoodsSkuDefBo> querySku = new EntityWrapper<>();
        querySku.eq(GoodsSkuDefBo.Key.goodsId.toString(), goodsid);
        List<GoodsSkuDefBo> skuBos = goodsSkudefBiz.selectList(querySku);
        
        Wrapper<GoodsPriceBo> queryPrice = new EntityWrapper<>();
        queryPrice.eq(GoodsPriceBo.Key.goodsId.toString(), goodsid);
        List<GoodsPriceBo> priceBos = goodsPriceBiz.selectList(queryPrice);
        
        Wrapper<GoodsInventoryBo> queryInvent = new EntityWrapper<>();
        queryInvent.eq(GoodsInventoryBo.Key.goodsId.toString(), goodsid);
        List<GoodsInventoryBo> inventBos = goodsInventoryBiz.selectList(queryInvent);
        
        List<SkuPriceInventoryVo> rs = margePriceInvent(skuBos,priceBos,inventBos);
        return JsonResult.newSuccess(rs);
    }
    
    private List<SkuPriceInventoryVo> margePriceInvent(List<GoodsSkuDefBo> skuBos ,List<GoodsPriceBo> priceBos ,List<GoodsInventoryBo> inventBos ){
    	List<SkuPriceInventoryVo> result = new ArrayList<>(skuBos.size());
    	for(GoodsSkuDefBo sku:skuBos){
    		Long skuId = sku.getId();
    		GoodsPriceBo prcbo = getBo(skuId, priceBos, true);
    		GoodsInventoryBo invbo = getBo(skuId, inventBos, false);
    		
    		SkuPriceInventoryVo vo  = Tools.bean.cloneBean(sku, new SkuPriceInventoryVo());
    		if(prcbo != null){
    			Tools.bean.cloneBean(prcbo,vo);
    			vo.setPriceId(prcbo.getId());
    		}
    		if(invbo!=null){
    			Tools.bean.cloneBean(invbo,vo);
    			vo.setInventoryId(invbo.getId());
    		}
    		vo.setGoodsId(sku.getGoodsId());
    		vo.setSkuId(skuId);
    		vo.setId(skuId);//前面的复制已经把id搞没了，这里要重新赋值
    		
    		String imageUrl = QiniuUtil.getFullPath(vo.getImageUrl());
    		vo.setImageUrl(imageUrl);
    		result.add(vo);
    	}
    	return result;
    }
    
    private <T> T getBo(Long skuId,List<T> rds,boolean isPriceBo){
    	T rs = null;
    	for(T e : rds){
    		Long objSku = (isPriceBo ? ((GoodsPriceBo)e).getSkuId():((GoodsInventoryBo)e).getSkuId());
    		if(skuId.equals(objSku)){
    			rs = e;
    			break;
    		}
    	}
    	return rs;
    }
    /**修改记录
     * @param goodsid 商品ID<br/>
     * @param prices 库存及单价信息<br/>
     */
    @RequestMapping("/update")
    public JsonResult<Integer> doUpdate(Long goodsid,@RequestBody List<SkuPriceInventoryVo> prices){
        //createMethodSinge创建方法
        if(goodsid==null){
            return JsonResult.newFail("商品ID不能为空");
        }
        if(Tools.collection.isEmpty(prices)){
            return JsonResult.newFail("库存及单价信息不能为空");
        }
        for(SkuPriceInventoryVo pv:prices){
        	String imageUrl = QiniuUtil.getRelativePath(pv.getImageUrl());
        	pv.setImageUrl(imageUrl);
        }
        Integer rs = goodsSkuService.updateSkuInvent(goodsid,prices);
        return JsonResult.newSuccess(rs);
    }
}