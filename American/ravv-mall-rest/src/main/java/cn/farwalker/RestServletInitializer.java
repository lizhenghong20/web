package cn.farwalker;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * BlockTime REST Web程序启动类
 *
 * @author Jason Chen
 * @date 2017年9月29日09:00:42
 */
public class RestServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(RestApplication.class);
    }

}
