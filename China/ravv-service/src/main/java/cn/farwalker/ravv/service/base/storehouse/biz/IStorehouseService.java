package cn.farwalker.ravv.service.base.storehouse.biz;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.farwalker.ravv.service.sys.user.model.SysUserBo;

/**
 * 仓库<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IStorehouseService  {
	/** 根据用户id取得可以操作的仓库id*/
	public List<Long> getStoreIds(Long userId);
	/** 根据仓库id取得可以操作的用户id*/
	public List<Long> getUserIds(Long storeId);
	
	/**
	 * 获取仓库管理员用户信息列表
	 * @param storeId 仓库id
	 * @return
	 */
	List<SysUserBo> sysUserListByStoreId(Long storeId);
}