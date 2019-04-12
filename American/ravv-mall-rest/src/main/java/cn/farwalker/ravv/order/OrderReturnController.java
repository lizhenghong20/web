package cn.farwalker.ravv.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import cn.farwalker.waka.core.HttpKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import cn.farwalker.ravv.order.dto.OrderAllInfoVo;
import cn.farwalker.ravv.service.base.storehouse.biz.IStorehouseBiz;
import cn.farwalker.ravv.service.base.storehouse.model.StorehouseBo;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsBiz;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.goodssku.skudef.biz.IGoodsSkuDefBiz;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuDefBo;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuVo;
import cn.farwalker.ravv.service.member.basememeber.biz.IMemberBiz;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.order.ordergoods.biz.IOrderGoodsBiz;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoBiz;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderStatusEnum;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderTypeEnum;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderReturnSkuVo;
import cn.farwalker.ravv.service.order.orderlogistics.biz.IOrderLogisticsBiz;
import cn.farwalker.ravv.service.order.orderlogistics.model.OrderLogisticsBo;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnLogService;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsBiz;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsDetailBiz;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsService;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsGoodsStatusEnum;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsTypeEnum;
import cn.farwalker.ravv.service.order.returns.model.MemberOrderReturnsVo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnLogVo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsBo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsDetailBo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsDetailVo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsVo;
import cn.farwalker.ravv.service.sys.dict.biz.ISysDictBiz;
import cn.farwalker.ravv.service.sys.dict.dao.ISysDictDao;
import cn.farwalker.waka.cache.CacheManager;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.base.controller.ControllerUtils;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;

/**
 * 订单退货处理<br/>
 * 
 * @author generateModel.java
 */
@RestController
@RequestMapping("/order/return")
public class OrderReturnController{
	public static final String K_MemberId = "memberId";
	private final static Logger log = LoggerFactory.getLogger(OrderReturnController.class);
	@Resource
	private IOrderReturnsService orderReturnsService;

	@Resource
	private IOrderReturnsBiz orderReturnsBiz;

	@Resource
	private IOrderInfoBiz orderInfoBiz;

	@Resource
	private OrderMyController myorder;

	@Resource
	private IGoodsBiz goodsBiz;

	@Resource
	private IGoodsSkuDefBiz goodsSkuDefBiz;

	@Resource
	private IOrderReturnsDetailBiz orderReturnsDetailBiz;

	@Resource
	private IOrderReturnLogService orderReturnLogService;

	@Resource
	private IMemberBiz memberBiz;

	@Resource
	private ISysDictDao dictMapper;

	@Resource
	private ISysDictBiz sysDictBiz;

	@Resource
	private IStorehouseBiz storehouseBiz;

	@Resource
	private IOrderGoodsBiz ordergoodBiz;

	@Resource
	private IOrderLogisticsBiz logisticsBiz;

	/**
	 * 退货前确认(显示详细详细)
	 * 
	 * @param orderid
	 *            订单id，必填
	 * @param skuid
	 *            skuid，为空时取所有商品
	 * @return
	 */
	@RequestMapping("/confirm")
	public JsonResult<OrderAllInfoVo> doConfirm(Long orderid, Long skuid) {
		// createMethodSinge创建方法
		if (orderid == null) {
			return JsonResult.newFail("订单id(不能是顶级订单id)不能为空");
		}
		OrderInfoBo orderBo = orderInfoBiz.selectById(orderid);
		OrderStatusEnum state = orderBo.getOrderStatus();
		List<OrderStatusEnum> valid = OrderStatusEnum.getValid(false);
		if (!valid.contains(state)) {// 已支付就才可以退
			log.error("订单未支付，不能退货" + orderid);
			return JsonResult.newFail("订单未支付，不能退货");
		}
		if (orderBo.getOrderType() == OrderTypeEnum.MASTER) {
			log.error("订单类型不正确，拆单主单不能退货" + orderid);
			return JsonResult.newFail("订单类型不正确，拆单主单不能退货");
		}

		/////////////////////////////////////////////////
		JsonResult<OrderAllInfoVo> js = myorder.doGet(orderid);
		if (Tools.number.nullIf(skuid, 0) > 0) { // 选择了商品
			List<OrderGoodsBo> goodsBos = js.getData().getGoodsBos();
			for (int i = goodsBos.size() - 1; i >= 0; i--) {
				OrderGoodsBo g = goodsBos.get(i);
				if (!g.getSkuId().equals(skuid)) {
					goodsBos.remove(i);
				}
			}
		}
		return js;
	}

	/**
	 * 保存退货信息
	 * 
	 * @param orderId
	 *            订单id(不能是顶级订单id)<br/>
	 * @param skus
	 *            sku及数量
	 * @param
	 */
	@RequestMapping("/save")
	@Transactional
	public JsonResult<OrderReturnsBo> doSave(@RequestBody List<OrderReturnSkuVo> skus, HttpSession session,
			Long orderId, ReturnsTypeEnum returnsType) {
		// createMethodSinge创建方法
		if (orderId == null) {
			return JsonResult.newFail("订单id(不能是顶级订单id)不能为空");
		}
		HttpSession sin = HttpKit.getRequest().getSession();
		Long memberId = (Long) sin.getAttribute(OrderReturnController.K_MemberId);
		MemberBo member = this.getMemberBo(memberId);
		// 退货状态
		OrderReturnsBo bo = orderReturnsService.createOrderReturns(orderId, skus, returnsType, member);
		return JsonResult.newSuccess(bo);
	}

	/**
	 * 获取退货单列表信息
	 * 
	 * @param returnsType
	 * @param status
	 * @param start
	 * @param size
	 * @param orderFields
	 * @param isReturnFinish
	 * @return
	 */
	@RequestMapping("/order_returns")
	public JsonResult<List<MemberOrderReturnsVo>> getOrderReturns(ReturnsTypeEnum returnsType,
			ReturnsGoodsStatusEnum status, Integer start, Integer size, String orderFields, Boolean isReturnFinish) {
		// 退货是否完成 isReturnFinish
		HttpSession sin = HttpKit.getRequest().getSession();
		Long memberId = (Long) sin.getAttribute(OrderReturnController.K_MemberId);
		// createMethodSinge创建方法
		if (memberId == null) {
			return JsonResult.newFail("订单id(不能是顶级订单id)不能为空");
		}

		List<String> orderFieldList = getOrderBys(orderFields);
		List<ReturnsTypeEnum> returnsTypeList = new ArrayList<>();
		if (returnsType != null) {
			returnsTypeList.add(returnsType);
		}
		List<ReturnsGoodsStatusEnum> statusList = new ArrayList<>();
		if (status != null) {
			statusList.add(status);
		}
		List<MemberOrderReturnsVo> rs = orderReturnsService.getMyReturns(memberId, returnsTypeList, statusList,
				orderFieldList, start, size, isReturnFinish, null);
		if (Tools.collection.isNotEmpty(rs)) {
			// 连表查询对应的商品信息
			// LoadJoinValueImpl.load(orderReturnsBiz, rs);
			for (MemberOrderReturnsVo vo : rs) {
				List<GoodsSkuVo> goodSkuInfo = orderReturnsService.getSkuInfoByReturnsId(vo.getId());
				// 补全图片路径
				if (Tools.collection.isNotEmpty(goodSkuInfo)) {
					for (GoodsSkuVo sku : goodSkuInfo) {
							sku.setImageUrl(QiniuUtil.getFullPath(sku.getImageUrl()));
					}
					vo.setGoodSkuInfo(goodSkuInfo);
				}

			}
		}
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 获取退货单信息
	 * 
	 * @param returnsId
	 * @return
	 */
	@RequestMapping("/returns_info")
	public JsonResult<OrderReturnsVo> getReturnsInfo(@RequestParam Long returnsId) {
		// createMethodSinge创建方法
		if (returnsId == null) {
			return JsonResult.newFail("订单id(不能是顶级订单id)不能为空");
		}
		// 需查询出 退单中退货商品数量
		OrderReturnsVo rs = orderReturnsService.getOrderReturnsInfo(returnsId);
		// OrderReturnsBo bo = orderReturnsBiz.selectById(returnsId);

		if (rs == null) {
			return JsonResult.newFail("找不到对应退货单");
		}
		List<OrderReturnLogVo> returnsLogList = orderReturnsService.getOrderReturnLogList(returnsId);
		// 添加 成功：1 进行中(当前)：0 失败：-1
		if (Tools.collection.isNotEmpty(returnsLogList)) {
			List<ReturnsGoodsStatusEnum> failedStatus = Arrays.asList(ReturnsGoodsStatusEnum.exchangeAuditFail,
					ReturnsGoodsStatusEnum.refuseExchange, ReturnsGoodsStatusEnum.refundAuditFail,
					ReturnsGoodsStatusEnum.refuseRefund, ReturnsGoodsStatusEnum.failed);
			for (OrderReturnLogVo log : returnsLogList) {
				if (failedStatus.contains(log.getReturnStatus())) {
					log.setSuccess(-1);
				} else {
					if (rs.getStatus() == log.getReturnStatus() || rs.getStatus() == ReturnsGoodsStatusEnum.finish) {
						log.setSuccess(0);
					} else {
						log.setSuccess(1);
					}
				}
			}
		}
		rs.setReturnsLogList(returnsLogList);
		// 物流信息
		{
			OrderLogisticsBo queryL = new OrderLogisticsBo();
			queryL.setReturnsId(returnsId);
			rs.setLogisticsBo(logisticsBiz.selectOne(new EntityWrapper<OrderLogisticsBo>(queryL)));
		}
		// 添加仓库地址信息
		OrderInfoBo order = orderInfoBiz.selectById(rs.getOrderId());
		if (order != null) {
			StorehouseBo storehouse = storehouseBiz.selectById(order.getStorehouseId());
			rs.setStorehouse(storehouse);
		}
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 获取退货商品详情信息
	 * 
	 * @param returnsId
	 * @return
	 */
	@RequestMapping("/returns_detail")
	public JsonResult<List<OrderReturnsDetailVo>> getReturnsDetailList(@RequestParam Long returnsId) {
		// createMethodSinge创建方法
		if (returnsId == null) {
			return JsonResult.newFail("订单id(不能是顶级订单id)不能为空");
		}
		OrderReturnsDetailBo query = new OrderReturnsDetailBo();
		query.setReturnsId(returnsId);
		List<OrderReturnsDetailBo> orderDetailList = orderReturnsDetailBiz
				.selectList(new EntityWrapper<OrderReturnsDetailBo>(query));
		List<OrderReturnsDetailVo> rs = ControllerUtils.convertList(orderDetailList, OrderReturnsDetailVo.class);
		// 商品详情及sku信息
		if (Tools.collection.isNotEmpty(rs)) {
			for (OrderReturnsDetailVo vo : rs) {
				// 处理图片为全路径
				if (Tools.string.isNotEmpty(vo.getImglistJson())) {
					List<String> imgList = Tools.json.toList(vo.getImglistJson(), String.class);
					if (Tools.collection.isNotEmpty(imgList)) {
						List<String> fullPathImgList = new ArrayList<>();
						for (String img : imgList) {
							fullPathImgList.add(QiniuUtil.getFullPath(img));
						}
						vo.setImgList(fullPathImgList);
					}
				}
				GoodsBo goods = goodsBiz.selectById(vo.getGoodsId());
				if (goods != null) {
					vo.setGoods(goods);
				}
				GoodsSkuDefBo skuVo = goodsSkuDefBiz.selectById(vo.getSkuId());
				if (skuVo != null) {
					skuVo.setImageUrl(QiniuUtil.getFullPath(skuVo.getImageUrl()));
					vo.setGoodsSkuDef(skuVo);
				}
			}
		}
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 填写单号，确认已发送退品给客服，待客服签收
	 * 
	 * @param returnsId
	 * @param logisticsNo
	 * @param logisticsNo
	 * @return
	 */
	@RequestMapping("/return_shipment")
	@Transactional
	public JsonResult<Boolean> returnShipment(Long returnsId, String logisticsNo, String shipmentId) {
		if (returnsId == null) {
			return JsonResult.newFail("退单id(不能是顶级订单id)不能为空");
		}
		// TODO 前端现在是通过logisticsCompany 来传递物流id的，需改为shipId来接收id 类型为Long型
		if (Tools.string.isEmpty(shipmentId)) {
			return JsonResult.newFail("物流公司不能为空");
		}
		HttpSession sin = HttpKit.getRequest().getSession();
		Long memberId = (Long) sin.getAttribute(OrderReturnController.K_MemberId);
		MemberBo member = this.getMemberBo(memberId);
		Boolean rs = orderReturnsService.updateForReturnShipment(member, returnsId, logisticsNo, shipmentId);

		return JsonResult.newSuccess(rs);
	}

	/**
	 * 确认收到退换商品
	 * 
	 * @param returnsId
	 * @return
	 */
	@RequestMapping("/confirm_receipt")
	@Transactional
	public JsonResult<Boolean> confirmReceipt(@RequestParam Long returnsId) {
		if (returnsId == null) {
			return JsonResult.newFail("退单id(不能是顶级订单id)不能为空");
		}
		OrderReturnsBo oldReturns = orderReturnsBiz.selectById(returnsId);
		Boolean rs = false;
		if (oldReturns.getStatus() == ReturnsGoodsStatusEnum.buyerWaitReceived) {
			HttpSession sin = HttpKit.getRequest().getSession();
			Long memberId = (Long) sin.getAttribute(OrderReturnController.K_MemberId);
			MemberBo member = this.getMemberBo(memberId);
			rs = orderReturnsService.updateForConfirmReceipt(member, returnsId);
		}
		return JsonResult.newSuccess(rs);
	}

	/**
	 * 单退款
	 * 
	 * @param orderId
	 * @param reasonType
	 * @param reason
	 * @return
	 */
	@RequestMapping("/refund")
	public JsonResult<OrderReturnsBo> toRefund(@RequestParam Long orderId, String reasonType, String reason) {
		if (orderId == null) {
			return JsonResult.newFail("订单id(不能是顶级订单id)不能为空");
		}
		HttpSession sin = HttpKit.getRequest().getSession();
		Long memberId = (Long) sin.getAttribute(OrderReturnController.K_MemberId);
		MemberBo member = this.getMemberBo(memberId);
		// 只是退款，直接生成退款单
		OrderReturnsBo bo = orderReturnsService.createOrderRefund(orderId, reasonType, reason, member);
		return JsonResult.newSuccess(bo);
	}

	private List<String> getOrderBys(String sortfield) {
		List<String> rs = new ArrayList<>(3);
		if (Tools.string.isEmpty(sortfield)) {
			return rs;
		}

		String[] fds = sortfield.split(",");
		for (String fd : fds) {
			if (Tools.string.isEmpty(fd)) {
				continue;
			}
			char fc = fd.charAt(0);
			boolean isAsc = true;// 默认升序
			if (fc == '-' || fc == '+') {
				isAsc = (fc == '+');
				fd = fd.substring(1);
			}
			if (isAsc) {
				rs.add(fd);
			} else {
				rs.add(fd + " desc");
			}
		}
		return rs;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param memberId
	 * @return
	 */
	private MemberBo getMemberBo(Long memberId) {
		String key = "member_" + memberId;
		MemberBo rd = CacheManager.cache.getObject(key);
		if (rd == null) {
			rd = memberBiz.selectById(memberId);
			CacheManager.cache.putObject(key, rd);
		}
		return rd;
	}

}