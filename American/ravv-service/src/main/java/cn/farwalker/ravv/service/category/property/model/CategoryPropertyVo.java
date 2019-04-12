package cn.farwalker.ravv.service.category.property.model;
import java.util.List;

import com.cangwu.frame.orm.core.annotation.LoadJoinValue;

import cn.farwalker.ravv.service.category.value.model.BaseCategoryPropertyValueBo;

/**
 * 商品分类属性及属性值
 * 
 * @author generateModel.java
 */
public class CategoryPropertyVo extends BaseCategoryPropertyBo{
    private static final long serialVersionUID = 68450092L;

    private List<BaseCategoryPropertyValueBo> values;
    /** 属性值*/
    public List<BaseCategoryPropertyValueBo> getValues(){
        return values;
    }
    /** 属性值*/
    @LoadJoinValue(by="id",table=BaseCategoryPropertyValueBo.TABLE_NAME,joinfield="propertyId")
    public void setValues(List<BaseCategoryPropertyValueBo> values){
        this.values =values;
    }
}