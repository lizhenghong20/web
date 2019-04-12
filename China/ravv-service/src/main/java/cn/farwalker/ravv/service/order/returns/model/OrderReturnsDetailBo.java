package cn.farwalker.ravv.service.order.returns.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import com.cangwu.frame.orm.core.BaseBo;
import com.cangwu.frame.orm.core.IFieldKey;
import com.cangwu.frame.orm.core.annotation.LoadJoinValue;
import com.cangwu.frame.orm.ddl.annotation.DDLColumn;
import com.cangwu.frame.orm.ddl.annotation.DDLTable;

import cn.farwalker.ravv.service.goods.base.model.GoodsBo;

/**
 * 订单退货详情
 * 
 * @author generateModel.java
 */
@TableName(OrderReturnsDetailBo.TABLE_NAME)
@DDLTable(name = OrderReturnsDetailBo.TABLE_NAME, comment = "订单退货详情")
public class OrderReturnsDetailBo extends Model<OrderReturnsDetailBo> implements BaseBo {
	/** 字段名枚举 */
	public static enum Key implements IFieldKey {
		id("id"), 
		actualRefundQty("actual_refund_qty"), 
		finishTime("finish_time"), 
		gmtCreate("gmt_create"), 
		gmtModified("gmt_modified"), 
		goodsId("goods_id"), 
		goodsName("goods_name"), 
		orderId("order_id"), 
		reasonType("reason_type"), 
		reason("reason"), 
		refundPrice("refund_price"), 
		refundQty("refund_qty"), 
		remark("remark"), 
		returnsId("returns_id"), 
//		returnsType("returns_type"), 
//		status("status"),
		skuId("sku_id"), 
		validesc("validesc"),
		imglistJson("imglistJson");
		private final String column;

		private Key(String k) {
			this.column = k;
		}

		@Override
		public String toString() {
			return column;
		}
	}

	/** 数据表名:order_returns_detail */
	public static final String TABLE_NAME = "order_returns_detail";
	private static final long serialVersionUID = -1100854829L;

	@TableId(value = "id", type = IdType.ID_WORKER)
	@DDLColumn(name = "id", comment = "退货详情ID")
	private Long id;

	@TableField("actual_refund_qty")
	@DDLColumn(name = "actual_refund_qty", comment = "实际退回数量")
	private Integer actualRefundQty;

	@TableField("finish_time")
	@DDLColumn(name = "finish_time", comment = "退货完成时间")
	private Date finishTime;

	@TableField(value = "gmt_create", strategy = FieldStrategy.NOT_EMPTY, fill = FieldFill.INSERT)
	@DDLColumn(name = "gmt_create", comment = "创建时间")
	private Date gmtCreate;

	@TableField(value = "gmt_modified", strategy = FieldStrategy.NOT_EMPTY, fill = FieldFill.INSERT_UPDATE)
	@DDLColumn(name = "gmt_modified", comment = "修改时间")
	private Date gmtModified;

	@TableField("goods_id")
	@DDLColumn(name = "goods_id", comment = "冗余商品ID")
	private Long goodsId;

	@TableField("goods_name")
	@DDLColumn(name = "goods_name", comment = "冗余商品名称", length = 255)
	private String goodsName;

	@TableField("order_id")
	@DDLColumn(name = "order_id", comment = "订单ID")
	private Long orderId;

	@TableField("reason_type")
	@DDLColumn(name = "reason_type", comment = "退货原因类型", length = 511)
	private String reasonType;

	@TableField("reason")
	@DDLColumn(name = "reason", comment = "退货原因", length = 511)
	private String reason;

	@TableField("refund_price")
	@DDLColumn(name = "refund_price", comment = "退款价")
	private BigDecimal refundPrice;

	@TableField("refund_qty")
	@DDLColumn(name = "refund_qty", comment = "申请退回数量")
	private Integer refundQty;

	@TableField("remark")
	@DDLColumn(name = "remark", comment = "备注", length = 2000)
	private String remark;

	@TableField("returns_id")
	@DDLColumn(name = "returns_id", comment = "退货单ID")
	private Long returnsId;

	@TableField("sku_id")
	@DDLColumn(name = "sku_id", comment = "SKUID")
	private Long skuId;
	
//	@TableField("returns_type")
//	@DDLColumn(name = "returns_type", comment = "退货类型", length = 63)
//	private ReturnsTypeEnum returnsType;
//	@TableField("status")
//	@DDLColumn(name = "status", comment = "退货状态", length = 63)
//	private ReturnsGoodsStatusEnum status;

	@TableField("validesc")
	@DDLColumn(name = "validesc", comment = "退货验收详情", length = 2000)
	private String validesc;
	
	@TableField("imglist_json")
	@DDLColumn(name = "imglist_json", comment = "退货相关图片列表（json格式）", length = 2000)
	private String imglistJson;

	/** 退货详情ID */
	public Long getId() {
		return id;
	}

	/** 退货详情ID */
	public void setId(Long id) {
		this.id = id;
	}

	/** 实际退回数量 */
	public Integer getActualRefundQty() {
		return actualRefundQty;
	}

	/** 实际退回数量 */
	public void setActualRefundQty(Integer actualRefundQty) {
		this.actualRefundQty = actualRefundQty;
	}

	/** 退货完成时间 */
	public Date getFinishTime() {
		return finishTime;
	}

	/** 退货完成时间 */
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	/** 创建时间 */
	public Date getGmtCreate() {
		return gmtCreate;
	}

	/** 创建时间 */
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	/** 修改时间 */
	public Date getGmtModified() {
		return gmtModified;
	}

	/** 修改时间 */
	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	/** 冗余商品ID */
	public Long getGoodsId() {
		return goodsId;
	}

	/** 冗余商品ID */
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	/** 冗余商品名称 */
	public String getGoodsName() {
		return goodsName;
	}

	/** 冗余商品名称 */
	@LoadJoinValue(by = "goodsId", table = GoodsBo.TABLE_NAME, joinfield = "id", returnfield = "goodsName")
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	/** 订单ID */
	public Long getOrderId() {
		return orderId;
	}

	/** 订单ID */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	/** 退货原因 */
	public String getReason() {
		return reason;
	}

	/** 退货原因 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/** 退款价 */
	public BigDecimal getRefundPrice() {
		return refundPrice;
	}

	/** 退款价 */
	public void setRefundPrice(BigDecimal refundPrice) {
		this.refundPrice = refundPrice;
	}

	/** 申请退回数量 */
	public Integer getRefundQty() {
		return refundQty;
	}

	/** 申请退回数量 */
	public void setRefundQty(Integer refundQty) {
		this.refundQty = refundQty;
	}

	/** 备注 */
	public String getRemark() {
		return remark;
	}

	/** 备注 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/** 退货单ID */
	public Long getReturnsId() {
		return returnsId;
	}

	/** 退货单ID */
	public void setReturnsId(Long returnsId) {
		this.returnsId = returnsId;
	}

//	/** 退货类型 */
//	public ReturnsTypeEnum getReturnsType() {
//		return returnsType;
//	}
//
//	/** 退货类型 */
//	public void setReturnsType(ReturnsTypeEnum returnsType) {
//		this.returnsType = returnsType;
//	}

	/** SKUID */
	public Long getSkuId() {
		return skuId;
	}

	/** SKUID */
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

//	/** 退货状态 */
//	public ReturnsGoodsStatusEnum getStatus() {
//		return status;
//	}
//
//	/** 退货状态 */
//	public void setStatus(ReturnsGoodsStatusEnum status) {
//		this.status = status;
//	}

	@Override
	protected Serializable pkVal() {
		return id;
	}

	/** 退货验收详情 */
	public String getValidesc() {
		return validesc;
	}

	/** 退货验收详情 */
	public void setValidesc(String validesc) {
		this.validesc = validesc;
	}

	public String getReasonType() {
		return reasonType;
	}

	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
	}

	public String getImglistJson() {
		return imglistJson;
	}

	public void setImglistJson(String imglistJson) {
		this.imglistJson = imglistJson;
	}
}