package cn.farwalker.ravv.service.order.returns.dao;
import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.farwalker.ravv.service.order.returns.constants.ReturnsGoodsStatusEnum;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsDetailBo;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 订单退货详情<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IOrderReturnsDetailDao extends BaseMapper<OrderReturnsDetailBo>{
	/** 取得明细的状态(如果明细统一时，需要更新主表状态)*/ 
    public List<OrderReturnsDetailBo> getReturnsDetailState(@Param("detailIds")List<Long> detailIds);
    /**
     * 通过条件获取对应退货详情
     * @param orderId
     * @param skuIdList
     * @param statusList
     * @return
     */
    public List<OrderReturnsDetailBo> getEffectiveReturnsDetail(@Param("orderId")Long orderId, @Param("skuIdList")List<Long> skuIdList, @Param("statusList")List<ReturnsGoodsStatusEnum> statusList);
    
    /**
     * 获取订单对应的退货数量（GROUP BY ord.order_id,ord.skuId ）
     * 统计订单的每个商品的退货情况(带退货主表id)
     * @param orderIdList
     * @param statusList
     * @return
     */
    public List<OrderReturnsDetailBo> getEffectiveReturnCount(@Param("orderIdList")Collection<Long> orderIdList, @Param("statusList")List<ReturnsGoodsStatusEnum> statusList);

    /**
     * 获取指定订单商品的订单退货列表（非换货类型）
     * @param orderId 订单id
     * @param goodsId 商品id
     * @return
     */
    public List<OrderReturnsDetailBo> getReturnsDetailList(@Param("orderId")Long orderId,@Param("goodsId")Long goodsId);
}