package cn.farwalker.ravv.service.member.basememeber.biz;

import cn.farwalker.ravv.service.member.basememeber.model.MemberExVo;
import cn.farwalker.ravv.service.member.basememeber.model.MemberInfoVo;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * Created by asus on 2018/11/8.
 */
public interface IMemberService {
     MemberExVo getBasicInfo(Long memberId, String loginType);

     MemberExVo addBasicInfo(Long memberId, MemberInfoVo memberInfo);

    /**
     * //如果没有登录，则为游客身份，为其分配一个memberId
     * @param httpSession
     * @return 若可分配且分配成功则返回true，若已有memberId 则返回false
     */
    String addGuestToMember(HttpSession httpSession);
}
