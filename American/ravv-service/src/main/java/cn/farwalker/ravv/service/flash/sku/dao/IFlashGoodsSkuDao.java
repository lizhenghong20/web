package cn.farwalker.ravv.service.flash.sku.dao;
import cn.farwalker.ravv.service.flash.sku.model.FlashGoodsSkuVo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.farwalker.ravv.service.flash.sku.model.FlashGoodsSkuBo;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 闪购商品<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IFlashGoodsSkuDao extends BaseMapper<FlashGoodsSkuBo>{
    List<FlashGoodsSkuVo> selectGoodsSkuListByFlashSaleId(Page page, @Param("flashSaleId") Long flashSaleId);
}