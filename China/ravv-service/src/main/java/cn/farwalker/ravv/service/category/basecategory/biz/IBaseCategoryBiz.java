package cn.farwalker.ravv.service.category.basecategory.biz;
import java.util.List;

import cn.farwalker.waka.core.ZTreeNode;
import com.baomidou.mybatisplus.service.IService;
import cn.farwalker.ravv.service.category.basecategory.model.BaseCategoryBo;

/**
 * 类目基础信息<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IBaseCategoryBiz extends IService<BaseCategoryBo>{
	
	/**
	 * 获取同一级相同名称的类目
	 * @param pid 父级ID
	 * @param categoryName 类目名称
	 * @return
	 */
	BaseCategoryBo getSameCategoryByPid(Long pid, String categoryName);
	
	/**
	 * 获取所有分类id
	 */
	List<Long> getCategoryIds();

	/**
	 * 获取分类列表树
	 */
	List<ZTreeNode> categoryListTree(List<Long> categoryIds);
	
}