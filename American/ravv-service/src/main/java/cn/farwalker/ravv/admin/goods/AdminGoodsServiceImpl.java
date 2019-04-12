package cn.farwalker.ravv.admin.goods;


import cn.farwalker.ravv.admin.goods.dto.GoodsPropertyVo;
import cn.farwalker.ravv.admin.goods.dto.GoodsSpecVo;
import cn.farwalker.ravv.admin.goods.dto.GoodsStoreVo;
import cn.farwalker.ravv.service.base.brand.biz.IBaseBrandBiz;
import cn.farwalker.ravv.service.base.brand.model.BaseBrandBo;
import cn.farwalker.ravv.service.base.storehouse.biz.IStorehouseBiz;
import cn.farwalker.ravv.service.base.storehouse.model.StorehouseBo;
import cn.farwalker.ravv.service.category.basecategory.biz.IBaseCategoryBiz;
import cn.farwalker.ravv.service.category.basecategory.model.BaseCategoryBo;
import cn.farwalker.ravv.service.category.property.biz.IBaseCategoryPropertyBiz;
import cn.farwalker.ravv.service.category.property.constants.PropertyTypeEnum;
import cn.farwalker.ravv.service.category.property.model.BaseCategoryPropertyBo;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsBiz;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsService;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.goods.base.model.GoodsVo;
import cn.farwalker.ravv.service.goods.base.model.PropsVo;
import cn.farwalker.ravv.service.goods.constants.GoodsStatusEnum;
import cn.farwalker.ravv.service.goods.image.model.GoodsImageBo;
import cn.farwalker.ravv.service.goods.inventory.biz.IGoodsInventoryBiz;
import cn.farwalker.ravv.service.goods.inventory.model.GoodsInventoryBo;
import cn.farwalker.ravv.service.goods.price.biz.IGoodsPriceBiz;
import cn.farwalker.ravv.service.goods.price.model.GoodsPriceBo;
import cn.farwalker.ravv.service.goods.utils.GoodsUtil;
import cn.farwalker.ravv.service.goodssku.skudef.biz.IGoodsSkuDefBiz;
import cn.farwalker.ravv.service.goodssku.skudef.biz.IGoodsSkuService;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuDefBo;
import cn.farwalker.ravv.service.goodssku.skudef.model.SkuPriceInventoryVo;
import cn.farwalker.ravv.service.goodssku.specification.biz.IGoodsSpecdefService;
import cn.farwalker.ravv.service.goodssku.specification.model.GoodsSpecificationDefBo;
import cn.farwalker.ravv.service.merchant.biz.IMerchantBiz;
import cn.farwalker.ravv.service.merchant.model.MerchantBo;
import cn.farwalker.waka.constants.StatusEnum;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.core.base.controller.ControllerUtils;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.orm.core.annotation.LoadJoinValueImpl;
import com.cangwu.frame.web.crud.QueryFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class AdminGoodsServiceImpl implements AdminGoodsService {

    @Resource
    private IGoodsPriceBiz goodsPriceBiz;

    @Resource
    private IGoodsSkuDefBiz goodsSkudefBiz;

    @Resource
    private IGoodsSkuService goodsSkuService;

    @Resource
    private IGoodsInventoryBiz goodsInventoryBiz;

    @Resource
    private IBaseBrandBiz brandBiz;

    @Resource
    private IBaseCategoryBiz categoryBiz;

    @Resource
    private IGoodsBiz goodsBiz;
    @Resource
    private IGoodsService goodsService;

    @Resource
    private IStorehouseBiz storehouseBiz;
    @Resource
    private IMerchantBiz merchantBiz;

    @Resource
    private IBaseCategoryPropertyBiz propertyBiz;
    @Resource
    private IGoodsSkuDefBiz goodsSkuBiz;

    @Resource
    private IGoodsSpecdefService specService;

    protected IGoodsBiz getBiz() {
        return goodsBiz;
    }

    @Override
    public GoodsVo get(Long id) {
        final Long goodsId = id;
        GoodsBo goods = goodsBiz.selectById(goodsId);

        if (null == goods) {
            throw new WakaException(RavvExceptionEnum.GOODS_NOT_FIND);
        }
        GoodsVo goodsVO = Tools.bean.cloneBean(goods, new GoodsVo());
        LoadJoinValueImpl.load(goodsBiz, goodsVO);
        List<GoodsImageBo> images = goodsVO.getImages();
        if (images != null) {
            for (GoodsImageBo img : images) {
                String fp = GoodsUtil.getCdnFullPaths(img.getImgUrl());
                img.setImgUrl(fp);
            }
        }

        return goodsVO;
    }

    @Override
    public Page<GoodsStoreVo> getList(List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        long t1 = System.currentTimeMillis(), t2 = 0, t3 = 0;
        Page<GoodsBo> page = ControllerUtils.getPage(start, size, sortfield);
        t2 = System.currentTimeMillis();
        Wrapper<GoodsBo> wrap = ControllerUtils.getWrapper(query);
        t3 = System.currentTimeMillis();

        Page<GoodsBo> rds = getBiz().selectPage(page, wrap);
        Page<GoodsStoreVo> rs = ControllerUtils.convertPageRecord(rds, GoodsStoreVo.class);
        LoadJoinValueImpl.load(goodsBiz, rs.getRecords());
        long t4 = System.currentTimeMillis();
        log.debug(String.format("time %d,%d,%d", t2 - t1, t3 - t2, t4 - t3));
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public GoodsBo create(GoodsVo vo) {
        if (!isLeaf(vo.getLeafCategoryId())) {
            throw new WakaException("分类需要选择最末节点");
        }
        // createMethodSinge创建方法
        GoodsBo bo = Tools.bean.cloneBean(vo, new GoodsBo());
        List<String> imageTitles = convertList(vo.getImageTitles());
        List<String> imageDetails = convertList(vo.getImageDetails());
        String videoTitle = vo.getVideoTitle();
        String videoDetail = vo.getVideoDetail();

        if(!goodsService.insert(bo, imageTitles, vo.getImageMajor(), imageDetails, videoTitle, videoDetail)){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        return bo;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public GoodsBo update(GoodsVo vo) {
        if (!isLeaf(vo.getLeafCategoryId())) {
            throw new WakaException("分类需要选择最末节点");
        }

        GoodsBo bo = Tools.bean.cloneBean(vo, new GoodsBo());
        List<String> imageTitles = convertList(vo.getImageTitles());
        List<String> imageDetails = convertList(vo.getImageDetails());
        String videoTitle = vo.getVideoTitle();
        String videoDetail = vo.getVideoDetail();
        if(!goodsService.update(bo, imageTitles, vo.getImageMajor(), imageDetails, videoTitle, videoDetail))
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);

        return bo;
    }

    @Override
    public List<BaseBrandBo> getBrandList(Long parentid) {
        Wrapper<BaseBrandBo> w = new EntityWrapper<>();
        w.last("limit 100");// 超过100个记录，可以使用动态加载
        List<BaseBrandBo> rds = brandBiz.selectList(w);
        return rds;
    }

    @Override
    public List<GoodsBo> doStatus(List<Long> goodsIds, GoodsStatusEnum status) {
        List<GoodsBo> st = goodsService.updateStatusEnum(goodsIds, status);
        return st;
    }

    @Override
    public Page<GoodsStoreVo> getNotInMenuGoods(List<QueryFilter> query, Integer start, Integer size, String sortfield, Long menuId) {
        long t1 = System.currentTimeMillis(), t2 = 0, t3 = 0;

        t2 = System.currentTimeMillis();
        Page<GoodsBo> page = ControllerUtils.getPage(start, size, sortfield);
        Wrapper<GoodsBo> wrap = ControllerUtils.getWrapper(query);
        wrap.eq(GoodsBo.Key.goodsStatus.toString(), GoodsStatusEnum.ONLINE.getKey());
        Page<GoodsBo> goodsPage = goodsService.getGoodsNotInMenugoods(page, wrap, menuId);

        // 联表获取对应id的名称
        Page<GoodsStoreVo> rs = ControllerUtils.convertPageRecord(goodsPage, GoodsStoreVo.class);
        List<GoodsStoreVo> goodsVoList = rs.getRecords();
        if (Tools.collection.isNotEmpty(goodsVoList)) {
            LoadJoinValueImpl.load(getBiz(), goodsVoList);
        }
        t3 = System.currentTimeMillis();
        long t4 = System.currentTimeMillis();
        log.debug(String.format("time %d,%d,%d", t2 - t1, t3 - t2, t4 - t3));
        return rs;
    }

    @Override
    public Page<GoodsStoreVo> getNotInModelGoods(List<QueryFilter> query, Integer start, Integer size, String sortfield, String modelCode) {
        long t1 = System.currentTimeMillis(), t2 = 0, t3 = 0;
        t2 = System.currentTimeMillis();
        Page<GoodsBo> page = ControllerUtils.getPage(start, size, sortfield);
        Wrapper<GoodsBo> wrap = ControllerUtils.getWrapper(query);
        wrap.eq(GoodsBo.Key.goodsStatus.toString(), GoodsStatusEnum.ONLINE.getKey());
        Page<GoodsBo> goodsPage = goodsService.getGoodsNotInModelgoods(page, wrap, modelCode);

        // 联表获取对应id的名称
        Page<GoodsStoreVo> rs = ControllerUtils.convertPageRecord(goodsPage, GoodsStoreVo.class);
        List<GoodsStoreVo> goodsVoList = rs.getRecords();
        if (Tools.collection.isNotEmpty(goodsVoList)) {
            LoadJoinValueImpl.load(getBiz(), goodsVoList);
        }
        t3 = System.currentTimeMillis();


        long t4 = System.currentTimeMillis();
        log.debug(String.format("time %d,%d,%d", t2 - t1, t3 - t2, t4 - t3));
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        Integer rs = goodsService.deleteGoodsById(id);
        if(rs < 0){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        return true;
    }

    @Override
    public List<StorehouseBo> getStoreList() {
        // createMethodSinge创建方法
        Wrapper<StorehouseBo> wp = new EntityWrapper<>();
        wp.eq(StorehouseBo.Key.status.toString(), Boolean.TRUE);
        List<StorehouseBo> rs = storehouseBiz.selectList(wp);
        return rs;
    }

    @Override
    public List<MerchantBo> findMerchant(String search) {
        // createMethodSinge创建方法
        Wrapper<MerchantBo> wp = new EntityWrapper<>();
        wp.like(MerchantBo.Key.name.toString(), search);
        wp.last("limit 20");
        List<MerchantBo> rs = merchantBiz.selectList(wp);
        return rs;
    }

    @Override
    public Page<GoodsStoreVo> getMerchantGoods(Long merchantId, Boolean isAlarm, List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        long t1 = System.currentTimeMillis(), t2 = 0, t3 = 0;

        Page<GoodsBo> page = ControllerUtils.getPage(start, size, sortfield);
        t2 = System.currentTimeMillis();
        Wrapper<GoodsBo> wrap = ControllerUtils.getWrapper(query);
        t3 = System.currentTimeMillis();

        wrap.eq(GoodsBo.Key.merchantId.toString(), merchantId);

        //查询库存告警商品
        if(isAlarm) {
            wrap.where("id in (SELECT goods_id FROM goods_inventory WHERE sale_stock_num < alarm_stock_num)");
        }

        Page<GoodsBo> rds = getBiz().selectPage(page, wrap);
        Page<GoodsStoreVo> rs = ControllerUtils.convertPageRecord(rds, GoodsStoreVo.class);
        LoadJoinValueImpl.load(goodsBiz, rs.getRecords());

        long t4 = System.currentTimeMillis();
        log.debug(String.format("time %d,%d,%d", t2 - t1, t3 - t2, t4 - t3));
        return rs;
    }

    @Override
    public JsonResult<List<GoodsPropertyVo>> getPropertys(Long goodsid) {
        //createMethodSinge创建方法
        GoodsBo goodsBo =(goodsid == null ? null : goodsBiz.selectById(goodsid));
        Long categoryId = goodsBo.getLeafCategoryId();
        List<BaseCategoryPropertyBo> rds = propertyBiz.getProListByCatId(categoryId, StatusEnum.ENABLE);
        List<GoodsPropertyVo> proValues = ControllerUtils.convertList(rds, GoodsPropertyVo.class);
        LoadJoinValueImpl.load(propertyBiz, proValues);
        setValueChecked(goodsBo,proValues);

        /** 已定义的SKU.PropertyValueIds */
        List<String> skuVs = new ArrayList<>();
        {//取已定义的SKU
            Wrapper<GoodsSkuDefBo> querySku = new EntityWrapper<>();
            querySku.eq(GoodsSkuDefBo.Key.goodsId.toString(), goodsid);
            List<GoodsSkuDefBo> skus = goodsSkuBiz.selectList(querySku);
            for(GoodsSkuDefBo bo :skus){
                skuVs.add(bo.getPropertyValueIds());
            }
        }
        JsonResult<List<GoodsPropertyVo>> rs = JsonResult.newSuccess(proValues);
        rs.put("skus",skuVs);
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean createSku(Long goodsid, List<GoodsSpecVo> values, String skus) {
        String[] items = skus.split(";");
        List<String[]> sku = new ArrayList<>(items.length);
        for(String s : items){
            String[] e = s.split("/");
            sku.add(e);
        }
        List<GoodsSpecificationDefBo> specVo = ControllerUtils.convertList(values, GoodsSpecificationDefBo.class);

        for(GoodsSpecificationDefBo vo :specVo){
            String url =  QiniuUtil.getRelativePath(vo.getImgUrl());
            vo.setImgUrl(url);
        }
        goodsService.createPropertySku(goodsid, specVo, sku);
        return true;
    }

    @Override
    public GoodsVo getGoodsRunstate(Long goodsId) {
        GoodsBo bo = getBiz().selectById(goodsId);
        GoodsVo vo = Tools.bean.cloneBean(bo, new GoodsVo());
        return vo;
    }

    @Override
    public Page<GoodsBo> list(List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        //createMethodSinge创建方法
        Page<GoodsBo> page =ControllerUtils.getPage(start,size,sortfield);
        Wrapper<GoodsBo> wrap =ControllerUtils.getWrapper(query);
        Page<GoodsBo> rs = goodsBiz.selectPage(page,wrap);
        return rs;
    }

    @Override
    public Page<GoodsBo> getMerchantGoodsrunstate(Long merchantId, List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        Page<GoodsBo> page =ControllerUtils.getPage(start,size,sortfield);
        Wrapper<GoodsBo> wrap =ControllerUtils.getWrapper(query);
        wrap.eq(GoodsBo.Key.merchantId.toString(), merchantId);

        Page<GoodsBo> rs = goodsBiz.selectPage(page,wrap);
        return rs;
    }

    @Override
    public List<SkuPriceInventoryVo> getSkulist(Long goodsid) {
        //createMethodSinge创建方法
        Wrapper<GoodsSkuDefBo> querySku = new EntityWrapper<>();
        querySku.eq(GoodsSkuDefBo.Key.goodsId.toString(), goodsid);
        List<GoodsSkuDefBo> skuBos = goodsSkudefBiz.selectList(querySku);

        Wrapper<GoodsPriceBo> queryPrice = new EntityWrapper<>();
        queryPrice.eq(GoodsPriceBo.Key.goodsId.toString(), goodsid);
        List<GoodsPriceBo> priceBos = goodsPriceBiz.selectList(queryPrice);

        Wrapper<GoodsInventoryBo> queryInvent = new EntityWrapper<>();
        queryInvent.eq(GoodsInventoryBo.Key.goodsId.toString(), goodsid);
        List<GoodsInventoryBo> inventBos = goodsInventoryBiz.selectList(queryInvent);

        List<SkuPriceInventoryVo> rs = margePriceInvent(skuBos,priceBos,inventBos);
        return rs;
    }

    @Override

    public Integer updateInventory(Long goodsid, List<SkuPriceInventoryVo> prices) {
        for(SkuPriceInventoryVo pv:prices){
            String imageUrl = QiniuUtil.getRelativePath(pv.getImageUrl());
            pv.setImageUrl(imageUrl);
        }
        Integer rs = goodsSkuService.updateSkuInvent(goodsid,prices);
        return rs;
    }


    /**
     * 判断分类节点是否末及
     *
     * @param categoryId
     * @return
     */
    private boolean isLeaf(Long categoryId) {
        if (categoryId == null || categoryId.longValue() <= 0) {
            return false;
        }
        Wrapper<BaseCategoryBo> query = new EntityWrapper<>();
        query.eq(BaseCategoryBo.Key.pid.toString(), categoryId);
        query.last("limit 1");
        BaseCategoryBo children = categoryBiz.selectOne(query);
        return (children == null);
    }

    private List<String> convertList(String imgs) {
        if (imgs == null) {
            return null;
        } else if (imgs.trim().length() == 0) {
            return Collections.emptyList();
        } else {
            String[] arys = imgs.trim().split(",");
            return Arrays.asList(arys);
        }
    }


    /**
     * 设置属性值,及是否已引用(转换类型PropertyValueVo)<br/>
     * 并且设置图片的全路径
     * @param goodsBo
     * @param proValues 分类预定义的所有属性及值(包括sku属性、普通属性)
     */
    private void setValueChecked(GoodsBo goodsBo, List<GoodsPropertyVo> proValues){
        if(Tools.collection.isEmpty(proValues)){
            return ;
        }
        Long goodsId = goodsBo.getId();
        List<GoodsSpecificationDefBo> goodsValues = specService.getValues(goodsId);
        List<PropsVo> goodsProps = GoodsUtil.parsePropsVo(goodsBo.getProps());

        //分类预定义的属性PropertyValueVo
        for(GoodsPropertyVo pv : proValues){
            List<GoodsSpecVo> valueDefs = pv.getValues();//分类预定义的属性值

            if(pv.getType() == PropertyTypeEnum.STANDARD){//SKU属性
                getSkuValue(goodsId, valueDefs, goodsValues);
            }
            else{//普通属性
                List<GoodsSpecVo> defs = getPropsValue(goodsId,valueDefs,pv,goodsProps);
                pv.setSpecValues(defs);
            }
        }
    }

    /**
     * 创建商品的普通属性,并且设置图片的全路径
     * @param goodsId
     * @param valueDefs 商品定义的值对象
     */
    private void getSkuValue(Long goodsId,List<GoodsSpecVo> valueDefs, List<GoodsSpecificationDefBo> goodsValues ){
        int size = (valueDefs==null?0:valueDefs.size());
        for(int i =0;i< size;i++){
            GoodsSpecVo e = valueDefs.get(i) ;
            e.setGoodsId(goodsId);
            GoodsSpecificationDefBo gv = findGoodsValue(goodsValues, e.getPropertyValueId());
            if(gv!=null){
                e.setChecked(Boolean.TRUE);
                String url = QiniuUtil.getFullPath(gv.getImgUrl());
                e.setImgUrl(url);
            }
            else{
                e.setChecked(Boolean.FALSE);
            }
        }
    }

    /**
     * 创建商品的普通属性
     * @param goodsId
     * @param valueDefs
     * @param pv
     * @param goodsProps
     */
    private List<GoodsSpecVo> getPropsValue(Long goodsId,List<GoodsSpecVo> valueDefs,GoodsPropertyVo pv,List<PropsVo> goodsProps ){
        int propsSize = (valueDefs==null?0:valueDefs.size());
        if(propsSize ==0){ //没有属性值，就补一个
            GoodsSpecVo v = new GoodsSpecVo();
            v.setId(Long.valueOf(-1));
            v.setIsimg(pv.getIsimage());
            v.setGoodsId(goodsId);

            if(Tools.collection.isNotEmpty(goodsProps)){//取第一个
                PropsVo ps = goodsProps.get(0);
                v.setChecked(Boolean.TRUE);
                v.setCustomValueName(ps.getValue());
            }
            else{
                v.setCustomValueName("");
            }
            valueDefs = Arrays.asList(v);//从新创建了
            //pv.setSpecValues(valueDefs);
        }
        else{
            for(int i =0;i< propsSize;i++){
                GoodsSpecVo e = valueDefs.get(i) ;
                e.setIsimg(pv.getIsimage());
                e.setGoodsId(goodsId);
                //e.setId(gv.getValueid());
                PropsVo gv = findPropsValue(goodsProps, e.getPropertyValueId());
                if(gv==null){
                    e.setChecked(Boolean.FALSE);
                }
                else{
                    e.setChecked(Boolean.TRUE);
                    e.setCustomValueName(gv.getValue());
                    e.setPropertyValueId(e.getPropertyValueId());
                }
            }
        }
        return valueDefs;
    }

    private GoodsSpecificationDefBo findGoodsValue(List<GoodsSpecificationDefBo> goodsValues, Long propertyValueId){
        if(Tools.collection.isEmpty(goodsValues) || propertyValueId == null){
            return null;
        }
        GoodsSpecificationDefBo rs = null;
        for(GoodsSpecificationDefBo bo:goodsValues){
            if(bo.getPropertyValueId().equals(propertyValueId)){
                rs = bo;
                break;
            }
        }
        return rs;
    }

    private PropsVo findPropsValue(List<PropsVo> propsValues, Long propertyValueId){
        if(Tools.collection.isEmpty(propsValues) || propertyValueId == null){
            return null;
        }
        PropsVo rs = null;
        for(PropsVo bo:propsValues){
            if(bo.getValueid().equals(propertyValueId)){
                rs = bo;
                break;
            }
        }
        return rs;
    }

    private List<SkuPriceInventoryVo> margePriceInvent(List<GoodsSkuDefBo> skuBos , List<GoodsPriceBo> priceBos , List<GoodsInventoryBo> inventBos ){
        List<SkuPriceInventoryVo> result = new ArrayList<>(skuBos.size());
        for(GoodsSkuDefBo sku:skuBos){
            Long skuId = sku.getId();
            GoodsPriceBo prcbo = getBo(skuId, priceBos, true);
            GoodsInventoryBo invbo = getBo(skuId, inventBos, false);

            SkuPriceInventoryVo vo  = Tools.bean.cloneBean(sku, new SkuPriceInventoryVo());
            if(prcbo != null){
                Tools.bean.cloneBean(prcbo,vo);
                vo.setPriceId(prcbo.getId());
            }
            if(invbo!=null){
                Tools.bean.cloneBean(invbo,vo);
                vo.setInventoryId(invbo.getId());
            }
            vo.setGoodsId(sku.getGoodsId());
            vo.setSkuId(skuId);
            vo.setId(skuId);//前面的复制已经把id搞没了，这里要重新赋值

            String imageUrl = QiniuUtil.getFullPath(vo.getImageUrl());
            vo.setImageUrl(imageUrl);
            result.add(vo);
        }
        return result;
    }

    private <T> T getBo(Long skuId,List<T> rds,boolean isPriceBo){
        T rs = null;
        for(T e : rds){
            Long objSku = (isPriceBo ? ((GoodsPriceBo)e).getSkuId():((GoodsInventoryBo)e).getSkuId());
            if(skuId.equals(objSku)){
                rs = e;
                break;
            }
        }
        return rs;
    }
}
