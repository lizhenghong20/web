package cn.farwalker.ravv.service.category.property.biz.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.farwalker.waka.constants.StatusEnum;
import cn.farwalker.waka.core.ZTreeNode;
import org.springframework.stereotype.Service;

import cn.farwalker.ravv.service.category.property.biz.IBaseCategoryPropertyBiz;
import cn.farwalker.ravv.service.category.property.dao.IBaseCategoryPropertyDao;
import cn.farwalker.ravv.service.category.property.model.BaseCategoryPropertyBo;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

/**
 * 类目与属性的关联<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class BaseCategoryPropertyBizImpl extends ServiceImpl<IBaseCategoryPropertyDao,BaseCategoryPropertyBo> implements IBaseCategoryPropertyBiz{

	@Resource
	private IBaseCategoryPropertyBiz baseCategoryPropertyBiz;
	
	@Resource
	private IBaseCategoryPropertyDao baseCategoryPropertyDao;
	
	@Override
	public BaseCategoryPropertyBo getSamePropertyByCatId(Long catId, String propertyName) {
		
		EntityWrapper<BaseCategoryPropertyBo> wrapper = new EntityWrapper<>();
		wrapper.eq(BaseCategoryPropertyBo.Key.catId.toString(), catId);
		wrapper.eq(BaseCategoryPropertyBo.Key.propertyName.toString(), propertyName);
		
		return baseCategoryPropertyBiz.selectOne(wrapper);
	}

	@Override
	public List<BaseCategoryPropertyBo> getProListByCatId(Long catId,StatusEnum status) {
		Wrapper<BaseCategoryPropertyBo> wrapper = new EntityWrapper<>();
		wrapper.eq(BaseCategoryPropertyBo.Key.catId.toString(), catId);
		if(status!=null){
			wrapper.eq(BaseCategoryPropertyBo.Key.status.toString(), status);
		}
		return baseCategoryPropertyBiz.selectList(wrapper);
	}

	@Override
	public List<ZTreeNode> getProListTreeByCatId(Long catId) {
		return baseCategoryPropertyDao.getProListTreeByCatId(catId);
	}
}