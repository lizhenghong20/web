package com.cangwu.frame.orm.ddl.generate;

import cn.farwalker.waka.util.Tools;
import cn.farwalker.waka.util.YMException;

import com.cangwu.frame.orm.ddl.annotation.DDLColumn;

/**
 * 数据库字段
 * DDLColumn 的实现 {@link DDLColumn} 
 * @author juno
 */ 
public class DDLColumnAdapter {
	
	/** 列名(强烈要求与java字段相同) */
	private String name ;
	/**是否主键(默认false)*/
	private boolean primary =false;
	
	/** 数据类型(默认根据java类型判断) */
	private DDLColumnType type ;
	/**
	 * 注释
	 * @return
	 */
	private  String comment = "";

	/**长度*/
	private int length  = DDLColumnType.Length;
	
	/**是否允许空(true:允许)*/
	private boolean nullable  =true;
	
	public DDLColumnAdapter(DDLColumn column){ 
		DDLColumnAdapter rs = this;
		rs.name = column.name();
		rs.primary = column.primary(); 
		rs.type = (column.type() == null ? DDLColumnType.NULL : column.type());
		rs.comment = column.comment(); 
		
		rs.nullable = column.nullable();
		rs.length = column.length(); 
	}
	/**
	 * 列名及类型是必须的
	 * @param column
	 * @param sqlType 参考{@link java.sql.Types}的类型
	 */
	public DDLColumnAdapter(String columnName,DDLColumnType sqlType){  
		this.name = columnName;
		this.type = sqlType;
	}
	
	public void setPrimary(boolean primary) {
		this.primary = primary;
		if(primary){
			nullable = false;
		}
	}
	public void setNullable(boolean nullable) {
		if(primary && nullable){
			throw new YMException("主键字段不允许为空");
		}
		this.nullable = nullable;
	}
	@Override
	public boolean equals(Object obj) { 
		if(obj == null){
			return false;
		}
		else if(obj == this){
			return true;
		}
		else if(!(obj instanceof DDLColumnAdapter)){
			return false;
		}
		
		String n1 = this.getName(),n2 = ((DDLColumnAdapter)obj).getName();
		if (Tools.string.isAnyEmpty(n1,n2)){
			return false;
		}
		return n1.equalsIgnoreCase(n2);
	}
	@Override
	public String toString() {
		return name + "-" + comment;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DDLColumnType getType() {
		return type;
	}
	public void setType(DDLColumnType type) {
		this.type = type;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public boolean isPrimary() {
		return primary;
	}
	public boolean isNullable() {
		return nullable;
	}
}
