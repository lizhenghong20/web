package cn.farwalker.ravv.service.merchant.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * 供应商销售量与销售额图表数据
 * @author chensl
 *
 */
public class MerchantSalesVo {
	
	/** 销售月份列表 */
	private List<String> saleDateList;
	
	/** 月销售商品数量列表 */
	private List<Integer> saleGoodsNumList;
	
	/** 月销售额列表 */
	private List<BigDecimal> saleAmountList;

	public List<String> getSaleDateList() {
		return saleDateList;
	}

	public void setSaleDateList(List<String> saleDateList) {
		this.saleDateList = saleDateList;
	}

	public List<Integer> getSaleGoodsNumList() {
		return saleGoodsNumList;
	}

	public void setSaleGoodsNumList(List<Integer> saleGoodsNumList) {
		this.saleGoodsNumList = saleGoodsNumList;
	}

	public List<BigDecimal> getSaleAmountList() {
		return saleAmountList;
	}

	public void setSaleAmountList(List<BigDecimal> saleAmountList) {
		this.saleAmountList = saleAmountList;
	}
	
}
