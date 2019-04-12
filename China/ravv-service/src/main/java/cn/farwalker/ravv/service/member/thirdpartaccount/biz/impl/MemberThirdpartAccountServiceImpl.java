package cn.farwalker.ravv.service.member.thirdpartaccount.biz.impl;

import cn.farwalker.ravv.service.email.IEmailService;
import cn.farwalker.ravv.service.member.basememeber.biz.IMemberBiz;
import cn.farwalker.ravv.service.member.basememeber.constants.ApplyStatusEnum;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.member.thirdpartaccount.model.MemberThirdpartAccountBo;
import cn.farwalker.ravv.service.member.pam.constants.LoginTypeEnum;
import cn.farwalker.ravv.service.member.pam.member.model.AuthLoginVo;
import cn.farwalker.ravv.service.member.thirdpartaccount.biz.IMemberThirdpartAccountBiz;
import cn.farwalker.ravv.service.member.thirdpartaccount.biz.IMemberThirdpartAccountService;
import cn.farwalker.waka.auth.util.JwtTokenUtil;
import cn.farwalker.waka.core.RavvExceptionEnum;

import cn.farwalker.waka.core.WakaException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by asus on 2018/11/8.
 */
@Service
public class MemberThirdpartAccountServiceImpl implements IMemberThirdpartAccountService {
    @Autowired
    private IMemberBiz iMemberBiz;

    @Autowired
    private IEmailService iEmailService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private IMemberThirdpartAccountBiz iMemberThirdpartAccountBiz;

    public AuthLoginVo facebookLogin(String userID, String name){

        return thirdLogin(userID, name, LoginTypeEnum.FACEBOOK);
    }

    @Override
    public AuthLoginVo googleLogin(String userID, String name) {
        return thirdLogin(userID, name, LoginTypeEnum.GOOGLE);
    }

    public AuthLoginVo thirdLogin(String userID, String name, LoginTypeEnum loginType){
        MemberThirdpartAccountBo queryMember = new MemberThirdpartAccountBo();
        queryMember.setAccount(userID);
        queryMember.setAccountType(loginType.getKey());
        EntityWrapper  queryWrapper =  new EntityWrapper<>();
        queryWrapper.setEntity(queryMember);
        MemberThirdpartAccountBo resultMember = iMemberThirdpartAccountBiz.selectOne(queryWrapper);

        queryMember.setBindTime(new Date());
        long memberId = 0;
        if(resultMember == null){
            insertMemberBo(queryMember,name);
            if(!iMemberThirdpartAccountBiz.insert(queryMember)){
                throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
            }else{
                memberId =  queryMember.getMemberId();
            }
        }else{
            updateMemberBo(resultMember,name);
            iMemberThirdpartAccountBiz.updateById(resultMember);
            memberId = resultMember.getMemberId();
        }


        String randomKey = jwtTokenUtil.getRandomKey();
        if(memberId == 0)
            throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
        String token = jwtTokenUtil.generateToken(userID,memberId, loginType.getLabel(), randomKey,true);
        AuthLoginVo authLoginVo = new AuthLoginVo();
        authLoginVo.setToken(token);
        authLoginVo.setAccount(userID);
        authLoginVo.setRandomKey(randomKey);
        authLoginVo.setLoginType(loginType.getLabel());
        return authLoginVo;
    }


    @Override
    public String validatorForFacebook(String userID,String email, String activationCode) {
        return validatorThirdLogin(userID, email, activationCode, LoginTypeEnum.FACEBOOK);
    }

    @Override
    public String validatorForGoogle(String userID, String email, String activationCode) {
        return validatorThirdLogin(userID, email, activationCode, LoginTypeEnum.GOOGLE);
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String validatorThirdLogin(String userID,String email, String activationCode, LoginTypeEnum loginType) {
        //判断验证是否成功
        if(!iEmailService.validator(email, activationCode))
            throw new WakaException(RavvExceptionEnum.USER_EMAIL_VERIFICATION_FAILED);

        //验证成功，向member表更新email信息
        MemberThirdpartAccountBo queryThirdMember = new MemberThirdpartAccountBo();
        queryThirdMember.setAccount(userID);
        queryThirdMember.setAccountType(loginType.getKey());
        EntityWrapper  queryWrapper =  new EntityWrapper<>();
        queryWrapper.setEntity(queryThirdMember);
        MemberThirdpartAccountBo resultThirdMember = iMemberThirdpartAccountBiz.selectOne(queryWrapper);
        if(resultThirdMember == null)
            throw new WakaException(RavvExceptionEnum.USER_NO_REGISTER);

        MemberBo queryMemberBo = new MemberBo();
        queryMemberBo.setEmail(email);
        EntityWrapper  queryMemberBoWrapper =  new EntityWrapper<>();
        queryMemberBoWrapper.setEntity(queryMemberBo);
        MemberBo resultMemberBo = iMemberBiz.selectOne(queryMemberBoWrapper);
        //查询当前邮箱在MemberBo表中是否已经存在,如果已经存在,且未绑定邮箱，则合并账户
        if(resultMemberBo != null&&resultMemberBo.getEmail()!=null){
            iMemberBiz.deleteById(resultThirdMember.getMemberId());
            resultThirdMember.setMemberId(resultMemberBo.getId());
            iMemberThirdpartAccountBiz.updateById(resultThirdMember);
            return "verified successfully! your email has been registered in our system.Account merged";
        }
        //更新用户表中email字段
        MemberBo updateMemberBo = new MemberBo();
        updateMemberBo.setId(resultThirdMember.getMemberId());
        updateMemberBo.setEmail(email);
        boolean updateEmail = iMemberBiz.updateById(updateMemberBo);
        if(!updateEmail)
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        return "verified successfully!";
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    //插入MemberBo,同时更新MemberThirdpartAccountBo的memberId
    public void insertMemberBo(MemberThirdpartAccountBo queryMember,String name){
        MemberBo memberBo = new MemberBo();
        Date current = new Date();
        long point = 0;
        memberBo.setRegisterTime(current);
        memberBo.setStatus( ApplyStatusEnum.ADUITING );
        memberBo.setOrderNum(0);
        memberBo.setLoginCount(0);
        memberBo.setPoint(point);
        memberBo.setAdvance(BigDecimal.ZERO);
        memberBo.setAdvanceFreeze(BigDecimal.ZERO);
        //memberBo.setName(name);
        if(!StringUtils.isEmpty(name)){
            parseName(memberBo,name);
        }
        boolean memberInsert = iMemberBiz.insert(memberBo);
        if(!memberInsert)
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        queryMember.setMemberId(memberBo.getId());

    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void updateMemberBo(MemberThirdpartAccountBo resultMember,String name){
        MemberBo memberBo = iMemberBiz.selectById(resultMember.getMemberId());
        if(memberBo == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        if(!StringUtils.isEmpty(name)){
            parseName(memberBo,name);
        }
        iMemberBiz.updateById(memberBo);
    }


   private void parseName(MemberBo memberBo,String name){
       /*String[] items = name.split(" ");
       if(items.length == 1){
           memberBo.setFirstname(items[0]);
       }else if(items.length >= 2){
           memberBo.setFirstname(items[0]);
           memberBo.setLastname(items[items.length-1]);
       }*/
	   memberBo.setName(name);
   }


}
