package cn.farwalker.ravv.service.member.basememeber.biz;

import cn.farwalker.ravv.service.member.basememeber.model.MemberExVo;
import cn.farwalker.ravv.service.member.basememeber.model.MemberInfoVo;

/**
 * Created by asus on 2018/11/8.
 */
public interface IMemberService {
    public MemberExVo getBasicInfo(Long memberId, String loginType);

    public MemberExVo addBasicInfo(Long memberId, MemberInfoVo memberInfo);
}
