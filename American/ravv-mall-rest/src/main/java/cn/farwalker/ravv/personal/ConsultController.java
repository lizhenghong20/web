package cn.farwalker.ravv.personal;

import cn.farwalker.ravv.service.goodsext.consult.biz.IGoodsConsultService;
import cn.farwalker.ravv.service.goodsext.consult.model.GoodsConsultBo;
import cn.farwalker.ravv.service.goodsext.consult.model.GoodsConsultVo;
import cn.farwalker.ravv.service.goodsext.consult.model.GoodsQuestionVo;
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
@RequestMapping("/consult")
@Slf4j
public class ConsultController {

    @Autowired
    private IGoodsConsultService iGoodsConsultService;

    @RequestMapping("/submit_question")
    public JsonResult<String> submitQuestion(HttpSession session, GoodsConsultBo consultBo){
        try{
            if(consultBo == null)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            consultBo.setMemberId(memberId);
            return JsonResult.newSuccess(iGoodsConsultService.addQuestion(consultBo));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("/search_question")
    public JsonResult<List<GoodsConsultVo>> searchQuestion(String question){
        try{
            if(Tools.string.isEmpty(question))
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return JsonResult.newSuccess(iGoodsConsultService.searchQuestion(question));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("/search_question_for_app")
    public JsonResult<List<GoodsQuestionVo>> searchQuestionForApp(String question){
        try{
            if(Tools.string.isEmpty(question))
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return JsonResult.newSuccess(iGoodsConsultService.searchQuestionForApp(question));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("/submit_answer")
    public JsonResult<String> submitAnswer(HttpSession session, GoodsConsultBo answer){
        try{
            if(answer == null)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            long memberId = 0;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            answer.setMemberId(memberId);
            return JsonResult.newSuccess(iGoodsConsultService.addAnswer(answer));
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
