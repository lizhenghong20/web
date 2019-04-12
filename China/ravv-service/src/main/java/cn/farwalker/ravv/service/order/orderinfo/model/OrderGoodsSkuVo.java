package cn.farwalker.ravv.service.order.orderinfo.model;

/**
 * 下单时选择的属性值id及数量
 * @author Administrator
 */
public class OrderGoodsSkuVo{
	private Long goodsId;
	private Long skuId;
    private Integer quan;
 
    //private List<Long> valueids;
    /** 下单数量*/
    public Integer getQuan(){
        return quan;
    }
    /** 下单数量*/
    public void setQuan(Integer quan){
        this.quan =quan;
    }
    /** sku的属性值id {@link GoodsSpecificationDefBo#getPropertyValueId()} 
    public List<Long> getValueids(){
        return valueids;
    }*/
    /** sku的属性值id {@link GoodsSpecificationDefBo#getPropertyValueId()}
    public void setValueids(List<Long> valueids){
        this.valueids =valueids;
    }*/
	
    /** 商品id*/
    public Long getGoodsId(){
		return goodsId;
	}
	
    /** 商品id*/
    public void setGoodsId(Long goodsId){
		this.goodsId = goodsId;
	}
    
    /** skuid*/
	public Long getSkuId(){
		return skuId;
	}
	/** skuid*/
	public void setSkuId(Long skuId){
		this.skuId = skuId;
	}

}
