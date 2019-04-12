package cn.farwalker.ravv.sys.param;

import java.util.List;

import javax.annotation.Resource;

import cn.farwalker.ravv.admin.sys.AdminSysService;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.util.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.web.crud.QueryFilter;

import cn.farwalker.ravv.service.goods.base.biz.IGoodsBiz;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsService;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.shipment.biz.IShipmentBiz;
import cn.farwalker.ravv.service.shipment.constants.ShipmentTypeEnum;
import cn.farwalker.ravv.service.shipment.model.ShipmentBo;
import cn.farwalker.ravv.service.shipment.model.ShipmentVo;
import cn.farwalker.waka.core.JsonResult;

/**
 * 系统参数<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */

@RestController
@RequestMapping("/sys/ship")
public class ShipController{

	private final static Logger log = LoggerFactory.getLogger(ShipController.class);
	@Resource
	private IShipmentBiz getshipBiz;

	protected IShipmentBiz getshipBiz() {
		return getshipBiz;
	}

	@Resource
	private IGoodsBiz goodsBiz;

	protected IGoodsBiz getBiz() {
		return goodsBiz;
	}

	@Resource
	private IGoodsService goodsService;

	@Autowired
	private AdminSysService adminSysService;

	/**
	 * 创建记录
	 * 
	 * @param vo
	 *            null<br/>
	 */
	@RequestMapping("/create")
	public JsonResult<Boolean> doCreate(@RequestBody ShipmentBo vo) {
		try{
			if (vo == null) {
				throw new WakaException("vo不能为空");
			}
			if (null == vo.getShipmentType() || Tools.string.isEmpty(vo.getShipmentType().getKey())) {
				throw new WakaException("运费类型不能为空");
			}
			if (vo.getShipmentType() == ShipmentTypeEnum.Category) {
				if (null == vo.getCategoryPaths()) {
					throw new WakaException("商品分类不能为空");
				}
			}
			if (vo.getShipmentType() == ShipmentTypeEnum.Goods) {
				if (null == vo.getGoodsId()) {
					throw new WakaException("商品id不能为空");
				}
			}
			return JsonResult.newSuccess(adminSysService.createShip(vo));
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
	public JsonResult<Boolean> doDelete(Long id) {
		try{
			if (id == null) {
				throw new WakaException("id不能为空");
			}

			return JsonResult.newSuccess(adminSysService.deleteShip(id));
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
	public JsonResult<ShipmentVo> doGet(Long id) {
		try{
			if (id == null) {
				throw new WakaException("vo不能为空");
			}
			return JsonResult.newSuccess(adminSysService.getOneShip(id));
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
	 * @param query 查询条件<br/>
	 * @param start
	 *            开始行号<br/>
	 * @param size
	 *            记录数<br/>
	 * @param sortfield
	 *            排序(+字段1，-字段名2)<br/>
	 */
	@RequestMapping("/list")
	public JsonResult<Page<ShipmentVo>> doList(@RequestBody List<QueryFilter> query, Integer start, Integer size,
			String sortfield) {
		try{
//			if (Tools.collection.isEmpty(query) || start < 0 || size < 0 || Tools.string.isEmpty(sortfield)) {
//				throw new WakaException("id不能为空");
//			}
			return JsonResult.newSuccess(adminSysService.getListShip(query, start, size, sortfield));
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
	public JsonResult<Boolean> doUpdate(@RequestBody ShipmentBo vo) {
		try{
			if (vo == null) {
				throw new WakaException("vo不能为空");
			}
			if (null == vo.getShipmentType() || Tools.string.isEmpty(vo.getShipmentType().getKey())) {
				throw new WakaException("运费类型不能为空");
			}
			if (vo.getShipmentType() == ShipmentTypeEnum.Category) {
				if (null == vo.getCategoryPaths()) {
					throw new WakaException("商品分类不能为空");
				}
			}
			if (vo.getShipmentType() == ShipmentTypeEnum.Goods) {
				if (null == vo.getGoodsId()) {
					throw new WakaException("商品id不能为空");
				}
			}
			return JsonResult.newSuccess(adminSysService.updateShip(vo));
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
	 * 获取对应模块未添加的商品 Id
	 */
	@RequestMapping("/shipGoodslist")
	public JsonResult<Page<GoodsBo>> getNotInModelGoods(@RequestBody List<QueryFilter> query, Integer start,
			Integer size, String sortfield, String modelCode, String catePaths) {
		try{
//			if (Tools.collection.isEmpty(query) || start < 0 || size < 0 || Tools.string.isEmpty(sortfield)) {
//				throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
//			}
			return JsonResult.newSuccess(adminSysService.getNotInModelGoods(query, start, size,
					sortfield, modelCode, catePaths));
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
	 * 获取通用运费模板 退货发货用
	 * 
	 * @return
	 */
	@RequestMapping("/general_shiplist")
	public JsonResult<List<ShipmentBo>> generalShiplist() {
		try{
			return JsonResult.newSuccess(adminSysService.generalShiplist());
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