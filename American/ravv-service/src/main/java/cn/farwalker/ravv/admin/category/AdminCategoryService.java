package cn.farwalker.ravv.admin.category;

import cn.farwalker.ravv.admin.category.dto.PropertySettingVo;
import cn.farwalker.ravv.admin.category.dto.PropertyTreeVo;
import cn.farwalker.ravv.service.category.basecategory.model.BaseCategoryBo;
import cn.farwalker.waka.core.ZTreeNode;

import java.util.List;

public interface AdminCategoryService {
    public Boolean deleteCategory(Long catId);

    public Boolean createCategory(Long pid,String categoryName,Integer sequence);

    public Boolean createCatProperty(Long catId,String propertyName,Integer sequence);

    public Boolean createProValue(Long propertyId,String valueName,Integer sequence);

    public List<ZTreeNode> categoryListTree();

    public Boolean modifyCategory(Long catId,String categoryName,Integer sequence);

    public Boolean deleteProValue(Long valueId);

    public Boolean modifyProValue(Long valueId,String valueName,Integer sequence);

    public Boolean deleteProperty(Long propertyId);

    public List<ZTreeNode> propertyListTree(Long catId);

    public Boolean modifyProperty(Long propertyId,String propertyName,Integer sequence);

    public PropertySettingVo getProSetting(Long propertyId);

    public Boolean modifyProSetting(PropertySettingVo vo);

    public List<BaseCategoryBo> getChildren(Long parentid);

    public List<PropertyTreeVo> getPropertyChild(Long catid);

}
