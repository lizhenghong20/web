package cn.farwalker.ravv.service.goodscart.biz.impl;

import cn.farwalker.ravv.common.constants.UpdateCartTypeEnum;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsBiz;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsService;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.goods.base.model.ParseSkuExtVo;
import cn.farwalker.ravv.service.goods.constants.GoodsStatusEnum;
import cn.farwalker.ravv.service.goods.image.biz.IGoodsImageBiz;
import cn.farwalker.ravv.service.goods.inventory.biz.IGoodsInventoryBiz;
import cn.farwalker.ravv.service.goods.price.biz.IGoodsPriceBiz;
import cn.farwalker.ravv.service.goodscart.biz.IGoodsCartBiz;
import cn.farwalker.ravv.service.goodscart.biz.IGoodsCartService;
import cn.farwalker.ravv.service.goodscart.model.*;
import cn.farwalker.ravv.service.goodssku.skudef.biz.IGoodsSkuDefBiz;
import cn.farwalker.ravv.service.goodssku.skudef.biz.IGoodsSkuService;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuDefBo;
import cn.farwalker.ravv.service.goodssku.specification.dao.IGoodsSpecificationDefDao;
import cn.farwalker.ravv.service.goodssku.specification.model.GoodsSpecificationDefVo;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by asus on 2018/11/27.
 */
@Slf4j
@Service
public class GoodsCartServiceImpl implements IGoodsCartService{
    @Autowired
    private IGoodsCartBiz iGoodsCartBiz;

    @Autowired
    private IGoodsInventoryBiz iGoodsInventoryBiz;

    @Autowired
    private IGoodsService iGoodsService;

    @Autowired
    private IGoodsBiz iGoodsBiz;

    @Autowired
    IGoodsSkuService iGoodsSkuService;

    @Autowired
    IGoodsSkuDefBiz iGoodsSkuDefBiz;

    @Autowired
    IGoodsPriceBiz iGoodsPriceBiz;

    @Autowired
    IGoodsImageBiz iGoodsImageBiz;

    @Autowired
    IGoodsSpecificationDefDao iGoodsSpecificationDefDao;

     @Autowired
    IGoodsSkuDefBiz goodsSkuDefBiz;


    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public UpdateCartVo updateCart(Long memberId,
                                   String  selectedCartIds,
                                   Long updateCartId,
                                   Long skuId,
                                   Integer quantity,
                                   String type) throws Exception{

        //查询需要更新的购物车条目updateCartId是否存在
        if(updateCartId != null&&updateCartId != 0){
            GoodsCartBo query = iGoodsCartBiz.selectById(updateCartId);
            if(query == null)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        }

        //查询需要选中的条目selectedCartIdList是否存在
        List<Long> selectedCartIdList = new ArrayList<>();
        if(!Tools.string.isEmpty(selectedCartIds)){
            String[] ids = selectedCartIds.split(",");
            for(int i = 0; i < ids.length; i++){
                selectedCartIdList.add(Long.parseLong(ids[i]));
            }
            List<GoodsCartBo> queryList = iGoodsCartBiz.selectBatchIds(selectedCartIdList);
            if(queryList.size() != selectedCartIdList.size())
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        }

        UpdateCartVo updateCartVo = new UpdateCartVo();
        updateCartVo.setSelectedCartIds(selectedCartIdList);

        if(UpdateCartTypeEnum.PARSESKU.toString().equals(type)){
            GoodsCartBo insert = new GoodsCartBo();
            insert.setId(updateCartId);
            insert.setSkuId(skuId);
            iGoodsCartBiz.updateById(insert);
            updateCartVo.setUpdatedCartItem(getOneCartGood(updateCartId));
        }else if(UpdateCartTypeEnum.UPDATEQUANTITY.toString().equals(type)){
            updateQuantity(updateCartId,quantity);
            updateCartVo.setUpdatedCartItem(getOneCartGood(updateCartId));
        }else if(UpdateCartTypeEnum.DELETEONE.toString().equals(type)){
            iGoodsCartBiz.deleteById(updateCartId);
            selectedCartIdList.remove(updateCartId);
        }else if(UpdateCartTypeEnum.DELETESELECTED.toString().equals(type)){
            iGoodsCartBiz.deleteBatchIds(selectedCartIdList);
            selectedCartIdList.clear();
        }else if(UpdateCartTypeEnum.DELETEALL.toString().equals(type)){
            iGoodsCartBiz.delete(Condition.create().eq(GoodsCartBo.Key.memberId.toString(),memberId));
        }
        else if(UpdateCartTypeEnum.SELECT.toString().equals(type)){
            log.info("在选中状态中");
        }
        else{
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        if(updateCartVo.getSelectedCartIds()==null || updateCartVo.getSelectedCartIds().size() == 0){
            updateCartVo.setTotalAmount(totalAmount);
        }else{
            for(Long item: updateCartVo.getSelectedCartIds()){
                totalAmount = totalAmount.add(getOneCartGood(item).getSum());
            }
        }
        //总价要取两位小数
        totalAmount.setScale(2,BigDecimal.ROUND_HALF_UP);
        updateCartVo.setTotalAmount(totalAmount);
        return updateCartVo;
    }


    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean addToCart(Long memberId, Long goodsId, Long skuId, Integer quantity){
        //验证商品有没有下架,以及sku是否存在
        if(!iGoodsService.verifySkuId(skuId)){
            return false;
        }

        GoodsCartBo newItem = new GoodsCartBo();
        newItem.setMemberId(memberId);
        newItem.setGoodsId(goodsId);
        newItem.setSkuId(skuId);
        EntityWrapper<GoodsCartBo> wrapper = new EntityWrapper<>();
        wrapper.setEntity(newItem);
        GoodsCartBo queryResult = iGoodsCartBiz.selectOne(wrapper);
        //如果购物车里没有则新增
        if(queryResult == null){
            newItem.setAddTime(new Date());
            newItem.setQuantity(quantity);
            if(!iGoodsCartBiz.insert(newItem))
                throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
            return true;
        }else{
            //若有则更新
            queryResult.setAddTime(new Date());
            queryResult.setQuantity(quantity + queryResult.getQuantity());
            iGoodsCartBiz.updateById(queryResult);
            return true;
        }
    }

    public String updateToCart(Long memberId, List<RecoverToCartForm> list){

        for(RecoverToCartForm item : list){
            if(item.getGoodsId() == 0||item.getSkuId() == 0|| item.getQuantity()< 0){
                continue;
            }
            addToCart(memberId,item.getGoodsId(),item.getSkuId(),item.getQuantity());
        }
        return "added successfully!";
    }

    public List<GoodsCartVo> getCartGoods(Long memberId) throws Exception{

        EntityWrapper queryWrapper = new EntityWrapper<GoodsCartBo>();
        queryWrapper.eq(GoodsCartBo.Key.memberId.toString(),memberId);
        List<String> orderList  = new ArrayList<>();
        orderList.add(GoodsCartBo.Key.gmtCreate.toString());
        queryWrapper.orderDesc(orderList);
        List<GoodsCartBo> goodsCartList = iGoodsCartBiz.selectList(queryWrapper);
        //从购物车中查出的商品要进行筛选，剔除sku合法的商品。
        goodsCartList = filterGoodsCartList(goodsCartList);
        List<GoodsCartVo>  goodsCartVoList = new ArrayList<>();
        for(GoodsCartBo item : goodsCartList)
                goodsCartVoList.add(getOneCartGood(item.getId()));
        return goodsCartVoList;
    }

    private GoodsCartVo getOneCartGood(Long cartId) throws Exception{
        if(cartId==0){
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        }
        GoodsCartBo item = iGoodsCartBiz.selectById(cartId);
        if(item == null)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);

        BigDecimal sum = BigDecimal.ZERO;
        GoodsCartVo newVo = new GoodsCartVo();
        Tools.bean.copyProperties(item,newVo);
        //根据goodsId查出属性值列表
        List<GoodsSpecificationDefVo> allPropertyValues = iGoodsSpecificationDefDao.selectGoodsSpecificationDefVosById(item.getGoodsId());
        allPropertyValues.forEach(propertyItem->{
            propertyItem.setImgUrl(QiniuUtil.getFullPath(propertyItem.getImgUrl()));
        });
        newVo.setAllPropertyValues(iGoodsService.parseToPropertyLineVo(allPropertyValues));
        String propertyValueIds =  iGoodsSkuDefBiz.selectById(item.getSkuId()).getPropertyValueIds();
        //筛选出商品添加购物车时所选中的sku对应的属性值列表
        List<GoodsSpecificationDefVo> selectedPropertyValues = allPropertyValues.stream().filter(s->propertyValueIds.contains(s.getPropertyValueId().toString())).collect(Collectors.toList());
        //包装成前端需要的格式
        newVo.setSelectedPropertyValues(iGoodsService.parseToPropertyLineVo(selectedPropertyValues));

        //根据goodsId查出商品名
        GoodsBo goodsBo = iGoodsBiz.selectById(item.getGoodsId());
        if(goodsBo != null){
            newVo.setGoodsName(goodsBo.getGoodsName());
            newVo.setGoodsStatus(goodsBo.getGoodsStatus().getKey());
        }

        ParseSkuExtVo parseSkuExtVo =  iGoodsService.parseSku(item.getGoodsId(),item.getSkuId());
        newVo.setParseSkuExtVo(parseSkuExtVo);
        //获得一个购物车条目的总价，取两位小数
        sum = Tools.bigDecimal.mul(parseSkuExtVo.getPrice().doubleValue(), item.getQuantity()).setScale(2,BigDecimal.ROUND_HALF_UP);
        newVo.setSum(sum);

        newVo.setSkuList(iGoodsSkuDefBiz.selectList(Condition.create().eq(GoodsSkuDefBo.Key.goodsId.toString(),item.getGoodsId())));
        return newVo;

    }



    private void updateQuantity(Long goodsCartId, Integer quantity) {
        GoodsCartBo queryGoodsCartBo = new GoodsCartBo();
        queryGoodsCartBo.setId(goodsCartId);
        queryGoodsCartBo.setQuantity(quantity);
        if(!iGoodsCartBiz.updateById(queryGoodsCartBo))
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
    }


    @Override

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String deleteByGoodsCartId(Long goodsCartId) {
        if(goodsCartId == null||goodsCartId == 0)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        if(!iGoodsCartBiz.deleteById(goodsCartId))
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        return "deleted successfully!";
    }

    @Override
    public boolean deleteByGoodsIdList(Long memberId, List<Long> skuIdList){
        if(memberId == 0||skuIdList == null||skuIdList.size() == 0){
            throw new  WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        }
        for(Long item : skuIdList){
            GoodsCartBo  queryItem = iGoodsCartBiz.selectOne( Condition.create().eq(GoodsCartBo.Key.memberId.toString(),memberId)
                    .eq(GoodsCartBo.Key.skuId.toString(),item));
            if(queryItem != null)
                deleteByGoodsCartId(queryItem.getId());
        }
        return true;
    }


    //从购物车中查出的商品要进行筛选，剔除sku合法的商品。
    private List<GoodsCartBo>  filterGoodsCartList(List<GoodsCartBo> goodsCartList){
        return  goodsCartList.stream().filter(s->iGoodsService.verifySkuId(s.getSkuId())).collect(Collectors.toList());

    }



}
