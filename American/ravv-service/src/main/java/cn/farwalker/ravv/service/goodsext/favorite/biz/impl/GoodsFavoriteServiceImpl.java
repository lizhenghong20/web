package cn.farwalker.ravv.service.goodsext.favorite.biz.impl;

import cn.farwalker.ravv.common.constants.FilterTypeEnum;
import cn.farwalker.ravv.common.constants.InventoryTypeEnum;
import cn.farwalker.ravv.service.flash.sale.biz.IFlashSaleBiz;
import cn.farwalker.ravv.service.flash.sale.model.FlashSaleBo;
import cn.farwalker.ravv.service.flash.sku.biz.IFlashGoodsSkuBiz;
import cn.farwalker.ravv.service.flash.sku.model.FlashGoodsSkuBo;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsBiz;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.goods.constants.GoodsStatusEnum;
import cn.farwalker.ravv.service.goods.image.biz.IGoodsImageBiz;
import cn.farwalker.ravv.service.goods.image.model.GoodsImageBo;
import cn.farwalker.ravv.service.goods.inventory.biz.IGoodsInventoryBiz;
import cn.farwalker.ravv.service.goods.inventory.model.GoodsInventoryBo;
import cn.farwalker.ravv.service.goods.price.dao.IGoodsPriceDao;
import cn.farwalker.ravv.service.goodsext.comment.biz.IGoodsCommentBiz;
import cn.farwalker.ravv.service.goodsext.comment.model.GoodsCommentBo;
import cn.farwalker.ravv.service.goodsext.favorite.biz.IGoodsFavoriteBiz;
import cn.farwalker.ravv.service.goodsext.favorite.biz.IGoodsFavoriteService;
import cn.farwalker.ravv.service.goodsext.favorite.dao.IGoodsFavoriteDao;
import cn.farwalker.ravv.service.goodsext.favorite.model.FavoriteFilterVo;
import cn.farwalker.ravv.service.goodsext.favorite.model.GoodsFavoriteBo;
import cn.farwalker.ravv.service.goodsext.favorite.model.GoodsFavoriteVo;
import cn.farwalker.ravv.service.web.menu.biz.IWebMenuBiz;
import cn.farwalker.ravv.service.web.menu.biz.IWebMenuGoodsBiz;
import cn.farwalker.ravv.service.web.menu.model.WebMenuBo;
import cn.farwalker.ravv.service.web.menu.model.WebMenuGoodsBo;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class GoodsFavoriteServiceImpl implements IGoodsFavoriteService {

    @Autowired
    private IGoodsFavoriteBiz iGoodsFavoriteBiz;

    @Autowired
    private IGoodsBiz iGoodsBiz;

    @Autowired
    private IGoodsImageBiz iGoodsImageBiz;

    @Autowired
    private IGoodsFavoriteDao iGoodsFavoriteDao;

    @Autowired
    private IGoodsInventoryBiz iGoodsInventoryBiz;

    @Autowired
    private IGoodsCommentBiz iGoodsCommentBiz;

    @Autowired
    private IFlashSaleBiz iFlashSaleBiz;

    @Autowired
    private IFlashGoodsSkuBiz iFlashGoodsSkuBiz;

    @Autowired
    private IGoodsPriceDao iGoodsPriceDao;

    @Autowired
    private IWebMenuGoodsBiz iWebMenuGoodsBiz;

    @Autowired
    private IWebMenuBiz iWebMenuBiz;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean addFavorite(Long memberId, Long goodsId) {
        GoodsFavoriteBo favoriteBo = new GoodsFavoriteBo();
        Date now = new Date();
        //查出商品最低价
        BigDecimal minPrice = iGoodsPriceDao.getMinPriceMyGoodsId(goodsId);
        //查看该商品有没有收藏过
        EntityWrapper<GoodsFavoriteBo> queryFavorite = new EntityWrapper<>();
        queryFavorite.eq(GoodsFavoriteBo.Key.memberId.toString(), memberId);
        queryFavorite.eq(GoodsFavoriteBo.Key.goodsId.toString(), goodsId);
        GoodsFavoriteBo isFavorite = new GoodsFavoriteBo();
        isFavorite = iGoodsFavoriteBiz.selectOne(queryFavorite);
        if(isFavorite != null){
            //更新收藏时间字段
            isFavorite.setOldPrice(minPrice);
            isFavorite.setGmtModified(now);
            return iGoodsFavoriteBiz.updateById(isFavorite);
        }
        //更新商品表的收藏数量字段
        //查询收藏字段是否为空
        GoodsBo goodsBo = new GoodsBo();
        goodsBo = iGoodsBiz.selectById(goodsId);
        if(goodsBo.getFavoriteCount() == null)
            goodsBo.setFavoriteCount(1);
        else
            goodsBo.setFavoriteCount(goodsBo.getFavoriteCount() + 1);
        if(!iGoodsBiz.updateById(goodsBo))
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR + "goods favorite");

        favoriteBo.setOldPrice(minPrice);
        favoriteBo.setGoodsId(goodsId);
        favoriteBo.setMemberId(memberId);
        favoriteBo.setGmtCreate(now);
        favoriteBo.setGmtModified(now);


        return iGoodsFavoriteBiz.insert(favoriteBo);

    }

    @Override
    public String addFavoriteBatch(Long memberId, String goodsIds) {
        if(Tools.string.isEmpty(goodsIds)){
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        }
        if(memberId == null || memberId == 0){
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        }
        List<Long> goodsIdList = Tools.string.convertStringToLong(goodsIds);
        if(goodsIdList.size() == 0){
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR + "转换出错");
        }
        goodsIdList.forEach(item->{
            if(!addFavorite(memberId, item)){
                throw new WakaException(RavvExceptionEnum.INSERT_ERROR + "sing insert favorite fail");
            }
        });
        return "add batch successful";
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String deleteFavorite(Long memberId, Long goodsId) {
        EntityWrapper<GoodsFavoriteBo> queryFavorite = new EntityWrapper<>();
        queryFavorite.eq(GoodsFavoriteBo.Key.goodsId.toString(), goodsId);
        queryFavorite.eq(GoodsFavoriteBo.Key.memberId.toString(), memberId);
        if(!iGoodsFavoriteBiz.delete(queryFavorite))
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        //获取商品收藏数量并减1
        GoodsBo goodsBo = iGoodsBiz.selectById(goodsId);
        if(goodsBo == null){
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        }
        int favoriteCount = goodsBo.getFavoriteCount();
        favoriteCount--;
        goodsBo.setFavoriteCount(favoriteCount);
        if(!iGoodsBiz.updateById(goodsBo)){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR + "goods favorite update error");
        }
        return "delete favorite successful";
    }

    @Override
    public String deleteAllFavorite(Long memberId) {
        if(!iGoodsFavoriteBiz.delete(Condition.create().eq(GoodsFavoriteBo.Key.memberId.toString(), memberId))){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        return "delete all favorite successful";
    }

    /**
     * @description: 获取所有收藏  深坑！！！ 商品可挂在多个菜单下
     * @param: memberId
     * @return list
     * @author Mr.Simple
     * @date 2018/12/27 10:02
     */
    @Override
    public List<GoodsFavoriteVo> getAllFavorite(Long memberId, boolean isReduction) {
        //要返回的字段
        List<GoodsFavoriteVo> goodsFavoriteVoList = new ArrayList<>();
        Date now = new Date();
        //查询该用户所有的收藏
        EntityWrapper<GoodsFavoriteBo> queryFavorit = new EntityWrapper<>();
        queryFavorit.eq(GoodsFavoriteBo.Key.memberId.toString(), memberId);
        queryFavorit.orderBy(GoodsFavoriteBo.Key.gmtCreate.toString(), false);
        List<GoodsFavoriteBo> favoriteBoList = iGoodsFavoriteBiz.selectList(queryFavorit);
        if(favoriteBoList.size() == 0){
            return goodsFavoriteVoList;
        }
        //遍历每一条收藏，包装数据
        favoriteBoList.forEach(item->{
            log.info("=================================================遍历favorite");
            GoodsFavoriteVo goodsFavoriteVo = new GoodsFavoriteVo();
            //将bo复制到vo里
            BeanUtils.copyProperties(item, goodsFavoriteVo);
            GoodsBo goodsBo = new GoodsBo();
            goodsBo = iGoodsBiz.selectById(item.getGoodsId());
            //将goodsBo设置到favoriteVo里
            goodsFavoriteVo.setGoodsBo(goodsBo);
            //查出评价数量
            Wrapper<GoodsCommentBo> commentQuery = new EntityWrapper<>();
            commentQuery.eq(GoodsCommentBo.Key.goodsId.toString(),item.getGoodsId());
            commentQuery.isNull(GoodsCommentBo.Key.forCommentId.toString());
            int reviewCount = 0;
            reviewCount = iGoodsCommentBiz.selectCount(commentQuery);
            if(reviewCount > 0)
                goodsFavoriteVo.setReviewCount(reviewCount);
            //查询出商品所在的一级菜单
            //先查出商品所在目录，商品可能挂在不同的目录下
            List<WebMenuGoodsBo> menuGoodsBoList = new ArrayList<>();
            menuGoodsBoList = iWebMenuGoodsBiz.selectList(Condition.create().eq(WebMenuGoodsBo.Key.goodsId.toString(), item.getGoodsId()));
            //查询该目录是否是一级目录
            Set<Long> menuSet = new HashSet<>();
            menuGoodsBoList.forEach(list->{
                log.info("=================================================遍历menu");
                log.info("getAllFavorite================================menuId:{}",list.getMenuId());
                WebMenuBo webMenuBo = new WebMenuBo();
                webMenuBo = iWebMenuBiz.selectById(list.getMenuId());
                while(webMenuBo.getParentid() != null){
                    webMenuBo = iWebMenuBiz.selectById(webMenuBo.getParentid());
                }
                log.info("一级目录==========================={}", webMenuBo.getId());
                menuSet.add(webMenuBo.getId());
            });
            List<Long> menuIdList = new ArrayList<>(menuSet);
            goodsFavoriteVo.setMenuIdList(menuIdList);
            //查询出图片
            EntityWrapper<GoodsImageBo> queryImage = new EntityWrapper<>();
            queryImage.eq(GoodsImageBo.Key.goodsId.toString(), item.getGoodsId());
            queryImage.eq(GoodsImageBo.Key.imgPosition.toString(), "MAJOR");
            GoodsImageBo goodsImageBo = iGoodsImageBiz.selectOne(queryImage);
            goodsFavoriteVo.setImgUrl(QiniuUtil.getFullPath(goodsImageBo.getImgUrl()));
            //查询商品价格,应当查询该商品在不在限时购里
            String priceValue = "";
            List<FlashGoodsSkuBo> inFlashSaleList = new ArrayList<>();
            Set<FlashGoodsSkuBo> flashGoodsSkuBoSet = new HashSet<>();
            //获取该商品所在的限时购信息
            List<FlashGoodsSkuBo> flashList = iFlashGoodsSkuBiz.selectList(Condition.create().eq(FlashGoodsSkuBo.Key.goodsId.toString(), item.getGoodsId()));
            if(flashList.size() != 0){
                flashList.forEach(list->{
                    //查看当前时间是否在活动中
                    EntityWrapper<FlashSaleBo> queryFlashSale = new EntityWrapper<>();
                    queryFlashSale.eq(FlashSaleBo.Key.id.toString(), list.getFlashSaleId());
                    queryFlashSale.le(FlashSaleBo.Key.endtime.toString(), now);
                    queryFlashSale.ge(FlashSaleBo.Key.starttime.toString(), now);
                    FlashSaleBo flashSaleBo = new FlashSaleBo();
                    flashSaleBo = iFlashSaleBiz.selectOne(queryFlashSale);
                    if(flashSaleBo != null){
                        flashGoodsSkuBoSet.add(list);
                    }
                });
                if(flashGoodsSkuBoSet != null)
                    inFlashSaleList.addAll(flashGoodsSkuBoSet);
            }
            //如果商品在限时购里，找出最低价
            if(inFlashSaleList.size() != 0){
                //重写排序,升序
                Collections.sort(inFlashSaleList, new Comparator<FlashGoodsSkuBo>() {
                    @Override
                    public int compare(FlashGoodsSkuBo o1, FlashGoodsSkuBo o2) {
                        return o1.getPrice().compareTo(o2.getPrice());
                    }
                });
                priceValue = inFlashSaleList.get(0).getPrice().toString();
            }else{
                priceValue = iGoodsPriceDao.getMinPriceMyGoodsId(item.getGoodsId()).toString();
            }
            BigDecimal price = new BigDecimal(priceValue);
            goodsFavoriteVo.setPrice(price);
            //计算差价
            BigDecimal spreadPrice = item.getOldPrice().subtract(price);
            //如果差价大于0，设置降价信息
            if(spreadPrice.intValue() > 0)
                goodsFavoriteVo.setSpreadPrice(spreadPrice);

            log.info("goodsStatus======================{}",goodsBo.getGoodsStatus());
            //查询库存（先查询是否下架）
            if(goodsBo.getGoodsStatus().equals(GoodsStatusEnum.ONLINE)){
                int inventory = 0;
                //查询库存
                List<GoodsInventoryBo> inventoryBoList = iGoodsInventoryBiz.selectList(Condition.create().eq(GoodsInventoryBo.Key.goodsId.toString(), item.getGoodsId()));
                if(inventoryBoList.size() == 0)
                    throw new WakaException(RavvExceptionEnum.GOODS_DATA_ERROR + "goodsId: " + goodsBo.getId() + " 商品库存被删除:by getAllFavorite");
                for (GoodsInventoryBo goodsInventoryBo : inventoryBoList) {
                    inventory += goodsInventoryBo.getSaleStockNum();
                }
                log.info("inventory:{}",inventory);
                if(inventory == 0)
                    goodsFavoriteVo.setGoodsStatus(InventoryTypeEnum.SALEOUT.getKey());
                else
                    goodsFavoriteVo.setGoodsStatus(InventoryTypeEnum.INSALE.getKey());
            }else
                goodsFavoriteVo.setGoodsStatus(InventoryTypeEnum.OFFLINE.getKey());
            //判断是否选择降价
            if(isReduction){
                if(goodsFavoriteVo.getSpreadPrice() != null)
                    goodsFavoriteVoList.add(goodsFavoriteVo);
            }
            else
                goodsFavoriteVoList.add(goodsFavoriteVo);

        });
        return goodsFavoriteVoList;
    }

    @Override
    public String deleteBatchFavorite(Long memberId, String goodsIds) {
        List<Long> goodsIdList = Tools.string.convertStringToLong(goodsIds);
        int deleteCount = 0;
        deleteCount = iGoodsFavoriteDao.deleteFavoriteByBatch(memberId, goodsIdList);
        if(deleteCount <= 0)
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        return "delete favorite successful";
    }

    /**
     * @description: 获取所有筛选信息
     * @param: memberId,filter
     * @return list
     * @author Mr.Simple
     * @date 2019/1/2 17:12
     */
    @Override
    public List<FavoriteFilterVo> getFilter(Long memberId, String filter) {
        //先获取所有商品id
        //查询该用户所有的收藏
        List<FavoriteFilterVo> favoriteFilterVoList = new ArrayList<>();
        Map<Long, Integer> classifyMap = new HashMap<>();
        Map<String, Integer> inventoryMap = new HashMap<>();
        EntityWrapper<GoodsFavoriteBo> queryFavorit = new EntityWrapper<>();
        queryFavorit.eq(GoodsFavoriteBo.Key.memberId.toString(), memberId);
        queryFavorit.orderBy(GoodsFavoriteBo.Key.gmtCreate.toString(), false);
        List<GoodsFavoriteBo> favoriteBoList = iGoodsFavoriteBiz.selectList(queryFavorit);
        if(favoriteBoList.size() != 0){
            //筛选类目
            if(FilterTypeEnum.MENU.getKey().equals(filter)){
                classifyMap = getMenuMap(favoriteBoList);
                //取得所有一级标题的名称
                for(Long menuId: classifyMap.keySet()){
                    FavoriteFilterVo favoriteFilterVo = new FavoriteFilterVo();
                    WebMenuBo webMenuBo = new WebMenuBo();
                    webMenuBo = iWebMenuBiz.selectById(menuId);
                    favoriteFilterVo.setId(webMenuBo.getId());
                    favoriteFilterVo.setFilterName(webMenuBo.getTitle());
                    favoriteFilterVo.setCount(classifyMap.get(menuId));
                    favoriteFilterVoList.add(favoriteFilterVo);
                }
            }
            //筛选库存
            if(FilterTypeEnum.INVENTORY.getKey().equals(filter)){
                inventoryMap = getInventoryMap(favoriteBoList);
                for(String inventoryType: inventoryMap.keySet()){
                    FavoriteFilterVo favoriteFilterVo = new FavoriteFilterVo();
                    favoriteFilterVo.setFilterName(inventoryType);
                    favoriteFilterVo.setCount(inventoryMap.get(inventoryType));
                    favoriteFilterVoList.add(favoriteFilterVo);
                }
            }
        }

        return favoriteFilterVoList;
    }

    @Override
    public List<GoodsFavoriteVo> getByInventoryFilter(Long memberId, String filter) {
        List<GoodsFavoriteVo> allList = getAllFavorite(memberId, false);
        List<GoodsFavoriteVo> filterList = new ArrayList<>();
        if(allList.size() != 0){
            allList.forEach(item->{
                if(item.getGoodsStatus().equals(filter)){
                    filterList.add(item);
                }
            });
        }

        return filterList;
    }

    @Override
    public List<GoodsFavoriteVo> getByMenuFilter(Long memberId, Long menuId) {
        List<GoodsFavoriteVo> allList = getAllFavorite(memberId, false);
        List<GoodsFavoriteVo> nullList = new ArrayList<>();
        if(allList.size() == 0)
            return nullList;
        Set<GoodsFavoriteVo> filterSet = new HashSet<>();
        allList.forEach(item->{
            List<Long> menuIdList = item.getMenuIdList();
            menuIdList.forEach(list->{
                log.info("getByMenuFilter==================menuId{}",list);
                if(list.equals(menuId))
                    filterSet.add(item);
            });

        });
        List<GoodsFavoriteVo> filterList = new ArrayList<>(filterSet);
        return filterList;
    }

    /**
     * @description: 深坑！！！  一个商品可挂在多个目录下
     * @param:
     * @return
     * @author Mr.Simple
     * @date 2019/1/3 15:26
     */
    public Map<Long, Integer> getMenuMap(List<GoodsFavoriteBo> favoriteBoList){
        Map<Long, Integer> classifyMap = new HashMap<>();
        favoriteBoList.forEach(item->{
            int count = 1;
            //查询所有分类
            //查询商品一级目录
            //先查出商品所在目录,商品可挂在多个目录下！！！启用list
            List<WebMenuGoodsBo> menuGoodsBoList = new ArrayList<>();
            menuGoodsBoList = iWebMenuGoodsBiz.selectList(Condition.create().eq(WebMenuGoodsBo.Key.goodsId.toString(), item.getGoodsId()));
            log.info("===============menuGoodsBoList.size():{}",menuGoodsBoList.size());
            if(menuGoodsBoList.size() == 0)
                throw new WakaException(RavvExceptionEnum.GOODS_DATA_ERROR + "goods don't have any menu");
            Set<Long> menuSet = new HashSet<>();
            //查询该目录是否是一级目录
            menuGoodsBoList.forEach(list->{
                log.info("=================商品目录:{}",list.getMenuId());
                WebMenuBo webMenuBo = new WebMenuBo();
                webMenuBo = iWebMenuBiz.selectById(list.getMenuId());
                while(webMenuBo.getParentid() != null){
                    log.info("webMenuBo.getId==============={}",webMenuBo.getId());
                    webMenuBo = iWebMenuBiz.selectById(webMenuBo.getParentid());
                }
                menuSet.add(webMenuBo.getId());
            });
            //遍历set
            for(Long menuId: menuSet){
                //判断集合中是否包含所有该目录id
                if(classifyMap.containsKey(menuId)){
                    log.info("menuId:{} contains count:{}",menuId,classifyMap.get(menuId));
                    classifyMap.put(menuId, classifyMap.get(menuId) + 1);
                }
                else
                    classifyMap.put(menuId, count);
            }
        });
        return classifyMap;
    }

    public Map<String, Integer> getInventoryMap(List<GoodsFavoriteBo> favoriteBoList){
        Map<String, Integer> inventoryMap = new HashMap<>();
        favoriteBoList.forEach(item->{
            int count = 1;
            //查询出商品信息
            GoodsBo goodsBo = new GoodsBo();
            goodsBo = iGoodsBiz.selectById(item.getGoodsId());
            if(goodsBo == null)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR + "商品已被删除");
            //如果在售，判断库存是否为空
            if(goodsBo.getGoodsStatus().equals(GoodsStatusEnum.ONLINE)){
                //查询出库存
                int inventory = 0;
                List<GoodsInventoryBo> inventoryBoList = iGoodsInventoryBiz.selectList(Condition.create().eq(GoodsInventoryBo.Key.goodsId.toString(), item.getGoodsId()));
                if(inventoryBoList.size() != 0){
                    for (GoodsInventoryBo goodsInventoryBo : inventoryBoList) {
                        if(goodsInventoryBo.getSaleStockNum() == null)
                            throw new WakaException(RavvExceptionEnum.GOODS_DATA_ERROR + "商品库存为null");
                        inventory += goodsInventoryBo.getSaleStockNum();
                    }
                }
                //商品不为空且在售，但数据表里无记录，商品被删除
                else{
                    throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR + "商品数据已被删除");
                }
                if(inventory == 0){
                    if(inventoryMap.containsKey(InventoryTypeEnum.SALEOUT.getKey()))
                        inventoryMap.put(InventoryTypeEnum.SALEOUT.getKey(), inventoryMap.get(InventoryTypeEnum.SALEOUT.getKey()) + 1);
                    else
                        inventoryMap.put(InventoryTypeEnum.SALEOUT.getKey(), count);
                }
                else {
                    if(inventoryMap.containsKey(InventoryTypeEnum.INSALE.getKey()))
                        inventoryMap.put(InventoryTypeEnum.INSALE.getKey(), inventoryMap.get(InventoryTypeEnum.INSALE.getKey()) + 1);
                    else
                        inventoryMap.put(InventoryTypeEnum.INSALE.getKey(), count);
                }
            }
            else {
                if(inventoryMap.containsKey(InventoryTypeEnum.OFFLINE.getKey()))
                    inventoryMap.put(InventoryTypeEnum.OFFLINE.getKey(), inventoryMap.get(InventoryTypeEnum.OFFLINE.getKey()) + 1);
                else
                    inventoryMap.put(InventoryTypeEnum.OFFLINE.getKey(), count);
            }
        });
        return inventoryMap;
    }
}
