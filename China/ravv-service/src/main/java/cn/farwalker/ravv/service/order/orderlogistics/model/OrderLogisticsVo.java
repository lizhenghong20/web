package cn.farwalker.ravv.service.order.orderlogistics.model;

/**
 * 订单物流信息
 * 
 * @author generateModel.java
 */
public class OrderLogisticsVo extends OrderLogisticsBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2488162216523793225L;
     
	/**
	 * 地址全路径
	 */
	private String areaFullPath;

	public String getAreaFullPath() {
		return areaFullPath;
	}

	public void setAreaFullPath(String areaFullPath) {
		this.areaFullPath = areaFullPath;
	}
}