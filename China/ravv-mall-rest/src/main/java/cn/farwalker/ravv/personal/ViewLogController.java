package cn.farwalker.ravv.personal;

import cn.farwalker.ravv.service.goodsext.viewlog.biz.IGoodsViewLogService;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.util.Tools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
@RequestMapping("/view_log")
@Slf4j
public class ViewLogController {

    @Autowired
    private IGoodsViewLogService iGoodsViewLogService;

    @RequestMapping("/add_log")
    public JsonResult<String> addLog(HttpSession session, long goodsId){
        try{
            if(goodsId == 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            if(iGoodsViewLogService.addViewLog(memberId, goodsId))
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

    @RequestMapping("/delete_view_log")
    public JsonResult<String> deleteViewLog(HttpSession session, long goodsId){
        try{
            if(goodsId == 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iGoodsViewLogService.deleteViewLog(memberId, goodsId));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());

        }

    }

    @RequestMapping("/delete_by_time")
    public JsonResult<String> deleteByTime(HttpSession session, Date time){
        try{
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iGoodsViewLogService.deleteByTime(memberId, time));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());

        }

    }

    @RequestMapping("/batch_delete_view_log")
    public JsonResult<String> batchCancelFavorite(HttpSession session, String goodsIds){
        try{
            if(Tools.string.isEmpty(goodsIds))
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iGoodsViewLogService.deleteBatchViewLog(memberId, goodsIds));
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
