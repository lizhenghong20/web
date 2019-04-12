package cn.farwalker.waka.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * json工具类(使用alibaba的fastjson),其他项目不要用引用果json包了
 * 由于有多种json，所以使用虚类方式 
 * @author Administrator
 */
public abstract class JsonUtil { 
	private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);
	protected static final String D_JSON_CLASS= "@class_name",D_JSON_VO= "@vo" ;


	/** 唯一实例*/
	public static final JsonUtil util = JsonUtil_Jackson.util;// new JsonUtil_fastjson();
	JsonUtil() {
    }
 
    public abstract Map<String, Object> toMap(final String json) ;

    public abstract <T> T toObject(final String json, final Class<T> cls) ;

    public abstract <T> List<T> toList(final String json, final Class<T> cls);
 
    protected abstract <T> T toJavaObject(final Object jsonObject, final Class<T> cls) ;
    /** 如果对象为null,就返回""*/
    public abstract String toJson(final Object o); 

    /**
     * 转换为json字符，并且把类名也加入json中<br/>
     * 递归把所有的元素遍历转换为json,如果是Collection/map,每个元素还要带类型
     * @param value 不能为空对象
     * @return
     * 
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public StringBuilder toJsonClass(Object value){
        if(value==null){
            throw new YMException("不能序列化null");
        }
        Class clazz = value.getClass();
        StringBuilder result = new StringBuilder();
        result.append("{\"" + D_JSON_CLASS + "\":\"").append(clazz.getName()).append("\",\"" + D_JSON_VO  + "\":");//添加class信息
        if(Collection.class.isAssignableFrom(clazz)){ //列表 
            result.append('[');
            Collection list =(Collection)value;
            
            for(Object o:list){
                if(o!=null){
                    StringBuilder e = toJsonClass(o);
                    result.append(e).append(',');
                }
            }
            if(list.isEmpty()){//空的
                result.append(']');
            }
            else{
                result.setCharAt(result.length()-1, ']');
            }
        }
        else if(Map.class.isAssignableFrom(clazz)){
            result.append('{');
            Map<Object,Object> map =(Map<Object,Object>)value;
            for(Map.Entry<Object,Object>  en: map.entrySet()){
                Object o = en.getValue();
                if(o!=null){
                    StringBuilder e = toJsonClass(o);
                    result.append("\"").append(en.getKey()).append("\":").append(e).append(',');
                }
            }
            int l = result.length();
            if(map.isEmpty() || result.charAt(l-1)=='{'){//空的
                result.append('}');
            }
            else{
                result.setCharAt(result.length()-1, '}');
            }
        }
        else{
            String s = this.toJson(value);
            result.append(s); 
        }
        result.append('}');
        return result;
    }
    
    /** 由json反序列化,json中需要包含类信息,与{@link #toJsonClass(Object)}对应<br/> 
     * json反序列化对于泛型的集合并不是十分适用
     */ 
    @SuppressWarnings("unchecked")
    public <T> T toClassObject(String json) { 
        if (Tools.string.isEmpty(json)) {
            return null;
        } 
        
        try {
        	Map<String, Object> classAndVo = this.toMap(json); 
            Object result = parseJsonToJava(classAndVo);
            return (T)result;
        }
        catch (ClassNotFoundException e) {
            throw new YMException("反序列化(" + json + "):" + e.getMessage(), e);
        }
        catch (YMException e) {
            Throwable t = e.getCause();
            if(t!=null){
                throw new YMException("反序列化(" + json + "):" + e.getMessage(), t);
            }
            else{
                throw new YMException("反序列化(" + json + "):" + e.getMessage(), e);
            }
        }
        catch (Throwable  e) {
            throw new YMException("反序列化(" + json + "):" + e.getMessage(), e);
        }
    }
    
    /** 
     * 递归把所有的元素遍历转换为java
     * @param classAndVo 由{@link #toJsonClass(Object)}封装的json，包含了类名及数据
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Object parseJsonToJava(Map<String, Object> classAndVo)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException{ 
        String className =(String) classAndVo.get (D_JSON_CLASS);
        Class clazz  = Class.forName(className);
        Object vo =  classAndVo.get(D_JSON_VO);
        
        Object result ;
        if(vo == null){
        	result  = null;
        }
        if(Collection.class.isAssignableFrom(clazz)){ //列表
        	Collection arys =(Collection)vo;
            Collection list =(Collection) clazz.newInstance(); 
            
            for(Object o : arys){
            	Object e = parseJsonToJava((Map)o);
                list.add(e);
            }
            result = list;
        }
        else if(Map.class.isAssignableFrom(clazz)){//Map
            Map rds = (Map)vo;
            Map map =(Map) clazz.newInstance();
         
            for(Object o: rds.entrySet() ){
            	Map.Entry en =(Map.Entry)o;
                String key =(String)en.getKey();
                Map classAndVo2 =(Map) en.getValue();
                Object e = parseJsonToJava(classAndVo2);
                map.put(key, e);
            }
            result = map;
        }
        else{
        	//Jackson不能完善处理enum的类型
        	//result = this.toJavaObject(vo , clazz);
        	//log.info("由于Jackson对enum反序列化没有好的解决方案，建议使用Tools.bean.toClassObject");
        	
        	//缺点-浅层的对象处理，优点-解决enum的反序列化问题
        	if(vo instanceof Map){
        		result =Tools.bean.mapToObject((Map)vo , clazz);
        	}
        	else{
        		result = Tools.bean.convert(clazz, vo);
        	}
        }
        
        return result; 
    }
    
}
