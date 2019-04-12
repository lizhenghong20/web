package cn.farwalker.ravv.service.goods.image.biz.impl;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.goods.image.model.GoodsImageBo;
import cn.farwalker.ravv.service.goods.image.dao.IGoodsImageDao;
import cn.farwalker.ravv.service.goods.image.biz.IGoodsImageBiz;
import cn.farwalker.ravv.service.goods.utils.GoodsUtil;

/**
 * 商品图片<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class GoodsImageBizImpl extends ServiceImpl<IGoodsImageDao,GoodsImageBo> implements IGoodsImageBiz{
	@Override
	public List<GoodsImageBo> getGoodsImages(Long goodsId){
		Wrapper<GoodsImageBo> wrap = new EntityWrapper<>();
		wrap.eq(GoodsBo.Key.id.toString(),goodsId);
		 List<GoodsImageBo> rs = this.selectList(wrap);
		 return rs;
	}
}