package cn.farwalker.ravv.service.category.basecategory.biz;

import cn.farwalker.ravv.service.category.basecategory.model.BaseCategoryBo;

/**
 * 
 * @author Administrator
 *
 */
public interface ICategoryService {
	/**与Biz的区别是，service的方法有逻辑处理*/
	public Boolean insert(BaseCategoryBo entity) ;

	/**与Biz的区别是，service的方法有逻辑处理*/
	public Boolean updateById(BaseCategoryBo entity) ;
	
	/**
	 * 递归查找分类路径,"/"分隔,包含末级(就是参数的值)
	 * @param lastCategoryId
	 * @return
	 */
	public StringBuilder getCategoryPaths(Long lastCategoryId);
}
