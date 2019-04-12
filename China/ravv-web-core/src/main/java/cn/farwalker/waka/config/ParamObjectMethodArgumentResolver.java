package cn.farwalker.waka.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.log4j.Log4j;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;
import org.springframework.web.multipart.MultipartFile;

import cn.farwalker.waka.util.Tools;
import cn.farwalker.waka.util.YMException;

/**
 * 参数解析器(HandlerMethodArgumentResolver)<br/>
 * 拦截每个controller的方法，逐个参数拦截一次，判断参数的类型是否需要本类处理<br/>
 * 本类处理自定义的类作为参数的controller方法<br/>
 * 处理过程：1、判断类型是否要处理，2、接收request的原生数据，3、转换成controller方法需要的类型<br/>
 * 
 * 注意：加了@RequestParam标签就不会执行本类的<br/>
 * 例如：<br/>
 * public JsonResult<UserBo> dologin(@RequestParam String account,@RequestParam String password)
 * @author juno
 *
 */
@Log4j
public class ParamObjectMethodArgumentResolver extends
		AbstractNamedValueMethodArgumentResolver {
	public static final String OLD_PROJ="cn.farwalker.standard.modular.";
	private static final Map<String,List<Field>> cacheFields = new HashMap<>();
	/** set方法的方法名及参数类型*/
	private static final Map<String,List<ObjectMethod>> cacheMethods = new HashMap<>();
	
	/**set方法的所有参数*/
	private static class ObjectMethod {
		private ObjectMethod(String f, Method m, Class<?> c) {
			field = f;
			method = m;
			clazz = c;
			if (Collection.class.isAssignableFrom(c)) {
				clazz = Tools.bean.getGenericSinge(m,2);
				genericType = 1;
				if(clazz == null){
					throw new YMException("不能识别方法的类型:" + f);
				}
			}
			else if(Map.class.isAssignableFrom(c)){
				// TODO 未实现
				genericType = 2;
				throw new YMException("未实现");
			}
			else {
				clazz = c;
				genericType = 0;
			}
		}
		
		/**字段名*/
		String field;
		/**方法体*/
		Method method;
		/**参数类型,当是泛型时，就是泛型元素的类型*/
		Class<?> clazz;
		
		/** 0:普通类型, 1:Collection类型,2:Map类型 */
		int genericType;
	}
	 
	/** 
	 * @deprecated 暂时没有使用
	 * http://blog.csdn.net/u010187242/article/details/73647670
	 * @author juno
	 *
	 */
	@Target(ElementType.PARAMETER)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface JsonParam {
 
		@AliasFor("name")
		String value() default ""; 
		@AliasFor("value")
		String name() default ""; 
		boolean required() default true; 
		String defaultValue() default ValueConstants.DEFAULT_NONE;
	}
	
	public ParamObjectMethodArgumentResolver() {
		log.debug("create MethodArgumentResolver() ...");
	}

	private static final Map<Class<?>,Integer> set = new HashMap<>();
	static {
		Integer v = Integer.valueOf(1);
		set.put(MultipartFile.class,v);
		set.put(String.class,v);
		set.put(Boolean.class,v);
		set.put(Date.class,v);
		set.put(Integer.class,v);
		set.put(Long.class,v);
		set.put(Float.class,v);
		set.put(Double.class,v);
		set.put(List.class,v);
		set.put(BigDecimal.class, v);
		// set.add(Month.class);
		// set.add(OnlyDate.class);
	}

	/**
	 * 判断是否支持某种类型的函数参数,如果返回true<br/>
	 * 则执行createNamedValueInfo(执行一次)、resolveName(以后每次执行)方法 
	 */
	@Override
	public boolean supportsParameter(MethodParameter mp) { 
		Class<?> type = mp.getParameterType();
		boolean rs ;
		if( type.isArray() || type.isPrimitive() ){//type.isEnum() ||
		    rs = false;//不支持
		}
		else if(mp.getContainingClass().getName().indexOf(OLD_PROJ)==0){
			//为了兼容旧项目
			return false;
		}
		else {
		    Integer supports = set.get(type);//原生对象都不需要处理(只处理自定义的类)
		    rs =(supports == null);//原生对象，不支持
		}
		//cn.farwalker.standard.modular
		StringBuilder sb = new StringBuilder(mp.getContainingClass().getSimpleName());
		sb.append('.').append(mp.getMethod().getName()).append('(').append(type.getSimpleName())
		    .append(")参数是否需要转换:").append(rs);
		log.debug(sb.toString());
		return rs;
	}

	@Override
	protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
		RequestParam ann = parameter.getParameterAnnotation(RequestParam.class);
		return (ann == null ? new ParamsNamedValue(): new ParamsNamedValue(ann));
	}

	/**
	 * {@link ParamObjectMethodArgumentResolver#supportsParameter}  已经过滤了原生类型，所以这里不会有原生类型
	 */
	@Override
	protected Object resolveName(String name, MethodParameter parameter,
			NativeWebRequest request) throws Exception {
		HttpServletRequest req = (HttpServletRequest) request.getNativeRequest();
		Class<?> clazz = parameter.getParameterType();
		
		//已经过滤了原生类型
		Object bean = null ; 
		String beanValue = RequestBodyParser.getParameter(req, name);
		
		if(Tools.string.isEmpty(beanValue)){
		    Method pm =parameter.getMethod(); 
			int pc= pm.getParameterCount() ;
			if(pc>1){ //检查javax.servlet参数
			    Class[] ptype = pm.getParameterTypes();
                for(Class c : ptype){
                    if(c.getName().startsWith("javax.servlet.")){
                        pc--;
                    }
                }
			}
			
			if(pc ==1){//只有一个参数的情况
				bean = resolveFieldName(clazz, req);
			}
			else{
			    String uri = req.getRequestURI();
			    log.error(uri + ":参数个数[" + pc + "]大于1，不能赋值，请检查是否参数过多");
			}
		}
		else if(clazz.isEnum()){
			if(!"{}".equalsIgnoreCase(beanValue.trim())){
				bean = Tools.bean.convert(clazz,beanValue);
			}
		}
		else{
			//bean = Tools.json.toObject(beanValue, clazz); //对枚举兼容不好
		    Map<String, Object> map = Tools.json.toMap(beanValue);
		    bean = Tools.bean.mapToObject(map, clazz);
		}
		return bean;
	}
	
	/** 解析字段值*/
	private Object resolveFieldName(Class<?> clazz,HttpServletRequest request)  { 
		Object bean;
		try {
			bean = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {			
			throw new YMException(clazz.getName() + "创建实例失败:" + e.getMessage());
		}
		
		List<ObjectMethod> methods = listSetMethods(clazz);
		for(ObjectMethod e : methods){
			String value = RequestBodyParser.getParameter(request, e.field);
			try {
				if(value!=null){
					Object rs;
					if(e.genericType ==1){
						rs = null;// ConvertDBUtil.listFromDatabaseJson(value, e.clazz);
					}
					else if(e.genericType==2){
						rs = null;//ConvertDBUtil.mapFromDatabaseJson(value, e.clazz);
					}
					else{
						rs  = Tools.bean.convert(e.clazz, value);
					}
					e.method.invoke(bean, rs); 
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
				throw new YMException(clazz.getName() + "的方法" + e.method.getName() + "(" + value +")赋值失败:" + ex.getMessage());
			}
		}
		return bean;
	}
	
 
	/** 递归所有字段(有缓存)*/
	private static List<Field> listFields(Class<?> clazz) {
		List<Field> list = cacheFields.get(clazz.getName());
		if(list == null){
			list = new ArrayList<Field>();
			cacheFields.put(clazz.getName(), list);
			
			Class<?> currentClazz = clazz;
			while (currentClazz!=null && currentClazz!=Object.class) {
				Field[] fields = currentClazz.getDeclaredFields();
				for (Field field : fields) {
					int  modifiers  = field.getModifiers();
					if(!Modifier.isStatic(modifiers)){
						list.add(field);
					}
				} 
				currentClazz = currentClazz.getSuperclass();
			}
		}
		return list;
	}
	/** set方法的方法名及参数类型 <br/>
	 * 已去掉set前缀，并且第一个字符转为小写
	 */
	private static List<ObjectMethod> listSetMethods(Class<?> clazz) {
		
		List<ObjectMethod> methodTypes = cacheMethods.get(clazz.getName());
		if(methodTypes!=null){
			return methodTypes;
		}
		methodTypes = new ArrayList<>();
		cacheMethods.put(clazz.getName(), methodTypes);
		List<Method> sets = Tools.bean.getClassMethods(clazz, "set");
		for(Method m:sets){
			String name = m.getName();
			String fn = Character.toLowerCase(name.charAt(3)) + name.substring(4);
			Class<?>[] types = m.getParameterTypes();
			ObjectMethod o = new ObjectMethod(fn,m,types[0]);
			methodTypes.add(o);
		}
		return methodTypes;
	}
	static class ParamsNamedValue extends NamedValueInfo{
		public ParamsNamedValue(RequestParam annotation) {
			super(annotation.name(), annotation.required(), annotation.defaultValue());
		}
		public ParamsNamedValue() {
			super("", false, ValueConstants.DEFAULT_NONE);
		}		
	}

}


/**
 * RequestBody参数解析器
 * 
 * @author 谭海潮
 *
 */
@Log4j
class RequestBodyParser {
	private static final String D_ParameterName="requestBody"; 

	// FIXME 未支持下划线
	public static String[] getParameterValues(HttpServletRequest request,
			String name) {
		String[] values = request.getParameterValues(name);
		if (values == null) {
			String value = getParameterForRequestBody(request, name);
			if (value == null) {
				return null;
			}
			return new String[] { value };
		}
		return values;
	}
 

	public static String getParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (value == null) {
			;//value = getParameterForRequestBody(request, name);
		}
		return value;
	}

	/** 
	 * @deprecated 其实没有使用requestBody 参数
	 * @param request
	 * @return
	 */
	private static Map<String, Object> getRequestBody(HttpServletRequest request) {
		@SuppressWarnings("unchecked")
		Map<String, Object> requestBody = (Map<String, Object>) request .getAttribute(D_ParameterName);
		if (requestBody == null) {
			String requestBodyJson = request.getParameter(D_ParameterName); 
			if (Tools.string.isNotEmpty(requestBodyJson)) { 
				requestBody = Tools.json.toMap(requestBodyJson);
				request.setAttribute(D_ParameterName, requestBody);
			}
		}
		return requestBody;
	}

	private static String getParameterForRequestBody(
			HttpServletRequest request, String name) {
		Map<String, Object> requestBody = getRequestBody(request);
		if (requestBody == null) {
			return null;
		}
		Object value = requestBody.get(name);

		if (value == null) {
			return null;
		}
		if (value instanceof String) {
			return (String) value;
		} else if (value instanceof Integer) {
			return Integer.toString((Integer) value);
		} else if (value instanceof Long) {
			return Long.toString((Long) value);
		} else if (value instanceof Float) {
			return Float.toString((Float) value);
		} else if (value instanceof Double) {
			return Double.toString((Double) value);
		} else if (value instanceof Date) {
			return ((Date) value).getTime() + "";
		} else if (value instanceof Boolean) {
			return value + "";
		}

		String json = Tools.json.toJson(value);
		// System.err.println("getParameterForRequestBody name:" + name +
		// " json:" + json);
		return json;
	}
}
