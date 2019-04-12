package cn.farwalker.ravv.service.category.basecategory.biz.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.farwalker.ravv.service.category.basecategory.biz.IBaseCategoryBiz;
import cn.farwalker.ravv.service.category.basecategory.biz.ICategoryService;
import cn.farwalker.ravv.service.category.basecategory.model.BaseCategoryBo;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryServiceImpl implements ICategoryService{
	@Resource
	private IBaseCategoryBiz categoryBiz;

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Boolean insert(BaseCategoryBo bo) {
		setCategoryPaths(bo);
		boolean leaf = isLeaf(bo.getId());
		bo.setLeaf(Boolean.valueOf(leaf));
		return categoryBiz.insert(bo);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Boolean updateById(BaseCategoryBo bo) {
		setCategoryPaths(bo);
		boolean leaf = isLeaf(bo.getId());
		bo.setLeaf(Boolean.valueOf(leaf));
		return categoryBiz.updateById(bo);
	}

    private boolean isLeaf(Long menuId){
    	return false;//TODO 没实现
    	/*Wrapper<T>
    	webMenuBiz.selectOne();*/
    }
    /**设置等级路径*/
	private void setCategoryPaths(BaseCategoryBo bo){
		Long parentId = bo.getPid();
		String ids =String.valueOf(bo.getId());
		if(parentId == null || parentId.longValue()==0){
			bo.setPaths(ids);
			return ;
		}
		
		String paths = bo.getPaths();
		boolean macth = false;
		if(paths!=null){
			if(paths.indexOf('/')==0){
				 macth = paths.equals(ids);
			}
			else {
				macth = paths.endsWith(ids);
			}
		}
		if(!macth){
			StringBuilder ps = getCategoryPaths(bo.getId());
			bo.setPaths(ps.toString());
		}
	}
	/**
	 * 递归查找分类路径
	 * @param lastCategoryId
	 * @return
	 */
	@Override
	public StringBuilder getCategoryPaths(Long lastCategoryId){
		if(lastCategoryId == null || lastCategoryId.longValue()==0){
			return null;
		}
		
		BaseCategoryBo bo = categoryBiz.selectById(lastCategoryId);
		if(bo==null){
			return null;
		}
		
		StringBuilder paths = getCategoryPaths(bo.getPid());
		if(paths == null){ //root
			paths = new StringBuilder();
			paths.append(lastCategoryId);
		}
		else{
			paths.append('/').append(lastCategoryId);
		}
		return paths;
	}
}
