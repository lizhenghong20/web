package cn.farwalker.ravv.service.sys.role.dao;

import cn.farwalker.waka.core.ZTreeNode;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;

/**
 * 角色相关的dao
 *搬”cn.farwalker.standard.modular.system.dao.RoleDao"
 * @author Jason Chen
 * @date 2017年2月12日 下午8:43:52
 */
public interface ISysRoleMgrDao {

    /**
     * 根据条件查询角色列表
     *
     * @return
     * @date 2017年2月12日 下午9:14:34
     */
    List<Map<String, Object>> selectRoles(@Param("condition") String condition);

    /**
     * 删除某个角色的所有权限
     *
     * @param roleId 角色id
     * @return
     * @date 2017年2月13日 下午7:57:51
     */
    int deleteRolesById(@Param("roleId") Long roleId);

    /**
     * 获取角色列表树
     *
     * @return
     * @date 2017年2月18日 上午10:32:04
     */
    List<ZTreeNode> roleTreeList();

    /**
     * 获取角色列表树
     *
     * @return
     * @date 2017年2月18日 上午10:32:04
     */
    List<ZTreeNode> roleTreeListByRoleId(String[] roleId);


}
