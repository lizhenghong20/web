package com.cangwu.frame.orm.ddl.generate;

import java.util.List;
import java.util.Map;
 
public class DDLTableStruct {
	
	private String name;

	/** 注释 */
	private String comment;

	public DDLTableStruct(String name,String comment){
		this.name = name;
		this.comment = comment;
	}
	@Override
	public String toString() {
		return this.getName();
	}

	private List<DDLColumnAdapter> columns;
	private Map<String, List<DDLIndexAdapter>> indexs;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public List<DDLColumnAdapter> getColumns() {
		return columns;
	}
	public void setColumns(List<DDLColumnAdapter> columns) {
		this.columns = columns;
	}
	public Map<String, List<DDLIndexAdapter>> getIndexs() {
		return indexs;
	}
	public void setIndexs(Map<String, List<DDLIndexAdapter>> indexs) {
		this.indexs = indexs;
	}
}
