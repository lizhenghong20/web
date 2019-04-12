package cn.farwalker.standard.modular.system.warpper;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import cn.farwalker.ravv.service.sys.role.model.SysRoleBo;
import cn.farwalker.standard.common.constant.factory.ConstantFactory;
import cn.farwalker.standard.core.util.MapUtil;


/**
 * 角色列表的包装类
 *
 * @author Jason Chen
 * @date 2017年2月19日10:59:02
 */
public class RoleWarpper extends BaseControllerWarpper {

    public RoleWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
    	Long pid = MapUtil.longValue(map, SysRoleBo.Key.pid.toString());
    	if(null != pid) {
    		map.put("pName", ConstantFactory.me().getSingleRoleName(pid));
    	}
    	Long deptid = MapUtil.longValue(map, SysRoleBo.Key.deptid.toString());
    	if(null != deptid) {
    		map.put("deptName", ConstantFactory.me().getDeptName(deptid));
    	}
    }

}
