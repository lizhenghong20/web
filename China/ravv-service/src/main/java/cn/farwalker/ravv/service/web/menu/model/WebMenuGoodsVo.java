package cn.farwalker.ravv.service.web.menu.model;

import com.cangwu.frame.orm.core.annotation.LoadJoinValue;
import com.cangwu.frame.orm.ddl.annotation.NotColumn;

import cn.farwalker.ravv.service.goods.base.model.GoodsBo;

public class WebMenuGoodsVo extends WebMenuGoodsBo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1503037844979557736L;
	/** 商品名称 */
	@NotColumn
	private String goodsName;

	/** */
	public String getGoodsName() {
		return goodsName;
	}

	/** */
	@LoadJoinValue(by = "goodsId", table = GoodsBo.TABLE_NAME, joinfield = "id", returnfield = "goodsName")
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
}
