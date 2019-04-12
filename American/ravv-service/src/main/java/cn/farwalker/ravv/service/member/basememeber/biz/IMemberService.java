package cn.farwalker.ravv.service.member.basememeber.biz;

import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.member.basememeber.model.MemberExVo;
import cn.farwalker.ravv.service.member.basememeber.model.MemberInfoVo;

/**
 * Created by asus on 2018/11/8.
 */
public interface IMemberService {
    public MemberExVo getBasicInfo(Long memberId);

    public MemberExVo addBasicInfo(Long memberId, MemberBo memberInfo);
}
