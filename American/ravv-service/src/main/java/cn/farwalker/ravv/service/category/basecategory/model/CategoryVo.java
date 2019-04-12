package cn.farwalker.ravv.service.category.basecategory.model;

import cn.farwalker.ravv.service.goods.base.model.GoodsBo;

import com.cangwu.frame.orm.core.annotation.LoadJoinValue;

public class CategoryVo extends BaseCategoryBo{
	/**父类名称*/
	private String parentname;

	public String getParentname() {
		return parentname;
	}

	/**这只是为了演示加载id对应的汉字名称，没有具体意义*/
	@LoadJoinValue(by="pid",table=GoodsBo.TABLE_NAME,joinfield="id",returnfield="goodsName")
	public void setParentname(String parentname) {
		this.parentname = parentname;
	}
	
}
