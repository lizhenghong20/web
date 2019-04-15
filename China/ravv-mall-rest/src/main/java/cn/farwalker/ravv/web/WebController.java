package cn.farwalker.ravv.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import cn.farwalker.ravv.common.constants.WebModelCodeEnum;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderCreateService;
import cn.farwalker.ravv.service.web.searchkeyhistory.biz.IWebSearchkeyHistoryBiz;
import cn.farwalker.ravv.service.web.slider.constants.PageNameEnum;
import cn.farwalker.ravv.service.web.webmodel.constants.ModelShowTypeEnum;
import cn.farwalker.waka.core.WakaException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taxjar.Taxjar;
import com.taxjar.model.taxes.TaxResponse;

import cn.farwalker.ravv.service.goods.base.model.GoodsListVo;
import cn.farwalker.ravv.service.order.orderinfo.biz.impl.TaxUtil;
import cn.farwalker.ravv.service.web.menu.biz.IWebMenuService;
import cn.farwalker.ravv.service.web.menu.model.AppMenuFrontVo;
import cn.farwalker.ravv.service.web.menu.model.WebMenuBo;
import cn.farwalker.ravv.service.web.menu.model.WebMenuFrontVo;
import cn.farwalker.ravv.service.web.slider.biz.IWebSliderService;
import cn.farwalker.ravv.service.web.slider.model.WebSliderVo;
import cn.farwalker.ravv.service.web.webmodel.biz.IWebModelGoodsService;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.RavvExceptionEnum;

/**
 * Created by Lee on 2018/11/20.
 */
@RestController
@RequestMapping("/web")
@Slf4j
public class WebController {

	@Resource
    IWebSliderService iWebSliderService;

    @Autowired
    private IWebMenuService iWebMenuService;

    @Autowired
    private IWebModelGoodsService iWebModelGoodsService;

    @Autowired
    private IWebSearchkeyHistoryBiz iWebSearchkeyHistoryBiz;

    @Autowired
    private IOrderCreateService iOrderCreateService;


    //进入new arrivals 根据第一级菜单加载新到商品
    @RequestMapping("/new_arrivals")
    public JsonResult<List<GoodsListVo>> getNewArrivalsGoods(Long menuId, Integer currentPage, Integer pageSize){
        try{
            currentPage++;
            String modelCode = "11";//表示为new_arrivals模块
            if(currentPage < 0 || pageSize < 0|| menuId == 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return JsonResult.newSuccess(iWebModelGoodsService.getNewArrivalsGoods(modelCode,currentPage,pageSize,menuId));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }
    }


    /**
     * 获得首页模块，此接口为获得新品推荐模块
     * @param menuSize
     * @param itemSize
     * @return
     */
    @RequestMapping("/web_model")
    public JsonResult<List<WebMenuFrontVo>> getWebModel(Integer menuSize, Integer itemSize){
        try{
            if(menuSize < 0 || itemSize < 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            String modelCode = WebModelCodeEnum.NEW_ARRIVAL.getKey();//11表示为new_arrivals模块
            String showType = ModelShowTypeEnum.HOME.getKey();//home表示为首页
            long menuId = 0;
            return JsonResult.newSuccess(iWebModelGoodsService.getWebModel(modelCode,showType,menuSize,itemSize));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    /**
     * 获取轮播图
     * @param pageName
     * @return
     */
    @RequestMapping("/slider_images")
    public JsonResult<List<WebSliderVo>> getSliderImages(String pageName){
        try{
        	/*
            if(Tools.string.isEmpty(pageName)|| PageNameEnum.getEnumByKey(pageName) == null){
            	pageName="home";
            }*/
            if(PageNameEnum.getEnumByKey(pageName) == null){
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            }
            return JsonResult.newSuccess(iWebSliderService.getSliderImage(pageName));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    //获取第一级菜单
    @RequestMapping("/first_level_menu")
    public JsonResult<List<WebMenuBo>> getFirstLevelMenu(){
        try{
            return JsonResult.newSuccess(iWebMenuService.getFirstLevelMenu());
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    //根据一级菜单获取二级，三级菜单
    @RequestMapping("/menu_by_pid")
    public JsonResult<List<WebMenuFrontVo>> getMenuByPid(Long menuId){
        try{
            if(menuId == null)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return JsonResult.newSuccess(iWebMenuService.getMenuByPid(menuId));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    @RequestMapping("/goods_by_menu_id")
    public JsonResult<List<GoodsListVo>> getGoodsByMenuId(Long menuId, Integer currentPage, Integer pageSize){
        try{
            currentPage++;
            if(menuId == 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return JsonResult.newSuccess(iWebMenuService.getGoodsByMenuId(menuId,currentPage,pageSize));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    //返回一，二，三级所有菜单
    @RequestMapping("/get_menu_for_app")
    public JsonResult<List<AppMenuFrontVo>> getMenuForApp(){
        try{

            return JsonResult.newSuccess(iWebMenuService.getMenuForApp());
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    @RequestMapping("/get_search_key")
    public JsonResult<List<String>> getSearchKey(HttpSession session){
        try{
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iWebSearchkeyHistoryBiz.getSearchKey(memberId));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    @RequestMapping("/test_quartz")
    public JsonResult<String> testQuartz(Long orderId){
        try{

            return JsonResult.newSuccess(iOrderCreateService.testQuartz(orderId));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }
    }

}
