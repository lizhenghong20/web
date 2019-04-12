package cn.farwalker.ravv.service.sys.menu.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.farwalker.ravv.service.sys.menu.model.SysMenuBo;

/**
 * 菜单表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface ISysMenuDao extends BaseMapper<SysMenuBo>{
	
    /**
     * 获取用户菜单编号列表（用于Vue前端做权限控制）
     * @param userId
     * @return
     */
    List<String> getRoleCodeList(@Param("userId") Long userId);
}