package com.cangwu.frame.orm.ddl.generate.impl;

import com.cangwu.frame.orm.ddl.generate.util.GenerateUtils;

/**
 * 生成数据库语句
 * @author juno
 */
public abstract class StructToDB{
	private final StringBuilder builder = new StringBuilder(128);
	
	protected void println(String... text){
		GenerateUtils.println(builder, text);
	}
	protected void print(String... text){
		GenerateUtils.print(builder, text);
	}
	/**
	 * 删除最后的逗号或其他
	 * @param split 逗号或其他
	 * @return 删除的位置
	 */
	protected int deleteLastComma( char split){
		return GenerateUtils.deleteLastComma(builder, split);
	}
	/**
	 * 删除最后的逗号或其他
	 * @param split 逗号或其他
	 * @return 删除的位置
	 */
	protected int deleteLastComma(String split){
		return GenerateUtils.deleteLastComma(builder, split);
	}
	public StringBuilder getBuilder() {
		return builder;
	}

	protected void clear(){
		builder.setLength(0);
	}
}
