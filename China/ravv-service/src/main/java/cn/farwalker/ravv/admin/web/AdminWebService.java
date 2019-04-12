package cn.farwalker.ravv.admin.web;

import cn.farwalker.ravv.service.web.menu.model.WebMenuBo;
import cn.farwalker.ravv.service.web.menu.model.WebMenuGoodsBo;
import cn.farwalker.ravv.service.web.menu.model.WebMenuGoodsVo;
import cn.farwalker.ravv.service.web.slider.model.WebSliderBo;
import cn.farwalker.ravv.service.web.webmodel.model.WebModelBo;
import cn.farwalker.ravv.service.web.webmodel.model.WebModelGoodsBo;
import cn.farwalker.ravv.service.web.webmodel.model.WebModelGoodsVo;
import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.web.crud.QueryFilter;

import java.util.List;

public interface AdminWebService {

    Boolean createWebMenu(WebMenuBo vo);

    Boolean deleteWebMenu(Long id);

    WebMenuBo getOneWebMenu(Long id);

    Page<WebMenuBo> getListWebMenu(List<QueryFilter> query, Integer start, Integer size,
                                String sortfield);

    List<WebMenuBo> getAllListWebMenu();

    Boolean updateWebMenu(WebMenuBo vo);


    Boolean createWebMenuGoods(WebMenuGoodsBo vo);

    Boolean deleteWebMenuGoods(Long id);

    WebMenuGoodsVo getOneWebMenuGoods(Long id);

    Page<WebMenuGoodsVo> getListWebMenuGoods(List<QueryFilter> query, Integer start, Integer size,
                                   String sortfield);

    Boolean updateWebMenuGoods(WebMenuGoodsBo vo);

    Boolean addWebMenuGoods(List<Long> goodsIdList, Long menuId);


    Boolean createWebModel(WebModelBo vo);

    Boolean deleteWebModel(Long id);

    WebModelBo getOneWebModel(Long id);

    Page<WebModelBo> getListWebModel(List<QueryFilter> query, Integer start, Integer size,
                                             String sortfield);

    Boolean updateWebModel(WebModelBo vo);

    List<WebModelBo> getAllListWebModel();


    Boolean createWebModelGoods(WebModelGoodsBo vo);

    Boolean deleteWebModelGoods(Long id);

    WebModelGoodsVo getOneWebModelGoods(Long id);

    Page<WebModelGoodsVo> getListWebModelGoods(List<QueryFilter> query, Integer start, Integer size,
                                     String sortfield);

    Boolean updateWebModelGoods(WebModelGoodsBo vo);

    Boolean addWebModelGoods(List<Long> goodsIdList, String modelCode);


    Boolean createWebSlider(WebSliderBo vo);

    Boolean deleteWebSlider(Long id);

    WebSliderBo getOneWebSlider(Long id);

    Page<WebSliderBo> getListWebSlider(List<QueryFilter> query, Integer start, Integer size,
                                               String sortfield);

    Boolean updateWebSlider(WebSliderBo vo);




}
