package cn.farwalker.waka.components.thirdTools;

import cn.farwalker.waka.util.Tools;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

import java.util.HashMap;
import java.util.Map;

/*
pom.xml
<dependency>
  <groupId>com.aliyun</groupId>
  <artifactId>aliyun-java-sdk-core</artifactId>
  <version>4.0.3</version>
</dependency>
*/
public class CommonRpc {
    public static void sendSMS(String phone, String activationCode) {
        DefaultProfile profile = DefaultProfile.getProfile("default", "LTAInoZCsAiaQ1Mp", "1ZIwHG4BG79ZMqYnfdSXsElqVIQxZf");
        IAcsClient client = new DefaultAcsClient(profile);
        Map<String ,String > map = new HashMap<>();
        map.put("code", activationCode);

        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("SignName", "珠海睿孚科技有限公司");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("TemplateCode", "SMS_163465074");
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(map)/*"{\"code\":\"1111\"}"*/);



        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}

