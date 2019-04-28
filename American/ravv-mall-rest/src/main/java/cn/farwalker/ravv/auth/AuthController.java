
package cn.farwalker.ravv.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.farwalker.ravv.service.email.IEmailService;
import cn.farwalker.ravv.service.member.pam.constants.LoginTypeEnum;
import cn.farwalker.ravv.service.member.thirdpartaccount.biz.IMemberThirdpartAccountService;
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
    private IEmailService iEmailService;

    @Autowired
    private IMemberThirdpartAccountService memberThirdpartAccountService;


    /**
     * @description: 注册
     * @param: email,password,lastname,firstname
     * @return json
     * @author Mr.Simple
     * @date 2018/11/9 17:57
     */
    @RequestMapping("/register")
    public JsonResult<String> register(HttpServletRequest request, String email, String password, String lastName, String firstName,
                                       @RequestParam(value = "referralCode", required = false, defaultValue = "abc*def*") String referralCode){


        try{
            //createMethodSinge创建方法
            if(Tools.string.isEmpty(lastName) || Tools.string.isEmpty(firstName)){
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR + "firstName, lastName is null");
            }
            if(Tools.string.isEmpty(email)){
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            }
            if(Tools.string.isEmpty(password)){
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            }
            String ip = request.getRemoteAddr();
            return JsonResult.newSuccess(iPamMemberService.createMember(email,password,ip, lastName, firstName, referralCode));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }
    }
    @RequestMapping("/email_login")
    public JsonResult<AuthLoginVo> emailLogin( String email, String password){


        try{
            //createMethodSinge创建方法
            if(Tools.string.isEmpty(email)){
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            }
            if(Tools.string.isEmpty(password)){
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            }
            AuthLoginVo vo =iPamMemberService.emailLogin(email,password);
            return JsonResult.newSuccess(vo);
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping("/thirdpart_login")
    public JsonResult<AuthLoginVo> thirdpartLogin(HttpServletRequest request, String firstname, String lastname, String email,
                                               String userId, String avator, LoginTypeEnum loginType){
        try{
            //createMethodSinge创建方法
            if(Tools.string.isEmpty(firstname) || Tools.string.isEmpty(lastname) || Tools.string.isEmpty(email) ||
                    Tools.string.isEmpty(userId) || Tools.string.isEmpty(avator)){
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            }
            String ip = request.getRemoteAddr();
            return JsonResult.newSuccess(memberThirdpartAccountService.thirdpartLogin(firstname, lastname, email, userId,
                                                                avator, ip, loginType));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }
    }

    /**
     * @description: 修改密码验证旧密码
     * @param:
     * @return json
     * @author Mr.Simple
     * @date 2019/4/1 16:17
     */
    @RequestMapping("/verified_old_password")
    public JsonResult<String> verifiedOldPassword(HttpSession session, String oldPassword){

        try{
            Long memberId = 0L;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            if(Tools.string.isEmpty(oldPassword))
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return JsonResult.newSuccess(iPamMemberService.verifiedOldPassword(memberId, oldPassword));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping("/modify_password")
    public JsonResult<String> modifyPassword(HttpSession session, String newPassword){

        try{
            Long memberId = 0L;
            if(session.getAttribute("memberId") != null)
                memberId = (Long)session.getAttribute("memberId");
            else
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            if(Tools.string.isEmpty(newPassword))
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return JsonResult.newSuccess(iPamMemberService.modifyPassword(memberId, newPassword));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }
    }



    /**
     * @description: 发送验证码认证邮箱
     * @param: email
     * @return json
     * @author Mr.Simple
     * @date 2018/11/9 17:49
     */
    @RequestMapping("/email_verification")
    public JsonResult<String> verifiedInfoEmail(String email){

        try{
            if(Tools.string.isEmpty(email))
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return JsonResult.newSuccess(iPamMemberService.verifiedInfoEmail(email));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }
    }

    /**
     * @description: 发送验证码修改密码（忘记密码时调用此接口）
     * @param: email
     * @return json
     * @author Mr.Simple
     * @date 2018/11/9 17:49
     */
    @RequestMapping("/email_modify_password")
    public JsonResult<String> modifyPassEmail(String email){

        try{
            if(Tools.string.isEmpty(email))
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return JsonResult.newSuccess(iPamMemberService.updatePassEmail(email));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }
    }

    /**
     * @description: 注册用户判断验证码
     * @param: email,activationCode
     * @return json
     * @author Mr.Simple
     * @date 2018/11/9 17:50
     */
    @RequestMapping("/validator_for_registered")
    public JsonResult<String> validator(String email, String activationCode){

        try{
            if(Tools.string.isEmpty(email) || Tools.string.isEmpty(activationCode))
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return JsonResult.newSuccess(iPamMemberService.updateAndValidatorForRegistered(email, activationCode));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }
    }

    /**
     * @description: 忘记密码判断验证码
     * @param: email,activationCode
     * @return Json
     * @author Mr.Simple
     * @date 2018/11/17 14:57
     */
    @RequestMapping("/validator_for_modify_pass")
    public JsonResult<String> validatorForModifyPass(String email, String activationCode){

        try{
            if(Tools.string.isEmpty(email) || Tools.string.isEmpty(activationCode))
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return JsonResult.newSuccess(iPamMemberService.validatorForModifyPass(email, activationCode));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }
    }

    /**
     * @description: 注册用户修改密码
     * @param: email,newPassword
     * @return json
     * @author Mr.Simple
     * @date 2018/11/9 17:51
     */
    @RequestMapping("/modify_pass_forget")
    public JsonResult<String> modifyPassForget(String email, String newPassword, String activationCode){

        try{
            if(Tools.string.isEmpty(email))
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            if(Tools.string.isEmpty(newPassword))
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            if(Tools.string.isEmpty(activationCode))
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return JsonResult.newSuccess(iPamMemberService.updatePass(email, newPassword, activationCode));
        }
        catch(WakaException e){
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


    @RequestMapping("/email_test")
    public JsonResult<String> emailTest(String email){
        try{
            if(Tools.string.isEmpty(email))
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            iEmailService.sendEmailForTest(email,"邮箱认证");
            return JsonResult.newSuccess("success");
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping("/test_mybatis")
    public JsonResult<String> testMybatis(){
        try{


            return JsonResult.newSuccess(iPamMemberService.testResultType());
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping("/test_param")
    public JsonResult<String> test_param(){
        try{


            return JsonResult.newSuccess(iPamMemberService.testParamMap());
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }
    }


}


