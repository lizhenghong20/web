package cn.farwalker.ravv.service.member.thirdpartaccount.biz.impl;

import cn.farwalker.ravv.service.member.basememeber.biz.IMemberBiz;
import cn.farwalker.ravv.service.member.basememeber.constants.ApplyStatusEnum;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.member.pam.constants.LoginTypeEnum;
import cn.farwalker.ravv.service.member.pam.member.model.AuthLoginVo;
import cn.farwalker.ravv.service.member.thirdpartaccount.biz.IMemberThirdAccountBiz;
import cn.farwalker.ravv.service.member.thirdpartaccount.biz.IMemberThirdAccountService;
import cn.farwalker.ravv.service.member.thirdpartaccount.model.MemberThirdpartAccountBo;
import cn.farwalker.waka.auth.util.JwtTokenUtil;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.Condition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Service
public class MemberThirdAccountServiceImpl implements IMemberThirdAccountService {

    @Autowired
    private IMemberBiz memberBiz;

    @Autowired
    private IMemberThirdAccountBiz memberThirdpartAccountBiz;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public AuthLoginVo thirdPartLogin(String firstName, String lastName, String email, String userId, String avator,
                                      String ip, String loginType) {
        LoginTypeEnum type = null;
        if(LoginTypeEnum.GOOGLE.getLabel().equals(loginType)){
            type = LoginTypeEnum.GOOGLE;
        } else if(LoginTypeEnum.FACEBOOK.getLabel().equals(loginType)){
            type  = LoginTypeEnum.FACEBOOK;
        }
        //先查询userId在第三方表中是否存在
        MemberThirdpartAccountBo thirdpartAccountBo = memberThirdpartAccountBiz.selectOne(Condition.create()
                                .eq(MemberThirdpartAccountBo.Key.userId.toString(), userId)
                                .eq(MemberThirdpartAccountBo.Key.accountType.toString(), type.getKey()));
        MemberBo memberBo = null;
        if(thirdpartAccountBo == null){
            //执行插入逻辑
            //计入member表
            memberBo = insertMember(ip);
            //插入thirdpart表
            thirdpartAccountBo = insertThirdAccount(memberBo.getId(), firstName, lastName, email, userId, avator,
                    type);
        } else {
            memberBo = memberBiz.selectById(thirdpartAccountBo.getMemberId());
        }
        String randomKey = jwtTokenUtil.getRandomKey();
        String token = jwtTokenUtil.generateToken(userId, memberBo.getId(), type.getLabel(), randomKey,false);
        AuthLoginVo authLoginVo = new AuthLoginVo();
        authLoginVo.setToken(token);
        authLoginVo.setAccount(userId);
        authLoginVo.setRandomKey(randomKey);
        authLoginVo.setLoginType(type.getLabel());
        authLoginVo.setAvator(Tools.string.isEmpty(memberBo.getAvator()) ? thirdpartAccountBo.getAvator() :
                                                                            QiniuUtil.getFullPath(memberBo.getAvator()));
        authLoginVo.setFirstname(Tools.string.isEmpty(memberBo.getFirstname()) ? thirdpartAccountBo.getFirstname() :
                                                                            memberBo.getFirstname());
        authLoginVo.setLastname(Tools.string.isEmpty(memberBo.getLastname()) ? thirdpartAccountBo.getLastname() :
                                                                            memberBo.getLastname());
        return authLoginVo;
    }

    private MemberBo insertMember(String ip){
        Date current = new Date();
        long point = 0;
        MemberBo memberBo = new MemberBo();
        memberBo.setRegisterTime(current);
        memberBo.setGmtCreate(current);
        memberBo.setGmtModified(current);
        memberBo.setStatus(ApplyStatusEnum.ADUITING );
        memberBo.setOrderNum(0);
        memberBo.setLoginCount(0);
        memberBo.setPoint(point);
        memberBo.setAdvance(BigDecimal.ZERO);
        memberBo.setAdvanceFreeze(BigDecimal.ZERO);
        memberBo.setRegIp(ip);
        if(!memberBiz.insert(memberBo)){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR + "插入member表失败");
        }
        return memberBo;
    }

    private MemberThirdpartAccountBo insertThirdAccount(Long memberId, String firstname, String lastname, String email,
                                              String userId, String avator, LoginTypeEnum loginTypeEnum){
        Date current  = new Date();
        MemberThirdpartAccountBo memberThirdpartAccountBo = new MemberThirdpartAccountBo();
        memberThirdpartAccountBo.setMemberId(memberId);
        memberThirdpartAccountBo.setFirstname(firstname);
        memberThirdpartAccountBo.setLastname(lastname);
        memberThirdpartAccountBo.setAccountType(loginTypeEnum.getKey());
        memberThirdpartAccountBo.setAvator(avator);
        if(Tools.string.isNotEmpty(email))
            memberThirdpartAccountBo.setEmail(email);
        memberThirdpartAccountBo.setUserId(userId);
        memberThirdpartAccountBo.setGmtCreate(current);
        memberThirdpartAccountBo.setGmtModified(current);
        if(!memberThirdpartAccountBiz.insert(memberThirdpartAccountBo)){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR + "insert thirdmember error");
        }
        return memberThirdpartAccountBo;
    }
}
