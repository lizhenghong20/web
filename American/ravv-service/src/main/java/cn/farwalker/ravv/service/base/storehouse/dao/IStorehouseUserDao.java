package cn.farwalker.ravv.service.base.storehouse.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.farwalker.ravv.service.base.storehouse.model.StorehouseUserBo;
import cn.farwalker.ravv.service.sys.user.model.SysUserBo;

/**
 * 仓库的操作用户<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IStorehouseUserDao extends BaseMapper<StorehouseUserBo>{
	
	/**
	 * 获取仓库管理员用户信息列表
	 * @param storeId 仓库id
	 * @return
	 */
	List<SysUserBo> sysUserListByStoreId(@Param("storeId") Long storeId);
}