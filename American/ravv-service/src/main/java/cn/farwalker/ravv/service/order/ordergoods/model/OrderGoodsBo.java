package cn.farwalker.ravv.service.order.ordergoods.model;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cn.farwalker.ravv.service.order.ordergoods.constants.OrderGoodsStatusEnum;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import com.cangwu.frame.orm.core.BaseBo;
import com.cangwu.frame.orm.core.IFieldKey;
import com.cangwu.frame.orm.ddl.annotation.DDLColumn;
import com.cangwu.frame.orm.ddl.annotation.DDLTable;

/**
 * 订单商品快照
 * 
 * @author generateModel.java
 */
@TableName(OrderGoodsBo.TABLE_NAME)
@DDLTable(name=OrderGoodsBo.TABLE_NAME,comment="订单商品快照")
public class OrderGoodsBo extends Model<OrderGoodsBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        goodsBrandName("goods_brand_name"),
        goodsCode("goods_code"),
        goodsId("goods_id"),
        goodsModelNum("goods_model_num"),
        goodsName("goods_name"),
        goodsfee("goodsfee"),
        
        imgDesc("img_desc"),
        imgMajor("img_major"),
        imgTitle("img_title"),
        orderId("order_id"),
        price("price"),
        propertyDef("property_def"),
        propertyDefDesc("property_def_desc"),
        quantity("quantity"),
        remark("remark"),
        skuId("sku_id"),
        
        unitId("unit_id"),
        unitName("unit_name"),
        orderGoodsStatus("order_goods_status"),
        imgSku("img_sku"),
        comment("comment");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:order_goods*/
    public static final String TABLE_NAME = "order_goods";
    private static final long serialVersionUID = 1261392693L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="商品快照ID")
    private Long id;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("goods_brand_name")
    @DDLColumn(name="goods_brand_name",comment="品牌名称",length=255)
    private String goodsBrandName;

    @TableField("goods_code")
    @DDLColumn(name="goods_code",comment="商品编码")
    private String goodsCode;

    @TableField("goods_id")
    @DDLColumn(name="goods_id",comment="商品ID")
    private Long goodsId;

    @TableField("goods_model_num")
    @DDLColumn(name="goods_model_num",comment="商品型号")
    private String goodsModelNum;

    @TableField("goods_name")
    @DDLColumn(name="goods_name",comment="商品名称",length=255)
    private String goodsName;

    @TableField("goodsfee")
    @DDLColumn(name="goodsfee",comment="商品金额(绝对值，经过促销运算后可能不是数量*单价了)")
    private BigDecimal goodsfee;

    @TableField("img_desc")
    @DDLColumn(name="img_desc",comment="商品描述URL",length=1024)
    private String imgDesc;

    @TableField("img_major")
    @DDLColumn(name="img_major",comment="主图",length=1024)
    private String imgMajor;

    @TableField("img_title")
    @DDLColumn(name="img_title",comment="图片URL",length=1024)
    private String imgTitle;

    @TableField("order_id")
    @DDLColumn(name="order_id",comment="订单ID")
    private Long orderId;

    @TableField("price")
    @DDLColumn(name="price",comment="价格")
    private BigDecimal price;

    @TableField("property_def")
    @DDLColumn(name="property_def",comment="属性定义串(对应goods_sku_def.propertyValueIds)",length=2000)
    private String propertyDef;

    @TableField("property_def_desc")
    @DDLColumn(name="property_def_desc",comment="属性定义描述(对应goods_sku_def.value_names)",length=2000)
    private String propertyDefDesc;

    @TableField("quantity")
    @DDLColumn(name="quantity",comment="数量")
    private Integer quantity;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("sku_id")
    @DDLColumn(name="sku_id",comment="SKUID")
    private Long skuId;

    @TableField("unit_id")
    @DDLColumn(name="unit_id",comment="单位ID")
    private Integer unitId;

    @TableField("unit_name")
    @DDLColumn(name="unit_name",comment="单位名称",length=255)
    private String unitName;
    
    @TableField("order_goods_status")
    @DDLColumn(name="order_goods_status",comment="商品状态")
    private OrderGoodsStatusEnum orderGoodsStatus;

    @TableField("img_sku")
    @DDLColumn(name="img_sku",comment="sku图片",length=1024)
    private String imgSku;

    @TableField("comment")
    @DDLColumn(name="comment",comment="评价次数")
    private Integer comment;
    
    /** 商品快照ID*/
    public Long getId(){
        return id;
    }
    /** 商品快照ID*/
    public void setId(Long id){
        this.id =id;
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
    /** 品牌名称*/
    public String getGoodsBrandName(){
        return goodsBrandName;
    }
    /** 品牌名称*/
    public void setGoodsBrandName(String goodsBrandName){
        this.goodsBrandName =goodsBrandName;
    }
    /** 商品编码*/
    public String getGoodsCode(){
        return goodsCode;
    }
    /** 商品编码*/
    public void setGoodsCode(String goodsCode){
        this.goodsCode =goodsCode;
    }
    /** 商品ID*/
    public Long getGoodsId(){
        return goodsId;
    }
    /** 商品ID*/
    public void setGoodsId(Long goodsId){
        this.goodsId =goodsId;
    }
    /** 商品型号*/
    public String getGoodsModelNum(){
        return goodsModelNum;
    }
    /** 商品型号*/
    public void setGoodsModelNum(String goodsModelNum){
        this.goodsModelNum =goodsModelNum;
    }
    /** 商品名称*/
    public String getGoodsName(){
        return goodsName;
    }
    /** 商品名称*/
    public void setGoodsName(String goodsName){
        this.goodsName =goodsName;
    }
    /** 商品金额(绝对值，经过促销运算后可能不是数量*单价了)*/
    public BigDecimal getGoodsfee(){
        return goodsfee;
    }
    /** 商品金额(绝对值，经过促销运算后可能不是数量*单价了)*/
    public void setGoodsfee(BigDecimal goodsfee){
        this.goodsfee =goodsfee;
    }
    /** 商品描述URL*/
    public String getImgDesc(){
        return imgDesc;
    }
    /** 商品描述URL*/
    public void setImgDesc(String imgDesc){
        this.imgDesc =imgDesc;
    }
    /** 主图*/
    public String getImgMajor(){
        return imgMajor;
    }
    /** 主图*/
    public void setImgMajor(String imgMajor){
        this.imgMajor =imgMajor;
    }
    /** 图片URL*/
    public String getImgTitle(){
        return imgTitle;
    }
    /** 图片URL*/
    public void setImgTitle(String imgTitle){
        this.imgTitle =imgTitle;
    }
    /** 订单ID*/
    public Long getOrderId(){
        return orderId;
    }
    /** 订单ID*/
    public void setOrderId(Long orderId){
        this.orderId =orderId;
    }
    /** 价格*/
    public BigDecimal getPrice(){
        return price;
    }
    /** 价格*/
    public void setPrice(BigDecimal price){
        this.price =price;
    }
    /** 属性定义串(对应goods_sku_def.propertyValueIds)*/
    public String getPropertyDef(){
        return propertyDef;
    }
    /** 属性定义串(对应goods_sku_def.propertyValueIds)*/
    public void setPropertyDef(String propertyDef){
        this.propertyDef =propertyDef;
    }
    /** 属性定义描述(对应goods_sku_def.value_names)*/
    public String getPropertyDefDesc(){
        return propertyDefDesc;
    }
    /** 属性定义描述(对应goods_sku_def.value_names)*/
    public void setPropertyDefDesc(String propertyDefDesc){
        this.propertyDefDesc =propertyDefDesc;
    }
    /** 数量*/
    public Integer getQuantity(){
        return quantity;
    }
    /** 数量*/
    public void setQuantity(Integer quantity){
        this.quantity =quantity;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** SKUID*/
    public Long getSkuId(){
        return skuId;
    }
    /** SKUID*/
    public void setSkuId(Long skuId){
        this.skuId =skuId;
    }
    /** 单位ID*/
    public Integer getUnitId(){
        return unitId;
    }
    /** 单位ID*/
    public void setUnitId(Integer unitId){
        this.unitId =unitId;
    }
    /** 单位名称*/
    public String getUnitName(){
        return unitName;
    }
    /** 单位名称*/
    public void setUnitName(String unitName){
        this.unitName =unitName;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
    
    /** 商品状态*/
    public OrderGoodsStatusEnum getOrderGoodsStatus(){
        return orderGoodsStatus;
    }
    /** 商品状态*/
    public void setOrderGoodsStatus(OrderGoodsStatusEnum orderGoodsStatus){
        this.orderGoodsStatus =orderGoodsStatus;
    }
    /** sku图片*/
    public String getImgSku(){
        return imgSku;
    }
    /** sku图片*/
    public void setImgSku(String imgSku){
        this.imgSku =imgSku;
    }
    /** 评价次数*/
    public Integer getComment(){
        return comment;
    }
    /** 评价次数*/
    public void setComment(Integer comment){
        this.comment =comment;
    }
}