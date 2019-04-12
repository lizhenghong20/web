package cn.farwalker.ravv.service.category.basecategory.biz.impl;

import java.util.List;
import javax.annotation.Resource;

import cn.farwalker.waka.core.ZTreeNode;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.farwalker.ravv.service.category.basecategory.model.BaseCategoryBo;
import cn.farwalker.ravv.service.category.basecategory.dao.IBaseCategoryDao;
import cn.farwalker.ravv.service.category.basecategory.biz.IBaseCategoryBiz;

/**
 * 类目基础信息<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class BaseCategoryBizImpl extends ServiceImpl<IBaseCategoryDao,BaseCategoryBo> implements IBaseCategoryBiz{

	@Resource
	private IBaseCategoryBiz baseCategoryBiz;
	
	@Resource
	private IBaseCategoryDao baseCategoryDao;
	
	@Override
	public BaseCategoryBo getSameCategoryByPid(Long pid, String categoryName) {

		EntityWrapper<BaseCategoryBo> wrapper = new EntityWrapper<>();
		wrapper.eq(BaseCategoryBo.Key.pid.toString(), pid);
		wrapper.eq(BaseCategoryBo.Key.catName.toString(), categoryName);
		
		return baseCategoryBiz.selectOne(wrapper);
	}

	@Override
	public List<Long> getCategoryIds() {
		return baseCategoryDao.getCategoryIds();
	}

	@Override
	public List<ZTreeNode> categoryListTree(List<Long> categoryIds) {
		return baseCategoryDao.categoryListTree(categoryIds);
	}
	
}