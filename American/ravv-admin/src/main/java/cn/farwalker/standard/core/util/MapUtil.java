package cn.farwalker.standard.core.util;

import java.util.Map;

public final class MapUtil {
	/**
	 * 获取map中的long值
	 * 
	 * @param map
	 * @param key
	 * @return
	 */
	public static Long longValue(Map<String, Object> map, String key) {
		Object value = map.get(key);
		if (value != null && value.toString().length() > 0) {
			return Long.valueOf(value.toString());
		}
		return null;

	}

	/**
	 * 获取字符类型的 值
	 * 
	 * @param map
	 * @param key
	 * @return
	 */
	public static String stringValue(Map<String, Object> map, String key) {
		Object value = map.get(key);
		if (value != null && value.toString().length() > 0) {
			return value.toString();
		}
		return "";
	}

	/**
	 * 獲取整型
	 * 
	 * @param map
	 * @param key
	 * @return
	 */
	public static Integer integerValue(Map<String, Object> map, String key) {
		Object value = map.get(key);
		if (value != null && value.toString().length() > 0) {
			return Integer.valueOf(value.toString());
		}
		return null;
	}

	/**
	 * 獲取boolean
	 * 
	 * @param map
	 * @param key
	 * @return
	 */
	public static boolean booleanValue(Map<String, Object> map, String key) {
		Object value = map.get(key);
		if (value != null && value.toString().length() > 0) {
			return (boolean) value;
		}
		return false;
	}
}
