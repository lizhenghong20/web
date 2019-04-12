package cn.farwalker.ravv.category;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;

import cn.farwalker.ravv.category.dto.PropertyTreeVo;
import cn.farwalker.ravv.service.category.basecategory.biz.IBaseCategoryBiz;
import cn.farwalker.ravv.service.category.basecategory.model.BaseCategoryBo;
import cn.farwalker.ravv.service.category.property.biz.IBaseCategoryPropertyBiz;
import cn.farwalker.ravv.service.category.property.model.BaseCategoryPropertyBo;
import cn.farwalker.ravv.service.category.value.biz.IBaseCategoryPropertyValueBiz;
import cn.farwalker.ravv.service.category.value.model.BaseCategoryPropertyValueBo;
import cn.farwalker.waka.core.JsonResult;

/** 分类树*/
@RestController
@RequestMapping("/category/tree")
public class CategoryTreeController{

	@Resource
	private IBaseCategoryBiz categoryBiz;
	
	@Resource
	private IBaseCategoryPropertyBiz categoryPropertyBiz;
	
	@Resource
	private IBaseCategoryPropertyValueBiz baseCategoryPropertyValueBiz;
	
	/**
	 * 取分类树
	 * @param parentid
	 * @return
	 */
	@RequestMapping("/children")
	public JsonResult<List<BaseCategoryBo>> getChildren(Long parentid){
		Wrapper<BaseCategoryBo> query = new EntityWrapper<>();
		final String PID = BaseCategoryBo.Key.pid.toString();
		if(parentid == null || parentid.longValue()==0){
			query.isNull(PID);
			query.or().eq(PID,Integer.valueOf(0));
		}
		else{
			query.eq(PID, parentid);
		}
		List<BaseCategoryBo> rds = categoryBiz.selectList(query);
		return JsonResult.newSuccess(rds);
	}
	
	/**
	 * 取属性树
	 * @param
	 * @return
	 */
	@RequestMapping("/property_tree")
	public JsonResult<List<PropertyTreeVo>> getPropertyChild(Long catid){
		Wrapper<BaseCategoryPropertyBo> query = new EntityWrapper<>();
		final String CATID = BaseCategoryPropertyBo.Key.catId.toString();
		if(catid == null){
			return JsonResult.newSuccess(null);
		}
		else{
			query.eq(CATID, catid);
		}
		List<BaseCategoryPropertyBo> catProperty = categoryPropertyBiz.selectList(query);
		List<PropertyTreeVo> propertyTreeVo = new ArrayList<>(catProperty.size());
		if(CollectionUtils.isNotEmpty(catProperty)) {
			for(BaseCategoryPropertyBo bo : catProperty){
				PropertyTreeVo vo = new PropertyTreeVo();
				vo.setId(bo.getId());
				vo.setLabel(bo.getPropertyName());
				vo.setType("property");
				vo.setSequence(bo.getSequence());
				vo.setAddAble(true);
				vo.setDelAble(true);
				//获取属性的所有属性值
				List<BaseCategoryPropertyValueBo> proValue = baseCategoryPropertyValueBiz.getProValListByProId(vo.getId());
				if(CollectionUtils.isNotEmpty(proValue)) {
					List<PropertyTreeVo> proValueTreeVo = new ArrayList<>();
					for(BaseCategoryPropertyValueBo value : proValue) {
						PropertyTreeVo valueVo = new PropertyTreeVo();
						valueVo.setId(value.getId());
						valueVo.setLabel(value.getValueName());
						valueVo.setType("proValue");
						valueVo.setAddAble(false);
						valueVo.setDelAble(true);
						proValueTreeVo.add(valueVo);
					}
					vo.setChildren(proValueTreeVo);
				}
				propertyTreeVo.add(vo);
			}
		}
		return JsonResult.newSuccess(propertyTreeVo);
	}
}
