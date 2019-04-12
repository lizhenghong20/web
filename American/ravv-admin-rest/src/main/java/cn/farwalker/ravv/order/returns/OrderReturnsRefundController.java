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
import org.springframework.web.bind.annotation.RestController;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsBo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsVo;
import cn.farwalker.waka.core.JsonResult;

/**
 * 财务订单退款<br/>
 * 财务订单退款，有发货时，仓库收到退货后才能退款，没有发货时，直接退款<br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
@RestController
@RequestMapping("/order/returns/refund")
public class OrderReturnsRefundController extends OrderReturnGoodsController {
	private final static Logger log = LoggerFactory.getLogger(OrderReturnsRefundController.class);

	@Autowired
	private AdminOrderService adminOrderService;

	/**
	 * 订单退款
	 * 
	 * @param returnsBo
	 *            退货单id<br/>
	 */
	@RequestMapping("/reundpay")
	public JsonResult<Boolean> doReundPay(@RequestBody OrderReturnsBo returnsBo, Long userId) {
		try{
			if (returnsBo == null) {
				throw new WakaException("退货单不能为空");
			}
			if (returnsBo.getId() == null) {
				throw new WakaException("退货单id不能为空");
			}
			return adminOrderService.doReundPay(returnsBo, userId);
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
	 * 客服操作退货/换货记录
	 * 
	 * @param vo
	 * @return
	 */
	@RequestMapping("/service_operate_refund")
	@Transactional
	public JsonResult<Boolean> serviceOperateRefund(@RequestBody OrderReturnsVo vo, Long userId) {
		try{
			if (vo == null || vo.getId() == null || vo.getStatus() == null) {
				throw new WakaException("退货单id不能为空");
			}
			return adminOrderService.serviceOperateRefund(vo, userId);
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
	 * 获取当前可选状态
	 * 
	 * @param returnsid
	 * @return
	 */
	@RequestMapping("/get_return_status_enum")
	public JsonResult<List<Map<String, String>>> getReturnsStatus(Long returnsid) {
		try{
			if (returnsid == null) {
				throw new WakaException("退货单id不能为空");
			}
			return JsonResult.newSuccess(adminOrderService.getReturnsStatus(returnsid));
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