package cn.farwalker.ravv.goods;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import cn.farwalker.waka.core.WakaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.orm.core.annotation.LoadJoinValueImpl;
import com.cangwu.frame.web.crud.QueryFilter;

import cn.farwalker.ravv.category.CategoryTreeController;
import cn.farwalker.ravv.goods.dto.GoodsStoreVo;
import cn.farwalker.ravv.service.base.brand.biz.IBaseBrandBiz;
import cn.farwalker.ravv.service.base.brand.model.BaseBrandBo;
import cn.farwalker.ravv.service.base.storehouse.biz.IStorehouseBiz;
import cn.farwalker.ravv.service.base.storehouse.model.StorehouseBo;
import cn.farwalker.ravv.service.category.basecategory.biz.IBaseCategoryBiz;
import cn.farwalker.ravv.service.category.basecategory.model.BaseCategoryBo;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsBiz;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsService;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.goods.base.model.GoodsVo;
import cn.farwalker.ravv.service.goods.constants.GoodsStatusEnum;
import cn.farwalker.ravv.service.goods.image.biz.IGoodsImageBiz;
import cn.farwalker.ravv.service.goods.image.model.GoodsImageBo;
import cn.farwalker.ravv.service.goods.utils.GoodsUtil;
import cn.farwalker.ravv.service.goodsext.viewlog.biz.IGoodsViewLogBiz;
import cn.farwalker.ravv.service.goodssku.skudef.biz.IGoodsSkuDefBiz;
import cn.farwalker.ravv.service.merchant.biz.IMerchantBiz;
import cn.farwalker.ravv.service.merchant.model.MerchantBo;
import cn.farwalker.ravv.service.web.menu.biz.IWebMenuGoodsBiz;
import cn.farwalker.ravv.service.web.webmodel.biz.IWebModelGoodsBiz;
import cn.farwalker.waka.config.properties.WakaProperties;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.base.controller.ControllerUtils;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.util.Tools;

/**
 * 商品基本信息管理<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
@RestController
@RequestMapping("/goods/goodsbase")
public class GoodsBaseController{
	private final static Logger log = LoggerFactory.getLogger(GoodsBaseController.class);

	@Autowired
	private IGoodsSkuDefBiz goodsSkuDefService;

	@Autowired
	private IGoodsImageBiz goodsImageService;

	@Autowired
	private IGoodsViewLogBiz goodsViewLogService;

	@Resource
	private IBaseBrandBiz brandBiz;

	@Resource
	private IBaseCategoryBiz categoryBiz;

	@Resource
	private CategoryTreeController categoryTreeCtl;

	@Resource
	private WakaProperties wakaProperties;
	@Resource
	private IGoodsBiz goodsBiz;
	@Resource
	private IGoodsService goodsService;

	@Resource
	private IWebMenuGoodsBiz webMenuGoodsBiz;

	@Resource
	private IStorehouseBiz storehouseBiz;
	@Resource
	private IMerchantBiz merchantBiz;

	
	@Resource
	private IWebModelGoodsBiz webModelGoodsBiz;

	protected IGoodsBiz getBiz() {
		return goodsBiz;
	}

	/**
	 * 商品基础信息详情
	 * 
	 * @param goodsId
	 * @param id
	 *            id<br/>
	 */
	@RequestMapping("/get")
	public JsonResult<GoodsVo> get(Long id) {
		final Long goodsId = id;
		GoodsBo goods = goodsBiz.selectById(goodsId);

		if (null == goods) {
			throw new WakaException(RavvExceptionEnum.GOODS_NOT_FIND);
		}
		GoodsVo goodsVO = Tools.bean.cloneBean(goods, new GoodsVo());
		LoadJoinValueImpl.load(goodsBiz, goodsVO);
		List<GoodsImageBo> images = goodsVO.getImages();
		if (images != null) {
			for (GoodsImageBo img : images) {
				String fp = GoodsUtil.getCdnFullPaths(img.getImgUrl());
				img.setImgUrl(fp);
			}
		}

		return JsonResult.newSuccess(goodsVO);
	}

	/**
	 * 获取商品基础信息列表
	 * 
	 * @param query 查询条件<br/>
	 * @param start 开始行号<br/>
	 * @param size 记录数<br/>
	 * @param sortfield 排序+/-字段<br/>
	 */
	@RequestMapping("/list")
	public JsonResult<Page<GoodsStoreVo>> doList(Long userId, @RequestBody List<QueryFilter> query, Integer start, Integer size,
			String sortfield) {
		long t1 = System.currentTimeMillis(), t2 = 0, t3 = 0;
		try {
			if (userId == null) {
				return JsonResult.newFail("当前用户id不能为空");
			}
			
			Page<GoodsBo> page = ControllerUtils.getPage(start, size, sortfield);
			t2 = System.currentTimeMillis();
			Wrapper<GoodsBo> wrap = ControllerUtils.getWrapper(query);
			t3 = System.currentTimeMillis();
			
			wrap.eq(GoodsBo.Key.merchantId.toString(), userId);
			
			Page<GoodsBo> rds = getBiz().selectPage(page, wrap);
			Page<GoodsStoreVo> rs = ControllerUtils.convertPageRecord(rds, GoodsStoreVo.class);
			LoadJoinValueImpl.load(goodsBiz, rs.getRecords());

			return JsonResult.newSuccess(rs);
		} finally {
			long t4 = System.currentTimeMillis();
			log.debug(String.format("time %d,%d,%d", t2 - t1, t3 - t2, t4 - t3));
		}
	}
	
	/**
	 * 获取预警商品基础信息列表
	 * 
	 * @param query 查询条件<br/>
	 * @param start 开始行号<br/>
	 * @param size 记录数<br/>
	 * @param sortfield 排序+/-字段<br/>
	 */
	@RequestMapping("/alarm_stocklist")
	public JsonResult<Page<GoodsStoreVo>> getAlarmStockList(Long userId, @RequestBody List<QueryFilter> query, Integer start, Integer size,
			String sortfield) {
		long t1 = System.currentTimeMillis(), t2 = 0, t3 = 0;
		try {
			if (userId == null) {
				return JsonResult.newFail("当前用户id不能为空");
			}
			
			Page<GoodsBo> page = ControllerUtils.getPage(start, size, sortfield);
			t2 = System.currentTimeMillis();
			Wrapper<GoodsBo> wrap = ControllerUtils.getWrapper(query);
			t3 = System.currentTimeMillis();
			
			wrap.eq(GoodsBo.Key.merchantId.toString(), userId);
			wrap.where("id in (SELECT goods_id FROM goods_inventory WHERE sale_stock_num < alarm_stock_num)");
			
			Page<GoodsBo> rds = getBiz().selectPage(page, wrap);
			Page<GoodsStoreVo> rs = ControllerUtils.convertPageRecord(rds, GoodsStoreVo.class);
			LoadJoinValueImpl.load(goodsBiz, rs.getRecords());

			return JsonResult.newSuccess(rs);
		} finally {
			long t4 = System.currentTimeMillis();
			log.debug(String.format("time %d,%d,%d", t2 - t1, t3 - t2, t4 - t3));
		}
	}

	/**
	 * 
	 * @param vo 记录Vo<br/>
	 */
	@RequestMapping("/create")
	public JsonResult<GoodsBo> doCreate(@RequestBody GoodsVo vo) {
		if (vo == null) {
			return JsonResult.newFail("记录Vo不能为空");
		}

		if (!isLeaf(vo.getLeafCategoryId())) {
			return JsonResult.newFail("分类需要选择最末节点");
		}

		// createMethodSinge创建方法
		GoodsBo bo = Tools.bean.cloneBean(vo, new GoodsBo());
		List<String> imageTitles = convertList(vo.getImageTitles());
		List<String> imageDetails = convertList(vo.getImageDetails());
		String videoTitle = vo.getVideoTitle();
		String videoDetail = vo.getVideoDetail();

		goodsService.insert(bo, imageTitles, vo.getImageMajor(), imageDetails, videoTitle, videoDetail);

		return JsonResult.newSuccess(bo);
	}

	/**
	 * 判断分类节点是否末及
	 * 
	 * @param categoryId
	 * @return
	 */
	private boolean isLeaf(Long categoryId) {
		if (categoryId == null || categoryId.longValue() <= 0) {
			return false;
		}
		Wrapper<BaseCategoryBo> query = new EntityWrapper<>();
		query.eq(BaseCategoryBo.Key.pid.toString(), categoryId);
		query.last("limit 1");
		BaseCategoryBo children = categoryBiz.selectOne(query);
		return (children == null);
	}

	/**
	 * 
	 * @param vo 记录<br/>
	 */
	@RequestMapping("/update")
	public JsonResult<GoodsBo> doUpdate(@RequestBody GoodsVo vo) {
		if (vo == null) {
			return JsonResult.newFail("记录不能为空");
		}
		if (!isLeaf(vo.getLeafCategoryId())) {
			return JsonResult.newFail("分类需要选择最末节点");
		}

		GoodsBo bo = Tools.bean.cloneBean(vo, new GoodsBo());
		List<String> imageTitles = convertList(vo.getImageTitles());
		List<String> imageDetails = convertList(vo.getImageDetails());
		String videoTitle = vo.getVideoTitle();
		String videoDetail = vo.getVideoDetail();
		goodsService.update(bo, imageTitles, vo.getImageMajor(), imageDetails, videoTitle, videoDetail);

		return JsonResult.newSuccess(bo);
	}

	private List<String> convertList(String imgs) {
		if (imgs == null) {
			return null;
		} else if (imgs.trim().length() == 0) {
			return Collections.emptyList();
		} else {
			String[] arys = imgs.trim().split(",");
			return Arrays.asList(arys);
		}
	}

	/**
	 * 取分类树
	 * 
	 * @param parentid
	 */
	@RequestMapping("/categorytree")
	public JsonResult<List<BaseCategoryBo>> getCategoryTree(Long parentid) {
		return categoryTreeCtl.getChildren(parentid);
	}

	/**
	 * 取得所有品牌
	 */
	@RequestMapping("/brandlist")
	public JsonResult<List<BaseBrandBo>> getBrandList(Long parentid) {
		Wrapper<BaseBrandBo> w = new EntityWrapper<>();
		w.last("limit 100");// 超过100个记录，可以使用动态加载
		List<BaseBrandBo> rds = brandBiz.selectList(w);
		return JsonResult.newSuccess(rds);
	}

	/**
	 * @param memberId 会员id<br/>
	 * @param goodsId 商品id<br/>
	 */
	@RequestMapping("/viewlog")
	public JsonResult<Integer> viewLog(Long memberId, Long goodsId) {
		// createMethodSinge创建方法
		Integer rs = null;
		return JsonResult.newSuccess(rs);
	}

	/**
	 * @param memberId 会员id<br/>
	 * @param goodsId 商品id<br/>
	 */
	@RequestMapping("/status")
	public JsonResult<List<GoodsBo>> doStatus(@RequestBody List<Long> goodsIds, GoodsStatusEnum status) {
		try {
			List<GoodsBo> st = goodsService.updateStatusEnum(goodsIds, status);
			return JsonResult.newSuccess(st);
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());
		}
	}

	/**
	 * 获取对应标签未添加的商品
	 * 
	 * @param query
	 *            null<br/>
	 * @param start
	 *            null<br/>
	 * @param size
	 *            null<br/>
	 * @param sortfield
	 *            null<br/>
	 * @param menuId
	 *            null<br/>
	 */
	@RequestMapping("/notinmenugoods")
	public JsonResult<Page<GoodsStoreVo>> getNotInMenuGoods(@RequestBody List<QueryFilter> query, Integer start,
			Integer size, String sortfield, Long menuId) {
		long t1 = System.currentTimeMillis(), t2 = 0, t3 = 0;
		try {
			t2 = System.currentTimeMillis();
			Page<GoodsBo> page = ControllerUtils.getPage(start, size, sortfield);
			Wrapper<GoodsBo> wrap = ControllerUtils.getWrapper(query);
			wrap.eq(GoodsBo.Key.goodsStatus.toString(), GoodsStatusEnum.ONLINE.getKey());
			Page<GoodsBo> goodsPage = goodsService.getGoodsNotInMenugoods(page, wrap, menuId);

			// 联表获取对应id的名称
			Page<GoodsStoreVo> rs = ControllerUtils.convertPageRecord(goodsPage, GoodsStoreVo.class);
			List<GoodsStoreVo> goodsVoList = rs.getRecords();
			if (Tools.collection.isNotEmpty(goodsVoList)) {
				LoadJoinValueImpl.load(getBiz(), goodsVoList);
			}
			t3 = System.currentTimeMillis();
			return JsonResult.newSuccess(rs);
		} finally {
			long t4 = System.currentTimeMillis();
			log.debug(String.format("time %d,%d,%d", t2 - t1, t3 - t2, t4 - t3));
		}
	}

	/**
	 * 获取对应模块未添加的商品 TODO 去除分配标签的商品
	 */
	@RequestMapping("/notinmodelgoods")
	public JsonResult<Page<GoodsStoreVo>> getNotInModelGoods(@RequestBody List<QueryFilter> query, Integer start,
			Integer size, String sortfield, String modelCode) {
		long t1 = System.currentTimeMillis(), t2 = 0, t3 = 0;
		try {
			t2 = System.currentTimeMillis();
			Page<GoodsBo> page = ControllerUtils.getPage(start, size, sortfield);
			Wrapper<GoodsBo> wrap = ControllerUtils.getWrapper(query);
			wrap.eq(GoodsBo.Key.goodsStatus.toString(), GoodsStatusEnum.ONLINE.getKey());
			Page<GoodsBo> goodsPage = goodsService.getGoodsNotInModelgoods(page, wrap, modelCode);

			// 联表获取对应id的名称
			Page<GoodsStoreVo> rs = ControllerUtils.convertPageRecord(goodsPage, GoodsStoreVo.class);
			List<GoodsStoreVo> goodsVoList = rs.getRecords();
			if (Tools.collection.isNotEmpty(goodsVoList)) {
				LoadJoinValueImpl.load(getBiz(), goodsVoList);
			}
			t3 = System.currentTimeMillis();
			return JsonResult.newSuccess(rs);
		} finally {
			long t4 = System.currentTimeMillis();
			log.debug(String.format("time %d,%d,%d", t2 - t1, t3 - t2, t4 - t3));
		}
	}

	/**
	 * 删除记录
	 * 
	 * @param id
	 *            null<br/>
	 */
	@RequestMapping("/delete")
	public JsonResult<Boolean> doDelete(@RequestParam Long id) {
		// createMethodSinge创建方法
		if (id == null) {
			return JsonResult.newFail("id不能为空");
		}
		try {
			Integer rs = goodsService.deleteGoodsById(id);
			return JsonResult.newSuccess(Boolean.TRUE);
		} catch (WakaException e) {
			log.error("删除记录", e);
			return JsonResult.newFail(e.getMessage());
		}
	}

	/** 取得仓库列表 */
	@RequestMapping("/storelist")
	public JsonResult<List<StorehouseBo>> getStoreList() {
		// createMethodSinge创建方法
		Wrapper<StorehouseBo> wp = new EntityWrapper<>();
		wp.eq(StorehouseBo.Key.status.toString(), Boolean.TRUE);
		List<StorehouseBo> rs = storehouseBiz.selectList(wp);
		return JsonResult.newSuccess(rs);
	}
	
	/** 取得供应商列表 */
	@RequestMapping("/findMerchant")
	public JsonResult<List<MerchantBo>> findMerchant(String search) {
		// createMethodSinge创建方法
		Wrapper<MerchantBo> wp = new EntityWrapper<>();
		wp.like(MerchantBo.Key.name.toString(), search);
		wp.last("limit 20");
		List<MerchantBo> rs = merchantBiz.selectList(wp);
		return JsonResult.newSuccess(rs);
	}
}