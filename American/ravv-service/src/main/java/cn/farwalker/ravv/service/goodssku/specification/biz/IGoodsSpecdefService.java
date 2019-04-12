package cn.farwalker.ravv.service.goodssku.specification.biz;

import java.util.List;

import cn.farwalker.ravv.service.goodssku.specification.model.GoodsSpecificationDefBo;

/**
 * 商品的属性值表
 * @author Administrator
 *
 */
public interface IGoodsSpecdefService {
	/**
	 * 根据商品id取得商品的属性值
	 * @param goodsId
	 * @return
	 */
	public List<GoodsSpecificationDefBo> getValues(Long goodsId);
	/**
	 * 根据商品id取得商品 + 分类属性值id
	 * @param goodsId
	 * @param categoryValueId 分类属性值id
	 * @return
	 */
	public GoodsSpecificationDefBo getValues(Long goodsId,Long categoryValueId);
}
