package com.cangwu.frame.orm.ddl.generate.iface;

import com.cangwu.frame.orm.ddl.generate.DDLTableStruct;


/**创建语句*/
public interface ICreateDDL {
	public StringBuilder create(DDLTableStruct struct);
}
