package cn.farwalker.ravv.order;

import java.util.List;

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

import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoBiz;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.order.orderlogistics.model.OrderLogisticsBo;
import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.util.Tools;

/**
 * 订单信息<br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
@RestController
@RequestMapping("/order/orderinfo")
public class OrderInfoController{
	private final static Logger log = LoggerFactory.getLogger(OrderInfoController.class);


	@Resource
	private IOrderInfoBiz orderInfoBiz;


	@Autowired
	private AdminOrderService adminOrderService;

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
			return JsonResult.newSuccess(adminOrderService.deleteOrderInfo(id));
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
	public JsonResult<OrderInfoBo> doGet(@RequestParam Long id) {
		try{
			if (id == null) {
				throw new WakaException("id不能为空");
			}
			return JsonResult.newSuccess(adminOrderService.getOneOrderInfo(id));
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
	 * @param userid
	 *            当前用户id<br/>
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
	public JsonResult<Page<OrderInfoBo>> doList(Long userid, @RequestBody List<QueryFilter> query, Integer start,
			Integer size, String sortfield) {
		try{
			if (userid == null) {
				throw new WakaException("用户id不能为空");
			}
			return JsonResult.newSuccess(adminOrderService.getListOrderInfo(userid, query, start, size, sortfield));
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
	 * 订单发货详情
	 */
	@RequestMapping("/getlogisticsinfo")
	public JsonResult<OrderLogisticsBo> getLogisticsInfo(@RequestParam Long orderId) {
		try{
			if (orderId == null) {
				throw new WakaException("订单id不能为空");
			}
			return JsonResult.newSuccess(adminOrderService.getOrderInfoLogisticsInfo(orderId));
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
	 * 退单发货详情
	 */
	@RequestMapping("/returnslogisticsinfo")
	public JsonResult<OrderLogisticsBo> getlogisticsByReturnsId(@RequestParam Long returnsId) {
		try{
			if (returnsId == null) {
				throw new WakaException("退单id不能为空");
			}
			return JsonResult.newSuccess(adminOrderService.getlogisticsByReturnsId(returnsId));
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
	 * 发货（非仓库管理员操作发货）
	 * 
	 * @param logisbo
	 * @return
	 */
	@RequestMapping("/sendgoods")
	@Transactional
	public JsonResult<Boolean> doSendGoods(@RequestBody OrderLogisticsBo logisbo, Long userId) {
		try{

			if (logisbo == null || logisbo.getOrderId() == null) {
				throw new WakaException("退单id不能为空");
			}
			return JsonResult.newSuccess(adminOrderService.doOrderInfoSendGoods(logisbo, userId));
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
	 * 修改物流信息
	 * 
	 * @param logisbo
	 *            订单id<br/>
	 */
	@RequestMapping("/updatelogistics")
	@Transactional
	public JsonResult<Boolean> updateOrderLogistics(@RequestBody OrderLogisticsBo logisbo, Long userId) {
		try{
			if (logisbo == null || logisbo.getId() == null) {
				throw new WakaException("物流单id不能为空");
			}
			return JsonResult.newSuccess(adminOrderService.updateOrderLogistics(logisbo, userId));
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
	 * 仓管发货
	 */
	@RequestMapping("/wearhouse_sendgoods")
	@Transactional
	public JsonResult<Boolean> wearhouseSendgoods(@RequestBody OrderLogisticsBo logisbo, Long userId) {
		try{
			if (logisbo == null || logisbo.getId() == null) {
				throw new WakaException("物流单id不能为空");
			}
			return JsonResult.newSuccess(adminOrderService.wearhouseSendgoods(logisbo, userId));
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
	 *            订单信息<br/>
	 */
	@RequestMapping("/update")
	@Transactional
	public JsonResult<?> doUpdate(@RequestBody OrderInfoBo vo, Long userId) {
		try {
			if (vo == null) {
				return JsonResult.newFail("vo不能为空");
			}
			return JsonResult.newSuccess(adminOrderService.updateOrderInfo(vo, userId));
		} catch (WakaException e) {
			log.error("", e);
			return JsonResult.newFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());
		}

	}

	/**
	 * 获取订单商品
	 */
	@RequestMapping("/ordergoods")
	public JsonResult<List<OrderGoodsBo>> getOrderGoods(Long orderId) {
		try {
			if (orderId == null) {
				return JsonResult.newFail("订单id不能为空");
			}
			return JsonResult.newSuccess(adminOrderService.getOrderGoods(orderId));
		} catch (WakaException e) {
			log.error("", e);
			return JsonResult.newFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());
		}

	}

	/**
	 * 修改订单金额信息
	 * 
	 * @param goods
	 *            订单金额信息<br/>
	 */
	@RequestMapping("/updategoods")
	public JsonResult<Boolean> doUpdateGoods(OrderGoodsBo goods) {
		try {
			if (goods == null) {
				return JsonResult.newFail("订单金额信息不能为空");
			}
			return JsonResult.newSuccess(adminOrderService.doUpdateGoods(goods));
		} catch (WakaException e) {
			log.error("", e);
			return JsonResult.newFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());
		}

	}

	/**
	 * 取得订单金额信息
	 * 
	 * @param orderid
	 *            订单id<br/>
	 */
	@RequestMapping("/payment")
	public JsonResult<OrderPaymemtBo> getPayment(@RequestParam Long orderid) {
		try {
			if (orderid == null) {
				return JsonResult.newFail("订单id不能为空");
			}
			return JsonResult.newSuccess(adminOrderService.getPayment(orderid));
		} catch (WakaException e) {
			log.error("", e);
			return JsonResult.newFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());
		}

	}

	/**
	 * 修改订单金额信息
	 * 
	 * @param paybo
	 *            订单金额信息<br/>
	 */
	@RequestMapping("/updatepayment")
	@Transactional
	public JsonResult<Boolean> doUpdatePayment(@RequestBody OrderPaymemtBo paybo, Long userId) {
		try {
			if (paybo == null) {
				return JsonResult.newFail("订单金额信息不能为空");
			}
			if (paybo.getId() == null) {
				return JsonResult.newFail("订单金额信息id不能为空");
			}
			return JsonResult.newSuccess(adminOrderService.doUpdatePayment(paybo, userId));
		} catch (WakaException e) {
			log.error("", e);
			return JsonResult.newFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());
		}

	}

	/**
	 * 修改订单商品
	 * 
	 * @param orderId
	 *            商品ID<br/>
	 * @param userId
	 *            库存及单价信息<br/>
	 */
	@RequestMapping("/update_ordergoods")
	@Transactional
	public JsonResult<Boolean> doUpdateOrderGoods(Long orderId, @RequestBody List<OrderGoodsBo> orderGoods,
			Long userId) {
		try {
			if (orderId == null) {
				return JsonResult.newFail("商品ID不能为空");
			}
			if (Tools.collection.isEmpty(orderGoods)) {
				return JsonResult.newFail("库存及单价信息不能为空");
			}
			return JsonResult.newSuccess(adminOrderService.doUpdateOrderGoods(orderId, orderGoods, userId));
		} catch (WakaException e) {
			log.error("", e);
			return JsonResult.newFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());
		}

	}

	/**
	 * 获取订单操作日志
	 */
	@RequestMapping("/order_operationLog")
	public JsonResult<Boolean> orderOperationLogList(Long orderId) {
		if (orderId == null) {
			return JsonResult.newFail("商品ID不能为空");
		}
		return JsonResult.newSuccess(true);
	}
	
	/**
	 * 供应商订单列表记录
	 * 
	 * @param merchantId 供应商id<br/>
	 * @param query 查询条件<br/>
	 * @param start 开始行号<br/>
	 * @param size 记录数<br/>
	 * @param sortfield 排序(+字段1,-字段名2)<br/>
	 */
	@RequestMapping("/merchant_orderinfo")
	public JsonResult<Page<OrderInfoBo>> getMerchantOrderInfo(Long merchantId, @RequestBody List<QueryFilter> query,
															  Integer start, Integer size, String sortfield) {
		try{
//			if (Tools.collection.isEmpty(query) || start < 0 || size < 0 || Tools.string.isEmpty(sortfield)) {
//				throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
//			}
			if (merchantId == null) {
				throw new WakaException("供应商id不能为空");
			}
			return JsonResult.newSuccess(adminOrderService.getMerchantOrderInfo(merchantId, query, start, size, sortfield));
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