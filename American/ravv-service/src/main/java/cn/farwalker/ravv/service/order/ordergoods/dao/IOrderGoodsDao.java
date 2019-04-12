package cn.farwalker.ravv.service.order.ordergoods.dao;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsSimpleVo;

/**
 * 订单商品快照<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Repository
public interface IOrderGoodsDao extends BaseMapper<OrderGoodsBo>{
    public List<OrderGoodsBo> getAwaitingOrderList(Page page, @Param("orderList") List<Long> orderList);

    public List<OrderGoodsBo> getPublishedOrderList(Page page, @Param("orderList") List<Long> orderList);
    
	/**
	 * 获取供应商某时间范围内的订单商品
	 * @param merchantId 供应商id
	 * @param startdate 开始时间
	 * @param enddate 结束时间
	 * @return
	 */
	public List<OrderGoodsSimpleVo> getOrderGoodsByMerchant(@Param("merchantId") Long merchantId, @Param("startdate") Date startdate, @Param("enddate") Date enddate);
}