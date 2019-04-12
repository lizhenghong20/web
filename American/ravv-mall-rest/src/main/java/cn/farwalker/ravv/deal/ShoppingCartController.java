package cn.farwalker.ravv.deal;

import cn.farwalker.ravv.common.constants.UpdateCartTypeEnum;
import cn.farwalker.ravv.service.goodscart.biz.IGoodsCartService;
import cn.farwalker.ravv.service.goodscart.model.GoodsCartVo;
import cn.farwalker.ravv.service.goodscart.model.RecoverToCartForm;
import cn.farwalker.ravv.service.goodscart.model.UpdateCartVo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.util.Tools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by asus on 2018/11/27.
 */
@Slf4j
@RestController
@RequestMapping("/shopping_cart")
public class ShoppingCartController {
    @Autowired
    private IGoodsCartService iGoodsCartService;

    @RequestMapping("/add_to_cart")
    public JsonResult<String> addToCart(HttpSession httpSession,
                                        Long goodsId,
                                        Long skuId,
                                        Integer quantity){
        try {
            if(skuId == 0||goodsId == 0||quantity<0){
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            }
            Long memberId;
            if((memberId = (Long)httpSession.getAttribute("memberId"))==0){
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            }
            if(iGoodsCartService.addToCart(memberId,goodsId,skuId,quantity)){
                return JsonResult.newSuccess("added successfully!");
            }else{
                return JsonResult.newFail("addition failed");
            }

        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("/recover_to_cart")
    public JsonResult<String> addToCart(HttpSession httpSession, @RequestBody List<RecoverToCartForm> list){
        try {
            Long memberId;
            if((memberId = (Long)httpSession.getAttribute("memberId"))==0){
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            }
            if(list == null||list.size() == 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);

            return JsonResult.newSuccess(iGoodsCartService.updateToCart(memberId,list));

        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("/update_cart")
    public JsonResult<UpdateCartVo> updateCart(HttpSession httpSession,
                                               @RequestParam(value = "selectedCartIds", required = false,defaultValue = "") String  selectedCartIds,
                                               @RequestParam(value = "updateCartId", required = false, defaultValue = "0") Long updateCartId,
                                               @RequestParam(value = "skuId", required = false, defaultValue = "0") Long skuId,
                                               @RequestParam(value = "quantity", required = false, defaultValue = "0") Integer quantity,
                                               @RequestParam(value = "type", required = true) String type){
        try {
            Long memberId;
            if((memberId = (Long)httpSession.getAttribute("memberId"))==0){
                throw new WakaException(RavvExceptionEnum.TOKEN_PARSE_MEMBER_ID_FAILED);
            }
            if(Tools.string.isEmpty(type)){
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            }

            if(UpdateCartTypeEnum.PARSESKU.toString().equals(type)){
                if(updateCartId == 0||
                        skuId == 0||
                        quantity == 0)
                    throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);

            }else if(UpdateCartTypeEnum.UPDATEQUANTITY.toString().equals(type)){
                if(updateCartId == 0||
                        skuId == 0||
                        quantity == 0)
                    throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);

            }else if(UpdateCartTypeEnum.DELETEONE.toString().equals(type)){
                if(updateCartId == 0)
                    throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);

            }else if(UpdateCartTypeEnum.DELETESELECTED.toString().equals(type)){
                if(Tools.string.isEmpty(selectedCartIds))
                    throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);

            }else if(UpdateCartTypeEnum.DELETEALL.toString().equals(type)){
                //不需要参数
            }
            else if(UpdateCartTypeEnum.SELECT.toString().equals(type)){
                if(Tools.string.isEmpty(selectedCartIds))
                    throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            }
            else{
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            }

            return JsonResult.newSuccess(iGoodsCartService.updateCart(memberId,
                    selectedCartIds,
                    updateCartId,
                    skuId,
                    quantity,
                    type));

        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }

    }

    @RequestMapping("/get_cart_goods")
    public JsonResult<List<GoodsCartVo>> getCartGoods(HttpSession httpSession){
        try {
            Long memberId;
            if((memberId = (Long)httpSession.getAttribute("memberId"))==0){
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            }

            return JsonResult.newSuccess(iGoodsCartService.getCartGoods(memberId));

        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }

    }


    @RequestMapping("/delete_by_goods_cart_id")
    public JsonResult<String> deleteByGoodsCartId(HttpSession httpSession, Long goodsCartId){
        try {
            Long memberId;
            if((memberId = (Long)httpSession.getAttribute("memberId"))==0){
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            }
            if(goodsCartId == 0){
                throw new  WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            }

            return JsonResult.newSuccess(iGoodsCartService.deleteByGoodsCartId(goodsCartId));

        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());
        }
    }



}
