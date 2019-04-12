package cn.farwalker.ravv.service.order.returns.model;

import java.io.Serializable;
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
import com.cangwu.frame.orm.ddl.annotation.DDLColumn;
import com.cangwu.frame.orm.ddl.annotation.DDLTable;

import cn.farwalker.ravv.service.order.returns.constants.ReturnsGoodsStatusEnum;

/**
 * 退货操作日志
 * 
 * @author generateModel.java
 */
@TableName(OrderReturnLogBo.TABLE_NAME)
@DDLTable(name = OrderReturnLogBo.TABLE_NAME, comment = "退货操作日志")
public class OrderReturnLogBo extends Model<OrderReturnLogBo> implements BaseBo {
	/** 字段名枚举 */
	public static enum Key implements IFieldKey {
		id("id"), behavior("behavior"), gmtCreate("gmt_create"), gmtModified("gmt_modified"), logText(
				"log_text"), operationTime("operation_time"), operatorId(
						"operator_id"), operatorName("operator_name"), orderId("order_id"),

		remark("remark"), result("result"), returnId("return_id"), returnStatus("return_status");
		private final String column;

		private Key(String k) {
			this.column = k;
		}

		@Override
		public String toString() {
			return column;
		}
	}

	/** 数据表名:order_return_log */
	public static final String TABLE_NAME = "order_return_log";
	private static final long serialVersionUID = 1793975123L;

	@TableId(value = "id", type = IdType.ID_WORKER)
	@DDLColumn(name = "id", comment = "订单操作日志ID")
	private Long id;

	@TableField("behavior")
	@DDLColumn(name = "behavior", comment = "操作行为", length = 127)
	private String behavior;

	@TableField(value = "gmt_create", strategy = FieldStrategy.NOT_EMPTY, fill = FieldFill.INSERT)
	@DDLColumn(name = "gmt_create", comment = "创建时间")
	private Date gmtCreate;

	@TableField(value = "gmt_modified", strategy = FieldStrategy.NOT_EMPTY, fill = FieldFill.INSERT_UPDATE)
	@DDLColumn(name = "gmt_modified", comment = "修改时间")
	private Date gmtModified;

	@TableField("log_text")
	@DDLColumn(name = "log_text", comment = "操作内容", length = 2000)
	private String logText;

	@TableField("operation_time")
	@DDLColumn(name = "operation_time", comment = "操作时间")
	private Date operationTime;

	@TableField("operator_id")
	@DDLColumn(name = "operator_id", comment = "操作人ID")
	private Long operatorId;

	@TableField("operator_name")
	@DDLColumn(name = "operator_name", comment = "操作人姓名", length = 255)
	private String operatorName;

	@TableField("order_id")
	@DDLColumn(name = "order_id", comment = "订单ID")
	private Long orderId;

	@TableField("remark")
	@DDLColumn(name = "remark", comment = "备注", length = 2000)
	private String remark;

	@TableField("result")
	@DDLColumn(name = "result", comment = "操作结果", length = 127)
	private String result;

	@TableField("return_id")
	@DDLColumn(name = "return_id", comment = "退货id")
	private Long returnId;

	@TableField("return_status")
	@DDLColumn(name = "return_status", comment = "退货状态")
	private ReturnsGoodsStatusEnum returnStatus;

	/** 订单操作日志ID */
	public Long getId() {
		return id;
	}

	/** 订单操作日志ID */
	public void setId(Long id) {
		this.id = id;
	}

	/** 操作行为 */
	public String getBehavior() {
		return behavior;
	}

	/** 操作行为 */
	public void setBehavior(String behavior) {
		this.behavior = behavior;
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

	/** 操作内容 */
	public String getLogText() {
		return logText;
	}

	/** 操作内容 */
	public void setLogText(String logText) {
		this.logText = logText;
	}

	/** 操作时间 */
	public Date getOperationTime() {
		return operationTime;
	}

	/** 操作时间 */
	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}

	/** 操作人ID */
	public Long getOperatorId() {
		return operatorId;
	}

	/** 操作人ID */
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	/** 操作人姓名 */
	public String getOperatorName() {
		return operatorName;
	}

	/** 操作人姓名 */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	/** 订单ID */
	public Long getOrderId() {
		return orderId;
	}

	/** 订单ID */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	/** 备注 */
	public String getRemark() {
		return remark;
	}

	/** 备注 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/** 操作结果 */
	public String getResult() {
		return result;
	}

	/** 操作结果 */
	public void setResult(String result) {
		this.result = result;
	}

	/** 退货id */
	public Long getReturnId() {
		return returnId;
	}

	/** 退货id */
	public void setReturnId(Long returnId) {
		this.returnId = returnId;
	}

	@Override
	protected Serializable pkVal() {
		return id;
	}

	public ReturnsGoodsStatusEnum getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(ReturnsGoodsStatusEnum returnStatus) {
		this.returnStatus = returnStatus;
	}
}