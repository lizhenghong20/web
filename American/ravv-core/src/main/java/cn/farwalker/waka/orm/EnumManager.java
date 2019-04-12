package cn.farwalker.waka.orm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.farwalker.waka.util.Tools;
import cn.farwalker.waka.util.YMException;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

public class EnumManager {
	/**枚举基类 <br/>
	private final String key;
	private final String label;

	private RoleType(String key, String label) {
		this.key = key;
		this.label = label;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return getKey();
	}
	*/
	
	
	/**
	 * 从前端到数据库的枚举和key-value,
	 * 这个不对直接外实现,普通的string实现可以通过IEnumJsons完成
	 * 枚举转换为SQL ,应用在：{@link CriterionExpression#toString()}、
	 * {@link DefaultFieldValue#getValue()}、
	 * {@link com.cangwu.frame.orm.jdbc.query.DefaultFieldValue#getValue()} <br/> 
	 * com.fasterxml.jackson.annotation.JsonFormat 注解是为了springBoot输出前台是格式化为json格式<br/> 
	 */
	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public interface IEnumJson extends IEnum  {
		public String getLabel();
		/**通用的方法*/
		public Serializable getValue(); 
	}
	
	/**字符类型的枚举*/
	public interface IEnumJsons extends IEnumJson{
		public String getKey();
		@Override
		default Serializable getValue(){
	        return getKey();
	    }
	}
	/**数值类型的枚举*/ 
	public interface IEnumJsonn extends IEnumJson{
		public Integer getKey();
		
		@Override
		default Serializable getValue(){
	        return getKey();
	    }
	}
	
	private static final Map<Class<? extends IEnumJson>,List<IEnumJson>> enumValues = new HashMap<>();
	
	/**创建元素时，不用注册的(通过枚举类就可以取得所有枚举值)*/
	/*public static void add(IEnumType e){
		if(e == null){
			throw new YMException("元素不能为空");
		}
		ArrayList<IEnumType> rds = types.get(e.getClass());
		if(rds == null){
			rds = new ArrayList<>();
			types.put(e.getClass(), rds);
		}
		int idx = indexOf(rds,e);
		if(idx ==-1){
			rds.add(e);
		}
		else{
			throw new YMException("元素已存在:" + e.getCode());
		}
	}*/ 
	
	/**
	 * 取得字符类型元素
	 * @param clazz
	 * @param keyOrJson 或json
	 * @return
	 */
	public static <T> T get(Class<T> clazz, String keyOrJson){
		String code = convertKey(keyOrJson); 
		if(Tools.string.isEmpty(code)){
			return null;
		}/*
		else if(BooleanState.class.equals(clazz)){
			boolean enabled = (code.equalsIgnoreCase("true") || code.equals("1"));
			BooleanState e =(enabled ? BooleanState.TRUE : BooleanState.FALSE);
			return (T)e;
		}*/
		//////////////////////////////////////////////
		
		List rds = getEnumValues(clazz);
		int idx ;
		if(IEnumJsonn.class.isAssignableFrom(clazz)){
			Integer key = Integer.valueOf(code);
			idx = indexOf(rds, key);
		}
		else if(IEnumJsons.class.isAssignableFrom(clazz)){
			idx = indexOf(rds,code);	
		}
		else{
			throw new YMException(clazz.getName() + "枚举必须实现:IEnumType的两个子接口");
		}

		/** 初始化已经判断
		if(!rs.getKey().equals(rs.toString())){
			throw new YMException("IEnumType#toString()必须等于getCode(),详细请看IEnumType接口说明");
		}*/
		if(idx >= 0){
			T rs = (T)rds.get(idx);
			return rs;
		}
		else{
			throw new YMException(clazz.getName() + "不存在元素:" + code);
		}
	}
	private static String convertKey(String keyOrJson){
		if(Tools.string.isEmpty(keyOrJson)){
			return null;
		}
		keyOrJson = keyOrJson.trim();
		int len = keyOrJson.length()-1;
		//int start = keyOrJson.indexOf('{'),endx =(start >=0 ? keyOrJson.lastIndexOf('}') :-1);
		
		if(len > 0 && keyOrJson.charAt(0) =='{' && keyOrJson.charAt(len) == '}'){
			/*if(start >1 && endx < len){
				keyOrJson = keyOrJson.substring(start, endx);
			}*/
			Map<String, Object> json = Tools.json.toMap(keyOrJson);
			Object v = null;
			for(Map.Entry<String, Object> e: json.entrySet()){
				if(e.getKey().equalsIgnoreCase("key")){
					v = e.getValue();
					break;
				}
			}
			return(v == null ? null : v.toString());
		}
		else{
			return keyOrJson;
		}
	}
	/**
	 * 取得数字类型元素
	 * @param clazz
	 * @param code
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public static <T> T get(Class<T> clazz, Integer code){
		if(code==null){
			return null;
		}
		/*else if(BooleanState.class.equals(clazz)){
			return (IEnumTypen)(code.equals(BooleanState.TRUE.getValue()) ? BooleanState.TRUE : BooleanState.FALSE);
		}*/
		
		@SuppressWarnings("rawtypes")
		List  rds = getEnumValues(clazz);
		int idx = indexOf(rds,code);
		if(idx < 0){
			throw new YMException(clazz.getName() + "不存在元素:" + code);
		}
		
		IEnumJsonn rs = (IEnumJsonn)rds.get(idx);
		/** 初始化已经判断
		if(!rs.getCode().equals(rs.toString())){
			throw new YMException("IEnumType#toString()必须等于getCode(),详细请看IEnumType接口说明");
		}*/
		return (T)rs;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<? extends IEnumJson> getEnumValues(Class clazz){
		List<IEnumJson> rds = enumValues.get(clazz);
		if(rds != null){
			return rds;
		}
		 
		EnumSet set = EnumSet.allOf(clazz);
		rds = new ArrayList(set);
		final boolean isNumber =IEnumJsonn.class.isAssignableFrom(clazz);  
		Collections.sort(rds, new Comparator<IEnumJson>(){
			@Override
			public int compare(IEnumJson o1, IEnumJson o2) {
				Object k1 = o1.getValue(),k2 = o2.getValue();
				int rs ;
				if(isNumber){
					int v1 = ((Number)k1).intValue(),v2 = ((Number)k2).intValue();
					rs =  v1-v2;
				}
				else{
					String s1 = k1.toString(),s2 = k2.toString();
					rs = s1.compareToIgnoreCase(s2);
				}
				if(rs ==0){
					throw new YMException(o1.getClass().getName() +"枚举值重复:" + k1);
				}
				return rs;
			}
		});
		//enumValues.put(clazz,rds);//排序
		enumValues.put(clazz,  new ArrayList(set));//按书写顺序 
		return rds;
	}
	/**
	 * 依赖FastJson的枚举输出，{@link DefaultFastjsonConfig}
	 * FastJsonConfig config = new FastJsonConfig();
		config.setSerializerFeatures(SerializerFeature.WriteEnumUsingToString);
	 * @param e
	 * @return
	 */
	public static Map<String,Object> toMap(IEnumJson e){
		//IEnumJson e =  (IEnumJson)value;
    	Map<String,Object> m = new HashMap<>();
    	m.put("key", e.getValue());
    	m.put("label", e.getLabel());
		//Map<String,Object> m = new HashMap<>(2);
    	return m;
		
	}
	private static int indexOf(List<IEnumJsons> rds,String code){
		if(Tools.string.isEmpty(code)){
			throw new YMException("元素code不能为空");
		}		
		
		int idx =-1;
		for(int i=0,size = rds.size();i < size;i++){
			IEnumJsons t = rds.get(i);
			if(code.equalsIgnoreCase(t.getKey())){
				idx = i;
				break;
			}
		}
		return idx;
	}
	private static int indexOf(List<IEnumJsonn> rds,Integer code){
		if(code == null){
			throw new YMException("元素code不能为空");
		}		
		
		int idx =-1;
		for(int i=0,size = rds.size();i < size;i++){
			IEnumJsonn t = rds.get(i);
			if(code.equals(t.getKey())){
				idx = i;
				break;
			}
		}
		return idx;
	}
}
