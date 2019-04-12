package cn.farwalker.ravv.goods;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.farwalker.ravv.goods.dto.GoodsPropertyVo;
import cn.farwalker.ravv.goods.dto.GoodsSpecVo;
import cn.farwalker.ravv.service.category.property.biz.IBaseCategoryPropertyBiz;
import cn.farwalker.ravv.service.category.property.constants.PropertyTypeEnum;
import cn.farwalker.ravv.service.category.property.model.BaseCategoryPropertyBo;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsBiz;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsService;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.goods.base.model.PropsVo;
import cn.farwalker.ravv.service.goods.utils.GoodsUtil;
import cn.farwalker.ravv.service.goodssku.skudef.biz.IGoodsSkuDefBiz;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuDefBo;
import cn.farwalker.ravv.service.goodssku.specification.biz.IGoodsSpecdefService;
import cn.farwalker.ravv.service.goodssku.specification.model.GoodsSpecificationDefBo;
import cn.farwalker.waka.constants.StatusEnum;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.base.controller.ControllerUtils;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cangwu.frame.orm.core.annotation.LoadJoinValueImpl;

/**
 * 商品属性管理<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@RestController
@RequestMapping("/goods/goodsproperty")
public class GoodsPropertyController {
    private final static  Logger log =LoggerFactory.getLogger(GoodsPropertyController.class);
    @Resource
    private IBaseCategoryPropertyBiz propertyBiz;
    @Resource
    private IGoodsService goodsService;
    @Resource
    private IGoodsBiz goodsBiz;
    @Resource
    private IGoodsSkuDefBiz goodsSkuBiz;
    
    @Resource
    private IGoodsSpecdefService specService;
    
    @Resource
    private IBaseCategoryPropertyBiz baseCategoryPropertyBiz;
    
    protected IBaseCategoryPropertyBiz getBiz(){
        return propertyBiz;
    }
    /**按分类取得属性
     * @param categoryid 分类<br/>
     */
    @RequestMapping("/propertys")
    //public JsonResult<List<CategoryPropertyVo>> getPropertys(@RequestParam Long categoryid){
    public JsonResult<List<GoodsPropertyVo>> getPropertys(@RequestParam Long goodsid){
        //createMethodSinge创建方法
    	GoodsBo goodsBo =(goodsid == null ? null : goodsBiz.selectById(goodsid));
        if(goodsBo==null){
            return JsonResult.newFail("商品不能为空");
        }
        Long categoryId = goodsBo.getLeafCategoryId();
        List<BaseCategoryPropertyBo> rds = propertyBiz.getProListByCatId(categoryId,StatusEnum.ENABLE);
        List<GoodsPropertyVo> proValues = ControllerUtils.convertList(rds, GoodsPropertyVo.class);
        LoadJoinValueImpl.load(propertyBiz, proValues);
        setValueChecked(goodsBo,proValues);

        /** 已定义的SKU.PropertyValueIds */
        List<String> skuVs = new ArrayList<>();
        {//取已定义的SKU
	        Wrapper<GoodsSkuDefBo> querySku = new EntityWrapper<>();
	        querySku.eq(GoodsSkuDefBo.Key.goodsId.toString(), goodsid);
	        List<GoodsSkuDefBo> skus = goodsSkuBiz.selectList(querySku);
	        for(GoodsSkuDefBo bo :skus){
	        	skuVs.add(bo.getPropertyValueIds());
	        }
        }
        
        JsonResult<List<GoodsPropertyVo>> rs = JsonResult.newSuccess(proValues);
        rs.put("skus",skuVs);
        return rs;
    }
    /**
     * 设置属性值,及是否已引用(转换类型PropertyValueVo)<br/>
     * 并且设置图片的全路径
     * @param goodsBo
     * @param proValues 分类预定义的所有属性及值(包括sku属性、普通属性)
     */
    private void setValueChecked(GoodsBo goodsBo, List<GoodsPropertyVo> proValues){
    	if(Tools.collection.isEmpty(proValues)){
        	return ;
        }
    	Long goodsId = goodsBo.getId();
    	List<GoodsSpecificationDefBo> goodsValues = specService.getValues(goodsId);
		List<PropsVo> goodsProps = GoodsUtil.parsePropsVo(goodsBo.getProps());
		
    	//分类预定义的属性PropertyValueVo
    	for(GoodsPropertyVo pv : proValues){
        	List<GoodsSpecVo> valueDefs = pv.getValues();//分类预定义的属性值 
        	
        	if(pv.getType() == PropertyTypeEnum.STANDARD){//SKU属性
        		getSkuValue(goodsId, valueDefs, goodsValues);
        	}
        	else{//普通属性
        		List<GoodsSpecVo> defs = getPropsValue(goodsId,valueDefs,pv,goodsProps);
        		pv.setSpecValues(defs);
        	}
        }    	
    }
    /**
     * 创建商品的普通属性,并且设置图片的全路径
     * @param goodsId
     * @param valueDefs 商品定义的值对象
     * @param pv
     * @param goodsProps
     */
    private void getSkuValue(Long goodsId,List<GoodsSpecVo> valueDefs, List<GoodsSpecificationDefBo> goodsValues ){
    	int size = (valueDefs==null?0:valueDefs.size());
    	for(int i =0;i< size;i++){
    		GoodsSpecVo e = valueDefs.get(i) ;
			e.setGoodsId(goodsId);
    		GoodsSpecificationDefBo gv = findGoodsValue(goodsValues, e.getPropertyValueId());
    		if(gv!=null){
    			e.setChecked(Boolean.TRUE);
    			String url = QiniuUtil.getFullPath(gv.getImgUrl());
    			e.setImgUrl(url);
    		}
    		else{
    			e.setChecked(Boolean.FALSE);
    		}
    	}
    }
    
    /**
     * 创建商品的普通属性
     * @param goodsId
     * @param valueDefs
     * @param pv
     * @param goodsProps
     */
    private List<GoodsSpecVo> getPropsValue(Long goodsId,List<GoodsSpecVo> valueDefs,GoodsPropertyVo pv,List<PropsVo> goodsProps ){
    	int propsSize = (valueDefs==null?0:valueDefs.size());
    	if(propsSize ==0){ //没有属性值，就补一个
			GoodsSpecVo v = new GoodsSpecVo();
			v.setId(Long.valueOf(-1));
			v.setIsimg(pv.getIsimage());
			v.setGoodsId(goodsId);
			
			if(Tools.collection.isNotEmpty(goodsProps)){//取第一个
				PropsVo ps = goodsProps.get(0);
				v.setChecked(Boolean.TRUE);
				v.setCustomValueName(ps.getValue());
			}
			else{
    			v.setCustomValueName("");
			}
			valueDefs = Arrays.asList(v);//从新创建了
			//pv.setSpecValues(valueDefs);
		}
		else{
			for(int i =0;i< propsSize;i++){
        		GoodsSpecVo e = valueDefs.get(i) ;
        		e.setIsimg(pv.getIsimage());
    			e.setGoodsId(goodsId);
    			//e.setId(gv.getValueid());
        		PropsVo gv = findPropsValue(goodsProps, e.getPropertyValueId());
        		if(gv==null){
        			e.setChecked(Boolean.FALSE);
        		}
        		else{
	        		e.setChecked(Boolean.TRUE);
	        		e.setCustomValueName(gv.getValue());
        			e.setPropertyValueId(e.getPropertyValueId());
        		}
        	}
		}
		return valueDefs;
    }
    private GoodsSpecificationDefBo findGoodsValue(List<GoodsSpecificationDefBo> goodsValues,Long propertyValueId){
    	if(Tools.collection.isEmpty(goodsValues) || propertyValueId == null){
    		return null;
    	}
    	GoodsSpecificationDefBo rs = null;
    	for(GoodsSpecificationDefBo bo:goodsValues){
    		if(bo.getPropertyValueId().equals(propertyValueId)){
    			rs = bo;
    			break;
    		}
    	}
    	return rs;
    }
    private PropsVo findPropsValue(List<PropsVo> propsValues,Long propertyValueId){
    	if(Tools.collection.isEmpty(propsValues) || propertyValueId == null){
    		return null;
    	}
    	PropsVo rs = null;
    	for(PropsVo bo:propsValues){
    		if(bo.getValueid().equals(propertyValueId)){
    			rs = bo;
    			break;
    		}
    	}
    	return rs;
    }
 
    /**
     * 创建商品的属性值及SKU
     * @param propertys 普通属性值<br/>
     * @param goodsid 商品ID<br/>
     * @param values SKU属性值<br/>
     * @param skus sku组合,每组以分号分隔,组内用斜杠"/"分隔 )<br/>
     */
    @RequestMapping("/createsku")
    public JsonResult<Boolean> doCreateSku(Long goodsid,@RequestBody List<GoodsSpecVo> values, String skus){
        //createMethodSinge创建方法
        if(goodsid==null){
            return JsonResult.newFail("商品ID不能为空");
        }
        if(Tools.string.isEmpty(skus)){
            return JsonResult.newFail("sku定义,每组以分号分隔,组内用逗号分隔(12,34;12,45)不能为空");
        }
        String[] items = skus.split(";");
        List<String[]> sku = new ArrayList<>(items.length);
        for(String s : items){
        	String[] e = s.split("/");
        	sku.add(e);
        } 
        List<GoodsSpecificationDefBo> specVo = ControllerUtils.convertList(values, GoodsSpecificationDefBo.class);

        for(GoodsSpecificationDefBo vo :specVo){
        	String url =  QiniuUtil.getRelativePath(vo.getImgUrl());
        	vo.setImgUrl(url);
        }
        goodsService.createPropertySku(goodsid, specVo, sku);
        return JsonResult.newSuccess(Boolean.TRUE);
    }

}