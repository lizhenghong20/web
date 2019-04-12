package com.cangwu.frame.orm.ddl.generate.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.farwalker.waka.util.Tools;
import cn.farwalker.waka.util.YMException;

import com.cangwu.frame.orm.core.BaseBo;
import com.cangwu.frame.orm.ddl.annotation.DDLColumn;
import com.cangwu.frame.orm.ddl.annotation.DDLIndex;
import com.cangwu.frame.orm.ddl.annotation.DDLTable;
import com.cangwu.frame.orm.ddl.annotation.NotColumn;
import com.cangwu.frame.orm.ddl.generate.DDLColumnAdapter;
import com.cangwu.frame.orm.ddl.generate.DDLColumnType;
import com.cangwu.frame.orm.ddl.generate.DDLIndexAdapter;
import com.cangwu.frame.orm.ddl.generate.DDLTableStruct;
import com.cangwu.frame.orm.ddl.generate.iface.IReverseStruct;
import com.cangwu.frame.orm.ddl.generate.util.GenerateUtils;
import com.cangwu.frame.orm.ddl.generate.util.SearchAnnotation;

/**
 * 搜索的标签
 * @author juno
 *
 */
public class ReverseStructForAnnotation implements IReverseStruct{
	private static final Map<Class<?>,List<DDLColumnAdapter>> cache = new HashMap<>();
	
	private String[] searchPackage ; 
	public ReverseStructForAnnotation(String[] searchPackageName){
		if(searchPackageName == null || searchPackageName.length==0){
			throw new YMException("搜索的包名不能为空");
		}
		searchPackage = searchPackageName;
	}
	public Collection<DDLTableStruct> getTableStructs(){
		SearchAnnotation search = new SearchAnnotation();
		List<Class> tables = search.searchClass(searchPackage, DDLTable.class);
		if(tables == null || tables.isEmpty()){
			throw new YMException("搜索包没有定义:" + DDLTable.class.getName());
		}
		Map<String,Class> checkRepeat = new HashMap<>();
		List<DDLTableStruct> rs = new ArrayList<>(tables.size());
		StringBuilder error = new StringBuilder();
		
		for(Class<?> c : tables){
			DDLTableStruct e = getTableStruct(c);
			String tableName =e.getName();
			Class clazz = checkRepeat.get(tableName);
			
			if(clazz!=null){
				//throw new YMException(s);
				error.append("数据表名").append(tableName).append("重复定义在:")
					.append(clazz.getSimpleName()).append('与').append(c.getSimpleName())
					.append("中;");
			}
			checkRepeat.put(tableName, c);
			rs.add(e);
		}
		if(error.length()>0){
			throw new YMException(error.toString());
		}
		return rs;
	}
	
	private DDLTableStruct getTableStruct(Class<?> annotation){ 
		DDLTable table = (DDLTable)annotation.getAnnotation(DDLTable.class);
		String tableName = table.name();
		String tableComment = DDLColumnType.getCommentEnum(table.comment(),annotation);
		if(Tools.string.isEmpty(tableName)){
			throw new YMException(annotation.getName() + "的DDLTable标签需要定义表名");
		}
		
		DDLTableStruct ts = new DDLTableStruct(tableName,tableComment);
		List<DDLColumnAdapter> columns = getColumns(annotation,false);
		ts.setColumns(columns);
		
		Map<String,List<DDLIndexAdapter>>  indexs = getIndex(tableName, annotation);
		ts.setIndexs(indexs);
		return ts;
	}
	
	private static DDLColumnAdapter getColumn(Field fd){
		DDLColumn ddl = fd.getAnnotation(DDLColumn.class);
		DDLColumnAdapter column;
		if(ddl==null){
			column = new DDLColumnAdapter("",DDLColumnType.NULL);
		}
		else{
			column = new DDLColumnAdapter(ddl);
		}
		
		
		if(Tools.string.isEmpty(column.getName())){ 
			//没有定义，需要生成默认值 
			column.setName(fd.getName());
		}
		DDLColumnType type = column.getType();
		if(type == DDLColumnType.NULL){
			type = DDLColumnType.getType(fd.getType());
			column.setType(type);
		}
		//加入类型信息
		String comment = DDLColumnType.getCommentEnum(column,fd.getType(),fd.getGenericType());
		column.setComment(comment);
		
		return column;
	}


	/**
	 * 返回复制的版本
	 * @param clazz
	 * @return
	 */
	public static List<DDLColumnAdapter> getColumns(Class<?> clazz){
		return getColumns(clazz, true);
	}
	/**
	 * 
	 * @param clazz
	 * @param copy 是否copy
	 * @return
	 */
	public static List<DDLColumnAdapter> getColumns(Class<?> clazz,boolean copy){
		if(clazz.equals(BaseBo.class)){
			DDLColumnAdapter e = new DDLColumnAdapter("ID",DDLColumnType.VARCHAR);
			return Arrays.asList(e);
		}
		else if(clazz.equals(Object.class)){
			return Collections.emptyList();
		}

		////////////////////////////////////////
		List<DDLColumnAdapter> rds = cache.get(clazz);
		if(rds!=null){
			return (copy  ? new ArrayList<>(rds) : rds);
		}
		rds = new ArrayList<>();
		Field[] fds = clazz.getDeclaredFields();
		for(Field fd:fds){
			boolean isStatic = Modifier.isStatic(fd.getModifiers());
			//Annotation not;
			if(isStatic || fd.getAnnotation(NotColumn.class)!=null){
				continue;//静态或 这个字段不写数据库
			}
			else{			
				DDLColumnAdapter e = getColumn(fd);
				rds.add(e);
			}
		}
		
		////////////////////////////////////////
		List<DDLColumnAdapter> superRds = getColumns(clazz.getSuperclass(),false);
		for(DDLColumnAdapter s : superRds){
			if(rds.contains(s)){
				;//已存在
			}
			else{
				rds.add(s);
			}
		}
		
		cache.put(clazz, rds);
		return (copy  ? new ArrayList<>(rds) : rds);
	}
	
	private Map<String,List<DDLIndexAdapter>> getIndex(String tableName, Class<?> cls){
		if(cls.equals(BaseBo.class) || cls.equals(Object.class)){
			return Collections.emptyMap();
		}
		////////////////////////////////////
		Field[] fds = cls.getDeclaredFields();
		Map<String,List<DDLIndexAdapter>> rds = new HashMap<>();
		for(Field fd:fds){
			boolean isStatic = Modifier.isStatic(fd.getModifiers());
			DDLIndex idx;
			if(isStatic || (idx = fd.getAnnotation(DDLIndex.class))==null){
				continue;//静态
			}
			DDLIndexAdapter e = new DDLIndexAdapter(idx);
			String idxName = e.getName();
			if(Tools.string.isEmpty(idxName)){
				idxName =GenerateUtils.getIndexName(tableName, fd.getName());
			}				
			idxName = idxName.toUpperCase();
			e.setName(idxName);
			
			//字段名
			if(Tools.string.isEmpty(e.getFieldname())){
				e.setFieldname(fd.getName());
			}
			List<DDLIndexAdapter> fidx = rds.get(idxName);
			if(fidx == null){
				fidx = new ArrayList<>();
				rds.put(idxName, fidx);
			}
			fidx.add(e);
		}
		/////////////////合并父级///////////////////
		Map<String,List<DDLIndexAdapter>> superRds= getIndex(tableName,cls.getSuperclass());
		for(Map.Entry<String,List<DDLIndexAdapter>> e : superRds.entrySet()){
			List<DDLIndexAdapter> fs = rds.get(e.getKey());
			if(fs == null){
				rds.put(e.getKey(), e.getValue());
			}
			else {
				fs.addAll(e.getValue());
			}
		}
		return rds;
	}
	
	
}


