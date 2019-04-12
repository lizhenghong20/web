package cn.farwalker.ravv.personal;

import cn.farwalker.ravv.service.goodsext.comment.biz.IGoodsCommentService;
import cn.farwalker.ravv.service.goodsext.comment.model.GoodsCommentBo;
import cn.farwalker.ravv.service.goodsext.comment.model.GoodsCommentDetailVo;
import cn.farwalker.ravv.service.goodsext.comment.model.GoodsCommentVo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {

    @Autowired
    private IGoodsCommentService iGoodsCommentService;

    /**
     * @description: 写评论
     * @param: goodsCommentVo
     * @return string
     * @author Mr.Simple
     * @date 2018/12/28 10:57
     */
    @RequestMapping("write_comment")
    public JsonResult<String> addComment(HttpSession session, GoodsCommentVo goodsCommentVo){
        try{
            if(goodsCommentVo == null)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            goodsCommentVo.setAuthorId(memberId);
            if(iGoodsCommentService.addComment(goodsCommentVo))
                return JsonResult.newSuccess("successful");
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());

        }
        return JsonResult.newFail("fail");
    }

    /**
     * @description: 批量写评论
     * @param: goodsCommentVo
     * @return string
     * @author Mr.Simple
     * @date 2018/12/28 10:57
     */
    @RequestMapping("write_comment_by_batch")
    public JsonResult<String> addCommentByBatch(HttpSession session, @RequestBody List<GoodsCommentVo> commentVoList){
        try{
            if(commentVoList.size() == 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);

            long lastMemberId = memberId;
            commentVoList.forEach(item->{
                item.setAuthorId(lastMemberId);
            });

            return JsonResult.newSuccess(iGoodsCommentService.addCommentByBatch(commentVoList));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("append_comment")
    public JsonResult<String> append_comment(HttpSession session, GoodsCommentBo goodsCommentBo){
        try{
            if(goodsCommentBo == null)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            goodsCommentBo.setAuthorId(memberId);
            return JsonResult.newSuccess(iGoodsCommentService.addAppendComment(goodsCommentBo));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("shop_reply")
    public JsonResult<String> shopReply(HttpSession session, GoodsCommentBo goodsCommentBo){
        try{
            if(goodsCommentBo == null)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            goodsCommentBo.setAuthorId(memberId);
            return JsonResult.newSuccess();
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("detail")
    public JsonResult<GoodsCommentDetailVo> comment_detail(HttpSession session, Long commentId){
        try{
            if(commentId == 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iGoodsCommentService.getCommentDetail(commentId));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("comment_detail_from_order")
    public JsonResult<GoodsCommentDetailVo> getCommentDetailFromOrder(HttpSession session, Long orderGoodsId){
        try{
            if(orderGoodsId == 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(iGoodsCommentService.getCommentDetailFromOrder(memberId, orderGoodsId));
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
