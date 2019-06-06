package cn.farwalker.ravv.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.farwalker.ravv.common.constants.WebModelCodeEnum;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderCreateService;
import cn.farwalker.ravv.service.shipstation.biz.IShipStationService;
import cn.farwalker.ravv.service.web.searchkeyhistory.biz.IWebSearchkeyHistoryBiz;
import cn.farwalker.ravv.service.web.slider.constants.PageNameEnum;
import cn.farwalker.ravv.service.web.webmodel.constants.ModelShowTypeEnum;
import cn.farwalker.waka.core.HttpKit;
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
    private IShipStationService iShipStationService;

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

    @RequestMapping("/goods_by_menu_id_for_model")
    public JsonResult<List<GoodsListVo>> getGoodsByMenuIdForModel(Long modelId,Long menuId, Integer currentPage, Integer pageSize){
        try{
            currentPage++;
            if(menuId == 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return JsonResult.newSuccess(iWebMenuService.getModelGoodsByMenuId(modelId,menuId,currentPage,pageSize));
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

    @RequestMapping("/ship")
    public JsonResult<String> ship(Long orderId){
        try{
            if(orderId == 0 || orderId == null)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            if(iShipStationService.addShipStationOrder(orderId))
                return JsonResult.newSuccess("success");
            else
                return JsonResult.newFail("fail");
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    @RequestMapping("/cancel_order")
    public JsonResult<String> cancelOrder(Long orderId){
        try{
            if(orderId == 0 || orderId == null)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            if(iShipStationService.cancelOrder(orderId))
                return JsonResult.newSuccess("success");
            else
                return JsonResult.newFail("fail");
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    @RequestMapping("/list_carriers")
    public JsonResult<String> listCarriers(){
        try{

            if(iShipStationService.listCarriers())
                return JsonResult.newSuccess("success");
            else
                return JsonResult.newFail("fail");
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    @RequestMapping("/list_services")
    public JsonResult<String> listServices(){
        try{

            if(iShipStationService.listService())
                return JsonResult.newSuccess("success");
            else
                return JsonResult.newFail("fail");
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

    
    @RequestMapping("/test")
    public JsonResult<Object> testTax(){
    	try{
    		//能正常计算税费
    		//http://52.53.127.206:8080/ravv-mall-rest/web/test?from_country=US&from_state=OK&from_zip=74965&amount=50&shipping=0&to_zip=92093&from_city=Adairville&to_country=US&to_city=Sandiego&from_street=%E5%8D%97%E6%96%B9%E8%BD%AF%E4%BB%B6%E5%9B%AD&to_street=address&500&to_state=CA
    		//校验邮政编码是否正确
    		//https://usa.youbianku.com/zh-hans/zipcode/74965
    		HttpServletRequest req = HttpKit.getRequest();
    		Enumeration<String> names = req.getParameterNames();
    		Map<String, Object> params = new HashMap<>();
    		while(names.hasMoreElements()){
    			String key = names.nextElement();
    			String value = req.getParameter(key);
    			params.put(key, value);
    		}     
        
	        Taxjar client = new Taxjar(TaxUtil.getTaxToken());
	        //被征税的总额 taxable_amount	Amount of the order to be taxed.
	        //税金 amount_to_collect	Amount of sales tax to collect.
	        //税率 rate Overall sales tax rate of the order (amount_to_collect ÷ taxable_amount).
	        TaxResponse res = client.taxForOrder(params);
	        
	        //Float fx = res.tax.getAmountToCollect();
	        //return new BigDecimal(fx);
	        params.put("tax",res.tax);
	        return JsonResult.newSuccess(params);
                
             
    		
	    	/**
	    	 * 463428992@qq.com
	    	 * 12345678asdf
	    	 
    		TaxUtil.Address from = new TaxUtil.Address("CA","92093");
    		TaxUtil.Address to = new TaxUtil.Address("CA","90002");
    		from.setCity("La Jolla");
    		from.setStreet("9500 Gilman Drive");
    		to.setCity("Los Angeles");
    		from.setStreet("1335 E 103rd St1335 E 103rd St");
    		
    		String rs = TaxUtil.calcTaxJson(new BigDecimal(199.99),  from, to);
			return JsonResult.newSuccess(rs);*/
    	}
    	catch(Exception e){
    		return JsonResult.newFail("error:" + e.getMessage());
    	}
    }
}
