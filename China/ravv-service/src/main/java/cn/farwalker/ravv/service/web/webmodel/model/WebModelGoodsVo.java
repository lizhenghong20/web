package cn.farwalker.ravv.service.web.webmodel.model;

import com.cangwu.frame.orm.core.annotation.LoadJoinValue;
import com.cangwu.frame.orm.ddl.annotation.NotColumn;

import cn.farwalker.ravv.service.goods.base.model.GoodsBo;

public class WebModelGoodsVo extends WebModelGoodsBo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6264274242564494411L;
	
	/** 商品名称 */
	@NotColumn
	private String goodsName;
	
	@NotColumn
	private String modelCodeName;

	/** */
	public String getGoodsName() {
		return goodsName;
	}

	/** */
	@LoadJoinValue(by = "goodsId", table = GoodsBo.TABLE_NAME, joinfield = "id", returnfield = "goodsName")
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getModelCodeName() {
		return modelCodeName;
	}
	@LoadJoinValue(by = "modelCode", table = WebModelBo.TABLE_NAME, joinfield = "modelCode", returnfield = "modelName")
	public void setModelCodeName(String modelCodeName) {
		this.modelCodeName = modelCodeName;
	}
}
