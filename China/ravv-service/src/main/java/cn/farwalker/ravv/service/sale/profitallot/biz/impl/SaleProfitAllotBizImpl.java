package cn.farwalker.ravv.service.sale.profitallot.biz.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;

import cn.farwalker.ravv.service.member.basememeber.biz.IMemberBiz;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderStatusEnum;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.order.paymemt.biz.IOrderPaymemtBiz;
import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsBiz;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsGoodsStatusEnum;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsBo;
import cn.farwalker.ravv.service.sale.profitallot.biz.ISaleProfitAllotBiz;
import cn.farwalker.ravv.service.sale.profitallot.constants.DistStatusEnum;
import cn.farwalker.ravv.service.sale.profitallot.dao.ISaleProfitAllotDao;
import cn.farwalker.ravv.service.sale.profitallot.model.ProfitAllotInfoVo;
import cn.farwalker.ravv.service.sale.profitallot.model.SaleProfitAllotBo;
import cn.farwalker.ravv.service.sys.param.biz.ISysParamBiz;
import cn.farwalker.ravv.service.sys.param.constants.DistributionLevelEnum;
import cn.farwalker.ravv.service.sys.param.model.SysParamBo;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单利润分配<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
@Service
public class SaleProfitAllotBizImpl extends ServiceImpl<ISaleProfitAllotDao, SaleProfitAllotBo>
		implements ISaleProfitAllotBiz {

	@Resource
	private IMemberBiz memberBiz;
	
	@Resource
	private ISysParamBiz sysParamBiz;

	@Resource
	private ISaleProfitAllotBiz saleProfitAllotBiz;
	
	@Resource
	private ISaleProfitAllotDao saleProfitAllotDao;
	
	@Resource
	private IOrderPaymemtBiz orderPaymemtBiz;
	
	@Resource
	private IOrderReturnsBiz orderReturnsBiz;

	@Override
	public Boolean setProfitAllot(List<OrderPaymemtBo> orderPayList, Long buyerId) {
		// 获取所有买家推荐人id(目前只往上找三级)
		MemberBo member = memberBiz.selectById(buyerId);
		if (null == member) {
			return false;
		}
		
		String reReferalCode = member.getReferrerReferalCode();
		if (Tools.string.isEmpty(reReferalCode)) {
			return false;
		}
		Boolean flag = true;
		Integer count = 0;
		List<Long> referalIdList = new ArrayList<>();
		while (flag) {
			MemberBo referalMember = memberBiz.memberByReferral(reReferalCode);
			if(null == referalMember) {
				break;
			}
			
			reReferalCode = referalMember.getReferrerReferalCode();
			referalIdList.add(referalMember.getId());
			count++;
			
			//RAVV系统名称
			if(Tools.string.isEmpty(reReferalCode) || reReferalCode.equals("RAVV") || count == 3) {
				flag = false;
			}
		}

		// 获取所有分润层级信息
		Wrapper<SysParamBo> distWrap = new EntityWrapper<>();
		distWrap.where("`code` = '" + DistributionLevelEnum.First_Distribution.getKey() 
		+ "'OR `code` = '" + DistributionLevelEnum.Second_Distribution.getKey() 
		+ "'OR `code` = '" + DistributionLevelEnum.Third_Distribution.getKey() + "'");
		List<SysParamBo> distLevelList = sysParamBiz.selectList(distWrap);
		if(CollectionUtils.isEmpty(distLevelList)) {
			return false;
		}
		
		//各分销等级的分销比例
		String distProportion[] = {"","",""};	
		for(SysParamBo distLevel : distLevelList) {
			if(distLevel.getCode().equals(DistributionLevelEnum.First_Distribution.getKey())) {
				distProportion[0] = distLevel.getPvalue();
			}else if(distLevel.getCode().equals(DistributionLevelEnum.Second_Distribution.getKey())){
				distProportion[1] = distLevel.getPvalue();
			}else if(distLevel.getCode().equals(DistributionLevelEnum.Third_Distribution.getKey())){
				distProportion[2] = distLevel.getPvalue();
			}
		}
		
		//创建分销记录
		for(int i = 0;i < referalIdList.size();i++) {
			if(Tools.string.isEmpty(distProportion[i]) || Double.valueOf(distProportion[i]) <= 0.0) {
				break;
			}
			for (OrderPaymemtBo orderPay : orderPayList) {
				SaleProfitAllotBo profitAllot = new SaleProfitAllotBo();
				profitAllot.setOrderId(orderPay.getOrderId());
				profitAllot.setMemberId(referalIdList.get(i));
				profitAllot.setAllotLevel(String.valueOf(i + 1));
				profitAllot.setSubordinateId(buyerId);
				profitAllot.setStatus(DistStatusEnum.Pending_Return);
				
				//计算分销金额，只保留小数点后两位
				BigDecimal dist = new BigDecimal(distProportion[i]);
				profitAllot.setProportion(dist);
				
				BigDecimal amt = dist.multiply(orderPay.getShouldPayTotalFee());
				//四舍五入，保留小数点后两位
				profitAllot.setAmt(amt.setScale(2, RoundingMode.HALF_UP));
				
				saleProfitAllotBiz.insert(profitAllot);
			}
		}

		return true;
	}

	@Override
	public Boolean updateFinalProfit(List<OrderInfoBo> orderInfoList) {
		if(Tools.collection.isEmpty(orderInfoList)) {
			return false;
		}
		for(OrderInfoBo orderInfo : orderInfoList) {
			//获取该订单的分销记录
			List<SaleProfitAllotBo> profitList = saleProfitAllotBiz.profitByOrderId(orderInfo.getId());
			
			if(CollectionUtils.isNotEmpty(profitList)) {
				if(orderInfo.getOrderStatus() == OrderStatusEnum.CANCEL) {
					this.updateSaleProfit(profitList, null, orderInfo.getId());
				}
				else {
					//获取订单支付信息
					OrderPaymemtBo orderPay = orderPaymemtBiz.orderPayByOrderId(orderInfo.getId());
					BigDecimal totalFee = orderPay.getShouldPayTotalFee();
					
					//获取该订单的退货单列表并计算买家最终支付金额
					List<OrderReturnsBo> orderReturnsList = orderReturnsBiz.returnsByOrderId(orderInfo.getId());
					if(CollectionUtils.isNotEmpty(orderReturnsList)) {
						for(OrderReturnsBo orderReturns : orderReturnsList) {
							ReturnsGoodsStatusEnum returnsStatus = orderReturns.getStatus();
							if(returnsStatus.equals(ReturnsGoodsStatusEnum.refundSucess)) {
								totalFee = totalFee.subtract(orderReturns.getAdjustFee());
							}
						}
					}
					this.updateSaleProfit(profitList, totalFee, orderInfo.getId());
				}
			}
		}
		
		return true;
	}
	
	/**
	 * 更新分销记录状态和最终分润金额，并存储修改账户总金额和累计分销金额
	 * @param profitList 分销记录列表
	 * @param totalFee 订单减去退货成功的金额后，买家最终支付金额
	 * @param orderId 订单id
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Boolean updateSaleProfit(List<SaleProfitAllotBo> profitList, BigDecimal totalFee, Long orderId) {
		for(SaleProfitAllotBo profit : profitList) {
			if(null != totalFee) {
				BigDecimal profitAmt = totalFee.multiply(profit.getProportion());
				profit.setAmt(profitAmt.setScale(2, RoundingMode.HALF_UP));
				profit.setStatus(DistStatusEnum.Returned);
			}else {
				profit.setStatus(DistStatusEnum.Cancel);
			}
			
			Boolean rs = saleProfitAllotBiz.updateById(profit);
			
			//将分销金额添加到账户余额和累计分销金额
			if(rs && null != totalFee) {
				memberBiz.addProfitToAdvance(profit.getMemberId(), orderId, profit.getAmt());
			}
		}
		return true;
	}

	@Override
	public List<SaleProfitAllotBo> profitByOrderId(Long orderId) {
		
		Wrapper<SaleProfitAllotBo> wrapper = new EntityWrapper<>();
		wrapper.eq(SaleProfitAllotBo.Key.orderId.toString(), orderId);
		
		return saleProfitAllotBiz.selectList(wrapper);
	}

	@Override
	public BigDecimal getAwaitingAmount(Long memberId) {
		
		return getTotal(memberId, DistStatusEnum.Pending_Return);
	}

	@Override
	public BigDecimal getReturnedAmount(Long memberId) {
		return getTotal(memberId, DistStatusEnum.Returned);
	}

	@Override
	public List<SaleProfitAllotBo> getProfitByStatus(Long memberId, DistStatusEnum status) {
		Wrapper<SaleProfitAllotBo> wrapper = new EntityWrapper<>();
		wrapper.eq(SaleProfitAllotBo.Key.memberId.toString(), memberId);
		wrapper.eq(SaleProfitAllotBo.Key.status.toString(), status);
		
		return saleProfitAllotBiz.selectList(wrapper);
	}

	@Override
	public Page<ProfitAllotInfoVo> profitAllotInfoPage(Long memberId, DistStatusEnum status, Integer start,
			Integer size) {
		
		if (Tools.number.nullIf(start, 0) <= 0) {
			start = Integer.valueOf(0);
		}
		if (Tools.number.nullIf(size, 0) == 0) {
			size = Integer.valueOf(10);
		}
		
		Page<ProfitAllotInfoVo> profitAllotInfoPage = new Page<>(start, size);
		
		List<ProfitAllotInfoVo> profitAllotInfoList = saleProfitAllotDao.profitAllotInfoList(profitAllotInfoPage, memberId, status.getKey());
		//补全图片全路径
		if(Tools.collection.isNotEmpty(profitAllotInfoList)) {
			for(ProfitAllotInfoVo allotInfo : profitAllotInfoList) {
				allotInfo.setAvator(QiniuUtil.getFullPath(allotInfo.getAvator()));
			}
		}
		
		return profitAllotInfoPage.setRecords(profitAllotInfoList);
	}

	@Override
	public Page<ProfitAllotInfoVo> getMonthRebatedPage(Long memberId, Date month, DistStatusEnum status, Integer start, Integer size) {
    	if (Tools.number.nullIf(start, 0) <= 0) {
			start = Integer.valueOf(0);
		}
		if (Tools.number.nullIf(size, 0) == 0) {
			size = Integer.valueOf(10);
		}
		
		// 获取所选月份最后一天
    	Calendar calendar = Calendar.getInstance();
		calendar.setTime(month);
		// 获取所选月最大天数
		int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		// 设置日历中月份的最大天数
		calendar.set(Calendar.DAY_OF_MONTH, lastDay);
		// 所选月份最后一天0点增加24小时
		calendar.set(Calendar.HOUR_OF_DAY, 24);
		
		Page<ProfitAllotInfoVo> allotInfoVoPage = new Page<>(start, size);
		
		List<ProfitAllotInfoVo> allotInfoVoList =  saleProfitAllotDao.getMonthRebatedlist(allotInfoVoPage, memberId, status.getKey(), month, calendar.getTime());
		//补全图片全路径
		if(Tools.collection.isNotEmpty(allotInfoVoList)) {
			for(ProfitAllotInfoVo allotInfo : allotInfoVoList) {
				allotInfo.setAvator(QiniuUtil.getFullPath(allotInfo.getAvator()));
			}
		}
		
		return allotInfoVoPage.setRecords(allotInfoVoList);
	}




	private BigDecimal getTotal(Long memberId, DistStatusEnum status){
		List<SaleProfitAllotBo> profitList = saleProfitAllotBiz.getProfitByStatus(memberId, status);
		BigDecimal totalAmount = new BigDecimal(0.00);
		if(Tools.collection.isEmpty(profitList)) {
			return totalAmount;
		}

		//统计待返现总金额
		for(SaleProfitAllotBo profit : profitList) {
			totalAmount = totalAmount.add(profit.getAmt());
		}

		return totalAmount;
	}

}