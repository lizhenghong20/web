package cn.farwalker.ravv.service.order.returns.biz;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.service.IService;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsDetailBo;

/**
 * 订单退货详情<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IOrderReturnsDetailBiz extends IService<OrderReturnsDetailBo>{
	
	/**
	 * 退换货单退换货成功的商品数量
	 * @param returnsId 退换货单id
	 * @return
	 */
	Integer succeedReGoodsNum(Long returnsId);
	
    /**
     * 获取指定订单商品的订单退货列表（非换货类型）
     * @param orderId 订单id
     * @param goodsId 商品id
     * @return
     */
    public List<OrderReturnsDetailBo> getReturnsDetailList(Long orderId, Long goodsId);
}