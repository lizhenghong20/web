package com.cangwu.frame.orm.ddl.generate;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.farwalker.waka.util.Tools;
import cn.farwalker.waka.util.YMException;

import com.cangwu.frame.orm.ddl.generate.dialect.AlterDDLMysql;
import com.cangwu.frame.orm.ddl.generate.dialect.CreateDDLMysql;
import com.cangwu.frame.orm.ddl.generate.dialect.DataBaseType;
import com.cangwu.frame.orm.ddl.generate.dialect.JdbcDrives;
import com.cangwu.frame.orm.ddl.generate.dialect.ReverseStructForMysql;
import com.cangwu.frame.orm.ddl.generate.iface.IAlterDDL;
import com.cangwu.frame.orm.ddl.generate.iface.ICreateDDL;
import com.cangwu.frame.orm.ddl.generate.iface.IReverseStruct;
import com.cangwu.frame.orm.ddl.generate.impl.CreateJavaModelUtil;
import com.cangwu.frame.orm.ddl.generate.impl.ReverseStructForAnnotation;

/**
 * 生成数据库的DDL <br/>
 * 要放在依赖最末端（依赖最多才能引用所有的bo类）,所以本类需要业务模块启动
 * @author juno
 * 
 */
public class Generate {

	
	private final DataBaseType type;
	public Generate(){
		this(DataBaseType.MySQL);
	}
	public Generate(DataBaseType type){
		this.type = type;
	}
 
 
	/**
	 * 由java对象 产生 drop ,create table 语句的方法
	 * @param searchPackageName 搜索的包名(搜索DDLTable.class注解)
	 * @param outfile 输出文件,可以为空
	 * @return 产生的语句
	 */
	public StringBuilder createDataTable(String[] searchPackageName,String outfile){  
		ReverseStructForAnnotation tables = new ReverseStructForAnnotation(searchPackageName);
		Collection<DDLTableStruct> strutcs = tables.getTableStructs();
		StringBuilder sql = createDataTable(strutcs);
		if(Tools.string.isNotEmpty(outfile)){
			File file = new File(outfile);
			write(file, sql);
		}
		return sql;
	}
 
	private StringBuilder createDataTable(Collection<DDLTableStruct> strutcs){
		ICreateDDL ddl ;
		switch (type) {
		case MySQL:
			ddl = new CreateDDLMysql();
			break;

		default:
			throw new YMException("未支持其他类型:" + type);
		}
		StringBuilder sql = new StringBuilder();
		for(DDLTableStruct s : strutcs){
			StringBuilder text = ddl.create(s);
			sql.append(text);
		}
		return sql;
	}
	
	/**
	 * 修改原来的结构,对比原来的结构， 产生修改语句
	 * @param outfile 输出文件,可以为空 
	 * @return
	 */
	public StringBuilder alterDataTable(JdbcDrives driver,String[] searchPackageName,String outfile){
		Connection cnt = null;
		try{ 
			ReverseStructForAnnotation ran = new ReverseStructForAnnotation(searchPackageName);
			Collection<DDLTableStruct> newStructs = ran.getTableStructs();
			cnt =getConnection(driver);
			ReverseStructForMysql rdb = new ReverseStructForMysql(cnt);
			Collection<DDLTableStruct> oldStructs = rdb.getTableStructs();
			
			StringBuilder sql = alterDataTable(newStructs, oldStructs);
			if(Tools.string.isNotEmpty(outfile)){
				if(sql.length()==0){
					sql.append("/** 检查没有变化 */");
				}
				File file = new File(outfile);
				write(file, sql);
			}
			return sql;
		}
		catch(ClassNotFoundException | SQLException e){
			throw new YMException("打开数据库出错",e);
		}
		finally{
			Tools.io.close(cnt);
		}
		 
		
	}
	
	/**
	 * 修改原来的结构,对比原来的结构， 产生修改语句
	 * @return
	 */
	public StringBuilder alterDataTable(Collection<DDLTableStruct> newStructs,Collection<DDLTableStruct> oldStructs){
		List<CompareTableStruct> alters = new ArrayList<>();
		List<DDLTableStruct> creates = new ArrayList<>();
		
		for(DDLTableStruct newtable : newStructs){
			String tableName = newtable.getName();
			DDLTableStruct oldtable = findTableStruct(oldStructs, tableName);
			if(oldtable == null){
				creates.add(newtable);
			}
			else {
				CompareTableStruct e = new CompareTableStruct(newtable,oldtable);
				if(!e.match()){
					alters.add(e);
				}
			}
		} 
		
		///////////////////////////////////////////////
		StringBuilder sql = createDataTable(creates);
		StringBuilder sql2 = alterDataTable(alters);
		sql.append(sql2);
		return sql;
	}
	private StringBuilder alterDataTable(List<CompareTableStruct> alters){
		IAlterDDL ddl ;
		switch (type) {
		case MySQL:
			ddl = new AlterDDLMysql();
			break;

		default:
			throw new YMException("未支持其他类型:" + type);
		}
		StringBuilder sql = new StringBuilder();
		for(CompareTableStruct s : alters){
			StringBuilder text = ddl.alter(s);
			sql.append(text);
		}
		return sql;
	}

	
	private DDLTableStruct findTableStruct(Collection<DDLTableStruct> tbs,String tableName){
		DDLTableStruct rs = null;
		for(DDLTableStruct st : tbs){
			if(tableName.equalsIgnoreCase(st.getName())){
				rs = st;
				break;
			}
		}
		return rs;
	}
	public void reverseDatabaseObject(JdbcDrives driver,String defaultPackage, String outdirs){
		this.reverseDatabaseObject(driver, defaultPackage, outdirs, null);
	}
	/**
	 * 逆向还原java对象(通过数据库的表结构生产java类)
	 * @param driver
	 * @param defaultPackage 包名，默认com.cangwu.facedesign.service
	 * @param outdir 输出目录,默认d:/temp
	 */
	public void reverseDatabaseObject(JdbcDrives driver,String defaultPackage, String outdirs,String[] prefixs){
		if(prefixs == null){
			prefixs = new String[]{};
		}
		else{
			for(int i =0 ;i < prefixs.length;i++){
				String p = prefixs[i].toLowerCase();
				prefixs[i]= p;
			}
		}
		File outdir ;
		if(Tools.string.isEmpty(outdirs)){
			outdir =new File("d:/temp");
		}
		else{
			outdir = new File(outdirs);
		}
		if(Tools.string.isEmpty(defaultPackage)){
			defaultPackage ="com.cangwu.facedesign.service";
		}
		Connection cnt = null;
		try{
            cnt =getConnection(driver);
            IReverseStruct rs = new ReverseStructForMysql(cnt);
            Collection<DDLTableStruct> tbs = rs.getTableStructs();
            //this.removePrefix(tbs,prefixs);

            //SQL语句
    		StringBuilder sql = createDataTable(tbs);
    		File file = new File(outdir,"database3.sql");
    		write(file, sql);
    		
    		{//删除已存在的包
	    		int idx = defaultPackage.indexOf('.');
	    		String firstPn = defaultPackage.substring(0,idx);
	    		File fp = new File(outdir,firstPn);
	    		if(fp.exists()){
	    			fp.delete();
	    		}
    		}
    		//java对象
    		for(DDLTableStruct s : tbs){
    			CreateJavaModelUtil reverse = new CreateJavaModelUtil(prefixs);
    			StringBuilder models = reverse.createModel(s, defaultPackage);
    			
    			String pn = reverse.getPackageName().replace('.', '/').replaceAll("_", "");
    			File fp = new File(outdir,pn);
    			if(!fp.exists()){//放到各自的包太麻烦了
    				fp.mkdirs();
    			}
    			File fn = new File(fp,reverse.getClassName() + ".java");
    			write(fn, models);
    		}
		}
		catch(ClassNotFoundException | SQLException e){
			throw new YMException("打开数据库出错",e);
		}
		finally{
			Tools.io.close(cnt);
		}
	}
	
	private static Connection getConnection(JdbcDrives driver) 
			throws ClassNotFoundException,SQLException {
		// 注册 JDBC 驱动
        Class.forName(driver.getDriver());
        Connection cnt = DriverManager.getConnection(driver.getUrl(),driver.getUser(),driver.getPassword());
        return cnt;
	}
	
	private static boolean write(File file, CharSequence sql) {
		
		Writer os = null;
		try{
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			os = new PrintWriter(file,"UTF-8");
			os.write(sql.toString());
			//System.out.println("********************************");
			System.out.print(file.getAbsolutePath() + ",");
			//System.out.println("********************************");
			return true;
		}
		catch(IOException e){
			throw new YMException("写SQL文件出错",e);
		}
		finally{
			Tools.io.close(os);
		}
	}
}
