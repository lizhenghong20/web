package cn.farwalker.ravv.service.member.pam.wechat.biz;

import cn.farwalker.ravv.common.constants.WechatLoginTypeEnum;
import cn.farwalker.ravv.service.member.pam.member.model.AuthLoginVo;
import cn.farwalker.waka.components.wechatpay.common.exception.WxErrorException;

public interface IPamWechatMemberService {

    public AuthLoginVo wechatLogin(String code, WechatLoginTypeEnum loginType, String ip) throws WxErrorException;

    public String sendActivationCode(String phone);

    public void sendSMS(String phone);

    public Boolean validatorActivationCode(Long memberId, String phone, String activationCode);


}
