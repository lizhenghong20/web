package com.cangwu.frame.orm.ddl.generate.dialect;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import cn.farwalker.waka.util.YMException;

/**
 * 数据库连接
 * jdbc.driverClass=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://xiangin01.mysql.rds.aliyuncs.com:3306/xiangin_v2?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull
jdbc.user=dev
jdbc.password=ym68dev
 * @author juno
 *
 */	 
public class JdbcDrives{
	private String driver;
	private String url;
	private String user;
	private String password;
	public static final String MYSQL_DRIVER="com.mysql.jdbc.Driver";
	public static final String MYSQL_URL="jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
	public JdbcDrives(String driverClass,String url,String userName,String passWord){
		this.driver = driverClass;
		this.url = url;
		this.user = userName;
		this.password = passWord;
	}
	
	public JdbcDrives(String driverClass, String userName,String passWord){
		this.driver = driverClass; 
		this.user = userName;
		this.password = passWord;
	}
	/**设置mysql连接 jdbc:mysql://xiangin01.mysql.rds.aliyuncs.com:3306/xiangin_v2?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull*/
	public void setDatabase(String serverIp,int port,String dataBase){
		String url = String.format(MYSQL_URL, serverIp,port,dataBase);
		this.setUrl(url);			
	}
	
	
	
	private static final String ROOT_USER="root";
	private static String convertDatabaseCharsetType(String in, DataBaseType type) {  
        if (in == null || in.length()==0) {
        	return null;
        }
        
        String dbUser;
        switch (type) {
		case MySQL:
			dbUser = null; 
			break;
		case Oracle:
			dbUser = in.toUpperCase(); 
			break;
		case MsSQL:
			dbUser = null; 
			break;
		case PostgreSQL:
			 dbUser = "public"; 
			break;
		case DB2:
			 dbUser = in; 
			break;
		default:
			throw new YMException("不支持的数据类型:" + type); 
		}  
        return dbUser;  
    } 
	
	public static DataBaseType getDataBaseType(DatabaseMetaData md) throws SQLException{
		String driver = md.getDriverName().toUpperCase();
		DataBaseType type = null;
		//通过driverName是否包含关键字判断
        if (driver.indexOf("MYSQL") >=0) {  
            type = DataBaseType.MySQL;  
        } else if (driver.indexOf("SQL SERVER") >=0 || driver.indexOf("SQLSERVER")>=0) {  
            //sqljdbc与sqljdbc4不同，sqlserver中间有空格  
            type = DataBaseType.MsSQL;  
        }  
        return type;  
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}