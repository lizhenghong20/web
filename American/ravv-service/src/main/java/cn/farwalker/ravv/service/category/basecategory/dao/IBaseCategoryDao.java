package cn.farwalker.ravv.service.category.basecategory.dao;
import java.util.List;

import cn.farwalker.waka.core.ZTreeNode;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.farwalker.ravv.service.category.basecategory.model.BaseCategoryBo;

/**
 * 类目基础信息<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IBaseCategoryDao extends BaseMapper<BaseCategoryBo>{
	
	/**
	 * 获取所有分类id
	 */
	List<Long> getCategoryIds();

	/**
	 * 获取分类列表树
	 */
	List<ZTreeNode> categoryListTree(List<Long> categoryIds);
}