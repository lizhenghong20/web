package cn.farwalker.ravv.web.model;

import java.util.List;

import javax.annotation.Resource;

import cn.farwalker.ravv.admin.web.AdminWebService;
import cn.farwalker.waka.core.WakaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.web.crud.QueryFilter;

import cn.farwalker.ravv.service.web.menu.biz.IWebMenuGoodsBiz;
import cn.farwalker.ravv.service.web.webmodel.biz.IWebModelGoodsBiz;
import cn.farwalker.ravv.service.web.webmodel.biz.IWebModelGoodsService;
import cn.farwalker.ravv.service.web.webmodel.model.WebModelGoodsBo;
import cn.farwalker.ravv.service.web.webmodel.model.WebModelGoodsVo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.base.controller.ControllerUtils;
import cn.farwalker.waka.util.Tools;

/**
 * 模块商品<br/>
 * 模块商品 controller<br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
@RestController
@RequestMapping("/web/model/goods")
public class WebModelGoodsController{
	private final static Logger log = LoggerFactory.getLogger(WebModelGoodsController.class);
	@Resource
	private IWebModelGoodsBiz webModelGoodsBiz;

	@Resource
	private IWebMenuGoodsBiz webMenuGoodsBiz;

	@Resource
	private IWebModelGoodsService webModelGoodsService;

	protected IWebModelGoodsBiz getWebModelGoodsBiz() {
		return webModelGoodsBiz;
	}

	@Autowired
	private AdminWebService adminWebService;

	/**
	 * 创建记录
	 * 
	 * @param vo
	 *            null<br/>
	 */
	@RequestMapping("/create")
	public JsonResult<?> doCreate(@RequestBody WebModelGoodsBo vo) {
		try{
			if (vo == null) {
				throw new WakaException("vo不能为空");
			}
			return JsonResult.newSuccess(adminWebService.createWebModelGoods(vo));
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
	 * 删除记录
	 * 
	 * @param id
	 *            null<br/>
	 */
	@RequestMapping("/delete")
	public JsonResult<Boolean> doDelete(@RequestParam Long id) {
		try{
			if (id == null) {
				throw new WakaException("id不能为空");
			}
			return JsonResult.newSuccess(adminWebService.deleteWebModelGoods(id));
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
	 * 取得单条记录
	 * 
	 * @param id
	 *            null<br/>
	 */
	@RequestMapping("/get")
	public JsonResult<WebModelGoodsVo> doGet(@RequestParam Long id) {
		try{
			if (id == null) {
				throw new WakaException("vo不能为空");
			}
			return JsonResult.newSuccess(adminWebService.getOneWebModelGoods(id));
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
	 * 列表记录
	 * 
	 * @param query
	 *            查询条件<br/>
	 * @param start
	 *            开始行号<br/>
	 * @param size
	 *            记录数<br/>
	 * @param sortfield
	 *            排序(+字段1,-字段名2)<br/>
	 */
	@RequestMapping("/list")
	public JsonResult<Page<WebModelGoodsVo>> doList(@RequestBody List<QueryFilter> query, Integer start, Integer size,
			String sortfield) {
		try{
//			if (Tools.collection.isEmpty(query) || start < 0 || size < 0 || Tools.string.isEmpty(sortfield)) {
//				throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
//			}
			return JsonResult.newSuccess(adminWebService.getListWebModelGoods(query, start, size, sortfield));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}

	}

	@RequestMapping("/serch_model_goods")
	public JsonResult<Page<WebModelGoodsVo>> searchModelGoods(String goodsName, String modelCode, Integer start,
			Integer size, String sortfield) {
		// createMethodSinge创建方法
		Page<WebModelGoodsVo> rs = ControllerUtils.getPage(start, size, sortfield);
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 修改记录
	 * 
	 * @param vo
	 *            null<br/>
	 */
	@RequestMapping("/update")
	public JsonResult<?> doUpdate(@RequestBody WebModelGoodsBo vo) {
		try{
			if (vo == null) {
				throw new WakaException("vo不能为空");
			}
			return JsonResult.newSuccess(adminWebService.updateWebModelGoods(vo));
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
	 * 给对应的模块添加商品列表
	 * 
	 * @param goodsIdList
	 *            null<br/>
	 */
	@RequestMapping("/addgoods")
	@Transactional
	public JsonResult<?> addGoods(@RequestBody List<Long> goodsIdList, String modelCode) {
		try{
			if (Tools.string.isEmpty(modelCode)) {
				throw new WakaException("未指定模块");
			}
			if (Tools.collection.isEmpty(goodsIdList)) {
				throw new WakaException("未选择商品");
			}
			return JsonResult.newSuccess(adminWebService.addWebModelGoods(goodsIdList, modelCode));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}

	}
}