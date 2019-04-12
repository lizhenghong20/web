package com.cangwu.frame.orm.ddl.consts;

import com.cangwu.frame.orm.ddl.generate.util.GenerateUtils;
/**
 * Farwalker 框架常量
 * @author Administrator
 *
 */
public interface ConstFarwalker {
	/** 创建时间的数据字段*/
    public String CreateTimeDB = "gmt_create";
    /** 创建时间的java字段*/
    public String CreateTimePro = GenerateUtils.convertUnderlineHump(CreateTimeDB) ;
    /**修改时间数据字段*/
    public String ModifiedTimeDB = "gmt_modified";
    /** 修改时间的java字段*/
    public String ModifiedTimePro = GenerateUtils.convertUnderlineHump(ModifiedTimeDB) ;
}
