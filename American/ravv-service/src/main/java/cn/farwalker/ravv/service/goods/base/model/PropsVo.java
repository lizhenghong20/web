package cn.farwalker.ravv.service.goods.base.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.farwalker.ravv.service.category.property.model.BaseCategoryPropertyBo;
import cn.farwalker.ravv.service.category.value.model.BaseCategoryPropertyValueBo;
import cn.farwalker.ravv.service.goods.constants.PropsUserInputEnum;
import cn.farwalker.waka.util.Tools;

/**
 * 普通属性
 * @author Administrator
 * 普通属性，3部分组成：属性id、输入类型、值
 */
public class PropsVo {
	private Long  proid;
	/**属性名称*/
	private String propertyName;
	private Long  valueid;
	private PropsUserInputEnum inputType;
	/**静态的显示值*/
	private String value;
	
	/** 属性id，{@link BaseCategoryPropertyBo#getId()}*/
	public Long getProid() {
		return proid;
	}
	/** 属性id，{@link BaseCategoryPropertyBo#getId()}*/
	public void setProid(Long proid) {
		this.proid = proid;
	}
	
	/** 属性值id，-1表示自定义 {@link BaseCategoryPropertyValueBo#getId()}*/
	public Long getValueid() {
		return valueid;
	}
	/** 属性值id，-1表示自定义 {@link BaseCategoryPropertyValueBo#getId()}*/
	public void setValueid(Long proid) {
		this.valueid = proid;
	}
	
	public PropsUserInputEnum getInputType() {
		return inputType;
	}
	public void setInputType(PropsUserInputEnum inputType) {
		this.inputType = inputType;
	}
	/** 商品的属性值 */
	public String getValue() {
		return value;
	}
	/** 商品的属性值 */
	public void setValue(String value) {
		this.value = value;
	}
	/**属性名称*/
	public String getPropertyName() {
		return propertyName;
	}
	/**属性名称*/
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String toJson(){
		List<String> fds = Tools.bean.getFields(this.getClass());
		Map<String,Object> rs = new HashMap<>();
		for(String f : fds){
			Object o = Tools.bean.getPropertis(this, f);
			if(o==null){
				continue;
			}
			else if(f.equalsIgnoreCase("InputType")){
				o = getInputType().getKey();
			}
			rs.put(f, o);
		}
		return Tools.json.toJson(rs);
	}
	@Override
	public String toString(){
		return toJson();
	}
}
