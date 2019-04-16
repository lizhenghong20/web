package cn.farwalker.ravv.service.member.pam.wechat.biz.impl;

import cn.farwalker.ravv.common.constants.WechatLoginTypeEnum;
import cn.farwalker.ravv.service.member.basememeber.biz.IMemberBiz;
import cn.farwalker.ravv.service.member.basememeber.constants.ApplyStatusEnum;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.member.pam.constants.LoginTypeEnum;
import cn.farwalker.ravv.service.member.pam.member.model.AuthLoginVo;
import cn.farwalker.ravv.service.member.pam.wechat.biz.IPamWechatMemberBiz;
import cn.farwalker.ravv.service.member.pam.wechat.biz.IPamWechatMemberService;
import cn.farwalker.ravv.service.member.pam.wechat.model.PamWechatMemberBo;
import cn.farwalker.waka.auth.util.JwtTokenUtil;
import cn.farwalker.waka.components.wechatpay.common.exception.WxErrorException;
import cn.farwalker.waka.components.wechatpay.mp.api.WxMpInMemoryConfigStorage;
import cn.farwalker.waka.components.wechatpay.mp.api.WxMpService;
import cn.farwalker.waka.components.wechatpay.mp.bean.result.WxMpOAuth2AccessToken;
import cn.farwalker.waka.components.wechatpay.mp.bean.result.WxMpUser;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.Condition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Service
public class PamWechatMemberServiceImpl implements IPamWechatMemberService {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private IPamWechatMemberBiz pamWechatMemberBiz;

    @Autowired
    private IMemberBiz memberBiz;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final static String webAppId = "wxd6edafc887910c4d";

    private final static String webAppSecret = "8020186adf819ca3b8afe0e6a08e67b3";

    private final static String appAppId = "wx474bf03c942287a7";

    private final static String appAppSecret = "2e971612c9223c421be272c336967afd";

    /**
     * @description: 微信登录
     * @param: 
     * @return authLoginVo
     * @author Mr.Simple
     * @date 2019/4/15 15:10
     */ 
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public AuthLoginVo wechatLogin(String code, WechatLoginTypeEnum loginType, String ip) throws WxErrorException {
        //获取微信用户信息，官方建议使用unionId甄别用户信息
        WxMpUser userInfo = getWechatUserInfo(code, loginType);
        //通过unionId查询数据库该用户是否已存在，已存在返回登录信息；否则存入数据库后返回登录信息
        PamWechatMemberBo wechatMemberBo = pamWechatMemberBiz.selectOne(Condition.create()
                                                .eq(PamWechatMemberBo.Key.unionId.toString(), userInfo.getUnionId()));
        if(wechatMemberBo == null){
            //执行插入逻辑
            wechatMemberBo = insertWechatUser(userInfo, ip);
        }
        //执行登录逻辑
        final String randomKey = jwtTokenUtil.getRandomKey();
        final String token = jwtTokenUtil.generateToken(userInfo.getNickname(), wechatMemberBo.getMemberId(),
                                                        LoginTypeEnum.WECHAT.getLabel(), randomKey,true);
        AuthLoginVo authLoginVo = new AuthLoginVo();
        authLoginVo.setLoginType(LoginTypeEnum.WECHAT.getLabel());
        authLoginVo.setToken(token);
        authLoginVo.setAccount(userInfo.getNickname());
        authLoginVo.setRandomKey(randomKey);
        if(wechatMemberBo.getPhone() == null){
            authLoginVo.setBindPhone(false);
        } else{
            authLoginVo.setBindPhone(true);
        }
        authLoginVo.setHeadImgUrl(wechatMemberBo.getHeadImgUrl());

        return authLoginVo;
    }


    private PamWechatMemberBo insertWechatUser(WxMpUser userInfo, String ip){
        //插入到member表
        Date current = new Date();
        MemberBo memberBo = insertMember(userInfo, ip);
        PamWechatMemberBo pamWechatMemberBo = new PamWechatMemberBo();
        Tools.bean.copyProperties(userInfo, pamWechatMemberBo);
        pamWechatMemberBo.setMemberId(memberBo.getId());
        pamWechatMemberBo.setGmtCreate(current);
        pamWechatMemberBo.setGmtModified(current);
        if(!pamWechatMemberBiz.insert(pamWechatMemberBo)){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR + "插入pamwechat失败");
        }
        return pamWechatMemberBo;
    }

    private MemberBo insertMember(WxMpUser userInfo, String ip){
        MemberBo memberBo = new MemberBo();
        Date current = new Date();
        long point = 0;
        memberBo.setNickName(userInfo.getNickname());
        memberBo.setRegisterTime(current);
        memberBo.setStatus(ApplyStatusEnum.ADUITING );
        memberBo.setOrderNum(0);
        memberBo.setLoginCount(0);
        memberBo.setPoint(point);
        memberBo.setAvator(userInfo.getHeadImgUrl());
        memberBo.setAdvance(BigDecimal.ZERO);
        memberBo.setAdvanceFreeze(BigDecimal.ZERO);
        memberBo.setRegIp(ip);
        if(!memberBiz.insert(memberBo)){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR + "插入member失败");
        }
        return memberBo;
    }

    private WxMpUser getWechatUserInfo(String code, WechatLoginTypeEnum loginType) throws WxErrorException {
        //通过code获取accessToken
        WxMpOAuth2AccessToken accessToken = getAccessToken(code, loginType);
        //通过accessToken获取用户信息
        return wxMpService.oauth2getUserInfo(accessToken, null);
    }

    private WxMpOAuth2AccessToken getAccessToken(String code, WechatLoginTypeEnum loginType) throws WxErrorException {
        WxMpInMemoryConfigStorage wxConfig = new WxMpInMemoryConfigStorage();
        if(WechatLoginTypeEnum.APP.equals(loginType)){
            wxConfig.setAppId(appAppId);
            wxConfig.setSecret(appAppSecret);
        } else if(WechatLoginTypeEnum.WEB.equals(loginType)){
            wxConfig.setAppId(webAppId);
            wxConfig.setSecret(webAppSecret);
        }
        wxMpService.setWxMpConfigStorage(wxConfig);
        return wxMpService.oauth2getAccessToken(code);
    }
}
