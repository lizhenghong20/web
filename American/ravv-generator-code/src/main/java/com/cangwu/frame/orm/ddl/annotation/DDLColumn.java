package com.cangwu.frame.orm.ddl.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cangwu.frame.orm.ddl.generate.DDLColumnType;

/**
 * 列信息 
 * 如果是字符类型，默认长度是64
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DDLColumn {
	/** 列名(强烈要求与java字段相同) */
	String name() default "";
	/**是否主键(默认false)*/
	boolean primary() default false;
	
	/** 数据类型(默认根据java类型判断) */
	DDLColumnType type() default DDLColumnType.NULL;
	/**
	 * 注释
	 * @return
	 */
	String comment() default "";

	/**长度*/
	int length() default 64;
	
	/**是否允许空(true:允许)*/
	boolean nullable() default true;
}
