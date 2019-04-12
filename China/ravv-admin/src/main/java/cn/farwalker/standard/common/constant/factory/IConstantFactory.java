package cn.farwalker.standard.common.constant.factory;

import java.util.List;

import cn.farwalker.ravv.service.sys.dict.model.SysDictBo;

/**
 * 常量生产工厂的接口
 *
 * @author Jason Chen
 * @date 2017-06-14 21:12
 */
public interface IConstantFactory {

	/**
	 * 根据用户id获取用户名称
	 *
	 * @author Jason Chen
	 * @Date 2017/5/9 23:41
	 */
	String getUserNameById(Long userId);

	/**
	 * 根据用户id获取用户账号
	 *
	 * @author Jason Chen
	 * @date 2017年5月16日21:55:371
	 */
	String getUserAccountById(Long userId);

	/**
	 * 通过角色ids获取角色名称
	 */
	String getRoleName(String roleIds);

	/**
	 * 通过角色id获取角色名称
	 */
	String getSingleRoleName(Long roleId);

	/**
	 * 通过角色id获取角色英文名称
	 */
	String getSingleRoleTip(Long roleId);

	/**
	 * 获取部门名称
	 */
	String getDeptName(Long deptId);

	/**
	 * 获取菜单的名称们(多个)
	 */
	String getMenuNames(String menuIds);

	/**
	 * 获取菜单名称
	 */
	String getMenuName(Long menuId);

	/**
	 * 获取菜单名称通过编号
	 */
	String getMenuNameByCode(String code);

	/**
	 * 获取字典名称
	 */
	String getDictName(Long dictId);

	/**
	 * 获取通知标题
	 */
	String getNoticeTitle(Long dictId);

	/**
	 * 根据字典名称和字典中的值获取对应的名称
	 */
	String getDictsByName(String name, Integer val);

	/**
	 * 获取性别名称
	 */
	String getSexName(String sex);
	
	/**
	 * 获取服务请求受理状态名称
	 */
	String getServiceReqStatus(Integer status);

	/**
	 * 获取用户登录状态
	 */
	String getStatusName(Integer status);

	/**
	 * 获取菜单状态
	 */
	String getMenuStatusName(Integer status);

	/**
	 * 查询字典
	 */
	List<SysDictBo> findInDict(Long id);

	/**
	 * 获取被缓存的对象(用户删除业务)
	 */
	String getCacheObject(String para);

	/**
	 * 获取子部门id
	 */
	List<Long> getSubDeptId(Long deptid);

	/**
	 * 获取所有父部门id
	 */
	List<Long> getParentDeptIds(Long deptid);

	/**
	 * 获取角色名称
	 * 
	 * @param long1
	 * @return
	 */
	String getRoleTypeName(Integer roleType);

	/**
	 * 获取激活状态名称
	 * 
	 * @param long1
	 * @return
	 */
	String getEnabledName(Integer enabled);
	
	
	/**
	 * 获取审核状态名称
	 * 
	 * @param long1
	 * @return
	 */
	String getAuditSatusName(Integer auditSatus);
	
	/**
	 * 获取对应的状态名称
	 */
	String getDictNameByValue(String dictName, Integer value);
	
	/**
	 * 获取当前状态名称(仅限 true，false)
	 * @param status
	 * @return
	 */
	String getStatusName(boolean status);
	
}
