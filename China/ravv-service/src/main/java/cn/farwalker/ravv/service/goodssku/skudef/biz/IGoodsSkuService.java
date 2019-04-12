package cn.farwalker.ravv.service.goodssku.skudef.biz;
import java.util.List;

import cn.farwalker.ravv.service.category.value.model.BaseCategoryPropertyValueBo;
import com.baomidou.mybatisplus.service.IService;

import cn.farwalker.ravv.service.goods.inventory.model.GoodsInventoryBo;
import cn.farwalker.ravv.service.goods.price.model.GoodsPriceBo;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuDefBo;
import cn.farwalker.ravv.service.goodssku.skudef.model.SkuPriceInventoryVo;

/**
 * 商品SKU定义<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IGoodsSkuService {
	/**更新商品的 SKU单价及库存
	 * @param goodsId 商品id
	 * @return 删除旧记录数
	 */
	public Integer updateSkuInvent(Long goodsId,List<SkuPriceInventoryVo> prinvBos);
	
	/**
	 * 更新商品的 SKU单价及库存(删除旧记录，再添加)
	 * @param goodsId 商品id
	 * @param priceBos
	 * @param invBos
	 * @return 删除旧记录数
	 */
	public Integer updateSkuInvent(Long goodsId,List<GoodsPriceBo> priceBos,List<GoodsInventoryBo> invBos);
	
	/**
	 * 删除SKU，并且删除库存及价格
	 * @param skuId
	 * @return 删除旧记录数
	 */
	public Integer deleteSku(Long skuId);

	/**
	 * @description: 根据 skuId 解析出属性值的列表
	 * @param: [skuId]
	 * @return  
	 * @author Lee 
	 * @date 2018/11/28 14:41 
	 */ 
	public List<BaseCategoryPropertyValueBo> parseSkuId(Long skuId);























}