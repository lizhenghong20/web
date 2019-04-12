package cn.farwalker.standard.core.shiro.factory;

import java.util.ArrayList;
import java.util.List;

import cn.farwalker.standard.core.temp.ManagerStatus;
import cn.farwalker.waka.util.Tools;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.farwalker.ravv.service.sys.menu.dao.ISysMenuMgrDao;
import cn.farwalker.ravv.service.sys.user.dao.ISysUserMgrDao;
import cn.farwalker.ravv.service.sys.user.model.SysUserBo;
import cn.farwalker.standard.common.constant.factory.ConstantFactory;
import cn.farwalker.standard.core.shiro.ShiroUser;

@Service
@Transactional(readOnly = true)
public class ShiroFactroy implements IShiro {

    @Autowired
    private ISysUserMgrDao userMgrDao;

    @Autowired
    private ISysMenuMgrDao menuDao;

    public static IShiro me() {
        return Tools.springContext.getBean(IShiro.class);
    }

    @Override
    public SysUserBo user(String account) {

    	SysUserBo user = userMgrDao.getByAccount(account);

        // 账号不存在
        if (null == user) {
            throw new CredentialsException();
        }
        // 账号被冻结
        if (user.getStatus() != ManagerStatus.OK.getCode()) {
            throw new LockedAccountException();
        }
        return user;
    }

    @Override
    public ShiroUser shiroUser(SysUserBo user) {
        ShiroUser shiroUser = new ShiroUser();

        shiroUser.setId(user.getId());            // 账号id
        shiroUser.setAccount(user.getAccount());// 账号
        shiroUser.setDeptId(user.getDeptid());    // 部门id
        shiroUser.setDeptName(ConstantFactory.me().getDeptName(user.getDeptid()));// 部门名称
        shiroUser.setName(user.getName());        // 用户名称

        Long[] roleArray = (Long[])Tools.string.convertStringToLong(user.getRoleid()).toArray();// 角色集合
        List<Long> roleList = new ArrayList<>();
        List<String> roleNameList = new ArrayList<String>();
        for (Long roleId : roleArray) {
            roleList.add(roleId);
            roleNameList.add(ConstantFactory.me().getSingleRoleName(roleId));
        }
        shiroUser.setRoleList(roleList);
        shiroUser.setRoleNames(roleNameList);

        return shiroUser;
    }

    @Override
    public List<String> findPermissionsByRoleId(Long roleId) {
        List<String> resUrls = menuDao.getResUrlsByRoleId(roleId);
        return resUrls;
    }

    @Override
    public String findRoleNameByRoleId(Long roleId) {
        return ConstantFactory.me().getSingleRoleTip(roleId);
    }

    @Override
    public SimpleAuthenticationInfo info(ShiroUser shiroUser, SysUserBo user, String realmName) {
        String credentials = user.getPassword();
        // 密码加盐处理
        String source = user.getSalt();
        ByteSource credentialsSalt = new Md5Hash(source);
        return new SimpleAuthenticationInfo(shiroUser, credentials, credentialsSalt, realmName);
    }

}
