package com.cangwu.frame.web.crud;

import java.util.List;

/** 查询对象*/

public class QueryVo {
	/** 高级的查询*/
	private List<QueryFilter> filter;
	/** 快捷查询*/
	private QueryFilter queryFilter;
	
	/** 开始位置及记录数*/
	private Integer start,  size;
	/** 高级的查询*/
	public List<QueryFilter> getFilter() {
		return filter;
	}
	/** 高级的查询*/
	public void setFilter(List<QueryFilter> filter) {
		this.filter = filter;
	}
	/** 快捷查询*/
	public QueryFilter getQueryFilter() {
		return queryFilter;
	}
	/** 快捷查询*/
	public void setQueryFilter(QueryFilter queryFilter) {
		this.queryFilter = queryFilter;
	}
	/** 开始行位置*/
	public Integer getStart() {
		return start;
	}
	/** 开始行位置*/
	public void setStart(Integer start) {
		this.start = start;
	}
	/**取记录数*/
	public Integer getSize() {
		return size;
	}
	/**取记录数*/
	public void setSize(Integer size) {
		this.size = size;
	}
}
