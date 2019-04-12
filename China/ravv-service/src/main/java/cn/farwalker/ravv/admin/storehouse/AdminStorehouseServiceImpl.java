package cn.farwalker.ravv.admin.storehouse;


import cn.farwalker.ravv.admin.storehouse.dto.StorehouseUserVO;
import cn.farwalker.ravv.service.base.area.biz.IBaseAreaBiz;
import cn.farwalker.ravv.service.base.area.model.BaseAreaBo;
import cn.farwalker.ravv.service.base.storehouse.biz.IStorehouseBiz;
import cn.farwalker.ravv.service.base.storehouse.biz.IStorehouseService;
import cn.farwalker.ravv.service.base.storehouse.biz.IStorehouseUserBiz;
import cn.farwalker.ravv.service.base.storehouse.model.StorehouseBo;
import cn.farwalker.ravv.service.base.storehouse.model.StorehouseUserBo;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsService;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.sys.user.biz.ISysUserBiz;
import cn.farwalker.ravv.service.sys.user.model.SysUserBo;
import cn.farwalker.waka.components.wechatpay.common.util.StringUtils;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.core.base.controller.ControllerUtils;
import cn.farwalker.waka.core.RavvExceptionEnum;
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

@Slf4j
@Service
public class AdminStorehouseServiceImpl implements AdminStorehouseService {

    @Resource
    private IStorehouseBiz storehouseBiz;
    protected IStorehouseBiz getBiz(){
        return storehouseBiz;
    }

    @Resource
    private IGoodsService goodsService;

    @Resource
    private IStorehouseUserBiz storehouseUserBiz;

    @Resource
    private IStorehouseService storehouseService;

    @Resource
    private IBaseAreaBiz baseAreaBiz;

    @Resource
    private ISysUserBiz sysUserBiz;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean updateStorehouse3(StorehouseBo vo) {
        Boolean rs = getBiz().updateById(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean create(StorehouseBo vo, List<SysUserBo> sysUserSelection) {
        //获取区域全路径名称
        StringBuffer areaname = new StringBuffer();
        if(StringUtils.isNotEmpty(vo.getAreaid())) {
            String[] areaId =  vo.getAreaid().split("/");
            if(areaId.length > 0) {
                for(int i = 0;i < areaId.length;i++) {
                    BaseAreaBo baseArea = baseAreaBiz.selectById(areaId[i]);
                    if(null != baseArea) {
                        if(i == 0) {
                            areaname.append(baseArea.getName());
                        }else {
                            areaname.append("/" + baseArea.getName());
                        }
                    }
                }
            }
        }

        Boolean rs =getBiz().insert(vo);

        //保存仓库管理员
        if(rs) {
            this.saveStorehouseUser(sysUserSelection, vo.getId());
        }

        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        //判断仓库是否已关联商品
        List<GoodsBo> goodsList =  goodsService.getGoodsBystoreId(id);
        if(CollectionUtils.isNotEmpty(goodsList)) {
            throw new WakaException("该仓库已有关联商品，不能删除。");
        }

        Boolean rs = getBiz().deleteById(id);
        return rs;
    }

    @Override
    public StorehouseBo getOne(Long id) {
        StorehouseBo rs = getBiz().selectById(id);
        if(rs == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        return rs;
    }

    @Override
    public Page<StorehouseBo> getList(List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        //createMethodSinge创建方法
        Page<StorehouseBo> page = ControllerUtils.getPage(start,size,sortfield);
        Wrapper<StorehouseBo> wrap = ControllerUtils.getWrapper(query);
        Page<StorehouseBo> rs = getBiz().selectPage(page,wrap);
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean update(StorehouseBo vo, List<SysUserBo> sysUserSelection) {
        //获取区域全路径名称
        StringBuffer areaname = new StringBuffer();
        if(StringUtils.isNotEmpty(vo.getAreaid())) {
            String[] areaId =  vo.getAreaid().split("/");
            if(areaId.length > 0) {
                for(int i = 0;i < areaId.length;i++) {
                    BaseAreaBo baseArea = baseAreaBiz.selectById(areaId[i]);
                    if(null != baseArea) {
                        if(i == 0) {
                            areaname.append(baseArea.getName());
                        }else {
                            areaname.append("/" + baseArea.getName());
                        }
                    }
                }
                vo.setAreaname(areaname.toString());
            }
        }else {
            vo.setAreaid("");
            vo.setAreaname("");
        }

        Boolean rs =getBiz().updateById(vo);
        //保存仓库管理员
        if(rs) {
            this.saveStorehouseUser(sysUserSelection, vo.getId());
        }

        return rs;
    }

    @Override
    public StorehouseUserVO getStorehouseUser(Long id) {
        StorehouseUserVO storehouseUserVO = new StorehouseUserVO();
        //获取系统管理员信息列表
        EntityWrapper<SysUserBo> userWrapper = new EntityWrapper<>();
        List<SysUserBo> sysUserList = sysUserBiz.selectList(userWrapper);
        if(CollectionUtils.isNotEmpty(sysUserList)) {
            for(SysUserBo sysUser : sysUserList) {
                sysUser.setSalt(null);
                sysUser.setPassword(null);
            }
            storehouseUserVO.setSysUserList(sysUserList);
        }

        //仓库关联用户信息列表
        if(null != id) {
            List<SysUserBo> storeUserList = storehouseService.sysUserListByStoreId(id);
            storehouseUserVO.setStorehouseUser(storeUserList);
        }

        return storehouseUserVO;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean saveStorehouseUser(List<SysUserBo> sysUserSelection, Long storeId) {
        EntityWrapper<StorehouseUserBo> storeWrapper = new EntityWrapper<>();
        storeWrapper.eq(StorehouseUserBo.Key.storeId.toString(), storeId);
        List<StorehouseUserBo> storehouseUserList = storehouseUserBiz.selectList(storeWrapper);
        if(CollectionUtils.isNotEmpty(storehouseUserList)) {
            for(StorehouseUserBo storehouseUser : storehouseUserList) {
                storehouseUserBiz.deleteById(storehouseUser.getId());
            }
        }

        //保存仓库新管理员
        if(CollectionUtils.isNotEmpty(sysUserSelection)) {
            for(SysUserBo sysUser : sysUserSelection) {
                StorehouseUserBo storehouseUser = new StorehouseUserBo();
                storehouseUser.setUserId(sysUser.getId());
                storehouseUser.setUserName(sysUser.getName());
                storehouseUser.setStoreId(storeId);

                storehouseUserBiz.insert(storehouseUser);
            }
        }

        return true;
    }
}
