package cn.farwalker.waka.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
/**
<dependency>
	<groupId>com.fasterxml.jackson.core</groupId>
	<artifactId>jackson-annotations</artifactId>
</dependency>
<dependency>
	<groupId>com.fasterxml.jackson.core</groupId>
	<artifactId>jackson-core</artifactId>
</dependency>
<dependency>
	<groupId>com.fasterxml.jackson.core</groupId>
	<artifactId>jackson-databind</artifactId>
</dependency>
 */
/**
 * json工具类(使用alibaba的fastjson),其他项目不要用引用果json包了
 * com.alibaba.fastjson.JSON;
 *
 * @author Administrator
 */
class JsonUtil_Jackson extends JsonUtil{
    private static final Logger log = LoggerFactory.getLogger(JsonUtil_Jackson.class);
    public static final JsonUtil_Jackson util = new JsonUtil_Jackson(); 
    
    /** 因为类必须为public，所以只能把构造函数给这样控制 */
	private  JsonUtil_Jackson() {

    }  

    @Override
    @SuppressWarnings("rawtypes")
	public Map<String, Object> toMap(final String json) {
        if (Tools.string.isEmpty(json) || "{}".equals(json)) {
            return Collections.emptyMap();//new HashMap<>();
        }
        try {
			ObjectMapper mapper = new ObjectMapper();
			TypeReference tr =new TypeReference<Map>(){};
			Map<String, Object> map = mapper.readValue(json, tr);
			return map;
		} catch (Exception e) {
			throw new YMException("json转换Map错误:",e);
		}
    }
    @Override
    public <T> T toObject(final String json, final Class<T> cls) {
        if (Tools.string.isEmpty(json) || json.equals("{}")) {
            return null;
        }
        
        try {
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
	      //设置JSON时间格式    
	        //SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
	        //cfg.setDateFormat(myDateFormat);
	        
	        T t =   mapper.readValue(json, cls);
	        return t;
	    } catch (Exception e) {
			throw new YMException("json转换Object错误:",e);
		}
    }
    @Override
    public <T> T toJavaObject(final Object jsonObject, final Class<T> cls) {
    	ObjectMapper m = new ObjectMapper(); 
    	m.setInjectableValues(new InjectableValues() {
            
            @Override
            public Object findInjectableValue(Object valueId, DeserializationContext ctxt, BeanProperty forProperty,
                    Object beanInstance) {
                return null;
            }
        });
    	T t = m.convertValue(jsonObject, cls);
    	return t;
    } 
	@Override
	public <T> List<T> toList(final String json, final Class<T> cls) {
		if (Tools.string.isEmpty(json) || json.equals("[]")
				|| json.equals("[{}]") || json.equals("{}")) {
			return null;
		}
		try {
			ObjectMapper mapper = new ObjectMapper(); 
			TypeFactory tf = mapper.getTypeFactory();
			JavaType javaType = tf .constructParametricType(ArrayList.class, cls);
			List<T> rs = mapper.readValue(json, javaType);
			return rs;
		} catch (Exception e) {
			throw new YMException("json转换List错误:", e);
		}
	}

    /** 使用alibaba的fastjson */
    @Override
    public String toJson(final Object o) {
    	if(o == null){
    		return "";
    	}
    	try {
			ObjectMapper m = new ObjectMapper();
			/*m.setFilterProvider(getFilter("objectFilter","userType","userType"));
			m.addMixIn(o.getClass(), ObjectFilterMixIn.class);*/
			String json = m.writeValueAsString(o);
			return json;
		} 
    	catch (JsonProcessingException e) {
			throw new YMException("Object转换Json错误:",e);
		}
    }

    private FilterProvider getFilter(String filterName, String... propertyes) {
        // 过滤不想要的
        FilterProvider filter = new SimpleFilterProvider().addFilter(filterName,
                SimpleBeanPropertyFilter.serializeAllExcept(propertyes));
        // 过滤想要的
        /*
        * FilterProvider filter = new SimpleFilterProvider().addFilter(
        * filterName, SimpleBeanPropertyFilter.filterOutAllExcept(propertyes);
        */
        return filter;
    }
}
/**动态图过滤使用*/
@JsonFilter("objectFilter")
interface ObjectFilterMixIn {
    //objectFilter 要与 m.setFilterProvider(getFilter("objectFilter","userType","userType")); 第一个 参数相同
    //实例:过滤userType字段
    //ObjectMapper.setFilterProvider(getFilter("objectFilter","userType"));
    //ObjectMapper.addMixIn(o.getClass(), ObjectFilterMixIn.class);
}