package cn.farwalker.waka.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import cn.farwalker.waka.auth.security.DataSecurityAction;
import cn.farwalker.waka.auth.security.impl.Base64SecurityAction;
import cn.farwalker.waka.config.properties.WakaProperties;

/**
 * web配置
 *
 * @author Jason Chen
 * @date 2017-08-23 15:48
 */
@Configuration
public class WebConfig {

    @Bean
    public DataSecurityAction dataSecurityAction() {
        return new Base64SecurityAction();
    }
}
