package cn.farwalker.waka.core.base.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.farwalker.waka.core.WakaException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.orm.ddl.generate.DDLColumnType;
import com.cangwu.frame.web.crud.QueryFilter;
import com.cangwu.frame.web.crud.QueryFilter.Logic;

import cn.farwalker.waka.util.Tools;

/**
 * Controller 辅助工具
 * @author Administrator
 */
public class ControllerUtils {
	/**
	 * @param start 开始行号<br/>
     * @param size 记录数<br/>
     * @param sortfield 排序字段(+/-表示升序或者降序，升序可略) 格式: +id,-name,fsdate
	 * @return
	 */
	public static <T> Page<T> getPage(Integer start,Integer size,String sortfield){
    	int size2 = Tools.number.nullIf(size, 20);
    	int start2 = Tools.number.nullIf(start, 1)  / size2 +1;
    	Page<T> p = new Page<>(start2, size2);
    	
    	if(null != sortfield && sortfield.length()>0){
    		String[] fds = sortfield.split(",");
    		List<String> fdes = new ArrayList<>(2),fasc =  new ArrayList<>(2);
    		
    		for(String fd:fds){
    			if(Tools.string.isEmpty(fd)){
    				continue;
    			}
    			char fc =fd.charAt(0);
    			boolean isAsc  = true;//默认升序
    			if(fc=='-' || fc =='+'){
    				isAsc = (fc =='+');
    				fd = fd.substring(1);
    			}
    			
    			if(isAsc){
    				fasc.add(fd);
    			}
    			else {
    				fdes.add(fd);
    			}
    		}
			p.setDescs(fdes);
			p.setAscs(fasc);
    	}
    	return p;
	}
	/**
	 * 页面条件转换,查询是使用原始的字段
	 * @param query
	 * @return
	 */
	public static <T> Wrapper<T> getWrapper(List<QueryFilter> query){
		if(Tools.collection.isEmpty(query)){
			return new EntityWrapper<>();
		}
		
		Wrapper<T> wrap = new EntityWrapper<T>();
		for(int i =0 ,size = query.size();i< size;i++){
			QueryFilter q = query.get(i);
			
			String fd = Tools.string.convertHumpUnderline(q.getField());
			DDLColumnType type = q.getFieldTypes();
			String startValue = q.getStartValue(),endValue = q.getEndValue();
			Logic logic = q.getLogicType();

			//只有一个值，或者两个值相同
			if((Tools.string.isEmpty(startValue) || Tools.string.isEmpty(endValue)) || (startValue.equalsIgnoreCase(endValue))) {
				String value = Tools.string.nullif(startValue, endValue);
				String comp ;
				switch (type) {
				case VARCHAR:
					if( value.equals(startValue)  && value.equals(endValue)){
						comp = fd + " = {0}";
					}
					else{
						if(value.indexOf('%')<0){
							value = "%" + value +"%";
						}
						comp = fd + " like {0}";	
					}
					
					break;
				case DATE:
				case TIMESTAMP:
					//break;
				default:
					comp = fd + " = {0}";
					break;
				}
				if(logic == Logic.OR){
					wrap.or(true, comp,value);
				}
				else{
					wrap.and(true, comp,value);
				}
			}
			else{
				//排序小到大
				ArrayList<String> rds = new ArrayList<>(Arrays.asList(startValue,endValue));
				Collections.sort(rds);
				if(logic == Logic.OR){
					wrap.or(true, fd  + " BETWEEN {0} and {1}", rds.get(0),rds.get(1));
				}
				else{
					wrap.and(true, fd  + " BETWEEN {0} and {1}", rds.get(0),rds.get(1));
				}
			}
		}
		return wrap;
	}
	/**
	 * Page的record转换为新类
	 * @param source
	 * @param
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Page<T> convertPageRecord(Page source,Class<T> clazz){
		//Page<T> rs = (Page<T>)source;//new Page<>(source.getCurrent(), source.getSize()) ;
		List  rds =convertList( source.getRecords(),clazz);
		source.setRecords(rds);
		return source;
	}

	/**
	 * 
	 * @param rds
	 * @param clazz
	 * @param
	 * @return
	 */
	public static <T> List<T> convertList(List rds,Class<T> clazz){
		return convertList(rds, clazz, false);
	}
	/**
	 * 
	 * @param rds
	 * @param clazz
	 * @param fore 強制轉換類型(mybatis-plus insert時，如果不是對應的類型會出錯的，子類也不行)
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<T> convertList(List rds,Class<T> clazz,boolean fore){
		if(Tools.collection.isEmpty(rds)){
			return rds;
		}
		Class<?> clz = rds.get(0).getClass();
		if(clazz.equals(clz) || (!fore && clazz.isAssignableFrom(clz))){
			return rds;//相同类型，或者子类(不是强制转换情况)
		}

		List  result = new ArrayList<>(rds.size());
		for(Object o : rds){ 
			try {
				T t = (T)clazz.newInstance();
				Tools.bean.cloneBean(o, t);
				result.add(t);
			}
			catch(Throwable e) {
				throw new WakaException("创建类实例出错:" + clazz.getName(),e);
			}
		} 
		return result;
	}
}
