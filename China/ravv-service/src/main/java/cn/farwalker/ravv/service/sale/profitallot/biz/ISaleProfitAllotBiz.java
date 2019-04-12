package cn.farwalker.ravv.service.sale.profitallot.biz;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;
import cn.farwalker.ravv.service.sale.profitallot.constants.DistStatusEnum;
import cn.farwalker.ravv.service.sale.profitallot.model.ProfitAllotInfoVo;
import cn.farwalker.ravv.service.sale.profitallot.model.SaleProfitAllotBo;

/**
 * 订单利润分配<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface ISaleProfitAllotBiz extends IService<SaleProfitAllotBo>{
	
	/**
	 * 创建分润记录
	 * @param OrderPayList 订单支付列表
	 * @param buyerId 买家（会员）id
	 * @return
	 */
	Boolean setProfitAllot(List<OrderPaymemtBo> orderPayList, Long buyerId);
	
	/**
	 * 订单关闭后更新最终分润金额
	 * @param orderIdList 订单id列表
	 * @return
	 */
	Boolean updateFinalProfit(List<OrderInfoBo> orderInfoList);
	
	/**
	 * 根据订单id获取分销记录
	 * @param orderId
	 * @return
	 */
	List<SaleProfitAllotBo> profitByOrderId(Long orderId);
	
	/**
	 * 获取会员某一类状态的分销记录
	 * @param memberId 会员id
	 * @param status 分销状态
	 * @return
	 */
	List<SaleProfitAllotBo> getProfitByStatus(Long memberId, DistStatusEnum status);
	
	/**
	 * 获取会员分销待返现总金额
	 * @param memberId 会员id
	 * @return
	 */
	BigDecimal getAwaitingAmount(Long memberId);

	BigDecimal getReturnedAmount(Long memberId);
	
	/**
	 * 获取某一分销状态会员分润信息列表
	 * @param memberId
	 * @param status
	 * @param start
	 * @param size
	 * @return
	 */
	Page<ProfitAllotInfoVo> profitAllotInfoPage(Long memberId, DistStatusEnum status, Integer start, Integer size);
	
	/**
	 * 获取某一分销状态会员某月分润信息列表
	 * @param memberId
	 * @param status
	 * @param start
	 * @param size
	 * @return
	 */
	Page<ProfitAllotInfoVo> getMonthRebatedPage(Long memberId, Date month, DistStatusEnum status, Integer start, Integer size);
}