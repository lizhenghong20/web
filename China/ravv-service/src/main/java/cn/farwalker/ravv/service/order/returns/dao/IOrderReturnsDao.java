package cn.farwalker.ravv.service.order.returns.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuVo;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsGoodsStatusEnum;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsTypeEnum;
import cn.farwalker.ravv.service.order.returns.model.MemberOrderReturnsVo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsBo;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsVo;

/**
 * 订单退货<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
public interface IOrderReturnsDao extends BaseMapper<OrderReturnsBo> {

	/**
	 * app按条件获取退货单列表
	 * @param memberId
	 * @param returnsTypeList
	 * @param statusList
	 * @param orderFieldList
	 * @param start
	 * @param size
	 * @return
	 */
	public List<MemberOrderReturnsVo> getMyReturns(@Param("memberId")Long memberId, @Param("returnsTypeList")List<ReturnsTypeEnum> returnsTypeList, @Param("statusList")List<ReturnsGoodsStatusEnum> statusList,
			@Param("orderFieldList")List<String> orderFieldList, @Param("start")Integer start, @Param("size")Integer size,@Param("closeStatusList")List<ReturnsGoodsStatusEnum> closeStatusList , @Param("isReturnFinish")Boolean isReturnFinish, @Param("orderId")Long orderId);

	/**
	 * 获取对应退单的相应sku信息
	 * @param returnsId
	 * @return
	 */
	public List<GoodsSkuVo> getSkuInfoByReturnsId(@Param("returnsId")Long returnsId);
	
	/**
	 * 退货相关信息
	 * 带上退货单中商品总数的
	 * @param returnsId
	 * @return
	 */
	public OrderReturnsVo getOrderReturnsInfo(@Param("returnsId")Long returnsId);
}