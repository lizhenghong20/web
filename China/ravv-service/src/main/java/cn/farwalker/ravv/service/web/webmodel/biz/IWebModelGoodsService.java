package cn.farwalker.ravv.service.web.webmodel.biz;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;

import cn.farwalker.ravv.service.goods.base.model.GoodsListVo;
import cn.farwalker.ravv.service.web.menu.model.WebMenuFrontVo;
import cn.farwalker.ravv.service.web.webmodel.model.WebModelGoodsBo;
import cn.farwalker.ravv.service.web.webmodel.model.WebModelGoodsVo;

/**
 * Created by asus on 2018/11/21.
 */

public interface IWebModelGoodsService {
	/**
     * @description: 首页获得限时购模块
     * @param: [modelCode, showType, menuSize, itemSize]
     * @return  
     * @author Lee 
     * @date 2018/12/7 11:52 
     */ 
    public List<WebMenuFrontVo> getWebModel(String modelCode, String showType, Integer menuSize, Integer itemSize);

	/**
     * 获得限时购商品列表
     * @param modelCode
     * @param currentPage
     * @param pageSize
     * @param menuId
     * @return
     */
    public  List<GoodsListVo> getNewArrivalsGoods(String modelCode,int currentPage, int pageSize, long menuId);

	/**
     * 搜索模块商品， 联表搜索名称
     * @param goodsName
     * @param modelCode
     * @param start
     * @param size
     * @return
     */
    public void searchModelGoods(Page<WebModelGoodsVo> page, String goodsName, String modelCode, String showType, Integer start,Integer size);
}
