package cn.farwalker.ravv.service.order.orderinfo.dao;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.farwalker.ravv.service.order.orderinfo.constants.OrderStatusEnum;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;

/**
 * 订单信息<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IOrderInfoDao extends BaseMapper<OrderInfoBo>{
	/**
	 * 取得个人的订单
	 * @param unpaidStatus 未支付时的状态
	 * @param paidStatus 已支付的状态
	 * @param buyerId 买家 可以null
	 * @param lastDate 最后时间 可以null
	 * @param statusList 查询状态 可以null
	 * @param search 商品名称
	 * @param orderList 排序字段
	 * @return
	 */
	public List<OrderInfoBo> getMyOrder(@Param("unpaidStatus")List<OrderStatusEnum> unpaidStatus
			,@Param("paidStatus")List<OrderStatusEnum> paidStatus
			,@Param("buyerId")Long buyerId
			,@Param("lastDate")Date lastDate
			,@Param("statusList")List<OrderStatusEnum> statusList
			,@Param("search")String search
			,@Param("waitReview")Boolean waitReview
			,@Param("afterSale") Boolean afterSale
			,@Param("orderList")List<String> orderList
			,@Param("start")Integer start 
			,@Param("size")Integer size);
}