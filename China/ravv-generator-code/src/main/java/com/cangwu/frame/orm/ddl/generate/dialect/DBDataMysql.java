package com.cangwu.frame.orm.ddl.generate.dialect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.farwalker.waka.util.Tools;
import cn.farwalker.waka.util.YMException;

/**
 * mysql可以直接取得SQL:
 * ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + table);
 * ResultSet rs = stmt.executeQuery("show full columns from " + table);   
 * @author juno
 *
 */
class DBDataMysql extends DBMetaData{
 	private Map<String,String> tableComment;
 	public DBDataMysql(Connection cnt){
		super(cnt);
	}
	@Override
	public Map<String,String> getTableComment() throws SQLException {
		Statement st = null;
		try{
			st = cnt.createStatement();
			List<String> tables = getAllTableName(st);
			Map<String,String> map = new HashMap<>();
			for (String tableName : tables) {            
	            ResultSet rs = st.executeQuery("SHOW CREATE TABLE " + tableName);
	            if (rs != null && rs.next()) {	
	                String createSQL = rs.getString(2);
	                String comment = getCommentByCreateSQL(createSQL);
	                map.put(tableName.toUpperCase(), comment);
	            }
	            rs.close();
	        }
			tableComment = map;
			return tableComment;
		}
		finally{
			Tools.io.close(st);
		}
    }
	@Override
	public List<String> getTableNames() throws SQLException {
		Statement st = null;
		try{
			st = cnt.createStatement();
			List<String> result = getAllTableName(st);
			return result;
		}
		finally{
			Tools.io.close(st);
		}
	}

	private String getCommentByCreateSQL(String createSQL) {
        String comment = null;
        int index = createSQL.toUpperCase().indexOf("COMMENT='");
        if (index < 0) {
            return "";
        }
        comment = createSQL.substring(index + 9);
        comment = comment.substring(0, comment.length() - 1); 
        return comment;
    }
	private List<String> getAllTableName(Statement st) throws SQLException{
		List<String> result = new ArrayList<>();
        ResultSet rs = st.executeQuery("SHOW TABLES ");
        while (rs.next()) {
            String tableName = rs.getString(1);
            result.add(tableName);
        }
        rs.close();
        return result;
    }
	@Override
	public String getTableComment(String tableName) {
		if(tableComment == null){
			try {
				getTableComment();
			} catch (SQLException e) {
				throw new YMException("取得表注释出错:",e);
			}
		}
		String comment = this.tableComment.get(tableName.toUpperCase());
		return comment;
	}
}