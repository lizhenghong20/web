package cn.farwalker.ravv.service.category.value.biz;
import java.util.List;

import cn.farwalker.waka.core.ZTreeNode;
import com.baomidou.mybatisplus.service.IService;

import cn.farwalker.ravv.service.category.property.model.BaseCategoryPropertyBo;
import cn.farwalker.ravv.service.category.value.model.BaseCategoryPropertyValueBo;


/**
 * 叶子类目的属性与属性值的关联表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IBaseCategoryPropertyValueBiz extends IService<BaseCategoryPropertyValueBo>{
	
	/**
	 * 检查同一属性下是否有相同属性值
	 * @param  属性id
	 * @param valueName 属性值名称
	 * @return
	 */
	BaseCategoryPropertyValueBo getSameCatProValByProId(Long propertyId, String valueName);
	
	/**
	 * 获取指定属性的所有属性值
	 * @param propertyId
	 * @return
	 */
	List<BaseCategoryPropertyValueBo> getProValListByProId(Long propertyId);
	
	/**
	 * 获取指定属性的属性值树
	 * @param propertyId
	 * @return
	 */
	List<ZTreeNode> getProValListTreeByCatId(Long catId);
}