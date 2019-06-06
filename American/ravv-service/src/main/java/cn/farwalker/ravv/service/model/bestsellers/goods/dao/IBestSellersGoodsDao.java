package cn.farwalker.ravv.service.model.bestsellers.goods.dao;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.farwalker.ravv.service.model.bestsellers.goods.model.BestSellersGoodsBo;

import java.util.List;

/**
 * 销售最好商品<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IBestSellersGoodsDao extends BaseMapper<BestSellersGoodsBo>{
    List<GoodsBo> selectGoodsForMenuId(Long menuId);
}