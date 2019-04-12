package cn.farwalker.ravv.service.goods.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsonn;

/**
 * 商品普通属性的输入方式
 * @author Administrator
 *
 */
public enum PropsUserInputEnum implements IEnumJsonn {
	DEF(1,"预定义值"),
    INPUY(2,"输入值"), 
    INPUYDEF( 3,"输入和预定义并存");
    
	private final Integer value;
	private final String label;
    private PropsUserInputEnum(int v,String label){
    	value = Integer.valueOf(v);
    	this.label = label;
    }

	@Override
	public String getLabel() {
		return label;
	}
	@Override
	public Integer getKey() { 
		return value;
	}
}
