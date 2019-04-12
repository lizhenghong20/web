package cn.farwalker.ravv.admin.goods.dto;
import java.util.ArrayList;
import java.util.List;

import cn.farwalker.ravv.service.category.property.model.BaseCategoryPropertyBo;
import cn.farwalker.ravv.service.category.value.model.BaseCategoryPropertyValueBo;
import cn.farwalker.waka.util.Tools;

import com.cangwu.frame.orm.core.annotation.LoadJoinValue;

/**
 * 商品分类属性及属性值
 * 
 * @author generateModel.java
 */
public class GoodsPropertyVo extends BaseCategoryPropertyBo{
    private static final long serialVersionUID = 68450092L;

    private List<GoodsSpecVo> values;
    /** 属性值*/
    public List<GoodsSpecVo> getValues(){
        return values;
    }
    /** 属性值*/
    public void setSpecValues(List<GoodsSpecVo> vos){
        values = vos;
    }
    
    /** 属性值*/
    @LoadJoinValue(by="id",table=BaseCategoryPropertyValueBo.TABLE_NAME,joinfield="propertyId")
    public void setValues(List<BaseCategoryPropertyValueBo> pvs){
    	if(Tools.collection.isEmpty(pvs)){
    		if(values!=null){
    			values.clear();
    		}
    		return ;
    	}
    	
    	///////////////////////////////
    	List<GoodsSpecVo> gvs = new ArrayList<>(pvs.size());
    	for(BaseCategoryPropertyValueBo pv : pvs){
    		GoodsSpecVo vo = new GoodsSpecVo();
    		vo.setCustomValueName(pv.getValueName());
    		vo.setGoodsId(null);
    		vo.setImgUrl(null);
    		vo.setIsimg(this.getIsimage());
    		vo.setPropertyValueId(pv.getId());
    		gvs.add(vo);
    	}
    	this.setSpecValues(gvs);
    }
}