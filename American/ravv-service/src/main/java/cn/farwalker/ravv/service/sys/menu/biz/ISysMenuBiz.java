package cn.farwalker.ravv.service.sys.menu.biz;
import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import cn.farwalker.ravv.service.sys.menu.model.SysMenuBo;

/**
 * 菜单表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface ISysMenuBiz extends IService<SysMenuBo>{
    /**
     * 删除菜单
     *
     * @author Jason Chen
     * @Date 2017/5/5 22:20
     */
    void deleteMenu(Long menuId);

    /**
     * 删除菜单包含所有子菜单
     *
     * @author Jason Chen
     * @Date 2017/6/13 22:02
     */
    void deleteMenuContainSubMenus(Long menuId);
    
    /**
     * 获取用户菜单编号列表（用于Vue前端做权限控制）
     * @param userId 用户id
     * @return
     */
    List<String> getRoleCodeList(Long userId);
}