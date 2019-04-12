package com.cangwu.frame.web.crud;

import cn.farwalker.waka.util.Tools;

import com.cangwu.frame.orm.ddl.generate.DDLColumnType;

/** 查询条件对象
 * let queryFilter = [{ "field": "nickName", "startValue": search }
        , { "logic": "or", "field": "name", "startValue": search }]; 
 * var pars = {"query":JSON.stringify(queryFilter), "start": startPage || 1, size: 25 };
 * $.post("list.ac",pars,rs=>{});
 */
public class QueryFilter {
	public static enum Logic{
		NONE,AND,OR
	}
	private String logic;
	private String field;
	private String fieldtype;
	private String startValue,endValue;
	
 
	public Logic getLogicType(){
		if("and".equalsIgnoreCase(logic)){
			return Logic.AND;
		}
		else if("or".equalsIgnoreCase(logic)){
			return Logic.OR;
		}
		else{
			return Logic.NONE;
		} 
	}
	public boolean isEnabled(){
		if(Tools.string.isEmpty(field) || Tools.string.isAllEmpty(startValue,endValue)){
			return false;
		}
		else{
			return true;
		} 
	}
	public DDLColumnType getFieldTypes(){
		if(Tools.string.isEmpty(fieldtype)){
			return DDLColumnType.NULL;
		}
		else if(fieldtype.equalsIgnoreCase("enum")){
			return DDLColumnType.ENUMS; //不能精确还原
		}
		else if(fieldtype.equalsIgnoreCase("boolean")){
			return DDLColumnType.ENUMN; //不能精确还原
		}
		else if(fieldtype.equalsIgnoreCase("string")){
			return DDLColumnType.VARCHAR;
		}
		else if(fieldtype.equalsIgnoreCase("datetime") || fieldtype.equalsIgnoreCase("date")){
			return DDLColumnType.DATE;
		}
		
		DDLColumnType rs = null;
		DDLColumnType[] types = DDLColumnType.values();
		for(DDLColumnType t : types){
			if(t.name().equalsIgnoreCase(fieldtype)){
				rs = t;
				break;
			}
		}
		return rs;
	}
	/**{@link Logic}的name*/
    public String getLogic() {
        return logic;
    }
    /**{@link Logic}的name*/
    public void setLogic(String logic) {
        this.logic = logic;
    }
    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }
    /**特殊的类型:{enum|boolean|string|INTEGER}，其他参考 {@link DDLColumnType}的name*/
    public String getFieldtype() {
        return fieldtype;
    }
    /**特殊的类型:{enum|boolean|string|INTEGER}，其他参考 {@link DDLColumnType}的name*/
    public void setFieldtype(String fieldtype) {
        this.fieldtype = fieldtype;
    }
    public String getStartValue() {
        return startValue;
    }
    public void setStartValue(String startValue) {
        this.startValue = startValue;
    }
    public String getEndValue() {
        return endValue;
    }
    public void setEndValue(String endValue) {
        this.endValue = endValue;
    }
}
