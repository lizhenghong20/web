package com.cangwu.frame.orm.ddl.generate.util;

import cn.farwalker.waka.util.Tools;
import cn.farwalker.waka.util.YMException;
 

public class GenerateUtils {
	/*public static final IOUtil io =IOUtil.util;
	public static final StringUtil string = StringUtil.util;
	public static final JsonUtil json = JsonUtil.util;*/

	public static final String CRLF="\r\n";
	/**4个空格*/
	public static final String SPLIT="    "; 
	
	public static final String C_JAVA_LANG="java.lang.";
    public static final String C_JAVA_UTIL="java.util."; 
    
	
	/**
	 * 索引的默认名称
	 * @param tableName
	 * @param fieldName
	 * @return
	 */
	public static String getIndexName(String tableName, String fieldName){
		 return "ix_" + tableName + "_" + fieldName;
	}
	
	
	public static String firstUpper(String s){ 
		if(Tools.string.isEmpty(s)){
			return "";
		}
		return Character.toUpperCase(s.charAt(0)) +s.substring(1);  
	}
	public static String firstLower(String s){ 
		if(Tools.string.isEmpty(s)){
			return "";
		}
		return Character.toLowerCase(s.charAt(0)) +s.substring(1);  
	}
	/**
	 * 删除最后的逗号或其他
	 * @param split 逗号或其他
	 * @return 删除的位置
	 */
	public static int deleteLastComma(StringBuilder sql, char split){
		//int idx = sql.lastIndexOf(",");
		int idx = -1;		
		for(int i = sql.length()-1;i >=0;i--){
			if(sql.charAt(i) == split){
				idx = i;
				break;
			}
		}
		if(idx <0){
			return idx;
		}
		String lasts = sql.substring(idx+1).trim();
		if(lasts.length()==0){//判断是空格回车等...
			sql.deleteCharAt(idx);
			return idx;
		}
		else{
			return -1;
		}
	}
	/**
	 * 删除最后的逗号或其他
	 * @param split 逗号或其他
	 * @return 删除的位置
	 */
	public static int deleteLastComma(StringBuilder sql,String split){
		int len =(split == null ?0 : split.length());
		if(len ==0){
			throw new YMException("分隔符不能为空");
		}
		else if(split.length()==1){
			return deleteLastComma(sql,split.charAt(0));
		}
		 
		int idx = sql.lastIndexOf(split);
		if(idx <0){
			return idx;
		}
		String lasts = sql.substring(idx + len).trim();
		if(lasts.length()==0){//判断是空格回车等...
			sql.delete(idx,idx+ len);
			return idx;
		}
		else{
			return -1;
		}
	} 
	
	public static void println(StringBuilder sql,String... text){
		for(String s : text){
			if(s!=null && s.length()>0){
				sql.append(s);
			}
		}
		sql.append(GenerateUtils.CRLF);
	}
	public static void print(StringBuilder sql,String... text){
		for(String s : text){
			if(s!=null && s.length()>0){
				sql.append(s);
			}
		}
	}
	
	/**下划线变驼峰*/
	public static String convertUnderlineHump(String tableName){
		if(tableName.indexOf('_')==-1){
			return tableName;
		}
		
		char[] ts = tableName.toCharArray();
		int l = ts.length-1;
		if(ts[l]=='_'){
			ts[l]=' ';
		}
		for(int i = l;i >0;i--){
			char c = ts[i],p = ts[i-1];
			if(p=='_'){
				ts[i-1] = Character.toUpperCase(c);
				for(int x = i;x <l;x++){
					ts[x]=ts[x+1];
				}
				ts[l]=' ';
			}
		}
		String s = new String(ts);
		return s.trim();
	}
	/**驼峰变下划线*/
	public static String convertHumpUnderline(String className){
		int l = (className==null ? 0: className.length());
		boolean convert = false;
		if(l<2){
			;//return className;
		}
		else{
			for(int i = 1; i < l;i++){
				char  c = className.charAt(i);
				if(c >='A' && c <='Z'){
					convert = true;
					break;
				}
			}
		}
		if(!convert){
			return className;
		}		
		///////////////////////////////////
		StringBuilder sb = new StringBuilder(l* 3/2);
		sb.append(className.charAt(0));
		for(int i = 1; i < l;i++){
			char  c = className.charAt(i);
			if(c >='A' && c <='Z'){
				sb.append('_').append(Character.toLowerCase(c));
			}
			else{
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
