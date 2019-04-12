package com.cangwu.frame.orm.core.annotation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.farwalker.waka.cache.CacheManager;
import cn.farwalker.waka.util.Tools;
import cn.farwalker.waka.util.YMException;

import com.baomidou.mybatisplus.service.IService;
import com.cangwu.frame.orm.core.ConvertDBUtil;

/**
 * 加载关联值,关联值比较忽略大小写
 * 实现标签的定义:{@link LoadJoinValue}
 * @author juno
 *
 */
public class LoadJoinValueImpl {
	private static final Logger log = LoggerFactory.getLogger(LoadJoinValueImpl.class);
	
	
	/**取得关联注释的方法(有缓存)*/
	public static List<RelationField> getAnnotation(Object o){
		if(o == null){
			return Collections.emptyList();
		}
		Class<?> clazz = o.getClass();
		String clazzName;{
    		int hashCode = clazz.getName().hashCode();
    		clazzName = "LoadJoinValue_"+hashCode + clazz.getSimpleName() ;
		}
		List<RelationField> mds = CacheManager.cache.getObject(clazzName);
		if(mds != null){
		    return mds;
		}
		///////////////
		
		List<Method> methods = Tools.bean.getClassMethods(clazz);
		mds = new ArrayList<>(methods.size()/5+1);
		for(Method m:methods){
		    String name =m.getName();
		    if(name.equals("getClass") || Modifier.isStatic(m.getModifiers())){
		        continue;
		    }
			LoadJoinValue j = m.getAnnotation(LoadJoinValue.class);
			if(j!=null){
	            if(!name.startsWith("set")){
	                throw new YMException("LoadJoinValue只能定义在set方法上:" + name);
	            }
	            
			    RelationField rf =new RelationField(j);
			    rf.setMethodName(m);
			    mds.add(rf); 
			}
		}
		checkCount(mds, o);
		
		List<RelationField> c = (mds.isEmpty() ? Collections.emptyList():mds);
		CacheManager.cache.putObject(clazzName, (Serializable)c);
		return mds;
	}

	/**
	 * 超过10个关联字段就很有问题了
	 * @param mds
	 * @param obj
	 */
    private static void checkCount(List<RelationField> mds, Object obj) {
        if(mds.size() <=3){
            return ;
        }
        int autoloadCount1 = 0, autoloadCount2 = 0;
        for (RelationField m : mds) {
            if (m.isAutoLoad()) {
                autoloadCount1++;
            }
            else {
                autoloadCount2++;
            }
        }

        if (autoloadCount1 > 3) {
            throw new YMException("自动关联加载字段大于5个了，需要优化:" + obj.getClass().getName());
        }

        if (autoloadCount2 > 7) {
            throw new YMException("手动关联加载字段大于5个了，需要优化:" + obj.getClass().getName());
        }
        else if (autoloadCount2 > 3) {
            log.warn("手动关联加载字段大于5个了，需要优化:" + obj.getClass().getName());
        }
    }
    /***
     * 手动加载关联值
     * @param biz
     * @param rds
     * @return
     */
    public static int load(IService<?> biz,Object... rds){
        if(rds == null || rds.length ==0){
            throw new YMException("对象不能为空");
        }
        List<?> arys;
        if(rds.length ==1 && rds[0] instanceof List){
    		//throw new YMException("如果是List对象，请直接使用load(IBaseBiz<?> biz,List<?> rds,boolean autoLoad)方法");
            arys= (List<?>)rds[0];
    	}
        else{
            arys = Arrays.asList(rds);
        }
        return load(biz, arys, false);
    }
    public static int load(IService<?> biz,List<?> rds){
        return load(biz, rds, false);
    }
    /**
	 * 加载关联值
	 * 实现标签的定义:{@link LoadJoinValue}
	 * @param dao
	 * @param rds
	 * @param autoLoad true - 表示biz加载如果马上执行，false是后面手工加载
	 * @return
	 */
	public static int load(IService<?> biz,List<?> rds,boolean autoLoad){
		int size =(rds==null ?0 : rds.size()); 
		if(size ==0){
			return 0;
		}
		Object obj = rds.get(0);
		List<RelationField> mds = getAnnotation(obj);
		if(mds.isEmpty()){
			return 0;
		}
		
		int rs =0 ;
		for(RelationField md : mds){
			try{
			    if(autoLoad){ //自动加载
			        if(md.isAutoLoad()){
		                rs += loadField(biz, rds, md); 
		            }
			    }
			    else{ //手动加载
			        Object v = Tools.bean.getPropertis(obj, md.getMethodName());
			        if(v==null){
			            rs += loadField(biz, rds, md);
			        }
			    }
			}
			catch(RuntimeException e){
				String err = "关联加载出错" + obj.getClass().getSimpleName() + "." + md.toString() ;
				log.error(err, e);
				throw new YMException(err );
			}
		}
		return rs;
	}

	
	/**
     * 加载关联值
     * 实现标签的定义:{@link LoadJoinValue}
     * @param dao
     * @param rds
     * @param field 本表接收数据的字段-不在LoadJoinValue标签中出现的(不包含get/set)
     * @return 返回第一个元素
     */
    public static Object load(IService<?> biz,List<?> rds,String field){
        int size =(rds==null ?0 : rds.size()); 
        if(size ==0){
            return null;
        }
        Object obj = rds.get(0);
        List<RelationField> mds = getAnnotation(obj);
        if(mds.isEmpty()){
            return null;
        }
        /////////////////////////////////////////////
        RelationField rf = null;
        for(RelationField md : mds){
            if(field.equalsIgnoreCase(md.getMethodName())){
                rf = md;
                break;
            }
        }
        if(rf == null){
            throw new YMException("没有为" + field + "定义@LoadJoinValue标签");
        }
        if(rf.isAutoLoad()){
            return obj; //已经自动加载
        }
        //////////////////////////////
        Object v = Tools.bean.getPropertis(obj, field);
        if(v==null){
            int rs = loadField(biz, rds, rf);
            return rs;
        }
        else{
            return obj;//已经加载了，不在重复加载
        }
    }
    /**
     * 加载关联值
     * @param biz
     * @param rds
     * @param md
     * @return
     */
	public static int load(IService<?> biz,List<?> rds,RelationField md){
	    int size =(rds==null ?0 : rds.size()); 
        if(size ==0 || md.isAutoLoad()){
            return 0;
        }
        Object v = Tools.bean.getPropertis(rds.get(0), md.getMethodName());
        int rs =0;
        if(v==null){
            rs = loadField(biz, rds, md);
        }
        return rs;        
	}
    /**
     * 加载关联值
     * @param biz
     * @param rds
     * @param md
     * @return
     */
    private static int loadField(IService<?> biz,List<?> rds,RelationField md){
    	/**引用id，与对象关系*/
		List<Object[]> bos = new ArrayList<>(rds.size());
		Map<Object,Integer> refids = new HashMap<>();
		
		//去掉重复的id
		for(Object o : rds){
		    String refby = md.getBy();
		    Object refv = Tools.bean.getPropertis(o, refby);
		    if(refv!=null){
		        refids.put(refv,Integer.valueOf(1));
		        
		        bos.add( new Object[]{refv,o});
		    }
		}
		/////////////////////////////////////////
		//final String joinField = md.getTable().toUpperCase() + "_" +md.getJoinField().toUpperCase();
		final String joinField = Tools.string.convertHumpUnderline( md.getJoinField());
		List<Map<String, Object>> records = loadDatabase(biz, refids.keySet(), md);
		
		for(Object[] o : bos){
		    Object id = o[0];
		    Object bo = o[1];//原来的数据对象
		    
		    if(md.isCollection()){
		        List<Map<String, Object>> datas = findByJoinValueList(records, joinField,id);
		        if(Tools.collection.isNotEmpty(datas)){//来源已经没有对应的数据了
		        	setJoinValue(bo,md,datas);
		        }
		    }
		    else{
    		    Map<String, Object> data = findByJoinValueSinge(records, joinField,id);
    		    if(data!=null){//来源已经没有对应的数据了
    		    	setJoinValue(bo,md,data);
    		    }
		    }
		}
		return records.size();
	}
	
    /***select returnfield from table where joinfield in (by1,by2,by3....);*/
	private static List<Map<String, Object>> loadDatabase(IService<?> biz,Collection<Object> refids,RelationField md){
        /*ICriterias query = biz.createCriteria();//new DefaultCriterias(md.getTable());
        query.getColumns().clear();
        query.setTableName(md.getTable());
        
        String joinField = md.getJoinField().toUpperCase();
        query.addColumn(joinField);//默认字段
        for(String fd: md.getReturnFields()){
            query.addColumn(fd);
        }
        query.or().andMultipleValue(joinField,CompareType.IN, refids);
        query.addOrder(joinField, true);
        
        List<Map<String, Object>> records = biz.findColumnsByParams(query, null);
        return records;*/
		final int SIZE= refids.size();
		if(SIZE==0){
			return Collections.emptyList();
		}
		
		String joinField = Tools.string.convertHumpUnderline( md.getJoinField());
		final int SL="SELECT ".length();
		StringBuilder sql = new StringBuilder("SELECT ");
		for(String fd: md.getReturnFields()){
			sql.append(Tools.string.convertHumpUnderline(fd)).append("  ,");
        }
		if(sql.length()> SL){
			sql.append(joinField).append(' ');
			//sql.setCharAt(SL,' ');
		}
		else{
			sql.append("* ");
		}
		sql.append("FROM ").append(md.getTable()).append(" WHERE ").append(joinField).append(" IN (?");
		for(int i =1 ;i < SIZE ;i++){
			sql.append(",?");
		}
		sql.append(')');
		
		//找不到封装的接口，所以直接使用了原生语句
		DataSource ds = Tools.springContext.getBean(DataSource.class);
		Connection cnt = null;
		PreparedStatement st = null;
		try{
			cnt =ds.getConnection();
			st = cnt.prepareStatement(sql.toString());
			log.debug("直接使用了原生语句:" + sql.toString());
			int idx =0;
			for(Object v : refids){
				st.setObject(++idx, v);
			}
			ResultSet rs = st.executeQuery();
			ResultSetMetaData rd = rs.getMetaData();
			List<Map<String, Object>> result = new ArrayList<>();
			
			while(rs.next()){
				Map<String, Object> row = new HashMap<>();
				result.add(row);
				
				for(int i =1,  count = rd.getColumnCount();i <= count ; i++){
					String fn = rd.getColumnName(i);
					Object v = rs.getObject(i);
					row.put(fn, v);
				}
			}
			rs.close();
			return result;
		}
		catch(Exception e){
			throw new YMException("使用原生语句查询关联出错:" + sql.toString(),e);	
		}
		//biz.selectById(1l);
		finally{
			close(st);
			close(cnt);
		}
	}

	private static void close(Connection cnt) {
		try {
			if (cnt == null || cnt.isClosed()) {
				return;
			}
			cnt.close();
		} catch (SQLException e) {
			log.error("关闭connection出错", e);
		}
	}

	private static void close(Statement st) {
		try {
			if (st == null || st.isClosed()) {
				return;
			}
			st.close();
		} catch (SQLException e) {
			log.error("关闭connection出错", e);
		}
	}
	/**
	 * 单值:根据关联值，找原来的对象(理论都会找到的)
	 * 关联值比较忽略大小写
	 * @param records 加载的关联数据
	 * @param jionField 关联字段(大写)
	 * @param joinValue 关联值
	 * @return
	 */
	private static Map<String,Object> findByJoinValueSinge(List<Map<String,Object>> records ,String jionField,Object joinValue){
	    if(joinValue == null){
	        return null;
	    }
	    Map<String,Object> rs = null;
	    String joins = joinValue.toString();
	    for(Map<String,Object> m : records){
	        //关联值比较忽略大小写
	        Object v = m.get(jionField);
	        if(v!=null && (joins.equals(v) || joins.equalsIgnoreCase(v.toString()))){
	            rs = m;
	            break;
	        }
	    } 
	    return rs;
	}
	/**
     * 多值:根据关联值，找原来的对象(理论都会找到的)
     * 关联值比较忽略大小写
     * @param records 加载的关联数据
     * @param jionField 关联字段(大写)
     * @param joinValue 关联值
     * @return
     */
    private static List<Map<String,Object>> findByJoinValueList(List<Map<String,Object>> records ,String jionField,Object joinValue){
        if(joinValue == null){
            return null;
        }
        String joins = joinValue.toString();
        List<Map<String,Object>> rs = new ArrayList<>();
        
        for(Map<String,Object> m : records){
            //关联值比较忽略大小写
            Object v = m.get(jionField);
            if(v!=null && (joins.equals(v) || joins.equalsIgnoreCase(v.toString()))){
                rs.add(m);
            }
        } 
        return rs;
    }
    
    /**
     * 单行赋值:加载父级记录，或者父级名称赋值：setParentName(),setParentBo()
     * @param owner
     * @param data
     * @param md
     * @return
     */
    private static Object setJoinValue(Object owner, RelationField md, Map<String, Object> data) {
        final String fieldName = md.getMethodName();
        Object fieldValue = "";
        try {
            if(md.isCollection()){
                throw new YMException("请使用加载从表方法处理");
            }
            
            String[] fds = md.getReturnFields();
            if (md.isPrimitive()) { //单值
                String key = Tools.string.convertHumpUnderline(fds[0]);//md.getTable() + "_" + fds[0]; 
                fieldValue = data.get(key);
                Tools.bean.setPropertis(owner, fieldName, fieldValue); 
            }
            else{ //对象赋值
                fieldValue=setJoinObject(md, data);
                Tools.bean.setPropertis(owner, fieldName, fieldValue);
            }
            
            return fieldValue;
        }
        catch (Exception e) {
            throw new YMException("字段赋值出错:" + fieldName + "(" + fieldValue + ")", e);
        }
    }
    /**
     * 对象赋值
     * @param md
     * @param data
     * @return
     */
    private static Object setJoinObject(RelationField md,Map<String, Object> data){
        Object fieldValue = "";
        String fieldName = "";
        try {
            final String table = md.getTable() + "_";
            String[] fds = md.getReturnFields();
            Class<?> clazz =md.getReturnClazz(); 
            Object obj = clazz.newInstance();
            
            for(String fd: fds){
            	String key = Tools.string.convertHumpUnderline(fd);
            	String fn = Tools.string.convertUnderlineHump(fd);
                fieldValue = data.get(key);
                if(fieldValue!=null){
                    fieldName = "set" + fn;
                    Method m = Tools.bean.getClassMethod(clazz,  fieldName);
                    ConvertDBUtil.fromDatabaseValue(obj, m, fieldValue);
                    //Tools.bean.setPropertis(obj, fd, fieldValue);
                }
            }
            return obj;
        }
        catch (Exception e) {
            throw new YMException("字段赋值出错:" + md.getReturnClazz().getSimpleName() + "." + fieldName + "(" + fieldValue + ")", e);
        }
    }
    /**
     * 多行赋值:加载从表记录赋值：setChildren()
     * @param owner
     * @param md
     * @param datas
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static Collection setJoinValue(Object owner, RelationField md, List<Map<String, Object>> datas) {
        if(!md.isCollection()){
            throw new YMException("请使用加载父表方法处理");
        }
        final String fieldName = md.getMethodName();
        try {//列表处理
            Collection list = (Collection)Tools.bean.getPropertis(owner, fieldName); 
            boolean isnull =(list == null);
            Method method = null;
            if(isnull){
                method= Tools.bean.getClassMethod(owner.getClass(), "set" + fieldName);
                Class<?> clz = method.getParameterTypes()[0]; 
                if(clz.equals(List.class) || clz.equals(Collection.class)){
                    list = new ArrayList<>();
                }
                else{
                    list = (Collection)clz.newInstance();
                }                
            }

            /////////////////////////////
            for(Map<String, Object> d : datas){
                Object o = setJoinObject(md, d);
                list.add(o);
            }
            
            if(isnull){//要放在后面，前面没有值会影响业务类判断
                method.invoke(owner, list);
            }
            return list;
        }
        catch (Exception e) {
            throw new YMException("列表字段赋值出错:set" + fieldName + "()", e);
        }
    }
    
    /** 
     * 取主表记录. 从表对象按field取值(主表id的值)，并且以主表id的值为key返回
     * @param biz 主表biz
     * @param beans 从表对象
     * @param field 从表字段属性
     * @return key-主表id,value-主表记录
     * /
    @SuppressWarnings("unchecked")
    public static <V> Map<String,V> findMasterByIds(IBaseBiz<?> biz,Collection<? extends BaseBo> beans,String childField){
        if(beans == null || beans.isEmpty()){
            return Collections.emptyMap();
        }
        List<String> ids = new ArrayList<>(beans.size());
        for(BaseBo bo :beans){
            Object value = Tools.bean.getPropertis(bo, childField);
            if(value!=null){
                ids.add(value.toString());
            }
        }
        if(ids.isEmpty()){
            return Collections.emptyMap();
        }
        
        ICriterias c = biz.createCriteria();
        c.or().and(IBaseDao.PRIMARY_KEY, CompareType.IN,ids);
        List<? extends BaseBo> rows = biz.findByParams(c,null);
        
        Map<String,V> rs = new HashMap<>();
        for(BaseBo bo : rows){
            rs.put(bo.getId(),(V) bo);
        }
        return rs;
    }
    / ** 
     * 取从表记录. 主表对象按id取值，并且以主表id的值为key返回
     * @param biz 从表biz
     * @param beans 主表对象
     * @param childField 从表字段
     * @return key-主表id,value-从表记录
     * /
    @SuppressWarnings("unchecked")
    public static <V> Map<String,List<V>> findChildByIds(IBaseBiz<?> biz,Collection<? extends BaseBo> beans,Object childField){
        if(beans == null || beans.isEmpty()){
            return Collections.emptyMap();
        }
        int size = beans.size();
        List<String> ids = new ArrayList<>(size);
        Map<String,List<V>> result = new HashMap<>(size);
        
        for(BaseBo bo :beans){
            String id = bo.getId();
            if(id!=null){
                ids.add(id);
                result.put(id, new ArrayList<>());
            }
        }
        
        if(ids.isEmpty()){
            return result;
        }
        String field = childField.toString();
        ICriterias c = biz.createCriteria();
        c.or().and(field, CompareType.IN,ids);
        List<? extends BaseBo> rows = biz.findByParams(c,null);
         
        for(BaseBo bo : rows){
            Object masterId = Tools.bean.getPropertis(bo, field);
            List<V> row = result.get(masterId.toString());
            if(row ==null){
                row = new ArrayList<>();
                result.put(masterId.toString(),row);
            }
            row.add((V)bo);
        }
        return result;
    }*/
}
