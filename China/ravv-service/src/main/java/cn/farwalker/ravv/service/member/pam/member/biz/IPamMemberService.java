package cn.farwalker.ravv.service.member.pam.member.biz;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.farwalker.ravv.service.member.pam.member.model.AuthLoginVo;
import cn.farwalker.waka.core.JsonResult;

import java.io.IOException;

public interface IPamMemberService {

    public String createMember(String email, String password, String ip, String lastname, String firstname, String referralCode);

    public String updatePass(String email, String newPassword, String activationCode);

    public String verifiedInfoEmail(String email);

    public String verifiedOldPassword(Long memberId, String password);

    public String modifyPassword(Long memberId, String newPassword);

    public String updatePassEmail(String email);

    public String updateAndValidatorForRegistered(String email, String activationCode);

    public String validatorForModifyPass(String email, String activationCode);

    public AuthLoginVo emailLogin( String account, String password);

    public String updateKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException;

    public String validatorKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String code);

    public String emailTest(String email);

    public String testResultType();

    public String testParamMap();

}

