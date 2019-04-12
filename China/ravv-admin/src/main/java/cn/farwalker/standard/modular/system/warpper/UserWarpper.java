package cn.farwalker.standard.modular.system.warpper;

import cn.farwalker.ravv.service.sys.user.model.SysUserBo;
import cn.farwalker.standard.common.constant.factory.ConstantFactory;
import cn.farwalker.standard.core.util.MapUtil;
import cn.farwalker.waka.components.wechatpay.common.util.StringUtils;
import cn.farwalker.waka.constants.SexEnum;

import java.util.List;
import java.util.Map;

/**
 * 用户管理的包装类
 *
 * @author Jason Chen
 * @date 2017年2月13日 下午10:47:03
 */
public class UserWarpper extends BaseControllerWarpper {

    public UserWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
    	String sex = MapUtil.stringValue(map, SysUserBo.Key.sex.toString());
    	if(StringUtils.isNotEmpty(sex)) {
            map.put("sexName", SexEnum.getLabelByKey(sex));
    	}
    	String roleid = MapUtil.stringValue(map, SysUserBo.Key.roleid.toString());
    	if(StringUtils.isNotEmpty(roleid)) {
            map.put("roleName", ConstantFactory.me().getRoleName(roleid));
    	}
    	Long deptid = MapUtil.longValue(map, SysUserBo.Key.deptid.toString());
    	if(StringUtils.isNotEmpty(roleid)) {
            map.put("deptName", ConstantFactory.me().getDeptName(deptid));
    	}
    	Integer status = MapUtil.integerValue(map, SysUserBo.Key.status.toString());
    	if(null != status) {
    		 map.put("statusName", ConstantFactory.me().getStatusName((Integer) map.get("status")));
    	}
    }

}
