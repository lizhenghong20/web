package cn.farwalker.ravv.service.flash.goods.model;

import com.cangwu.frame.orm.core.annotation.LoadJoinValue;

import cn.farwalker.ravv.service.goods.base.model.GoodsBo;

public class FlashGoodsVo extends FlashGoodsBo{

	private static final long serialVersionUID = 1L;

	private GoodsBo goodsBo;
	
	private String goodsName;
	
	private String leafCategoryName;
	
	private String goodsCode;

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getLeafCategoryName() {
		return leafCategoryName;
	}

	public void setLeafCategoryName(String leafCategoryName) {
		this.leafCategoryName = leafCategoryName;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public GoodsBo getGoodsBo() {
		return goodsBo;
	}

	@LoadJoinValue(by="goodsId",table=GoodsBo.TABLE_NAME,joinfield="id")
	public void setGoodsBo(GoodsBo goodsBo) {
		this.goodsBo = goodsBo;
		if(goodsBo!=null) {
			goodsCode = goodsBo.getGoodsCode();
		}
	}
	
}
