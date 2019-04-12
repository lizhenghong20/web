package cn.farwalker.ravv.admin.storehouse;

import cn.farwalker.ravv.admin.storehouse.dto.StorehouseUserVO;
import cn.farwalker.ravv.service.base.storehouse.model.StorehouseBo;
import cn.farwalker.ravv.service.sys.user.model.SysUserBo;
import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.web.crud.QueryFilter;

import java.util.List;

public interface AdminStorehouseService {

    Boolean updateStorehouse3(StorehouseBo vo);

    Boolean create(StorehouseBo vo, List<SysUserBo> sysUserSelection);

    Boolean delete(Long id);

    StorehouseBo getOne(Long id);

    Page<StorehouseBo> getList(List<QueryFilter> query, Integer start, Integer size,
                               String sortfield);

    Boolean update(StorehouseBo vo, List<SysUserBo> sysUserSelection);

    StorehouseUserVO getStorehouseUser(Long id);

    Boolean saveStorehouseUser(List<SysUserBo> sysUserSelection,Long storeId);
}
