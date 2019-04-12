package cn.farwalker.ravv.service.category.value.dao;
import java.util.List;

import cn.farwalker.waka.core.ZTreeNode;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.farwalker.ravv.service.category.value.model.BaseCategoryPropertyValueBo;

/**
 * 叶子类目的属性与属性值的关联表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IBaseCategoryPropertyValueDao extends BaseMapper<BaseCategoryPropertyValueBo>{
	
	/**
	 * 获取指定分类下的属性值树
	 * @param catId
	 * @return
	 */
	List<ZTreeNode> getProValListTreeByCatId(@Param(value="catId") Long catId);

}