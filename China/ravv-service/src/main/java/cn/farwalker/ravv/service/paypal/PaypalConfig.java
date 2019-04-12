
package cn.farwalker.ravv.service.paypal;

/**
 * Created by asus on 2018/12/11.
 */

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Data
@Configuration
public class PaypalConfig {

//    @Value("${paypal.client.app}")
//    private String clientId;
//    @Value("${paypal.client.secret}")
//    private String clientSecret;
//    @Value("${paypal.mode}")
//    private String mode;


    private final String clientId = "AX-x-YAnir3Wy25ha46CB7Xx8-FiE4rQgraI5hg-C2YB2EMzTmAX4z8iCAHot9LmqKbw2GLnRFmJMGLN";
    private final String clientSecret = "EIIQHJ-PxWEI__RZ7B1qMDTFJpzz87j7ri3Qyab7tNBjh9m1lqN0HaGMV7eFEGj5UkxRee4H1rgNfAir";
    private final String mode = "sandbox";
    private APIContext apiContext;

    /*private final String clientId = "AfFuKVWJ3cax69FfHiZ5F8JHIIYCy1FM4mGSu05VCpxgYGoeOfn4JXKrH37tw9-ymE3GzCh1k6CvbhrC";
    private final String clientSecret = "EPZLq1uTeUcGqMaWOYCa-cdI5GNzpVOEL8hvppi9OHg5kZBfSFF44ILjTV9F_ryssOwLysm5lX5TIt_2";
    private final String mode = "live";
    private APIContext apiContext;*/

//    @Bean
//    public Map<String, String> paypalSdkConfig(){
//        Map<String, String> sdkConfig = new HashMap<>();
//        sdkConfig.put("mode", mode);
//        return sdkConfig;
//    }
//
//    @Bean
//    public OAuthTokenCredential authTokenCredential(){
//        return new OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig());
//    }
//
//    @Bean
//    public APIContext apiContext() throws PayPalRESTException{
//    	//测试
//    	String token = "junit-不能getAccessToken(),443端口连接超时";
//    	token = authTokenCredential().getAccessToken();
//        APIContext apiContext = new APIContext(token);
//        apiContext.setConfigurationMap(paypalSdkConfig());
//        return apiContext;
//    }

}


