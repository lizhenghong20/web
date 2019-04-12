package cn.farwalker.ravv.service.goodssku.skudef.model;
import java.util.Date;
import java.util.List;
import com.cangwu.frame.orm.core.BaseBo;
import com.cangwu.frame.orm.core.annotation.LoadJoinValue;
import com.cangwu.frame.orm.ddl.annotation.DDLColumn;
import com.cangwu.frame.orm.ddl.annotation.DDLTable;
import com.cangwu.frame.orm.core.IFieldKey;
import java.io.Serializable;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import cn.farwalker.ravv.service.goodssku.skudef.biz.IGoodsSkuDefBiz;

/**
 * 商品SKU定义
 * 
 * @author generateModel.java
 */
@TableName(GoodsSkuDefBo.TABLE_NAME)
@DDLTable(name=GoodsSkuDefBo.TABLE_NAME,comment="商品SKU定义")
public class GoodsSkuDefBo extends Model<GoodsSkuDefBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        goodsId("goods_id"),
        //propertyDef("property_def"),
        //propertyDefType("property_def_type"),
        remark("remark"),
        skuCode("sku_code"),
        unitId("unit_id"),
        
        unitName("unit_name"),
        propertyValueIds("property_value_ids"),
        valueNames("value_names"),
        imageUrl("image_url");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:goods_sku_def*/
    public static final String TABLE_NAME = "goods_sku_def";
    private static final long serialVersionUID = -1463414421L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键")
    private Long id;

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
    @TableField("property_def")
    @DDLColumn(name="property_def",comment="SKU属性串",length=1023)
    private String propertyDef;

    @TableField("property_def_type")
    @DDLColumn(name="property_def_type",comment="sku定义的类型",length=63)
    private String propertyDefType;*/

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("sku_code")
    @DDLColumn(name="sku_code",comment="SKU编码")
    private String skuCode;

    @TableField("unit_id")
    @DDLColumn(name="unit_id",comment="库存单位ID")
    private Long unitId;

    @TableField("unit_name")
    @DDLColumn(name="unit_name",comment="库存单位名称",length=255)
    private String unitName;

    @TableField("property_value_ids")
    @DDLColumn(name="property_value_ids",comment="分类的属性值id(括号分隔)",length=512)
    private String propertyValueIds;

    @TableField("value_names")
    @DDLColumn(name="value_names",comment="冗余属性值名组合",length=512)
    private String valueNames;

    @TableField("image_url")
    @DDLColumn(name="image_url",comment="sku图片",length=512)
    private String imageUrl;
    /** 主键*/
    public Long getId(){
        return id;
    }
    /** 主键*/
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
    /** 商品ID*/
    public Long getGoodsId(){
        return goodsId;
    }
    /** 商品ID*/
    public void setGoodsId(Long goodsId){
        this.goodsId =goodsId;
    }
    /** SKU属性描述串 
    public String getPropertyDef(){
        return propertyDef;
    }
      SKU属性描述串 
    public void setPropertyDef(String propertyDef){
        this.propertyDef =propertyDef;
    }
      sku定义的类型
    public String getPropertyDefType(){
        return propertyDefType;
    }
    /** sku定义的类型 
    public void setPropertyDefType(String propertyDefType){
        this.propertyDefType =propertyDefType;
    }*/
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** SKU编码*/
    public String getSkuCode(){
        return skuCode;
    }
    /** SKU编码*/
    public void setSkuCode(String skuCode){
        this.skuCode =skuCode;
    }
    /** 库存单位ID*/
    public Long getUnitId(){
        return unitId;
    }
    /** 库存单位ID*/
    public void setUnitId(Long unitId){
        this.unitId =unitId;
    }
    /** 库存单位名称*/
    public String getUnitName(){
        return unitName;
    }
    /** 库存单位名称*/
    public void setUnitName(String unitName){
        this.unitName =unitName;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
    /** 分类的属性值id(括号分隔)*/
    public String getPropertyValueIds(){
        return propertyValueIds;
    }
    /** 分类的属性值id(括号分隔)*/
    public void setPropertyValueIds(String propertyValueIds){
        this.propertyValueIds =propertyValueIds;
    }
    /** 冗余属性值名组合(尺码:大码;颜色:红色)*/
    public String getValueNames(){
        return valueNames;
    }
    /** 冗余属性值名组合:(尺码:大码;颜色:红色);*/
    public void setValueNames(String valueNames){
        this.valueNames =valueNames;
    }
    /** sku图片*/
    public String getImageUrl(){
        return imageUrl;
    }
    /** sku图片*/
    public void setImageUrl(String imageUrl){
        this.imageUrl =imageUrl;
    }
}