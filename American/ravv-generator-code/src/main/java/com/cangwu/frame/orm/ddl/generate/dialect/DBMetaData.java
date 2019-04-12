package com.cangwu.frame.orm.ddl.generate.dialect;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
/**
 * 数据库的元素
 * @author juno
 */
public abstract class  DBMetaData{
	protected Connection cnt;
	public DBMetaData(Connection cnt){
		this.cnt = cnt;
	}
	/**
	 * 取得所有表名及注释
	 * @return key-大写表名,value-注释
	 * @throws SQLException
	 */
	public abstract Map<String,String> getTableComment() throws SQLException;
	/**取得所有表名*/
	public abstract List<String> getTableNames() throws SQLException ;
	/**取得注释*/
	public abstract String getTableComment(String tableName);
}
