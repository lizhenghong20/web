package cn.farwalker.ravv.admin.goods;

import cn.farwalker.ravv.admin.goods.dto.GoodsPropertyVo;
import cn.farwalker.ravv.admin.goods.dto.GoodsSpecVo;
import cn.farwalker.ravv.admin.goods.dto.GoodsStoreVo;
import cn.farwalker.ravv.service.base.brand.model.BaseBrandBo;
import cn.farwalker.ravv.service.base.storehouse.model.StorehouseBo;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.goods.base.model.GoodsVo;
import cn.farwalker.ravv.service.goods.constants.GoodsStatusEnum;
import cn.farwalker.ravv.service.goodssku.skudef.model.SkuPriceInventoryVo;
import cn.farwalker.ravv.service.merchant.model.MerchantBo;
import cn.farwalker.waka.core.JsonResult;
import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.web.crud.QueryFilter;

import java.util.List;

public interface AdminGoodsService {

    GoodsVo get(Long id);

    Page<GoodsStoreVo> getList(List<QueryFilter> query, Integer start, Integer size,
                              String sortfield);

    GoodsBo create(GoodsVo vo);

    GoodsBo update(GoodsVo vo);

    List<BaseBrandBo> getBrandList(Long parentid);

    List<GoodsBo> doStatus(List<Long> goodsIds, GoodsStatusEnum status);

    Page<GoodsStoreVo> getNotInMenuGoods(List<QueryFilter> query, Integer start,
                                          Integer size, String sortfield, Long menuId);

    Page<GoodsStoreVo> getNotInModelGoods(List<QueryFilter> query, Integer start,
                                           Integer size, String sortfield, String modelCode);

    Boolean delete(Long id);

    List<StorehouseBo> getStoreList();

    List<MerchantBo> findMerchant(String search);

    Page<GoodsStoreVo> getMerchantGoods(Long merchantId, Boolean isAlarm,
                          List<QueryFilter> query, Integer start, Integer size, String sortfield);

    JsonResult<List<GoodsPropertyVo>> getPropertys(Long goodsid);

    Boolean createSku(Long goodsid,List<GoodsSpecVo> values, String skus);

    GoodsVo getGoodsRunstate(Long goodsId);

    Page<GoodsBo> list(List<QueryFilter> query, Integer start, Integer size, String sortfield);

    Page<GoodsBo> getMerchantGoodsrunstate(Long merchantId, List<QueryFilter> query,
                                            Integer start,Integer size,String sortfield);

    List<SkuPriceInventoryVo> getSkulist(Long goodsid);

    Integer updateInventory(Long goodsid, List<SkuPriceInventoryVo> prices);
}
