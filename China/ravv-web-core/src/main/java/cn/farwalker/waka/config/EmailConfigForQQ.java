package cn.farwalker.waka.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@Profile({"dev","daily"})
public class EmailConfigForQQ {

    @Autowired
    @Qualifier("mailProperties")
    private Properties mailProperties;


    private String tlsEnable = "true";


    private String tlsRequired = "true";


    private String host = "smtp.qq.com";


    private String port = "587";


    private String auth = "true";


    private String username = "756518776@qq.com";


    private String password = "suqboifqqhwjbebh";


    //qq密码,谷歌密码shop.LIVE,0816.hao
//    private static final String password = "iqrfiusvhrzjbegg";
    //qq账号,谷歌账号notification@shop.live,simple.ding816@gmail.com
//    public static final String username = "756518776@qq.com";

    @Bean
    public Properties mailProperties(){
        Properties props = new Properties();
        props.setProperty("mail.smtp.starttls.enable", tlsEnable);
        props.setProperty("mail.smtp.starttls.required", tlsRequired);
        props.setProperty("mail.smtp.host", host);
        props.setProperty("mail.smtp.auth", auth);
        props.setProperty("mail.smtp.port", port);
        props.setProperty("password",password);
        props.setProperty("username",username);
        return props;
    }


    @Bean
    public JavaMailSender mail(){

        JavaMailSenderImpl mail = new JavaMailSenderImpl();
        mail.setHost(host);
        mail.setUsername(username);
        mail.setPassword(password);
        mail.setPort(Integer.parseInt(port));
        mail.setJavaMailProperties(mailProperties);

        return mail;
    }
}
