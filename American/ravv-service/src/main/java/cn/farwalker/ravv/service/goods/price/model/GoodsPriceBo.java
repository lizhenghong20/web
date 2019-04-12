package cn.farwalker.ravv.service.goods.price.model;
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

import cn.farwalker.ravv.service.goods.price.biz.IGoodsPriceBiz;

/**
 * 商品价格表
 * 
 * @author generateModel.java
 */
@TableName(GoodsPriceBo.TABLE_NAME)
@DDLTable(name=GoodsPriceBo.TABLE_NAME,comment="商品价格表")
public class GoodsPriceBo extends Model<GoodsPriceBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        /*agentStandardPrice("agent_standard_price"),
        delStandardPrice("del_standard_price"),
        highRetailPrice("high_retail_price"),
        lowRetailPrice("low_retail_price"),
        promoteCommission("promote_commission"),
        saleCommission("sale_commission"),
        */
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        goodsId("goods_id"),
        remark("remark"),
        skuId("sku_id"),
        costPrice("cost_price"),
        price("price");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:goods_price*/
    public static final String TABLE_NAME = "goods_price";
    private static final long serialVersionUID = -2103831712L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="价格ID")
    private Long id;
/*
    @TableField("agent_standard_price")
    @DDLColumn(name="agent_standard_price",comment="代销基准采购价(分)")
    private BigDecimal agentStandardPrice;
*/
    @TableField("cost_price")
    @DDLColumn(name="cost_price",comment="成本价(分)")
    private BigDecimal costPrice;
/*
    @TableField("del_standard_price")
    @DDLColumn(name="del_standard_price",comment="经销基准采购价(分)")
    private BigDecimal delStandardPrice;
*/
    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("goods_id")
    @DDLColumn(name="goods_id",comment="商品ID")
    private Long goodsId;
/*
    @TableField("high_retail_price")
    @DDLColumn(name="high_retail_price",comment="最高零售价(分)")
    private BigDecimal highRetailPrice;

    @TableField("low_retail_price")
    @DDLColumn(name="low_retail_price",comment="最低零售价(分)")
    private BigDecimal lowRetailPrice;

    @TableField("promote_commission")
    @DDLColumn(name="promote_commission",comment="推广佣金(%)")
    private BigDecimal promoteCommission;
*/
    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=1024)
    private String remark;
/*
    @TableField("sale_commission")
    @DDLColumn(name="sale_commission",comment="销售佣金(%)")
    private BigDecimal saleCommission;
*/
    @TableField("sku_id")
    @DDLColumn(name="sku_id",comment="SKUID")
    private Long skuId;

    @TableField("price")
    @DDLColumn(name="price",comment="销售价(分)")
    private BigDecimal price;
    /** 价格ID*/
    public Long getId(){
        return id;
    }
    /** 价格ID*/
    public void setId(Long id){
        this.id =id;
    }
    /** 代销基准采购价(分)
    public BigDecimal getAgentStandardPrice(){
        return agentStandardPrice;
    }
    代销基准采购价(分)
    public void setAgentStandardPrice(BigDecimal agentStandardPrice){
        this.agentStandardPrice =agentStandardPrice;
    }
    */
    /** 成本价(分)*/
    public BigDecimal getCostPrice(){
        return costPrice;
    }
    /** 成本价(分)*/
    public void setCostPrice(BigDecimal costPrice){
        this.costPrice =costPrice;
    }
    /** 经销基准采购价(分)
    public BigDecimal getDelStandardPrice(){
        return delStandardPrice;
    }
    经销基准采购价(分)
    public void setDelStandardPrice(BigDecimal delStandardPrice){
        this.delStandardPrice =delStandardPrice;
    }*/
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
    /** 商品ID*/
    public Long getGoodsId(){
        return goodsId;
    }
    /** 商品ID*/
    public void setGoodsId(Long goodsId){
        this.goodsId =goodsId;
    }
    /** 最高零售价(分) 
    public BigDecimal getHighRetailPrice(){
        return highRetailPrice;
    }
      最高零售价(分) 
    public void setHighRetailPrice(BigDecimal highRetailPrice){
        this.highRetailPrice =highRetailPrice;
    }
     * 最低零售价(分) 
    public BigDecimal getLowRetailPrice(){
        return lowRetailPrice;
    }
     最低零售价(分) 
    public void setLowRetailPrice(BigDecimal lowRetailPrice){
        this.lowRetailPrice =lowRetailPrice;
    }
      推广佣金(%) 
    public BigDecimal getPromoteCommission(){
        return promoteCommission;
    }
     推广佣金(%)
    public void setPromoteCommission(BigDecimal promoteCommission){
        this.promoteCommission =promoteCommission;
    }*/
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 销售佣金(%)
    public BigDecimal getSaleCommission(){
        return saleCommission;
    }
     销售佣金(%) 
    public void setSaleCommission(BigDecimal saleCommission){
        this.saleCommission =saleCommission;
    }*/
    /** SKUID*/
    public Long getSkuId(){
        return skuId;
    }
    /** SKUID*/
    public void setSkuId(Long skuId){
        this.skuId =skuId;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
    /** 销售价(分)*/
    public BigDecimal getPrice(){
        return price;
    }
    /** 销售价(分)*/
    public void setPrice(BigDecimal price){
        this.price =price;
    }
}