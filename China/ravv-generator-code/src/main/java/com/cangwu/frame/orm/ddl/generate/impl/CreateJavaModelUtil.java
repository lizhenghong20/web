package com.cangwu.frame.orm.ddl.generate.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.farwalker.waka.core.WakaObject;
import cn.farwalker.waka.util.Tools;
import cn.farwalker.waka.util.YMException;

import com.cangwu.frame.orm.core.IBaseDao;
import com.cangwu.frame.orm.ddl.annotation.DDLIndex.IndexType;
import com.cangwu.frame.orm.ddl.consts.ConstFarwalker;
import com.cangwu.frame.orm.ddl.generate.DDLColumnAdapter;
import com.cangwu.frame.orm.ddl.generate.DDLColumnType;
import com.cangwu.frame.orm.ddl.generate.DDLIndexAdapter;
import com.cangwu.frame.orm.ddl.generate.DDLTableStruct;
import com.cangwu.frame.orm.ddl.generate.dialect.DDLUtils;
import com.cangwu.frame.orm.ddl.generate.util.GenerateUtils;


/**
 * 生成数据库对应的java对象
 * @author juno
 */
public class CreateJavaModelUtil { 
	
	public static final String SPLIT=GenerateUtils.SPLIT,D_IMPORT_MORE="/**import more*/";
	private static final String D_IMPORT_DATE="import java.util.Date;",D_IMPORT_INDEX="import com.cangwu.frame.orm.ddl.annotation.DDLIndex;";
	private StringBuilder sql = new StringBuilder();
	private String className,packageName;
	private final String[] prefixs;
	/**
	 * 
	 * @param prefixs 小写的前缀
	 */
	public CreateJavaModelUtil(String[] prefixs){
		this.prefixs = prefixs;
	}
	
	/**
	 * 去掉前缀
	 * @param prefixs
	 */
	private String removePrefix(String tableName ){
		if(prefixs == null || prefixs.length==0){
			return tableName;
		}
		String name = tableName.toLowerCase();//转为小写，与prefixs相同
		for(String p : this.prefixs){
			if(name.startsWith(p)){
				tableName = tableName.substring(p.length());
				break;
			}
		}
		return tableName;
	}
	
	/**
	 * 产生 java Model对象
	 */
	public StringBuilder createModel(DDLTableStruct structs,String defaultPackage){
		this.sql.setLength(0); 
		this.setClassName(structs , defaultPackage);
		DDLUtils.sortColumns(structs.getColumns());
		
		createClass(structs);
		createFields(structs);
		createEnums(structs);
		createGetset(structs);
		println("}");
		
		return this.sql;
	}
	/**返回类名(首字母大写)*/
	public String getClassName() {
		return className;
	}
	/**包名*/
	public String getPackageName() {
		return packageName;
	}
	/**初始类名及包名*/
	private void setClassName(DDLTableStruct table,String defaultPackage) {
		String tableName =table.getName();
		//int idx = tableName.indexOf('_');		
		//String defaultModel = (idx>0 ? tableName.substring(idx+1): tableName) ;
		String defaultModel = this.removePrefix(tableName);
		String defaultClass = GenerateUtils.convertUnderlineHump(defaultModel);
		defaultClass = GenerateUtils.firstUpper(defaultClass)+ "Bo";
		
		String commentClass = DDLColumnType.parseCommentEnum(table.getComment());
		if(Tools.string.isEmpty(commentClass)){ //注释为null
			this.className = defaultClass;
			this.packageName =defaultPackage  +"." +  defaultModel +".model";
			return ;
		}
		
		int classDot = commentClass.lastIndexOf('.');
		String commentName =(classDot>=0?commentClass.substring(classDot+1):commentClass); 
		
		if(defaultClass.equalsIgnoreCase(commentName)){//与注释相同 
			if(classDot>=0){
				this.packageName =commentClass.substring(0,classDot); 
				this.className = commentName ;
			}
			else{
				this.packageName =defaultPackage  +"." +  defaultModel +".model";
				this.className = commentName ;
			}
		}
		else{//与注释不相同(只取包名)
			this.className = defaultClass ; 
			if(classDot >=0){
				this.packageName = commentClass.substring(0,classDot); 
			}
			else{
				this.packageName = defaultPackage  +"." +  defaultModel +".model";
			}
		}
	}
	
	
	/**返回类名*/
	private void createClass(DDLTableStruct table){		
		WakaObject<String> comment = new WakaObject<>();
		DDLColumnType.parseCommentEnum(table.getComment(), comment);
		String packageName = this.getPackageName().replaceAll("_", "");
		int idx = packageName.lastIndexOf('.');
		String packageBiz = packageName;
		if(idx>0){//去掉下划线
			packageBiz = packageName.substring(0,idx) + ".iface";
		}
		
		println("package " ,packageName,";");
		println();
		//println("//" + D_IMPORT_DATE );
		println("import com.cangwu.frame.orm.core.BaseBo;");
		println("import com.cangwu.frame.orm.ddl.annotation.DDLColumn;");
		println("import com.cangwu.frame.orm.ddl.annotation.DDLTable;");
		println("//" + D_IMPORT_INDEX );
		println("import com.cangwu.frame.orm.core.IFieldKey;");
		println();
		
		//mybatisplus
		println("import java.io.Serializable;");
		println("import com.baomidou.mybatisplus.activerecord.Model;");
		println("import com.baomidou.mybatisplus.annotations.TableField;");
		println("import com.baomidou.mybatisplus.annotations.TableId;");
		println("import com.baomidou.mybatisplus.annotations.TableName;");
		println("import com.baomidou.mybatisplus.enums.FieldFill;");
		println("import com.baomidou.mybatisplus.enums.FieldStrategy;");
		println("import com.baomidou.mybatisplus.enums.IdType;");

		println(D_IMPORT_MORE);
		
		String className = getClassName(); //没有Bo的字符
		if(className.toUpperCase().endsWith("BO")){
			className = className.substring(0,className.length()-2);
		}
		//String bizname = "I" + className +"Biz";
		//println("import ",packageBiz,"." ,bizname ,";");
		
		println("/**");
		println(" * ",comment.getValue() );
		println(" * @author generateModel.java");
		println(" */");
		
		println("@TableName(",className,"Bo.TABLE_NAME)");//mybatisplus
		println("@DDLTable(name=",className,"Bo.TABLE_NAME,comment=\"", comment.getValue()  ,"\")");
		println("public class ",className , "Bo extends Model<",className,"Bo> implements BaseBo{"); 
		
		long serial = className.hashCode();
		println(SPLIT +"private static final long serialVersionUID = ",String.valueOf(serial) ,"L;");
		println(SPLIT +"/**数据表名*/");
		println(SPLIT +"public static final String TABLE_NAME = \"",table.getName(),"\";");
		println(); 
	}
 
	private void createFields(DDLTableStruct table){
		Collection<List<DDLIndexAdapter>> indexs = table.getIndexs().values();
		String tableName = table.getName();
		int indexCount =0;
		int dateCount =0;
		for(DDLColumnAdapter c : table.getColumns()){
			String columnName = c.getName();
			if(IBaseDao.PRIMARY_KEY.equalsIgnoreCase(columnName)){
				;//continue;//跳过ID字段
				//mybatisplus
				println(SPLIT +"@TableId(value = \"",columnName,"\", type = IdType.ID_WORKER)");
			}
			else if(ConstFarwalker.CreateTimeDB.equalsIgnoreCase(columnName)){
				println(SPLIT + "@TableField(value = \"",columnName ,"\", strategy=FieldStrategy.NOT_EMPTY, fill = FieldFill.INSERT)");
			}
			else if(ConstFarwalker.ModifiedTimeDB.equalsIgnoreCase(columnName)){
				println(SPLIT + "@TableField(value = \"",columnName ,"\",strategy=FieldStrategy.NOT_EMPTY, fill = FieldFill.UPDATE)");
			}
			else{
				println(SPLIT + "@TableField(\"",columnName ,"\")");
			}
			WakaObject<String> comment = new WakaObject<>();
			DDLColumnType.parseCommentEnum(c.getComment(), comment);
			String fieldName = GenerateUtils.convertUnderlineHump(columnName);
			
			String comment2 = "",length2="";
			if(Tools.string.isNotEmpty(comment.getValue())){
				comment2 = "comment=\"" + comment.getValue() + "\"";
			}
			
			DDLColumnType types =c.getType();
			if(types == DDLColumnType.VARCHAR || types == DDLColumnType.LIST || types == DDLColumnType.MAP){
				if(c.getLength()!= DDLColumnType.Length){
					length2="length=" + c.getLength();
				}
			}
			else if(types == DDLColumnType.DATE || types == DDLColumnType.TIMESTAMP){
				dateCount++;//有日期字段
			}
			String name = (columnName.equalsIgnoreCase(fieldName) ? "": "name=\"" + columnName + "\"");
			if(comment2.length()>0 || length2.length()>0 || name.length()>0){
				print(SPLIT +"@DDLColumn(");
				if(name.length()>0){
					print(name,",");
				}
				if(comment2.length()>0){
					print(comment2,",");
				}
				if(length2.length()>0){
					print(length2);
				}
				deleteLastComma(',');
				println(")");
			}
			else{
				println(SPLIT +"@DDLColumn");
			}
			indexCount += createIndex(tableName, c, indexs);
			String fieldTypes = getMenthodType(c);
			println(SPLIT +"private  ",fieldTypes, " ",fieldName,";");
		}
		if(indexCount>0){
			this.insertImport(D_IMPORT_INDEX);
		}
		if(dateCount>0){
			this.insertImport(D_IMPORT_DATE);
		}
	}
	
	private String getJavaType(DDLColumnType type){
		Class<?> rs;
		switch (type) {
		case BIGINT:
			rs = Long.class;
			break;
		case DATE:
		case TIMESTAMP:
			rs =Date.class;
			break;
		case DECIMAL:
			rs =Double.class;
			break;
			
		case ENUMN:
		case INTEGER:
			rs =Integer.class;
			break;
		case NULL:
		case ENUMS:
		case VARCHAR:
			rs =String.class;
			break;
		case BOOLEAN:
			rs = Boolean.class;
			break;
		default:
			throw new YMException("不支持的类型：" + type.name());
		}
		return rs.getSimpleName();
	}
	
	/**
	 * 取得类型，并且插入引用
	 * @param column
	 * @return
	 */
	private String getMenthodType(DDLColumnAdapter column){
		DDLColumnType columnType = column.getType();
		String clazz = DDLColumnType.parseCommentEnum(column.getComment());
		String type;
		if(Tools.string.isEmpty(clazz)){
			 type = getJavaType(columnType);
		}
		else{
			String imports;
			int idx,endx;
			if((idx = clazz.indexOf('<'))>0 && (endx = clazz.indexOf('>', idx+1))>0){
				//java.util.List<com.cangwu.facedesign.service.face.modeljiekou.consts.FaceName>
				String c1 = clazz.substring(0,idx);
				String c2 = clazz.substring(idx+1,endx);
				imports  ="import " + c1 + ";" + GenerateUtils.CRLF;
				
				if(c2.startsWith(GenerateUtils.C_JAVA_LANG)){
					c2 = c2.substring(10);
				}
				else{
					imports = imports + "import " + c2 + ";" + GenerateUtils.CRLF;
				}
				
				int ix1 = c1.lastIndexOf('.'),ix2 = c2.lastIndexOf('.');
				type = c1.substring(ix1+1) + "<" + (ix2>0 ? c2.substring(ix2+1): c2) + ">";
				
				insertImport(imports);
			}
			else if(clazz.indexOf('.')>0){
				imports  ="import " + clazz + ";" + GenerateUtils.CRLF; 
				int ix = clazz.lastIndexOf('.');
				type = clazz.substring(ix+1);
				
				insertImport(imports);
			}
			else{ //没有报名，就是java.lang包或同一个包下面
				type = clazz;
			}
		}
		return type;
	}
	
	/**
	 * DDLIndex(type=IndexType.Normal,name="ac",order=0)
	 * @param tableName
	 * @param c
	 * @param indexs
	 * @return 索引数量
	 */
	private int createIndex(String tableName,DDLColumnAdapter c,Collection<List<DDLIndexAdapter>> indexs){
		List<DDLIndexAdapter> idxs = findColumnIndex(c.getName(), indexs); 
		int rs =0;
		for(DDLIndexAdapter ix : idxs){
			String type =(ix.getType()==IndexType.Normal? null :" type=\"IndexType.Unique\",") ;
			String name = GenerateUtils.getIndexName(tableName, ix.getFieldname());
			if(name.equalsIgnoreCase(ix.getName())){
				name = null;
			}
			else{
				name =" name=\"" + ix.getName() + "\",";
			}
			String order = (ix.getOrder() >0 ?" order=" + String.valueOf(ix.getOrder()) + "," :null);
			if(Tools.string.isAllEmpty(type,name,order)){
				println(SPLIT +"@DDLIndex");	
			}
			else{
				print(SPLIT +"@DDLIndex(",type, name, order);
				this.deleteLastComma(',');
				println(")");
			}
			
			rs ++;
		}
		return rs;
	}
	
	private List<DDLIndexAdapter> findColumnIndex(String column,Collection<List<DDLIndexAdapter>> idxs){
		List<DDLIndexAdapter> rs = Collections.emptyList();
		boolean exist = false;
		for(List<DDLIndexAdapter> ls : idxs){
			for(DDLIndexAdapter i : ls){
				if(column.equalsIgnoreCase(i.getFieldname())){
					if(!exist){
						exist = true;
						rs = new ArrayList<>(3);
					}
					rs.add(i);
				}
			}
		}
		return rs;
	}
	/**字段名枚举*/
	private void createEnums(DDLTableStruct table){
		println();
		println(SPLIT +"/** 字段名枚举 */");
		println(SPLIT +"public static enum Key implements IFieldKey{");
		//print(SPLIT +SPLIT );
		for(DDLColumnAdapter c : table.getColumns()){
			String column = c.getName();
			String field = GenerateUtils.convertUnderlineHump(column);
			println(SPLIT +SPLIT, field,"(\"",column,"\")", ",");	        
	    }
		int l = this.sql.length()-3;
		this.sql.setCharAt(l,';');
		
		println(SPLIT  +SPLIT+"private final String column;");
		println(SPLIT +SPLIT +"private Key(String k){");
		println(SPLIT +SPLIT+SPLIT +"this.column = k;");
		println(SPLIT +SPLIT +"}");
		println(SPLIT +SPLIT +"@Override");
		println(SPLIT +SPLIT +"public String toString(){");
		println(SPLIT +SPLIT +SPLIT +"return column;");
		println(SPLIT +SPLIT +"}");
		println(SPLIT +"}");
	}
	
	private void createGetset(DDLTableStruct table){
		
		for(DDLColumnAdapter c : table.getColumns()){
			if(IBaseDao.PRIMARY_KEY.equalsIgnoreCase(c.getName())){
				//continue;//跳过ID字段
			}
			
			println();
			WakaObject<String> comment = new WakaObject<>();
			DDLColumnType.parseCommentEnum(c.getComment(), comment);
			String type = getMenthodType(c);
			
			println(SPLIT +"/** ",comment.getValue(),"*/");
			
			String columnName =GenerateUtils.convertUnderlineHump(c.getName());
			String firstUpperName = GenerateUtils.firstUpper(columnName);
			println(SPLIT +"public ",type, " get",firstUpperName,"(){");
			println(SPLIT +SPLIT +"return this.",columnName,";");
			println(SPLIT +"}");
			println();

			println(SPLIT +"/** ",comment.getValue(),"*/");
			println(SPLIT +"public void set",firstUpperName,"(",type," v){");
			println(SPLIT +SPLIT +"this.",columnName,"=v;");
			println(SPLIT +"}");
		}
		
		println(SPLIT +"@Override");
		println(SPLIT +"protected Serializable pkVal() {");
		println(SPLIT+SPLIT +"return this.id;");
		println(SPLIT +"}");
	}
	/**插入入引用*/
	private boolean insertImport(String imports){
		if(sql.indexOf(imports)==-1){
			int fromIndex = sql.indexOf(D_IMPORT_MORE);
			//String imports ="import " + clazz + ";" + GenerateUtils.D_CRLF;
			sql.insert( fromIndex,imports+ GenerateUtils.CRLF);
			return true;
		}
		else{
			return false;
		}
	}
	
	private void println(String... text){
		GenerateUtils.println(sql, text);
	}
	private void print(String... text){
		GenerateUtils.print(sql, text);
	}
	/**
	 * 删除最后的逗号或其他
	 * @param split 逗号或其他
	 * @return 删除的位置
	 */
	private int deleteLastComma( char split){
		return GenerateUtils.deleteLastComma(sql, split);
	}
}
