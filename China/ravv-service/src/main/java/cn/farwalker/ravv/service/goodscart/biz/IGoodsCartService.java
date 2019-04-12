package cn.farwalker.ravv.service.goodscart.biz;

import cn.farwalker.ravv.service.goods.base.model.ParseSkuExtVo;
import cn.farwalker.ravv.service.goods.base.model.ParseSkuTemp;
import cn.farwalker.ravv.service.goodscart.model.GoodsCartVo;
import cn.farwalker.ravv.service.goodscart.model.RecoverToCartForm;
import cn.farwalker.ravv.service.goodscart.model.UpdateCartVo;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by asus on 2018/11/27.
 */
public interface IGoodsCartService  {

    /**
     * 加入购物车
     * @param memberId
     * @param goodsId
     * @param skuId
     * @param quantity
     * @return
     */
    public Boolean addToCart(Long memberId,Long goodsId, Long skuId, Integer quantity);

    /**
     * 批量添加到购物车，用于从取消的订单中恢复购物车
     * @param memberId
     * @param list
     * @return
     */
    public String updateToCart(Long memberId, List<RecoverToCartForm> list);

    /**
     * 获得购物车商品
     * @param memberId
     * @return
     */
    List<GoodsCartVo> getCartGoods(Long memberId) throws Exception;


    /**
     * 删除购物车中的条目
     * @param goodsCartId
     * @return
     */
    public String deleteByGoodsCartId(Long goodsCartId);

    /**
     * 用户生成订单后批量删除购物车商品
     * @param memberId
     * @param skuIdList  订单中所有商品的skuId 组成的列表
     * @return
     */
    public boolean deleteByGoodsIdList(Long memberId, List<Long> skuIdList);

    /**
     * 更新购物车，包括勾选或取消勾选商品，更新sku，更新数量
     * @param memberId
     * @param selectedCartIds
     * @param updateCartId
     * @param skuId
     * @param quantity
     * @param type
     * @return
     */
    public UpdateCartVo updateCart(Long memberId,
                                   String  selectedCartIds,
                                   Long updateCartId,
                                   Long skuId,
                                   Integer quantity,
                                   String type) throws Exception;
}
