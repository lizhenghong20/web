package cn.farwalker.ravv.service.model.newarrivals.goods.dao;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.goods.base.model.GoodsDetailsVo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.farwalker.ravv.service.model.newarrivals.goods.model.NewArrivalsGoodsBo;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;

/**
 * 新品到达商品<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface INewArrivalsGoodsDao extends BaseMapper<NewArrivalsGoodsBo>{

    List<GoodsDetailsVo> getGoods();
}