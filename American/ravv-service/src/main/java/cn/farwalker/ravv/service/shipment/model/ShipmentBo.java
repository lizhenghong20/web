package cn.farwalker.ravv.service.shipment.model;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cn.farwalker.ravv.service.shipment.constants.ShipmentTypeEnum;

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
 * 运费模板(美国的方式)
 * 
 * @author generateModel.java
 */
@TableName(ShipmentBo.TABLE_NAME)
@DDLTable(name=ShipmentBo.TABLE_NAME,comment="运费模板(美国的方式)")
public class ShipmentBo extends Model<ShipmentBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        days("days"),
        fee("fee"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        name("name"),
        remark("remark"),
        title("title"),
        categoryPaths("category_paths"),
        
        goodsId("goods_id"),
        maxAmt("max_amt"),
        shipmentType("shipment_type"),
        carrierCode("carrier_code"),
        serviceCode("service_code"),
        expCode("exp_code") ;
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:shipment*/
    public static final String TABLE_NAME = "shipment";
    private static final long serialVersionUID = -277522521L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="运费模板ID")
    private Long id;

    @TableField("days")
    @DDLColumn(name="days",comment="快递天数")
    private Integer days;

    @TableField("fee")
    @DDLColumn(name="fee",comment="金额")
    private BigDecimal fee;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("name")
    @DDLColumn(name="name",comment="模板名称",length=255)
    private String name;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("title")
    @DDLColumn(name="title",comment="标题",length=255)
    private String title;

    @TableField("category_paths")
    @DDLColumn(name="category_paths",comment="商品分类路径",length=255)
    private String categoryPaths;

    @TableField("goods_id")
    @DDLColumn(name="goods_id",comment="商品id")
    private Long goodsId;

    @TableField("max_amt")
    @DDLColumn(name="max_amt",comment="超额免运费")
    private BigDecimal maxAmt;

    @TableField("shipment_type")
    @DDLColumn(name="shipment_type",comment="运费模板类型(通用/指定商品)",length=63)
    private ShipmentTypeEnum shipmentType;

    @TableField("carrier_code")
    @DDLColumn(name="carrier_code",comment="承运商",length=255)
    private String carrierCode;

    @TableField("service_code")
    @DDLColumn(name="service_code",comment="快递类型",length=255)
    private String serviceCode;

    @TableField("exp_code")
    @DDLColumn(name="exp_code",comment="快递代码",length=255)
    private String expCode;

    /** 运费模板ID*/
    public Long getId(){
        return id;
    }
    /** 运费模板ID*/
    public void setId(Long id){
        this.id =id;
    }
    /** 快递天数*/
    public Integer getDays(){
        return days;
    }
    /** 快递天数*/
    public void setDays(Integer days){
        this.days =days;
    }
    /** 金额*/
    public BigDecimal getFee(){
        return fee;
    }
    /** 金额*/
    public void setFee(BigDecimal fee){
        this.fee =fee;
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
    /** 模板名称*/
    public String getName(){
        return name;
    }
    /** 模板名称*/
    public void setName(String name){
        this.name =name;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 标题*/
    public String getTitle(){
        return title;
    }
    /** 标题*/
    public void setTitle(String title){
        this.title =title;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
    /** 商品分类路径*/
    public String getCategoryPaths(){
        return categoryPaths;
    }
    /** 商品分类路径*/
    public void setCategoryPaths(String categoryPaths){
        this.categoryPaths =categoryPaths;
    }
    /** 商品id*/
    public Long getGoodsId(){
        return goodsId;
    }
    /** 商品id*/
    public void setGoodsId(Long goodsId){
        this.goodsId =goodsId;
    }
    /** 超额免运费*/
    public BigDecimal getMaxAmt(){
        return maxAmt;
    }
    /** 超额免运费*/
    public void setMaxAmt(BigDecimal maxAmt){
        this.maxAmt =maxAmt;
    }
    /** 运费类型(通用/指定商品)*/
    public ShipmentTypeEnum getShipmentType(){
        return shipmentType;
    }
    /** 运费类型(通用/指定商品)*/
    public void setShipmentType(ShipmentTypeEnum shipmentType){
        this.shipmentType =shipmentType;
    }
    /** 承运商*/
    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getExpCode() {
        return expCode;
    }

    public void setExpCode(String expCode) {
        this.expCode = expCode;
    }
}