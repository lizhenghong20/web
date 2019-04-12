package com.cangwu.frame.orm.core;

import java.io.Serializable;
 

/**
 * 字段名 不建议使用驼峰
 * 如果子类存在primary,在创建表示不要建id字段 {@link StructToDBMysql#setPrimaryColumn()}
 * @author juno
 */
public interface BaseBo extends Serializable {
    public Long getId() ;
    public void setId(Long id);
 
}
