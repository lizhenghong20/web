package cn.farwalker.ravv.admin.category;

import cn.farwalker.ravv.admin.category.dto.PropertySettingVo;
import cn.farwalker.ravv.admin.category.dto.PropertyTreeVo;
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
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.core.ZTreeNode;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {
    @Resource
    private IBaseCategoryBiz baseCategoryBiz;

    @Resource
    private IBaseCategoryPropertyBiz baseCategoryPropertyBiz;

    @Resource
    private IBaseCategoryPropertyValueBiz baseCategoryPropertyValueBiz;

    @Resource
    private IGoodsSpecificationDefBiz goodsSpecificationDefBiz;

    @Resource
    private IBaseCategoryBiz categoryBiz;

    @Resource
    private IBaseCategoryPropertyBiz categoryPropertyBiz;

    @Resource
    private IGoodsBiz goodsBiz;

    protected IBaseCategoryBiz getBiz(){
        return baseCategoryBiz;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean deleteCategory(Long catId) {
        //判断该分类是否为叶节点(有下级分类)，有则不给删除
        BaseCategoryBo category = baseCategoryBiz.selectById(catId);
        if (null != category) {
            if (!category.getLeaf()) {
                throw new WakaException("该分类有下级分类，不能删除");
            }
        } else {
            throw new WakaException("没有对应分类");
        }

        //获取指定分类下的所有属性
        List<BaseCategoryPropertyBo> proListByCatId = baseCategoryPropertyBiz.getProListByCatId(catId, null);

        //删除指定分类下的所有属性
        if (CollectionUtils.isNotEmpty(proListByCatId)) {
            throw new WakaException("该分类有下级属性，不能删除");
        }
        //删除指定分类
        Boolean rs = baseCategoryBiz.deleteById(catId);
        if(rs){
            //判断父级分类是否成为子节点
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

            return rs;
        } else{
            throw 	new WakaException("删除分类失败");
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean createCategory(Long pid, String categoryName, Integer sequence) {
        Boolean isAtRoot = false;
        if(pid == -1L){
            pid = 0L;
            isAtRoot = true;
        }

        // 检查同级下是否有相同名称的类目
        BaseCategoryBo sameCategoryByPid = baseCategoryBiz.getSameCategoryByPid(pid, categoryName);
        if(null != sameCategoryByPid){
            throw new WakaException("添加类目失败，类目已经存在");
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

            return rs;
        } else{
            throw new WakaException("添加类目失败");
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean createCatProperty(Long catId, String propertyName, Integer sequence) {
        // 检查相同类目下有无相同属性
        BaseCategoryPropertyBo samePropertyByCatId = baseCategoryPropertyBiz.getSamePropertyByCatId(catId,propertyName);
        if(null != samePropertyByCatId){
            throw new WakaException("添加属性失败，属性已经存在");
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
        if(!rs){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }

        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean createProValue(Long propertyId, String valueName, Integer sequence) {
        // 获取对应属性信息
        BaseCategoryPropertyBo baseCategoryProperty = baseCategoryPropertyBiz.selectById(propertyId);
        if(null == baseCategoryProperty){
            throw new WakaException("没有对应属性");
        }

        //检查相同属性名下是否有相同的属性值名称
        BaseCategoryPropertyValueBo sameCatProValByProId = baseCategoryPropertyValueBiz.getSameCatProValByProId(propertyId, valueName);
        if(null != sameCatProValByProId){
            throw new WakaException("添加属性值失败，属性值已经存在");
        }

        BaseCategoryPropertyValueBo baseCategoryPropertyValue = new BaseCategoryPropertyValueBo();
        baseCategoryPropertyValue.setValueName(valueName);
        baseCategoryPropertyValue.setPropertyId(propertyId);
        baseCategoryPropertyValue.setTmpPropertyName(baseCategoryProperty.getPropertyName());
        baseCategoryPropertyValue.setTmpCategoryId(baseCategoryProperty.getCatId());
        baseCategoryPropertyValue.setStatus(StatusEnum.ENABLE);

        Boolean rs = baseCategoryPropertyValueBiz.insert(baseCategoryPropertyValue);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        return rs;
    }

    @Override
    public List<ZTreeNode> categoryListTree() {
        List<Long> categoryIds = baseCategoryBiz.getCategoryIds();

        if(CollectionUtils.isEmpty(categoryIds)){
            return new ArrayList<>();
        } else{
            List<ZTreeNode> categoryTree = baseCategoryBiz.categoryListTree(categoryIds);
            if(Tools.collection.isEmpty(categoryTree)){
                throw new WakaException("categoryTree为空");
            }
            return categoryTree;
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean modifyCategory(Long catId, String categoryName, Integer sequence) {
        BaseCategoryBo baseCategory = baseCategoryBiz.selectById(catId);
        if(null == baseCategory){
            throw new WakaException("找不到对应分类");
        }
        // 检查同级下是否有相同名称的类目
        BaseCategoryBo sameCategoryByPid = baseCategoryBiz.getSameCategoryByPid(baseCategory.getPid(), categoryName);
        if(null != sameCategoryByPid){
            if(!sameCategoryByPid.getId().equals(catId)){
                throw new WakaException("修改分类失败，分类名已经存在");
            }
        }
        baseCategory.setCatName(categoryName);
        baseCategory.setSequence(sequence);
        Boolean rs = baseCategoryBiz.updateById(baseCategory);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean deleteProValue(Long valueId) {
        //判断是否被商品引用
        EntityWrapper<GoodsSpecificationDefBo> wrapper = new EntityWrapper<>();
        wrapper.eq(GoodsSpecificationDefBo.Key.propertyValueId.toString(), valueId);
        GoodsSpecificationDefBo goodsSpecDef = goodsSpecificationDefBiz.selectOne(wrapper);
        if(null != goodsSpecDef) {
            GoodsBo good = goodsBiz.selectById(goodsSpecDef.getGoodsId());
            if(null != good) {
                throw new WakaException("该属性值已被商品：" + good.getGoodsName() + "引用，不能删除");
            }else {
                throw new WakaException("该属性值已被商品引用，不能删除");
            }
        }
        //删除指定属性值
        Boolean rs = baseCategoryPropertyValueBiz.deleteById(valueId);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean modifyProValue(Long valueId, String valueName, Integer sequence) {

        BaseCategoryPropertyValueBo baseCatProValue = baseCategoryPropertyValueBiz.selectById(valueId);
        if(null == baseCatProValue){
            throw new WakaException("找不到对应属性值");
        }
        //检查相同属性名下是否有相同的属性值名称
        BaseCategoryPropertyValueBo sameCatProValByProId = baseCategoryPropertyValueBiz.getSameCatProValByProId(baseCatProValue.getPropertyId(), valueName);
        if(null != sameCatProValByProId){
            if(!sameCatProValByProId.getId().equals(valueId)){
                throw new WakaException("修改属性值失败，属性值已经存在");
            }
        }
        baseCatProValue.setValueName(valueName);
        Boolean rs = baseCategoryPropertyValueBiz.updateById(baseCatProValue);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean deleteProperty(Long propertyId) {
        //获取指定属性的所有属性值
        List<BaseCategoryPropertyValueBo> proValListByProId = baseCategoryPropertyValueBiz.getProValListByProId(propertyId);

        //删除指定属性下的所有属性值
        if(CollectionUtils.isNotEmpty(proValListByProId)){
            throw new WakaException("该属性有下级属性值，不能删除");
        }

        //删除指定分类属性
        Boolean rs = baseCategoryPropertyBiz.deleteById(propertyId);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        return rs;
    }

    @Override
    public List<ZTreeNode> propertyListTree(Long catId) {
        // createMethodSinge创建方法
        List<ZTreeNode> proListTree = baseCategoryPropertyBiz.getProListTreeByCatId(catId);
        List<ZTreeNode> proValListTree = baseCategoryPropertyValueBiz.getProValListTreeByCatId(catId);
        proListTree.addAll(proValListTree);

        return proListTree;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean modifyProperty(Long propertyId, String propertyName, Integer sequence) {
        BaseCategoryPropertyBo baseCatProperty = baseCategoryPropertyBiz.selectById(propertyId);
        if(null == baseCatProperty){
            throw new WakaException("没找到对应属性");
        }
        //检查相同分类下是否有相同的属性名称
        BaseCategoryPropertyBo samePropertyByCatId = baseCategoryPropertyBiz.getSamePropertyByCatId(baseCatProperty.getCatId(), propertyName);
        if(null != samePropertyByCatId){
            if(!samePropertyByCatId.getId().equals(propertyId)){
                throw new WakaException("修改属性名失败，属性名已经存在");
            }
        }
        baseCatProperty.setPropertyName(propertyName);
        baseCatProperty.setSequence(sequence);

        Boolean rs = baseCategoryPropertyBiz.updateById(baseCatProperty);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }

    @Override
    public PropertySettingVo getProSetting(Long propertyId) {
        BaseCategoryPropertyBo property = baseCategoryPropertyBiz.selectById(propertyId);
        if(null == property) {
            throw new WakaException("没有对应属性");
        }

        PropertySettingVo propertySetting = new PropertySettingVo();
        propertySetting.setPropertyId(property.getId());
        propertySetting.setPropertyType(property.getType().getKey());
        propertySetting.setRequired(property.getRequired());
        propertySetting.setManuallyInput(property.getManuallyInput());
        propertySetting.setMultiSelect(property.getMultiSelect());
        propertySetting.setIsimage(property.getIsimage());

        return propertySetting;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean modifyProSetting(PropertySettingVo vo) {
        BaseCategoryPropertyBo property = baseCategoryPropertyBiz.selectById(vo.getPropertyId());
        if(null == property) {
            throw new WakaException("没有对应属性");
        }

        property.setType(PropertyTypeEnum.getEnumByKey(vo.getPropertyType()));
        property.setRequired(vo.getRequired());
        property.setManuallyInput(vo.getManuallyInput());
        property.setMultiSelect(vo.getMultiSelect());
        property.setIsimage(vo.getIsimage());

        Boolean rs = baseCategoryPropertyBiz.updateById(property);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }

    @Override
    public List<BaseCategoryBo> getChildren(Long parentid) {
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
        return rds;
    }

    @Override
    public List<PropertyTreeVo> getPropertyChild(Long catid) {

        Wrapper<BaseCategoryPropertyBo> query = new EntityWrapper<>();
        final String CATID = BaseCategoryPropertyBo.Key.catId.toString();
        if(catid == null){
            return new ArrayList<>();
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
        return propertyTreeVo;
    }
}

