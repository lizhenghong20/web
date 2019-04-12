package cn.farwalker.ravv.order.returns;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.farwalker.ravv.admin.order.AdminOrderService;
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

import cn.farwalker.ravv.service.order.orderlogistics.model.OrderLogisticsBo;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsService;
import cn.farwalker.ravv.service.order.returns.constants.OperatorTypeEnum;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsBo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsDetailVo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsVo;
import cn.farwalker.waka.core.JsonResult;

/**
 * 仓库订单退货<br/>
 * 仓库订单退货,只显示有发货的退货订单，没有发货时，直接退款了，跟仓库没有关系了<br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
@RestController
@RequestMapping("/order/returns/goods")
public class OrderReturnGoodsController{
	private final static Logger log = LoggerFactory.getLogger(OrderReturnGoodsController.class);

	@Resource
	private IOrderReturnsService orderReturnsService;

	@Autowired
	private AdminOrderService adminOrderService;

	/**
	 * 退货申请审核
	 * 
	 * @param returnsid
	 *            退货单id<br/>
	 * @param state
	 *            审核状态<br/>
	 */
	@RequestMapping("/audi")
	public JsonResult<OrderReturnsBo> doAudi(Long returnsid, Boolean state) {
		try {
			if (returnsid == null) {
				throw new WakaException("vo不能为空");
			}
			if (state == null) {
				throw new WakaException("审核状态不能为空");
			}
			return JsonResult.newSuccess(adminOrderService.createReturnGoods(returnsid, state));
		} catch (WakaException e) {
			log.error("", e);
			return JsonResult.newFail(e.getCode(), e.getMessage());
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
		try{
			if (id == null) {
				throw new WakaException("id不能为空");
			}
			return JsonResult.newSuccess(adminOrderService.deleteReturnGoods(id));
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
	public JsonResult<OrderReturnsBo> doGet(@RequestParam Long id) {
		try{
			if (id == null) {
				throw new WakaException("id不能为空");
			}
			return JsonResult.newSuccess(adminOrderService.getOneReturnGoods(id));
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
	public JsonResult<Page<OrderReturnsBo>> doList(@RequestBody List<QueryFilter> query, Integer start, Integer size,
			String sortfield) {
		try{
//			if (Tools.collection.isEmpty(query) || start < 0 || size < 0 || Tools.string.isEmpty(sortfield)) {
//				throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
//			}
			return JsonResult.newSuccess(adminOrderService.getListReturnGoods(query, start, size, sortfield));
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
	public JsonResult<?> doUpdate(@RequestBody OrderReturnsBo vo, Long userId) {
		try {
			if (vo == null) {
				return JsonResult.newFail("vo不能为空");
			}
			return orderReturnsService.updateOrderReturnsAndCreateLog(userId, OperatorTypeEnum.server, vo);
		} catch (WakaException e) {
			log.error("", e);
			return JsonResult.newFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());
		}
	}

	/**
	 * 仓库操作退货/换货记录
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping("/warehouse_operate_refund")
	public JsonResult<Boolean> warehouseOperateRefund(@RequestBody OrderReturnsVo vo, Long userId) {
		try {
			if (vo == null || vo.getId() == null || vo.getStatus() == null) {
				return JsonResult.newFail("退货单id不能为空");
			}
			return adminOrderService.warehouseOperateRefund(vo, userId);
		} catch (WakaException e) {
			log.error("", e);
			return JsonResult.newFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());
		}
	}

	/**
	 * 仓库验收退品
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping("/acceptance_returns_goods")
	@Transactional
	public JsonResult<Boolean> checkAndAcceptanceGoods(@RequestBody OrderReturnsVo vo, Long userId) {
		try {
			if (vo == null || vo.getOrderReturnsDetailId() == null) {
				return JsonResult.newFail("退详情id不能为空");
			}
			return JsonResult.newSuccess(adminOrderService.checkAndAcceptanceGoods(vo, userId));
		} catch (WakaException e) {
			log.error("", e);
			return JsonResult.newFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());
		}

	}

	/**
	 * 获取可选状态列表
	 * 
	 * @param returnsid
	 * @return
	 */
	@RequestMapping("/get_warehouse_return_status")
	public JsonResult<List<Map<String, String>>> getReturnsGoodsStatus(Long returnsid) {
		try {
			if (returnsid == null) {
				return JsonResult.newFail("退货单id不能为空");
			}
			return JsonResult.newSuccess(adminOrderService.getReturnsGoodsStatus(returnsid));
		} catch (WakaException e) {
			log.error("", e);
			return JsonResult.newFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());
		}

	}

	/**
	 * 获取换货物流信息
	 * 
	 * @param returnsId
	 * @return
	 */
	@RequestMapping("/getlogisticsinfo")
	public JsonResult<OrderLogisticsBo> getLogisticsInfo(Long returnsId, Long orderId) {
		try {
			if (returnsId == null) {
				return JsonResult.newFail("退货单id不能为空");
			}
			return JsonResult.newSuccess(adminOrderService.getReturnGoodsLogisticsInfo(returnsId, orderId));
		} catch (WakaException e) {
			log.error("", e);
			return JsonResult.newFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());
		}
	}

	/**
	 * 订单发货
	 * 
	 * @param logisbo
	 * @param userId
	 * @return
	 */
	@RequestMapping("/sendgoods")
	@Transactional
	public JsonResult<Boolean> doSendGoods(@RequestBody OrderLogisticsBo logisbo, Long userId) {
		try {
			Long returnsId = logisbo.getReturnsId();
			if (logisbo == null || returnsId == null) {
				return JsonResult.newFail("退货单id不能为空");
			}else if(logisbo.getShipmentId() == null){
				return JsonResult.newFail("运费模板不能为空");
			}
			return JsonResult.newSuccess(adminOrderService.doReturnGoodsSendGoods(logisbo, returnsId, userId));
		} catch (WakaException e) {
			log.error("", e);
			return JsonResult.newFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());
		}
	}

	/**
	 * 取得单条退货记录对应的退货记录
	 * 
	 * @param returnsId
	 * @return
	 */
	@RequestMapping("/returns_detail")
	public JsonResult<List<OrderReturnsDetailVo>> getReturnsDetail(@RequestParam Long returnsId) {
		try {

			if (returnsId == null) {
				return JsonResult.newFail("退货单id不能为空");
			}
			return JsonResult.newSuccess(adminOrderService.getReturnsDetail(returnsId));
		} catch (WakaException e) {
			log.error("", e);
			return JsonResult.newFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());
		}

	}
	
	/**
	 * 供应商订单退货列表记录
	 * 
	 * @param merchantId 供应商id
	 * @param query 查询条件<br/>
	 * @param start 开始行号<br/>
	 * @param size 记录数<br/>
	 * @param sortfield 排序(+字段1,-字段名2)<br/>
	 */
	@RequestMapping("/merchant_returns")
	public JsonResult<Page<OrderReturnsBo>> getMerchantReturns(Long merchantId, @RequestBody List<QueryFilter> query,
															   Integer start, Integer size, String sortfield) {
		try{
//			if (Tools.collection.isEmpty(query) || start < 0 || size < 0 || Tools.string.isEmpty(sortfield)) {
//				throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
//			}
			if (merchantId == null) {
				throw new WakaException("供应商id不能为空");
			}
			return JsonResult.newSuccess(adminOrderService.getMerchantReturns(merchantId, query, start, size, sortfield));
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