package cn.farwalker.ravv.login.dto;

import java.util.List;

import cn.farwalker.ravv.service.merchant.model.MerchantBo;

/**前端需要的对象*/
public class MerchantRoleVo extends MerchantBo{
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
