package cn.farwalker.waka.config;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;

import cn.farwalker.waka.config.ParamObjectMethodArgumentResolver.ParamsNamedValue;
import cn.farwalker.waka.util.Tools;



/**
 * 参数解析器(HandlerMethodArgumentResolver)<br/>
 * 拦截每个controller的方法，逐个参数拦截一次，判断参数的类型是否需要本类处理<br/>
 * 本类处理Collection作为参数的controller方法<br/>
 * 处理过程：1、判断类型是否要处理，2、接收request的原生数据，3、转换成controller方法需要的类型<br/>
 * 
 * 注意：加了@RequestParam标签就不会执行本类的<br/>
 * 例如：<br/>
 * public JsonResult<UserBo> dologin(@RequestParam String account,@RequestParam String password)
 * @author juno
 *
 */
public class ParamListMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver   {

	protected Log logger = LogFactory.getLog(this.getClass());

	/**
	 * 判断是否支持某种类型的函数参数,如果返回true<br/>
	 * 则执行createNamedValueInfo(执行一次)、resolveName(以后每次执行)方法 
	 */
	@Override
	public boolean supportsParameter(MethodParameter mp) {
		if(mp.getContainingClass().getName().indexOf(ParamObjectMethodArgumentResolver.OLD_PROJ)==0){
			//为了兼容旧项目
			return false;
		}
		
		Class<?> type = mp.getParameterType();
		boolean rs = Collection.class.isAssignableFrom(type);
		
		return rs;
	}

	@Override
	protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
		HttpServletRequest req = (HttpServletRequest) request.getNativeRequest();
		String[] values = getParameterValues(req, name); 
		if (values == null ) {
			return null;
		}
		else if (values.length != 1) {
			return values;
		}
		else if (Tools.string.isEmpty(values[0]) || values[0].equals("[]")) {
			return null;
		}
		
		String beanValue= values[0].trim();
		int len = beanValue.length(); 
		if (beanValue.charAt(0)=='['  && beanValue.charAt(len-1) ==']' ) {
			ParameterizedType pt=(ParameterizedType) parameter.getGenericParameterType();
			Type arg =pt.getActualTypeArguments()[0];

			Class<?> clazz;
			if (arg instanceof ParameterizedType) { 
				clazz = parameter.getGenericParameterType().getClass();
			}
			else{
				clazz= (Class<?>) arg;
			}
			//Object rs = Tools.json.toList(beanValue, clazz);//对枚举兼容不好
			@SuppressWarnings("rawtypes")
			List arys = Tools.json.toObject(beanValue,List.class);
			List<Object> result = new ArrayList<>(arys.size());
			for(Object m : arys){
				Object e = Tools.bean.mapToObject((Map<?, ?>)m, clazz);
				result.add(e);
			}
			return result;
		}
		else {
			String msg = String.format("%s 参数(%s)格式不正确:%s",  req.getRequestURI(),name,beanValue);
			logger.warn(msg);
			return beanValue.split(",");
		}  
	}
	
	
	/**返回参数以逗号分隔的字符(配合前端使用 JSON.stringify(value) 转换Array的js数组的json格式)*/
	private static String[] getParameterValues(HttpServletRequest req, String name) { 
		String[] values = req.getParameterValues(name);
		/*
		 //谭海朝的代码，留以后参考
		String[] values = RequestBodyParser.getParameterValues(req, name);
		 
		if (values == null) {
			values = RequestBodyParser.getParameterValues(req, name.replaceFirst("List$", ""));
			if (values == null) {
				return null;
			}
		}*/
		return values;
	}

	@Override
	protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) { 
		RequestParam ann = parameter.getParameterAnnotation(RequestParam.class);
		return (ann != null ? new ParamsNamedValue(ann) : new ParamsNamedValue());
	}

}
