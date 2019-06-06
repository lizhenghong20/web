package cn.farwalker.ravv.service.web.menu.biz;

import java.util.List;
import java.util.Map;

import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.goods.base.model.GoodsListVo;
import cn.farwalker.ravv.service.web.menu.model.AppMenuFrontVo;
import cn.farwalker.ravv.service.web.menu.model.WebMenuBo;
import cn.farwalker.ravv.service.web.menu.model.WebMenuFrontVo;
public interface IWebMenuService {
    public List<WebMenuBo> getFirstLevelMenu();

    public List<WebMenuFrontVo> getMenuByPid(long menuId);

	List<GoodsListVo> getGoodsByMenuId(long menuId, int currentPage, int pageSize);

	List<GoodsListVo> getModelGoodsByMenuId(long modelId, long menuId, int currentPage, int pageSize);

    public List<AppMenuFrontVo> getMenuForApp();
    
	/**与Biz的区别是，service的方法有逻辑处理*/
	public Boolean insert(WebMenuBo entity) ;

	/**与Biz的区别是，service的方法有逻辑处理*/
	public Boolean updateById(WebMenuBo entity) ;
	
	/**
	 * 递归查找分类路径
	 * @param lastCategoryId
	 * @return
	 */
	public StringBuilder getLevelPaths(Long lastCategoryId);

	/**
	 * 找出与其关联的所有子菜单（包括自己）
	 * @param menuId
	 * @return
     */
	public List<WebMenuBo> getAllSubMenus(long menuId);
}
