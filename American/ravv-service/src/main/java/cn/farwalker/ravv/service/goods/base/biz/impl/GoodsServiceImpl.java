package cn.farwalker.ravv.service.goods.base.biz.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.farwalker.waka.core.WakaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.farwalker.ravv.common.constants.FlashSaleStatusEnum;
import cn.farwalker.ravv.common.constants.ImagePositionEnum;
import cn.farwalker.ravv.common.constants.SearchOrderRuleEnum;
import cn.farwalker.ravv.service.category.basecategory.biz.ICategoryService;
import cn.farwalker.ravv.service.category.property.biz.IBaseCategoryPropertyBiz;
import cn.farwalker.ravv.service.category.property.constants.PropertyTypeEnum;
import cn.farwalker.ravv.service.category.property.model.BaseCategoryPropertyBo;
import cn.farwalker.ravv.service.category.value.biz.IBaseCategoryPropertyValueBiz;
import cn.farwalker.ravv.service.category.value.model.BaseCategoryPropertyValueBo;
import cn.farwalker.ravv.service.flash.sale.biz.IFlashSaleBiz;
import cn.farwalker.ravv.service.flash.sale.model.FlashSaleBo;
import cn.farwalker.ravv.service.flash.sku.biz.IFlashGoodsSkuBiz;
import cn.farwalker.ravv.service.flash.sku.model.FlashGoodsSkuBo;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsBiz;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsService;
import cn.farwalker.ravv.service.goods.base.dao.IGoodsDao;
import cn.farwalker.ravv.service.goods.base.model.ActivityField;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.goods.base.model.GoodsDetailsVo;
import cn.farwalker.ravv.service.goods.base.model.GoodsListVo;
import cn.farwalker.ravv.service.goods.base.model.ParseSkuExtVo;
import cn.farwalker.ravv.service.goods.base.model.ParseSkuTemp;
import cn.farwalker.ravv.service.goods.base.model.PropertyLineVo;
import cn.farwalker.ravv.service.goods.base.model.PropsVo;
import cn.farwalker.ravv.service.goods.constants.GoodsStatusEnum;
import cn.farwalker.ravv.service.goods.constants.PropsUserInputEnum;
import cn.farwalker.ravv.service.goods.image.biz.IGoodsImageBiz;
import cn.farwalker.ravv.service.goods.image.model.GoodsImageBo;
import cn.farwalker.ravv.service.goods.inventory.biz.IGoodsInventoryBiz;
import cn.farwalker.ravv.service.goods.inventory.model.GoodsInventoryBo;
import cn.farwalker.ravv.service.goods.price.biz.IGoodsPriceBiz;
import cn.farwalker.ravv.service.goods.price.dao.IGoodsPriceDao;
import cn.farwalker.ravv.service.goods.price.model.GoodsPriceBo;
import cn.farwalker.ravv.service.goods.utils.GoodsUtil;
import cn.farwalker.ravv.service.goodsext.comment.biz.IGoodsCommentBiz;
import cn.farwalker.ravv.service.goodsext.comment.biz.IGoodsCommentService;
import cn.farwalker.ravv.service.goodsext.comment.model.GoodsCommentBo;
import cn.farwalker.ravv.service.goodsext.consult.biz.IGoodsConsultBiz;
import cn.farwalker.ravv.service.goodsext.consult.model.GoodsConsultBo;
import cn.farwalker.ravv.service.goodsext.favorite.biz.IGoodsFavoriteBiz;
import cn.farwalker.ravv.service.goodsext.favorite.model.GoodsFavoriteBo;
import cn.farwalker.ravv.service.goodsext.viewlog.biz.IGoodsViewLogService;
import cn.farwalker.ravv.service.goodssku.skudef.biz.IGoodsSkuDefBiz;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuDefBo;
import cn.farwalker.ravv.service.goodssku.skudef.model.SkuPriceInventoryVo;
import cn.farwalker.ravv.service.goodssku.skuspec.biz.IGoodsSkuSpecBiz;
import cn.farwalker.ravv.service.goodssku.specification.biz.IGoodsSpecificationDefBiz;
import cn.farwalker.ravv.service.goodssku.specification.dao.IGoodsSpecificationDefDao;
import cn.farwalker.ravv.service.goodssku.specification.model.GoodsSpecificationDefBo;
import cn.farwalker.ravv.service.goodssku.specification.model.GoodsSpecificationDefVo;
import cn.farwalker.ravv.service.web.menu.biz.IWebMenuService;
import cn.farwalker.ravv.service.web.menu.model.WebMenuBo;
import cn.farwalker.waka.components.sequence.SequenceManager;
import cn.farwalker.waka.core.base.controller.ControllerUtils;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.orm.core.BaseBo;
import com.cangwu.frame.orm.core.annotation.LoadJoinValueImpl;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GoodsServiceImpl implements IGoodsService{
	private static final Logger log = LoggerFactory.getLogger(GoodsServiceImpl.class);
	@Autowired
	private IGoodsBiz goodsBiz;
 
	@Autowired
	private IGoodsImageBiz imageBiz;

	@Resource
	private IGoodsSkuDefBiz goodsSkuDefBiz;
	
	@Resource
	private IGoodsPriceBiz goodsPriceBiz;

	@Resource
	private IGoodsInventoryBiz goodsInventoryBiz;

	@Resource
	private IGoodsCommentBiz goodsCommentBiz;

	@Resource
	private IGoodsConsultBiz goodsConsultBiz;
 
	@Resource
	private IGoodsImageBiz goodsImageBiz;
 

	@Resource
	private IGoodsSpecificationDefBiz goodsValueBiz; 


	@Autowired
    private IBaseCategoryPropertyValueBiz baseCategoryPropertyValueBiz;

	@Resource
    private IBaseCategoryPropertyBiz categoryPropertyBiz;
	@Autowired
	private IGoodsPriceDao goodsPriceDao;

	@Autowired
	private IGoodsDao goodsDao;

	@Autowired
	private IFlashGoodsSkuBiz iFlashGoodsSkuBiz;

	@Autowired
	private IFlashSaleBiz iFlashSaleBiz;

	@Autowired
	private IGoodsSpecificationDefDao iGoodsSpecificationDefDao;

	@Autowired
	private IWebMenuService iWebMenuService;

	@Autowired
	private IGoodsViewLogService iGoodsViewLogService;

	@Autowired
	private IGoodsFavoriteBiz iGoodsFavoriteBiz;

	@Resource
	private ICategoryService categoryService;

	/**
	 * 取下一个流水号
	 * @return
	 */
	public String getNextCode(){
		SequenceManager sm = SequenceManager.getInstance(GoodsBo.TABLE_NAME);
		sm.setStep(10);
		long g = sm.nextValue();
		String s = "G" + Tools.number.formatNumber(g, 4);
		return s;
	}

	/**
	 * @description: 解耦合，单独获取skuId
	 * @param: goodsId,propertyList
	 * @return skuId
	 * @author Mr.Simple
	 * @date 2018/12/3 10:08
	 */
	public Long parseSkuToId(Long goodsId, String propertyValueIds){
		List<String> propertyList = Tools.string.convertStringList(propertyValueIds);
		//根据goodsId和propertyValuesIds查出skuId
		Wrapper<GoodsSkuDefBo> skuQuery = new EntityWrapper<>();
		skuQuery.eq(GoodsSkuDefBo.Key.goodsId.toString(), goodsId);
		for (String propertyId : propertyList) {
			if(Tools.string.isNotEmpty(propertyId))
				skuQuery.like(GoodsSkuDefBo.Key.propertyValueIds.toString(), propertyId);
		}
		GoodsSkuDefBo goodsSkuDefBo = goodsSkuDefBiz.selectOne(skuQuery);
		if(goodsSkuDefBo == null)
			throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
		long skuId = goodsSkuDefBo.getId();
		log.info("skuId:{}",skuId);
		return skuId;
	}

	/**
	 * @description: 通过goodsId和属性id查询商品售价和销量
	 * @param: goodsId,propertyValueIds
	 * @return map<price,stockNum>
	 * @author Mr.Simple
	 * @date 2018/11/19 14:18
	 */
	@Override
	public ParseSkuExtVo parseSku(Long goodsId, String propertyValueIds) throws Exception{
		long skuId = parseSkuToId(goodsId, propertyValueIds);
		return parseSku(goodsId,skuId);

	}
	/**
	 * @description: 找出商品价格和库存（为下单服务）
	 * @param  goodsId
	 * @param  {@link GoodsSpecificationDefBo#getId()}
	 * @return
	 */
	@Override
	public SkuPriceInventoryVo getGoodsSkuPriceVo(Long goodsId, List<Long> propertyValueIds ){
		Wrapper<GoodsSkuDefBo> skuQuery = new EntityWrapper<>();
		skuQuery.eq(GoodsSkuDefBo.Key.goodsId.toString(), goodsId);
		final String pvfn =  GoodsSkuDefBo.Key.propertyValueIds.toString();
		for (Long propertyId : propertyValueIds) {
			skuQuery.like(pvfn,"(" + propertyId + ")");
		}
		GoodsSkuDefBo goodsSkuDefBo = goodsSkuDefBiz.selectOne(skuQuery);
		SkuPriceInventoryVo vo = getSkuPriceInventoryVo(goodsSkuDefBo);
		return vo;
	}

	private SkuPriceInventoryVo getSkuPriceInventoryVo(GoodsSkuDefBo goodsSkuDefBo){
		if(goodsSkuDefBo == null){
			throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
		}
		Wrapper<GoodsPriceBo> prcQuery = new EntityWrapper<>();
		prcQuery.eq(GoodsPriceBo.Key.skuId.toString(), goodsSkuDefBo.getId());
		GoodsPriceBo priceBo = goodsPriceBiz.selectOne(prcQuery);
		
		Wrapper<GoodsInventoryBo> invQuery = new EntityWrapper<>();
		invQuery.eq(GoodsInventoryBo.Key.skuId.toString(), goodsSkuDefBo.getId());
		GoodsInventoryBo invBo = goodsInventoryBiz.selectOne(invQuery);
		
		SkuPriceInventoryVo vo =new SkuPriceInventoryVo(invBo,priceBo);
		Tools.bean.cloneBean(goodsSkuDefBo, vo);
		return vo;
	}
	@Override
	public SkuPriceInventoryVo getGoodsSkuPriceVo(Long skuId){
		GoodsSkuDefBo goodsSkuDefBo = goodsSkuDefBiz.selectById(skuId);
		SkuPriceInventoryVo vo = getSkuPriceInventoryVo(goodsSkuDefBo);
		return vo;
	}
	
	/**
	 * @description: 通过goodsId和属性id查询商品售价和销量
	 * @param: goodsId,skuId
	 * @return map<price,stockNum>
	 * @author Mr.Simple
	 * @date 2018/11/19 14:18
	 */
	@Override
	public ParseSkuExtVo parseSku(Long goodsId, Long skuId ) throws Exception{

		//根据skuId和goodsId查出销售价格
		Wrapper<GoodsPriceBo> priceQuery = new EntityWrapper<>();
		priceQuery.eq(GoodsPriceBo.Key.goodsId.toString(), goodsId);
		priceQuery.eq(GoodsPriceBo.Key.skuId.toString(), skuId);
		GoodsPriceBo priceBo = goodsPriceBiz.selectOne(priceQuery);
		BigDecimal price = null;
		if(priceBo != null)
			price = priceBo.getPrice();


		//根据skuId和goodsId查出库存数量,库存数应该减去库存表中的冻结的数量
		Wrapper<GoodsInventoryBo> inventoryQuery = new EntityWrapper<>();
		inventoryQuery.eq(GoodsInventoryBo.Key.skuId.toString(), skuId);
		inventoryQuery.eq(GoodsInventoryBo.Key.goodsId.toString(), goodsId);
		GoodsInventoryBo inventoryBo = goodsInventoryBiz.selectOne(inventoryQuery);
		Integer  saleStockNum = 0;
		if(inventoryBo != null){
			int freeze = inventoryBo.getFreeze()==null? 0:inventoryBo.getFreeze();
			//库存要减掉冻结，以及未来限时购的预定
			if(countFutureFlashSaleReserve(goodsId,skuId) > (inventoryBo.getSaleStockNum() - freeze))
				throw new WakaException("未来限时购的库存预定数量已经超出现有的库存");
			saleStockNum = inventoryBo.getSaleStockNum() - freeze - countFutureFlashSaleReserve(goodsId,skuId);
		}


		//从sku中取图
		GoodsSkuDefBo goodsSkuDefBo = goodsSkuDefBiz.selectById(skuId);
		String goodsImageUrl = null;
		if(goodsSkuDefBo != null)
			goodsImageUrl = goodsSkuDefBo.getImageUrl();

		ParseSkuTemp parseSkuTemp = new ParseSkuTemp();
		if(goodsImageUrl != null)
			parseSkuTemp.setGoodsImageUrl(QiniuUtil.getFullPath(goodsImageUrl));
		parseSkuTemp.setSkuId(skuId);
		parseSkuTemp.setInFlashSale(false);
		parseSkuTemp.setPrice(price);
		parseSkuTemp.setSaleStockNum(saleStockNum);
		parseSkuTemp.setCurrentTime(new Date());

		//根据goodsId 和 skuId 查出限时购活动相关信息,并更新
		FlashGoodsSkuBo flashGoodsSkuBo = selectFlashSale(goodsId,skuId);
		//如果在限时购中
		if (flashGoodsSkuBo != null) {
			Date currentTime = new Date();
			Long flashSaleId = flashGoodsSkuBo.getFlashSaleId();
			FlashSaleBo flashSaleBo = iFlashSaleBiz.selectById(flashSaleId);
			if (flashSaleBo != null) {
				parseSkuTemp.setInFlashSale(true);
				if(flashSaleBo.getStarttime().getTime() < currentTime.getTime()&&
						flashSaleBo.getEndtime().getTime() > currentTime.getTime())
					parseSkuTemp.setUnderWay(true);
				if(flashSaleBo.getFreezetime().getTime() < currentTime.getTime()&&
						flashSaleBo.getStarttime().getTime() > currentTime.getTime())
					parseSkuTemp.setFrozen(true);
				int flashActivityReserve = flashGoodsSkuBo.getInventory();

				parseSkuTemp.setFlashActivityReserve(flashActivityReserve);
				//如果在限时购当中，库存应该减去限时购中的冻结数
				parseSkuTemp.setSaleStockNum(flashActivityReserve - parseSkuTemp.getFlashSaleCount() - (flashGoodsSkuBo.getFreezeCount()==null?0:flashGoodsSkuBo.getFreezeCount()));
				log.info("saleCount:{}",flashGoodsSkuBo.getSaleCount());
				parseSkuTemp.setFlashSaleCount(flashGoodsSkuBo.getSaleCount());
				parseSkuTemp.setActivityPrice(flashGoodsSkuBo.getPrice());
				parseSkuTemp.setTitle(flashSaleBo.getTitle());
				parseSkuTemp.setStartTime(flashSaleBo.getStarttime());
				parseSkuTemp.setEndTime(flashSaleBo.getEndtime());

			}
		}

		String propertyValueIds = goodsSkuDefBo.getPropertyValueIds();
		List<GoodsSpecificationDefVo> propertyValues = iGoodsSpecificationDefDao.selectGoodsSpecificationDefVosById(goodsId);
		String valueNames =
				propertyValues.stream().filter(s->propertyValueIds.contains(s.getPropertyValueId().toString()))
						.map(s->s.getTmpPropertyName()+":" + s.getValueName()+";").collect(Collectors.joining()).toString();

		log.info("skuId:{}",skuId);
		parseSkuTemp.setValueNames(valueNames);
		ParseSkuExtVo result = new ParseSkuExtVo();
		result.setOriginPrice(price);
		//如果当前sku是在限时购中
		if(parseSkuTemp.isInFlashSale()){
			result.setPrice(parseSkuTemp.getActivityPrice());
			result.setSaleCount(parseSkuTemp.getFlashSaleCount());
			result.setSaleStockNum(parseSkuTemp.getSaleStockNum());
			result.setValueNames(parseSkuTemp.getValueNames());
			result.setGoodsImageUrl(parseSkuTemp.getGoodsImageUrl());
			result.setSkuId(parseSkuTemp.getSkuId());
			result.setCurrentTime(parseSkuTemp.getCurrentTime());
			result.setInFlashSale(parseSkuTemp.isInFlashSale());
			ActivityField activityField = new ActivityField();
			activityField.setEndTime(parseSkuTemp.getEndTime());
			activityField.setFrozen(parseSkuTemp.isFrozen());
			activityField.setUnderWay(parseSkuTemp.isUnderWay());
			activityField.setTitle(parseSkuTemp.getTitle());
			activityField.setStartTime(parseSkuTemp.getStartTime());
			result.setActivityField(activityField);
		}else{
			Tools.bean.copyProperties(parseSkuTemp,result);
		}

		return result;
	}

	/**
	 * 根据goodsId 和 skuId 查出限时购活动相关信息,并更新
	 * @param goodsId
	 * @param skuId
     * @return
     */
	private FlashGoodsSkuBo selectFlashSale(Long goodsId,Long skuId) throws Exception{
		List<FlashGoodsSkuBo> flashGoodsList =   iFlashGoodsSkuBiz.selectList(Condition.create().eq(FlashGoodsSkuBo.Key.goodsId.toString(), goodsId)
						        						 .eq(FlashGoodsSkuBo.Key.goodsSkuDefId.toString(), skuId));

		if(flashGoodsList == null||flashGoodsList.size() == 0)
			return null;
		List<Long> flashSaleIdList = flashGoodsList.stream().map(s->s.getFlashSaleId()).collect(Collectors.toList());
		if(new HashSet(flashSaleIdList).size() != flashSaleIdList.size())
			throw new WakaException("同一件商品不能多次添加到同一个限时购");

		List<FlashSaleBo> flashSaleList = iFlashSaleBiz.selectBatchIds(flashSaleIdList);
		if(flashSaleList == null||flashSaleList.size() == 0)
			return null;
		FlashSaleBo valid = pickValidFlashSaleCurrent(flashSaleList);
		if(valid != null){
			List<FlashGoodsSkuBo> list =  flashGoodsList.stream().filter(s->s.getFlashSaleId().equals(valid.getId())).collect(Collectors.toList());
			return list.size() == 0? null:list.get(0);
		}
		return null;
	}

	/**
	 * 统计出限时购在未来的时间里预定的库存之和
	 * @param goodsId
	 * @param skuId
	 * @return
	 * @throws Exception
     */
	private Integer countFutureFlashSaleReserve(Long goodsId,Long skuId) throws Exception{
		List<FlashGoodsSkuBo> flashGoodsList =   iFlashGoodsSkuBiz.selectList(Condition.create().eq(FlashGoodsSkuBo.Key.goodsId.toString(), goodsId)
				.eq(FlashGoodsSkuBo.Key.goodsSkuDefId.toString(), skuId));

		Integer sum = 0;
		if(flashGoodsList == null||flashGoodsList.size() == 0)
			return sum;
		List<Long> flashSaleIdList = flashGoodsList.stream().map(s->s.getFlashSaleId()).collect(Collectors.toList());
		if(new HashSet(flashSaleIdList).size() != flashSaleIdList.size())
			throw new WakaException("同一件商品不能多次添加到同一个限时购");

		List<FlashSaleBo> flashSaleList = iFlashSaleBiz.selectBatchIds(flashSaleIdList);
		if(flashSaleList == null||flashSaleList.size() == 0)
			return sum;
		List<FlashSaleBo> validList = pickValidFlashSaleFuture(flashSaleList);
		if(validList != null && validList.size() != 0){
			List<Long> validFlashIdList = validList.stream().map(s->s.getId()).collect(Collectors.toList());
			List<FlashGoodsSkuBo> validFlashGoodsList =  flashGoodsList.stream().filter(s->validFlashIdList.contains(s.getFlashSaleId())).collect(Collectors.toList());
			for(FlashGoodsSkuBo item : validFlashGoodsList){
				sum += item.getInventory();
			}
		}
		return sum;

	}

	/**
	 * 在多个限时购列表中选取唯一一个对于当前时间生效的（即当前时间落在此限时购条目的冻结时间和结束时间的区间之内）的限时购条目
	 * 如果符合上述条件的限时购有多个，则抛出异常
	 * @param flashSaleList
	 * @return
     */
	private FlashSaleBo pickValidFlashSaleCurrent(List<FlashSaleBo> flashSaleList){
		flashSaleList.sort(Comparator.comparing(FlashSaleBo::getFreezetime));
		Date  currentTime = new Date();
		if(!isFlashSaleValid( flashSaleList )){
			throw new WakaException("此商品所处的多个限时购在冻结--结束时间区间上有交叠");
		}else{
			List<FlashSaleBo>	 list = flashSaleList.stream().filter(item->currentTime.getTime() > item.getFreezetime().getTime() && currentTime.getTime() < item.getEndtime().getTime()).collect(Collectors.toList());
			return list.size() == 0 ? null : list.get(0);
		}
	}


	/**
	 * 在多个限时购列表中选取所有对于未来时间生效的（即当前时间小于其冻结时间）的限时购条目
	 * 如果符合上述条件的限时购有多个，则抛出异常
	 * @param flashSaleList
	 * @return
	 */
	private List<FlashSaleBo> pickValidFlashSaleFuture(List<FlashSaleBo> flashSaleList){
		flashSaleList.sort(Comparator.comparing(FlashSaleBo::getFreezetime));
		Date  currentTime = new Date();
		if(!isFlashSaleValid( flashSaleList )){
			throw new WakaException("此商品所处的多个限时购在冻结--结束时间区间上有交叠");
		}else{
			return  flashSaleList.stream().filter(item->currentTime.getTime() < item.getFreezetime().getTime()).collect(Collectors.toList());
		}
	}

	//判断多个限时购在冻结--结束时间区间上是否合法(没有交叠即为合法)
	boolean isFlashSaleValid(List<FlashSaleBo> flashSaleList){
		Date  currentTime = new Date();
		for(int i = 0; i< flashSaleList.size()-1; i++ ){
			//如果限时购已经过期，不对其进行判断
			if(flashSaleList.get(i).getEndtime().getTime() < currentTime.getTime())
				continue;
			if(flashSaleList.get(i).getEndtime().getTime() > flashSaleList.get(i+1).getFreezetime().getTime())
				return false;
		}
		return true;
	}






	/**
	 * @description: 获取商品详情
	 * @param: memberId,goodsId
	 * @return GoodsDetailVo
	 * @author Mr.Simple
	 * @date 2018/11/22 14:37
	 */
	public GoodsDetailsVo updateAndGetDetails(long memberId,long goodsId){
		if(!verifyGoodId(goodsId)){
			throw new WakaException(RavvExceptionEnum.GOODS_HAS_BEEN_TAKEN_OFF_THE_SHELVES);
		}

		GoodsDetailsVo goodsDetailsVo = new GoodsDetailsVo();
		goodsDetailsVo.setId(goodsId);

		//记录足迹，查询是否收藏
		if(memberId != 0){
			if(!iGoodsViewLogService.addViewLog(memberId, goodsId))
				throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
			//查看该用户是否收藏该商品
			boolean isFavorite = false;
			EntityWrapper<GoodsFavoriteBo> queryFavorite = new EntityWrapper<>();
			queryFavorite.eq(GoodsFavoriteBo.Key.memberId.toString(), memberId);
			queryFavorite.eq(GoodsFavoriteBo.Key.goodsId.toString(), goodsId);
			int favoriteCount = iGoodsFavoriteBiz.selectCount(queryFavorite);
			if(favoriteCount == 1)
				goodsDetailsVo.setFavorite(true);
		}

		//获取所有图片并拼装
		LoadJoinValueImpl.load(goodsBiz, goodsDetailsVo);
		goodsDetailsVo.getImageDetails();
		goodsDetailsVo.getImageMajor();
		goodsDetailsVo.getImageTitles();

		String shippingType = null;
		String shippingTo = null;
		if(memberId != 0){
			shippingType = "test";
			shippingTo = "test";
			goodsDetailsVo.setShippingTo(shippingTo);
			goodsDetailsVo.setShippingType(shippingType);
		}

		//获取最高价和最低价
		BigDecimal highestPrice = goodsPriceDao.getMaxPriceMyGoodsId(goodsId);
		BigDecimal lowestPrice = goodsPriceDao.getMinPriceMyGoodsId(goodsId);
		goodsDetailsVo.setHighestPrice(highestPrice);
		goodsDetailsVo.setLowestPrice(lowestPrice);


		//获取所有评价数
		Wrapper<GoodsCommentBo> commentQuery = new EntityWrapper<>();
		commentQuery.eq(GoodsCommentBo.Key.goodsId.toString(),goodsId);
		commentQuery.isNull(GoodsCommentBo.Key.forCommentId.toString());
		int commentCount = 0;
		commentCount = goodsCommentBiz.selectCount(commentQuery);
		if(commentCount != 0)
			goodsDetailsVo.setReview(commentCount);

		//获取所有咨询数
		Wrapper<GoodsConsultBo> consultQuery = new EntityWrapper<>();
		consultQuery.eq(GoodsConsultBo.Key.goodsId.toString(),goodsId);
		consultQuery.isNull(GoodsConsultBo.Key.replyContent.toString());
		int consultCount = goodsConsultBiz.selectCount(consultQuery);
		goodsDetailsVo.setAnsweredQuestions(consultCount);
		//获取该商品下所有propertyValue
		List<GoodsSpecificationDefVo> goodsSpecificationDefVos = iGoodsSpecificationDefDao.selectGoodsSpecificationDefVosById(goodsId);
		goodsSpecificationDefVos.forEach(item->{
			if(item.getIsimg() == true)
				item.setImgUrl(QiniuUtil.getFullPath(item.getImgUrl()));
		});
		goodsDetailsVo.setAllPropertyValues(parseToPropertyLineVo(goodsSpecificationDefVos));

		//通过goodsId查出goods所有信息
		EntityWrapper<GoodsBo> goodsBoQuery = new EntityWrapper<>();
		goodsBoQuery.eq(GoodsBo.Key.id.toString(),goodsId);
		GoodsBo good = goodsBiz.selectOne(goodsBoQuery);
		BeanUtils.copyProperties(good,goodsDetailsVo);
		if(goodsDetailsVo.getFavoriteCount() == null)
			goodsDetailsVo.setFavoriteCount(0);
		if(goodsDetailsVo.getPoint() == null)
			goodsDetailsVo.setPoint(BigDecimal.ZERO);
		return goodsDetailsVo;
	}

	public List<PropertyLineVo> parseToPropertyLineVo(List<GoodsSpecificationDefVo> sourceList){
		if(sourceList == null || sourceList.size() == 0)
			return null;

		Long prePropertyId = sourceList.get(0).getPropertyId();
		PropertyLineVo propertyLineVo = new PropertyLineVo();
		propertyLineVo.setPropertyValueList(new ArrayList<>());
		propertyLineVo.setPropertyId(sourceList.get(0).getPropertyId());
		propertyLineVo.setImage(sourceList.get(0).getIsimg());
		if(!Tools.string.isEmpty(sourceList.get(0).getCustomValueName()))
			propertyLineVo.setPropertyName(sourceList.get(0).getCustomValueName());
		else
			propertyLineVo.setPropertyName(sourceList.get(0).getTmpPropertyName());

		List<PropertyLineVo> resultList = new ArrayList<>();
		for(GoodsSpecificationDefVo item:sourceList){
			if(!item.getPropertyId().equals(prePropertyId)){
				//保存之前的数据
				resultList.add(propertyLineVo);
				//开始新的数据存入
				propertyLineVo = new PropertyLineVo();
				propertyLineVo.setPropertyValueList(new ArrayList<>());
				propertyLineVo.getPropertyValueList().add(item);
				propertyLineVo.setPropertyId(item.getPropertyId());
				propertyLineVo.setImage(item.getIsimg());
				prePropertyId = item.getPropertyId();
				if(!Tools.string.isEmpty(item.getCustomValueName()))
					propertyLineVo.setPropertyName(item.getCustomValueName());
				else
					propertyLineVo.setPropertyName(item.getTmpPropertyName());
			}else{
				propertyLineVo.getPropertyValueList().add(item);
			}

		}
		resultList.add(propertyLineVo);
		return resultList;
	}



	/**
	 *搜索关键字，或搜索目录
	 * @param menuId
	 * @param keyWords
	 * @param floor
	 * @param ceiling
	 * @param currentPage
     * @param pageSize
     * @return
     */
	public List<GoodsListVo> search(Long menuId,String keyWords,
									BigDecimal floor, BigDecimal ceiling,
									String oneOfThree,
									Boolean freeShipment,
									Integer goodsPoint,
									int currentPage,int pageSize){
		Page<GoodsListVo> page = new Page<>(currentPage,pageSize);
		List<String> descList = new ArrayList<>();
		List<String> ascList = new ArrayList<>();
		List<Long> menuIdList = new ArrayList<>();
		if(menuId != 0){
			List<WebMenuBo> menuList = iWebMenuService.getAllSubMenus(menuId);
			if(menuList.size() != 0){
				menuList.forEach(item->{
					menuIdList.add(item.getId());
				});
			}
		}
		//设置价格排序
		if(oneOfThree.equals(SearchOrderRuleEnum.PRICEASC.getKey())){
			ascList.add("lowest_price");
		}else if(oneOfThree.equals(SearchOrderRuleEnum.PRICEDESC.getKey())){
			descList.add("lowest_price");
		}else if(oneOfThree.equals(SearchOrderRuleEnum.ORDER.getKey())){//设置历史销售数量排序,降序
			descList.add("order_goods_count");
		}
		else{
			descList.add(GoodsBo.Key.gmtCreate.toString());//默认按插入时间，降序排列
		}

		page.setAscs(ascList);
		page.setDescs(descList);
		if(Tools.string.isEmpty(keyWords)){
			keyWords = null;
		}else{
			keyWords = keyWords.toLowerCase();
		}
		//不筛选运费
		if(freeShipment!=null&&freeShipment)
			freeShipment = null;

		List<GoodsListVo> result =  goodsDao.selectGoodsListBySearch(page,keyWords,floor,ceiling,menuIdList,freeShipment, goodsPoint);
		result.forEach(item->{
			item.setImgUrl(QiniuUtil.getFullPath(item.getImgUrl()));
		});
		return result;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public boolean insert(GoodsBo bo, List<String> imageTitles,
			String majorImage, List<String> imageDetails,String videoTitle,String videoDetail) {
		if(Tools.string.isEmpty(bo.getGoodsCode())){
			String code = this.getNextCode();
			bo.setGoodsCode(code);
		}
		StringBuilder cps = categoryService.getCategoryPaths(bo.getLeafCategoryId());
		if(cps == null){
			throw new WakaException("商品分类不能为空");
		}
		bo.setCategoryPath(cps.toString());
		goodsBiz.insert(bo);
		
		Long goodsId = bo.getId();
		insertImages(goodsId, imageTitles, majorImage, imageDetails,videoTitle,videoDetail);
		return true;
	}
 

	private List<GoodsImageBo> insertImages(Long goodsId, List<String> imageTitles,
			String majorImage, List<String> imageDetails,String videoTitle,String videoDetail){
		if(Tools.string.isEmpty(majorImage) && Tools.collection.isNotEmpty(imageTitles)){
			majorImage = imageTitles.get(0);
			//imageTitles.remove(0);
		}
		List<GoodsImageBo> images = getImageBos(Arrays.asList(majorImage), ImagePositionEnum.MAJOR, goodsId);
		List<GoodsImageBo> titles = getImageBos(imageTitles, ImagePositionEnum.TITLE, goodsId);
		List<GoodsImageBo> details = getImageBos(imageDetails, ImagePositionEnum.DETAIL, goodsId);
		GoodsImageBo videoTitleBo = getImageBo(videoTitle, ImagePositionEnum.TITLE, goodsId);
		GoodsImageBo videoDetailBo = getImageBo(videoDetail, ImagePositionEnum.DETAIL, goodsId);
		
		for(int i =titles.size()-1;i>=0;i--){
			String img =  titles.get(i).getImgUrl();
			if(img.indexOf(majorImage)>=0){
				titles.remove(i);//包含了主图
				break;
			}
		}
		
		images.addAll(titles);
		images.addAll(details);
		
		if(videoTitleBo!=null){
			videoTitleBo.setIsVideo(Boolean.TRUE);
			images.add(videoTitleBo);
		}
		if(videoDetailBo!=null){
			videoDetailBo.setIsVideo(Boolean.TRUE);
			images.add(videoDetailBo);
		}
		
		if(Tools.collection.isNotEmpty(images)){
			imageBiz.insertBatch(images);
		}
		return images;
	}

	private List<GoodsImageBo> getImageBos(List<String> imgs, ImagePositionEnum type, Long goodsId){
		if(Tools.collection.isEmpty(imgs)){
			return Collections.emptyList();
		}
		List<GoodsImageBo> rs = new ArrayList<>(imgs.size());
		for(int i =0,size = imgs.size();i < size;i++){
			GoodsImageBo e = getImageBo(imgs.get(i), type, goodsId);
			if(e!=null){
				e.setSequence(Integer.valueOf(i));
				rs.add(e);
			}
		}
		return rs;
	}
	private GoodsImageBo getImageBo(String imgs, ImagePositionEnum type, Long goodsId){
		if(Tools.string.isEmpty(imgs)){
			return null;
		}
		
		GoodsImageBo e = new GoodsImageBo();
		e.setGoodsId(goodsId);
		e.setImgPosition(type);
		
		String url =  GoodsUtil.getCdnRelativePath(imgs);
		e.setImgUrl(url);
		boolean video = GoodsUtil.isVideo(url);
		e.setIsVideo(Boolean.valueOf(video));
		return e;
	}
	
	private boolean deleteImages(ImagePositionEnum type, Long goodsId){
		Wrapper<GoodsImageBo> query = new EntityWrapper<>();
		query.eq(GoodsImageBo.Key.goodsId.toString(), goodsId);
		query.eq(GoodsImageBo.Key.imgPosition.toString(), type);
		boolean rs = imageBiz.delete(query);
		return rs;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public boolean update(GoodsBo bo, List<String> imageTitles,
			String majorImage, List<String> imageDetails,String videoTitle,String videoDetail) {
		StringBuilder cps = categoryService.getCategoryPaths(bo.getLeafCategoryId());
		if(cps == null){
			throw new WakaException("商品分类不能为空");
		}
		bo.setCategoryPath(cps.toString());
		goodsBiz.updateById(bo);
		
		Long goodsId = bo.getId();
		if(imageTitles!=null){
			deleteImages(ImagePositionEnum.TITLE, goodsId);
		}
		if(imageDetails!=null){
			deleteImages(ImagePositionEnum.DETAIL, goodsId);
		}
		if(majorImage!=null){
			deleteImages(ImagePositionEnum.MAJOR, goodsId);
		}
		insertImages(goodsId, imageTitles, majorImage, imageDetails, videoTitle, videoDetail);
		return true;
	}

	
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer createPropertySku(Long goodsId, List<GoodsSpecificationDefBo> specBos, List<String[]> skus){
		if(specBos!=null){
			Wrapper<GoodsSpecificationDefBo> des = new EntityWrapper<>();
			des.eq(GoodsSpecificationDefBo.Key.goodsId.toString(), goodsId);
			des.last("limit 1");
			//删除前查一查，有效减低死锁
			GoodsSpecificationDefBo exist = goodsValueBiz.selectOne(des);
			if(exist!=null){
				des.last(null);
				goodsValueBiz.delete(des);
			}
		}

		if(Tools.collection.isEmpty(specBos)){
			return Integer.valueOf(0);
		}
		
		//加载分类的属性及属性值
		GoodsBo goodsBo = goodsBiz.selectById(goodsId);
		List<Long> propertyIds ;
		List<BaseCategoryPropertyValueBo> valueBos ; {
			List<Long> valueIds = new ArrayList<>(specBos.size());
			for(GoodsSpecificationDefBo sp:specBos){
				Long vid = sp.getPropertyValueId();
				valueIds.add(vid);
			}
			valueBos =baseCategoryPropertyValueBiz.selectBatchIds(valueIds);
			propertyIds = new ArrayList<>(valueBos.size());
			for(BaseCategoryPropertyValueBo vo : valueBos){
				propertyIds.add(vo.getPropertyId());
			}
		}
		List<BaseCategoryPropertyBo> propertyBos =categoryPropertyBiz.selectBatchIds(propertyIds);
		
		//區分普通属性与sku属性
		List<PropsVo> props = new ArrayList<>();
		List<GoodsSpecificationDefBo> specValueBos = new ArrayList<>();
		for(GoodsSpecificationDefBo specVo : specBos){
			BaseCategoryPropertyValueBo valueBo = getBo(specVo.getPropertyValueId(), valueBos);
			BaseCategoryPropertyBo pbo =(valueBo == null ? null: getBo(valueBo.getPropertyId(), propertyBos));
			if(pbo ==null){
				log.error("属性值没有对应的属性:valueid=" + specVo.getId());
				continue;
			}
			
			if(pbo.getType() == PropertyTypeEnum.STANDARD){ 
				specVo.setGoodsId(goodsId);
				if(valueBo.getValueName().equalsIgnoreCase(specVo.getCustomValueName())){
					specVo.setCustomValueName("");//相同就去掉,保持继承
				}
				specVo.setIsimg(pbo.getIsimage());
				specVo.setTmpPropertyName(pbo.getPropertyName());
				specValueBos.add(specVo);
			}
			else{
				PropsUserInputEnum inputEnum = getInputEnum( pbo.getManuallyInput());
				PropsVo pso = new PropsVo();
				pso.setInputType(inputEnum);
				pso.setProid(pbo.getId());
				pso.setValue(Tools.string.nullif(specVo.getCustomValueName(), valueBo.getValueName()));
				pso.setValueid(valueBo.getId());
				pso.setPropertyName(pbo.getPropertyName());
				props.add(pso);
			}
		}
		
		List<GoodsSpecificationDefBo> sbos = ControllerUtils.convertList(specValueBos, GoodsSpecificationDefBo.class,true);
		goodsValueBiz.insertBatch(sbos); 
		
		String proJson = GoodsUtil.parsePropsVo(props);
		goodsBo.setProps(proJson);
		goodsBiz.updateById(goodsBo);
		
		//sku定义
		List<GoodsSkuDefBo> oldskus;{//尽量保留以前SKU的id
			Wrapper<GoodsSkuDefBo> des = new EntityWrapper<>();
			des.eq(GoodsSkuDefBo.Key.goodsId.toString(), goodsId);
			oldskus = goodsSkuDefBiz.selectList(des);
			if(oldskus.size()>0){// oldskus 始终都要删除的，所有先删除了
				goodsSkuDefBiz.delete(des);
			}
		}
		
		List<GoodsSkuDefBo> skuBos = new ArrayList<>(skus.size());
		for(String[] sku: skus){
			StringBuilder skuValues = new StringBuilder();
			StringBuilder skuName = new StringBuilder();
			for(String s : sku){
				skuValues.append('(').append(s).append(')');
				
				//取属性值的名称
				GoodsSpecificationDefBo defbo = getSpecBo(Long.valueOf(s), sbos);
				BaseCategoryPropertyValueBo pv = getBo(Long.valueOf(s), valueBos);
				String valueName = Tools.string.nullif( defbo.getCustomValueName(),pv.getValueName());
				BaseCategoryPropertyBo proBo = getBo(pv.getPropertyId(), propertyBos);
				skuName.append(';').append(proBo.getPropertyName()).append(':').append(valueName);
			}
			skuName.deleteCharAt(0);
			
			GoodsSkuDefBo skuBo = null;//尽量保留以前SKU的id
			for(GoodsSkuDefBo old: oldskus){
				boolean match = matchProperyValueIds(old.getPropertyValueIds(), skuValues.toString());
				if(match){
					skuBo = old;
					break;
				}
			}
			if(skuBo == null){
				skuBo = new GoodsSkuDefBo();
			}
			
			skuBo.setPropertyValueIds(skuValues.toString());
			skuBo.setGoodsId(goodsId);
			skuBo.setValueNames(skuName.toString());			
			
			skuBos.add(skuBo);
		}
		goodsSkuDefBiz.insertBatch(skuBos);
		
		///////////goods_sku_spec sku与goods的值对应关系(复杂又没有使用场景，就不做了)////////////////////
		{
			/*Wrapper<GoodsSkuSpecBo> ssb = new EntityWrapper<>();
			ssb.eq(GoodsSkuSpecBo.Key.goodsId.toString(),goodsId);
			skuSpecBiz.delete(ssb);*/
		}
		return specBos.size();
	}
 
	
	private boolean matchProperyValueIds(String sku1,String sku2){
		if(sku1==null || sku2==null){
			return false;
		}
		else if(sku1.equalsIgnoreCase(sku2)){
			return true;
		}
		
		String[] s1 = sku1.split("\\(");
		boolean match = true;
		for(String s :s1){
			if(Tools.string.isEmpty(s)){
				continue;
			}
			
			int idx = sku2.indexOf("(" + s);
			if(idx <0){
				match = false;
				break;
			}
		}
		return match;
	}
	
	/**
	 * 
	 * @param manually 是否手工输入(true-是)
	 * @return
	 */
	private PropsUserInputEnum getInputEnum(Boolean manually){
		if(manually != null && manually.booleanValue()){
			return PropsUserInputEnum.INPUY;
		}
		else{
			return PropsUserInputEnum.DEF;
		}
	}
	private <T extends BaseBo> T getBo(Long id,List<T> bos){
		T rs = null;
		for(T v :bos){
			if(v.getId().equals(id)){
				rs = v;
				break;
			}
		}
		return rs;
	}
	
	private GoodsSpecificationDefBo getSpecBo(Long propertyValueId, List<GoodsSpecificationDefBo> bos){
		GoodsSpecificationDefBo rs = null;
		for(GoodsSpecificationDefBo bo :bos){
			if(bo.getPropertyValueId().equals(propertyValueId)){
				rs = bo;
				break;
			}
		}
		return rs;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer deleteGoodsById(Long goodsId) {
		GoodsBo goodsBo = goodsBiz.selectById(goodsId);
		if(goodsBo.getGoodsStatus() != GoodsStatusEnum.DELETE && goodsBo.getGoodsStatus() != GoodsStatusEnum.OPERATOR_DELETE){
			throw new WakaException("只有删除状态的商品才能删除");
		}
		goodsBiz.deleteById(goodsId);
		
		Wrapper<GoodsImageBo> delImage = new EntityWrapper<>();
		delImage.eq(GoodsImageBo.Key.goodsId.toString(), goodsId);
		goodsImageBiz.delete(delImage);
		
		Wrapper<GoodsInventoryBo> delInventory = new EntityWrapper<>();
		delInventory.eq(GoodsInventoryBo.Key.goodsId.toString(), goodsId);
		goodsInventoryBiz.delete(delInventory);
		
		Wrapper<GoodsPriceBo> delPrice = new EntityWrapper<>();
		delPrice.eq(GoodsPriceBo.Key.goodsId.toString(), goodsId);
		goodsPriceBiz.delete(delPrice);
		
		Wrapper<GoodsSkuDefBo> delSkudef  = new EntityWrapper<>();
		delSkudef.eq(GoodsSkuDefBo.Key.goodsId.toString(), goodsId);
		goodsSkuDefBiz.delete(delSkudef);
		
		Wrapper<GoodsSpecificationDefBo> delSpce  = new EntityWrapper<>();
		delSpce.eq(GoodsSpecificationDefBo.Key.goodsId.toString(), goodsId);
		goodsValueBiz.delete(delSpce);
		
		return Integer.valueOf(1);
	}

	public static void main(String[] args) {
		System.out.println(FlashSaleStatusEnum.FROZEN);
	}

	/**
	 * 更新商品状态(改变状态时需要做的业务处理)
	 * @param goodsIds
	 * @param statuts
	 * @return
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public List<GoodsBo> updateStatusEnum(List<Long> goodsIds,GoodsStatusEnum statuts){
		if(Tools.collection.isEmpty(goodsIds) || statuts == null){
			throw new WakaException("缺少商品id或者状态");
		}
		List<GoodsBo> rds = new ArrayList<>(goodsIds.size());
		for(Long id : goodsIds){
			GoodsBo bo = goodsBiz.selectById(id);
			rds.add(bo);
			if(bo==null || bo.getGoodsStatus() == statuts){
				continue;
			}
			
			/////////////////////////////
			switch (statuts) {
			case ONLINE://上线
				updateStatusGoodsOnline(bo);
				break;
	
			default:
				//其他没有业务逻辑
				bo.setGoodsStatus(statuts);
				goodsBiz.updateById(bo);
				break;
			}
		}
		return rds;
	}
	
	/**商品上线处理*/
	private void updateStatusGoodsOnline(GoodsBo bo){
		Wrapper<GoodsPriceBo> wpp = new EntityWrapper<>();
		wpp.eq(GoodsPriceBo.Key.goodsId.toString(), bo.getId());
		final String pf = GoodsPriceBo.Key.price.toString();
		wpp.where("(" + pf + " is null or " + pf + " = {0})", "0");
		wpp.last("limit 1");
		GoodsPriceBo pb = goodsPriceBiz.selectOne(wpp);
		if(pb!=null){
			throw new WakaException(bo.getGoodsName()+  "-不能上线，还存在单价为0的SKU");
		}
		/////////////////////
		bo.setGoodsStatus(GoodsStatusEnum.ONLINE);
		goodsBiz.updateById(bo);
	}
	
	public Page<GoodsBo> getGoodsNotInMenugoods(Page<GoodsBo> page, Wrapper<GoodsBo> wrapper, Long menuId){
		wrapper.where("id not in (SELECT goods_id FROM web_menu_goods WHERE menu_id =" + menuId +" and goods_id IS NOT NULL)");
		Page<GoodsBo> rs = goodsBiz.selectPage(page, wrapper);
		return rs;
	}
	
	public Page<GoodsBo> getGoodsNotInModelgoods(Page<GoodsBo> page, Wrapper<GoodsBo> wrapper, String modelCode){
		wrapper.where("id in (SELECT goods_id FROM web_menu_goods WHERE goods_id IS NOT NULL)");
		wrapper.where("id not in (SELECT goods_id FROM web_model_goods WHERE model_code=" + modelCode +" and goods_id IS NOT NULL)");
		Page<GoodsBo> rs = goodsBiz.selectPage(page, wrapper);
		return rs;
	}

	@Override
	public List<GoodsBo> getGoodsBystoreId(Long storeId) {
		Wrapper<GoodsBo> wrapper = new EntityWrapper<>();
		wrapper.eq(GoodsBo.Key.storehouseId.toString(), storeId);
		
		return goodsBiz.selectList(wrapper);
	}

	@Override
	public List<GoodsBo> getGoodsBybrandId(Long brandId) {
		Wrapper<GoodsBo> wrapper = new EntityWrapper<>();
		wrapper.eq(GoodsBo.Key.brandId.toString(), brandId);
		
		return goodsBiz.selectList(wrapper);
	}

	@Override
	public Boolean verifyGoodId(Long goodsId){
		if(goodsId == null){
			return false;
		}
		GoodsBo queryGoods = goodsBiz.selectOne(Condition.create().eq(GoodsBo.Key.id.toString(),goodsId).eq(GoodsBo.Key.goodsStatus.toString(), GoodsStatusEnum.ONLINE.getKey()));
		return  queryGoods == null ? false : true;

	}


	@Override
	public Boolean verifySkuId(Long skuId){
		if(skuId == null){
			return false;
		}
		GoodsSkuDefBo query = goodsSkuDefBiz.selectById(skuId);
		return query == null ? false : verifyGoodId(query.getGoodsId());
	}
}
