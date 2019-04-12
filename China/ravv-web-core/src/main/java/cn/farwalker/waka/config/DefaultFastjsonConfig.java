package cn.farwalker.waka.config;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import cn.farwalker.waka.orm.EnumManager;
import cn.farwalker.waka.orm.EnumManager.IEnumJson;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

/**
 * fastjson配置类
 *
 * @author Jason Chen
 * @date 2017-11-23 22:56
 */
@Configuration("defaultFastjsonConfig")
@ConditionalOnClass(com.alibaba.fastjson.JSON.class)
@ConditionalOnMissingBean(FastJsonHttpMessageConverter.class)
@ConditionalOnWebApplication
public class DefaultFastjsonConfig {

    //@Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        converter.setFastJsonConfig(fastjsonConfig());
        converter.setSupportedMediaTypes(getSupportedMediaType());
        return converter;
    }

    /**
     * fastjson的配置
     */
    public FastJsonConfig fastjsonConfig() {
    	//SerializerFeature.DisableCircularReferenceDetect 
    	//禁用FastJson的“循环引用检测”特性。 https://www.cnblogs.com/zjrodger/p/4630237.html
    	
    	/** SerializerFeature.WriteMapNullValue:的作用是如果属性值是null,输出的json也有null
    	 * 例如{name:null,age:10},如果不配置WriteMapNullValue,则输出{age:10}
    	 */ 
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat,
                //SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteEnumUsingToString,
                SerializerFeature.DisableCircularReferenceDetect //禁用FastJson的“循环引用检测”特性。
        );
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        
        ValueFilter valueFilter = new ValueFilter() {
            public Object process(Object bean, String s, Object value) {
                //if (null == value) {
                    //o1 = "";
                    //如果是null，需输出什么(建议不要改变，会影响前端对null的判断)
                //}
                if(value!=null && value instanceof IEnumJson){
                	/*IEnumJson e =  (IEnumJson)value;
                	Map<String,Object> m = new HashMap<>();
                	m.put("key", e.getValue());
                	m.put("label", e.getLabel());*/
                	return EnumManager.toMap((IEnumJson)value);
                }
                else{
                	return value;	
                }
                
            }
        };
        fastJsonConfig.setCharset(Charset.forName("utf-8"));
        fastJsonConfig.setSerializeFilters(valueFilter);

        //解决Long转json精度丢失的问题
        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
        serializeConfig.put(Long.class, ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
        fastJsonConfig.setSerializeConfig(serializeConfig);
        return fastJsonConfig;
    }

    /**
     * 支持的mediaType类型
     */
    public List<MediaType> getSupportedMediaType() {
        ArrayList<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        return mediaTypes;
    }

}
