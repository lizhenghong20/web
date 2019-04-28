package cn.farwalker.ravv.service.member.thirdpartaccount.biz;

import cn.farwalker.ravv.service.member.pam.constants.LoginTypeEnum;
import cn.farwalker.ravv.service.member.pam.member.model.AuthLoginVo;

public interface IMemberThirdpartAccountService {
    public AuthLoginVo thirdpartLogin(String firstname, String lastname, String email, String userId,
                                      String avator, String ip, LoginTypeEnum loginType);
}
