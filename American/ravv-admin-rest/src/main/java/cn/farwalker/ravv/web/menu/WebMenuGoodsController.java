package cn.farwalker.ravv.web.menu;

import java.util.List;

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

import cn.farwalker.ravv.service.web.menu.model.WebMenuGoodsBo;
import cn.farwalker.ravv.service.web.menu.model.WebMenuGoodsVo;
import cn.farwalker.waka.core.JsonResult;

import cn.farwalker.waka.util.Tools;

/**
 * 前端的分类标签<br/>
 * 分类标签商品 controller<br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
@RestController
@RequestMapping("/web/menu/goods")
public class WebMenuGoodsController{
	private final static Logger log = LoggerFactory.getLogger(WebMenuGoodsController.class);

	@Autowired
	private AdminWebService adminWebService;

	/**
	 * 创建记录
	 * 
	 * @param vo
	 *            null<br/>
	 */
	@RequestMapping("/create")
	public JsonResult<?> doCreate(@RequestBody WebMenuGoodsBo vo) {
		try{
			if (vo == null) {
				throw new WakaException("vo不能为空");
			}
			return JsonResult.newSuccess(adminWebService.createWebMenuGoods(vo));
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
			return JsonResult.newSuccess(adminWebService.deleteWebMenuGoods(id));
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
	public JsonResult<WebMenuGoodsVo> doGet(@RequestParam Long id) {
		try{
			if (id == null) {
				throw new WakaException("vo不能为空");
			}
			return JsonResult.newSuccess(adminWebService.getOneWebMenuGoods(id));
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
	public JsonResult<Page<WebMenuGoodsVo>> doList(@RequestBody List<QueryFilter> query, Integer start, Integer size,
			String sortfield) {
		try{
//			if (Tools.collection.isEmpty(query) || start < 0 || size < 0 || Tools.string.isEmpty(sortfield)) {
//				throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
//			}
			return JsonResult.newSuccess(adminWebService.getListWebMenuGoods(query, start, size, sortfield));
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
	 * 修改记录
	 * 
	 * @param vo
	 *            null<br/>
	 */
	@RequestMapping("/update")
	public JsonResult<?> doUpdate(@RequestBody WebMenuGoodsBo vo) {
		try{
			if (vo == null) {
				throw new WakaException("vo不能为空");
			}
			return JsonResult.newSuccess(adminWebService.updateWebMenuGoods(vo));
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
	 * 给对应的标签添加商品列表
	 * 
	 * @param menuId
	 *            null<br/>
	 */
	@RequestMapping("/addgoods")
	@Transactional
	public JsonResult<Boolean> addGoods(@RequestBody List<Long> goodsIdList, Long menuId) {
		try{
			if (menuId == null) {
				throw new WakaException("未指定标签");
			}
			if (Tools.collection.isEmpty(goodsIdList)) {
				throw new WakaException("未选择商品");
			}
			return JsonResult.newSuccess(adminWebService.addWebMenuGoods(goodsIdList, menuId));
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