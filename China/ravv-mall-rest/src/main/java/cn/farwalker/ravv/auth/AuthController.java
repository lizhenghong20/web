
package cn.farwalker.ravv.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.farwalker.ravv.common.constants.WechatLoginTypeEnum;
import cn.farwalker.ravv.service.email.IEmailService;
import cn.farwalker.ravv.service.member.pam.wechat.biz.IPamWechatMemberService;
import cn.farwalker.ravv.service.member.thirdpartaccount.biz.IMemberThirdpartAccountService;
import cn.farwalker.waka.components.wechatpay.common.exception.WxErrorException;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.farwalker.ravv.service.member.pam.member.biz.IPamMemberService;
import cn.farwalker.ravv.service.member.pam.member.model.AuthLoginVo;
import cn.farwalker.waka.core.JsonResult;

import cn.farwalker.waka.util.Tools;

@RestController
@RequestMapping("/auth")
public class AuthController{

    private final static Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private IPamMemberService iPamMemberService;

    @Autowired
    private IPamWechatMemberService pamWechatMemberService;

    @RequestMapping("/wechat_login")
    public JsonResult<AuthLoginVo> wechatLogin(HttpServletRequest request, String code, WechatLoginTypeEnum loginType){
        try{
            //createMethodSinge创建方法
            if(Tools.string.isEmpty(code)){
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            }
            AuthLoginVo vo = pamWechatMemberService.wechatLogin(code, loginType, request.getRemoteAddr());
            return JsonResult.newSuccess(vo);
        } catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        } catch(WxErrorException e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    @RequestMapping("/send_sms")
    public JsonResult<String> sendSMS(String phone){
        try{
            //createMethodSinge创建方法
            if(Tools.string.isEmpty(phone)){
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            }
            return JsonResult.newSuccess(pamWechatMemberService.sendActivationCode(phone));
        } catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }
    }

    /**
     * @description: 校验验证码
     * @param:
     * @return Boolean
     * @author Mr.Simple
     * @date 2019/4/16 16:09
     */
    @RequestMapping("/validator")
    public JsonResult<Boolean> validator(HttpSession session, String phone, String activationCode){
        try{
            if(Tools.string.isEmpty(phone) || Tools.string.isEmpty(activationCode)){
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            }
            Long memberId = 0L;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            return JsonResult.newSuccess(pamWechatMemberService.validatorActivationCode(memberId, phone, activationCode));
        } catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping("/generate_kaptcha")
    public JsonResult<String> generateKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        try{


            return JsonResult.newSuccess(iPamMemberService.updateKaptcha(httpServletRequest,httpServletResponse));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch (Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    @RequestMapping("/validator_kaptcha")
    public JsonResult<String> validatorKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String code){
        try{

            return JsonResult.newSuccess(iPamMemberService.validatorKaptcha(httpServletRequest,httpServletResponse,code));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }
    }


}


