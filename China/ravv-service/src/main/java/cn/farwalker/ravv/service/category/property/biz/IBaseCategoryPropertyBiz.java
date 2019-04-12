package cn.farwalker.ravv.service.category.property.biz;
import java.util.List;

import cn.farwalker.waka.constants.StatusEnum;
import cn.farwalker.waka.core.ZTreeNode;
import com.baomidou.mybatisplus.service.IService;

import cn.farwalker.ravv.service.category.property.model.BaseCategoryPropertyBo;


/**
 * 类目与属性的关联<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IBaseCategoryPropertyBiz extends IService<BaseCategoryPropertyBo>{
	
	/**
	 * 检查是否有相同属性名
	 * @param catId 分类id
	 * @param propertyName 属性名称
	 * @return
	 */
	BaseCategoryPropertyBo getSamePropertyByCatId(Long catId, String propertyName);
	
	/**
	 * 获取指定分类的所有属性
	 * @param propertyId
	 * @param status 状态（null表示全部）
	 * @return
	 */
	List<BaseCategoryPropertyBo> getProListByCatId(Long catId,StatusEnum status);
	
	/**
	 * 获取指定分类的属性树
	 * @param catId
	 * @return
	 */
	List<ZTreeNode> getProListTreeByCatId(Long catId);
}