package cn.farwalker.ravv.service.sys.roleuser.biz;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.service.IService;
import cn.farwalker.ravv.service.sys.roleuser.model.SysRoleUserBo;

/**
 * 角色和菜单关联表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface ISysRoleUserBiz extends IService<SysRoleUserBo>{
	
	/**
	 * 保存用户关联的角色
	 * @param userId 用户id
	 * @param roleIds 角色id
	 * @return
	 */
	Boolean saveRoleIds(Long userId, String roleIds);
	
}