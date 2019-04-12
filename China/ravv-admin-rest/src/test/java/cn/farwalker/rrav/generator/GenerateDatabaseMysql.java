package cn.farwalker.rrav.generator;

import java.io.IOException;

import com.cangwu.frame.orm.ddl.generate.Generate;
import com.cangwu.frame.orm.ddl.generate.dialect.JdbcDrives;

/**
 * 1.由javaBo创建数据库
 * 2.比较bo与数据库的差异
 * @author juno
 *
 */
public class GenerateDatabaseMysql {
	private static final String[] D_SearchPackage ={"cn.farwalker.ravv.service","com.cangwu.frame.components"};
	private static final String[] prefixs={};
	private static final String jdbcURL = String.format(JdbcDrives.MYSQL_URL,"120.77.217.189",3306,"ravv");
	private static final String jdbcUser ="ravv" ,jdbcPwd="Ravv1688";
//	private static final String jdbcURL = String.format(JdbcDrives.MYSQL_URL,"39.105.22.254",3306,"ravv");
//	private static final String jdbcUser ="ravv" ,jdbcPwd="ravv";
	private static final String D_OutFile ="d:/temp/";
	public static void main(String[] args){//|com.cangwu.frame.components3
		int type = readType();
		switch (type) {
		case 1:
			createDataTable();
			break;
		case 2://较bo与数据库的差异
			alterDataTable();
			break;
		case 3://通过数据库的表结构生产java类
			reverseObject();
			break; 
		default:
			System.err.println("不能识别的类型:" + type);
			break;
		}
	}

	/**比较bo与数据库的差异*/
	private static void alterDataTable(){
		JdbcDrives driver = getJdbcDrivers();
		Generate ddl = new Generate();
		ddl.alterDataTable(driver, D_SearchPackage, D_OutFile + "alter.sql");
	}
	private static void createDataTable(){
		Generate ddl = new Generate();
		ddl.createDataTable(D_SearchPackage, D_OutFile + "create.sql");
	}
	
	private static void reverseObject(){
		
		JdbcDrives driver = getJdbcDrivers();
		Generate g = new Generate();
		g.reverseDatabaseObject(driver, D_SearchPackage[0], D_OutFile,prefixs);
	}
	
	private static JdbcDrives getJdbcDrivers(){
		//String url = String.format(JdbcDrives.MYSQL_URL,"yomnn.xicp.io",3306,"myexample");
	    
		//String url = "jdbc:mysql://rdsx8t1ufi6ezjdaam2guo.mysql.rds.aliyuncs.com:3306/toupiao?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
		JdbcDrives driver = new JdbcDrives(JdbcDrives.MYSQL_DRIVER,jdbcURL, jdbcUser,jdbcPwd);
		return driver;
	} 
	private static int readType(){
		System.out.println("请输入数据结构处理类型:1-创建表,2-修改,3-逆向创建java对象");
		int rs =1;
		try{
			int in = System.in.read();
			rs  =in - (int)'0';
		}
		catch(IOException e){
			System.err.println(e.getMessage());
		}
		return rs;
	}
}
