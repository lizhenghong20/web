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
@Profile("prod")
public class EmailConfigForGmail {

    @Autowired
    @Qualifier("mailProperties")
    private Properties mailProperties;

    private String sslEnable = "true";

    private String socketClass = "javax.net.ssl.SSLSocketFactory";

    private String fallback = "false";

    private String socketPort = "465";

    private String host = "smtp.gmail.com";

    private String port = "465";

    private String auth = "true";

    private String username = "notification@shop.live";

    private String password = "shop.LIVE";

//    mail:
//    smtp:
//    user: simple.ding816@gmail.com
//    password: 0816.hao
//    starttls:
//    enable: true
//    required: true
//    ssl:
//    enable: true
//    socketFactory:
//    port: 465
//    class: javax.net.ssl.SSLSocketFactory
//    fallback: false
//    host: smtp.gmail.com
//    auth: true
//    port: 465


    //qq密码,谷歌密码shop.LIVE,0816.hao
//    private static final String password = "iqrfiusvhrzjbegg";
    //qq账号,谷歌账号notification@shop.live,simple.ding816@gmail.com
//    public static final String username = "756518776@qq.com";

    @Bean
    public Properties mailProperties(){
        Properties props = new Properties();
        //        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.ssl.enable", sslEnable);
        props.setProperty("mail.smtp.host", host);
        props.setProperty("mail.smtp.auth", auth);
        props.setProperty("mail.smtp.port", port);
        props.setProperty("mail.smtp.socketFactory.port", socketPort);
        props.setProperty("mail.smtp.socketFactory.class", socketClass);
//        props.setProperty("mail.smtp.socketFactory.fallback", fallback);
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
