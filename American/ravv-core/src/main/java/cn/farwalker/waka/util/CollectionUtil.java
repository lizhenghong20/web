package cn.farwalker.waka.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 集合工具类
 *
 * @author Administrator
 */
public class CollectionUtil {

    /** 因为类必须为public，所以只能把构造函数给这样控制 */
    CollectionUtil() {

    }

    public boolean isEmpty(Collection<?> ars) {
        return ars == null || ars.isEmpty();
    }

    public boolean isNotEmpty(Collection<?> ars) {
        return !isEmpty(ars);
    }

    public boolean isEmpty(Object[] ars) {
        return ars == null || ars.length == 0;
    }

    public boolean isNotEmpty(Object[] ars) {
        return !isEmpty(ars);
    }

    public <T> List<T> arrayToList(T... array) {
        return Arrays.asList(array);
    }

    public <T> List<T> collectionToList(Collection<T> collection) {
        return new ArrayList<>(collection);
    }

    public <T> T[] listToArray(List<T> list) {
        return (T[]) list.toArray();
    }

    /**
     * 忽略空元素,使用的Tools.string.joinSplit()方法,与Tools.string.join()有区别的
     *
     * @param collection
     * @param separator
     * @return
     */
    public String join(Collection<?> collection, String separator) {
        /*if (isEmpty(collection)) {
			return "";
		} else if (collection.size() == 1) {
			Object obj = collection.iterator().next();
			return obj == null ? "" : obj.toString();
		} else {
			StringBuilder str = new StringBuilder(collection.size() * 50);
			for (Iterator<?> itr = collection.iterator(); itr.hasNext();) {
				Object obj = itr.next();
				if (obj != null) {
					if (itr.hasNext()) {
						str.append(obj.toString()).append(separator);
					} else {
						str.append(obj.toString());
					}
				}
			}
			return str.toString();
		}*/
        if (isEmpty(collection)) {
            return "";
        }
        int i = 0;
        String[] strs = new String[collection.size()];
        for (Object s : collection) {
            if (s == null) {
                strs[i++] = "";
            } else {
                strs[i++] = s.toString();
            }
        }
        StringBuilder sb = Tools.string.joinSplit(separator, strs);
        return sb.toString();
    }

    /**
     * 忽略空元素,使用的Tools.string.joinSplit()方法,与Tools.string.join()有区别的
     *
     * @param collection
     * @param separator
     * @return
     */
    public String join(Collection<?> collection, char separator) {
		/*if (isEmpty(collection)) {
			return "";
		} else if (collection.size() == 1) {
			Object obj = collection.iterator().next();
			return obj == null ? "" : obj.toString();
		} else {
			StringBuilder str = new StringBuilder(collection.size() * 50);
			for (Object obj : collection) { 
				if (obj != null) { 
					str.append(obj.toString()).append(separator); 
				}
			}
			if(str.length()>0){
				return str.substring(0, str.length() - 1);
			}
			return "";
		}*/
        return join(collection, String.valueOf(separator));
    } 
    
    /**
     * 按id比较取得对象
     * @param bos
     * @param id
     * @return
     */
	public <T> T getBo(List<T> bos,Object id){
		return getBo(bos,id,"id");
	}
	/**
     * 按属性名比较取得对象
     * @param bos
     * @param id
     * @param propertiName
     * @return
     */
	public <T> T getBo(List<T> bos,Object id,String propertiName){
		T t = null;
		for(T e : bos){
			Object v = Tools.bean.getPropertis(e, propertiName);
			if(id.equals(v)){
				t = e;
				break;
			}
		}
		return t;
	}
}
