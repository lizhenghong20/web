package cn.farwalker.ravv.profitallot.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cn.farwalker.ravv.service.sale.profitallot.model.ProfitAllotInfoVo;

/**
 * 分销按月分类列表信息
 * @author chensl
 *
 */
public class RebatedMonthVo {
	
	/** 日期 */
	private Date date;
	
	/** 月金额总计 */
	private BigDecimal amtMonthTotal;
	
	/** 分销信息列表 */
	private List<ProfitAllotInfoVo> profitAllotInfoList;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<ProfitAllotInfoVo> getProfitAllotInfoList() {
		return profitAllotInfoList;
	}

	public void setProfitAllotInfoList(List<ProfitAllotInfoVo> profitAllotInfoList) {
		this.profitAllotInfoList = profitAllotInfoList;
	}

	public BigDecimal getAmtMonthTotal() {
		return amtMonthTotal;
	}

	public void setAmtMonthTotal(BigDecimal amtMonthTotal) {
		this.amtMonthTotal = amtMonthTotal;
	}
	
	
}
