package cn.farwalker.ravv.goods;

import cn.farwalker.ravv.common.constants.SearchOrderRuleEnum;
import cn.farwalker.ravv.order.CreateOrderController;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsService;
import cn.farwalker.ravv.service.goods.base.model.GoodsDetailsVo;
import cn.farwalker.ravv.service.goods.base.model.GoodsListVo;
import cn.farwalker.ravv.service.goods.base.model.ParseSkuExtVo;
import cn.farwalker.ravv.service.goodsext.comment.biz.IGoodsCommentService;
import cn.farwalker.ravv.service.goodsext.comment.model.GoodsCommentDetailVo;
import cn.farwalker.ravv.service.goodsext.comment.model.GoodsCommentPointVo;
import cn.farwalker.ravv.service.goodsext.consult.biz.IGoodsConsultService;
import cn.farwalker.ravv.service.goodsext.consult.model.GoodsConsultVo;
import cn.farwalker.ravv.service.goodsext.consult.model.GoodsQaVo;
import cn.farwalker.ravv.service.goodsext.favorite.biz.IGoodsFavoriteService;
import cn.farwalker.ravv.service.web.searchkeyhistory.biz.IWebSearchkeyHistoryBiz;
import cn.farwalker.ravv.service.web.searchkeyhistory.constants.SearchTypeEnum;
import cn.farwalker.ravv.service.web.searchkeyhistory.model.WebSearchkeyHistoryBo;
import cn.farwalker.waka.core.HttpKit;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.plugins.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
  * @description:
  * @author Lee
  * @date 2018/11/19 14:03
  */
@RestController
@RequestMapping("/goods")
@Slf4j
public class GoodsWebController {

    @Autowired
    private IGoodsService iGoodsService;

    @Autowired
    private IGoodsFavoriteService iGoodsFavoriteService;

    @Autowired
    private IGoodsConsultService iGoodsConsultService;

    @Autowired
    private IGoodsCommentService iGoodsCommentService;
    
	@Resource
	private IWebSearchkeyHistoryBiz webSearchkeyHistoryBiz;

    @RequestMapping("/parse_sku")
    public JsonResult<ParseSkuExtVo> parseSku(long goodsId, String propertyValueIds) {
        try {
            if (goodsId == 0) {
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            }
            if (Tools.string.isEmpty(propertyValueIds))
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return JsonResult.newSuccess(iGoodsService.parseSku(goodsId, propertyValueIds));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }

    }

    @RequestMapping("/details")
    public JsonResult<GoodsDetailsVo> updateAndgetDetails(HttpSession session, long goodsId){
        try{
            if(goodsId == 0){
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            }
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            return JsonResult.newSuccess(iGoodsService.updateAndGetDetails(memberId,goodsId));
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
     * @return
     * @description: 搜索界面
     * @param: [keyWord, floor, cell, priceOrder]
     * @author Lee
     * @date 2018/11/24 11:02
     */
    @RequestMapping("/search")
    public JsonResult<Page<GoodsListVo>> search(@RequestParam(value = "keyWords", required = false)String keyWords,
                                                @RequestParam(value = "menuId", required = false, defaultValue = "0") Long menuId,
                                                @RequestParam(value = "floorStr", required = false, defaultValue = "0") String floorStr,
                                                @RequestParam(value = "ceilingStr", required = false, defaultValue = "999999999") String ceilingStr,
                                                @RequestParam(value = "oneOfThree", required = false,defaultValue = "bestmatch") String oneOfThree,//oneOfThree 四个值选一个，bestmatch,order,pricedesc,priceasc
                                                @RequestParam(value = "freeShipment",required = false)Boolean freeShipment,
                                                @RequestParam(value = "goodsPoint",required = false)Integer goodsPoint,
                                                Integer currentPage, Integer pageSize) {
        try {
            if ( currentPage < 0 || pageSize < 0) {
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            }
            if(SearchOrderRuleEnum.getEnumByKey(oneOfThree)== null)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);

            if(Tools.string.isEmpty(keyWords)&&menuId == 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            
            //存储用户关键字搜索记录
        	HttpSession sin =HttpKit.getRequest().getSession();
        	Long memberId = (Long)sin.getAttribute(CreateOrderController.K_MemberId);
        	if(null != memberId) {
        		webSearchkeyHistoryBiz.addKeyHistory(memberId, keyWords, SearchTypeEnum.Goods);
        	}
            
            BigDecimal floor = new BigDecimal(floorStr);
            BigDecimal ceiling = new BigDecimal(ceilingStr);
            return JsonResult.newSuccess(iGoodsService.search(menuId,keyWords, floor, ceiling,
                                                                oneOfThree,freeShipment,
                                                            goodsPoint,currentPage, pageSize));
        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }
    }



    @RequestMapping("/consult_list")
    public JsonResult<List<GoodsConsultVo>> getConsultList(Long goodsId, int currentPage, int pageSize){
        try{
            currentPage++;
            if(goodsId == 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return JsonResult.newSuccess(iGoodsConsultService.getConsultList(goodsId, currentPage, pageSize));
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
     * @description: 商品详情页下获取评论
     * @param: goodsId
     * @return list
     * @author Mr.Simple
     * @date 2018/12/28 14:47
     */
    @RequestMapping("/comment_with_point")
    public JsonResult<GoodsCommentPointVo> getComment(Long goodsId){
        try{
            if(goodsId == 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return JsonResult.newSuccess(iGoodsCommentService.getCommentWithPoint(goodsId));
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
     * @description: 商品详情页下获取评论(带筛选条件)
     * @param: goodsId
     * @return list
     * @author Mr.Simple
     * @date 2018/12/28 14:47
     */
    @RequestMapping("/comment_with_filter")
    public JsonResult<List<GoodsCommentDetailVo>> getCommentWithFilter(Long goodsId,
                          @RequestParam(value = "goodsPoint", required = false, defaultValue = "0") int goodsPoint,
                          @RequestParam(value = "picture", required = false, defaultValue = "false") boolean picture,
                          @RequestParam(value = "addtion", required = false, defaultValue = "false") boolean addtion,
                          int currentPage, int pageSize  ){
        try{
            currentPage++;
            if(goodsId == 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return JsonResult.newSuccess(iGoodsCommentService.getCommentWithFilter(goodsId, goodsPoint, picture, addtion, currentPage, pageSize));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("/question_detail")
    public JsonResult<GoodsQaVo> questionDetail(HttpSession session, Long consultId){
        try{
            if(consultId == 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            return JsonResult.newSuccess(iGoodsConsultService.questionDetail(memberId, consultId));
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
     * 获取会员关键字搜索历史列表
     * @return
     */
    @RequestMapping("/get_keyword_history")
    public JsonResult<List<WebSearchkeyHistoryBo>> getKeyWordsHistory(){
    	//获取会员id
    	HttpSession sin =HttpKit.getRequest().getSession();
    	Long memberId = (Long)sin.getAttribute(CreateOrderController.K_MemberId);
    	
    	if(null == memberId) {
    		JsonResult.newSuccess(null);
    	}
        List<WebSearchkeyHistoryBo> searchKeyList = webSearchkeyHistoryBiz.selectList(Condition.create()
                .eq(WebSearchkeyHistoryBo.Key.memberId.toString(), memberId)
                .isNotNull(WebSearchkeyHistoryBo.Key.word.toString())
                .orderBy(WebSearchkeyHistoryBo.Key.gmtModified.toString(), false));
    	List<WebSearchkeyHistoryBo> returnKeyList = new ArrayList<>();
    	if(Tools.collection.isNotEmpty(searchKeyList)){
            searchKeyList.forEach(item->{
                if(Tools.string.isNotEmpty(item.getWord()))
                    returnKeyList.add(item);
            });
        }
    	return JsonResult.newSuccess(returnKeyList);
    }
    
    /**
     * 一键清空会员关键字搜索列表
     * @return
     */
    @RequestMapping("/del_keyword_history")
    public JsonResult<Boolean> delKeyWordsHistory(){
    	//获取会员id
    	HttpSession sin = HttpKit.getRequest().getSession();
    	Long memberId = (Long)sin.getAttribute(CreateOrderController.K_MemberId);
    	if(null == memberId) {
    		JsonResult.newSuccess(null);
    	}
    	
    	Boolean rs = webSearchkeyHistoryBiz.deleteAllKeyHistory(memberId);
    	
    	return JsonResult.newSuccess(rs);
    }

}
