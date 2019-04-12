package cn.farwalker.ravv.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = {"cn.farwalker.waka", "cn.farwalker.ravv"})
public class RestApplication extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer{
	private static final Logger log = LoggerFactory.getLogger(RestApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
    }

	@Override
	public void customize(ConfigurableEmbeddedServletContainer e) {
		//final int port = 9100;
		//final String context ="/ravv";
		//e.setPort(port); 
		//e.setContextPath(context);
		log.info("上下文:" + e.toString());
	}
    
}
