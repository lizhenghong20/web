package cn.farwalker.ravv.service.goods.base.dao;
import cn.farwalker.ravv.service.goods.base.model.GoodsListVo;
import cn.farwalker.ravv.service.web.menu.model.WebMenuBo;
import cn.farwalker.ravv.service.web.menu.model.WebMenuGoodsFrontVo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品基础信息表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */

@Repository
public interface IGoodsDao extends BaseMapper<GoodsBo>{
	//可用
	//public Integer countByParams();

    public List<GoodsBo> getGoodsListById(Page page, @Param("goodsIdList") List<Long> goodsIdList);

    /**
     * 通过商品id列表查出商品详情
     * @param page
     * @param goodsIdList
     * @return
     */
    public List<GoodsListVo> selectGoodsListByIdList(Page page, @Param("goodsIdList") List<Long> goodsIdList);

    public List<GoodsListVo> selectGoodsListBySearch(Page page, @Param("keyWords")String keyWords,
                                                     @Param("floor")BigDecimal floor,
                                                     @Param("ceiling")BigDecimal ceiling,
                                                     @Param("menuIdList")List<Long> menuIdList,
                                                     @Param("freeShipment")Boolean freeShipment,
                                                     @Param("goodsPoint")Integer goodsPoint);
}