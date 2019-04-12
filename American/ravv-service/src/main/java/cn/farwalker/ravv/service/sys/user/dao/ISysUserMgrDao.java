package cn.farwalker.ravv.service.sys.user.dao;

import java.util.List;
import java.util.Map;

import cn.farwalker.waka.core.DataScope;
import org.apache.ibatis.annotations.Param;

import cn.farwalker.ravv.service.sys.user.model.SysUserBo;

/**
 * 管理员的dao
 * 是以前的“cn.farwalker.standard.modular.system.dao.UserMgrDao"搬过来
 * @author Jason Chen
 * @date 2017年2月12日 下午8:43:52
 */
public interface ISysUserMgrDao {

    /**
     * 修改用户状态
     *
     * @param user
     * @date 2017年2月12日 下午8:42:31
     */
    int setStatus(@Param("userId") Long userId, @Param("status") int status);

    /**
     * 修改密码
     *
     * @param userId
     * @param pwd
     * @date 2017年2月12日 下午8:54:19
     */
    int changePwd(@Param("userId") Long userId, @Param("pwd") String pwd);

    /**
     * 根据条件查询用户列表
     *
     * @return
     * @date 2017年2月12日 下午9:14:34
     */
    List<Map<String, Object>> selectUsers(@Param("dataScope") DataScope dataScope, @Param("name") String name, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("deptid") Integer deptid);

    /**
     * 设置用户的角色
     *
     * @return
     * @date 2017年2月13日 下午7:31:30
     */
    int setRoles(@Param("userId") Long userId, @Param("roleIds") String roleIds);

    /**
     * 通过账号获取用户
     *
     * @param account
     * @return
     * @date 2017年2月17日 下午11:07:46
     */
    SysUserBo getByAccount(@Param("account") String account);
}
