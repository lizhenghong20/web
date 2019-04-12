package com.cangwu.frame.web;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cn.farwalker.waka.core.WakaObject;
import cn.farwalker.waka.util.Tools;
import cn.farwalker.waka.util.YMException;

/**
 * 返回处理结果<br/>
 * 已经精简最轻量级了
 * @deprecated 已经搬到 {@link cn.farwalker.waka.core.JsonResult} 了
 * @author 纪其俊
 */
public class JsonResult<T> extends JsonMap implements Map<Object, Object> , Serializable {
	private static final long serialVersionUID = 1343154322085757263L;

	private static final String K_SUCCESS = "success", K_MESSAGE = "message",
			K_EXCEPTION = "exception", K_DATA = "data";
	private static final String D_FIAL = "fial";

	// ///////////////////////////////////////////////////////
	/**不是真正的复制，为了屏蔽泛型 <b>不同类型返回值</b> 的编译问题,对 data==null 特别安全*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> JsonResult<T> clone(JsonResult rs) {
        return rs;
    }
	
	/** 返回失败的对象 */
	public static <T> JsonResult<T> newFail(String message) {
		return newFail(message, (T)null);
	}
	/** 返回失败的对象 */
	public static <T> JsonResult<T> newFail(String message,T t) {
		JsonResult<T> rs = new JsonResult<>();
		rs.setMessage(message);
		rs.setData(t);
		return rs;
	}
	/** 返回失败的对象 */
	public static <T> JsonResult<T> newFails(String message,
			String exceptionClassName) {
		JsonResult<T> rs = new JsonResult<>();
		rs.setMessage(message);
		if (Tools.string.isNotEmpty(exceptionClassName)) {
			rs.setException(exceptionClassName);
		}
		return rs;
	}

	/** 返回成功的对象 */
	public static <T> JsonResult<T> newSuccess() {
		return newSuccess(null);
	}

	/** 返回成功的对象 */
	public static <T> JsonResult<T> newSuccess(T t) {
		JsonResult<T> rs = new JsonResult<>();
		rs.setSuccess(true);
		rs.setData(t);
		return rs;
	}

	// ///////////////////////////////////////////////////////
	private boolean success;
	private String message;
	/** 错误类型，提供消费者判断错误类型,一般是exception的类名,如果为空表示正常 */
	private String exception;
	private T data;

	/** 创建空的失败对象 */
	public JsonResult() {
		this.setSuccess(false);
	}

	public JsonResult(boolean success, Map<Object, Object> values) {
		this.setMessage("");
		this.putAll(values);
		this.setSuccess(success);
	}

	public void setData(T v) {
		data = v;
	}

	/** 错误类型，提供消费者判断错误类型,一般是exception的类名 */
	public void setException(String exception) {
		if (Tools.string.isEmpty(exception)) {
			throw new YMException("exception不能为空");
		}
		this.exception = exception;
		this.success = false;
		// this.setSuccess(false);
	}

	public void setMessage(String s) {
		message = s;
	}

	public void setSuccess(boolean success) {
		this.success = success;
		if (success) {
			this.exception = null;
		} else if (Tools.string.isEmpty(exception)) {
			this.setException(D_FIAL); // 初始状态
		}
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return this.message;
	}

	public T getData() {
		return this.data;
	}

	/** 错误类型，提供消费者判断错误类型 */
	public String getException() {
		return this.exception;
	}

	/** 设置固定值 */
	@SuppressWarnings("unchecked")
	protected boolean setFixedValue(String key, Object value) {
		boolean rs = true;
		if (K_SUCCESS.equalsIgnoreCase(key)) {
			Boolean success = this.setSuccessObject(value);
			this.setSuccess(success.booleanValue());
		} else if (K_MESSAGE.equalsIgnoreCase(key)) {
			this.setMessage((String) value);
		} else if (K_DATA.equalsIgnoreCase(key)) {
			this.setData((T) value);
		} else if (K_EXCEPTION.equalsIgnoreCase(key)) {
			this.setException((String) value);
		} else {
			rs = false;
		}
		return rs;
	}
	
	@Override
	protected String[] getFixedKey() {
		String[] keys ={K_SUCCESS,K_MESSAGE,K_DATA,K_EXCEPTION};
		return keys;
	}
	
	/** 取得固定值 */
	protected boolean getFixedValue(String key, WakaObject<Object> obj) { 
		Object rs = null;
		boolean fiexd = true;
		if (K_SUCCESS.equalsIgnoreCase(key)) {
			rs = Boolean.valueOf(isSuccess());
		} else if (K_MESSAGE.equalsIgnoreCase(key)) {
			rs = getMessage();
		} else if (K_DATA.equalsIgnoreCase(key)) {
			rs = getData();
		} else if (K_EXCEPTION.equalsIgnoreCase(key)) {
			rs = getException();
		} else {
			fiexd = false;
		}

		if (fiexd && obj != null) {
			obj.setValue(rs);
		}
		return fiexd;
	}

	@Override
	public String toString() {
		String rs = Tools.json.toJson(this);
		return rs;
	}

	private Boolean setSuccessObject(Object value) {
		boolean b;
		if (value == null) {
			b = false;
		} else if (value instanceof Boolean) {
			b = ((Boolean) value).booleanValue();
		} else {
			String v = value.toString();
			b = (v.equals("1") || v.equalsIgnoreCase("true") || v
					.equalsIgnoreCase("y"));
		}
		return Boolean.valueOf(b);
	}

	
}

/**为了简洁元素，并且实现map,而且能输出josn 特殊处理的类 (实现元素最小化)*/
abstract class JsonMap implements Map<Object, Object> {

	private Map<Object, Object> map;
	
	private class JsonResultSet<T> extends AbstractSet<T> implements Set<T> {
		private final ArrayList<T> arrays;

		public JsonResultSet() {
			arrays = new ArrayList<>();
		}

		@Override
		public boolean add(T t) {
			return arrays.add(t);
		}

		@Override
		public boolean addAll(Collection<? extends T> c) {
			return arrays.addAll(c);
		}

		@Override
		public Iterator<T> iterator() {
			return arrays.iterator();
		}

		@Override
		public int size() {
			return arrays.size();
		}
	}

	private class FixedEntry implements Entry<Object, Object> {
		private final Object key;

		protected FixedEntry(Object key) {
			this.key = key;
		}

		@Override
		public Object getKey() {
			return this.key;
		}

		@Override
		public Object getValue() {
			if (key == null) {
				return null;
			}
			WakaObject<Object> obj = new WakaObject<>();
			JsonMap.this.getFixedValue(key.toString(), obj);
			return obj.getValue();
		}

		@Override
		public Object setValue(Object value) {
			if (key == null) {
				return null;
			}
			JsonMap.this.setFixedValue(key.toString(), value);
			return value;
		}
	}

	/** 设置固定值 */ 
	protected abstract boolean setFixedValue(String key, Object value);
	/** 取得固定值 */
	protected abstract String[] getFixedKey();
	/** 取得固定值 */
	protected abstract boolean getFixedValue(String key, WakaObject<Object> obj) ;
	
	/**固定元素个数*/
	private int fixedSize= -1;
	// //////////////////////////////////////
	/** 继承了Map，所以最少有4个元素,所以永远是false */
	@Override
	public int size() {
		if(fixedSize == -1){
			fixedSize = this.getFixedKey().length;
		}
		return fixedSize + (map == null ? 0 : map.size());
	}

	/** 继承了Map，所以最少有4个元素,所以永远是false */
	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean containsKey(Object key) {
		if (key == null) {
			return false;
		}
		boolean rs = false;// 判断固定值
		for (Entry<Object, Object> e : getEntrySet()) {
			if (e.getKey().equals(key)) {
				rs = true;
				break;
			}
		}

		if (!rs && map != null) {
			rs = map.containsKey(key);
		}
		return rs;
	}

	@Override
	public boolean containsValue(Object value) {
		if (value == null) {
			return false;
		}
		// 判断固定值
		boolean rs = false;// 判断固定值
		for (Entry<Object, Object> e : getEntrySet()) {
			if (e.getValue().equals(value)) {
				rs = true;
				break;
			}
		}
		if (!rs && map != null) {
			rs = map.containsValue(value);
		}
		return rs;
	}

	@Override
	public Object remove(Object key) {
		return (map == null ? null : map.remove(key));
	}

	private Map<Object, Object> createMap() {
		if (this.map == null) {
			this.map = new HashMap<>();
		}
		return this.map;
	}

	@Override
	public Object put(Object key, Object value) {
		if (key == null) {
			throw new NullPointerException("JsonResult不接收null的key");
		}

		Object rs = value;
		boolean fixed = setFixedValue(key.toString(), value);
		if (!fixed) {
			// 额外的信息,默认处理
			rs = createMap().put(key, value);
		}
		return rs;
	}

	@Override
	public void putAll(Map<? extends Object, ? extends Object> m) {
		if (m != null && m.size() > 0) {
			createMap().putAll(m);
		}
	}

	@Override
	public Object get(Object key) {
		if (key == null) {
			throw new NullPointerException("JsonResult不接收null的key");
		}

		WakaObject<Object> rs = new WakaObject<>();
		boolean fixedd = getFixedValue(key.toString(), rs);
		if (!fixedd && map != null) { // 默认方法
			return map.get(key);
		} else {
			return rs.getValue();
		}
	}

	@Override
	public void clear() {
		if (map != null) {
			map.clear();
		}
	}

	private JsonResultSet<Map.Entry<Object, Object>> myEntrySet;

	private Set<Map.Entry<Object, Object>> getEntrySet() {
		if (myEntrySet == null) {
			myEntrySet = new JsonResultSet<>();
			for(String key:getFixedKey()){
				myEntrySet.add(new FixedEntry(key));	
			}
		}
		return myEntrySet;
	}

	@Override
	public Set<Object> keySet() {
		JsonResultSet<Object> rs = new JsonResultSet<>();
		for (Map.Entry<Object, Object> e : getEntrySet()) {
			rs.add(e.getKey());
		}
		if (map == null) {
			return rs;
		} else {
			rs.addAll(map.keySet());
			return rs;
		}
	}

	@Override
	public Collection<Object> values() {
		ArrayList<Object> rs = new ArrayList<>();
		for (Map.Entry<Object, Object> e : getEntrySet()) {
			rs.add(e.getValue());
		}
		if (map == null) {
			return rs;
		} else {
			rs.addAll(map.values());
			return rs;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Set<Map.Entry<Object, Object>> entrySet() {
		Set<Map.Entry<Object, Object>> myset = this.getEntrySet();
		if (map == null) {
			return myset;
		} else {
			JsonResultSet rs = new JsonResultSet<>();
			rs.addAll(map.entrySet());
			rs.addAll(myset);
			return rs;
		}
	}
}
