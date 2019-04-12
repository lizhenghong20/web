package cn.farwalker.ravv.service.sys.menu.biz.impl;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.farwalker.ravv.service.sys.menu.biz.ISysMenuBiz;
import cn.farwalker.ravv.service.sys.menu.dao.ISysMenuDao;
import cn.farwalker.ravv.service.sys.menu.dao.ISysMenuMgrDao;
import cn.farwalker.ravv.service.sys.menu.model.SysMenuBo;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * 菜单表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class SysMenuBizImpl extends ServiceImpl<ISysMenuDao,SysMenuBo> implements ISysMenuBiz{
	@Resource
    private ISysMenuDao menuMapper;

    @Resource
    private ISysMenuMgrDao menuDao;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void deleteMenu(Long menuId) {

        //删除菜单
        this.menuMapper.deleteById(menuId);

        //删除关联的relation
        this.menuDao.deleteRelationByMenu(menuId);
    }

    @Override
    public void deleteMenuContainSubMenus(Long menuId) {

    	SysMenuBo menu = menuMapper.selectById(menuId);

        //删除当前菜单
    	deleteMenu(menuId);

        //删除所有子菜单
        Wrapper<SysMenuBo> wrapper = new EntityWrapper<>();
        wrapper = wrapper.like("pcodes", "%[" + menu.getCode() + "]%");
        List<SysMenuBo> menus = menuMapper.selectList(wrapper);
        for (SysMenuBo temp : menus) {
        	deleteMenu(temp.getId());
        }
    }

	@Override
	public List<String> getRoleCodeList(Long userId) {
		return menuMapper.getRoleCodeList(userId);
	}	
}