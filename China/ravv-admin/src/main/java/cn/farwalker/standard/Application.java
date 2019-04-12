package cn.farwalker.standard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.farwalker.waka.config.properties.WakaProperties;

/**
 * SpringBoot方式启动类
 *
 * @author Jason Chen
 * @Date 2018/2/10 12:12
 */
@SpringBootApplication
@ComponentScan(basePackages= {"cn.farwalker.waka","cn.farwalker.standard","cn.farwalker.ravv"})
public class Application extends WebMvcConfigurerAdapter {

    protected final static Logger logger = LoggerFactory.getLogger(Application.class);

    @Autowired
    WakaProperties wakaProperties;

    /**
     * 增加swagger的支持
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (wakaProperties.getSwaggerOpen()) {
            registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
            registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.info("Application is success!");
    }
}
