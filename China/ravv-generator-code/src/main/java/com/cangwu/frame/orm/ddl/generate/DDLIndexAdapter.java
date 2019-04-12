package com.cangwu.frame.orm.ddl.generate;

import com.cangwu.frame.orm.ddl.annotation.DDLIndex;
import com.cangwu.frame.orm.ddl.annotation.DDLIndex.IndexType;

/**
 * 数据库索引
 * DDLColumn 的实现 {@link DDLIndex} 
 * @author juno
 */  
public class DDLIndexAdapter {

	/**类型*/
	private IndexType type;

	/**索引名*/
	private String name;
	/**字段顺序*/
	private int  order;
	/**字段名(建议不设置,保持与数据库同步)*/
	private String fieldname ;
	
	public DDLIndexAdapter(DDLIndex idx){ 
		DDLIndexAdapter rs = this;
		rs.name = idx.name(); 
		rs.type =( idx.type() == null ?IndexType.Normal : idx.type());
		rs.order = idx.order();
		rs.fieldname = idx.fieldname();
	}
	
	public DDLIndexAdapter(String fieldname){ 
		DDLIndexAdapter rs = this;
		rs.fieldname = fieldname;  
	}
	 
	
	@Override
	public String toString() {
		return name + "-" + fieldname;
	}

	public IndexType getType() {
		return type;
	}

	public void setType(IndexType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getFieldname() {
		return fieldname;
	}

	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}
}
