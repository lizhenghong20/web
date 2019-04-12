package cn.farwalker.standard.common.constant.factory;

import java.util.ArrayList;
import java.util.List;

import cn.farwalker.standard.core.temp.LogObjectHolder;
import cn.farwalker.standard.core.temp.ManagerStatus;
import cn.farwalker.standard.core.temp.MenuStatus;
import cn.farwalker.waka.core.StrKit;
import cn.farwalker.waka.util.Tools;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import cn.farwalker.ravv.service.sys.dept.dao.ISysDeptDao;
import cn.farwalker.ravv.service.sys.dept.model.SysDeptBo;
import cn.farwalker.ravv.service.sys.dict.dao.ISysDictDao;
import cn.farwalker.ravv.service.sys.dict.model.SysDictBo;
import cn.farwalker.ravv.service.sys.menu.dao.ISysMenuDao;
import cn.farwalker.ravv.service.sys.menu.model.SysMenuBo;
import cn.farwalker.ravv.service.sys.notice.dao.ISysNoticeDao;
import cn.farwalker.ravv.service.sys.notice.model.SysNoticeBo;
import cn.farwalker.ravv.service.sys.role.dao.ISysRoleDao;
import cn.farwalker.ravv.service.sys.role.model.SysRoleBo;
import cn.farwalker.ravv.service.sys.user.dao.ISysUserDao;
import cn.farwalker.ravv.service.sys.user.model.SysUserBo;
import cn.farwalker.waka.constants.SexEnum;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * 常量的生产工厂
 *
 * @author Jason Chen
 * @date 2017年2月13日 下午10:55:21
 */
@Component
public class ConstantFactory implements IConstantFactory {

	private ISysRoleDao roleMapper = Tools.springContext.getBean(ISysRoleDao.class);
	private ISysDeptDao deptMapper = Tools.springContext.getBean(ISysDeptDao.class);
	private ISysDictDao dictMapper = Tools.springContext.getBean(ISysDictDao.class);
	private ISysUserDao userMapper = Tools.springContext.getBean(ISysUserDao.class);
	private ISysMenuDao menuMapper = Tools.springContext.getBean(ISysMenuDao.class);
	private ISysNoticeDao noticeMapper = Tools.springContext.getBean(ISysNoticeDao.class);

	public static IConstantFactory me() {
		return (IConstantFactory)Tools.springContext.getBean("constantFactory");
	}

	/**
	 * 根据用户id获取用户名称
	 *
	 * @author Jason Chen
	 * @Date 2017/5/9 23:41
	 */
	@Override
	public String getUserNameById(Long userId) {
		SysUserBo user = userMapper.selectById(userId);
		if (user != null) {
			return user.getName();
		} else {
			return "--";
		}
	}

	/**
	 * 根据用户id获取用户账号
	 *
	 * @author Jason Chen
	 * @date 2017年5月16日21:55:371
	 */
	@Override
	public String getUserAccountById(Long userId) {
		SysUserBo user = userMapper.selectById(userId);
		if (user != null) {
			return user.getAccount();
		} else {
			return "--";
		}
	}

	/**
	 * 通过角色ids获取角色名称
	 */
	@Override
	public String getRoleName(String roleIds) {
		Long[] roles = (Long[])Tools.string.convertStringToLong(roleIds).toArray();
		StringBuilder sb = new StringBuilder();
		for (Long role : roles) {
			SysRoleBo roleObj = roleMapper.selectById(role);
			if (roleObj != null && Tools.string.isNotEmpty(roleObj.getName())) {
				sb.append(roleObj.getName()).append(",");
			}
		}
		return StrKit.removeSuffix(sb.toString(), ",");
	}

	/**
	 * 通过角色id获取角色名称
	 */
	@Override
	public String getSingleRoleName(Long roleId) {
		if (0 == roleId) {
			return "--";
		}
		SysRoleBo roleObj = roleMapper.selectById(roleId);
		if (roleObj != null && Tools.string.isNotEmpty(roleObj.getName())) {
			return roleObj.getName();
		}
		return "";
	}

	/**
	 * 通过角色id获取角色英文名称
	 */
	@Override
	public String getSingleRoleTip(Long roleId) {
		if (0 == roleId) {
			return "--";
		}
		SysRoleBo roleObj = roleMapper.selectById(roleId);
		if (roleObj != null && Tools.string.isNotEmpty(roleObj.getName())) {
			return roleObj.getTips();
		}
		return "";
	}

	/**
	 * 获取部门名称
	 */
	@Override
	public String getDeptName(Long deptId) {
		SysDeptBo dept = deptMapper.selectById(deptId);
		if (dept != null && Tools.string.isNotEmpty(dept.getFullname())) {
			return dept.getFullname();
		}
		return "";
	}

	/**
	 * 获取菜单的名称们(多个)
	 */
	@Override
	public String getMenuNames(String menuIds) {
		List<Long> menus = Tools.string.convertStringToLong(menuIds);
		StringBuilder sb = new StringBuilder();
		for (Long menu : menus) {
			SysMenuBo menuObj = menuMapper.selectById(menu);
			if (menuObj != null && Tools.string.isNotEmpty(menuObj.getName())) {
				sb.append(menuObj.getName()).append(",");
			}
		}
		return StrKit.removeSuffix(sb.toString(), ",");
	}

	/**
	 * 获取菜单名称
	 */
	@Override
	public String getMenuName(Long menuId) {
		if (menuId != null) {
			return "";
		} else {
			SysMenuBo menu = menuMapper.selectById(menuId);
			if (menu == null) {
				return "";
			} else {
				return menu.getName();
			}
		}
	}

	/**
	 * 获取菜单名称通过编号
	 */
	@Override
	public String getMenuNameByCode(String code) {
		if (Tools.string.isEmpty(code)) {
			return "";
		} else {
			SysMenuBo param = new SysMenuBo();
			param.setCode(code);
			SysMenuBo menu = menuMapper.selectOne(param);
			if (menu == null) {
				return "";
			} else {
				return menu.getName();
			}
		}
	}

	/**
	 * 获取字典名称
	 */
	@Override
	public String getDictName(Long dictId) {
		if (dictId == null) {
			return "";
		} else {
			SysDictBo dict = dictMapper.selectById(dictId);
			if (dict == null) {
				return "";
			} else {
				return dict.getName();
			}
		}
	}

	/**
	 * 获取通知标题
	 */
	@Override
	public String getNoticeTitle(Long dictId) {
		if (dictId == null) {
			return "";
		} else {
			SysNoticeBo notice = noticeMapper.selectById(dictId);
			if (notice == null) {
				return "";
			} else {
				return notice.getTitle();
			}
		}
	}

	/**
	 * 根据字典名称和字典中的值获取对应的名称
	 */
	@Override
	public String getDictsByName(String name, Integer val) {
		SysDictBo temp = new SysDictBo();
		temp.setName(name);
		SysDictBo dict = dictMapper.selectOne(temp);
		if (dict == null) {
			return "";
		} else {
			Wrapper<SysDictBo> wrapper = new EntityWrapper<>();
			wrapper = wrapper.eq(SysDictBo.Key.pid.toString() , dict.getId());
			List<SysDictBo> dicts = dictMapper.selectList(wrapper);
			for (SysDictBo item : dicts) {
				if (item.getNum() != null && item.getNum().equals(val)) {
					return item.getName();
				}
			}
			return "";
		}
	}

	/**
	 * 获取性别名称
	 */
	@Override
	public String getSexName(String sex) {
		SexEnum sexEnum = SexEnum.getEnumByKey(sex);
		if(null == sexEnum) {
			return null;
		}else {
			return sexEnum.getLabel();
		}
	}

	/**
	 * 获取服务请求受理状态名称
	 */
	@Override
	public String getServiceReqStatus(Integer status) {
		return getDictsByName("服务请求受理", status);
	}

	/**
	 * 获取用户登录状态
	 */
	@Override
	public String getStatusName(Integer status) {
		return ManagerStatus.valueOf(status);
	}

	/**
	 * 获取菜单状态
	 */
	@Override
	public String getMenuStatusName(Integer status) {
		return MenuStatus.valueOf(status);
	}

	/**
	 * 查询字典
	 */
	@Override
	public List<SysDictBo> findInDict(Long id) {
		if (id == null) {
			return null;
		} else {
			EntityWrapper<SysDictBo> wrapper = new EntityWrapper<>();
			List<SysDictBo> dicts = dictMapper.selectList(wrapper.eq(SysDictBo.Key.pid.toString(), id));
			if (dicts == null || dicts.size() == 0) {
				return null;
			} else {
				return dicts;
			}
		}
	}

	/**
	 * 获取被缓存的对象(用户删除业务)
	 */
	@Override
	public String getCacheObject(String para) {
		return LogObjectHolder.me().get().toString();
	}

	/**
	 * 获取子部门id
	 */
	@Override
	public List<Long> getSubDeptId(Long deptid) {
		Wrapper<SysDeptBo> wrapper = new EntityWrapper<>();
		wrapper = wrapper.like(SysDeptBo.Key.pids.toString(), "%[" + deptid + "]%");
		List<SysDeptBo> depts = this.deptMapper.selectList(wrapper);

		ArrayList<Long> deptids = new ArrayList<>();

		if (depts != null && depts.size() > 0) {
			for (SysDeptBo dept : depts) {
				deptids.add(dept.getId());
			}
		}

		return deptids;
	}

	/**
	 * 获取所有父部门id
	 */
	@Override
	public List<Long> getParentDeptIds(Long deptid) {
		SysDeptBo dept = deptMapper.selectById(deptid);
		String pids = dept.getPids();
		String[] split = pids.split(",");
		ArrayList<Long> parentDeptIds = new ArrayList<>();
		for (String s : split) {
			parentDeptIds.add(Long.valueOf(StrKit.removeSuffix(StrKit.removePrefix(s, "["), "]")));
		}
		return parentDeptIds;
	}

	/**
	 * 获取角色名称
	 */
	@Override
	public String getRoleTypeName(Integer type) {
		return getDictsByName("角色", type);
	}

	/**
	 * 激活状态名称
	 */
	@Override
	public String getEnabledName(Integer enabled) {
		return getDictsByName("激活状态", enabled);
	}



	/**
	 * 获取审核状态
	 */
	@Override
	public String getAuditSatusName(Integer auditSatus) {
		return getDictsByName("审核状态", auditSatus);
	}

	/**
	 * 公共获取字典对应数据名称的方法
	 */
	@Override
	public String getDictNameByValue(String dictName, Integer value) {
		return getDictsByName(dictName, value);
	}

	@Override
	public String getStatusName(boolean status) {
		if(status) {
			return "可用";
		}
		return "禁用";
	}

}
