package cn.farwalker.ravv.service.sys.role.biz;
import com.baomidou.mybatisplus.service.IService;
import cn.farwalker.ravv.service.sys.role.model.SysRoleBo;

/**
 * 角色表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface ISysRoleBiz extends IService<SysRoleBo>{
    /**
     * 设置某个角色的权限
     *
     * @param roleId 角色id
     * @param ids    权限的id
     * @date 2017年2月13日 下午8:26:53
     */
    void saveAuthority(Long roleId, String ids);

    /**
     * 删除角色
     *
     * @author Jason Chen
     * @Date 2017/5/5 22:24
     */
    void deleteRoleById(Long roleId);
}