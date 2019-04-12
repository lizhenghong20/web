package com.cangwu.frame.orm.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.farwalker.waka.orm.EnumManager;
import cn.farwalker.waka.orm.EnumManager.IEnumJson;
import cn.farwalker.waka.orm.EnumManager.IEnumJsonn;
import cn.farwalker.waka.orm.EnumManager.IEnumJsons;
import cn.farwalker.waka.util.Tools;
import cn.farwalker.waka.util.YMException;

/**数据库字段值与java的类型转换*/
public class ConvertDBUtil {
	/**全局的boolean状态(是:1,否:2)
	 * 已经支持boolean映射到数据库的值
	 * {@link ConvertDBUtil#fromDatabaseValue(Object, java.lang.reflect.Method, Object)}
	 */
	public static final int BooleanTRUE=1,BooleanFALSE=2;
	private static final Object[] args = {};
	private static final Class<?>[] classDate = {Date.class, Timestamp.class,java.sql.Date.class};
	private static final Class<?>[] classPrimitive = {String.class, int.class,Integer.class
		,long.class, Long.class,float.class,boolean.class,Boolean.class, Float.class, double.class,Double.class
		,BigDecimal.class, short.class,Short.class,char.class, Character.class}; 
	
	/**数据库能直接识别的对象*/
	public static boolean isPrimitive(Class<?> clazz){
		if (isPrimitiveType(clazz)) {
            return true;
		}
		else if(isDateType(clazz)){
			return true;
		}
		else{
			return false;
		}
	}
	
	
	private static boolean isPrimitiveType(Class<?> clazz){
		boolean rs = false;
		for(int i =0 ;i < classPrimitive.length;i++){
			if(clazz == classPrimitive[i]){
				rs = true;
				break;
			}
		}
		return rs;
	}
	private static boolean isDateType(Class<?> clazz){
		boolean rs = false;
		for(int i =0 ;i < classDate.length;i++){
			if(clazz == classDate[i]){
				rs = true;
				break;
			}
		}
		if(!rs){
			rs = Date.class.isAssignableFrom(clazz);
		}
		return rs;
	}

	/**
	 * 取原生类型
	 * @param generic 是否包含泛原生:true，泛原生是包含日期类型
	 * @return
	 */
	public static String[] getPrimitiveName(boolean generic) {
		int l = classPrimitive.length ;
		if(generic){
			l += classDate.length;
		}
		String[] rs = new String[l];
		int idx = 0;
		for (int i = 0; i < classPrimitive.length; i++) {
			rs[idx++] = classPrimitive[i].getSimpleName();
		}
		if(generic){
			for (int i = 0; i < classDate.length; i++) {
				rs[idx++] = classDate[i].getName();
			}
		}
		return rs;
	}
	/**
	 * 取bean的字段值转换数据库字段值
	 * @param bean
	 * @param fieldName bean的字段名
	 * @return
	 */
	public static Object toDatabaseValue(Object bean,String fieldName){
		Method m = Tools.bean.getClassMethod(bean.getClass(), "get" + fieldName);
		Object value;
		try {
			value = m.invoke(bean, args);
		} catch (IllegalAccessException  | IllegalArgumentException | InvocationTargetException e) {
			throw new YMException("获取" + bean.getClass().getName() + "方法"  + m.getName() +"出错",e);
		}
		Object r =null;
		if(value!=null){
			r = toDatabaseValue(value);	
		}
		return r; 
	}
	/**
	 * @deprecated clazz应该不会与value类型不一致吧？ 所以暂时不需要这个方法
	 * 根据的java类型，转换为数据库类型的值 <br/>
	 * 数据库能直接识别的类型 不转换<br/>
	 * boolean 会转换为数字 1(true)/2(false) <br/>
	 * 枚举 转换为字符或数字对应的值<br/>
	 * List、Map 转换为json字符
	 * @param clazz 指定的java类型 
	 * @param value 
	 * @return
	 */
	private static Object toDatabaseValue(Class<?> clazz,Object value){
		throw new YMException("clazz应该不会与value类型不一致吧？ 所以暂时不需要这个方法");
	}
	/**
	 * 按值的类型直接转换为数据库字段值
	 */ 
	public static  Object toDatabaseValue(Object value){
		if(value==null){
			return null;
		}
		Class<?> clazz = value.getClass();
		
        
		Object rs ;
        if (clazz == boolean.class || clazz == Boolean.class || value instanceof Boolean) {
        	boolean b = ((Boolean)value).booleanValue();
        	/*if(value instanceof Boolean){
        		b = (Boolean)value;
        	}
        	else{
        		b = Tools.bean.convert(Boolean.class, value);
        	}*/
            rs = Integer.valueOf(b?BooleanTRUE:BooleanFALSE);
        } 
        else if(isPrimitive(clazz)){
			rs = value;
		}
        else if(clazz.isEnum()){
        	if(value instanceof IEnumJson){
        		rs =((IEnumJson)value).getValue();
        	}
        	else{
        		throw new YMException("无法转换为数据库值，不能识别的枚举:" + value.getClass());
        	}
        }
        else { //转换为json对象
            rs = Tools.json.toJson(value);
        }
        return rs;
	}
	
	/**
	 * 数据库字段值 转换为java值
	 * @param bean
	 * @param method set方法对象
	 * @param dbvalue 数据库的值
	 * @return java对象值
	 */
	public static Object fromDatabaseValue(Object bean,Method method,Object dbvalue){  
		Class<?> clazz = method.getParameterTypes()[0];
		Object[] values = {null};
		if(dbvalue == null){
			values[0] = null;
		}
		else if(Collection.class.isAssignableFrom(clazz)){
			Class<?> returnType = Tools.bean.getGenericSinge(method,2);
			values[0] = listFromDatabaseJson(dbvalue.toString(), returnType);
		}
		else if(Map.class.isAssignableFrom(clazz)){
			//clazz = getMethodGenericClass(method,false);
			throw new YMException("未实现");
		}
		else{
			values[0] = fromDatabaseValue(clazz, dbvalue);
		}
		
		try {
			method.invoke(bean, values);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new YMException("赋值" + bean.getClass().getName() + "方法"  + method.getName() +"(" + dbvalue+ ")出错",e);
		}
		return values[0];
	}
	/**
	 * 转换java对象为数据库能识别的对象<br/>
	 * boolean 会转换为 1:true,2:false;
	 * @param returnType 指定类型
	 * @param value 
	 * @return
	 */
	private static Object fromDatabaseValue(Class<?> returnType,Object dbvalue){
	    Object rs = null;
		if(dbvalue==null){
			;//空
		}
		else if (returnType == boolean.class || returnType == Boolean.class) {
			if(dbvalue instanceof Boolean){
				rs = dbvalue;
			}
			else{
	            Number b = Tools.bean.convert(Number.class, dbvalue);
	            rs = Boolean.valueOf(b.intValue()==1);
			}
        } 
		else if(isPrimitive(returnType)){
			rs = Tools.bean.convert(returnType, dbvalue);
		}
		else if(returnType.isEnum()){
        	rs =enumFromDatabaseValue(returnType, dbvalue);
        }
        else { //转换为json对象
            rs = Tools.bean.convert(returnType,dbvalue);
        }
        return rs;
	}
	
	/** 把数据库的字段值，转换为java 的IEnumType枚举*/
	private static IEnumJson enumFromDatabaseValue(Class<?> returnType,Object dbvalue){
		if(dbvalue==null){
			return null;
		}
		IEnumJson rs = null;
		if(IEnumJsonn.class.isAssignableFrom(returnType)){
			Number code  = Tools.bean.convert(Number.class, dbvalue);
        	if(code!=null){
        		Integer c =(code instanceof Integer ? (Integer)code: Integer.valueOf(code.intValue()));
        		rs = (IEnumJson)EnumManager.get(returnType, c);
        	}
    	}
		else if(IEnumJsons.class.isAssignableFrom(returnType)){
			String code  = Tools.bean.convert(String.class, dbvalue);
        	if(code!=null){ 
        		rs =(IEnumJson)EnumManager.get(returnType, code);
        	}
		}
    	else{
    		throw new YMException("无法转换为数据库值，不能识别的枚举:" + returnType.getClass());
    	}
		
		return rs;
	} 
	
	/**
	 * 1.针对后端的数组保存到数据库时使用 toJSON(value) 转换后的json还原
	 * 2.针对前端的数组被JSON.stringify(value) 转换后的json还原
	 */
	public static Collection<?> listFromDatabaseJson(String value, Class<?> returnType) {
		if(value == null){
			return null; 
		}
		boolean isString = String.class.equals(returnType);
		value = value.trim();
		if(value.length()==0){
			return new ArrayList<>(0); 
		}
		else if(isString){
			int len = value.length();
			char fc = value.charAt(0),lc = value.charAt(len-1);
			if(len>=2 && fc=='[' && lc==']'){
				if(len==2){
					return new ArrayList<>(0);
				}
				else {
					value = value.substring(1,len-1);
				}
			}
			
			String[] arys =(value.indexOf(',')==-1 ? new String[]{value} : value.split(","));
			List<Object> rs = new ArrayList<>(arys.length);
			for(int i = 0;i < arys.length;i++){
				String e = arys[i].trim();
				int l = e.length();
				if(l>1){
					char fc2 = e.charAt(0),lc2 = e.charAt(l-1);
					if((fc2 =='"' && lc2 =='"') || (fc2 == '\'' && lc2 =='\'')){
						e = e.substring(1,l-1);
					}
				}
				rs.add(e);
			}
			return rs;
		}
		else if(returnType == Boolean.class || returnType == boolean.class){
			Object rs =  Tools.bean.convert(Boolean.class, value);
			return (Collection<?>)rs;
		}
		else{
			List<?> rs = Tools.json.toList(value, returnType);
			/*try {
				rs = Tools.json.toList(value, returnType);
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}*/
			return rs;
		}
	}
	/**针对前端的 map对象 被JSON.stringify(value) 转换后的json还原 */
	public static Map mapFromDatabaseJson(String value, Class<?> returnType) {
		// TODO 未实现
		throw new YMException("未实现");
	}
	

	
}
