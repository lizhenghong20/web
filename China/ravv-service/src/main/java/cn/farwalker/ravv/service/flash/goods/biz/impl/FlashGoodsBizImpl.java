package cn.farwalker.ravv.service.flash.goods.biz.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import cn.farwalker.ravv.service.flash.goods.model.FlashGoodsBo;
import cn.farwalker.ravv.service.flash.goods.dao.IFlashGoodsDao;
import cn.farwalker.ravv.service.flash.goods.biz.IFlashGoodsBiz;

/**
 * 闪购的商品(与flash_goods_sku没有直接关系)<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class FlashGoodsBizImpl extends ServiceImpl<IFlashGoodsDao,FlashGoodsBo> implements IFlashGoodsBiz{
}