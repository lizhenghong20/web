package cn.farwalker.ravv.service.shipment.model;

public class ShipmentVo extends ShipmentBo {

   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* 商品分类路径id*/
	private Long catId;
	
	/* 商品分类路径name*/
	private String catName;
	
	
	/*商品名称goodsName*/
	private String goodsName;
	
	public Long getCatId() {
		return catId;
	}

	public void setCatId(Long catId) {
		this.catId = catId;
	}
	
	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	
}
