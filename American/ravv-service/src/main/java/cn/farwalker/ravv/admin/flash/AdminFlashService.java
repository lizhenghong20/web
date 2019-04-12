package cn.farwalker.ravv.admin.flash;

import cn.farwalker.ravv.service.flash.goods.model.FlashGoodsVo;
import cn.farwalker.ravv.service.flash.sale.model.FlashSaleBo;
import cn.farwalker.ravv.service.flash.sku.model.FlashGoodsSkuBo;
import cn.farwalker.ravv.service.goodssku.skudef.model.SkuPriceInventoryVo;
import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.web.crud.QueryFilter;
import java.util.List;

public interface AdminFlashService {

    Page<FlashGoodsVo> getFlashGoodsList(Integer start, Integer size, String sortfield, Long flashSaleId);

    Boolean selectFlashGoods(List<Long> flashGoodsIdList, Long flashSaleId);

    Boolean changeFlashGoodsSeq(Long flashGoodsId, Integer sequence);

    Boolean removeFlashGoods(List<Long> flashGoodsIdList, Long flashSaleId);

    Boolean saveFlashGoodsSku(List<SkuPriceInventoryVo> goodsSkuList, Long flashSaleId, Long goodsId);

    List<FlashGoodsSkuBo> getFlashGoodsSku(Long flashSaleId, Long goodsId);

    Boolean create(FlashSaleBo vo);

    Boolean delete(Long id);

    FlashSaleBo getOne(Long id);

    Page<FlashSaleBo> getList(List<QueryFilter> query, Integer start, Integer size, String sortfield);

    Boolean update(FlashSaleBo vo);
}
