package com.cangwu.frame.orm.ddl.generate.iface;

import java.util.Collection;

import com.cangwu.frame.orm.ddl.generate.DDLTableStruct;

/**
 * 对象还原
 * @author juno
 *
 */
public interface IReverseStruct {
	public Collection<DDLTableStruct> getTableStructs();
}
