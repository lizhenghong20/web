package com.cangwu.frame.orm.ddl.generate.dialect;

import java.util.List;
import java.util.Map;

import cn.farwalker.waka.util.Tools;

import com.cangwu.frame.orm.ddl.generate.CompareTableStruct;
import com.cangwu.frame.orm.ddl.generate.DDLColumnAdapter;
import com.cangwu.frame.orm.ddl.generate.DDLIndexAdapter;
import com.cangwu.frame.orm.ddl.generate.iface.IAlterDDL;
import com.cangwu.frame.orm.ddl.generate.impl.StructToDB;
import com.cangwu.frame.orm.ddl.generate.util.GenerateUtils;

public class AlterDDLMysql extends StructToDB implements IAlterDDL{ 
	private static final String SPLIT=GenerateUtils.SPLIT;
	@Override
	public StringBuilder alter(CompareTableStruct table) {
		this.clear();
 		String tableName = table.getNewStruct().getName();
		if (table.match()) {
			println("/* 没有修改:",tableName,"*/" + GenerateUtils.CRLF );
		} else {

			println("ALTER TABLE ", tableName, " ");
			doAlterColumnToString(table);
			doAlterIndexToString(table);
			doAlterTableComment(table.getTableComment());

			deleteLastComma(GenerateUtils.CRLF);
			deleteLastComma(',');
			println(";");
		}

		return getBuilder();
	}
	
	/**
	 * 修改列 ALTER TABLE `cw_visitcontrol` ADD COLUMN `remark` varchar(255) NULL
	 * COMMENT '备注', MODIFY COLUMN `referer` varchar(100) NULL COMMENT '来源地址'
	 * 
	 * @param tc
	 */
	private void doAlterColumnToString(CompareTableStruct table) {
		for (DDLColumnAdapter c : table.getDropColumns()) {
			//StringBuilder ddl = DDLUtils.getColumnDDL(c);
			println(SPLIT + "DROP COLUMN ", c.getName() ,",");
		}
		for (DDLColumnAdapter c : table.getAddColumns()) {
			StringBuilder ddl = DDLUtils.getColumnDDL(c);
			println(SPLIT + "ADD COLUMN ", ddl.toString(),",");
		}
		for (DDLColumnAdapter c : table.getModifyColumns()) {
			StringBuilder ddl = DDLUtils.getColumnDDL(c);
			println(SPLIT + "MODIFY COLUMN ", ddl.toString(),",");
		}
	}

	/**
	 * ALTER TABLE `cw_roleuser` DROP INDEX `IX_ROULEID_USERID`, ADD INDEX
	 * `ix_user_id` (`userid`) ;
	 */
	protected void doAlterIndexToString(CompareTableStruct table) {
		for (Map.Entry<String, List<DDLIndexAdapter>> e : table.getDropIndex().entrySet()) {
			println(SPLIT + "DROP INDEX ", e.getKey(), ",");
		}
		for (Map.Entry<String, List<DDLIndexAdapter>> e : table.getAddIndex().entrySet()) {
			StringBuilder sb = DDLUtils.doIndexCreate(e.getKey(), e.getValue());
			println(SPLIT, sb.toString());
		}
	}
	
	/**
	 * ALTER TABLE `fd_modeljiekou`
	 * MODIFY COLUMN `packagename`  varchar(64) DEFAULT NULL COMMENT '接口包名' ,
	 * COMMENT='界面模块从表';
	 * @param comment
	 */
	private void doAlterTableComment(String comment){
		if(Tools.string.isNotEmpty(comment)){
			println(SPLIT + "COMMENT='", comment, "',");
		}
	}
}
