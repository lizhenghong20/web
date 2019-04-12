package cn.farwalker.ravv.service.goods.base.biz;

import java.math.BigDecimal;
import java.util.List;

import cn.farwalker.ravv.service.goods.base.model.*;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;

import cn.farwalker.ravv.service.category.value.model.BaseCategoryPropertyValueBo;
import cn.farwalker.ravv.service.goods.constants.GoodsStatusEnum;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuDefBo;
import cn.farwalker.ravv.service.goodssku.skudef.model.SkuPriceInventoryVo;
import cn.farwalker.ravv.service.goodssku.specification.model.GoodsSpecificationDefBo;
import cn.farwalker.ravv.service.goodssku.specification.model.GoodsSpecificationDefVo;


public interface IGoodsService {
	/**
	 * 创建对象时，如果goodsCode为空，就自动生成
	 * @param bo
	 * @param imageTitles 标题图片,允许包含主图((null不更新，empty时清除所有))
	 * @param majorImage 可以null，如果为null就取imageTitles的第一个
	 * @param imageDetails 详情图片(null不更新，empty时清除所有) 
	 * @param videoTitle 标题视频路径
	 * @param videoDetail 详情视频路径
	 * @return
	 */
	public boolean insert(GoodsBo bo, List<String> imageTitles,String majorImage, List<String> imageDetails,String videoTitle,String videoDetail); 
	
	/**
	 * 创建对象时，如果goodsCode为空，就自动生成
	 * @param bo
	 * @param imageTitles 标题图片(包含主图)
	 * @param majorImage 可以null，如果为null就取imageTitles的第一个
	 * @param imageDetails 详情图片
	 * @param videoTitle 标题视频路径
	 * @param videoDetail 详情视频路径
	 * @return
	 */
	public boolean update(GoodsBo bo, List<String> imageTitles,String majorImage, List<String> imageDetails,String videoTitle,String videoDetail);
	
	/**
	 * 取下一个流水号
	 * @return
	 */
	public String getNextCode();

	/**
	 * @description: 解耦合，单独获取skuId
	 * @param: goodsId,propertyValueIds
	 * @return skuId
	 * @author Mr.Simple
	 * @date 2018/12/3 10:11
	 */
	public Long parseSkuToId(Long goodsId, String propertyValueIds);

	/**
	 * @description: 找出商品价格和库存（为商品详情页面服务）
	 * @param: [goodsId, propertyValueIds]
	 * @return
	 * @author Lee
	 * @date 2018/11/19 11:40
	 */
	public ParseSkuExtVo parseSku(Long goodsId, String propertyValueIds ) throws Exception;

	/**
	 * @description: 找出商品价格和库存（为下单服务）
	 * @param  goodsId
	 * @param 商品属性id {@link GoodsSpecificationDefBo#getId()}
	 * @return
	 */
	public SkuPriceInventoryVo getGoodsSkuPriceVo(Long goodsId, List<Long> propertyValueIds );
	
	/**
	 * @description: 找出商品价格和库存（为下单服务） 
	 * @param 商品属性skuId {@link GoodsSkuDefBo#getId()}
	 * @return
	 */
	public SkuPriceInventoryVo getGoodsSkuPriceVo(Long skuId);
	
	/**
	 * @description: 找出商品价格和库存
	 * @param: [goodsId, skuId]
	 * @return
	 * @author Lee
	 * @date 2018/11/19 11:40
	 */
	public ParseSkuExtVo parseSku(Long goodsId, Long skuId ) throws Exception;
	/**
	 * @description: 
	 * @param: [goodsId] 
	 * @return  
	 * @author Lee 
	 * @date 2018/11/19 14:38
	 */ 
	public GoodsDetailsVo updateAndGetDetails(long memberId,long goodsId);

/**
 * @description:  商品搜索
 * @param: [keyWords, floor, ceiling, priceOrder]
 * @return  
 * @author Lee 
 * @date 2018/11/24 11:28 
 */ 
	List<GoodsListVo> search(Long menuId,String keyWords, BigDecimal floor, BigDecimal ceiling,String oneOfThree,Boolean freeShipment, Integer goodsPoint, int currentPage, int PageSize);

	/**
	 * 商品创建普通属性及SKU
	 * @param goodsId 商品id
	 * @param customerValueBos 前端处理过的普通属性及sku属性值(null时不删除旧记录)
	 * @param skus sku属性值id组合,id对应{@link BaseCategoryPropertyValueBo#getId()}(null时不删除旧记录)
	 * @return
	 */
	public Integer createPropertySku(Long goodsId,List<GoodsSpecificationDefBo> specBos, List<String[]> skus);
	
	/**
	 * 删除商品及sku等一套数据
	 * @param goodsId
	 * @return
	 */
	public Integer deleteGoodsById(Long goodsId);

	
	/**
	 * 更新商品状态(改变状态时需要做的业务处理)
	 * @param goodsIds
	 * @param statuts
	 * @return
	 */
	public List<GoodsBo> updateStatusEnum(List<Long> goodsIds,GoodsStatusEnum statuts);

	/**
	 * 解析出属性名和属性值给前端
	 * @param sourceList
	 * @return
     */
	public List<PropertyLineVo> parseToPropertyLineVo(List<GoodsSpecificationDefVo> sourceList);
	
	/**
     * 获取未加入menugoods的商品 
     * @return
     */
	public Page<GoodsBo> getGoodsNotInMenugoods(Page<GoodsBo> page, Wrapper<GoodsBo> wrapper, Long menuId);
	
	/**
     * 获取未加入modelgoods的商品，且商品需要设置标签
     * @return
     */
	public Page<GoodsBo> getGoodsNotInModelgoods(Page<GoodsBo> page, Wrapper<GoodsBo> wrapper, String modelCode);
	
	/**
	 * 根据仓库id获取商品列表
	 * @param storeId 仓库id
	 * @return
	 */
	public List<GoodsBo> getGoodsBystoreId(Long storeId);
	
	/**
	 * 根据品牌id获取商品列表
	 * @param brandId 品牌id
	 * @return
	 */
	public List<GoodsBo> getGoodsBybrandId(Long brandId);

	/**
	 * 判断goodsId是否合法，即商品存不存在，有没有下线
	 * @param goodsId
     */
	Boolean verifyGoodId(Long goodsId);

	/**
	 * 判断skuId是否合法，即sku存不存在，sku对应的goods是否存在，有没有下线
	 * @param skuId
     */
	Boolean verifySkuId(Long skuId);
}
