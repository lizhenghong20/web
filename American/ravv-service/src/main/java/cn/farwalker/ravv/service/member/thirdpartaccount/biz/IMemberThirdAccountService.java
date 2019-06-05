package cn.farwalker.ravv.service.member.thirdpartaccount.biz;

import cn.farwalker.ravv.service.member.pam.member.model.AuthLoginVo;

public interface IMemberThirdAccountService {
     AuthLoginVo thirdPartLogin(String firstName, String lastName, String email, String userId,
                                      String avator, String ip, String loginType);
}
