package cn.farwalker.ravv.service.flash.sku.model;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.cangwu.frame.orm.core.BaseBo;
import com.cangwu.frame.orm.core.annotation.LoadJoinValue;
import com.cangwu.frame.orm.ddl.annotation.DDLColumn;
import com.cangwu.frame.orm.ddl.annotation.DDLTable;
import com.cangwu.frame.orm.core.IFieldKey;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;

import cn.farwalker.ravv.service.flash.sku.biz.IFlashGoodsSkuBiz;
import cn.farwalker.ravv.service.goodssku.skudef.model.GoodsSkuDefBo;

/**
 * 闪购商品
 * 
 * @author generateModel.java
 */
@TableName(FlashGoodsSkuBo.TABLE_NAME)
@DDLTable(name=FlashGoodsSkuBo.TABLE_NAME,comment="闪购商品")
public class FlashGoodsSkuBo extends Model<FlashGoodsSkuBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        flashSaleId("flash_sale_id"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        goodsId("goods_id"),
        inventory("inventory"),
        price("price"),
        propertyValueIds("property_value_ids"),
        saleCount("sale_count"),
        
        goodsSkuDefId("goods_sku_def_id"),
        freezeCount("freeze_count");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:flash_goods_sku*/
    public static final String TABLE_NAME = "flash_goods_sku";
    private static final long serialVersionUID = -1266095996L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="")
    private Long id;

    @TableField("flash_sale_id")
    @DDLColumn(name="flash_sale_id",comment="闪购ID")
    private Long flashSaleId;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("goods_id")
    @DDLColumn(name="goods_id",comment="Goods.id")
    private Long goodsId;

    @TableField("inventory")
    @DDLColumn(name="inventory",comment="库存")
    private Integer inventory;

    @TableField("price")
    @DDLColumn(name="price",comment="销售价格")
    private BigDecimal price;

    @TableField("property_value_ids")
    @DDLColumn(name="property_value_ids",comment="分类的属性值id(与goods_sku_def相同)",length=127)
    private String propertyValueIds;

    @TableField("sale_count")
    @DDLColumn(name="sale_count",comment="销售数量")
    private Integer saleCount;

    @TableField("goods_sku_def_id")
    @DDLColumn(name="goods_sku_def_id",comment="商品的SKUID {@link GoodsSkuDefBo#getId()}")
    private Long goodsSkuDefId;

    @TableField("freeze_count")
    @DDLColumn(name="freeze_count",comment="冻结数量")
    private Integer freezeCount;
    /** null*/
    public Long getId(){
        return id;
    }
    /** null*/
    public void setId(Long id){
        this.id =id;
    }
    /** 闪购ID*/
    public Long getFlashSaleId(){
        return flashSaleId;
    }
    /** 闪购ID*/
    public void setFlashSaleId(Long flashSaleId){
        this.flashSaleId =flashSaleId;
    }
    /** 创建时间*/
    public Date getGmtCreate(){
        return gmtCreate;
    }
    /** 创建时间*/
    public void setGmtCreate(Date gmtCreate){
        this.gmtCreate =gmtCreate;
    }
    /** 修改时间*/
    public Date getGmtModified(){
        return gmtModified;
    }
    /** 修改时间*/
    public void setGmtModified(Date gmtModified){
        this.gmtModified =gmtModified;
    }
    /** Goods.id*/
    public Long getGoodsId(){
        return goodsId;
    }
    /** Goods.id*/
    public void setGoodsId(Long goodsId){
        this.goodsId =goodsId;
    }
    /** 库存*/
    public Integer getInventory(){
        return inventory;
    }
    /** 库存*/
    public void setInventory(Integer inventory){
        this.inventory =inventory;
    }
    /** 销售价格*/
    public BigDecimal getPrice(){
        return price;
    }
    /** 销售价格*/
    public void setPrice(BigDecimal price){
        this.price =price;
    }
    /** 分类的属性值id(与goods_sku_def相同)*/
    public String getPropertyValueIds(){
        return propertyValueIds;
    }
    /** 分类的属性值id(与goods_sku_def相同)*/
    public void setPropertyValueIds(String propertyValueIds){
        this.propertyValueIds =propertyValueIds;
    }
    /** 销售数量*/
    public Integer getSaleCount(){
        return saleCount;
    }
    /** 销售数量*/
    public void setSaleCount(Integer saleCount){
        this.saleCount =saleCount;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
    /** 商品的SKUID {@link GoodsSkuDefBo#getId()}*/
    public Long getGoodsSkuDefId(){
        return goodsSkuDefId;
    }
    /** 商品的SKUID {@link GoodsSkuDefBo#getId()}*/
    public void setGoodsSkuDefId(Long goodsSkuDefId){
        this.goodsSkuDefId =goodsSkuDefId;
    }
    /** 冻结数量*/
    public Integer getFreezeCount(){
        return freezeCount;
    }
    /** 冻结数量*/
    public void setFreezeCount(Integer freezeCount){
        this.freezeCount =freezeCount;
    }
}