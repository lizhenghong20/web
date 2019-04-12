package cn.farwalker.ravv.service.order.returns.model;

/**
 * 退货操作日志
 * 
 */
public class OrderReturnLogVo extends OrderReturnLogBo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8182502948749035057L;
	/**
	 * 判断当前状态为那个状态 成功：1 进行中(当前)：0 失败：-1
	 */
	private Integer success;

	/** 1 进行中(当前)：0 失败：-1 */
	public Integer getSuccess() {
		return success;
	}

	/** 1 进行中(当前)：0 失败：-1 */
	public void setSuccess(Integer success) {
		this.success = success;
	}
}