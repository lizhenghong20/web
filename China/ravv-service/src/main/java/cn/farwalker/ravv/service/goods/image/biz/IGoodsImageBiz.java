package cn.farwalker.ravv.service.goods.image.biz;
import java.util.List;

import com.baomidou.mybatisplus.service.IService;

import cn.farwalker.ravv.service.goods.image.model.GoodsImageBo;

/**
 * 商品图片<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IGoodsImageBiz extends IService<GoodsImageBo>{
	/**取得商品的所有图片*/
	public List<GoodsImageBo> getGoodsImages(Long goodsId);
}