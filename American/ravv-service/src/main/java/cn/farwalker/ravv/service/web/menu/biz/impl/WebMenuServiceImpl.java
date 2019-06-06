package cn.farwalker.ravv.service.web.menu.biz.impl;

import java.util.ArrayList;
import java.util.List;

import cn.farwalker.ravv.service.goods.base.model.GoodsListVo;
import cn.farwalker.ravv.service.web.menu.model.*;
import cn.farwalker.ravv.service.web.webmodel.biz.IWebModelBiz;
import cn.farwalker.ravv.service.web.webmodel.dao.IWebModelGoodsDao;
import cn.farwalker.ravv.service.web.webmodel.model.WebModelBo;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import com.baomidou.mybatisplus.mapper.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.farwalker.ravv.service.goods.base.biz.IGoodsBiz;
import cn.farwalker.ravv.service.goods.base.dao.IGoodsDao;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.web.menu.biz.IWebMenuBiz;
import cn.farwalker.ravv.service.web.menu.biz.IWebMenuGoodsBiz;
import cn.farwalker.ravv.service.web.menu.biz.IWebMenuService;
import cn.farwalker.waka.core.RavvExceptionEnum;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WebMenuServiceImpl implements IWebMenuService {

    @Autowired
    private IWebMenuBiz webMenuBiz;

    @Autowired
    private IWebMenuGoodsBiz webMenuGoodsBiz;

    @Autowired
    private IGoodsBiz goodsBiz;

    @Autowired
    private IGoodsDao goodsDao;

    @Autowired
	private IWebModelGoodsDao webModelGoodsDao;

	@Autowired
	private IWebModelBiz iWebModelBiz;

    /**
     * @description: 查出一级菜单
     * @param:
     * @return list
     * @author Mr.Simple
     * @date 2018/11/22 12:01
     */
    @Override
    public List<WebMenuBo> getFirstLevelMenu() {
        Wrapper<WebMenuBo> menuQuery = new EntityWrapper<>();
        menuQuery.isNull(WebMenuBo.Key.parentid.toString());
        menuQuery.eq(WebMenuBo.Key.visible.toString(),true);
        List<WebMenuBo> menuList = webMenuBiz.selectList(menuQuery);
        if(menuList.size() == 0)
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
		menuList.forEach(item->{
			item.setLogo(QiniuUtil.getFullPath(item.getLogo()));
			item.setPicture1(QiniuUtil.getFullPath(item.getPicture1()));
			item.setPicture2(QiniuUtil.getFullPath(item.getPicture2()));
		});
        return menuList;
    }

    /**
     * @description: 根据一级菜单id查出后续菜单
     * @param: menuId
     * @return List
     * @author Mr.Simple
     * @date 2018/11/22 12:00
     */
    @Override
    public List<WebMenuFrontVo> getMenuByPid(long menuId) {
        List<WebMenuFrontVo> menuFrontVoList = new ArrayList<>();
        Wrapper<WebMenuBo> menuQuery = new EntityWrapper<>();
        menuQuery.eq(WebMenuBo.Key.parentid.toString(),menuId);
        menuQuery.eq(WebMenuBo.Key.visible.toString(),true);
        List<WebMenuBo> secondMenuList = webMenuBiz.selectList(menuQuery);
        secondMenuList.forEach(item->{
			item.setPicture1(QiniuUtil.getFullPath(item.getPicture1()));
			item.setPicture2(QiniuUtil.getFullPath(item.getPicture2()));
		});
        //// TODO: 通过二级菜单id查询出下一级菜单信息
        for(WebMenuBo webMenuBo: secondMenuList){
            //因为pid不是主键，所以不能使用批量查询
            Wrapper<WebMenuBo> newQuery = new EntityWrapper<>();
            newQuery.eq(WebMenuBo.Key.parentid.toString(),webMenuBo.getId());
			menuQuery.eq(WebMenuBo.Key.visible.toString(),true);
            List<WebMenuBo> thirdList = webMenuBiz.selectList(newQuery);
            //更新图片路径
			thirdList.forEach(item->{
				item.setPicture1(QiniuUtil.getFullPath(item.getPicture1()));
				item.setPicture2(QiniuUtil.getFullPath(item.getPicture2()));
			});
            WebMenuFrontVo menuFrontVo = new WebMenuFrontVo();
            menuFrontVo.setSecondWebMenuBo(webMenuBo);
            menuFrontVo.setLowLevelMenu(thirdList);
            menuFrontVoList.add(menuFrontVo);
        }
        return menuFrontVoList;
    }

	/**
	 * @description: 找出当前菜单的所有子菜单
	 * @param: [menuId]
	 * @return  
	 * @author Lee 
	 * @date 2018/12/6 18:07 
	 */ 
	@Override
	public List<WebMenuBo> getAllSubMenus(long menuId) {
		List<WebMenuBo> allSubMenus = new ArrayList<>();
		recursive(menuId,allSubMenus);
		WebMenuBo self = webMenuBiz.selectById(menuId);
		allSubMenus.add(self);
		return allSubMenus;
	}


	private void recursive(long menuId, List<WebMenuBo> allSubMenus){
		List<WebMenuBo> nextMenus = webMenuBiz.selectList(Condition.create().eq(WebMenuBo.Key.parentid.toString(),menuId));
		if(nextMenus.size() == 0)
			return;
		allSubMenus.addAll(nextMenus);
		nextMenus.forEach(item->{
			recursive(item.getId(),allSubMenus);
		});
	}


    @Override
    public List<GoodsListVo> getGoodsByMenuId(long menuId, int currentPage, int pageSize) {
        Page<GoodsBo> page = new Page<>(currentPage, pageSize, GoodsBo.Key.gmtCreate.toString(),false);
        Wrapper<WebMenuGoodsBo> menuGoodsQuery = new EntityWrapper<>();
        menuGoodsQuery.eq(WebMenuGoodsBo.Key.menuId.toString(),menuId);
        //确保筛选出来的商品id唯一
        menuGoodsQuery.groupBy(WebMenuGoodsBo.Key.goodsId.toString());
        List<WebMenuGoodsBo> menuGoodsList = webMenuGoodsBiz.selectList(menuGoodsQuery);
        if(menuGoodsList.size() == 0)
        	throw new WakaException(RavvExceptionEnum.GOODS_NOT_FIND);
        List<Long> goodsIdList = new ArrayList<>();
        for(WebMenuGoodsBo menuGoodsBo: menuGoodsList){
            goodsIdList.add(menuGoodsBo.getGoodsId());
        }
        //此处应当联表查询
		List<GoodsListVo> menuGoodsFrontVoList = goodsDao.selectGoodsListByIdList(page,goodsIdList);
        if(menuGoodsFrontVoList.size() == 0)
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
		menuGoodsFrontVoList.forEach(item->{
			item.setImgUrl(QiniuUtil.getFullPath(item.getImgUrl()));
		});
        return menuGoodsFrontVoList;
    }

	@Override
	List<GoodsListVo> getModelGoodsByMenuId(long modelId, long menuId, int currentPage, int pageSize){
		WebModelBo query =  iWebModelBiz.selectById(modelId);
		if(query == null){
			throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
		}

		if()
		
	}


	@Override
	public List<AppMenuFrontVo> getMenuForApp() {
    	List<AppMenuFrontVo> appMenuList = new ArrayList<>();
		List<WebMenuBo> firstMenuList = getFirstLevelMenu();
		firstMenuList.forEach(item->{
			List<WebMenuFrontVo> lowLevel = new ArrayList<>();
			lowLevel = getMenuByPid(item.getId());
			AppMenuFrontVo appMenuFrontVo = new AppMenuFrontVo();
			appMenuFrontVo.setOtherLevelList(lowLevel);
			appMenuFrontVo.setFirstLevel(item);
			appMenuList.add(appMenuFrontVo);
		});
		return appMenuList;
	}

	@Override
	public Boolean insert(WebMenuBo entity) {
		setLevelPaths(entity);
		boolean leaf = isLeaf(entity.getId());
		entity.setLeaf(Boolean.valueOf(leaf));
		return webMenuBiz.insert(entity);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Boolean updateById(WebMenuBo entity) {
		setLevelPaths(entity);
		
		boolean leaf = isLeaf(entity.getId());
		entity.setLeaf(Boolean.valueOf(leaf));
		return webMenuBiz.updateById(entity);
	}

	/**设置等级路径*/
    private void setLevelPaths(WebMenuBo bo){
		Long parentId = bo.getParentid();
		String ids =String.valueOf(bo.getId());
		if(parentId == null || parentId.longValue()==0){
			bo.setPaths(ids);
			return ;
		}
		
		String paths = bo.getPaths();
		boolean macth = false;
		if(paths!=null){
			if(paths.indexOf('/')==0){
				 macth = paths.equals(ids);
			}
			else {
				macth = paths.endsWith(ids);
			}
		}
		if(!macth){
			StringBuilder ps = getLevelPaths(bo.getId());
			bo.setPaths(ps.toString());
		}
	}
    private boolean isLeaf(Long menuId){
    	return false;//TODO 没实现
    	/*Wrapper<T>
    	webMenuBiz.selectOne();*/
    }
	/**
	 * 递归查找分类路径
	 * @param lastCategoryId
	 * @return
	 */
	@Override
	public StringBuilder getLevelPaths(Long lastCategoryId){
		if(lastCategoryId == null || lastCategoryId.longValue()==0){
			return null;
		}
		
		WebMenuBo bo = webMenuBiz.selectById(lastCategoryId);
		if(bo==null){
			return null;
		}
		
		StringBuilder paths = getLevelPaths(bo.getParentid());
		if(paths == null){ //root
			paths = new StringBuilder();
			paths.append(lastCategoryId);
		}
		else{
			paths.append('/').append(lastCategoryId);
		}
		return paths;
	}



}
