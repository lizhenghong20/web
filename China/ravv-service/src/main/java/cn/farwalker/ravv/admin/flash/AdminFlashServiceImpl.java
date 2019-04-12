package cn.farwalker.ravv.admin.flash;


import cn.farwalker.ravv.service.flash.goods.biz.IFlashGoodsBiz;
import cn.farwalker.ravv.service.flash.goods.model.FlashGoodsBo;
import cn.farwalker.ravv.service.flash.goods.model.FlashGoodsVo;
import cn.farwalker.ravv.service.flash.sale.biz.IFlashSaleBiz;
import cn.farwalker.ravv.service.flash.sale.biz.IFlashSaleService;
import cn.farwalker.ravv.service.flash.sale.model.FlashSaleBo;
import cn.farwalker.ravv.service.flash.sku.biz.IFlashGoodsSkuBiz;
import cn.farwalker.ravv.service.flash.sku.model.FlashGoodsSkuBo;
import cn.farwalker.ravv.service.goodssku.skudef.model.SkuPriceInventoryVo;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.core.base.controller.ControllerUtils;
import cn.farwalker.waka.core.RavvExceptionEnum;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.cangwu.frame.orm.core.annotation.LoadJoinValueImpl;
import com.cangwu.frame.web.crud.QueryFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Slf4j
@Service
public class AdminFlashServiceImpl implements AdminFlashService{

    @Resource
    private IFlashSaleService flashSaleService;

    @Resource
    private IFlashSaleBiz flashSaleBiz;

    @Resource
    private IFlashGoodsBiz flashGoodsBiz;

    @Resource
    private IFlashGoodsSkuBiz flashGoodsSkuBiz;

    protected IFlashSaleBiz getBiz() {
        return flashSaleBiz;
    }


    @Override
    public Page<FlashGoodsVo> getFlashGoodsList(Integer start, Integer size, String sortfield, Long flashSaleId) {
        Page<FlashGoodsBo> page = ControllerUtils.getPage(start,size,sortfield);
        Wrapper<FlashGoodsBo> wrap = new EntityWrapper<>();
        wrap.eq(FlashGoodsBo.Key.flashSaleId.toString(), flashSaleId);
        wrap.orderBy(FlashGoodsBo.Key.sequence.toString(), false);
        Page<FlashGoodsBo> flashGoodsBoPage = flashGoodsBiz.selectPage(page,wrap);

        Page<FlashGoodsVo> flashGoodsVoPage =  ControllerUtils.convertPageRecord(flashGoodsBoPage, FlashGoodsVo.class);

        LoadJoinValueImpl.load(flashGoodsBiz, flashGoodsVoPage.getRecords());

        //移除已被删除的商品
        flashGoodsVoPage.getRecords().removeIf(flashGoodsVo -> flashGoodsVo.getGoodsBo() == null);

        return flashGoodsVoPage;
    }

    @Override
    public Boolean selectFlashGoods(List<Long> flashGoodsIdList, Long flashSaleId) {
        for(Long flashGoodsId : flashGoodsIdList) {
            //判断是否加入重复商品
            Wrapper<FlashGoodsBo> wrapper = new EntityWrapper<>();
            wrapper.eq(FlashGoodsBo.Key.flashSaleId.toString(), flashSaleId);
            wrapper.eq(FlashGoodsBo.Key.goodsId.toString(), flashGoodsId);

            FlashGoodsBo SameFlashGoods = flashGoodsBiz.selectOne(wrapper);
            if(null == SameFlashGoods) {
                FlashGoodsBo flashGoods = new FlashGoodsBo();
                flashGoods.setFlashSaleId(flashSaleId);
                flashGoods.setGoodsId(flashGoodsId);

                flashGoodsBiz.insert(flashGoods);
            }
        }
        return true;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean changeFlashGoodsSeq(Long flashGoodsId, Integer sequence) {
        FlashGoodsBo flashGoods = flashGoodsBiz.selectById(flashGoodsId);
        if(null == flashGoods) {
            throw new WakaException("没有对应的限时购商品");
        }
        flashGoods.setSequence(sequence);

        Boolean rs =  flashGoodsBiz.updateById(flashGoods);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean removeFlashGoods(List<Long> flashGoodsIdList, Long flashSaleId) {
        //删除选中商品
        for(Long flashGoodsId : flashGoodsIdList) {
            EntityWrapper<FlashGoodsBo> wrapper = new EntityWrapper<>();
            wrapper.eq(FlashGoodsBo.Key.flashSaleId.toString(), flashSaleId);
            wrapper.eq(FlashGoodsBo.Key.goodsId.toString(), flashGoodsId);

            FlashGoodsBo flashGoods = flashGoodsBiz.selectOne(wrapper);

            if(null != flashGoods) {
                flashGoodsBiz.deleteById(flashGoods.getId());

                //删除选中限时购商品的sku
                List<FlashGoodsSkuBo> flashGoodsSkuList = flashSaleService.flashGoodsSkuList(flashSaleId, flashGoodsId);

                if(CollectionUtils.isNotEmpty(flashGoodsSkuList)) {
                    for(FlashGoodsSkuBo flashGoodsSku : flashGoodsSkuList) {
                        flashGoodsSkuBiz.deleteById(flashGoodsSku.getId());
                    }
                }
            }else {
                throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
            }
        }

        return true;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean saveFlashGoodsSku(List<SkuPriceInventoryVo> goodsSkuList, Long flashSaleId, Long goodsId) {
        //清空原有限时购的商品的sku
        List<FlashGoodsSkuBo> flashGoodsSkuList = flashSaleService.flashGoodsSkuList(flashSaleId, goodsId);

        if(CollectionUtils.isNotEmpty(flashGoodsSkuList)) {
            for(FlashGoodsSkuBo flashGoodsSku : flashGoodsSkuList) {
                flashGoodsSkuBiz.deleteById(flashGoodsSku.getId());
            }
        }
        //添加新的限时购商品sku
        if(CollectionUtils.isNotEmpty(goodsSkuList)) {
            for(SkuPriceInventoryVo skuVo : goodsSkuList) {
                if(skuVo.getPrice() == null || skuVo.getPrice().compareTo(BigDecimal.ZERO) < 0 ) {
                    throw new WakaException("价格不能为空");
                }
                else if(skuVo.getSaleStockNum() == null || skuVo.getSaleStockNum() < 0) {
                    throw new WakaException("库存不能为空");
                }
                else if(skuVo.getFreeze() == null || skuVo.getFreeze() < 0) {
                    throw new WakaException("冻结库存不能为空");
                }
                else if(skuVo.getSaleStockNum() < skuVo.getFreeze()) {
                    throw new WakaException("冻结库存不能超过库存量");
                }
                FlashGoodsSkuBo flashGoodsSku = new FlashGoodsSkuBo();
                flashGoodsSku.setGoodsId(goodsId);
                flashGoodsSku.setFlashSaleId(flashSaleId);
                flashGoodsSku.setInventory(skuVo.getSaleStockNum());
                flashGoodsSku.setPrice(skuVo.getPrice());
                flashGoodsSku.setPropertyValueIds(skuVo.getPropertyValueIds());
                flashGoodsSku.setGoodsSkuDefId(skuVo.getSkuId());
                flashGoodsSku.setFreezeCount(skuVo.getFreeze());
                flashGoodsSkuBiz.insert(flashGoodsSku);
            }
        }

        return true;
    }

    @Override
    public List<FlashGoodsSkuBo> getFlashGoodsSku(Long flashSaleId, Long goodsId) {
        List<FlashGoodsSkuBo> flashGoodsSkuList = flashSaleService.flashGoodsSkuList(flashSaleId, goodsId);

        return flashGoodsSkuList;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean create(FlashSaleBo vo) {
        Boolean rs = getBiz().insert(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        Boolean rs = getBiz().deleteById(id);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        return rs;
    }

    @Override
    public FlashSaleBo getOne(Long id) {
        FlashSaleBo rs = getBiz().selectById(id);
        if(rs == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        return rs;
    }

    @Override
    public Page<FlashSaleBo> getList(List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        // createMethodSinge创建方法
        Page<FlashSaleBo> page = ControllerUtils.getPage(start, size, sortfield);
        Wrapper<FlashSaleBo> wrap = ControllerUtils.getWrapper(query);
        Page<FlashSaleBo> rs = getBiz().selectPage(page, wrap);
        if(rs == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean update(FlashSaleBo vo) {
        // 判断三个时间是否正确
        Date freezeTime = vo.getFreezetime();
        Date starttime = vo.getStarttime();
        Date endtime = vo.getEndtime();
        if(starttime.before(freezeTime)) {
            throw new WakaException("冻结时间需在开始时间之前");//冻结时间需在开始时间之前
        }else if(endtime.before(starttime)) {
            throw new WakaException("开始时间需在结束时间之前");
        }
        Boolean rs = getBiz().updateById(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }
}
