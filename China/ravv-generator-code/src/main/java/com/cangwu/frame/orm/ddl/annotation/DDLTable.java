package com.cangwu.frame.orm.ddl.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 数据表
 * @author juno
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DDLTable {
	/**数据表名(为什么不默认：因为涉及表名前缀和biz也要定义TABLE_NAME，所以不默认) */
	 String name();
	 //String name() default null;
	 
	 /** 注释*/
	 String comment() default "" ;
}
