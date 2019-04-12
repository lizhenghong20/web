package cn.farwalker.ravv.service.goodssku.skudef.model;

import java.math.BigDecimal;

import cn.farwalker.ravv.service.goods.inventory.model.GoodsInventoryBo;
import cn.farwalker.ravv.service.goods.price.model.GoodsPriceBo;

/**
 * goods的SKU库存及单价
 * @author Administrator
 */
public class SkuPriceInventoryVo extends GoodsSkuDefBo{
	private static final long serialVersionUID = -977220966366831728L;
	private GoodsInventoryBo invbo;
	private GoodsPriceBo prcbo;
	
	public SkuPriceInventoryVo(){
		this( new GoodsInventoryBo(), new GoodsPriceBo());
	}
	public SkuPriceInventoryVo(GoodsInventoryBo invBo,GoodsPriceBo prcbo){
		this.invbo = invBo;
		this.prcbo = prcbo;
	}
	
	public void setSkuId(Long skuId) {
		invbo.setSkuId(skuId);
		prcbo.setSkuId(skuId);
	}
	/** SKUID*/
    public Long getSkuId(){
        return prcbo.getSkuId();
    }

	////////////////////////////////////////
    
    public Long getInventoryId(){
    	return invbo.getId();
    }
    public void setInventoryId(Long id){
    	invbo.setId(id);
    }
    
    /** 预警库存数量(低于这个值时报警，0不报警)*/
    public Integer getAlarmStockNum(){
        return invbo.getAlarmStockNum();
    }
	public void setAlarmStockNum(Integer alarmStockNum) { 
		invbo.setAlarmStockNum(alarmStockNum);
	}

    /** 销售库存数量*/
    public Integer getSaleStockNum(){
        return invbo.getSaleStockNum();
    }
	public void setSaleStockNum(Integer saleStockNum) {
		invbo.setSaleStockNum(saleStockNum);
	}
	/** 冻结库存数量*/
    public Integer getFreeze(){
        return invbo.getFreeze();
    }
	public void setFreeze(Integer freeze) {
		invbo.setFreeze(freeze);
	}
	
    public Long getPriceId(){
    	return prcbo.getId();
    }
    public void setPriceId(Long id){
    	prcbo.setId(id);
    }
    
    /** 销售价(分)*/
    public BigDecimal getPrice(){
        return prcbo.getPrice();
    }
    /** 销售价(分)*/
    public void setPrice(BigDecimal price){
        prcbo.setPrice(price);
    }
    
    /** 成本价(分)*/
    public BigDecimal getCostPrice(){
        return prcbo.getCostPrice();
    }
    /** 成本价(分)*/
    public void setCostPrice(BigDecimal costPrice){
        prcbo.setCostPrice(costPrice);
    }
}
 