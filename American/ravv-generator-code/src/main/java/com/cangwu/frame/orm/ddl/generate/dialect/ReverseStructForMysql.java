package com.cangwu.frame.orm.ddl.generate.dialect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.farwalker.waka.core.WakaObject;
import cn.farwalker.waka.util.Tools;
import cn.farwalker.waka.util.YMException;

import com.cangwu.frame.orm.ddl.annotation.DDLIndex.IndexType;
import com.cangwu.frame.orm.ddl.generate.DDLColumnAdapter;
import com.cangwu.frame.orm.ddl.generate.DDLColumnType;
import com.cangwu.frame.orm.ddl.generate.DDLIndexAdapter;
import com.cangwu.frame.orm.ddl.generate.DDLTableStruct;
import com.cangwu.frame.orm.ddl.generate.iface.IReverseStruct;
import com.cangwu.frame.orm.ddl.generate.util.GenerateUtils;

/**
 * 还原数据库对象
 * @author juno
 *
 */
public class ReverseStructForMysql implements IReverseStruct{
	private final static String MD_CATALOG="TABLE_CAT",MD_SCHEM="TABLE_SCHEM",MD_TABLE_NAME ="TABLE_NAME"
			,MD_COLUMN_NAME ="COLUMN_NAME",MD_DATA_TYPE ="DATA_TYPE" 
			,MD_COLUMN_SIZE ="COLUMN_SIZE";
	/**小数部分的位数*/
	private final static String  MD_DECIMAL_DIGITS ="DECIMAL_DIGITS";
	/**是否允许使用 NULL(columnNullable 明确允许null)*/
	private final static String  MD_NULLABLE ="NULLABLE";
	/**描述的注释*/
	private final static String  MD_REMARKS ="REMARKS";
	
	/**描述的注释*/
	private final static String  MD_INDEX_NAME  ="INDEX_NAME", MD_INDEX_TYPE  ="TYPE"
			, MD_INDEX_NON_UNIQUE   ="NON_UNIQUE", MD_INDEX_ASC_OR_DESC ="ASC_OR_DESC";
	
	private Collection<DDLTableStruct> structs;
	public ReverseStructForMysql(Connection cnt){
		structs = this.init(cnt);
	}
	
	/**
	 * 取得数据库的结构<br/>
	 * mysql可以直接:
	 * ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + table);
	 * ResultSet rs = stmt.executeQuery("show full columns from " + table);    
	 * @param cnt 数据库连接
	 * @return
	 */
	public Collection<DDLTableStruct> getTableStructs(){
		return structs;
	}
	private Collection<DDLTableStruct> init(Connection cnt){
		try{
			DatabaseMetaData md = cnt.getMetaData();
			String catalog =cnt.getCatalog(); 
			String schem =cnt.getSchema();
			DataBaseType dbtype = JdbcDrives.getDataBaseType(md);
			
			//表名及注释
			DBMetaData dbmeta = null; 
			if(dbtype == DataBaseType.MySQL){
				dbmeta = new DBDataMysql(cnt); 
				dbmeta.getTableComment();//初始化一下
			}
			
			final String[] types = {"TABLE"}; // 类型：表
			//final String schem = convertDatabaseCharsetType(ROOT_USER, "mysql");
			ResultSet rs = md.getTables(catalog,schem , null,types);  
			//ResultSet columnRs = md.getColumns(catalog, schem,"%","%");  
			
			List<DDLTableStruct> result = new ArrayList<>();
	        while (rs.next()) {
	        	String tableName = rs.getString(MD_TABLE_NAME);
	        	String tableComment = rs.getString(MD_REMARKS);
	        	if(Tools.string.isEmpty(tableComment) && dbmeta!=null){
	        		tableComment = dbmeta.getTableComment(tableName);
	        	}
	        	
                // 根据表名提前表里面信息：  
                ResultSet columnRs = md.getColumns(catalog,schem, tableName,"%");
                Map<String,DDLTableStruct> tbm = getTableColumns(columnRs);
                DDLTableStruct tb = tbm.get(tableName);
                tb.setComment(tableComment);
                result.add(tb);
                
                ResultSet indexRs = md.getIndexInfo(catalog,schem, tableName,false,true);
                WakaObject<String> primaryColumn = new WakaObject<>();
                Map<String,List<DDLIndexAdapter>> indexs = getTableIndexs(indexRs,primaryColumn);
                tb.setIndexs(indexs);
                
                //设置主键字段
                String primaryField = primaryColumn.getValue();
                if(Tools.string.isNotEmpty(primaryField)){
                	for(DDLColumnAdapter column : tb.getColumns()){
                		if(primaryField.equalsIgnoreCase(column.getName())){
                			column.setPrimary(true);
                			break;
                		}
                	}
                }
            }
			
			return result;
			 
		}
		catch(SQLException e){
			throw new YMException("取得表结构出错:",e);
		}
	}
	
	/**可以整个库一起获取*/
	private Map<String,DDLTableStruct> getTableColumns(ResultSet columnRs) throws SQLException{
		Map<String,DDLTableStruct> rs = new HashMap<>();
		while(columnRs.next()){
			String tableName = columnRs.getString(MD_TABLE_NAME);
			String columnName = columnRs.getString(MD_COLUMN_NAME);
			int dataType = columnRs.getInt(MD_DATA_TYPE);
			int nullable = columnRs.getInt(MD_NULLABLE);			
			
			String comment = columnRs.getString(MD_REMARKS);
			DDLColumnType type = DDLColumnType.getType(dataType,comment);
			
			int length=0,digits = 0;
			if(type==null){
				String msg ="不能识别:" + tableName + "." + columnName + "的类型:" + dataType;
				msg = msg + ",现在只支持" + DDLColumnType.class.getSimpleName() + "中的类型";
				throw new YMException(msg);
			}
			else if(type == DDLColumnType.ENUMN || type == DDLColumnType.ENUMS){
				//comment是保存全文本的，截取后不能还原
				/*YMObject<String> text = new YMObject<>();
				DDLColumnType.parseCommentEnumType(comment,text);
				comment = text.getValue();*/
			}
			else if(type.getKey() == java.sql.Types.VARCHAR){
				length = columnRs.getInt(MD_COLUMN_SIZE);
			}
			else if(type == DDLColumnType.DECIMAL){
				digits = columnRs.getInt(MD_DECIMAL_DIGITS);
			}
			
			/////////////////////////////////////////////////
			DDLTableStruct struct = rs.get(tableName);
			if(struct == null){
				struct = new DDLTableStruct(tableName,"");
				struct.setColumns(new ArrayList<>());
				
				rs.put(tableName, struct);
			}
			
			DDLColumnAdapter column = new DDLColumnAdapter(columnName, type);
			column.setComment(comment);
			if(length>0){
				column.setLength(length);
			}
			column.setNullable(nullable == DatabaseMetaData.columnNullable);
			struct.getColumns().add(column);
		}
		return rs;
	}
	/**
	 * 取得索引
	 * @param columnRs
	 * @param primaryColumn 返回主键字段
	 * @return key-索引名称，value-索引字段
	 * @throws SQLException
	 */
	private Map<String,List<DDLIndexAdapter>> getTableIndexs(ResultSet columnRs,WakaObject<String> primaryColumn) 
			throws SQLException{
		Map<String,List<DDLIndexAdapter>> rds = new HashMap<>();
		while(columnRs.next()){
			String indexName = columnRs.getString(MD_INDEX_NAME);
			int indexType = columnRs.getInt(MD_INDEX_TYPE );
			boolean noUnique = columnRs.getBoolean(MD_INDEX_NON_UNIQUE) ;
			//String ascOrDesc = columnRs.getString(MD_INDEX_ASC_OR_DESC);
			//idx.setOrder("A".equalsIgnoreCase(ascOrDesc));
			String columnName = columnRs.getString(MD_COLUMN_NAME); 
			
			DDLIndexAdapter idx = new DDLIndexAdapter(columnName);
			idx.setName(indexName);
			IndexType type = (noUnique ?  IndexType.Normal :IndexType.Unique);
			idx.setType(type);
			
			//设置主键字段
			if(indexType == DatabaseMetaData.tableIndexClustered
					||(type == IndexType.Unique && indexName.toUpperCase().indexOf("PRIMARY")>=0)){
				primaryColumn.setValue(columnName);
			}
			else{
				List<DDLIndexAdapter> fidx = rds.get(indexName);
				if(fidx == null){
					fidx = new ArrayList<>();
					rds.put(indexName, fidx);
				}
				idx.setOrder(fidx.size()+1);
				fidx.add(idx);
			}
		}
		return rds;
	}
	
	
}
