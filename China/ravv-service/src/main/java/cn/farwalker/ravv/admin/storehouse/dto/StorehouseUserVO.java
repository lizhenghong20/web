package cn.farwalker.ravv.admin.storehouse.dto;

import java.util.List;

import cn.farwalker.ravv.service.sys.user.model.SysUserBo;

/**
 * 仓库管理员信息
 * @author chensl
 *
 */
public class StorehouseUserVO {

	/**系统用户信息列表*/
	private List<SysUserBo> sysUserList;
	
	/**仓库关联用户信息列表*/
	private List<SysUserBo> storehouseUser;

	public List<SysUserBo> getSysUserList() {
		return sysUserList;
	}

	public void setSysUserList(List<SysUserBo> sysUserList) {
		this.sysUserList = sysUserList;
	}

	public List<SysUserBo> getStorehouseUser() {
		return storehouseUser;
	}

	public void setStorehouseUser(List<SysUserBo> storehouseUser) {
		this.storehouseUser = storehouseUser;
	}
}
