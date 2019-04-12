package cn.farwalker.ravv.config;

import cn.farwalker.ravv.filter.AuthFilter;
import cn.farwalker.waka.config.properties.WakaProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    @ConditionalOnProperty(prefix = WakaProperties.PREFIX, name = "auth-open", havingValue = "true", matchIfMissing = true)
    public AuthFilter jwtAuthenticationTokenFilter() {
        return new AuthFilter();
    }

}