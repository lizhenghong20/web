package cn.farwalker.ravv.service.sys.user.model;

import java.util.List;

public class SysUserVo extends SysUserBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 权限列表
	 */
	List<String> roles;

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
}
