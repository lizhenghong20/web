package cn.farwalker.ravv.service.sys.role.biz.impl;
import java.util.List;

import javax.annotation.Resource;

import cn.farwalker.waka.util.Tools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.farwalker.ravv.service.sys.relation.dao.ISysRelationDao;
import cn.farwalker.ravv.service.sys.relation.model.SysRelationBo;
import cn.farwalker.ravv.service.sys.role.model.SysRoleBo;
import cn.farwalker.ravv.service.sys.role.dao.ISysRoleDao;
import cn.farwalker.ravv.service.sys.role.dao.ISysRoleMgrDao;
import cn.farwalker.ravv.service.sys.role.biz.ISysRoleBiz;
/**
 * 角色表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class SysRoleBizImpl extends ServiceImpl<ISysRoleDao,SysRoleBo> implements ISysRoleBiz{
	@Resource
   private ISysRoleDao roleMapper;

    @Resource
   private  ISysRoleMgrDao roleDao;

    @Resource
    private ISysRelationDao relationMapper;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void saveAuthority(Long roleId, String ids) {

        // 删除该角色所有的权限
        this.roleDao.deleteRolesById(roleId);

        // 添加新的权限
        for (Long id : Tools.string.convertStringToLong(ids)) {
            SysRelationBo relation = new SysRelationBo();
            relation.setRoleid(roleId);
            relation.setMenuid(id);
            this.relationMapper.insert(relation);
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void deleteRoleById(Long roleId) {
        //删除角色
        this.roleMapper.deleteById(roleId);

        // 删除该角色所有的权限
        this.roleDao.deleteRolesById(roleId);
    }
}