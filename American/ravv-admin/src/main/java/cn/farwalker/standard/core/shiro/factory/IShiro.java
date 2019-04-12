package cn.farwalker.standard.core.shiro.factory;

import java.util.List;

import org.apache.shiro.authc.SimpleAuthenticationInfo;

import cn.farwalker.ravv.service.sys.user.model.SysUserBo;
import cn.farwalker.standard.core.shiro.ShiroUser;

/**
 * 定义shirorealm所需数据的接口
 *
 * @author Jason Chen
 * @date 2016年12月5日 上午10:23:34
 */
public interface IShiro {

    /**
     * 根据账号获取登录用户
     *
     * @param account 账号
     */
    SysUserBo user(String account);

    /**
     * 根据系统用户获取Shiro的用户
     *
     * @param user 系统用户
     */
    ShiroUser shiroUser(SysUserBo user);

    /**
     * 获取权限列表通过角色id
     *
     * @param roleId 角色id
     */
    List<String> findPermissionsByRoleId(Long roleId);

    /**
     * 根据角色id获取角色名称
     *
     * @param roleId 角色id
     */
    String findRoleNameByRoleId(Long roleId);

    /**
     * 获取shiro的认证信息
     */
    SimpleAuthenticationInfo info(ShiroUser shiroUser, SysUserBo user, String realmName);

}
