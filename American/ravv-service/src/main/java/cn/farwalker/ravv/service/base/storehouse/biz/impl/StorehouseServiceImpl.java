package cn.farwalker.ravv.service.base.storehouse.biz.impl;
 

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.farwalker.ravv.service.base.storehouse.biz.IStorehouseService;
import cn.farwalker.ravv.service.base.storehouse.biz.IStorehouseUserBiz;
import cn.farwalker.ravv.service.base.storehouse.dao.IStorehouseUserDao;
import cn.farwalker.ravv.service.base.storehouse.model.StorehouseUserBo;
import cn.farwalker.ravv.service.sys.user.model.SysUserBo;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

@Service
public class StorehouseServiceImpl implements IStorehouseService{

	@Resource
	private IStorehouseUserBiz storeUserBiz;
	
	@Resource
	private IStorehouseUserDao storehouseUserDao;
	
	@Override
	public List<Long> getStoreIds(Long userId) {
		if(userId == null || userId.longValue()<=0){
			return  Collections.emptyList();
		}
		
		Wrapper<StorehouseUserBo> query =  new EntityWrapper<>();
		query.eq(StorehouseUserBo.Key.userId.toString(),userId);
		
		List<StorehouseUserBo> rds = storeUserBiz.selectList(query);
		List<Long> ids = new ArrayList<>(rds.size());
		for(StorehouseUserBo bo :rds){
			ids.add(bo.getStoreId());
		}
		return ids;
	}

	@Override
	public List<Long> getUserIds(Long storeId) {
		if(storeId == null || storeId.longValue()<=0){
			return  Collections.emptyList();
		}
		Wrapper<StorehouseUserBo> query =  new EntityWrapper<>();
		query.eq(StorehouseUserBo.Key.storeId.toString(),storeId);
		
		List<StorehouseUserBo> rds = storeUserBiz.selectList(query);
		List<Long> ids = new ArrayList<>(rds.size());
		for(StorehouseUserBo bo :rds){
			ids.add(bo.getUserId());
		}
		return ids;
	}

	@Override
	public List<SysUserBo> sysUserListByStoreId(Long storeId) {
		return storehouseUserDao.sysUserListByStoreId(storeId);
	}

}
