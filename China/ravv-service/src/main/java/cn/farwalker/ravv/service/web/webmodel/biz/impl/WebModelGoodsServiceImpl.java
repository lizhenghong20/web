package cn.farwalker.ravv.service.web.webmodel.biz.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import cn.farwalker.waka.core.WakaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;

import cn.farwalker.ravv.service.goods.base.dao.IGoodsDao;
import cn.farwalker.ravv.service.goods.base.model.GoodsListVo;
import cn.farwalker.ravv.service.web.menu.biz.IWebMenuBiz;
import cn.farwalker.ravv.service.web.menu.biz.IWebMenuService;
import cn.farwalker.ravv.service.web.menu.model.WebMenuBo;
import cn.farwalker.ravv.service.web.menu.model.WebMenuFrontVo;
import cn.farwalker.ravv.service.web.webmodel.biz.IWebModelGoodsBiz;
import cn.farwalker.ravv.service.web.webmodel.biz.IWebModelGoodsService;
import cn.farwalker.ravv.service.web.webmodel.dao.IWebModelGoodsDao;
import cn.farwalker.ravv.service.web.webmodel.model.WebModelGoodsBo;
import cn.farwalker.ravv.service.web.webmodel.model.WebModelGoodsVo;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;

/**
 * Created by asus on 2018/11/21.
 */
@Service
public class WebModelGoodsServiceImpl implements IWebModelGoodsService {
	@Autowired
	IWebModelGoodsBiz iWebModelGoodsBiz;

	@Autowired
	IWebModelGoodsDao iWebModelGoodsDao;

	@Autowired
	IGoodsDao iGoodsDao;

	@Autowired
	IWebMenuService iWebMenuService;

	@Autowired
	IWebMenuBiz iWebMenuBiz;

	@Override
	public List<WebMenuFrontVo> getWebModel(String modelCode, String showType, Integer menuSize, Integer itemSize) {
		List<GoodsListVo> goodsList = iWebModelGoodsDao.selectHomePage(modelCode, showType);
		if (goodsList == null) {
			throw new WakaException(RavvExceptionEnum.GOODS_NOT_FIND);
		}
		goodsList.forEach(item -> {
			item.setImgUrl(QiniuUtil.getFullPath(item.getImgUrl()));
			WebMenuBo currentMenu = iWebMenuBiz.selectById(item.getMenuId());
			String paths = currentMenu.getPaths();
			if (Tools.string.isEmpty(paths)) {
				if (currentMenu.getParentid() == null)
					paths = currentMenu.getId().toString();
				else
					throw new WakaException("获取菜单路径错误");
			}
			String[] ids = paths.split("/");
			Long firstMenuId = new Long(ids[0]);
			WebMenuBo firstMenu = iWebMenuBiz.selectById(firstMenuId);
			item.setMenuId(firstMenu.getId());
			item.setPicture1(firstMenu.getPicture1());
			item.setPicture2(firstMenu.getPicture2());
			item.setMenuPath(firstMenu.getPaths());
			item.setTitle(firstMenu.getTitle());
		});

		List<WebMenuFrontVo> menuList = new ArrayList<>();
		Long preMenuId = goodsList.get(0).getMenuId();
		List<GoodsListVo> newList = new ArrayList<>();
		// 几个菜单
		int menuSizeCount = menuSize;
		// 每个菜单中有几个商品
		int itemSizeCount = itemSize;

		for (GoodsListVo item : goodsList) {
			if (menuSizeCount <= 0)
				break;
			if (!item.getMenuId().equals(preMenuId)) {
				// 进出新的菜单商品
				menuSizeCount--;
				itemSizeCount = itemSize;
				// 将之前的数据存起来
				WebMenuBo webMenuBo = iWebMenuBiz.selectById(preMenuId);
				WebMenuFrontVo menu = new WebMenuFrontVo();
				Tools.bean.copyProperties(webMenuBo, menu);
				menu.setGoodsListVos(newList);
				menuList.add(menu);
				// 添加新数据,并更新状态
				newList = new ArrayList<>();
				newList.add(item);
				itemSizeCount--;
				preMenuId = item.getMenuId();
			} else {
				// menuId相等的情况
				if (itemSizeCount > 0)
					newList.add(item);
				itemSizeCount--;
			}
		}

		WebMenuBo webMenuBo = iWebMenuBiz.selectById(preMenuId);
		WebMenuFrontVo menu = new WebMenuFrontVo();
		Tools.bean.copyProperties(webMenuBo, menu);
		menu.setGoodsListVos(newList);
		menuList.add(menu);
		// 存入之前的数据

		menuList.forEach(item -> {
			item.setPicture1(QiniuUtil.getFullPath(item.getPicture1()));
			item.setPicture2(QiniuUtil.getFullPath(item.getPicture2()));
		});
		return menuList;
	}

	@Override
	public List<GoodsListVo> getNewArrivalsGoods(String modelCode, int currentPage, int pageSize, long menuId) {
		List<WebMenuBo> allSubMenus = iWebMenuService.getAllSubMenus(menuId);
		if (allSubMenus == null) {
			throw new WakaException(RavvExceptionEnum.GOODS_NOT_FIND);
		}
		Page<WebModelGoodsBo> page = new Page<>(currentPage, pageSize);
		page.setOrderByField(WebModelGoodsBo.Key.sequence.toString());
		page.setDescs(Arrays.asList(WebModelGoodsBo.Key.sequence.toString()));
		List<Long> goodsIdList = iWebModelGoodsDao.selectGoodsIdListByMenuId(page, modelCode,
				allSubMenus.stream().map(s -> s.getId()).collect(Collectors.toList()));
		if (goodsIdList.size() == 0)
			throw new WakaException(RavvExceptionEnum.GOODS_NOT_FIND);
		// 查出商品
		List<GoodsListVo> resultList = iGoodsDao.selectGoodsListByIdList(page, goodsIdList);
		resultList.forEach(item -> {
			item.setImgUrl(QiniuUtil.getFullPath(item.getImgUrl()));
		});
		// 更新最低价
		return resultList;

	}

	@Override
	public void searchModelGoods(Page<WebModelGoodsVo> page, String goodsName, String modelCode, String showType,
			Integer start, Integer size) {
		page.setRecords(iWebModelGoodsDao.searchModelGoods(goodsName, modelCode, showType, start, size));
		page.setTotal(iWebModelGoodsDao.countModelGoods(goodsName, modelCode, showType));
	}

}
