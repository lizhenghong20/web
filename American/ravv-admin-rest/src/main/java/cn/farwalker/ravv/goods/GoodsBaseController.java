package cn.farwalker.ravv.goods;

import java.util.List;

import javax.annotation.Resource;

import cn.farwalker.ravv.admin.goods.AdminGoodsService;
import cn.farwalker.waka.core.WakaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.web.crud.QueryFilter;

import cn.farwalker.ravv.category.CategoryTreeController;
import cn.farwalker.ravv.admin.goods.dto.GoodsStoreVo;
import cn.farwalker.ravv.service.base.brand.model.BaseBrandBo;
import cn.farwalker.ravv.service.base.storehouse.model.StorehouseBo;
import cn.farwalker.ravv.service.category.basecategory.model.BaseCategoryBo;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.goods.base.model.GoodsVo;
import cn.farwalker.ravv.service.goods.constants.GoodsStatusEnum;
import cn.farwalker.ravv.service.merchant.model.MerchantBo;
//import cn.farwalker.waka.common.exception.BizExceptionEnum;
import cn.farwalker.waka.core.JsonResult;


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

	@Resource
	private CategoryTreeController categoryTreeCtl;

	@Autowired
	private AdminGoodsService adminGoodsService;

	/**
	 * 商品基础信息详情
	 * 
	 * @param id
	 * @param id
	 *            id<br/>
	 */
	@RequestMapping("/get")
	public JsonResult<GoodsVo> get(Long id) {
		try{
			// createMethodSinge创建方法
			if (id == null) {
				throw new WakaException("vo不能为空");
			}
			return JsonResult.newSuccess(adminGoodsService.get(id));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}

	}

	/**
	 * 获取商品基础信息列表
	 * 
	 * @param query
	 *            查询条件<br/>
	 * @param start
	 *            开始行号<br/>
	 * @param size
	 *            记录数<br/>
	 * @param sortfield
	 *            排序+/-字段<br/>
	 */
	@RequestMapping("/list")
	public JsonResult<Page<GoodsStoreVo>> doList(@RequestBody List<QueryFilter> query, Integer start, Integer size,
			String sortfield) {
		try{
			// createMethodSinge创建方法
//			if (Tools.collection.isEmpty(query) || start < 0 || size < 0 || Tools.string.isEmpty(sortfield)) {
//				throw new WakaException("vo不能为空");
//			}
			return JsonResult.newSuccess(adminGoodsService.getList(query, start, size, sortfield));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}
	}

	/**
	 * null<br/>
	 * 
	 * @param vo
	 *            记录Vo<br/>
	 */
	@RequestMapping("/create")
	public JsonResult<GoodsBo> doCreate(@RequestBody GoodsVo vo) {
		try{
			// createMethodSinge创建方法
			if (vo == null) {
				throw new WakaException("vo不能为空");
			}
			return JsonResult.newSuccess(adminGoodsService.create(vo));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}

	}

	/**
	 * 
	 * @param vo
	 *            记录<br/>
	 */
	@RequestMapping("/update")
	public JsonResult<GoodsBo> doUpdate(@RequestBody GoodsVo vo) {
		try{
			// createMethodSinge创建方法
			if (vo == null) {
				throw new WakaException("vo不能为空");
			}
			return JsonResult.newSuccess(adminGoodsService.update(vo));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}
	}

	/**
	 * 取分类树
	 * 
	 * @param parentid
	 *            null<br/>
	 */
	@RequestMapping("/categorytree")
	public JsonResult<List<BaseCategoryBo>> getCategoryTree(Long parentid) {
		try{
			// createMethodSinge创建方法
//			if (parentid == null) {
//				throw new WakaException("vo不能为空");
//			}
			return categoryTreeCtl.getChildren(parentid);
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}

	}

	/**
	 * 取得所有品牌
	 */
	@RequestMapping("/brandlist")
	public JsonResult<List<BaseBrandBo>> getBrandList(Long parentid) {
		try{
			// createMethodSinge创建方法
//			if (parentid == null) {
//				throw new WakaException("vo不能为空");
//			}
			return JsonResult.newSuccess(adminGoodsService.getBrandList(parentid));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}

	}

	/**
	 * @param memberId
	 *            会员id<br/>
	 * @param goodsId
	 *            商品id<br/>
	 */
	@RequestMapping("/viewlog")
	public JsonResult<Integer> viewLog(Long memberId, Long goodsId) {
		try{
			// createMethodSinge创建方法
			Integer rs = null;
			return JsonResult.newSuccess(rs);
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}

	}

	/**
	 * @param status
	 *            会员id<br/>
	 * @param goodsIds
	 *            商品id<br/>
	 */
	@RequestMapping("/status")
	public JsonResult<List<GoodsBo>> doStatus(@RequestBody List<Long> goodsIds, GoodsStatusEnum status) {
		try {
			return JsonResult.newSuccess(adminGoodsService.doStatus(goodsIds, status));
		} catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
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
		try {
//			if(Tools.collection.isEmpty(query) || start < 0 || size < 0
//					|| Tools.string.isEmpty(sortfield) || menuId == null){
//				throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
//			}
			return JsonResult.newSuccess(adminGoodsService.getNotInMenuGoods(query, start, size, sortfield, menuId));

		} catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());
		}

	}

	/**
	 * 获取对应模块未添加的商品 TODO 去除分配标签的商品
	 */
	@RequestMapping("/notinmodelgoods")
	public JsonResult<Page<GoodsStoreVo>> getNotInModelGoods(@RequestBody List<QueryFilter> query, Integer start,
			Integer size, String sortfield, String modelCode) {
		try {
//			if(Tools.collection.isEmpty(query) || start < 0 || size < 0
//					|| Tools.string.isEmpty(sortfield) || Tools.string.isEmpty(modelCode)){
//				throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
//			}
			return JsonResult.newSuccess(adminGoodsService.getNotInModelGoods(query, start, size, sortfield, modelCode));

		} catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());
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
		try {
			if (id == null) {
				return JsonResult.newFail("id不能为空");
			}
			return JsonResult.newSuccess(adminGoodsService.delete(id));
		} catch (WakaException e) {
			log.error("删除记录", e);
			return JsonResult.newFail(e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());
		}
	}

	/** 取得仓库列表 */
	@RequestMapping("/storelist")
	public JsonResult<List<StorehouseBo>> getStoreList() {
		try {
			return JsonResult.newSuccess(adminGoodsService.getStoreList());
		} catch (WakaException e) {
			log.error("删除记录", e);
			return JsonResult.newFail(e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());
		}

	}
	
	/** 取得供应商列表 */
	@RequestMapping("/findMerchant")
	public JsonResult<List<MerchantBo>> findMerchant(String search) {
		try {
			return JsonResult.newSuccess(adminGoodsService.findMerchant(search));
		} catch (WakaException e) {
			log.error("删除记录", e);
			return JsonResult.newFail(e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());
		}

	}
	
	/**
	 * 获取供应商商品基础信息列表
	 * 
	 * @param query 查询条件<br/>
	 * @param start 开始行号<br/>
	 * @param size 记录数<br/>
	 * @param sortfield 排序+/-字段<br/>
	 */
	@RequestMapping("/merchant_goods")
	public JsonResult<Page<GoodsStoreVo>> getMerchantGoods(Long merchantId, Boolean isAlarm,
							 @RequestBody List<QueryFilter> query, Integer start, Integer size, String sortfield) {
		try {
			if (merchantId == null) {
				throw new WakaException("供应商id不能为空");
			}

			return JsonResult.newSuccess(adminGoodsService.getMerchantGoods(merchantId,
					isAlarm, query, start, size, sortfield));

		} catch (WakaException e) {
			log.error("删除记录", e);
			return JsonResult.newFail(e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());
		}

	}
}