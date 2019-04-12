package cn.farwalker.ravv.admin.sys;


import cn.farwalker.ravv.service.category.basecategory.biz.IBaseCategoryBiz;
import cn.farwalker.ravv.service.category.basecategory.model.BaseCategoryBo;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsBiz;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsService;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.shipment.biz.IShipmentBiz;
import cn.farwalker.ravv.service.shipment.constants.ShipmentTypeEnum;
import cn.farwalker.ravv.service.shipment.model.ShipmentBo;
import cn.farwalker.ravv.service.shipment.model.ShipmentVo;
import cn.farwalker.ravv.service.sys.param.biz.ISysParamBiz;
import cn.farwalker.ravv.service.sys.param.constants.DistributionLevelEnum;
import cn.farwalker.ravv.service.sys.param.model.SysParamBo;
import cn.farwalker.ravv.service.sys.user.biz.ISysUserBiz;
import cn.farwalker.ravv.service.sys.user.model.SysUserBo;
import cn.farwalker.waka.cache.CacheManager;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.core.base.controller.ControllerUtils;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.cangwu.frame.web.crud.QueryFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class AdminSysServiceImpl implements AdminSysService {

    @Resource
    private IShipmentBiz getshipBiz;

    @Resource
    private IBaseCategoryBiz baseCategoryBiz;

    protected IShipmentBiz getshipBiz() {
        return getshipBiz;
    }

    @Resource
    private ISysParamBiz sysParamBiz;

    protected ISysParamBiz getSysParamBiz(){
        return sysParamBiz;
    }

    @Resource
    private IGoodsBiz goodsBiz;

    protected IGoodsBiz getBiz() {
        return goodsBiz;
    }

    @Resource
    private ISysUserBiz sysUserBiz;
    protected ISysUserBiz getSysUserBiz(){
        return sysUserBiz;
    }

    @Resource
    private IGoodsService goodsService;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean createShip(ShipmentBo vo) {
        Boolean rs = getshipBiz().insert(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean deleteShip(Long id) {
        Boolean rs = getshipBiz().deleteById(id);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        return rs;
    }

    @Override
    public ShipmentVo getOneShip(Long id) {
        ShipmentBo shipmentBo = getshipBiz().selectById(id);
        ShipmentVo shipmentVo = new ShipmentVo();
        // 赋值BO数据到Vo中
        Tools.bean.cloneBean(shipmentBo, shipmentVo);
        if (Tools.string.isNotEmpty(shipmentBo.getCategoryPaths())) {
            String baseCategoryid = shipmentBo.getCategoryPaths();

            if (baseCategoryid.contains("/")) {
                int idx = baseCategoryid.lastIndexOf("/");
                baseCategoryid = baseCategoryid.substring(idx + 1, baseCategoryid.length());
            }
            Long catId = Long.parseLong(baseCategoryid);
            BaseCategoryBo baseCategoryBo = baseCategoryBiz.selectById(catId);
            shipmentVo.setCatId(catId);
            shipmentVo.setCatName(baseCategoryBo.getCatName());

            // 获取商品名称
            if (shipmentVo.getGoodsId() != null) {
                GoodsBo goodsBo = goodsBiz.selectById(shipmentVo.getGoodsId());
                if (null != goodsBo) {
                    shipmentVo.setGoodsName(goodsBo.getGoodsName());
                }
            }
        }
        return shipmentVo;
    }

    @Override
    public Page<ShipmentVo> getListShip(List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        Page<ShipmentBo> page = ControllerUtils.getPage(start, size, sortfield);
        Wrapper<ShipmentBo> wrap = ControllerUtils.getWrapper(query);
        Page<ShipmentBo> shipRs = getshipBiz().selectPage(page, wrap);

        // 将BO的数据复制到VO
        Page<ShipmentVo> shipmentVoPage = ControllerUtils.convertPageRecord(shipRs, ShipmentVo.class);

        for (ShipmentVo shipmentVo : shipmentVoPage.getRecords()) {
            // 获取末级类目名称
            if (Tools.string.isNotEmpty(shipmentVo.getCategoryPaths())) {
                String baseCategoryid = shipmentVo.getCategoryPaths();
                if (baseCategoryid.contains("/")) {
                    int idx = baseCategoryid.lastIndexOf("/");
                    baseCategoryid = baseCategoryid.substring(idx + 1, baseCategoryid.length());
                }
                Long catId = Long.parseLong(baseCategoryid);
                BaseCategoryBo baseCategoryBo = baseCategoryBiz.selectById(catId);
                shipmentVo.setCatId(catId);
                shipmentVo.setCatName(baseCategoryBo.getCatName());
            }

            // 获取商品名称
            if (shipmentVo.getGoodsId() != null) {
                GoodsBo goodsBo = goodsBiz.selectById(shipmentVo.getGoodsId());
                if (null != goodsBo) {
                    shipmentVo.setGoodsName(goodsBo.getGoodsName());
                }
            }
        }

        return shipmentVoPage;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean updateShip(ShipmentBo vo) {
        Boolean rs = getshipBiz().updateAllColumnById(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }

    @Override
    public Page<GoodsBo> getNotInModelGoods(List<QueryFilter> query, Integer start, Integer size, String sortfield, String modelCode, String catePaths) {
        long t1 = System.currentTimeMillis(), t2 = 0, t3 = 0;

        t2 = System.currentTimeMillis();
        Page<GoodsBo> page = ControllerUtils.getPage(start, size, sortfield);
        Wrapper<GoodsBo> wrap = ControllerUtils.getWrapper(query);
        wrap.like(GoodsBo.Key.categoryPath.toString(), catePaths);
        Page<GoodsBo> rs = goodsService.getGoodsNotInModelgoods(page, wrap, modelCode);

        t3 = System.currentTimeMillis();


        long t4 = System.currentTimeMillis();
        log.debug(String.format("time %d,%d,%d", t2 - t1, t3 - t2, t4 - t3));
        return rs;
    }

    @Override
    public List<ShipmentBo> generalShiplist() {
        ShipmentBo query = new ShipmentBo();
        //query.setShipmentType(ShipmentTypeEnum.General);
        EntityWrapper<ShipmentBo> wp = new EntityWrapper<ShipmentBo>(query);
        String typeList = ShipmentTypeEnum.General.getKey() + "," + ShipmentTypeEnum.Default.getKey();
        wp.in(ShipmentBo.Key.shipmentType.toString(), typeList);
        List<ShipmentBo> rs = getshipBiz().selectList(wp);
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean createSysparam(SysParamBo vo) {
        //校验分销层级数据录入是否合理
        if(vo.getCode().equals(DistributionLevelEnum.First_Distribution.getKey()) ||
                vo.getCode().equals(DistributionLevelEnum.Second_Distribution.getKey()) ||
                vo.getCode().equals(DistributionLevelEnum.Third_Distribution.getKey())) {
            //判断参数值为数字且不大于1
            Pattern pattern = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)?$");
            Matcher isNum = pattern.matcher(vo.getPvalue());
            if(!isNum.matches()){
                throw new WakaException("分销层级的参数值只能输入数字");
            }else if(Double.valueOf(vo.getPvalue()) <= 0 || Double.valueOf(vo.getPvalue()) > 1) {
                throw new WakaException("分销层级的参数值不能小于等于0或大于1");
            }
            //判断是否保存相同的分销等级
            Boolean checkSameDistLevel = sysParamBiz.checkSameDistLevel(vo.getId(), vo.getCode());
            if(checkSameDistLevel) {
                throw new WakaException(vo.getName() + "已存在，不能重复添加");
            }
        }
        Boolean rs = getSysParamBiz().insert(vo);
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean deleteSysparam(Long id) {
        Boolean rs = getSysParamBiz().deleteById(id);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        return rs;
    }

    @Override
    public SysParamBo getOneSysparam(Long id) {
        SysParamBo rs = getSysParamBiz().selectById(id);
        if(rs == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        return rs;
    }

    @Override
    public Page<SysParamBo> getListSysparam(List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        //createMethodSinge创建方法
        Page<SysParamBo> page =ControllerUtils.getPage(start,size,sortfield);
        Wrapper<SysParamBo> wrap =ControllerUtils.getWrapper(query);
        Page<SysParamBo> rs = getSysParamBiz().selectPage(page,wrap);
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean updateSysparam(SysParamBo vo) {
        //校验分销层级数据录入是否合理
        if(vo.getCode().equals(DistributionLevelEnum.First_Distribution.getKey()) ||
                vo.getCode().equals(DistributionLevelEnum.Second_Distribution.getKey()) ||
                vo.getCode().equals(DistributionLevelEnum.Third_Distribution.getKey())) {

            //判断参数值为数字且不大于1
            Pattern pattern = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)?$");
            Matcher isNum = pattern.matcher(vo.getPvalue());
            if(!isNum.matches()){
                throw new WakaException("分销层级的参数值只能输入数字");
            }else if(Double.valueOf(vo.getPvalue()) <= 0 || Double.valueOf(vo.getPvalue()) > 1) {
                throw new WakaException("分销层级的参数值不能小于等于0或大于1");
            }

            //判断是否保存相同的分销等级
            Boolean checkSameDistLevel = sysParamBiz.checkSameDistLevel(vo.getId(), vo.getCode());
            if(checkSameDistLevel) {
                throw new WakaException(vo.getName() + "已存在，不能重复添加");
            }
        }

        Boolean rs = getSysParamBiz().updateById(vo);
        return rs;
    }

    @Override
    public Boolean cacheRefresh() {
        CacheManager.cache.refresh();
        return true;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean createSysUser(SysUserBo vo) {
        Boolean rs = getSysUserBiz().insert(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean deleteSysUser(Long id) {
        Boolean rs = getSysUserBiz().deleteById(id);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        return rs;
    }

    @Override
    public SysUserBo getOneSysUser(Long id) {
        SysUserBo rs = getSysUserBiz().selectById(id);
        if(rs == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        return rs;
    }

    @Override
    public Page<SysUserBo> getListSysUser(List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        //createMethodSinge创建方法
        Page<SysUserBo> page =ControllerUtils.getPage(start,size,sortfield);
        Wrapper<SysUserBo> wrap =ControllerUtils.getWrapper(query);
        Page<SysUserBo> rs = getSysUserBiz().selectPage(page,wrap);
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean updateSysUser(SysUserBo vo) {
        Boolean rs = getSysUserBiz().updateById(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }

    @Override
    public List<SysUserBo> getSysUserList() {
        EntityWrapper<SysUserBo> wrapper = new EntityWrapper<>();
        List<SysUserBo> sysUserList = getSysUserBiz().selectList(wrapper);
        if(CollectionUtils.isNotEmpty(sysUserList)) {
            for(SysUserBo sysUser : sysUserList) {
                sysUser.setSalt(null);
                sysUser.setPassword(null);
            }
        }

        return sysUserList;
    }


}
