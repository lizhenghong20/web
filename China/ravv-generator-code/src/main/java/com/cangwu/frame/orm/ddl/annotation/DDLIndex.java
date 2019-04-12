package com.cangwu.frame.orm.ddl.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 索引信息
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DDLIndex {
	/**索引类型(普通或唯一)*/
	public enum IndexType{
		Normal,Unique
	}
	/**索引类型(普通或唯一)*/
	IndexType type() default IndexType.Normal;
	
	/**索引 默认ix_table_field,名称相同则合拼一个索引 */
	String name() default "";
	
	/**字段名(建议不设置,保持与数据库同步)*/
	String fieldname() default "";
	
	/**复合索引时的顺序*/
	int order() default 0;
	
}
