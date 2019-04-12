package cn.farwalker.ravv.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.farwalker.ravv.rest.filter.AuthFilter;
import cn.farwalker.waka.config.properties.WakaProperties;

@Configuration
public class FilterConfig {

    @Bean
    @ConditionalOnProperty(prefix = WakaProperties.PREFIX, name = "auth-open", havingValue = "true", matchIfMissing = true)
    public AuthFilter jwtAuthenticationTokenFilter() {
        return new AuthFilter();
    }

}