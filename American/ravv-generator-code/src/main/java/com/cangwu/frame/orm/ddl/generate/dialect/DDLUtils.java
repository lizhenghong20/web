package com.cangwu.frame.orm.ddl.generate.dialect;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.farwalker.waka.core.WakaObject;
import cn.farwalker.waka.util.Tools;

import com.cangwu.frame.orm.core.IBaseDao;
import com.cangwu.frame.orm.ddl.annotation.DDLIndex.IndexType;
import com.cangwu.frame.orm.ddl.generate.DDLColumnAdapter;
import com.cangwu.frame.orm.ddl.generate.DDLColumnType;
import com.cangwu.frame.orm.ddl.generate.DDLIndexAdapter;
import com.cangwu.frame.orm.ddl.generate.util.GenerateUtils;

public class DDLUtils {

	public static final String TAB="    ";
	/** 检查并设置默认主键 */
	public static void setPrimaryColumn(List<DDLColumnAdapter> columns) {
		boolean exitPrimary = false;
		DDLColumnAdapter columnId = null;// PRIMARY_KEY
		for (DDLColumnAdapter column : columns) {
			if (IBaseDao.PRIMARY_KEY.equalsIgnoreCase(column.getName())) {
				columnId = column;
			} else if (column.isPrimary()) {
				exitPrimary = true;// 必须优先判断字段名
			}
		}

		// 存在id字段的情况判断
		if (columnId != null) {
			if (exitPrimary) { // primary重复了
				columns.remove(columnId);
			} else {// 存在id，并且没有主键
				columnId.setPrimary(true);
			}
		}
	}

	public static List<DDLColumnAdapter> sortColumns(List<DDLColumnAdapter> columns){
		WakaObject<DDLColumnAdapter> columnId = new WakaObject<>();
		Collections.sort(columns ,new Comparator<DDLColumnAdapter>() {
			public int compare(DDLColumnAdapter o1, DDLColumnAdapter o2){
				String name1 = o1.getName();
				if(name1.equalsIgnoreCase(IBaseDao.PRIMARY_KEY)){
					columnId.setValue(o1);
					return 0;
				}
				else{
					return name1.compareToIgnoreCase(o2.getName());
				}
			};//rdsx8t1ufi6ezjdaam2guo.mysql.rds.aliyuncs.com
		});
		if(columnId.getValue() != null){
			DDLColumnAdapter ids = columnId.getValue();
			int idx =columns.indexOf(ids);
			if(idx>0){
				columns.remove(idx);
				columns.add(0, ids);
			}
		}
		return columns;
	}
	/**
	 * 返回逗号结尾<br/>
	 * 
	 * @param indexName
	 * @param fields
	 * @return
	 */
	public static StringBuilder doIndexCreate(String IndexName,
			List<DDLIndexAdapter> fields) {
		String idxName =  GenerateUtils.convertHumpUnderline(IndexName);
		StringBuilder sb = new StringBuilder();
		DDLIndexAdapter first = fields.get(0);
		String INDEX = (first.getType() == IndexType.Unique ? "UNIQUE INDEX "
				: "INDEX ");
		GenerateUtils.print(sb, TAB, INDEX, idxName, "(");

		for (DDLIndexAdapter idx : fields) {
			GenerateUtils.print(sb, idx.getFieldname(), ",");
		}
		GenerateUtils.deleteLastComma(sb, ',');
		GenerateUtils.println(sb, "),");
		return sb;
	}
	
	/**
	 * 取得列的DDL语句,不带逗号结束
	 * @param c
	 * @return
	 */
	public static StringBuilder getColumnDDL(DDLColumnAdapter c){
		//所有关键字的前面留空格
		DDLColumnType type = c.getType();
		String columnTypes = type.getTypeSQL(c.getLength());
		
		String nullable =(c.isPrimary() || !c.isNullable() ?" NOT NULL":" DEFAULT NULL");
		String comment = (Tools.string.isEmpty(c.getComment()) ? "":" COMMENT '" + c.getComment() + "'");
		StringBuilder sb = new StringBuilder();
		sb.append(c.getName()).append(columnTypes).append(nullable).append(comment);//.append(",");
		return sb;
	}
}
