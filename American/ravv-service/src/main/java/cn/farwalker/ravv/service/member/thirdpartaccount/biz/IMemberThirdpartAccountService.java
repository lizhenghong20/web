package cn.farwalker.ravv.service.member.thirdpartaccount.biz;

import cn.farwalker.ravv.service.member.pam.member.model.AuthLoginVo;

/**
 * Created by asus on 2018/11/8.
 */
public interface IMemberThirdpartAccountService {
    public AuthLoginVo facebookLogin(String userID, String name);

    public AuthLoginVo googleLogin(String userID, String name);

    public String  validatorForFacebook(String userID,String email, String activationCode);

    public String  validatorForGoogle(String userID,String email, String activationCode);

}
