package cn.farwalker.ravv.service.member.basememeber.biz;

import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.member.basememeber.model.MemberExVo;
import cn.farwalker.ravv.service.member.basememeber.model.MemberInfoVo;
import cn.farwalker.ravv.service.member.pam.constants.LoginTypeEnum;

/**
 * Created by asus on 2018/11/8.
 */
public interface IMemberService {
    public MemberExVo getBasicInfo(Long memberId, LoginTypeEnum loginType);

    public MemberExVo addBasicInfo(Long memberId, MemberBo memberInfo, LoginTypeEnum loginType);
}
