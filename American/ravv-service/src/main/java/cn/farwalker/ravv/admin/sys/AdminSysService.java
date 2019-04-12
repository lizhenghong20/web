package cn.farwalker.ravv.admin.sys;

import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.shipment.model.ShipmentBo;
import cn.farwalker.ravv.service.shipment.model.ShipmentVo;
import cn.farwalker.ravv.service.sys.param.model.SysParamBo;
import cn.farwalker.ravv.service.sys.user.model.SysUserBo;
import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.web.crud.QueryFilter;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface AdminSysService {

    Boolean createShip(ShipmentBo vo);

    Boolean deleteShip(Long id);

    ShipmentVo getOneShip(Long id);

    Page<ShipmentVo> getListShip(List<QueryFilter> query, Integer start, Integer size,
                             String sortfield);

    Boolean updateShip(ShipmentBo vo);

    Page<GoodsBo> getNotInModelGoods(@RequestBody List<QueryFilter> query, Integer start,
                                      Integer size, String sortfield, String modelCode, String catePaths);

    List<ShipmentBo> generalShiplist();

    Boolean createSysparam(SysParamBo vo);

    Boolean deleteSysparam(Long id);

    SysParamBo getOneSysparam(Long id);

    Page<SysParamBo> getListSysparam(List<QueryFilter> query, Integer start, Integer size,
                                 String sortfield);

    Boolean updateSysparam(SysParamBo vo);

    Boolean cacheRefresh();

    Boolean createSysUser(SysUserBo vo);

    Boolean deleteSysUser(Long id);

    SysUserBo getOneSysUser(Long id);

    Page<SysUserBo> getListSysUser(List<QueryFilter> query, Integer start, Integer size,
                                     String sortfield);

    Boolean updateSysUser(SysUserBo vo);

    List<SysUserBo> getSysUserList();

}
