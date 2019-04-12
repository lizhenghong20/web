package cn.farwalker.ravv.category;

import java.util.List;

import cn.farwalker.ravv.admin.category.AdminCategoryService;
import cn.farwalker.waka.core.WakaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.farwalker.ravv.admin.category.dto.PropertyTreeVo;
import cn.farwalker.ravv.service.category.basecategory.model.BaseCategoryBo;
import cn.farwalker.waka.core.JsonResult;


/** 分类树*/
@Slf4j
@RestController
@RequestMapping("/category/tree")
public class CategoryTreeController{


	@Autowired
	private AdminCategoryService adminCategoryService;
	
	/**
	 * 取分类树
	 * @param parentid
	 * @return
	 */
	@RequestMapping("/children")
	public JsonResult<List<BaseCategoryBo>> getChildren(Long parentid){
		try{
			return JsonResult.newSuccess(adminCategoryService.getChildren(parentid));
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
	 * 取属性树
	 * @param catid
	 * @return
	 */
	@RequestMapping("/property_tree")
	public JsonResult<List<PropertyTreeVo>> getPropertyChild(Long catid){
		try{
			return JsonResult.newSuccess(adminCategoryService.getPropertyChild(catid));
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
