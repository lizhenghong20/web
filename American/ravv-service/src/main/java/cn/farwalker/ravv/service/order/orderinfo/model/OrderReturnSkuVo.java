package cn.farwalker.ravv.service.order.orderinfo.model;

import java.util.List;

import cn.farwalker.ravv.service.order.returns.constants.ReturnsTypeEnum;

/**
 * 退货的界面内容
 * 
 * @author generateModel.java
 */
public class OrderReturnSkuVo extends OrderGoodsSkuVo {
	private static final long serialVersionUID = 1800334680L;

	/**
	 * 退货原因类型（字典字段）
	 */
	private String reasonType;

	/**
	 * 退款/退货原因
	 */
	private String reason;

	/**
	 * 退回类型
	 */
	private ReturnsTypeEnum returnsType;
	
	/** 退货上传的图片 */
	private List<String> returnGoodsImgList;

	/** 退货原因 */
	public String getReason() {
		return reason;
	}

	/** 退货原因 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/** 退货类型 */
	public ReturnsTypeEnum getReturnsType() {
		return returnsType;
	}

	/** 退货类型 */
	public void setReturnsType(ReturnsTypeEnum returnsType) {
		this.returnsType = returnsType;
	}

	/** 退货原因类型 */
	public String getReasonType() {
		return reasonType;
	}

	/** 退货原因类型 */
	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
	}
	/** 退货上传的图片 */
	public List<String> getReturnGoodsImgList() {
		return returnGoodsImgList;
	}
	/** 退货上传的图片 */
	public void setReturnGoodsImgList(List<String> returnGoodsImgList) {
		this.returnGoodsImgList = returnGoodsImgList;
	}

}