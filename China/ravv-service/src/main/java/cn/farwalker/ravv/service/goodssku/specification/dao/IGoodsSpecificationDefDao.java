package cn.farwalker.ravv.service.goodssku.specification.dao;
import cn.farwalker.ravv.service.goodssku.specification.model.GoodsSpecificationDefVo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.farwalker.ravv.service.goodssku.specification.model.GoodsSpecificationDefBo;

import java.util.List;

/**
 * 商品规格定义<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IGoodsSpecificationDefDao extends BaseMapper<GoodsSpecificationDefBo>{
    List<GoodsSpecificationDefVo> selectGoodsSpecificationDefVosById(Long goodsId);
}