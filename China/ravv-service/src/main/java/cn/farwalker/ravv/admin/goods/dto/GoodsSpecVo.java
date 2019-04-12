package cn.farwalker.ravv.admin.goods.dto;

import cn.farwalker.ravv.service.goodssku.specification.model.GoodsSpecificationDefBo;

/**
 * 商品的可选属性值
 * @author Administrator
 */
public class GoodsSpecVo extends GoodsSpecificationDefBo{
	private static final long serialVersionUID = -3747401796980578573L;
 
	/**是否选择*/
	private Boolean checked;
 
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	 
}
