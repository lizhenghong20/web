package cn.farwalker.ravv.service.category.property.dao;
import java.util.List;

import cn.farwalker.waka.core.ZTreeNode;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.farwalker.ravv.service.category.property.model.BaseCategoryPropertyBo;


/**
 * 类目与属性的关联<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IBaseCategoryPropertyDao extends BaseMapper<BaseCategoryPropertyBo>{
	
	/**
	 * 获取指定分类的属性树
	 * @param catId
	 * @return
	 */
	List<ZTreeNode> getProListTreeByCatId(@Param(value="catId") Long catId);
}