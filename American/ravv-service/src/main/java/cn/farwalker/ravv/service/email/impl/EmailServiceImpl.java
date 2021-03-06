package cn.farwalker.ravv.service.email.impl;

import cn.farwalker.ravv.service.email.IEmailService;
import cn.farwalker.waka.cache.CacheManager;
import cn.farwalker.waka.util.Tools;

import com.sun.mail.smtp.SMTPTransport;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class EmailServiceImpl implements IEmailService {
	/** 需要在application-dev.yml 增加下面的配置就能注入了
	mail:
    host: smtp.163.com
    username: 7661601@163.com
    password: 960816
    properties:
        mail:
          smtp:
            auth: true
            starttls:
              enable: true
              required: true
	 */

    private static final Lock lock = new ReentrantLock(true);


    @Autowired
    private JavaMailSender mail;

    @Autowired
    @Qualifier("mailProperties")
    private Properties mailProperties;

    @Override
    public String sendEmail(String email, String message) {

        return "verification code has send";

    }

    @Async
    @Override
    public void asynSendEmail(String email, String message){

        //生成激活码
        String activationCode = Tools.salt.createActivationCode();
        //将过期时间加入激活码中
        String appendCode = Tools.timeValue.codeAppendTime(activationCode);
        try{
            lock.lock();
            //存到redis
            CacheManager.cache.put(email,appendCode);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        //发送验证码
//        SimpleMailMessage sendMessage = new SimpleMailMessage();
//        sendMessage.setFrom(mailProperties.getProperty("username"));
//        sendMessage.setTo(email);
//
//        sendMessage.setSubject("验证邮件");
//        sendMessage.setText("验证码：" + activationCode + "，验证码有效时间为30分钟");
//
//        mail.send(sendMessage);
        sendByGmail(email, activationCode);
    }


    @Async
    @Override
    public void sendEmailForTest(String email, String message) {
       //生成激活码
       String activationCode = Tools.salt.createActivationCode();
       //将过期时间加入激活码中
       String appendCode = Tools.timeValue.codeAppendTime(activationCode);

       CacheManager.cache.put(email,appendCode);

       log.info("进入测试email:{}",email);

        sendByGmail(email, activationCode);

    }

    private void sendByGmail(String email, String activationCode){
        final String username = mailProperties.getProperty("username");
        final String password = mailProperties.getProperty("password");
        log.info("注入账号密码:{},{}",mailProperties.getProperty("username"),mailProperties.getProperty("password"));
        Session session = Session.getDefaultInstance(mailProperties,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        session.setDebug(true);
        // -- Create a new message --
        Message msg = new MimeMessage(session);

       // -- Set the FROM and TO fields --
        try {
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            msg.setSubject("E-mail verification");
            msg.setText("【Shop.live】Your Shop.live ID verification code is ：" + activationCode + "，\n" +
                    "verification code is valid for 30 minutes");
            msg.setSentDate(new Date());
            Transport.send(msg);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
   }

    @Override
    public boolean validator(String email, String activationCode) {
        //从redis根据邮箱取出验证码
        String cacheCode = CacheManager.cache.get(email);
        String code = Tools.timeValue.getActivationCode(cacheCode);
        //对比验证
        if(!code.equals(activationCode))
            return false;

        return true;
    }


}
