package cn.farwalker.ravv.service.merchant.biz;

import java.util.Date;
import java.util.List;

import cn.farwalker.ravv.service.merchant.model.MerchantOrderVo;
import cn.farwalker.ravv.service.merchant.model.MerchantSalesVo;

public interface IMerchantService {
	/**
	 * 订单统计
	 * @author Administrator
	 * 供应商名称，订单数（含本供应商商品的订单数量），
	 * 发货数量（按件计算），
	 * 完成订单数（含本供应商商品的订单数量，且无退换货情况），
	 * 取消订单数量（没有发货，取消的订单），
	 * 退货件数量，
	 * 换货件数量，
	 * 订单总收入（有效订单合计）
	 */
	public List<MerchantOrderVo> getMerchantOrderStats(List<Long> merchantIds,Date startdate,Date enddate);
	
	/**
	 * 获取供应商某时间范围内的销售量和销售额
	 * @param merchantId 供应商id
	 * @param startdate 开始时间
	 * @param enddate 结束时间
	 * @return
	 */
	public MerchantSalesVo getSalesStatistics(Long merchantId, Date startdate,Date enddate);
	
}
