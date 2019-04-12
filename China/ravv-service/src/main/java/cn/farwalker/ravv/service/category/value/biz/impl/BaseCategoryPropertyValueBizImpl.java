package cn.farwalker.ravv.service.category.value.biz.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.farwalker.waka.core.ZTreeNode;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.farwalker.ravv.service.category.value.biz.IBaseCategoryPropertyValueBiz;
import cn.farwalker.ravv.service.category.value.dao.IBaseCategoryPropertyValueDao;
import cn.farwalker.ravv.service.category.value.model.BaseCategoryPropertyValueBo;

/**
 * 叶子类目的属性与属性值的关联表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class BaseCategoryPropertyValueBizImpl extends ServiceImpl<IBaseCategoryPropertyValueDao,BaseCategoryPropertyValueBo> implements IBaseCategoryPropertyValueBiz{

	@Resource
	private IBaseCategoryPropertyValueBiz baseCategoryPropertyValueBiz;
	
	@Resource
	private IBaseCategoryPropertyValueDao baseCategoryPropertyValueDao;
	
	@Override
	public BaseCategoryPropertyValueBo getSameCatProValByProId(Long propertyId, String valueName) {
		EntityWrapper<BaseCategoryPropertyValueBo> wrapper = new EntityWrapper<>();
		wrapper.eq(BaseCategoryPropertyValueBo.Key.propertyId.toString(), propertyId);
		wrapper.eq(BaseCategoryPropertyValueBo.Key.valueName.toString(), valueName);
		
		return baseCategoryPropertyValueBiz.selectOne(wrapper);
	}

	@Override
	public List<BaseCategoryPropertyValueBo> getProValListByProId(Long propertyId) {
		Map<String, Object> map = new HashMap<>();
		map.put(BaseCategoryPropertyValueBo.Key.propertyId.toString(), propertyId);
		
		return baseCategoryPropertyValueBiz.selectByMap(map);
	}

	@Override
	public List<ZTreeNode> getProValListTreeByCatId(Long catId) {
		return baseCategoryPropertyValueDao.getProValListTreeByCatId(catId);
	}

}