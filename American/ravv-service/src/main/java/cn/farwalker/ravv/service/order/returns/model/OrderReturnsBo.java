package cn.farwalker.ravv.service.order.returns.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cn.farwalker.ravv.service.order.returns.constants.ReturnsGoodsStatusEnum;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsTypeEnum;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import com.cangwu.frame.orm.core.BaseBo;
import com.cangwu.frame.orm.core.IFieldKey;
import com.cangwu.frame.orm.ddl.annotation.DDLColumn;
import com.cangwu.frame.orm.ddl.annotation.DDLTable;

/**
 * 订单退货
 * 
 * @author generateModel.java
 */
@TableName(OrderReturnsBo.TABLE_NAME)
@DDLTable(name = OrderReturnsBo.TABLE_NAME, comment = "订单退货")
public class OrderReturnsBo extends Model<OrderReturnsBo> implements BaseBo {

	 /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        adjustFee("adjust_fee"),
        buyerId("buyer_id"),
        buyerNick("buyer_nick"),
        checkTime("check_time"),
        finishTime("finish_time"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        logisticsCompany("logistics_company"),
        
        logisticsNo("logistics_no"),
        orderCode("order_code"),
        orderId("order_id"),
        refundFee("refund_fee"),
        remark("remark"),
        sellerAddress("seller_address"),
        sellerPhone("seller_phone"),
        returnsType("returns_type"),
        status("status"),
        shipmentId("shipment_id");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
	/** 数据表名:order_returns */
	public static final String TABLE_NAME = "order_returns";
	private static final long serialVersionUID = -1303123614L;

	@TableId(value = "id", type = IdType.ID_WORKER)
	@DDLColumn(name = "id", comment = "订单退货ID")
	private Long id;

	@TableField("adjust_fee")
	@DDLColumn(name = "adjust_fee", comment = "调整金额(实退金额)")
	private BigDecimal adjustFee;

	@TableField("buyer_id")
	@DDLColumn(name = "buyer_id", comment = "退货方ID")
	private Long buyerId;

	@TableField("buyer_nick")
	@DDLColumn(name = "buyer_nick", comment = "退货方昵称", length = 255)
	private String buyerNick;

	@TableField("check_time")
	@DDLColumn(name = "check_time", comment = "审核时间")
	private Date checkTime;

	@TableField("finish_time")
	@DDLColumn(name = "finish_time", comment = "退货完成时间")
	private Date finishTime;

	@TableField(value = "gmt_create", strategy = FieldStrategy.NOT_EMPTY, fill = FieldFill.INSERT)
	@DDLColumn(name = "gmt_create", comment = "创建时间")
	private Date gmtCreate;

	@TableField(value = "gmt_modified", strategy = FieldStrategy.NOT_EMPTY, fill = FieldFill.UPDATE)
	@DDLColumn(name = "gmt_modified", comment = "修改时间")
	private Date gmtModified;

	@TableField("logistics_company")
	@DDLColumn(name = "logistics_company", comment = "物流公司", length = 255)
	private String logisticsCompany;

	@TableField("logistics_no")
	@DDLColumn(name = "logistics_no", comment = "物流编号")
	private String logisticsNo;

	@TableField("order_code")
	@DDLColumn(name = "order_code", comment = "订单编号")
	private String orderCode;

	@TableField("order_id")
	@DDLColumn(name = "order_id", comment = "订单ID")
	private Long orderId;

	@TableField("refund_fee")
	@DDLColumn(name = "refund_fee", comment = "退货金额")
	private BigDecimal refundFee;

	@TableField("remark")
	@DDLColumn(name = "remark", comment = "备注", length = 2000)
	private String remark;

	@TableField("seller_address")
	@DDLColumn(name = "seller_address", comment = "收货方地址", length = 511)
	private String sellerAddress;

	@TableField("seller_phone")
	@DDLColumn(name = "seller_phone", comment = "收货方电话", length = 50)
	private String sellerPhone;

	@TableField("returns_type")
	@DDLColumn(name = "returns_type", comment = "退货类型", length = 63)
	private ReturnsTypeEnum returnsType;
	
	@TableField("status")
	@DDLColumn(name = "status", comment = "退货状态(以明细为准)", length = 63)
	private ReturnsGoodsStatusEnum status;

	@TableField("shipment_id")
    @DDLColumn(name="shipment_id",comment="物流模板id")
    private Long shipmentId;

	/** 订单退货ID */
	public Long getId() {
		return id;
	}

	/** 订单退货ID */
	public void setId(Long id) {
		this.id = id;
	}

	/** 调整金额(实退金额) */
	public BigDecimal getAdjustFee() {
		return adjustFee;
	}

	/** 调整金额(实退金额) */
	public void setAdjustFee(BigDecimal adjustFee) {
		this.adjustFee = adjustFee;
	}

	/** 退货方ID */
	public Long getBuyerId() {
		return buyerId;
	}

	/** 退货方ID */
	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	/** 退货方昵称 */
	public String getBuyerNick() {
		return buyerNick;
	}

	/** 退货方昵称 */
	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	/** 审核时间 */
	public Date getCheckTime() {
		return checkTime;
	}

	/** 审核时间 */
	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
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

	/** 物流公司 */
	public String getLogisticsCompany() {
		return logisticsCompany;
	}

	/** 物流公司 */
	public void setLogisticsCompany(String logisticsCompany) {
		this.logisticsCompany = logisticsCompany;
	}

	/** 物流编号 */
	public String getLogisticsNo() {
		return logisticsNo;
	}

	/** 物流编号 */
	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
	}

	/** 订单编号 */
	public String getOrderCode() {
		return orderCode;
	}

	/** 订单编号 */
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	/** 订单ID */
	public Long getOrderId() {
		return orderId;
	}

	/** 订单ID */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	/** 退货金额 */
	public BigDecimal getRefundFee() {
		return refundFee;
	}

	/** 退货金额 */
	public void setRefundFee(BigDecimal refundFee) {
		this.refundFee = refundFee;
	}

	/** 备注 */
	public String getRemark() {
		return remark;
	}

	/** 备注 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/** 收货方地址 */
	public String getSellerAddress() {
		return sellerAddress;
	}

	/** 收货方地址 */
	public void setSellerAddress(String sellerAddress) {
		this.sellerAddress = sellerAddress;
	}

	/** 收货方电话 */
	public String getSellerPhone() {
		return sellerPhone;
	}

	/** 收货方电话 */
	public void setSellerPhone(String sellerPhone) {
		this.sellerPhone = sellerPhone;
	}

	/** 退货状态(以明细为准) */
	public ReturnsGoodsStatusEnum getStatus() {
		return status;
	}

	/** 退货状态(以明细为准) */
	public void setStatus(ReturnsGoodsStatusEnum status) {
		this.status = status;
	}

	@Override
	protected Serializable pkVal() {
		return id;
	}

	public ReturnsTypeEnum getReturnsType() {
		return returnsType;
	}

	public void setReturnsType(ReturnsTypeEnum returnsType) {
		this.returnsType = returnsType;
	}

	public Long getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(Long shipmentId) {
		this.shipmentId = shipmentId;
	}

}