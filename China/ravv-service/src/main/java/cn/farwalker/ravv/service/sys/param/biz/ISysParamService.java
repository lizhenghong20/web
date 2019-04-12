package cn.farwalker.ravv.service.sys.param.biz;

/**
 * 系统参数<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface ISysParamService  {
	/** 取订单关闭天数(正数)*/
	public Integer getOrderCloseDay();
	/** 取没有支付的订单解除冻结时间(分钟)*/
	public Integer getOrderUnfreezeTime();
}