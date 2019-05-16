package cn.farwalker.ravv.service.order.orderinfo.biz.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import cn.farwalker.ravv.service.member.pam.member.biz.IPamMemberBiz;
import cn.farwalker.ravv.service.member.pam.member.model.PamMemberBo;
import cn.farwalker.ravv.service.paypal.PaymentForm;
import cn.farwalker.ravv.service.paypal.PaymentResultVo;
import cn.farwalker.ravv.service.shipstation.biz.IShipStationService;
import cn.farwalker.waka.core.HttpKit;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import com.baomidou.mybatisplus.mapper.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.farwalker.ravv.service.member.accountflow.biz.IMemberAccountFlowBiz;
import cn.farwalker.ravv.service.member.accountflow.constants.ChargeSourceEnum;
import cn.farwalker.ravv.service.member.accountflow.constants.ChargeTypeEnum;
import cn.farwalker.ravv.service.member.accountflow.model.MemberAccountFlowBo;
import cn.farwalker.ravv.service.member.basememeber.biz.IMemberBiz;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.order.constants.PayStatusEnum;
import cn.farwalker.ravv.service.order.constants.PaymentPlatformEnum;
import cn.farwalker.ravv.service.order.operationlog.biz.IOrderLogService;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoBiz;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInventoryService;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderPaymentService;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderStatusEnum;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.order.paymemt.biz.IOrderPaymemtBiz;
import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;
import cn.farwalker.ravv.service.payment.paymentlog.biz.IMemberPaymentLogBiz;
import cn.farwalker.ravv.service.payment.paymentlog.model.MemberPaymentLogBo;
import cn.farwalker.ravv.service.sys.message.biz.ISystemMessageService;
import cn.farwalker.waka.util.Tools;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderPaymentServiceImpl implements IOrderPaymentService{
	private static final Logger log = LoggerFactory.getLogger(OrderPaymentServiceImpl.class);
	@Resource
	private IMemberPaymentLogBiz paymentLogBiz;
	@Resource
	private IOrderInfoBiz orderInfoBiz; 
	 
	
	@Resource
	private IOrderPaymemtBiz paymentBiz;
	
	@Resource
	private IMemberAccountFlowBiz memberAccountFlowBiz;
 
	@Resource
	private IOrderLogService orderLogService;
	
	@Resource
	private IOrderInventoryService orderInfoServer;
	@Resource
	private IMemberBiz memberBiz;
	@Resource
	private ISystemMessageService systemMessageService;

	@Autowired
	private IShipStationService iShipStationService;

	@Autowired
	private IMemberPaymentLogBiz iMemberPaymentLogBiz;

	@Autowired
	private IPamMemberBiz iPamMemberBiz;
	
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Boolean updatePaymentCallback(Long orderId, PaymentPlatformEnum platform, MemberPaymentLogBo paylogBo){
		log.info("系统支付回调:" + orderId + "," + Tools.json.toJson(paylogBo));


		Wrapper<OrderPaymemtBo> qlog = new EntityWrapper<>();
		qlog.eq(OrderPaymemtBo.Key.orderId.toString(), orderId);
		qlog.last("limit 1");
			
		OrderPaymemtBo payBo = paymentBiz.selectOne(qlog);
		if(payBo.getPayStatus() == PayStatusEnum.PAID || payBo.getPayStatus() == PayStatusEnum.REFUND){
				log.error("支付交易号已支付成功，可能重复回调:" + orderId);
				return Boolean.FALSE;
		}

		orderLogService.createLog(orderId, "系统支付回调", "订单支付回调","支付logid=" + paylogBo.getId());
		boolean up = updatePaymentSuccess(orderId,platform);
		if(!up){
			return Boolean.FALSE;
		}
		
		BigDecimal amt = paylogBo.getTotalAmount();
		updateGoodsSuccess(orderId);
		if(platform == PaymentPlatformEnum.Advance){//余额支付
			Long memberId = paylogBo.getMemberId();
			if(memberId == null || memberId.longValue() ==0){
				throw new WakaException("余额支付时,会员id不能为空");
			}
			createAccountFlow(orderId, memberId, amt);
		}
		
		return Boolean.valueOf(up);
	}


	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public JsonResult<PaymentResultVo> updatePayFromAdvance(PaymentForm paymentForm){

		HttpSession sin = HttpKit.getRequest().getSession();
		final Long memberId = (Long)sin.getAttribute("memberId");
		if(memberId == null || memberId.longValue()==0){
			throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
		}
		paymentForm.setMemberId(memberId);

		//核查密码
//		if(!verifyPayPassword(paymentForm.getMemberId(),paymentForm.getPayPassword())){
//			throw new WakaException(RavvExceptionEnum.INCORRECT_PASSWORD);
//		}

		BigDecimal total =  Tools.bigDecimal.add(paymentForm.getSubTotal(),paymentForm.getShipping(),paymentForm.getTax()).setScale(2, BigDecimal.ROUND_HALF_UP);
		if(!total.equals(paymentForm.getOrderTotal().setScale(2,BigDecimal.ROUND_HALF_UP)))
			throw new WakaException("生成支付订单的金额不合法");
		MemberPaymentLogBo paylogBo = new MemberPaymentLogBo();

		paylogBo.setPayType(PaymentPlatformEnum.Advance);
		paylogBo.setStatus(PayStatusEnum.PAID);
		paylogBo.setMemberId(memberId);
		paylogBo.setIp(HttpKit.getIp());
		paylogBo.setTotalAmount(total);
		paylogBo.setOrderId(paymentForm.getOrderId());
		paylogBo.setPayedTime(new Date());
		paylogBo.setShipping(paymentForm.getShipping());
		paylogBo.setTax(paymentForm.getTax());
		paylogBo.setSubtotal(paymentForm.getSubTotal());

		MemberBo memberBo = memberBiz.selectById(memberId);
		if(memberBo == null){
			throw new WakaException("查询不到当前用户余额信息");
		}

		BigDecimal advance	= memberBo.getAdvance() == null ? BigDecimal.ZERO : memberBo.getAdvance();
		//检查能否减去余额,要算上冻结余额
		if(total.compareTo(advance.subtract(memberBo.getAdvanceFreeze() == null ? BigDecimal.ZERO : memberBo.getAdvanceFreeze())) >= 0){
			throw new WakaException("支付金额(" + total + ")不能大于账户余额(" + memberBo.getAdvance() +")");
		}
		//更新余额和支付日志，逻辑上相当于执行支付
		updateAdvanceAndPaymentLog(memberId,advance,paylogBo);

		Boolean rd = updatePaymentCallback(paymentForm.getOrderId(), PaymentPlatformEnum.Advance, paylogBo);
		PaymentResultVo resultVo = new PaymentResultVo();
		resultVo.setPayType(PaymentPlatformEnum.Advance.getKey());
		resultVo.setOrderId(paymentForm.getOrderId());
		if(rd){
			return JsonResult.newSuccess(resultVo);
		}else{
			return JsonResult.newFail(null);
		}


	}


   	public Boolean isOrderValidForPay(Long orderId){
		if(orderId == null || orderId < 0){
			return false;
		}
		OrderInfoBo query = 	orderInfoBiz.selectById(orderId);
	   if(query == null){
		   return false;
	   }

	   if(query.getOrderStatus().equals(OrderStatusEnum.REVIEWADOPT_UNPAID)){
		   return true;
	   }

	   return false;
	}

	/**
	 * 创建支付流水
	 * @param amt 订单支付金额
	 * @return
	 */
	private MemberAccountFlowBo createAccountFlow(Long orderId, Long memberId, BigDecimal amt){
		MemberBo bo = memberBiz.selectById(memberId);
        if(amt.compareTo(bo.getAdvance())==1){
        	throw new WakaException("支付金额(" + amt + ")不能大于账户余额(" + bo.getAdvance() +")");
        }
        
        BigDecimal amt2 = new BigDecimal(0); 
        amt2 = amt2.subtract(amt);//转为负数
        MemberAccountFlowBo flowBo = new MemberAccountFlowBo();
        flowBo.setAmt(amt2);
        
        flowBo.setAuditDate(new Date());
        flowBo.setChargesource(ChargeSourceEnum.Order);
        flowBo.setChargetype(ChargeTypeEnum.Advance);
        flowBo.setMemberId(memberId);
        
        flowBo.setOperator("sys");
        flowBo.setSources(String.valueOf(orderId));
        flowBo.setRemark("订单" + orderId + "支付" + amt);
        memberAccountFlowBiz.insert(flowBo);
        //不需要多次扣除余额，在执行逻辑支付的时候扣除即可，此处删去
//		BigDecimal advance = bo.getAdvance().subtract(amt);
//		bo.setAdvance(advance);
//		memberBiz.updateById(bo);
        return flowBo;
	}
	
	/**
	 * 更新支付成功状态（拆单时，更新所有记录）
	 * @return
	 */
	private boolean updatePaymentSuccess(Long orderId,PaymentPlatformEnum platform){
		boolean uporder;
		List<Long> orderIds ;{//拆单的所有记录
			Wrapper<OrderInfoBo> wpStatus = new EntityWrapper<>();
			wpStatus.eq(OrderInfoBo.Key.id.toString(), orderId)
				.or().eq(OrderInfoBo.Key.pid.toString(), orderId);
		
			OrderInfoBo orderBo = new OrderInfoBo();
			orderBo.setId(orderId);
			orderBo.setOrderStatus(OrderStatusEnum.PAID_UNSENDGOODS);
			uporder = orderInfoBiz.update(orderBo,wpStatus);
			
			wpStatus.setSqlSelect(OrderInfoBo.Key.id.toString());
			List<OrderInfoBo> bos = orderInfoBiz.selectList(wpStatus);
			if(Tools.collection.isEmpty(bos)){
				throw new WakaException("没有满足条件的记录");
			}
			orderIds = new ArrayList<>(bos.size());
			for(OrderInfoBo bo :bos){
				orderIds.add(bo.getId());
			}
			
			//添加订单消息
			Long memberId = bos.get(0).getBuyerId();
			systemMessageService.addOrderMessage(memberId, orderId, null, OrderStatusEnum.PAID_UNSENDGOODS, new Date());
		}
		boolean upay ;
		{
			Wrapper<OrderPaymemtBo> wrap = new EntityWrapper<>();
			wrap.in(OrderPaymemtBo.Key.orderId.toString(), orderIds);
			OrderPaymemtBo vo = new OrderPaymemtBo() ;// paymentBiz.selectOne(wrap);
			
			vo.setPayStatus(PayStatusEnum.PAID);
			vo.setPayTime(new Date());
			vo.setBuyerPaymentType(platform);
			upay = paymentBiz.update(vo,wrap);
		}
		//这里向shipstation发送订单
//		boolean upshipstation;{
//			upshipstation = iShipStationService.createShipStationOrder(orderId);
//		}
		if(upay && uporder /**&& upshipstation*/){
			return true;
		}
		else{
			log.error(orderId + "更新支付成功状态出错,订单或者支付记录更新不成功");
			return false;
		}
	}
	/**
	 * 使用线程更新库存信息
	 * 更新库存需要更新促销数量，具体参考{@link OrderCreateServiceImpl#(java.util.List, java.math.BigDecimal, Long, String)}
	 * @param
	 * @return
	 */
	private boolean updateGoodsSuccess(Long orderId){
		//使用线程更新库存信息
		Tools.thread.runSingle(new Runnable() {
			@Override
			public void run() {
				try{
					orderInfoServer.updateBuySuccessGoodsInventory(orderId);
				}
				catch(Exception e){
					log.error("更新库存信息出错",e);
				}
			}
		}, "updateGoodsSuccess");
		return true;
	}

	/**
	 * 更新余额，以及更新支付日志
	 * @param memberId
	 * @param advance
	 * @param paylogBo
     */
	private void updateAdvanceAndPaymentLog(Long memberId,BigDecimal advance,MemberPaymentLogBo paylogBo){
		//减去余额，逻辑上相当于执行支付
		MemberBo updateMemberBo = new MemberBo();
		updateMemberBo.setId(memberId);
		updateMemberBo.setAdvance(advance.subtract(paylogBo.getTotalAmount()).setScale(2,BigDecimal.ROUND_HALF_UP));
		if(!memberBiz.updateById(updateMemberBo)){
			throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
		}
		//支付完成则更新log
		if(!iMemberPaymentLogBiz.insert(paylogBo))
			throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
	}

	private Boolean verifyPayPassword(final Long memberId,final String payPassword){
		//从数据库获取密码盐
		PamMemberBo queryPam = iPamMemberBiz.selectOne(Condition.create().eq(PamMemberBo.Key.memberId.toString(),memberId));
		if(queryPam == null){
			throw new WakaException("can not find user information");
		}
		queryPam.getSalt();
		String encryptPW = Tools.md5.md5Hex(payPassword + "@" + queryPam.getSalt());

		MemberBo queryMember =  memberBiz.selectById(memberId);
		if(queryMember == null){
			throw new WakaException("can not find user information");
		}

		if(queryMember.getPayPassword().equals(encryptPW)){
			return true;
		}else{
			return false;
		}

	}

	public static void main(String[] args) {
		String pw1 = Tools.md5.md5Hex("123456");
		String salt = "(@sHHTA9troYJXlKS8niuVEX";
		String encryptPW = Tools.md5.md5Hex(pw1.toLowerCase() + "@" + salt);
		System.out.println(pw1.toLowerCase());
		System.out.println(encryptPW);
	}


}
