package cn.farwalker.ravv.service.order.returns.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.farwalker.ravv.service.order.time.OrderTaskTimer;
import cn.farwalker.waka.orm.EnumManager.IEnumJsons;
import cn.farwalker.waka.util.Tools;
import com.alibaba.fastjson.annotation.JSONType;

/**
 * 商品退货状态 有些状态需要定时器触发的 {@link OrderTaskTimer}
 * 
 * @author Administrator
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = ReturnsGoodsStatusEnumDeser.class)
public enum ReturnsGoodsStatusEnum implements IEnumJsons {
	/** 未退货/退款 **/
	normal("normal", "未退货/退款"),

	/** 换货申请中 */
	exchangeApply("exchangeApply", "换货申请中"),

	/** 换货审核通过 */
	exchangeAuditSucess("exchangeAuditSucess", "换货审核通过"),

	/** 换货审核不通过 */
	exchangeAuditFail("exchangeAuditFail", "换货审核不通过"),

	/** 待卖方收货 */
	exchangeWaitReceived("exchangeWaitReceived", "待卖方收货"),

	/** 验收换货退品 */
	exchangeAcceptanceReceived("exchangeAcceptanceReceived", "已验收换货退品"),

	/** 允许换货 */
	allowExchange("allowExchange", "允许换货"),
	/** 拒绝换货 */
	refuseExchange("refuseExchange", "拒绝换货"),
	/** 待买家收货 */
	buyerWaitReceived("buyerWaitReceived", "待买家收货"),
	/** 换货成功 */
	exchangeSucess("exchangeSucess", "换货成功"),

	/** 退款申请中 */
	refundApply("refundApply", "退货退款申请中"),

	/** 退款审核通过 */
	refundAuditSucess("refundAuditSucess", "退款审核通过"),

	/** 退款审核不通过 */
	refundAuditFail("refundAuditFail", "退款审核不通过"),

	/** 待卖方收货 */
	refundWaitReceived("refundWaitReceived", "待卖方收货"),

	/** 验收退货退品 */
	refundAcceptanceReceived("refundAcceptanceReceived", "验收退货退品"),
	/** 允许退货 */
	allowRefund("allowRefund", "允许退货退款"),
	/** 拒绝退货 */
	refuseRefund("refuseRefund", "拒绝退货退款"),

	/** 退款成功 */
	refundSucess("refundSucess", "退款成功"),

	/** 交易完成 (退/换货,退款成功完成状态) */
	finish("finish", "交易完成"),

	/** 交易关闭(拒绝后的完成状态) */
	failed("failed", "交易失败");

	private final String label, key;

	private ReturnsGoodsStatusEnum(String key, String label) {
		this.key = key;
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getKey() {
		return key;
	}

	public static ReturnsGoodsStatusEnum value(String key){
		if("normal".equals(key)){
			return normal;
		} else if("exchangeApply".equals(key)){
			return exchangeApply;
		} else if("exchangeAuditSucess".equals(key)){
			return exchangeAuditSucess;
		} else if("exchangeAuditFail".equals(key)){
			return exchangeAuditFail;
		} else if("exchangeWaitReceived".equals(key)){
			return exchangeWaitReceived;
		} else if("exchangeAcceptanceReceived".equals(key)){
			return exchangeAcceptanceReceived;
		} else if("allowExchange".equals(key)){
			return allowExchange;
		} else if("refuseExchange".equals(key)){
			return refuseExchange;
		} else if("buyerWaitReceived".equals(key)){
			return buyerWaitReceived;
		} else if("exchangeSucess".equals(key)){
			return exchangeSucess;
		} else if("refundApply".equals(key)){
			return refundApply;
		} else if("refundAuditSucess".equals(key)){
			return refundAuditSucess;
		} else if("refundAuditFail".equals(key)){
			return refundAuditFail;
		} else if("refundWaitReceived".equals(key)){
			return refundWaitReceived;
		} else if("refundAcceptanceReceived".equals(key)){
			return refundAcceptanceReceived;
		} else if("allowRefund".equals(key)){
			return allowRefund;
		} else if("refuseRefund".equals(key)){
			return refuseRefund;
		} else if("refundSucess".equals(key)){
			return refundSucess;
		} else if("finish".equals(key)){
			return finish;
		} else if("failed".equals(key)){
			return failed;
		}
		return null;
	}

	/**
	 * 属于订单失败的枚举列表
	 * 
	 * @param returnsStatus
	 * @return
	 */
	public static ReturnsGoodsStatusEnum checkAndReturnStatus(ReturnsGoodsStatusEnum returnsStatus) {
		List<ReturnsGoodsStatusEnum> failedList = Arrays.asList(
				/** 拒绝换货 */
				ReturnsGoodsStatusEnum.refuseExchange,
				/** 拒绝退货 */
				ReturnsGoodsStatusEnum.refuseRefund,
				/** 换货审核不通过 */
				ReturnsGoodsStatusEnum.exchangeAuditFail,
				/** 退货审核不通过 */
				ReturnsGoodsStatusEnum.refundAuditFail);
		List<ReturnsGoodsStatusEnum> successList = Arrays.asList(
				/** 换货成功 */
				ReturnsGoodsStatusEnum.exchangeSucess,
				/** 退款成功 */
				ReturnsGoodsStatusEnum.refundSucess);
		if (successList.contains(returnsStatus)) {
			return ReturnsGoodsStatusEnum.finish;
		} else if (failedList.contains(returnsStatus)) {
			return ReturnsGoodsStatusEnum.failed;
		}
		return returnsStatus;
	}

	public static Boolean isChecked(ReturnsGoodsStatusEnum returnsStatus) {
		List<ReturnsGoodsStatusEnum> checkedStatus = Arrays.asList(
				/** 拒绝换货 */
				ReturnsGoodsStatusEnum.exchangeAuditFail,
				/** 拒绝退货 */
				ReturnsGoodsStatusEnum.exchangeAuditSucess,
				/** 换货审核不通过 */
				ReturnsGoodsStatusEnum.refundAuditFail,
				/** 退货审核不通过 */
				ReturnsGoodsStatusEnum.refundAuditSucess);

		if (checkedStatus.contains(returnsStatus)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取换货流程状态列表
	 * 
	 * @param status
	 * @return
	 */
	public static List<ReturnsGoodsStatusEnum> getChangeGoodsStatusList(ReturnsGoodsStatusEnum status) {
		List<ReturnsGoodsStatusEnum> statusList = new ArrayList<ReturnsGoodsStatusEnum>();

		/** 未退货/退款 **/
		statusList.add(normal);

		/** 换货申请中 */
		statusList.add(exchangeApply);

		if (!status.equals(exchangeAuditFail)) {
			/** 换货审核通过 */
			statusList.add(exchangeAuditSucess);
		} else {
			/** 换货审核不通过 */
			statusList.add(exchangeAuditFail);
		}

		/** 待卖方收货 */
		statusList.add(exchangeWaitReceived);

		/** 验收换货退品 */
		statusList.add(exchangeAcceptanceReceived);

		if (!status.equals(refuseExchange)) {
			/** 允许换货 */
			statusList.add(allowExchange);
		} else {
			/** 拒绝换货 */
			statusList.add(refuseExchange);
		}

		/** 待买家收货 */
		statusList.add(buyerWaitReceived);
		/** 换货成功 */
		statusList.add(exchangeSucess);
		Integer length = statusList.size() - 1;
		List<ReturnsGoodsStatusEnum> toRemoveS = new ArrayList<>();
		for (Integer i = length; i >= 0; i--) {
			if (status.equals(statusList.get(i))) {
				break;
			} else {
				toRemoveS.add(statusList.get(i));
			}
		}
		if (Tools.collection.isNotEmpty(toRemoveS)) {
			statusList.removeAll(toRemoveS);
		}

		return statusList;
	}

	/**
	 * 获取退货流程状态列表
	 * 
	 * @param status
	 * @return
	 */
	public static List<ReturnsGoodsStatusEnum> getReGoodsStatusList(ReturnsGoodsStatusEnum status) {
		List<ReturnsGoodsStatusEnum> statusList = new ArrayList<ReturnsGoodsStatusEnum>();

		/** 未退货/退款 **/
		statusList.add(normal);

		/** 退款申请中 */
		statusList.add(refundApply);

		if (!status.equals(refundAuditFail)) {
			/** 退款审核通过 */
			statusList.add(refundAuditSucess);
		} else {
			/** 退款审核不通过 */
			statusList.add(refundAuditFail);
		}

		/** 待卖方收货 */
		statusList.add(refundWaitReceived);

		/** 验收退货退品 */
		statusList.add(refundAcceptanceReceived);

		if (!status.equals(refuseRefund)) {
			/** 允许退货 */
			statusList.add(allowRefund);
		} else {
			/** 拒绝退货 */
			statusList.add(refuseRefund);
		}

		/** 退款成功 */
		statusList.add(refundSucess);
		return statusList;
	}

	/**
	 * 获取退货不退款流程状态列表
	 * 
	 * @param status
	 * @return
	 */
	public static List<ReturnsGoodsStatusEnum> getRefundStatusList(ReturnsGoodsStatusEnum status) {
		List<ReturnsGoodsStatusEnum> statusList = new ArrayList<ReturnsGoodsStatusEnum>();

		/** 未退货/退款 **/
		statusList.add(normal);

		/** 退款申请中 */
		statusList.add(refundApply);

		if (!status.equals(refundAuditFail)) {
			/** 退款审核通过 */
			statusList.add(refundAuditSucess);
		} else {
			/** 退款审核不通过 */
			statusList.add(refundAuditFail);
		}
		/** 退款成功 */
		statusList.add(refundSucess);
		return statusList;
	}

	/**
	 * 是否允许订单关闭(符合可以用来判断订单关闭的退单状态)
	 * 
	 * @param status
	 * @return
	 */
	public static boolean allowOrderClose(ReturnsGoodsStatusEnum status) {
		if (status.equals(exchangeSucess) || status.equals(refundSucess) || status.equals(finish)) {
			// 失败不计算退货数量
			// status.equals(failed)
			return true;
		} else {
			return false;
		}
	}

}
