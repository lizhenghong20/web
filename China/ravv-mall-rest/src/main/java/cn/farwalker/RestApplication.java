package cn.farwalker;

import cn.farwalker.waka.util.SpringContextUtil;
import cn.farwalker.waka.util.Tools;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = {"cn.farwalker.waka","cn.farwalker.ravv"})
public class RestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
//        ApplicationContext app = SpringApplication.run(RestApplication.class, args);
//        Tools.springContext.setApplicationContext(app);
    }
}
