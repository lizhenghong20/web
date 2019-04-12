package com.cangwu.frame.orm.core.annotation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import cn.farwalker.waka.util.Tools;
import cn.farwalker.waka.util.YMException;

import com.cangwu.frame.orm.ddl.generate.DDLColumnAdapter;
import com.cangwu.frame.orm.ddl.generate.impl.ReverseStructForAnnotation;

/**
 * 关联字段
 * 构建SQL: select * from table where joinField in (by1,by2...)
 * @author juno
 */
public class RelationField implements Serializable{ 
    private static final long serialVersionUID = 5560173037071816372L;
    public static final String getFMT="by=table(joinField,return)";
    
    private String by;
	private String table;
	private String joinField;
	private String returnField;
	private boolean autoLoad;
	
	/**返回字段，多个以逗号分隔*/
	private String[] returnFields;
	/**是否返回列表*/
	private boolean returnCollection;
	/**泛型*/
	private Class<?> returnClazz;
	
	/**
	 * 构建SQL: select * from table where joinField in (by1,by2...)
	 * 
	 * @param by 依据字段
	 * @param table 来源表名
	 * @param joinField 关联字段
	 */
	public RelationField(LoadJoinValue j) {
		this.by = j.by();
		this.table = j.table();
		this.joinField = j.joinfield();
		this.returnField = j.returnfield();
		this.autoLoad  = j.autoload();
		returnCollection = false;
		parsetGet(j.get());
		
		if(Tools.string.isAnyEmpty(by,table,joinField)){
            throw new YMException("关联取值关键元素不能为空:关联字段(by,joinField),来源表名");
        }
        /**已转大写*/
        this.table = this.table.toUpperCase();
		
	}
	/**
	 * 
	 * @param by 依据字段
	 * @param table 来源表名
	 * @param joinField 关联字段
	 * @param returnFields 返回字段，可以为null
	 */
	public RelationField(String by,String table,String joinField,String returnFields) {
	    if(Tools.string.isAnyEmpty(by,table,joinField)){
            throw new YMException("关联取值关键元素不能为空:关联字段(by,joinField),来源表名");
        }
        
	    this.by = by;
	    /**已转大写*/
        this.table = this.table.toUpperCase();
        
        this.joinField = joinField;
        this.returnField = returnFields;
        this.autoLoad  = false;
        returnCollection = false;
	}
	
	/** get:把4个字段简写成: by=table(field,return) */
	private void parsetGet(String get){
	    if(Tools.string.isEmpty(get)){
            return ;
        }
	    int eq = get.indexOf('=');
	    if(eq<=0){
	        throw new YMException("LoadJoinValue#get的简写格式是" + getFMT);
	    }
	    this.by = get.substring(0,eq).trim();
	    
	    int lp = get.indexOf('(',eq+1);
	    if(lp<=0){
	        throw new YMException("LoadJoinValue#get的简写格式是" + getFMT);
	    }
	    this.table = get.substring(eq+1,lp).trim();
	    
	    int comma = get.indexOf(',',lp+1);
	    int rp = get.indexOf(')',lp+1);
	    if(rp <=0){
	        throw new YMException("LoadJoinValue#get的简写格式是" + getFMT);
	    }		    
	    if(comma>0){
	        this.joinField = get.substring(lp+1,comma);
	        this.returnField = get.substring(comma+1,rp);
	    }
	    else{ //没有返回字段
	        this.joinField = get.substring(lp+1,rp);
	    }
	     
	} 
	/**本表的依赖字段(已经查出来的字段)*/
	public String getBy() {
		return by;
	}
	
	/**已转大写*/
	public String getTable() {
		return table;
	}
	/**从表关联字段(来源表的字段)*/
	public String getJoinField() {
		return joinField;
	}
 
		/**返回字段的接收方法名(不包含set)*/
	private String methodName;
	/**返回字段的接收方法名(不包含set)*/
    public String getMethodName() {
        return methodName;
    }
    public void setMethodName(Method method) {
        String name =method.getName(); 
        methodName = name.substring(3);
        this.setReturnFields(method);
    }
    
    /**赋值的方法 set()*/
    private void setReturnFields(Method method){
        String name =method.getName(); 
        if(!name.startsWith("set") && method.getParameterCount()==1){
            throw new YMException("LoadJoinValue只能定义在set方法上,并且参数唯一:" + name);
        }
        
        Class<?> type = method.getParameterTypes()[0];
        if(String.class.equals(type) || type.isPrimitive() || type.isAssignableFrom(Date.class)){//基本类型
            if(Tools.string.isEmpty(returnField)){
                String fn = name.substring(3);
                this.returnField = fn;//.toUpperCase();
            }
            this.returnFields = new String[]{ this.returnField};//.toUpperCase()
        }
        else{
            returnClazz = type;
            if(Collection.class.isAssignableFrom(type)){
                returnClazz = Tools.bean.getGenericSinge(method, 2);
                returnCollection = true; 
            }
            List<DDLColumnAdapter> columns = ReverseStructForAnnotation.getColumns(returnClazz,false);
            int size = columns.size(),idx =0;
            this.returnFields = new String[size];
            
            for(DDLColumnAdapter d : columns){
                String c = d.getName();//.toUpperCase();
                this.returnFields[idx++] = c;    
            }
        }
    }
    /**返回字段(全大写字符)*/
    public String[] getReturnFields() {
        return returnFields;
    }
    
    /**返回值是否基本类型*/
    public boolean isPrimitive(){
        return (returnClazz == null);
    }
    /**返回值是否列表*/
    public boolean isCollection(){
        return  returnCollection  ;
    }
    public Class<?> getReturnClazz() {
        return returnClazz;
    }
    /**自动加载*/
    public boolean isAutoLoad() {
        return autoLoad;
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("set").append(getMethodName()).append("()#")
            .append(by).append('=').append(table).append('.').append(joinField).append('(');
        if(returnFields==null || returnFields.length==0 ){
            if(Tools.string.isNotEmpty(returnField)){
                sb.append(returnField).append(',');    
            }
        }
        else{
            for(int i =0 ;i < returnFields.length;i++){
                sb.append(returnFields[i]).append(',');
            }
        }
        
        int l = sb.length();
        if(sb.charAt(l-1)==','){
        	sb.setCharAt(l-1, ')');
        }
        else{
        	sb.append(')');
        }
        return sb.toString();
    }
}