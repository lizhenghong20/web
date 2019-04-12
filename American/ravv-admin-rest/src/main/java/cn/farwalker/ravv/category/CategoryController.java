package cn.farwalker.ravv.category;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import cn.farwalker.ravv.admin.category.AdminCategoryService;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.core.ZTreeNode;
import org.beetl.ext.fn.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cn.farwalker.ravv.admin.category.dto.PropertySettingVo;

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
	@Autowired
	private AdminCategoryService adminCategoryService;

	/**
	 * 删除分类
	 * 
     * @param catId 分类id<br/>
    */
	@RequestMapping("/delete_category")
	@Transactional
	public JsonResult<Boolean> deleteCategory(@RequestParam Long catId){
		try{
			// createMethodSinge创建方法
			if(catId == null){
				throw new WakaException("分类id不能为空");
			}
			return JsonResult.newSuccess(adminCategoryService.deleteCategory(catId));

		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());

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

		try{
			// createMethodSinge创建方法
			if(pid == null){
				throw new WakaException("父级id不能为空");
			}
			if(Tools.string.isEmpty(categoryName)){
				throw new WakaException("分类名称不能为空");
			}
			if(sequence == null){
				throw new WakaException("排序不能为空");
			}
			return JsonResult.newSuccess(adminCategoryService.createCategory(pid, categoryName, sequence));

		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());

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
		try{
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
			return JsonResult.newSuccess(adminCategoryService.createCatProperty(catId, propertyName, sequence));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());

		}

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
		try{
			// createMethodSinge创建方法
			if(propertyId == null){
				return JsonResult.newFail("属性id不能为空");
			}
			if(Tools.string.isEmpty(valueName)){
				return JsonResult.newFail("属性值名称不能为空");
			}
			return JsonResult.newSuccess(adminCategoryService.createProValue(propertyId, valueName, sequence));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());

		}


	}

	/** 获取分类列表（按树结构） */
	@RequestMapping("/category_tree")
	public JsonResult<List<ZTreeNode>> categoryListTree(){
		try{
			return JsonResult.newSuccess(adminCategoryService.categoryListTree());
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());

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
		try{
			// createMethodSinge创建方法
			if(catId == null){
				throw new WakaException("分类id不能为空");
			}
			if(Tools.string.isEmpty(categoryName)){
				throw new WakaException("分类名称不能为空");
			}
			if(sequence == null){
				throw new WakaException("排序不能为空");
			}
			return JsonResult.newSuccess(adminCategoryService.modifyCategory(catId, categoryName, sequence));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());

		}

	}

	/**
	 * 删除分类属性值
	 * 
     * @param valueId 属性值id<br/>
    */
	@RequestMapping("/delete_property_value")
	public JsonResult<Boolean> deleteProValue(@RequestParam Long valueId){
		try{
			// createMethodSinge创建方法
			if(valueId == null){
				return JsonResult.newFail("属性值id不能为空");
			}
			return JsonResult.newSuccess(adminCategoryService.deleteProValue(valueId));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());

		}

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
		try{
			// createMethodSinge创建方法
			if(valueId == null){
				throw new WakaException("属性值id不能为空");
			}
			if(Tools.string.isEmpty(valueName)){
				throw new WakaException("属性值名称不能为空");
			}
			return JsonResult.newSuccess(adminCategoryService.modifyProValue(valueId, valueName, sequence));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());

		}

	}

	/**
	 * 删除分类属性
	 * 
     * @param propertyId 属性id<br/>
     */
	@RequestMapping("/delete_property")
	@Transactional
	public JsonResult<Boolean> deleteProperty(@RequestParam Long propertyId){
		try{
			// createMethodSinge创建方法
			if(propertyId == null){
				throw new WakaException("属性id不能为空");
			}
			return JsonResult.newSuccess(adminCategoryService.deleteProperty(propertyId));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());

		}

	}

	/** 获取分类属性列表（按树结构）
     * @param catId 分类id<br/>
      */
	@RequestMapping("/property_tree")
	public JsonResult<List<ZTreeNode>> propertyListTree(Long catId){
		try{
			if(catId == null){
				throw new WakaException("属性id不能为空");
			}

			return JsonResult.newSuccess(adminCategoryService.propertyListTree(catId));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());

		}

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

		try{
			// createMethodSinge创建方法
			if(propertyId == null){
				throw new WakaException("属性id不能为空");
			}
			if(Tools.string.isEmpty(propertyName)){
				throw new WakaException("属性名称不能为空");
			}
			if(sequence == null){
				throw new WakaException("排序不能为空");
			}
			return JsonResult.newSuccess(adminCategoryService.modifyProperty(propertyId, propertyName, sequence));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());

		}

	}
	
	/**
	 * 获取分类属性设置
	 * 
     * @param propertyId 属性id<br/>
     */
	@RequestMapping("/get_propertyset")
	public JsonResult<PropertySettingVo> getProSetting(Long propertyId){
		try{
			// createMethodSinge创建方法
			if(propertyId == null){
				throw new WakaException("属性id不能为空");
			}
			return JsonResult.newSuccess(adminCategoryService.getProSetting(propertyId));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());

		}

	}
	
	/**
	 * 修改分类属性设置
	 * 
     * @param vo 分类设置
     */
	@RequestMapping("/modify_propertyset")
	public JsonResult<Boolean> modifyProSetting(@RequestBody PropertySettingVo vo){
		try{
			if(vo.getPropertyId() == null){
				throw new WakaException("属性id不能为空");
			}
			return JsonResult.newSuccess(adminCategoryService.modifyProSetting(vo));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}
	}

}