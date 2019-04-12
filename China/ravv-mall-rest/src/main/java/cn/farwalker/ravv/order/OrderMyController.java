package cn.farwalker.ravv.order;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;


import cn.farwalker.waka.core.HttpKit;
import cn.farwalker.ravv.service.order.orderlogistics.biz.IOrderLogisticsService;
import cn.farwalker.ravv.service.order.orderlogistics.model.LogisticsTraceVo;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.farwalker.ravv.order.dto.OrderAllInfoVo;
import cn.farwalker.ravv.order.dto.OrderFilterEnum;
import cn.farwalker.ravv.order.dto.OrderGoodsReturnVo;
import cn.farwalker.ravv.order.dto.OrderPayVo;
import cn.farwalker.ravv.service.goods.utils.GoodsUtil;
import cn.farwalker.ravv.service.goodsext.comment.biz.IGoodsCommentBiz;
import cn.farwalker.ravv.service.goodsext.comment.model.GoodsCommentBo;
import cn.farwalker.ravv.service.member.basememeber.biz.IMemberBiz;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.order.constants.PayStatusEnum;
import cn.farwalker.ravv.service.order.ordergoods.biz.IOrderGoodsBiz;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoBiz;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoService;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderPaymentService;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderStatusEnum;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderTypeEnum;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.order.orderlogistics.biz.IOrderLogisticsBiz;
import cn.farwalker.ravv.service.order.orderlogistics.model.OrderLogisticsBo;
import cn.farwalker.ravv.service.order.paymemt.biz.IOrderPaymemtBiz;
import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsBiz;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsService;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsGoodsStatusEnum;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsBo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsDetailBo;
import cn.farwalker.ravv.service.shipment.biz.IShipmentBiz;
import cn.farwalker.ravv.service.shipment.constants.ShipmentTypeEnum;
import cn.farwalker.ravv.service.shipment.model.ShipmentBo;
import cn.farwalker.ravv.service.sys.param.biz.ISysParamService;
import cn.farwalker.ravv.service.sys.param.biz.SysParamCache;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.util.Tools;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cangwu.frame.orm.core.annotation.LoadJoinValueImpl;

/**
 * 我的订单信息<br/>
 */
@RestController
@RequestMapping("/order/my")
public class OrderMyController{
    private final static  Logger log =LoggerFactory.getLogger(OrderMyController.class);
    @Resource
    private IOrderInfoBiz orderInfoBiz;
    @Resource
    private IOrderInfoService orderInfoService;
    
    @Resource
    private IOrderGoodsBiz orderGoodsBiz;
    @Resource
    private IOrderPaymemtBiz paymentBiz;
    @Resource
    private IOrderLogisticsBiz logisticsBiz;
    @Resource
    private IOrderReturnsService orderReturnsService;
    @Resource
    private IOrderReturnsBiz orderReturnsBiz;
    
    @Resource
    private IOrderPaymentService orderPayemntServer;

    @Resource
	private ISysParamService sysparamService;
    @Resource
	private IGoodsCommentBiz goodsCommentBiz;
    @Resource
	private IMemberBiz memberBiz;
    @Resource
	private IShipmentBiz shipmentBiz;

    @Autowired
	private IOrderLogisticsService orderLogisticsService;
    
    protected IOrderInfoBiz getBiz(){
        return orderInfoBiz;
    }
    /**
     * 执行支付前处理
     * @param orderid 订单id(必须是顶级订单id)<br/>
     */
    @RequestMapping("/dopay")
    public JsonResult<OrderPayVo> doPayment(@RequestParam Long orderid){
        //createMethodSinge创建方法
        if(orderid==null){
            return JsonResult.newFail("订单id(必须是顶级订单id)不能为空");
        }
        
        Wrapper<OrderPaymemtBo> wrap = new EntityWrapper<>();
        wrap.eq(OrderPaymemtBo.Key.orderId.toString(), orderid);
        wrap.last("limit 1");
        OrderPaymemtBo payBo = paymentBiz.selectOne(wrap);

        if(payBo.getPayStatus()==PayStatusEnum.PAID){
        	log.error(orderid + "订单已支付");
        	return JsonResult.newFail("订单已支付");
        }
        
        OrderInfoBo orderBo = orderInfoBiz.selectById(orderid);
        if(orderBo.getOrderType()==OrderTypeEnum.CHILD){
        	return JsonResult.newFail("只有总订单才能支付,拆单表不能支付");
        }
        
        OrderPayVo payVo = Tools.bean.cloneBean(payBo, new OrderPayVo());
        List<OrderGoodsBo> goodsBos = getOrderGoodsList(orderid);
        payVo.setGoodsBos(goodsBos);
        
        int delay = SysParamCache.getUnfreezeCacheDelay(sysparamService);
        { //过期时间
        	Calendar timeOut = Calendar.getInstance();
        	Date createTime =orderBo.getGmtCreate(); 
        	if(createTime != null){
        		timeOut.setTime(createTime);
        		payVo.setGmtCreate(createTime); //使用订单的创建时间(其实不同步也不会有问题)
        	}
        	timeOut.add(Calendar.MINUTE, delay);
        	payVo.setTimeout(timeOut.getTime());
        	payVo.setSystime(new Date());
        }
		Long membrId =  (Long)HttpKit.getRequest().getSession().getAttribute("memberId");
		if(membrId == null||membrId == 0){
			throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
		}
		MemberBo query =  memberBiz.selectById(membrId);
		if(query != null){
			payVo.setAdvance(query.getAdvance() == null ? BigDecimal.ZERO:query.getAdvance());
		}

        return JsonResult.newSuccess(payVo);
    }
    
    
    /**取得单条记录
     * @param id 订单id<br/>
     */
    @RequestMapping("/get")
    public JsonResult<OrderAllInfoVo> doGet(@RequestParam Long id){
        //createMethodSinge创建方法
        if(id==null){
            return JsonResult.newFail("id不能为空");
        }
        OrderInfoBo bo = getBiz().selectById(id);
        if(bo == null){
        	return JsonResult.newFail(id + "订单不存在");
        }
        OrderAllInfoVo vo = Tools.bean.cloneBean(bo, new OrderAllInfoVo());
        List<OrderAllInfoVo> vos = Arrays.asList(vo); 
        LoadJoinValueImpl.load(orderInfoBiz, vos);
        
        if(vo.getOrderType() == OrderTypeEnum.MASTER){
        	List<OrderAllInfoVo> childs = vo.getChildren(); //未支付时，取明细订单
        	LoadJoinValueImpl.load(orderInfoBiz, childs);
        	for(OrderAllInfoVo v :childs){
        		fillGoodsImage(v.getGoodsBos());	
        	}
        	loadReturnQuanByGoods(childs);
        	loadReturnTypeByFirstGoods(childs);
        }
        else{
        	fillGoodsImage(vo.getGoodsBos());
        	loadReturnQuanByGoods(vos);
        	loadReturnTypeByFirstGoods(vos);
        }
        loadCommentCount(vos);
        ShipmentBo shipmentBo = getShipmentBo(vo.getLogisticsBo(), vo.getPaymentBo());
        vo.setShipmentBo(shipmentBo);
        
        vo.setSystime(new Date());
       // loadOrderGoodsReturnVo(vo.getGoodsBos());
        
        return JsonResult.newSuccess(vo);
    }
    /**
     * 取得物流信息（如果没有模板id，创建默认的信息）
     * @param logisticsBo
     * @param paymentBo
     * @return
     */
    private ShipmentBo getShipmentBo(OrderLogisticsBo logisticsBo,OrderPaymemtBo paymentBo){
    	Long shipmentId = logisticsBo.getShipmentId();
    	ShipmentBo bo ;
    	if(Tools.number.nullIf(shipmentId, -1)>0){
    		bo = shipmentBiz.selectById(shipmentId);
    		if(bo == null){
    			bo = new ShipmentBo();
    	    	bo.setName("default");
    	    	bo.setShipmentType(ShipmentTypeEnum.General);
    		}
    	}
    	else{
    		bo = new ShipmentBo();
	    	bo.setName("default");
	    	bo.setShipmentType(ShipmentTypeEnum.Goods);
    	}
    	bo.setFee(paymentBo.getPostFee());
    	return bo;
    }

	@RequestMapping("/get_logistics")
	public JsonResult<List<LogisticsTraceVo>> getLogistics(Long orderId){
		try{
			//createMethodSinge创建方法
			if(orderId == null){
				throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR + "订单号为空");
			}
			return JsonResult.newSuccess(orderLogisticsService.getLogisticsTrace(orderId));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		} catch (Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}
	}

	@RequestMapping("/test_logistics")
	public JsonResult<List<LogisticsTraceVo>> testLogistics(String expCode, String expNo){
		try{
			//createMethodSinge创建方法
			if(Tools.string.isEmpty(expCode) || Tools.string.isEmpty(expNo)){
				throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR + "订单号为空");
			}
			log.info("查询");
			return JsonResult.newSuccess(orderLogisticsService.test(expCode, expNo));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		} catch (Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}
	}
 
    
    /**
     * 取得单条商品记录
     *
     */
    @RequestMapping("/ordergoods")
    public JsonResult<OrderAllInfoVo> getOrderGoods(@RequestParam Long orderid,@RequestParam Long skuid){
    	OrderInfoBo bo = getBiz().selectById(orderid);
        OrderAllInfoVo vo = Tools.bean.cloneBean(bo, new OrderAllInfoVo());
        Wrapper<OrderGoodsBo> wp = new EntityWrapper<>();
        wp.eq(OrderGoodsBo.Key.orderId.toString(), orderid);
        wp.eq(OrderGoodsBo.Key.skuId.toString(), skuid);
        List<OrderGoodsBo> goodsBos = orderGoodsBiz.selectList(wp);
        vo.setGoodsBos(goodsBos);
        fillGoodsImage(goodsBos);
        return JsonResult.newSuccess(vo);
    }
    
    /**列表记录

     * @param tabType 标签类型(0-全部/1-open/2-cancel)<br/>
     * @param status 订单状态<br/>
     * @param lastmonth 最后的月数<br/>
     * @param search 商品关键字<br/>
     * @param start 开始行号<br/>
     * @param size 记录数<br/>
     * @param sortfield 排序(+字段1/-字段名2)<br/>
     */
    @RequestMapping("/list")
    public JsonResult<List<OrderAllInfoVo>> doList(Integer tabType,OrderFilterEnum status,Integer lastmonth,String search,Integer start,Integer size,String sortfield){
    	//Long memberId = (Long)session.getAttribute(CreateOrderController.K_MemberId);
    	HttpSession sin = HttpKit.getRequest().getSession();
    	Long memberId = (Long)sin.getAttribute(CreateOrderController.K_MemberId);
        if(memberId == null || memberId.longValue()==0){
    	   return JsonResult.newFail("买家id不能为空");
        }
        List<OrderStatusEnum> orderStatus = new ArrayList<>();
        int tableType = Tools.number.nullIf(tabType, 0);
        if(tableType == 2){//cancel
        	orderStatus.addAll(getOrderStatusEnum(true));
        }
        else if(tableType ==1 ){ //open
        	if(status == null){
        		orderStatus.addAll(getOrderStatusEnum(false));	
        	}
        	else if(status.getOrderStatus()!=null){ //只要前端传过来的状态
        		orderStatus.add(status.getOrderStatus());
        	} 
        }
        else if(status!=null && status.getOrderStatus()!=null){ //全部
        	orderStatus.add(status.getOrderStatus());//只要前端传过来的状态
        }
        
        Boolean waitReview = Boolean.valueOf(status== OrderFilterEnum.WaitReview);
        Boolean afterSale =  Boolean.valueOf(status== OrderFilterEnum.AfterSale);
        List<String> orderFields = getOrderBys(sortfield); 
        if(Tools.collection.isEmpty(orderFields)){//按编号排序
        	orderFields.add(OrderInfoBo.Key.orderCode.toString() + " desc");
        }
        List<OrderInfoBo> rds = orderInfoService.getMyOrderList(memberId,orderStatus
        		, search, lastmonth,waitReview,afterSale,
        		orderFields,start, size);
        List<OrderAllInfoVo> result = new ArrayList<>(rds.size());
        
        Date systime = new Date();
        for(OrderInfoBo bo : rds){
        	OrderAllInfoVo vo  = Tools.bean.cloneBean(bo, new OrderAllInfoVo());
        	vo.setSystime(systime);
        	result.add(vo);
        }
        LoadJoinValueImpl.load(orderInfoBiz, result);
        loadCommentCount(result);
        fillOrderImage(result,true);
        loadReturnQuanByOrder(result);
        //loadOrderAddress(result);
        return JsonResult.newSuccess(result);
    }
    /** 
     * 加载商品信息及图片信息 
     * @param orderBos
     * @param loadChildrenGoods 如果是主表，就自动把子表的挂到主表下面
     * @return
     */
    private List<OrderAllInfoVo> fillOrderImage(List<OrderAllInfoVo> orderBos,boolean loadChildrenGoods){
    	for(OrderAllInfoVo vo : orderBos){
    		List<OrderGoodsBo> goodsBos = vo.getGoodsBos();
    		if(loadChildrenGoods && vo.getOrderType() == OrderTypeEnum.MASTER && Tools.collection.isEmpty(goodsBos)){
    			//主表需加载明细表的商品
				Wrapper<OrderInfoBo> wp = new EntityWrapper<>();
				wp.eq(OrderInfoBo.Key.pid.toString(), vo.getId());
				wp.setSqlSelect(OrderInfoBo.Key.id.toString());
				
				List<Object> bos = orderInfoBiz.selectObjs(wp);
				Wrapper<OrderGoodsBo> wpg = new EntityWrapper<>();
				wpg.in(OrderGoodsBo.Key.orderId.toString(), bos);
				goodsBos = orderGoodsBiz.selectList(wpg);
				vo.setGoodsBos(goodsBos);
			}
    		fillGoodsImage(vo.getGoodsBos());
    	}
    	return orderBos;
    }
    /** 加载商品信息及图片信息*/
    private void fillGoodsImage(List<? extends OrderGoodsBo> goodsBos){
    	if(Tools.collection.isEmpty(goodsBos)){
			return ;
		}
		
		for(OrderGoodsBo g :goodsBos){
			 String desc = GoodsUtil.getCdnFullPaths(g.getImgDesc());
			 g.setImgDesc(desc);
			 
			 String major = GoodsUtil.getCdnFullPaths(g.getImgMajor());
			 g.setImgMajor(major);
			 
			 String title = GoodsUtil.getCdnFullPaths(g.getImgTitle());
			 g.setImgTitle(title);
			 
			 String sku = GoodsUtil.getCdnFullPaths(g.getImgSku());
			 g.setImgSku(sku);
		}
    }
    /**加载评论次数*/
    private void loadCommentCount(List<OrderAllInfoVo> orderBos){
    	if(Tools.collection.isEmpty(orderBos)){
    		return ;
    	}
    	List<Long> orderIds = new ArrayList<>();
    	for(OrderAllInfoVo vo : orderBos){
    		orderIds.add(vo.getId());
    	}
    	Wrapper<GoodsCommentBo> wp = new EntityWrapper<>();
    	wp.setSqlSelect(GoodsCommentBo.Key.orderId.toString());
    	wp.in(GoodsCommentBo.Key.orderId.toString(), orderIds);
    	List<GoodsCommentBo> rds = goodsCommentBiz.selectList(wp);
    	for(GoodsCommentBo c : rds){
    		Long orderId = c.getOrderId();
    		OrderAllInfoVo vo = Tools.collection.getBo(orderBos, orderId);
    		int count = Tools.number.nullIf(vo.getCommentCount(), 0) +1;
    		vo.setCommentCount(Integer.valueOf(count));
    	}
    }
    /**加载退货数量(按订单合计)*/
    private void loadReturnQuanByOrder(List<OrderAllInfoVo> orderBos){
    	int size = orderBos.size();
    	if(size ==0){
    		return ;
    	}
    	Map<Long,OrderAllInfoVo> orderMp =new HashMap<>(orderBos.size()); 
    	for(OrderAllInfoVo vo : orderBos){ //没有支付时不能退货，所以不用处理拆单
    		orderMp.put(vo.getId(),vo);
    	}
    	List<ReturnsGoodsStatusEnum> status = getReturnsGoodsEnumALL();
    	Map<Long,Integer> mp = orderReturnsService.getReturnQuanByOrder(orderMp.keySet(), status);
    	for(Map.Entry<Long,Integer> e: mp.entrySet()){
    		OrderAllInfoVo vo = orderMp.get(e.getKey());
    		vo.setReturnCount(e.getValue());
    	}
    }
    /**加载退/换货数量(按订单及商品sku统计),包含loadReturnQuanByOrder的处理*/
    private void loadReturnQuanByGoods(List<OrderAllInfoVo> orderBos){
    	if(Tools.collection.isEmpty(orderBos) ){
    		return ;
    	}
    	List<Long> orderIds =new ArrayList<>(orderBos.size()); 
    	for(OrderAllInfoVo vo : orderBos){ //没有支付时不能退货，所以不用处理拆单
    		orderIds.add(vo.getId());
    	}
    	
    	List<ReturnsGoodsStatusEnum> statusIng = getReturnsGoodsEnumIng();
    	List<OrderReturnsDetailBo> ingBos = orderReturnsService.getReturnQuanByGoods(orderIds, statusIng);
    	List<ReturnsGoodsStatusEnum> statusFinish = getReturnsGoodsEnumFinish();
    	List<OrderReturnsDetailBo> finishBos = orderReturnsService.getReturnQuanByGoods(orderIds, statusFinish);
    	
    	for(OrderAllInfoVo orderVo : orderBos){
    		Long orderId = orderVo.getId();
    		List<OrderGoodsBo> goodsBos = orderVo.getGoodsBos();
    		int size = goodsBos.size();
    		List<OrderGoodsReturnVo> goodsVo = new ArrayList<>(size);
    		
    		int returnCount = 0;
    		for(OrderGoodsBo g : goodsBos){
    			OrderGoodsReturnVo v = Tools.bean.cloneBean(g, new OrderGoodsReturnVo());
    			goodsVo.add(v);
    			
    			OrderReturnsDetailBo ingBo =loadReturnQuanByGoods_findSku(ingBos,orderId, g.getSkuId());
    			if(ingBo!=null){
    				v.setReturnIng(ingBo.getRefundQty());
    				OrderReturnsBo rv = orderReturnsBiz.selectById(ingBo.getReturnsId());
    				v.setReturnIngBo(rv);
    				
    				returnCount += Tools.number.nullIf(ingBo.getRefundQty(), 0);
    			}
    			OrderReturnsDetailBo finishBo =loadReturnQuanByGoods_findSku(finishBos,orderId, g.getSkuId());
    			if(finishBo!=null){
    				v.setReturnFinish(finishBo.getRefundQty());
    				returnCount += Tools.number.nullIf(finishBo.getRefundQty(), 0);
    			}
    		}
    		
    		////////////////////////////
    		orderVo.setGoodsVos(goodsVo);
    		orderVo.setReturnCount(Integer.valueOf(returnCount));
    	}
    }
    private OrderReturnsDetailBo loadReturnQuanByGoods_findSku(
    		List<OrderReturnsDetailBo> returnDetailsBos,Long orderId,Long skuId){
    	OrderReturnsDetailBo rs = null;
    	for(OrderReturnsDetailBo vo : returnDetailsBos){
    		if(orderId.equals(vo.getOrderId()) && skuId.equals(vo.getSkuId())){
    			rs = vo;
    			break;
    		}
    	}
    	return rs;
    }
    /**取第一个商品的退货状态*/
    private void loadReturnTypeByFirstGoods(List<OrderAllInfoVo> orderBos){
    	for(OrderAllInfoVo vo :orderBos){
    		Wrapper<OrderReturnsBo> query = new EntityWrapper<>();
    		query.setSqlSelect(OrderReturnsBo.Key.returnsType.toString());
    		query.eq(OrderReturnsBo.Key.orderId.toString(), vo.getId());
    		query.last("limit 1");
    		OrderReturnsBo rbo = orderReturnsBiz.selectOne(query);
    		if(rbo!=null){
    			vo.setReturnType(rbo.getReturnsType());
    		}
    	}
    }
    
    /**拒绝后的的退货状态 */
    private List<ReturnsGoodsStatusEnum> getReturnsGoodsEnumRefuse(){
    	List<ReturnsGoodsStatusEnum> status = Collections.singletonList(
			ReturnsGoodsStatusEnum.failed
    	);
    	return status;
    }
    
	/**取有效的退货状态（包括进行及完成）*/
    private List<ReturnsGoodsStatusEnum> getReturnsGoodsEnumALL(){
    	List<ReturnsGoodsStatusEnum> ings = getReturnsGoodsEnumIng();
    	List<ReturnsGoodsStatusEnum> finish = getReturnsGoodsEnumFinish();
    	List<ReturnsGoodsStatusEnum> rds = new ArrayList<>(ings);
    	rds.addAll(finish);
    	return rds;
    }
    
    /** 退货进行的状态*/
    private List<ReturnsGoodsStatusEnum> getReturnsGoodsEnumIng(){
    	List<ReturnsGoodsStatusEnum> status = Arrays.asList(
    	/** 换货申请中 */
    			ReturnsGoodsStatusEnum.exchangeApply ,

    	/** 退货审核通过 */
    			ReturnsGoodsStatusEnum.exchangeAuditSucess,

    	/** 退货审核不通过 */
    	//exchangeAuditFail("exchangeAuditFail", "换货审核不通过"),

    	/** 待卖方收货 */
    			ReturnsGoodsStatusEnum.exchangeWaitReceived,

    	/** 验收换货退品 */
    			ReturnsGoodsStatusEnum.exchangeAcceptanceReceived,

    	/** 允许换货 */
    			ReturnsGoodsStatusEnum.allowExchange,
    	/** 拒绝换货 */
    	//refuseExchange("refuseExchange", "拒绝换货"),
    	/** 待买家收货 */
    			ReturnsGoodsStatusEnum.buyerWaitReceived,
    	/** 换货成功 */
    			ReturnsGoodsStatusEnum.exchangeSucess ,

    	/** 退款申请中 */
    			ReturnsGoodsStatusEnum.refundApply ,

    	/** 退款审核通过 */
    			ReturnsGoodsStatusEnum.refundAuditSucess ,

    	/** 退款审核不通过 */
    			//refundAuditFail("refundAuditFail", "退款审核不通过"),

    	/** 待卖方收货 */
    			ReturnsGoodsStatusEnum.refundWaitReceived ,

    	/** 验收退货退品 */
    			ReturnsGoodsStatusEnum.refundAcceptanceReceived,
    	/** 允许退货 */
    			ReturnsGoodsStatusEnum.allowRefund ,
    	/** 拒绝退货 */
    	//refuseRefund("refuseRefund", "拒绝退货退款"),

    	/** 退款成功 */
    			ReturnsGoodsStatusEnum.refundSucess)
    	;
    	return status;
    }
    /** 退货完成的状态*/
    private List<ReturnsGoodsStatusEnum> getReturnsGoodsEnumFinish(){
    	List<ReturnsGoodsStatusEnum> status = Collections.singletonList(
			ReturnsGoodsStatusEnum.finish
    	);
    	return status;
    }
    /**取有效的退货状态（包括完成）*/
    private List<ReturnsGoodsStatusEnum> getReturnsGoodsEnumSucess(){
    	List<ReturnsGoodsStatusEnum> status = Arrays.asList(
    	/** 换货申请中 */
    			ReturnsGoodsStatusEnum.exchangeApply ,

    	/** 退货审核通过 */
    			ReturnsGoodsStatusEnum.exchangeAuditSucess,

    	/** 退货审核不通过 */
    	//exchangeAuditFail("exchangeAuditFail", "换货审核不通过"),

    	/** 待卖方收货 */
    			ReturnsGoodsStatusEnum.exchangeWaitReceived,

    	/** 验收换货退品 */
    			ReturnsGoodsStatusEnum.exchangeAcceptanceReceived,

    	/** 允许换货 */
    			ReturnsGoodsStatusEnum.allowExchange,
    	/** 拒绝换货 */
    	//refuseExchange("refuseExchange", "拒绝换货"),
    	/** 待买家收货 */
    			ReturnsGoodsStatusEnum.buyerWaitReceived,
    	/** 换货成功 */
    			ReturnsGoodsStatusEnum.exchangeSucess ,

    	/** 退款申请中 */
    			ReturnsGoodsStatusEnum.refundApply ,

    	/** 退款审核通过 */
    			ReturnsGoodsStatusEnum.refundAuditSucess ,

    	/** 退款审核不通过 */
    			//refundAuditFail("refundAuditFail", "退款审核不通过"),

    	/** 待卖方收货 */
    			ReturnsGoodsStatusEnum.refundWaitReceived ,

    	/** 验收退货退品 */
    			ReturnsGoodsStatusEnum.refundAcceptanceReceived,
    	/** 允许退货 */
    			ReturnsGoodsStatusEnum.allowRefund ,
    	/** 拒绝退货 */
    	//refuseRefund("refuseRefund", "拒绝退货退款"),

    	/** 退款成功 */
    			ReturnsGoodsStatusEnum.refundSucess)
    	;
    			return status;
    }
    
    
    private List<String> getOrderBys(String sortfield){
    	List<String> rs = new ArrayList<>(3);
    	if(Tools.string.isEmpty(sortfield)){
    		return rs;
    	}
    	
		String[] fds = sortfield.split(",");
		for(String fd:fds){
			if(Tools.string.isEmpty(fd)){
				continue;
			}
			char fc =fd.charAt(0);
			boolean isAsc  = true;//默认升序
			if(fc=='-' || fc =='+'){
				isAsc = (fc =='+');
				fd = fd.substring(1);
			}
			if(isAsc){
				rs.add(fd);
			}
			else{
				rs.add(fd +  " desc");
			}
		} 
		return rs;
    }
    
    /**
     * 
     * @param isCancel true:取得无效的状态,false :取有效的状态
     * @return
     */
    private List<OrderStatusEnum> getOrderStatusEnum(boolean isCancel){
    	if(isCancel){
    		return Arrays.asList(OrderStatusEnum.CANCEL);
    	}
    	
    	List<OrderStatusEnum> rs = new ArrayList<>();
    	for(OrderStatusEnum e :OrderStatusEnum.values()){
    		if(e != OrderStatusEnum.CANCEL){
    			rs.add(e);
    		}
    	}
    	return rs;
    }
    /**
     * 取消订单
     * @param orderid 订单id<br/>
     */
    @RequestMapping("/cancel")
    public JsonResult<OrderInfoBo> doCancel(@RequestParam Long orderid){
        //createMethodSinge创建方法
        if(orderid==null){
            return JsonResult.newFail("订单id不能为空");
        }
        OrderInfoBo bo = orderInfoBiz.selectById(orderid);
        OrderStatusEnum status = bo.getOrderStatus();
        if(status == OrderStatusEnum.REVIEWADOPT_UNPAID){// status == OrderStatusEnum.CREATED_UNREVIEW){
        	OrderInfoBo rs = orderInfoService.updateCancelOrder(orderid);
        	return JsonResult.newSuccess(rs);
        }
        else{
        	return JsonResult.newFail(status.getLabel() + "的状态不能取消订单");
        }
        
    }
    /**取得订单的所有商品(包括主表/子订单商品)
     * @param orderid 订单id（主单、子单都可以）<br/> 
     */ 
    @RequestMapping("/goodslist")
    public JsonResult<List<OrderGoodsBo>> orderGoodsList(@RequestParam Long orderid){
    	List<OrderGoodsBo> rds = getOrderGoodsList(orderid);
    	return JsonResult.newSuccess(rds);
    }
    
    /**取得订单的所有商品(包括主表/子订单商品)
     * @param orderid 订单id（主单、子单都可以）<br/> 
     */
    private List<OrderGoodsBo> getOrderGoodsList(Long orderid){
        List<Object> orderIds ;{
	        Wrapper<OrderInfoBo> wp = new EntityWrapper<>();
	        wp.eq(OrderInfoBo.Key.id.toString(), orderid);
	        wp.or().eq(OrderInfoBo.Key.pid.toString(), orderid);
	        wp.setSqlSelect(OrderInfoBo.Key.id.toString());
	        orderIds = orderInfoBiz.selectObjs(wp);
        }
        List<OrderGoodsBo> rs ;
        if(Tools.collection.isEmpty(orderIds)){
        	rs = Collections.emptyList();
        }
        else{
        	Wrapper<OrderGoodsBo> wp = new EntityWrapper<>();
	        wp.in(OrderGoodsBo.Key.orderId.toString(), orderIds);
	        rs = orderGoodsBiz.selectList(wp);
	        this.fillGoodsImage(rs);
        }
    	return rs;
    }
    /**
     * 取得当前用户的余额
     * @return
     */
    @RequestMapping("/advance")
    public JsonResult<MemberBo> getAdvance( ){
    	HttpSession sin =HttpKit.getRequest().getSession();
    	Long memberId = (Long)sin.getAttribute(CreateOrderController.K_MemberId);
        if(memberId == null || memberId.longValue()==0){
    	   return JsonResult.newFail("买家id不能为空");
        }
        
        MemberBo bo = memberBiz.selectById(memberId);
    	return JsonResult.newSuccess(bo);
    }
    
    /**
     * 订单收货
     * @return
     */
    @RequestMapping("/receiver")
    public JsonResult<OrderInfoBo> doReceiver(Long orderid){
    	try{
	    	OrderInfoBo bo = orderInfoService.updateOrderReceiver(orderid);
	    	return JsonResult.newSuccess(bo);
    	}
    	catch(RuntimeException e){
    		log.error("订单收货",e);
    		return JsonResult.newFail("订单收货失败");
    	}
    }
}