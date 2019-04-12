package cn.farwalker.waka.util;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.farwalker.waka.orm.EnumManager;
import cn.farwalker.waka.orm.EnumManager.IEnumJson;

/**
 * 复制反射工具类
 *
 * @author Administrator
 */
public class BeanUtil {

    private class RelationMethod {
    	private RelationMethod(Method s,Method t){
    		source =s;
    		target =t;
    	}
    	
        /** 来源对像的get方法与目标对象的set方法对应关系 */
        Method source, target;
    }

    private final Map<String, List<RelationMethod>> cacheRelations;
    public static final BeanUtil util = new BeanUtil(); 
	
    /** 因为类必须为public，所以只能把构造函数给这样控制 */
	private BeanUtil() {
		cacheRelations = new HashMap<>();
        cacheClazzMethods2 = new HashMap<>();
        //cacheClazzFieldNames = new HashMap<>();
    }

    /**
     * map拷贝为对象
     *
     * @param map  数据
     * @param bean 对象
     * @return 对象
     */
    public <T> T mapToObject(Map<?, ?> map, T bean) {
        List<String> fds = getFields(bean.getClass());

        for (Map.Entry<?, ?> e : map.entrySet()) {
            Object k = e.getKey(), val = e.getValue();
            String fn;
            if (k != null && val != null &&
                (fn = containIgnoreCase(fds, k.toString()))!=null) { 
                Tools.bean.setPropertis(bean, fn, val); 
            }
        }
        return bean;
    }

    /**
     * map拷贝为对象
     *
     * @param map 数据
     * @param cls 需要转换的对象的类型
     * @return 对象
     */
    public <T> T mapToObject(Map<?, ?> map, Class<T> cls) {
        try {
            T bean = cls.newInstance();
            return this.mapToObject(map, bean);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new YMException(e.getMessage(), e);
        }
    }

    /* 不区分大小写的查找是否存在list中 */
    private String containIgnoreCase(List<String> fds, String fieldName) {
        String rs = null;
        for (String f : fds) {
            if (f.equalsIgnoreCase(fieldName)) {
                rs = f;
                break;
            }
        }
        return rs;
    }

    /**
     * 把source的所有属性值 复制给 target对象. 规则:复制所有属性值,包含来源对象的属性为null也复制 <br>
     * set方法只支持一个参数
     *
     * @param source 来源对象
     * @param target 目标对象
     * @return target 返回目标对象
     */
    public <T> T cloneBean(Object source, T target) {
        return copyProperties(source, target, true);
    }

    /**
     * 把source的不为null的属性值复制比target对象.<br>
     * 规则:如果source的属性值为null则不复制,如果想整个复制,可以使用cloneBean方法 <br>
     * 2.两个对象必须是公共对象(public 访问域) <br>
     * 3.set方法只支持一个参数,get、set方法的前缀必须是小写的get、set
     *
     * @param source 来源对象
     * @param target 目标对象
     * @return target 返回目标对象
     */
    public <T> T copyProperties(Object source, T target) {
        return copyProperties(source, target, false);
    }

    /**
     * 以source为标准,同步这两个对象<br>
     * 实际就是分别执行copyProperties(source, target)及copyProperties(target,source)
     *
     * @param source
     *            来源对象
     * @param target
     *            目标对象
     *
     *            public void synchronization(Object source, Object target){
     *            copyProperties(source, target,false);
     *            copyProperties(target,source,false); }
     */

    /**
     * 把source的不为null的属性值复制比target对象.<br>
     * 规则:1.如果source的属性值为null则不复制,如果想整个复制,可以使用cloneBean方法 <br>
     * 2.两个对象必须是公共对象(public 访问域) <br>
     * 3.set方法只支持一个参数,get、set方法的前缀必须是小写的get、set
     *
     * @param source 来源对象
     * @param target 目标对象
     * @return target 返回目标对象
     */
    protected <T> T copyProperties(Object source, T target, boolean all) {
    	RelationMethod method = null ; 
        try {
            // 两个对象共有的方法,0-来源对象的方法,1 = 对于目标对象的方法
            List<RelationMethod> methods = getRelationMethods(source.getClass(), target.getClass());

            Object[] tmp = new Object[]{};
            for (RelationMethod rm : methods) {
                // 从来源对象get出来
            	method = rm;
                Object val = rm.source.invoke(source, tmp);
                if (!all && val == null) {
                    continue;// 为null
                }
                
                /****** set到目标对象中去 *************/
                Object[] args = new Object[]{val};// 调用目标方法时的参数值
                rm.target.invoke(target, args);
            }
            return target;
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) { 
        	String k = getMethodKey(source.getClass(), target.getClass());
            String err = k + "复制属性时参数出错," + copyPropertiesError(method.source, method.target);
            throw new YMException(err,e);
        }
    }
    /**
     * 取值
     *
     * @param bean
     * @param fieldName 字段名,不能包含get/set
     * @return
     */
    public Object getPropertis(Object bean, String fieldName) {
        if (bean == null || fieldName == null || fieldName.trim().length() == 0){
            throw new YMException("bean或方法名为空,赋值不成功!");
        }

        Method method = getClassMethod(bean.getClass(),"get" + fieldName.trim());  
        if (method == null)
            throw new YMException(bean.getClass().getName() + "不存在方法get" + fieldName + ",或者方法的参数不为0");

        Object[] args = {};
        try {
            Object rs = method.invoke(bean, args);
            return rs;
        } catch (Exception e) {
            throw new YMException(bean.getClass().getName() + ".get" + fieldName + "取值出错," + e.getMessage());
        }
    }
    
    /**
     * 赋值
     *
     * @param bean
     * @param fieldName 字段名,不能包含get/set
     * @param value
     */
    public void setPropertis(Object bean, String fieldName, Object value) {
        if (bean == null || fieldName == null || fieldName.trim().length() == 0){
            return;
        }
        Method method = getClassMethod(bean.getClass(),"set"+fieldName.trim());  
        if (method == null) {
            throw new YMException(bean.getClass().getName() + "不存在方法set" + fieldName + ",或者方法的参数不为1");
        }
        try {
            Object args = null;
            if(value != null){
                Class[] clz = method.getParameterTypes();
                Type[] actualArgumentTypes = getActualTypeArguments(method);
    
                ValueConvert vc = ValueConvert.getInstance();
                args = vc.convert(clz[0], value, actualArgumentTypes);    
            }
        
            method.invoke(bean, args);
        }
        catch (Exception e) {
            String msg = bean.getClass().getName() + "." + (method != null ? method.getName() : fieldName);
            throw new YMException(msg + "=[" + value + "]赋值出错," + e.getMessage());
        }
    }
    /**缓存get及set前缀的方法*/
    private final Map<String, List<Method>> cacheClazzMethods2;
    
    
    /**
     * 取类的方法,{@link #getCacheMethods(Class, int)}
     * @param clazz
     * @param methodName 方法名,必须包含小写的get、set
     * @return
     */
    public Method getClassMethod(Class<?> clazz,String methodName){
    	List<Method> methods = getClassMethods(clazz, null,false);
    	Method rs = null;
    	for(Method m : methods){
    		String name = m.getName();
    		if(name.equalsIgnoreCase(methodName)){
    			rs = m;
    			break;
    		}
    	}
    	return rs;
    }
    /**取类的方法：已排除静态方法，只取get/set方法，并且方法名大于3位*/
    public List<Method> getClassMethods(Class<?> clazz){
    	return getClassMethods(clazz, null);
    }
    /**
     * 取类的方法：已排除静态方法，只取get/set方法，并且方法名大于3位
     * （对外使用的是副本）
     * @param clazz
     * @param px null:全部,或者get、set方法
     * @return
     */
    public List<Method> getClassMethods(Class<?> clazz,String px){
    	return getClassMethods(clazz, px, true);
    }
    /**
     * 对外使用的是副本，内部使用是缓存对象，提高效率
     * @param clazz
     * @param px
     * @param copy
     * @return
     */
    private List<Method> getClassMethods(Class<?> clazz,String px,boolean copy){
    	String className = clazz.getName();
    	List<Method> methods = cacheClazzMethods2.get(className);
    	if(methods == null){
    		Method[] mds = clazz.getMethods();
    		methods = new ArrayList<>(mds.length);
    		cacheClazzMethods2.put(className, methods);
    		
            for (int i = 0; i < mds.length; i++) {
            	Method m = mds[i];
                String name = m.getName();
                if (name.length()<=3 || name.equals("getClass") || Modifier.isStatic(m.getModifiers()) ) {
                	//是否静态方法(静态方法不需要，导致dao查询查多了 2018-6-9)
                    continue;
                } 
                
                if ((name.startsWith("get") && m.getParameterCount() ==0) || 
                	(name.startsWith("set") && m.getParameterCount() ==1)) {
                    methods.add(mds[i]);
                }
            }
    	}
    	
    	if(px==null || px.trim().length()==0){
    		if(copy){
    			return new ArrayList<>(methods);
    		}
    		else{
    			return methods;
    		}
    	}
    	
		px = px.trim();
		px = (px.equals("get") ? "get":"set");//保证get、set
    	List<Method> rs = new ArrayList<>(methods.size()/2);
		for(Method m : methods){
			if(m.getName().startsWith(px)){
				rs.add(m);
			}
		}
		return rs;
    }
    private Type[] getActualTypeArguments(final Method method) {
        final Type[] genericTypes = method.getGenericParameterTypes();
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
    public Class<?> getGenericSinge(Method m,int returnOrParamType){
    	List<Class<?>> rs = getGenericClass(m, returnOrParamType);
    	return (rs.size()>0 ? rs.get(0) : null);
    	
    }
    /**
	 * 取得方法的返回值或者第一参数的泛型
	 * @param m
	 * @param returnOrParamType 1-返回值的泛型 ，2 -第一参数的泛型
	 * @return
	 */
	public List<Class<?>> getGenericClass(Method m,int returnOrParamType){
		Type type = null;
		if(returnOrParamType ==1){
			type = m.getGenericReturnType() ;
		}
		else{
			Type[] ts = m.getGenericParameterTypes();
			if(ts.length ==1){
				type = ts[0];
			}
		}
		return getGenericClass(type);
	}
	/**
	 * 取得数组的泛型类型
	 * @param clazz
	 * @return
	 */
	public List<Class<?>> getGenericClass(Type type){
		if(type ==null){
			throw new YMException("genericType不能为空");
		}
		else if(type instanceof ParameterizedType){
			Type[] args = ((ParameterizedType) type).getActualTypeArguments();
			List<Class<?>> rs = new ArrayList<>(args.length);
			
			for(Type arg : args){
				Class<?> clazz = null;
				if (arg instanceof Class) {
					clazz = (Class) arg;
				}
				else if (arg instanceof ParameterizedType) {
					final ParameterizedType parameterizedType = (ParameterizedType)arg;
					final Type rawType = parameterizedType.getRawType();
					if (rawType instanceof Class) {
						clazz = (Class) rawType;
					}
				}
				
				if(clazz==null){
					throw new YMException("不能识别泛型");
				}
				else{
					rs.add(clazz);
				}
			}
			return rs;
		}
		else{
			throw new YMException("genericType必须是ParameterizedType类型");
		}
	}

    private String copyPropertiesError(Method srcMethod, Method tarMethod) {
        String result = "";
        if (srcMethod != null){
            result = "来源方法:" + srcMethod.getName();
        }
        if (tarMethod != null) {
            if (result.length() == 0){
                result = ",";
            }
            result += "目标方法:" + tarMethod.getName();
        }
        return result;
    }

    private String getMethodKey(Class<?> source, Class<?> target) {
    	//String sn = source.getName(),tn = target.getName();
    	//int ix1 = sn.lastIndexOf('.'),ix2 = tn.lastIndexOf('.');
    	int sp =  source.getName().hashCode();
    	int tp = target.getName().hashCode();//(ix2 >0 ? tn.substring(0, ix2).hashCode() : 0);
    	String sn = source.getSimpleName(),tn = target.getSimpleName();
    	//String sn1 = sn.substring(ix1+1),tn1 = tn.substring(ix2+1);
        return sn + sp + "-" + tn + tp;
    }

    /**
     * 取来源对象的get方法对应目标对象的set方法
     *
     * @param source 来源对象
     * @param target 目标对象
     * @return 只要来源对象的get方法,和对应目标对象的set方法
     */
    private List<RelationMethod> getRelationMethods(Class<?> source, Class<?> target) {
        String key = getMethodKey(source, target);
        List<RelationMethod> result = cacheRelations.get(key);
        if (result != null){
            return result;
        }
        
        List<Method> sourceMethods = getClassMethods(source,"get",false);
        result = new ArrayList<>(sourceMethods.size());
        
        for(Method m : sourceMethods){
        	String name = m.getName().substring(3);
        	Method t = getClassMethod(target,"set" + name);
        	if(t!=null){
        		Class<?> type = m.getReturnType(); // 返回类型
        		Class<?>[] tarpar =t.getParameterTypes();
        		if(type.isAssignableFrom(tarpar[0])) {// 返回类型属于参数的子类时参数兼容
        			RelationMethod rm = new RelationMethod(m,t);
        			result.add(rm);
        		}
        	}
        }
         
        cacheRelations.put(key, result);
        return result;
    }

    public <T> T convert(Class<T> returnType, Object value) {
        return (T) ValueConvert.getInstance().convert(returnType, value);
    }

    /**
     * 取类的方法字段,去除了前缀get及set,并且第一个字符转为小写
     * @param clazz
     */
    public List<String> getFields(Class<?> clazz) {
        return getFields(clazz, null);
    }
    public List<String> getFields(Class<?> clazz, String px) {
    	List<Method> methods = getClassMethods(clazz, px,false);
    	List<String> rs = new ArrayList<>(methods.size());
    	for(Method m : methods){
    		String name = m.getName().substring(3);
    		String fn = Character.toLowerCase(name.charAt(0)) + name.substring(1);
    		if(rs.indexOf(fn)==-1){
    			rs.add(fn);
    		}
    	}
    	return rs;
    } 
}

class ValueConvert {

    private static ValueConvert vc;

    public static ValueConvert getInstance() {
        if (vc == null) {
            vc = new ValueConvert();
        }
        return vc;
    }

    /**
     * 判断类型
     *
     * @return
     */
    @SuppressWarnings({"rawtypes" })
	private Object convertValue(Class<?> clazz, Object vals) {
        if (vals == null) {
            return null;
        }
        Class<?> valclz = vals.getClass();
        if (clazz == valclz || clazz.isAssignableFrom(valclz)) {
            // valclz 是否从clazz派生 2013-4-4
            return vals;
        }
        // ////////////////////////////////////

        if (clazz.isArray()) {
            clazz = (Class<?>) Array.get(clazz, 0);// 取第一个元素
        }
        if (valclz.isArray()) {
            vals = Array.get(vals, 0);// 取第一个元素
        }

        Object result = null;
        if (clazz == vals.getClass()) {
            result = vals;
        } else if (clazz == String.class) {
            result = vals.toString();
        } else if (clazz == int.class || clazz == Integer.class) {
            result = convertNumber(vals, 1);
        } else if (clazz == long.class || clazz == Long.class) {
            result = convertNumber(vals, 2);
        } else if (clazz == float.class || clazz == Float.class) {
            result = convertNumber(vals, 3);
        } else if (clazz == double.class || clazz == Double.class) {
            result = convertNumber(vals, 4);
        } else if (clazz == BigDecimal.class) {
            result = convertNumber(vals, 5);
        } else if (clazz == boolean.class || clazz == Boolean.class) {
            if (vals instanceof Boolean) {
                result = vals;
            }
            else if(vals instanceof Number){
                int n = ((Number)vals).intValue();
                result = Boolean.valueOf(n==1);
            }
            else {
            	String s = vals.toString();//
            	result = Boolean.valueOf( "true".equalsIgnoreCase(s) || "1".equals(s) || "yes".equalsIgnoreCase(s));
            }
        } else if (clazz == java.util.Date.class) {
            if (vals instanceof java.util.Date) {
                result = vals;
            } else {
                Calendar c = parseDate(vals.toString());
                if (c != null) {
                    result = c.getTime();
                }
            }
        } else if (clazz == java.sql.Date.class) {
            if (vals instanceof java.sql.Date) {
                result = vals;
            } else if (vals instanceof java.util.Date) {
                java.util.Date d = (java.util.Date) vals;
                result = new java.sql.Date(d.getTime());
            } else {
                Calendar c = parseDate(vals.toString());
                if (c != null) {
                    result = new java.sql.Date(c.getTimeInMillis());
                }
            }
        } else if (clazz == java.sql.Timestamp.class) {
            if (vals instanceof java.sql.Timestamp) {
                result = vals;
            } else if (vals instanceof java.util.Date) {
                java.util.Date d = (java.util.Date) vals;
                result = new java.sql.Timestamp(d.getTime());
            } else {
                Calendar c = parseDate(vals.toString());
                if (c != null) {
                    result = new java.sql.Timestamp(c.getTimeInMillis());
                }
            }
        }
        else if(clazz.isEnum()){
        	result = toEnum(clazz, vals);
        }
        else {
            try {
                if (vals instanceof Map) {
                    result = Tools.bean.mapToObject((Map<?, ?>) vals, clazz);
                }
            } catch (Exception e) {
                throw new YMException("不能转换类型:" + clazz.getName());
            }
        }

        return result;
    }
    /**
     * 接受3种格式：整型，字符，json: {"key": 1 }
     * 根据{@link EnumManager#get(Class, String)}方法的处理，所以改用key
     * @param clz
     * @param vals
     * @return
     */
    private IEnumJson toEnum(Class<?> clz ,Object vals){ 
    	IEnumJson result = null; 
    	if(vals instanceof Number){
    		Number n = (Number)vals;
    		Integer o = (vals instanceof Integer ? (Integer)n: Integer.valueOf(n.intValue()));
    		result = (IEnumJson)EnumManager.get(clz, o);
    	}
    	else if(vals instanceof Map){
    	    Map mp = (Map)vals;
    	    Object v = mp.get("key");
    	    if(v == null){
    	        v = mp.get("value");//兼容以前的处理，建议以后统一使用key   
    	    }
    		result = (v == null ? null : toEnum(clz, v));
    	}
    	else{
        	String enumValue = vals.toString();
        	if(Tools.string.isNotEmpty(enumValue)){
        	    result =(IEnumJson) EnumManager.get(clz, enumValue);
        	}
    	}
    	if(result == null){
    		throw new YMException(clz.getName() +  "不存在枚举值:" + vals);
    	}
    	return result;
    }
    private int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -99;
        }
    }

    /**
     * 把字符串转换日期
     *
     * @param date 字符串,支持的格式为:yyyy/MM/dd 或 yyyy/MM/dd hh:mm:ss
     * @return 如果格式不符, 则返回null
     */
    public Calendar parseDate(String date) {
        if (Tools.string.isEmpty(date)) {
            return null;
        }

        // 字符格式 yyyy/MM/dd 或 yyyy/MM/dd hh:mm:ss
        boolean format = false;
        int year = 1970, month = 0, day = 1, hrs = 0, min = 0, sec = 0;

        date = date.trim().replace('.', '-').replace('/', '-');
        int i = date.indexOf(' ');
        String time = null;
        if (i > 0) {
            time = date.substring(i + 1);
            date = date.substring(0, i);
        } else if (date.indexOf(':') > 0) {
            time = date;
            date = null;
        }

        // 日期
        if (date != null) {
            String[] d = date.split("-");
            if (format = (d.length == 3)) {
                int y = parseInt(d[0]);
                if (y != -99) {
                    year = y;
                } else {
                    format = false;
                }

                if (format) {
                    int m = parseInt(d[1]);
                    if (m != -99)
                        month = m;
                    else
                        format = false;
                }
                if (format) {
                    int dx = parseInt(d[2]);
                    if (dx != -99)
                        day = dx;
                    else
                        format = false;
                }
            }
        }

        // 时间
        if (time != null) {
            String[] t = time.split(":");
            if (t.length == 3) {
                format = true;
                int x = parseInt(t[0]);
                if (x != -99) {
                    hrs = x;
                } else {
                    format = false;
                }
                if (format) {
                    int dx = parseInt(t[1]);
                    if (dx != -99)
                        min = dx;
                    else
                        format = false;
                }

                if (format) {
                    String s = t[2];
                    int idx = s.indexOf('-');
                    if (idx > 0) {
                        s = s.substring(0, idx);
                    }
                    x = parseInt(s);
                    if (x != -99) {
                        sec = Integer.parseInt(s);
                    } else {
                        format = false;
                    }
                }
            }
        }

        if (format) {
            Calendar gdate = Calendar.getInstance();
            gdate.set(year, month - 1, day, hrs, min, sec);
            gdate.set(Calendar.MILLISECOND, 0);
            return gdate;
        } else {
            return null;
        }
    }

    private Number convertNumber(Object v, int type) {
        if (v == null) {
            return null;
        }
        Number rs = null;
        switch (type) {
            case 1:// int
                if (v instanceof Integer) {
                    rs = (Integer) v;
                } else if (v instanceof Number) {
                    rs = Integer.valueOf(((Number) v).intValue());
                } else {
                    String s = v.toString();
                    if (Tools.number.isInteger(s)) {
                        rs = Integer.valueOf(s);
                    }
                }
                break;

            case 2:// long
                if (v instanceof Long) {
                    rs = (Long) v;
                } else if (v instanceof Number) {
                    rs = Long.valueOf(((Number) v).longValue());
                } else {
                    String s = v.toString();
                    rs = Long.valueOf(s);
                }
                break;
            case 3:// float
                if (v instanceof Float) {
                    rs = (Float) v;
                } else if (v instanceof Number) {
                    rs = Float.valueOf(((Number) v).floatValue());
                } else {
                    String s = v.toString();
                    rs = Float.valueOf(s);
                }
                break;
            case 4:// double
                if (v instanceof Double) {
                    rs = (Double) v;
                } else if (v instanceof Number) {
                    rs = Double.valueOf(((Number) v).doubleValue());
                } else {
                    String s = v.toString();
                    rs = Double.valueOf(s);
                }
                break;
            case 5:// BigDecimal
                if (v instanceof BigDecimal) {
                    rs = (BigDecimal) v;
                } else if (v instanceof Number) {
                    rs = BigDecimal.valueOf(((Number) v).doubleValue());
                } else {
                    double d = Double.parseDouble(v.toString());
                    rs = BigDecimal.valueOf(d);
                }
        }
        return rs;
    }

    /**
     * 自动赋值时,进行类型转换:把vals(String)转换为clazz类型
     *
     * @param clazz class
     * @param value 值
     * @param types Collection<T> 中的T 的实际类型
     * @return 对象
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Object convert(Class clazz, Object value, final Type... types) {
        Object result;
        Class<?> valclz = value.getClass();
        if ((types==null || types.length ==0) && (clazz == valclz || clazz.isAssignableFrom(valclz))) {
            // 因为还有转换泛型，使用不能简单判断
        	result = value;
        }
        else if (clazz.isArray() && valclz.isArray()) {
            result = convertArrayBaseType(clazz, value); // 基础类型数组
            if (result == null) {
                result = convertArray(clazz, value);// 扩展类型
            }
        } else if (Collection.class.isAssignableFrom(clazz)) { // Collection
            result = convertCollection(clazz, value, types);
        } else {
            result = convertValue(clazz, value);
        }
        return result;
    }

    private Object convertCollection(final Class clazz, final Object value, final Type... types) {
        if (types == null || types.length == 0) {
            return null;
        }
        if (value instanceof Collection) {
            final Collection col = (Collection) value;

            final Collection<Object> collection = newCollection(clazz, col.size());
            if (collection == null) {
                return null;
            }

            for (final Object obj : col) {
                if (types[0] instanceof Class) {
                    final Object result = convertValue((Class) types[0], obj);
                    collection.add(result);
                } else if (types[0] instanceof ParameterizedType) {
                    final ParameterizedType parameterizedType = (ParameterizedType) types[0];
                    final Type rawType = parameterizedType.getRawType();
                    if (rawType instanceof Class) {
                        final Object result = convertValue((Class) rawType, obj);
                        collection.add(result);
                    }
                }
            }
            return collection;
        }
        return null;
    }

    private Collection<Object> newCollection(final Class clazz, final int size) {
        if (List.class.isAssignableFrom(clazz)) {
            return new ArrayList<>(size);
        }
        if (Set.class.isAssignableFrom(clazz)) {
            return new LinkedHashSet<>(size);
        }
        return null;
    }

    /**
     * 扩展类型数组
     *
     * @param clazz class
     * @param value 值
     * @return 对象
     */
    private Object convertArray(Class clazz, Object value) {
        Class componentType = clazz.getComponentType();
        Object result = Array.newInstance(componentType, Array.getLength(value));
        for (int i = 0, icount = Array.getLength(value); i < icount; i++) {
            Object o = convertValue(componentType, Array.get(value, i));
            Array.set(result, i, o);
        }
        return result;
    }

    /**
     * 基础类型数组
     *
     * @param clazz class
     * @param value 值
     * @return 对象
     */
    private Object convertArrayBaseType(Class clazz, Object value) {
        int size = Array.getLength(value);
        Object result = null;
        if (clazz == int[].class) {
            int[] r = new int[size];
            for (int x = 0; x < size; x++) {
                Object o = Array.get(value, x);
                r[x] = (o == null ? 0 : Integer.parseInt(o.toString()));
            }
            result = r;
        } else if (clazz == long[].class) {
            long[] r = new long[size];
            for (int x = 0; x < size; x++) {
                Object o = Array.get(value, x);
                r[x] = (o == null ? 0 : Long.parseLong(o.toString()));
            }
            result = r;
        } else if (clazz == float[].class) {
            float[] r = new float[size];
            for (int x = 0; x < size; x++) {
                Object o = Array.get(value, x);
                r[x] = (o == null ? 0 : Float.parseFloat(o.toString()));
            }
            result = r;
        } else if (clazz == double[].class) {
            double[] r = new double[size];
            for (int x = 0; x < size; x++) {
                Object o = Array.get(value, x);
                r[x] = (o == null ? 0 : Double.parseDouble(o.toString()));
            }
            result = r;
        } else if (clazz == boolean[].class) {
            boolean[] r = new boolean[size];
            for (int x = 0; x < size; x++) {
                Object o = Array.get(value, x);
                r[x] = (o == null ? false : Boolean.parseBoolean(o.toString()));
            }
            result = r;
        }

        return result;
    }
}
