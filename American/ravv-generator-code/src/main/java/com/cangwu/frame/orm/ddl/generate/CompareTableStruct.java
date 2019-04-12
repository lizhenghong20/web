package com.cangwu.frame.orm.ddl.generate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.farwalker.waka.util.Tools;
import cn.farwalker.waka.util.YMException;

import com.cangwu.frame.orm.ddl.generate.dialect.DDLUtils;

/** 比较两个结构,<br/>
 * 比较时会，如果没有主键字段，会默认设置 */
public class CompareTableStruct {

	private final DDLTableStruct newStruct, oldStruct;
	private final List<DDLColumnAdapter> addColumns, modifyColumns;
	/** 删除的列不一样处理:需要旧结构的副本 */
	private final List<DDLColumnAdapter> dropColumns;

	private final Map<String, List<DDLIndexAdapter>> addIndex, dropIndex;
	
	/**是否修改了注释*/
	private String tableComment;
	/**
	 * 
	 * @param newStruct
	 * @param oldStruct
	 */
	public CompareTableStruct(DDLTableStruct newStruct, DDLTableStruct oldStruct) {		
		if(!newStruct.getName().equalsIgnoreCase(oldStruct.getName())){
			throw new YMException("数据表名不匹配:" +newStruct.getName() + "," + oldStruct.getName());
		}
		
		this.newStruct = newStruct;
		this.oldStruct = oldStruct;
		List<DDLColumnAdapter> oldColumns =  oldStruct.getColumns();
		List<DDLColumnAdapter> thisColumns = newStruct.getColumns(); 
		DDLUtils.setPrimaryColumn(oldColumns);
		DDLUtils.setPrimaryColumn(thisColumns);
		
		int size = (oldColumns.size() / 2);
		if (size < 3) {
			size = 3;
		}
		addColumns = new ArrayList<>(size / 2);
		modifyColumns = new ArrayList<>(size / 2);
		dropColumns = new ArrayList<>(oldColumns);

		int idxs = oldStruct.getIndexs().size();
		if (idxs < 2) {
			size = 2;
		}
		addIndex = new HashMap<>(idxs);
		dropIndex = new HashMap<>(idxs);	 
		
		this.compareColumn();
		this.compareIndex();
		this.compareTableComment();
	}

	private void compareTableComment() {
		String newComment = newStruct.getComment(),oldComment = oldStruct.getComment();
		if(Tools.string.isAllEmpty(newComment ,oldComment) || newComment.equalsIgnoreCase(oldComment)){
			this.tableComment = "";
		}
		else{
			this.tableComment = newComment;
		}
	}
	
	/**
	 * 比较列结构 
	 * @param thisColumns 新结构
	 * @param oldColumns 旧结构
	 * @return
	 */
	private void compareColumn() {
		List<DDLColumnAdapter> newColumns = newStruct.getColumns();
		List<DDLColumnAdapter> oldColumns = oldStruct.getColumns();

		for (DDLColumnAdapter c : newColumns) {
			DDLColumnAdapter oldcol = findColumn(oldColumns, c.getName());
			if (oldcol == null) {//
				// doAlterColumn(c,1);
				addColumns.add(c);
				continue;
			}

			if (oldcol.getType() != c.getType()
					|| !oldcol.getComment().equalsIgnoreCase(c.getComment())
					|| oldcol.getLength() != c.getLength()
					|| oldcol.isNullable() != c.isNullable()) {
				// doAlterColumn(c,2);
				modifyColumns.add(c);
			}
			dropColumns.remove(oldcol); // 删除修改的元素
		}
	}

	/**
	 * 比较列结构
	 * 
	 * @param thisColumns
	 *            新结构
	 * @param oldColumns
	 *            旧结构
	 * @return
	 */
	private void compareIndex() {
		Map<String, List<DDLIndexAdapter>> newIndexs = newStruct.getIndexs(), oldIndexs = oldStruct
				.getIndexs();

		for (Map.Entry<String, List<DDLIndexAdapter>> e : newIndexs.entrySet()) {
			String indexName = e.getKey();
			List<DDLIndexAdapter> oldfield = oldIndexs.get(indexName);
			if (oldfield == null) {
				addIndex.put(indexName, e.getValue());
			} else {
				StringBuilder newIndex = DDLUtils.doIndexCreate(indexName, e.getValue());
				StringBuilder oldIndex = DDLUtils.doIndexCreate(indexName, oldfield);

				// 先删，再增
				if (!newIndex.toString().equalsIgnoreCase(oldIndex.toString())) {
					dropIndex.put(indexName, oldfield);
					addIndex.put(indexName, e.getValue());
				}
			}
		}
	}

	private DDLColumnAdapter findColumn(Collection<DDLColumnAdapter> tbs,
			String columnName) {
		DDLColumnAdapter rs = null;
		for (DDLColumnAdapter st : tbs) {
			if (columnName.equalsIgnoreCase(st.getName())) {
				rs = st;
				break;
			}
		}
		return rs;
	}

	/** 添加的列 */
	public List<DDLColumnAdapter> getAddColumns() {
		return addColumns;
	}


	/** 修改的列 */
	public List<DDLColumnAdapter> getModifyColumns() {
		return modifyColumns;
	}

	/** 删除的列 */
	public List<DDLColumnAdapter> getDropColumns() {
		return dropColumns;
	}
 
	public DDLTableStruct getNewStruct() {
		return newStruct;
	}
 
	public DDLTableStruct getOldStruct() {
		return oldStruct;
	}

	

	/** 是否匹配 */
	public boolean match() {
		int size = addColumns.size() + modifyColumns.size()
				+ dropColumns.size() + addIndex.size() + dropIndex.size() 
				+ tableComment.length();
		return (size>0 ? false:true);
	}

	/** 添加的索引 */
	public Map<String, List<DDLIndexAdapter>> getAddIndex(){
		return this.addIndex;
	}

	/** 删除的索引 */
	public Map<String, List<DDLIndexAdapter>> getDropIndex(){
		return this.dropIndex;
	}

	@Override
	public String toString() {
		DDLTableStruct s =( newStruct == null ? oldStruct : newStruct);
		return s.toString();
	}
	/**是否修改了注释*/
	public String getTableComment() {
		return tableComment;
	}
	
}