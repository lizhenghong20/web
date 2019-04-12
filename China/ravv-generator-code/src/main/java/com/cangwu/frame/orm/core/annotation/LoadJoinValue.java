package com.cangwu.frame.orm.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 取得关联字段：根据某id，取得关联表的汉字描述<br/>
 * 1.有些关联字段并不是100%的场景都要加载，所以在bo层写这个标签，也可以在vo层使用这标签<br/>
 * 2.应用在set方法,关联值比较忽略大小写<br/>
 * 3.可以加载一个关联字段，也可以多，也可以把从表的所有记录都加载出来
 * 4.传入的是属性名(不是数据库的字段:区别是驼峰及下划线的转换)
 * @author juno
 * 构建的SQL： select returnfield from table where joinfield in (by1,by2,by3....);
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoadJoinValue {
	/** 本表依据的字段(已经加载到到内存的字段) */
	public String by() default "parentid";
	
	/** 来源表名 */
	public String table() default "";
	
	/** 关联来源字段(用属性名-带驼峰的那个) */
	public String joinfield()  default "id";
	
	/** 返回来源字段,可以空，默认去了set的当前方法名(当返回值为对象时才能空-代码可以根据对象的字段构成返回字段)*/
	public String returnfield() default "name";
	
	/** 把4个字段简写成: by=table(field,return) */
	public String get() default "";
	
	/** dao加载后是否马上加载:除了显示页面外,大部分情况都不需要自动加载的,设置为false可以提高性能*/
	public boolean autoload() default false;
}
