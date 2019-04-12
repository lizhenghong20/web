package com.cangwu.frame.orm.ddl.generate.dialect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.cangwu.frame.orm.ddl.generate.DDLColumnAdapter;
import com.cangwu.frame.orm.ddl.generate.DDLIndexAdapter;
import com.cangwu.frame.orm.ddl.generate.DDLTableStruct;
import com.cangwu.frame.orm.ddl.generate.iface.ICreateDDL;
import com.cangwu.frame.orm.ddl.generate.impl.StructToDB;

/**
drop table if exists `fd_facemodel`;
		CREATE TABLE `fd_facemodel` (
		  `id` varchar(64) NOT NULL,
		  `name` varchar(60) DEFAULT NULL COMMENT '名称',
		  `facetype` varchar(30) DEFAULT NULL COMMENT '类型',
		  `enabled` int(255) DEFAULT NULL COMMENT '有效',
		  `requireno` varchar(50) DEFAULT NULL COMMENT '需求编号',
		  `clazz` varchar(255) DEFAULT NULL COMMENT '包名+类名',
		  `url` varchar(255) DEFAULT NULL COMMENT '访问地址',
		  `remark` varchar(255) DEFAULT NULL COMMENT '模块说明',
		  INDEX `ix_faceModel_abc` (`clazz`, `url`) ,
		  PRIMARY KEY (`id`)
		) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='界面模块主表'; 
 *
 */

public class CreateDDLMysql extends StructToDB implements ICreateDDL{
	private static final String TAB=DDLUtils.TAB;
	@Override
	public StringBuilder create(DDLTableStruct table) {
		this.clear();
		List<DDLColumnAdapter> columns = table.getColumns();
		DDLUtils.setPrimaryColumn(columns);
		DDLUtils.sortColumns(columns);
		
		println("-- ",table.getName()," - ",table.getComment());
		doDrop(table);
		doCreateTableBegin(table);
		doCreateColumns(columns); //返回逗号结尾
		doCreatePrimaryKey(columns);//返回逗号结尾
		doCreateIndex(table);//返回逗号结尾
		doCreateTableEnd(table);
		println();
		return getBuilder();
	}
	
	
	/**drop table if exists getTableName();*/
	private void doDrop(DDLTableStruct table){
		println("drop table if exists " , table.getName() , ";");
	}
	/**CREATE TABLE getTableName() (;*/
	private void doCreateTableBegin(DDLTableStruct table){
		println("CREATE TABLE ",table.getName()," (");
	}
	/**) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='界面模块主表'; */
	private void doCreateTableEnd(DDLTableStruct table){
		deleteLastComma(',');
		println(") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='" ,table.getComment(),"'; ");
	}
	/**
	 返回逗号结尾<br/>
	 PRIMARY KEY (`id`,....),*/
	private void doCreatePrimaryKey(List<DDLColumnAdapter> columns){
		DDLUtils.setPrimaryColumn(columns);
		List<DDLColumnAdapter> pks = new ArrayList<>();
		for(int i =0,size = columns.size();i <size ;i++){
			DDLColumnAdapter column = columns.get(i);		
			if(column.isPrimary()){
				pks.add(column);
			}
		}
		if(pks.isEmpty() ){
			return ;
		}
		////////////可以考虑加入排序，但复合主键的几率很小，没必要把标签搞得太复杂///////////////
		print(TAB + "PRIMARY KEY (");
		for(DDLColumnAdapter p:pks){
			print(p.getName(),",");
		}
		deleteLastComma(',');
		println("),");
	}
	/** 
	 返回逗号结尾<br/>
	id varchar(64) NOT NULL,
	 `name` varchar(60) DEFAULT NULL COMMENT '名称',*/
	private void doCreateColumns(List<DDLColumnAdapter> columns){ 
		//for(DDLColumnAdapter column : columns){
		for(int i =0,size = columns.size();i <size ;i++){
			DDLColumnAdapter column = columns.get(i);
			StringBuilder sc = DDLUtils.getColumnDDL(column);
			println(TAB,sc.toString(),",");
		}
	}
	
	
	/**
	 * 返回逗号结尾<br/>
	 * INDEX `ix_faceModel_abc` (`clazz`, `url`) ,*/
	private void doCreateIndex(DDLTableStruct table){
		Map<String,List<DDLIndexAdapter>> mapIdx = table.getIndexs();
		for(Map.Entry<String,List<DDLIndexAdapter>> e : mapIdx.entrySet()){
			String indexName = e.getKey();
			List<DDLIndexAdapter> fds = e.getValue();
			StringBuilder sb = DDLUtils.doIndexCreate(indexName,fds);
			print(sb.toString());
		}
	}

}
