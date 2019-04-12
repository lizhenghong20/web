package cn.farwalker.ravv.service.sys.roleuser.biz.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;import com.baomidou.mybatisplus.toolkit.CollectionUtils;

import cn.farwalker.ravv.service.sys.roleuser.model.SysRoleUserBo;
import cn.farwalker.ravv.service.sys.roleuser.dao.ISysRoleUserDao;
import cn.farwalker.ravv.service.sys.roleuser.biz.ISysRoleUserBiz;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色和菜单关联表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class SysRoleUserBizImpl extends ServiceImpl<ISysRoleUserDao,SysRoleUserBo> implements ISysRoleUserBiz{

	@Resource
	private ISysRoleUserBiz sysRoleUserBiz;
	
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Boolean saveRoleIds(Long userId, String roleIds) {
		//清除原有的角色
		Wrapper<SysRoleUserBo> wrapper = new EntityWrapper<>();
		wrapper.eq(SysRoleUserBo.Key.userid.toString(), userId);
		List<SysRoleUserBo> sysRoleUserList = sysRoleUserBiz.selectList(wrapper);
		if(CollectionUtils.isNotEmpty(sysRoleUserList)) {
			for(SysRoleUserBo sysRoleUserBo : sysRoleUserList) {
				sysRoleUserBiz.deleteById(sysRoleUserBo.getId());
			}
		}
		
		//保存用户新的角色
		String[] roleIdList = roleIds.split(",");
		if(roleIdList.length > 0) {
			for(int i = 0;i < roleIdList.length;i++) {
				SysRoleUserBo sysRoleUser = new SysRoleUserBo();
				sysRoleUser.setUserid(userId);
				sysRoleUser.setRoleid(Long.valueOf(roleIdList[i]));	
				
				sysRoleUserBiz.insert(sysRoleUser);
			}
		}
		
		return true;
	}
}