package cn.farwalker.ravv.category;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.core.ZTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;

import cn.farwalker.ravv.category.dto.PropertySettingVo;
import cn.farwalker.ravv.service.category.basecategory.biz.IBaseCategoryBiz;
import cn.farwalker.ravv.service.category.basecategory.model.BaseCategoryBo;
import cn.farwalker.ravv.service.category.property.biz.IBaseCategoryPropertyBiz;
import cn.farwalker.ravv.service.category.property.constants.PropertyTypeEnum;
import cn.farwalker.ravv.service.category.property.model.BaseCategoryPropertyBo;
import cn.farwalker.ravv.service.category.value.biz.IBaseCategoryPropertyValueBiz;
import cn.farwalker.ravv.service.category.value.model.BaseCategoryPropertyValueBo;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsBiz;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.goodssku.specification.biz.IGoodsSpecificationDefBiz;
import cn.farwalker.ravv.service.goodssku.specification.model.GoodsSpecificationDefBo;
import cn.farwalker.waka.constants.StatusEnum;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.util.Tools;

/**
 * 商品分类<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
@RestController
@RequestMapping("/category")
public class CategoryController{
	private final static Logger log = LoggerFactory.getLogger(CategoryController.class);
	@Resource
	private IBaseCategoryBiz baseCategoryBiz;

	@Resource
	private IBaseCategoryPropertyBiz baseCategoryPropertyBiz;

	@Resource
	private IBaseCategoryPropertyValueBiz baseCategoryPropertyValueBiz;
	
	@Resource
	private IGoodsSpecificationDefBiz goodsSpecificationDefBiz;
	
	@Resource
	private IGoodsBiz goodsBiz;

	protected IBaseCategoryBiz getBiz(){
		return baseCategoryBiz;
	}

	/**
	 * 删除分类
	 * 
     * @param catId 分类id<br/>
    */
	@RequestMapping("/delete_category")
	@Transactional
	public JsonResult<Boolean> deleteCategory(@RequestParam Long catId){
		// createMethodSinge创建方法
		if(catId == null){
			return JsonResult.newFail("分类id不能为空");
		}
		
		//判断该分类是否为叶节点(有下级分类)，有则不给删除
		BaseCategoryBo category = baseCategoryBiz.selectById(catId);
		if(null != category) {
			if(!category.getLeaf()) {
				return JsonResult.newFail("该分类有下级分类，不能删除");
			}
		}else {
			return JsonResult.newFail("没有对应分类");
		}
		
		//获取指定分类下的所有属性
		List<BaseCategoryPropertyBo> proListByCatId = baseCategoryPropertyBiz.getProListByCatId(catId,null);
		
		//删除指定分类下的所有属性
		if(CollectionUtils.isNotEmpty(proListByCatId)){
			return JsonResult.newFail("该分类有下级属性，不能删除");
//			for(BaseCategoryPropertyBo property : proListByCatId) {
//				//获取指定属性的所有属性值
//				List<BaseCategoryPropertyValueBo> proValListByProId = baseCategoryPropertyValueBiz.getProValListByProId(property.getId());
//				
//				//删除指定属性下的所有属性值
//				if(CollectionUtils.isNotEmpty(proValListByProId)) {
//					for(BaseCategoryPropertyValueBo propertyValue : proValListByProId) {
//						baseCategoryPropertyValueBiz.deleteById(propertyValue.getId());
//					}
//				}
//			}
		}
		
		//删除指定分类
		Boolean rs = baseCategoryBiz.deleteById(catId);
		
		if(rs){
			if(null != category.getPid() && !category.getPid().equals(0L)){
				Wrapper<BaseCategoryBo> wrapper = new EntityWrapper<>();
				wrapper.eq(BaseCategoryBo.Key.pid.toString(), category.getPid());
				List<BaseCategoryBo> catList = baseCategoryBiz.selectList(wrapper);
				if(catList.size() == 0) {
					BaseCategoryBo baseCategoryBo = baseCategoryBiz.selectById(category.getPid());
					if(null != baseCategoryBo) {
						baseCategoryBo.setLeaf(true);
						baseCategoryBiz.updateById(baseCategoryBo);
					}
				}
			}
			
			return JsonResult.newSuccess(rs);
		} else{
			throw 	new WakaException("删除分类失败");
		}
	}

	/**
	 * 创建分类记录
	 * 
     * @param pid 父级id<br/>
     * @param categoryName 分类名称<br/>
     * @param sequence 排序<br/>
      */
	@RequestMapping("/create_category")
	public JsonResult<Boolean> createCategory(Long pid,String categoryName,Integer sequence){
		// createMethodSinge创建方法
		if(pid == null){
			return JsonResult.newFail("父级id不能为空");
		}
		if(Tools.string.isEmpty(categoryName)){
			return JsonResult.newFail("分类名称不能为空");
		}
		if(sequence == null){
			return JsonResult.newFail("排序不能为空");
		}
		Boolean isAtRoot = false;
		if(pid == -1L){
			pid = 0L;
			isAtRoot = true;
		}

		// 检查同级下是否有相同名称的类目
		BaseCategoryBo sameCategoryByPid = baseCategoryBiz.getSameCategoryByPid(pid, categoryName);
		if(null != sameCategoryByPid){
			return JsonResult.newFail("添加类目失败，类目已经存在");
		}

		BaseCategoryBo baseCategory = new BaseCategoryBo();
		baseCategory.setCatName(categoryName);
		baseCategory.setPid(pid);
		baseCategory.setLeaf(true);
		baseCategory.setStatus(StatusEnum.ENABLE);
		baseCategory.setSequence(sequence);

		Boolean rs = baseCategoryBiz.insert(baseCategory);

		if(rs){
			//类目路径
			String paths = baseCategory.getId().toString();
			
			if(!isAtRoot){
				BaseCategoryBo baseCategoryBo = baseCategoryBiz.selectById(pid);
				baseCategoryBo.setLeaf(false);
				baseCategoryBiz.updateById(baseCategoryBo);
				
				paths = baseCategoryBo.getPaths() + "/" + baseCategory.getId();
			}
			
			//存储区域全路径
			baseCategory.setPaths(paths);
			baseCategoryBiz.updateById(baseCategory);
			
			return JsonResult.newSuccess(rs);
		} else{
			throw new WakaException("添加类目失败");
		}
	}

	/**
	 * 创建分类属性记录
	 * 
	
     * @param catId 分类id<br/>
     * @param propertyName 属性名称<br/>
     * @param sequence 排序<br/>
      */
	@RequestMapping("/create_category_property")
	public JsonResult<Boolean> createCatProperty(Long catId,String propertyName,Integer sequence){
		// createMethodSinge创建方法
		if(catId == null){
			return JsonResult.newFail("分类id不能为空");
		}
		if(Tools.string.isEmpty(propertyName)){
			return JsonResult.newFail("属性名称不能为空");
		}
		if(sequence == null){
			return JsonResult.newFail("排序不能为空");
		}
		// 检查相同类目下有无相同属性
		BaseCategoryPropertyBo samePropertyByCatId = baseCategoryPropertyBiz.getSamePropertyByCatId(catId,propertyName);
		if(null != samePropertyByCatId){
			return JsonResult.newFail("添加属性失败，属性已经存在");
		}

		BaseCategoryPropertyBo baseCategoryProperty = new BaseCategoryPropertyBo();
		baseCategoryProperty.setCatId(catId);
		baseCategoryProperty.setPropertyName(propertyName);
		baseCategoryProperty.setSequence(sequence);
		baseCategoryProperty.setRequired(Boolean.FALSE);
		baseCategoryProperty.setManuallyInput(Boolean.TRUE);
		baseCategoryProperty.setIsimage(Boolean.FALSE);
		baseCategoryProperty.setMultiSelect(Boolean.FALSE);
		baseCategoryProperty.setType(PropertyTypeEnum.NORMAL);
		baseCategoryProperty.setStatus(StatusEnum.ENABLE);

		Boolean rs = baseCategoryPropertyBiz.insert(baseCategoryProperty);
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 创建分类属性值记录
	 * 
     * @param propertyId 属性id<br/>
     * @param valueName 属性值名称<br/>
     * @param sequence 排序<br/>
     */
	@RequestMapping("/create_property_value")
	public JsonResult<Boolean> createProValue(Long propertyId,String valueName,Integer sequence){
		// createMethodSinge创建方法
		if(propertyId == null){
			return JsonResult.newFail("属性id不能为空");
		}
		if(Tools.string.isEmpty(valueName)){
			return JsonResult.newFail("属性值名称不能为空");
		}
		// 获取对应属性信息
		BaseCategoryPropertyBo baseCategoryProperty = baseCategoryPropertyBiz.selectById(propertyId);
		if(null == baseCategoryProperty){
			throw new WakaException("没有对应属性");
		}
		
		//检查相同属性名下是否有相同的属性值名称
		BaseCategoryPropertyValueBo sameCatProValByProId = baseCategoryPropertyValueBiz.getSameCatProValByProId(propertyId, valueName);
		if(null != sameCatProValByProId){
			return JsonResult.newFail("添加属性值失败，属性值已经存在");
		}

		BaseCategoryPropertyValueBo baseCategoryPropertyValue = new BaseCategoryPropertyValueBo();
		baseCategoryPropertyValue.setValueName(valueName);
		baseCategoryPropertyValue.setPropertyId(propertyId);
		baseCategoryPropertyValue.setTmpPropertyName(baseCategoryProperty.getPropertyName());
		baseCategoryPropertyValue.setTmpCategoryId(baseCategoryProperty.getCatId());
		baseCategoryPropertyValue.setStatus(StatusEnum.ENABLE);

		Boolean rs = baseCategoryPropertyValueBiz.insert(baseCategoryPropertyValue);
		return JsonResult.newSuccess(rs);

	}

	/** 获取分类列表（按树结构） */
	@RequestMapping("/category_tree")
	public JsonResult<List<ZTreeNode>> categoryListTree(){
		// createMethodSinge创建方法
		List<Long> categoryIds = baseCategoryBiz.getCategoryIds();
		
		if(CollectionUtils.isEmpty(categoryIds)){
			return JsonResult.newSuccess(new ArrayList<ZTreeNode>());
		} else{
			List<ZTreeNode> categoryTree = baseCategoryBiz.categoryListTree(categoryIds);
//			// 展开树的第一级
//			if(CollectionUtils.isNotEmpty(categoryTree)){
//				for (ZTreeNode roleTree : categoryTree) {
//					if (roleTree.getpId() == 0) {
//						roleTree.setIsOpen(true);
//						break;
//					}
//				}
//			}
			return JsonResult.newSuccess(categoryTree);
		}
	}

	/**
	 * 修改分类
	 * 
     * @param catId 分类id<br/>
     * @param categoryName 分类名称<br/>
     * @param sequence 排序<br/>
    */
	@RequestMapping("/modify_category")
	public JsonResult<Boolean> modifyCategory(Long catId,String categoryName,Integer sequence){
		// createMethodSinge创建方法
		if(catId == null){
			return JsonResult.newFail("分类id不能为空");
		}
		if(Tools.string.isEmpty(categoryName)){
			return JsonResult.newFail("分类名称不能为空");
		}
		if(sequence == null){
			return JsonResult.newFail("排序不能为空");
		}
		
		BaseCategoryBo baseCategory = baseCategoryBiz.selectById(catId);
		if(null == baseCategory){
			return JsonResult.newFail("找不到对应属性");
		}
		
		// 检查同级下是否有相同名称的类目
		BaseCategoryBo sameCategoryByPid = baseCategoryBiz.getSameCategoryByPid(baseCategory.getPid(), categoryName);
		if(null != sameCategoryByPid){
			if(!sameCategoryByPid.getId().equals(catId)){
				return JsonResult.newFail("修改分类失败，分类名已经存在");
			}
		}
		
		baseCategory.setCatName(categoryName);
		baseCategory.setSequence(sequence);
		
		//下方方法为修改数据用，之后删除
		Wrapper<BaseCategoryBo> wrapper = new EntityWrapper<>();
		wrapper.eq(BaseCategoryBo.Key.pid.toString(), catId);
		List<BaseCategoryBo> catList = baseCategoryBiz.selectList(wrapper);
		if(catList.size() > 0) {
			baseCategory.setLeaf(false);
		}else {
			baseCategory.setLeaf(true);
		}

		Boolean rs = baseCategoryBiz.updateById(baseCategory);
		
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 删除分类属性值
	 * 
     * @param valueId 属性值id<br/>
    */
	@RequestMapping("/delete_property_value")
	public JsonResult<Boolean> deleteProValue(@RequestParam Long valueId){
		// createMethodSinge创建方法
		if(valueId == null){
			return JsonResult.newFail("属性值id不能为空");
		}
		
		//判断是否被商品引用
		EntityWrapper<GoodsSpecificationDefBo> wrapper = new EntityWrapper<>();
		wrapper.eq(GoodsSpecificationDefBo.Key.propertyValueId.toString(), valueId);
		GoodsSpecificationDefBo goodsSpecDef = goodsSpecificationDefBiz.selectOne(wrapper);
		if(null != goodsSpecDef) {
			GoodsBo good = goodsBiz.selectById(goodsSpecDef.getGoodsId());
			if(null != good) {
				return JsonResult.newFail("该属性值已被商品：" + good.getGoodsName() + "引用，不能删除");
			}else {
				return JsonResult.newFail("该属性值已被商品引用，不能删除");
			}
		}
		
		//删除指定属性值
		Boolean rs = baseCategoryPropertyValueBiz.deleteById(valueId);
		
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 修改分类属性值
	 * 
     * @param valueId 属性值id<br/>
     * @param valueName 属性值名称<br/>
     * @param sequence 排序<br/>
    */
	@RequestMapping("/modify_property_value")
	public JsonResult<Boolean> modifyProValue(Long valueId,String valueName,Integer sequence){
		// createMethodSinge创建方法
		if(valueId == null){
			return JsonResult.newFail("属性值id不能为空");
		}
		if(Tools.string.isEmpty(valueName)){
			return JsonResult.newFail("属性值名称不能为空");
		}
		
		BaseCategoryPropertyValueBo baseCatProValue = baseCategoryPropertyValueBiz.selectById(valueId);
		if(null == baseCatProValue){
			return JsonResult.newFail("找不到对应属性值");
		}
		
		//检查相同属性名下是否有相同的属性值名称
		BaseCategoryPropertyValueBo sameCatProValByProId = baseCategoryPropertyValueBiz.getSameCatProValByProId(baseCatProValue.getPropertyId(), valueName);
		if(null != sameCatProValByProId){
			if(!sameCatProValByProId.getId().equals(valueId)){
				return JsonResult.newFail("修改属性值失败，属性值已经存在");
			}
		}
		
		baseCatProValue.setValueName(valueName);
		
		Boolean rs = baseCategoryPropertyValueBiz.updateById(baseCatProValue);

		return JsonResult.newSuccess(rs);
	}

	/**
	 * 删除分类属性
	 * 
     * @param propertyId 属性id<br/>
     */
	@RequestMapping("/delete_property")
	@Transactional
	public JsonResult<Boolean> deleteProperty(@RequestParam Long propertyId){
		// createMethodSinge创建方法
		if(propertyId == null){
			return JsonResult.newFail("属性id不能为空");
		}
		
		//获取指定属性的所有属性值
		List<BaseCategoryPropertyValueBo> proValListByProId = baseCategoryPropertyValueBiz.getProValListByProId(propertyId);
		
		//删除指定属性下的所有属性值
		if(CollectionUtils.isNotEmpty(proValListByProId)){
			return JsonResult.newFail("该属性有下级属性值，不能删除");
//			for(BaseCategoryPropertyValueBo propertyValue : proValListByProId) {
//				baseCategoryPropertyValueBiz.deleteById(propertyValue.getId());
//			}
		}
		
		//删除指定分类属性
		Boolean rs = baseCategoryPropertyBiz.deleteById(propertyId);
		
		return JsonResult.newSuccess(rs);
	}

	/** 获取分类属性列表（按树结构）
     * @param catId 分类id<br/>
      */
	@RequestMapping("/property_tree")
	public JsonResult<List<ZTreeNode>> propertyListTree(Long catId){
		// createMethodSinge创建方法
		List<ZTreeNode> proListTree = baseCategoryPropertyBiz.getProListTreeByCatId(catId);
		List<ZTreeNode> proValListTree = baseCategoryPropertyValueBiz.getProValListTreeByCatId(catId);
		proListTree.addAll(proValListTree);
		
		return JsonResult.newSuccess(proListTree);
	}

	/**
	 * 修改分类属性
	 * 
     * @param propertyId 属性id<br/>
     * @param propertyName 属性名称<br/>
     * @param sequence 排序<br/>
      */
	@RequestMapping("/modify_property")
	public JsonResult<Boolean> modifyProperty(Long propertyId,String propertyName,Integer sequence){
		// createMethodSinge创建方法
		if(propertyId == null){
			return JsonResult.newFail("属性id不能为空");
		}
		if(Tools.string.isEmpty(propertyName)){
			return JsonResult.newFail("属性名称不能为空");
		}
		if(sequence == null){
			return JsonResult.newFail("排序不能为空");
		}
		
		BaseCategoryPropertyBo baseCatProperty = baseCategoryPropertyBiz.selectById(propertyId);
		if(null == baseCatProperty){
			return JsonResult.newFail("没找到对应属性");
		}
		
		//检查相同分类下是否有相同的属性名称
		BaseCategoryPropertyBo samePropertyByCatId = baseCategoryPropertyBiz.getSamePropertyByCatId(baseCatProperty.getCatId(), propertyName);
		if(null != samePropertyByCatId){
			if(!samePropertyByCatId.getId().equals(propertyId)){
				return JsonResult.newFail("修改属性名失败，属性名已经存在");
			}
		}
		
		baseCatProperty.setPropertyName(propertyName);
		baseCatProperty.setSequence(sequence);
		
		Boolean rs = baseCategoryPropertyBiz.updateById(baseCatProperty);

		return JsonResult.newSuccess(rs);
	}
	
	/**
	 * 获取分类属性设置
	 * 
     * @param propertyId 属性id<br/>
     */
	@RequestMapping("/get_propertyset")
	public JsonResult<PropertySettingVo> getProSetting(Long propertyId){
		if(propertyId == null){
			return JsonResult.newFail("属性id不能为空");
		}
		BaseCategoryPropertyBo property = baseCategoryPropertyBiz.selectById(propertyId);
		if(null == property) {
			return JsonResult.newFail("没有对应属性");
		}
		
		PropertySettingVo propertySetting = new PropertySettingVo();
		propertySetting.setPropertyId(property.getId());
		propertySetting.setPropertyType(property.getType().getKey());
		propertySetting.setRequired(property.getRequired());
		propertySetting.setManuallyInput(property.getManuallyInput());
		propertySetting.setMultiSelect(property.getMultiSelect());
		propertySetting.setIsimage(property.getIsimage());
		
		return JsonResult.newSuccess(propertySetting);
	}
	
	/**
	 * 修改分类属性设置
	 * 
     * @param vo 分类设置
     */
	@RequestMapping("/modify_propertyset")
	public JsonResult<Boolean> modifyProSetting(@RequestBody PropertySettingVo vo){
		if(vo.getPropertyId() == null){
			return JsonResult.newFail("属性id不能为空");
		}
		BaseCategoryPropertyBo property = baseCategoryPropertyBiz.selectById(vo.getPropertyId());
		if(null == property) {
			return JsonResult.newFail("没有对应属性");
		}
		
		property.setType(PropertyTypeEnum.getEnumByKey(vo.getPropertyType()));
		property.setRequired(vo.getRequired());
		property.setManuallyInput(vo.getManuallyInput());
		property.setMultiSelect(vo.getMultiSelect());
		property.setIsimage(vo.getIsimage());
		
		Boolean rs = baseCategoryPropertyBiz.updateById(property);
		
		return JsonResult.newSuccess(rs);
	}

}