package com.cangwu.frame.orm.ddl.generate.iface;

import com.cangwu.frame.orm.ddl.generate.CompareTableStruct;

public interface IAlterDDL {
	public StringBuilder alter(CompareTableStruct struct);
}
