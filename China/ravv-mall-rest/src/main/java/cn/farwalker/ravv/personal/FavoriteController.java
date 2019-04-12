package cn.farwalker.ravv.personal;

import cn.farwalker.ravv.service.goodsext.favorite.biz.IGoodsFavoriteService;
import cn.farwalker.ravv.service.goodsext.favorite.model.FavoriteFilterVo;
import cn.farwalker.ravv.service.goodsext.favorite.model.GoodsFavoriteVo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.util.Tools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/favorite")
@Slf4j
public class FavoriteController {

    @Autowired
    private IGoodsFavoriteService iGoodsFavoriteService;

    @RequestMapping("/add_favorite")
    public JsonResult<String> addFavorite(HttpSession session, long goodsId){
        try{
            if(goodsId == 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            if(iGoodsFavoriteService.addFavorite(memberId, goodsId)){
                return JsonResult.newSuccess("add favorite successful");
            }
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());

        }
        return JsonResult.newFail("add fail");
    }

    @RequestMapping("/add_favorite_batch")
    public JsonResult<String> addFavoriteBatch(HttpSession session, String goodsIds){
        try{
            if(Tools.string.isEmpty(goodsIds))
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);

            return JsonResult.newSuccess(iGoodsFavoriteService.addFavoriteBatch(memberId, goodsIds));

        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("/delete_all_favorite")
    public JsonResult<String> deleteAllFavorite(HttpSession session){
        try{
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);

            return JsonResult.newSuccess(iGoodsFavoriteService.deleteAllFavorite(memberId));

        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("/cancel_favorite")
    public JsonResult<String> deleteFavorite(HttpSession session, long goodsId){
        try{
            if(goodsId == 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iGoodsFavoriteService.deleteFavorite(memberId, goodsId));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());

        }

    }

    @RequestMapping("/batch_cancel_favorite")
    public JsonResult<String> deletebatchFavorite(HttpSession session, String goodsIds){
        try{
            if(Tools.string.isEmpty(goodsIds))
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iGoodsFavoriteService.deleteBatchFavorite(memberId, goodsIds));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());

        }

    }

    @RequestMapping("/get_filter")
    public JsonResult<List<FavoriteFilterVo>> getFilter(HttpSession session, String filter){
        try{
            if(Tools.string.isEmpty(filter))
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iGoodsFavoriteService.getFilter(memberId, filter));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());

        }

    }

    @RequestMapping("/select_by_inventory")
    public JsonResult<List<GoodsFavoriteVo>> selectByInventory(HttpSession session, String filter){
        try{
            if(Tools.string.isEmpty(filter))
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iGoodsFavoriteService.getByInventoryFilter(memberId, filter));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());

        }

    }

    @RequestMapping("/select_by_menu_id")
    public JsonResult<List<GoodsFavoriteVo>> selectByMenuId(HttpSession session, Long menuId){
        try{
            if(menuId == 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iGoodsFavoriteService.getByMenuFilter(memberId, menuId));
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
