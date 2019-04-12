package cn.farwalker.ravv.service.goods.price.dao;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.farwalker.ravv.service.goods.price.model.GoodsPriceBo;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * 商品价格表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Repository
public interface IGoodsPriceDao extends BaseMapper<GoodsPriceBo>{

    public BigDecimal getMaxPriceMyGoodsId(long goodsId);

    public BigDecimal getMinPriceMyGoodsId(long goodsId);
}