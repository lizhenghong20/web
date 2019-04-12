package com.cangwu.frame.orm.query;

import cn.farwalker.waka.util.Tools;

/**
 * 分页条件. 生成的sql： limit (pageNo-1)*pageSize , pageSize<br>
 * 1为起始页
 */
public class PageParam {//implements IPageParam {
    //private static final long serialVersionUID = 5124239712991420217L;
	/**
	 * 页面大小
	 */
	public int DEFAULT_PAGE_SIZE = 15;

	/**
	 * 第几页
	 */
	public int DEFAULT_PAGE_NUM = 1;
	
    private int pageSize;

	private int pageNum;

	private int startRowIndex;
	
	private int offset;

	public PageParam() {
		//this(DEFAULT_PAGE_SIZE, DEFAULT_PAGE_NUM);
		setPageSize(DEFAULT_PAGE_SIZE);
		setPageNum(DEFAULT_PAGE_NUM);
	}
	/**
	 * 
	 * @param pageSize 页大小
	 * @param pageNum 页码,1为起始页
	 */
	public PageParam(Integer pageSize, Integer pageNum) {
        setPageSize(Tools.number.nullIf(pageSize,DEFAULT_PAGE_SIZE));
        setPageNum(Tools.number.nullIf(pageNum,DEFAULT_PAGE_NUM));
    }
	/**
	 * 
	 * @param pageSize 页大小 
	 * @param pageNum 页码,1为起始页
	 */
	public PageParam(int pageSize, int pageNum) {
		setPageSize(pageSize);
		setPageNum(pageNum);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		startRowIndex = -1;
		this.pageSize = pageSize;
	}
	/**页码：由1开始，第一页是"1" */

	public int getPageNum() {
		return pageNum;
	}
	/**页码：由1开始，第一页是"1" */
	public void setPageNum(int pageNum) {
		startRowIndex = -1;
		this.pageNum = pageNum;
	}

	public int getStartRowIndex() {
		if (startRowIndex == -1) {
			int size = (pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize);
			int pno = (pageNum <= 1 ? 0 : pageNum - 1);
			startRowIndex = (pno <= 0 ? 0 : pno * size) + offset;
		}

		return startRowIndex;
	}

	/** 偏移量(在列表额外增加行的时候有用)*/
    public int getOffset() {
        return offset;
    }

    /** 偏移量(在列表额外增加行的时候有用)*/
    public void setOffset(int offset) {
        this.offset = offset;
    }

}
