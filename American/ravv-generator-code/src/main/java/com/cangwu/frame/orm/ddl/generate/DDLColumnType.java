package com.cangwu.frame.orm.ddl.generate;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.farwalker.waka.core.WakaObject;
import cn.farwalker.waka.orm.EnumManager.IEnumJsonn;
import cn.farwalker.waka.orm.EnumManager.IEnumJsons;
import cn.farwalker.waka.util.Tools;
import cn.farwalker.waka.util.YMException;

import com.cangwu.frame.orm.ddl.generate.dialect.DDLUtils;
import com.cangwu.frame.orm.ddl.generate.util.GenerateUtils;

/**系统支持的字段类型*/
public enum DDLColumnType{
	VARCHAR(Types.VARCHAR),
	INTEGER(Types.INTEGER),
	BIGINT(Types.BIGINT),
	/** decimal(18,2) 设计小数点为2位的原因是 1.偷懒：分转换为元的情况偷懒，2:如果要4位的小数点，可以用分再加2位足矣*/
	DECIMAL(Types.DECIMAL),
	
	TIMESTAMP(Types.TIMESTAMP),
	DATE(Types.DATE),
	
	BOOLEAN(Types.INTEGER),

	NULL(Types.NULL),
	
	/**泛型:字符类型的枚举*/
	ENUMS(Types.VARCHAR),
	/**泛型:数字类型的枚举*/
	ENUMN(Types.INTEGER),
	/**泛型:字符类型的数组(只支持List或者ArrayList)*/
	LIST(Types.VARCHAR),
	/**泛型:字符类型的MAP类型(只支持Map或者HashMap)*/
	MAP(Types.VARCHAR);
	
	/** 设计小数点为2位的原因是 {@link #DECIMAL}*/
	private static final int digits=2;
	/** 字符默认长度(2的倍数) */
	public static final int Length = 64;
	/** Text的长度  65535*/
	public static final int LengthText = 0xffff;//Integer.MAX_VALUE;
	
	private int sqlType;
	DDLColumnType(int type){
		this.sqlType = type;
	}
	public int getKey(){
		return sqlType;
	}
	
	/**
	 * @param sqlType sql的数据类型({@link Types})
	 * @param comment 字段注释(判断枚举),可以为空, ({@link DDLUtils#getColumnDDL(DDLColumnAdapter)},{@link #getCommentEnum(String, Class)})
	 * @return
	 */
	public static DDLColumnType getType(int sqlType,String comment){
		DDLColumnType rs = null;
		switch (sqlType) {
		case Types.VARCHAR:
		case Types.LONGVARCHAR://length=65535
			rs = VARCHAR;
			break;
		case Types.INTEGER:
			rs = INTEGER;
			break;
		case Types.BIGINT:
			rs = BIGINT;
		break;
		case Types.BIT://不建议使用(还原时直接使用int)
		case Types.TINYINT:
			rs = BOOLEAN;
			break;
		case Types.DECIMAL:
			rs = DECIMAL;
			break;			
		case Types.TIMESTAMP:
			rs = TIMESTAMP;
			break;
		case Types.DATE:
			rs = DATE;
			break;
		default:
			rs =NULL;
			break;
		}
		
		//varchar及int类型是应用比较广
		if(rs==VARCHAR || rs == INTEGER ){
			DDLColumnType g = getGenericType(rs, comment);
			if(g!=null){
				rs = g;
			}
		}
		return rs;
	}
	private static DDLColumnType getGenericType(DDLColumnType type,String comment){
		String enumClazz ;
		if(Tools.string.isEmpty(comment) || (enumClazz = parseCommentEnum(comment))==null){
			return null;
		}
		DDLColumnType rs ;
		if(type == INTEGER && enumClazz.equalsIgnoreCase("Boolean")){
			rs = BOOLEAN;
		}
		else if(enumClazz.indexOf(GenerateUtils.C_JAVA_UTIL)==0){
			String c = enumClazz.substring(GenerateUtils.C_JAVA_UTIL.length());
			if(c.startsWith("List") || c.startsWith("ArrayList")){
				rs = LIST;
			}
			else if(c.startsWith("Map") || c.startsWith("HashMap")){
				rs = MAP;
			}
			else{
				throw new YMException("不能还原的类型:" + comment);
			}
		}
		else{
			rs = (type == INTEGER ? ENUMN :ENUMS);//默认是枚举()
		}
		return rs;
	}
	
	/**
	 * 返回type的SQL语句,所有关键字的前面留空格
	 * @param lengh 只对varchar有效
	 */
	public String getTypeSQL(int length){
		String rs;
		switch (this) {
		case VARCHAR:
			/** 字符默认长度*/
			if(length >= LengthText){
				rs =" TEXT"; //长字符串
			}
			else{
				rs =(length <=0 ? " VARCHAR(" + Length + ")" : " VARCHAR(" + length + ")");
			}
			break;
		case BOOLEAN:
		case INTEGER:
			rs = " INT";
			break;
		case BIGINT: 
			rs = " BIGINT";
			break;
		case DECIMAL: 
			rs = " DECIMAL(18," + DDLColumnType.digits +")" ;
			break;
		case DATE:
			rs = " DATE";
			break;
		case TIMESTAMP:
			rs = " DATETIME";
			break;
		case ENUMS:
			rs = " VARCHAR(" + (Length ) + ")";
			break;
		case ENUMN:
			rs = " INT";
			break;
		case LIST:
			/** 字符默认长度*/
			rs =(length <=0 ? " VARCHAR(" + Length + ")" : " VARCHAR(" + length + ")");
			break;
		case MAP:
			/** 字符默认长度*/
			rs =(length <=0 ? " VARCHAR(" + Length + ")" : " VARCHAR(" + length + ")");
			break;
		default:
			throw new YMException("不支持的类型:" + this.name());
		}
		return rs;
	}

	private static final Map<String,String[]> commentClass = new HashMap<>();
	/**
	 * 分析注释中包含的类信息
	 * @param comment
	 * @return 返回enumType的类型信息(包含 包名)
	 */
	public static String parseCommentEnum(String comment){ 
		WakaObject<String> text = new WakaObject<>();
		return parseCommentEnum(comment, text);
	}
	
	/**
	 * 分析注释中包含的类信息
	 * (解封类信息,封装参考 {@link #getCommentEnum(String, Class)})
	 * @param comment
	 * @param text 注释文本
	 * @return 返回enumType的类型信息(包含 包名)
	 */
	public static String parseCommentEnum(String comment,WakaObject<String> text){ 
		int idx ,endx;
		if(Tools.string.isEmpty(comment) || (idx = comment.indexOf("${"))<0 
				|| (endx = comment.lastIndexOf('}'))<0){
			text.setValue(comment);
			return null;
		}
		String[] value = commentClass.get(comment);
		if(value!=null && value.length==2){
			String clazz = value[0];
			text.setValue(value[1]);
			return clazz;			
		}
		else{
			String txt= comment.substring(0,idx);
			text.setValue(txt);
			String js = comment.substring(idx+1, endx+1);
			String clazz = (String)Tools.json.toMap(js).get("clazz");
			
			commentClass.put(comment, new String[]{clazz,txt});
			return clazz;
		}
	}
	/**
	 * 字段注释增加枚举类型说明(封装类信息,解封参考 {@link #parseCommentEnum(String, YMObject)})
	 * @param comment 注释文字
	 * @param enumClazz 枚举类
	 * @return
	 */
	public static String getCommentEnum(DDLColumnAdapter column,Class<?> enumClazz,Type genericType){
		DDLColumnType type = column.getType();
		String comment = column.getComment();
		if(type == DDLColumnType.ENUMN || type == DDLColumnType.ENUMS 
				|| type== DDLColumnType.BOOLEAN ){
			return getCommentEnum(comment, enumClazz);
		}
		else if(type == DDLColumnType.LIST || type == DDLColumnType.MAP){
			String clazz = getCommentEnum(comment, enumClazz);
			List<Class<?>> gt = Tools.bean.getGenericClass(genericType);
			StringBuilder sb = new StringBuilder();
			
			for(Class<?> clz : gt){
				String gn = clz.getName();
				if(gn.startsWith(GenerateUtils.C_JAVA_LANG)){
					gn = gn.substring(GenerateUtils.C_JAVA_LANG.length());
				}
				sb.append(gn).append(',');
			}
			
			int l = sb.length();
			if(l>0){
				sb.setCharAt(l-1, '>');
				String px = clazz.substring(0,clazz.length()-2) ;
				sb.insert(0,  px + "<");				
				sb.append("\"}");
				clazz = sb.toString();
			}
			
			return clazz;
		} 
		else{
			return comment;
		}
	}
	/**
	 * 字段注释增加枚举类型说明(封装类信息,解封参考 {@link #parseCommentEnum(String, YMObject)})
	 * @param comment 注释文字
	 * @param enumClazz 枚举类
	 * @return
	 */
	public static String getCommentEnum(String comment,Class<?> enumClazz){		
		int idx ;
		if(Tools.string.isEmpty(comment)){
			comment ="";
		}
		else if((idx = comment.indexOf("${")) >=0 && comment.lastIndexOf('}')>0){
			comment = comment.substring(0, idx);//去掉类型信息
		}
		else{
			//comment =""; //没有类型信息
		}
		
		String cls = enumClazz.getName().replace('$', '.');
		if(cls.startsWith("java.lang.")){
			cls = cls.substring(10);
		}
		String js = comment + "${\"clazz\":\"" + cls + "\"}";
		//c.setComment(js);
		return js;
	}
	
	/**
	 * 
	 * @param fieldType
	 * @return 返回java.sql.Types
	 */ 
	public static DDLColumnType getType(final Class<?> fieldType ){
		DDLColumnType type  ; 
	    if (fieldType == String.class) {
	    	type = DDLColumnType.VARCHAR; 
        } else if (fieldType == int.class || fieldType == Integer.class ) {
        	type = DDLColumnType.INTEGER; 
        } else if (fieldType == long.class || fieldType == Long.class) {
        	type = DDLColumnType.BIGINT; 
        } else if (fieldType == float.class || fieldType == Float.class ||
        		fieldType == double.class || fieldType == Double.class || fieldType == BigDecimal.class) {
        	type = DDLColumnType.DECIMAL;//decimal(18,3) 
        } else if (fieldType == java.util.Date.class || fieldType == java.sql.Date.class 
        		|| fieldType == java.sql.Timestamp.class) {
        	type = DDLColumnType.TIMESTAMP; 
        }
        else if(fieldType == boolean.class || fieldType == Boolean.class){
        	type = DDLColumnType.BOOLEAN;
        }
        else if(fieldType.isEnum()){
        	if(IEnumJsons.class.isAssignableFrom(fieldType)){
        		type = DDLColumnType.ENUMS; 
        	}
        	else if(IEnumJsonn.class.isAssignableFrom(fieldType)){
        		type = DDLColumnType.ENUMN; 
        	}
        	else{
        		throw new YMException("枚举类型字段只支持IEnumTypes和IEnumTypen的实现!");
        	}
        }
        else if (Collection.class.isAssignableFrom(fieldType)) {
        	type = DDLColumnType.LIST; 
        }
        else if (Map.class.isAssignableFrom(fieldType)) {
        	type = DDLColumnType.MAP; 
        }
        else{
        	throw new YMException("数据库实体类不支持的类型:" + fieldType.getName());
        }
	     
	    return type;
	}
}