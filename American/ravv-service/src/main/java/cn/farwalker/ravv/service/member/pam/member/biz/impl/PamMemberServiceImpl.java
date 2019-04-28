package cn.farwalker.ravv.service.member.pam.member.biz.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import cn.farwalker.ravv.service.member.pam.member.dao.IPamMemberDao;
import cn.farwalker.ravv.service.member.pam.member.model.TestParam;
import cn.farwalker.ravv.service.member.pam.member.model.TestVo;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import com.baomidou.mybatisplus.mapper.Condition;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.farwalker.ravv.service.email.IEmailService;
import cn.farwalker.ravv.service.member.basememeber.biz.IMemberBiz;
import cn.farwalker.ravv.service.member.basememeber.constants.ApplyStatusEnum;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.member.pam.constants.LoginTypeEnum;
import cn.farwalker.ravv.service.member.pam.member.biz.IPamMemberBiz;
import cn.farwalker.ravv.service.member.pam.member.biz.IPamMemberService;
import cn.farwalker.ravv.service.member.pam.member.model.AuthLoginVo;
import cn.farwalker.ravv.service.member.pam.member.model.PamMemberBo;
import cn.farwalker.waka.auth.util.JwtTokenUtil;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.util.MD5PasswordUtil;
import cn.farwalker.waka.util.Tools;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
public class PamMemberServiceImpl implements IPamMemberService {


    @Autowired
    private IMemberBiz iMemberBiz;

    @Autowired
    private IPamMemberBiz iPamMemberBiz;

    @Autowired
    private IEmailService iEmailService;


    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private IPamMemberDao iPamMemberDao;

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    private static final String avator = "http://ravv.qiniu.farwalker.cn/task_cutover_attachment/attachment/191M91702XCA0000/app.jpg";


    /**
     *
     * @param: email,password,ip
     * @return String
     * @author Mr.Simple
     * @date 2018/11/8 10:48
     */
    @Override
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public String createMember(String email, String password, String ip, String lastName,
                               String firstName, String referralCode) {

        //查询这个账号是否之前有登录记录
        PamMemberBo condition = new PamMemberBo();
        condition.setEmailAccount(email);
        EntityWrapper<PamMemberBo> wrapperPamMember = new EntityWrapper<>(condition);
        PamMemberBo temp = iPamMemberBiz.selectOne(wrapperPamMember);
        if(temp != null)
            throw new WakaException(RavvExceptionEnum.USER_DUPLICATE_REGISTER_ERROR);
        //先插入数据至member表
        //id（自动生成）,register_time,status,order_num,login_count,point,advance,advance_freeze不能为空
        MemberBo memberBo = new MemberBo();
        Date current = new Date();
        long point = 0;
        memberBo.setRegisterTime(current);
        memberBo.setStatus( ApplyStatusEnum.ADUITING );
        memberBo.setOrderNum(0);
        memberBo.setLoginCount(0);
        memberBo.setPoint(point);
        memberBo.setAvator(QiniuUtil.getRelativePath(avator));
        memberBo.setAdvance(BigDecimal.ZERO);
        memberBo.setAdvanceFreeze(BigDecimal.ZERO);
        memberBo.setRegIp(ip);
        //生成推荐码
        memberBo.setReferralCode(Tools.string.generateShortUuid());
        //插入推荐人的推荐码
        memberBo.setReferrerReferalCode(referralCode);
        memberBo.setLastname(lastName);
        memberBo.setFirstname(firstName);
        boolean memberInsert = iMemberBiz.insert(memberBo);
        if(!memberInsert)
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        log.info("------------------------");
        log.info("自动生成主键:{}", memberBo.getId());
        //插入数据至pammember表中
        //生成盐
        String salt = Tools.salt.createSalt();
        String passwordSalt = password + "@" + salt;
        String pwd = Tools.md5.md5Hex(passwordSalt);
        PamMemberBo pamMemberBo = new PamMemberBo();
        pamMemberBo.setType(LoginTypeEnum.EMAIL.getKey());
        pamMemberBo.setEmailAccount(email);
        pamMemberBo.setPassword(pwd);
        
        pamMemberBo.setSalt(salt);
        pamMemberBo.setEnabled(Boolean.TRUE);
        pamMemberBo.setMemberId(memberBo.getId());
        pamMemberBo.setGmtCreate(current);
        pamMemberBo.setGmtModified(current);
        boolean pamMemberInsert = iPamMemberBiz.insert(pamMemberBo);
        if(!pamMemberInsert)
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
//        iEmailService.asynSendEmail(email,"邮箱认证");
        return "register success";
    }

    /**
     * @description: 忘记密码修改密码
     * @param:
     * @return string
     * @author Mr.Simple
     * @date 2019/4/1 16:15
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String updatePass(String email, String newPassword, String activationCode) {
        //判断验证是否成功
        if(!iEmailService.validator(email, activationCode))
            throw new WakaException(RavvExceptionEnum.USER_EMAIL_VERIFICATION_FAILED);
        //从数据库获取密码盐
        PamMemberBo condition = new PamMemberBo();
        condition.setEmailAccount(email);
        //从数据库获取之前的加密盐
        EntityWrapper<PamMemberBo> conditionWrapper = new EntityWrapper<PamMemberBo>(condition);
        PamMemberBo pamMemberBo = iPamMemberBiz.selectOne(conditionWrapper);
        if(!updatePasswordByAll(pamMemberBo, newPassword))
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        //更新member表里的email字段
        MemberBo memberBo = new MemberBo();
        memberBo.setId(pamMemberBo.getMemberId());
        memberBo.setEmail(email);
        boolean updateEmail = iMemberBiz.updateById(memberBo);
        if(!updateEmail)
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        return "password update";
    }

    /**
     * @description: 邮箱认证发送验证码
     * @param: email
     * @return string
     * @author Mr.Simple
     * @date 2018/11/9 15:20
     */
    @Override
    public String verifiedInfoEmail(String email) {
        iEmailService.asynSendEmail(email,"邮箱认证");
        return "verification code had send";
    }

    /**
     * @description: 修改密码验证旧密码
     * @param:
     * @return string
     * @author Mr.Simple
     * @date 2019/4/1 16:14
     */
    @Override
    public String verifiedOldPassword(Long memberId, String password) {
        //获取加密盐
        PamMemberBo pamMemberBo = iPamMemberBiz.selectOne(Condition.create()
                                    .eq(PamMemberBo.Key.memberId.toString(), memberId));
        if(pamMemberBo == null){
            throw new WakaException(RavvExceptionEnum.USER_NO_REGISTER);
        }
        String salt = pamMemberBo.getSalt();
        String oldPassword = pamMemberBo.getPassword();
        String passSalt = password + "@" + salt;
        if(!oldPassword.equals(Tools.md5.md5Hex(passSalt))){
            throw new WakaException(RavvExceptionEnum.INCORRECT_PASSWORD);
        }
        return "verified success";
    }

    /**
     * @description: 修改密码（非忘记密码）
     * @param: memberId, newPassword
     * @return string
     * @author Mr.Simple
     * @date 2019/4/1 16:22
     */
    @Override
    public String modifyPassword(Long memberId, String newPassword) {
        PamMemberBo pamMemberBo = iPamMemberBiz.selectOne(Condition.create()
                .eq(PamMemberBo.Key.memberId.toString(), memberId));
        if(pamMemberBo == null){
            throw new WakaException(RavvExceptionEnum.USER_NO_REGISTER);
        }
        if(!updatePasswordByAll(pamMemberBo, newPassword)){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR + "更新密码");
        }
        return "update success";
    }

    /**
     * @description: 忘记密码发送验证码
     * @param: email
     * @return string
     * @author Mr.Simple
     * @date 2018/11/9 15:21
     */
    @Override
    public String updatePassEmail(String email) {
        //修改密码之前判断用户输入的邮箱是否已注册
        PamMemberBo condition = new PamMemberBo();
        condition.setEmailAccount(email);
        EntityWrapper<PamMemberBo> conditionWrapper = new EntityWrapper<PamMemberBo>(condition);
        PamMemberBo pamMemberBo = iPamMemberBiz.selectOne(conditionWrapper);
        if(pamMemberBo == null)
            throw new WakaException(RavvExceptionEnum.USER_NO_REGISTER);
        iEmailService.asynSendEmail(email,"修改密码");
        return "verification code had send";
    }

    /**
     * @description: 判断验证码是否有效
     * @param: email,activationCode
     * @return string
     * @author Mr.Simple
     * @date 2018/11/9 15:24
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String updateAndValidatorForRegistered(String email, String activationCode) {
        //判断验证是否成功
        if(!iEmailService.validator(email, activationCode))
            throw new WakaException(RavvExceptionEnum.USER_EMAIL_VERIFICATION_FAILED);


        PamMemberBo queryPamMember = new PamMemberBo();
        queryPamMember.setEmailAccount(email);
        queryPamMember.setType(LoginTypeEnum.EMAIL.getKey());
        EntityWrapper  queryPamWrapper =  new EntityWrapper<>();
        queryPamWrapper.setEntity(queryPamMember);
        PamMemberBo resultPamMember = iPamMemberBiz.selectOne(queryPamWrapper);
        if(resultPamMember == null)
            throw new WakaException(RavvExceptionEnum.USER_NO_REGISTER);

        MemberBo queryMemberBo = new MemberBo();
        queryMemberBo.setEmail(email);
        EntityWrapper  queryMemberBoWrapper =  new EntityWrapper<>();
        queryMemberBoWrapper.setEntity(queryMemberBo);
        MemberBo resultMemberBo = iMemberBiz.selectOne(queryMemberBoWrapper);
        //查询当前邮箱在MemberBo表中是否已经存在,如果已经存在则合并账户
        if(resultMemberBo != null&&resultMemberBo.getEmail()!=null){
            iMemberBiz.deleteById(resultPamMember.getMemberId());
            resultPamMember.setMemberId(resultMemberBo.getId());
            iPamMemberBiz.updateById(resultPamMember);
            return "verified successfully! your email has been registered in our system.Account merged";
        }
        //更新用户表中email字段
        MemberBo updateMemberBo = new MemberBo();
        updateMemberBo.setId(resultPamMember.getMemberId());
        updateMemberBo.setEmail(email);
        boolean updateEmail = iMemberBiz.updateById(updateMemberBo);
        if(!updateEmail)
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        return "verified successfully!";
    }

    @Override
    public String validatorForModifyPass(String email, String activationCode) {
        //判断验证是否成功
        if(!iEmailService.validator(email, activationCode))
            throw new WakaException(RavvExceptionEnum.USER_EMAIL_VERIFICATION_FAILED);
        return "verification success";
    }


//    /**
//     * @description: 修改密码验证信息（可用作验证码测试）
//     * @param: email,activationCode
//     * @return string
//     * @author Mr.Simple
//     * @date 2018/11/9 15:25
//     */
//    @Override
//    public String modifyValidator(String email, String activationCode) {
//        //判断验证是否成功
//        if(!iEmailService.validator(email, activationCode))
//            throw new WakaException("验证码无效");
//        return null;
//    }

    public AuthLoginVo emailLogin(String account, String password) {
        PamMemberBo queryPamMember = new PamMemberBo();
        queryPamMember.setEmailAccount(account);
        EntityWrapper  queryWrapper =  new EntityWrapper<>();
        queryWrapper.setEntity(queryPamMember);

        PamMemberBo resultPamMember = iPamMemberBiz.selectOne(queryWrapper);
        if(resultPamMember == null) {
            throw new WakaException(RavvExceptionEnum.USER_CHECK_FAILED);
        }

        String salt = resultPamMember.getSalt();
        String dbEncryptedPassword = resultPamMember.getPassword();
        long memberId = resultPamMember.getMemberId();
        if(MD5PasswordUtil.verify(password, salt, dbEncryptedPassword)) {
            final String randomKey = jwtTokenUtil.getRandomKey();
            if(memberId == 0)
                throw new WakaException(RavvExceptionEnum.USER_MEMBER_ID_ERROR);
            final String token = jwtTokenUtil.generateToken(account,memberId,LoginTypeEnum.EMAIL.getLabel(), randomKey,true);
            AuthLoginVo authLoginVo = new AuthLoginVo();
            authLoginVo.setLoginType(LoginTypeEnum.EMAIL.getLabel());
            authLoginVo.setToken(token);
            authLoginVo.setAccount(account);
            authLoginVo.setRandomKey(randomKey);
            authLoginVo.setLoginType(LoginTypeEnum.EMAIL.getLabel());
            return authLoginVo;
        } else {
            throw new WakaException(RavvExceptionEnum.USER_ACCOUNT_OR_PASSWORD_INCORRECT);
        }
    }

    @Override
    public String updateKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
            httpServletRequest.getSession().setAttribute("vrifyCode", createText);
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            throw new WakaException(RavvExceptionEnum.USER_CAPTCHA_FAILED);
        }
        //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();

        return "generated kaptcha success";
    }

    @Override
    public String validatorKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String code) {
        String captchaId = (String) httpServletRequest.getSession().getAttribute("vrifyCode");
        log.info("Session vrifyCode:{};form vrifyCode:{}",captchaId, code);
        if(!captchaId.equals(code))
            throw new WakaException(RavvExceptionEnum.USER_CAPTCHA_VALIDATOR_FAILED);
        else
            return "verified successfully!";
    }

    @Override
    public String emailTest(String email) {

        iEmailService.sendEmailForTest(email,"邮箱认证");


        return "verification code had send";
    }

    @Override
    public String testResultType() {
        TestVo t = new TestVo();
        t = iPamMemberDao.getInfo();
        if(t == null)
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        return "success";
    }

    @Override
    public String testParamMap() {
        TestParam t1 = new TestParam();
        long memberId1 = Long.valueOf("1062590086136315905").longValue();
        t1.setMemberId(memberId1);
        t1.setPoint(2);
        if(Tools.number.isEmpty(iPamMemberDao.updatePointById(t1)))
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        return "success";
    }


    private boolean updatePasswordByAll(PamMemberBo pamMemberBo, String newPassword){
        String salt = pamMemberBo.getSalt();
        String passSalt = newPassword + "@" + salt;
        String pwd = Tools.md5.md5Hex(passSalt);
        pamMemberBo.setPassword(pwd);
        pamMemberBo.setGmtModified(new Date());

        return iPamMemberBiz.updateById(pamMemberBo);
    }


}
