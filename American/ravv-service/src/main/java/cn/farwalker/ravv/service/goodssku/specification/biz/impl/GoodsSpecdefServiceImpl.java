package cn.farwalker.ravv.service.goodssku.specification.biz.impl;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

import cn.farwalker.ravv.service.goodssku.specification.biz.IGoodsSpecdefService;
import cn.farwalker.ravv.service.goodssku.specification.biz.IGoodsSpecificationDefBiz;
import cn.farwalker.ravv.service.goodssku.specification.model.GoodsSpecificationDefBo;

@Service
public class GoodsSpecdefServiceImpl implements IGoodsSpecdefService{
	
	@Resource
	private IGoodsSpecificationDefBiz specBiz;
	
	@Override
	public List<GoodsSpecificationDefBo> getValues(Long goodsId) {
		if(goodsId == null || goodsId.longValue()==0){
			return Collections.emptyList();
		}
	
		Wrapper<GoodsSpecificationDefBo> query = new EntityWrapper<>();
		query.eq(GoodsSpecificationDefBo.Key.goodsId.toString(), goodsId);
		List<GoodsSpecificationDefBo> rs = specBiz.selectList(query);
		return rs;
	}

	@Override
	public GoodsSpecificationDefBo getValues(Long goodsId, Long categoryValueId) {
		if(goodsId == null || goodsId.longValue()==0 || categoryValueId == null || categoryValueId.longValue()==0){
			return null;
		}
		Wrapper<GoodsSpecificationDefBo> query = new EntityWrapper<>();
		query.eq(GoodsSpecificationDefBo.Key.goodsId.toString(), goodsId);
		query.eq(GoodsSpecificationDefBo.Key.propertyValueId.toString(),categoryValueId);
		GoodsSpecificationDefBo rs = specBiz.selectOne(query);
		return rs;
	}

}
