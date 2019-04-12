package cn.farwalker.ravv.service.web.slider.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;

/**
 * 跳转到那个页面枚举
 * @author Administrator
 */
public enum JumpTypeEnum implements IEnumJsons{
	NONE("none","不跳转"),/**OUTLINK("outline","外链"),*/
	/**商品,参数是GOODSID */
	GOODS("goods","跳到商品页"),
	/**限时购，没有参数*/
    FLASHSALE("flashsale","跳到限时购"),
    /**新品上市，大类的id*/
    NEWARRIVAL("newarrival","跳到新品上市"),
    /**8大类，大类的id*/
    FIRSTMENUM("firstmenu","跳到第一级菜单");
	
	private final String key;
	private final String label;

	private JumpTypeEnum(String key, String label) {
		this.key = key;
		this.label = label;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return getKey();
	}
}
