package cn.farwalker.ravv.service.order.returns.biz;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuVo;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderReturnSkuVo;
import cn.farwalker.ravv.service.order.orderlogistics.model.OrderLogisticsBo;
import cn.farwalker.ravv.service.order.returns.constants.OperatorTypeEnum;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsGoodsStatusEnum;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsTypeEnum;
import cn.farwalker.ravv.service.order.returns.model.MemberOrderReturnsVo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnLogVo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsBo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsDetailBo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsDetailVo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsVo;
import cn.farwalker.waka.core.JsonResult;

/**
 * 订单退货<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
public interface IOrderReturnsService {

	/**
	 * 仓管获取对应状态
	 * @param status
	 * @return
	 */
	List<Map<String, String>> getReturnsStatusByStatusForWarehouse(ReturnsGoodsStatusEnum status);

	/**
	 * 通过当前状态获取操作订单可选状态
	 * @param status
	 * @return
	 */
	List<Map<String, String>> getReturnsStatusByStatusForService(ReturnsGoodsStatusEnum status);

	/**
	 * 判断操作者及操作类型添加订单操作日志
	 * @param userId
	 * @param operatorType
	 * @param vo
	 * @param changeStatus 修改状态
	 */
	JsonResult<Boolean> saveOperateOrderReturnsAndLog(Long userId, OperatorTypeEnum operatorType
			, OrderReturnsVo vo,ReturnsGoodsStatusEnum changeStatus);

	/**
	 * 仓管审核对应退品
	 * @param userId
	 * @param operatorType
	 * @param vo
	 */
	void saveCheckAndAcceptanceGoods(Long userId, OperatorTypeEnum operatorType, OrderReturnsVo vo);

	/**
	 * 按明细id更新状态，更新明细后，再检查是否需要更新主表
	 * @param detailBos (只使用id、status、validesc)3个字段
	 */
	public void updateReturnsStatus(List<OrderReturnsDetailBo> detailBos);

	/**
	 * 按明细id更新状态，更新明细后，再检查是否需要更新主表
	 * @param detailsIds
	 * @param status
	 * @param validesc 验收描述(null不更新)
	 */
	public void updateReturnsStatus(Long detailsIds, ReturnsGoodsStatusEnum status,String validesc);

	/**
	 * 创建退货
	 * @param orderid
	 * @param skus
	 * @param
	 * @return
	 */
	public OrderReturnsBo createOrderReturns(Long orderid, List<OrderReturnSkuVo> skus, ReturnsTypeEnum returnsType, MemberBo member);

	/**
	 * 通过订单创建退款申请（整单退）
	 * @param orderId
	 * @return
	 */
	public OrderReturnsBo createOrderRefund(Long orderId, String reasonType, String reason, MemberBo member);

	/**
	 * 统计每个订单的退货数量(按订单合计)
	 * @param orderid
	 * @param status 指定的统计状态（退/换货）
	 * @return key-订单id，value-退货数量
	 */
	public Map<Long,Integer> getReturnQuanByOrder(Collection<Long> orderIds,List<ReturnsGoodsStatusEnum> status);

	/**
	 * 统计每个订单的退货数量(按订单及商品sku统计，带退货主表id)
	 * @param orderid
	 * @param status 指定的统计状态（退/换货）
	 * @return key-订单id，value-退货数量
	 */
	public List<OrderReturnsDetailBo> getReturnQuanByGoods(Collection<Long> orderIds,List<ReturnsGoodsStatusEnum> status);

	/**
	 * 获取状态流程列表
	 * @param status
	 * @return
	 */
	public List<OrderReturnLogVo> getOrderReturnLogList(Long returnsId);

	/**
	 * app按条件获取退货单列表
	 * @param orderId
	 * @param returnsTypeList
	 * @param statusList
	 * @param orderFields
	 * @param start
	 * @param size
	 * @return
	 */
	public List<MemberOrderReturnsVo> getMyReturns(Long memberId,List<ReturnsTypeEnum> returnsTypeList, List<ReturnsGoodsStatusEnum> statusList, List<String> orderFields,Integer start,
			Integer size, Boolean isReturnFinish, Long orderId);

	/**
	 * 获取退单中對應的的sku信息
	 * @param returnsId
	 * @return
	 */
	public List<GoodsSkuVo> getSkuInfoByReturnsId(Long returnsId);

	/**
	 * TODO 客服是否允许任意修改修改
	 * 修改退单
	 * @param userId
	 * @param operatorType
	 * @param vo
	 * @return
	 */
	public JsonResult<Boolean> updateOrderReturnsAndCreateLog(Long userId, OperatorTypeEnum operatorType, OrderReturnsBo vo);

	/**
	 * 仓管发货给用户
	 * @param logisbo
	 * @param userId
	 * @return
	 */
	public Boolean updateStatusAndSendGoods(OrderLogisticsBo logisbo, Long userId, Long returnsId);

	/**
	 * 获取退货单详情 
	 * @param returnsId
	 * @return
	 */
	public List<OrderReturnsDetailVo> getOrderReturnsDetailList(Long returnsId);

	/**
	 * 用户发货
	 * @param memberId
	 * @param status
	 * @param returnsId
	 * @param logisticsNo
	 * @param logisticsCompany
	 * @return
	 */
	public Boolean updateForReturnShipment(MemberBo member, Long returnsId, String logisticsNo, String logisticsCompany);

	/**
	 * 用户收货
	 * @param member
	 * @param returnsId
	 * @return
	 */
	public Boolean updateForConfirmReceipt(MemberBo member, Long returnsId);

	/**
	 * 退货相关信息
	 * 带上退货单中商品总数的
	 * @param returnsId
	 * @return
	 */
	public OrderReturnsVo getOrderReturnsInfo(Long returnsId);
}