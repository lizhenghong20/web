package cn.farwalker.waka.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration /*测试有哪些converter的时候打开*/
public class CustomMVCConfiguration extends WebMvcConfigurerAdapter{//1
	private static final Logger log = LoggerFactory.getLogger(CustomMVCConfiguration.class);
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.clear(); //实验发现，不起作用，可能是boot的bug，或者官方不让修改？
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> messageConverter : converters) {
            System.out.println(messageConverter); //2
        }
    }
	/**请求参数拦截*/
	@Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> args) {
        super.addArgumentResolvers(args);
        log.debug("创建参数拦截器...");
        args.add(new ParamObjectMethodArgumentResolver());
        args.add(new ParamListMethodArgumentResolver());
        //args.add(new RequestParamArgumentResolver());
    }
}