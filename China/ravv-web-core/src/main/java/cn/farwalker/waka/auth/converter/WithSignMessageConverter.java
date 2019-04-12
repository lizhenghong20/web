package cn.farwalker.waka.auth.converter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cn.farwalker.waka.core.HttpKit;
import cn.farwalker.waka.core.WakaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;

import cn.farwalker.waka.auth.properties.JwtProperties;
import cn.farwalker.waka.auth.security.DataSecurityAction;
import cn.farwalker.waka.auth.util.JwtTokenUtil;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.util.Tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

/**
 * 带签名的http信息转化器
 *
 * @author Jason Chen
 * @date 2017-08-25 15:42
 */
public class WithSignMessageConverter extends FastJsonHttpMessageConverter {

    private static final Logger log = LoggerFactory.getLogger(WithSignMessageConverter.class);
    
    @Autowired
    JwtProperties jwtProperties;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    DataSecurityAction dataSecurityAction;

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        InputStream in = inputMessage.getBody();
        Object o = JSON.parseObject(in, super.getFastJsonConfig().getCharset(), BaseTransferEntity.class, super.getFastJsonConfig().getFeatures());

        //先转化成原始的对象
        BaseTransferEntity baseTransferEntity = (BaseTransferEntity) o;

        String tokenHeader = HttpKit.getRequest().getHeader(jwtProperties.getHeader());
        if(Tools.string.isEmpty(tokenHeader) && tokenHeader.length() <= 7) {
        	throw new WakaException(RavvExceptionEnum.TOKEN_VERIFICATION_FAILED);
        }
        //校验签名
        String token = tokenHeader.substring(7);
        String md5KeyFromToken = jwtTokenUtil.getMd5KeyFromToken(token);

        String object = baseTransferEntity.getObject();
        String json = dataSecurityAction.unlock(object);
        String encrypt = Tools.md5.encrypt(object + md5KeyFromToken);

        if (encrypt.equals(baseTransferEntity.getSign())) {
            //System.out.println("签名校验成功!");
        	log.debug("签名校验成功!");
        } else {
        	log.debug("签名校验失败,数据被改动过!");
            throw new WakaException(RavvExceptionEnum.SIGN_VERIFICATION_FAILED);
        }
        //校验签名后再转化成应该的对象
        Object rs = convertBean(type, json);
        //Object rs = JSON.parseObject(json, type);//对自定义枚举兼容不好
        return rs;
    }
    
    private Object convertBean(Type type,String json){
    	Object bean = null ;
    	Class<?> beanClazz = null;
    	if(type instanceof Class<?>){
    		beanClazz =(Class<?>)type;
    	}    		
    	else if (type instanceof ParameterizedType) {//这里转换的都是参对象，所以都是ParameterizedType
    		ParameterizedType pars =(ParameterizedType)type;
    		Class<?> raw =(Class<?>)pars.getRawType();
    		if(Collection.class.isAssignableFrom(raw)){//Collection参数
    			bean =convertList(pars, json);
    		}
    		else{//bean参数
    			beanClazz =  (Class<?>)raw;
    		}
		}
    	else{
    		throw new WakaException("未实现的类型" + type);
    	}
    	
    	
    	if(beanClazz!=null){
    		Map<String, Object> map = Tools.json.toMap(json);
			bean = Tools.bean.mapToObject(map, beanClazz);
		    //Object rs = JSON.parseObject(json, type);//对自定义枚举兼容不好
    	}
		
		return bean;
    }
    /**
     * 取得parType的泛型
     * @param
     * @return
    private Type[] getActualTypeArguments(final ParameterizedType parType) {
        final Type[] genericTypes = parType.getActualTypeArguments();// method.getGenericParameterTypes();
        int len =genericTypes.length; 
        if (len == 0) {
            return new Type[0];
        }
        List<Type> types = new ArrayList<>(len);
        for (final Type type : genericTypes) {
            if (type instanceof ParameterizedType) {
                final Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                types.addAll(Arrays.asList(actualTypeArguments));
            }
        }
        
        len = types.size();
        Type[] rs = new Type[len];
        if(len>0){
        	types.toArray(rs);
        }
        return rs;
    }
     */
    private List<?> convertList(ParameterizedType pars,String json)  {
		if (Tools.string.isEmpty(json) || json.equals("[]")) {
			return Collections.emptyList();
		}
		/*Type[] generics = getActualTypeArguments(pars);
		if(generics.length>0){//Collection参数大部分情况都只有一个泛型
			Class<?> clz =  (Class)generics[0];
			bean =Tools.json.toList(json,clz);
		}
		else{
			bean =Tools.json.toList(json,Map.class);
		}*/
		
		String beanValue= json.trim();
		int len = beanValue.length(); 
		if (beanValue.charAt(0)=='['  && beanValue.charAt(len-1) ==']' ) {
			Class<?> clazz ;//=(Class<?>)pars.getActualTypeArguments()[0];
			Type[] generics =pars.getActualTypeArguments();// getActualTypeArguments(pars);
			if(generics.length>0){//Collection参数大部分情况都只有一个泛型
				clazz =(Class<?>) generics[0];
			}
			else{
				clazz = Map.class;
			}
			//Object rs = Tools.json.toList(beanValue, clazz);//对枚举兼容不好
			@SuppressWarnings("rawtypes")
			List arys = Tools.json.toObject(beanValue,List.class);
			List<Object> result = new ArrayList<>(arys.size());
			for(Object m : arys){
				Object e ;
				if(m instanceof Map) {
					e = Tools.bean.mapToObject((Map<?, ?>)m, clazz);
				}
				else {
					e = Tools.bean.convert(clazz, m);
				}
				result.add(e);
			}
			return result;
		}
		else {
			String msg = "collectin参数格式不正确:"+json;
			logger.warn(msg);
			return Arrays.asList(beanValue.split(","));
		}  
	}
}
