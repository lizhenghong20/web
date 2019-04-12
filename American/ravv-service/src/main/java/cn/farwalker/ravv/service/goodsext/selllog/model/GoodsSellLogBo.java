package cn.farwalker.ravv.service.goodsext.selllog.model;
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
import cn.farwalker.ravv.service.goodsext.selllog.biz.IGoodsSellLogBiz;

/**
 * 商品售卖纪录
 * 
 * @author generateModel.java
 */
@TableName(GoodsSellLogBo.TABLE_NAME)
@DDLTable(name=GoodsSellLogBo.TABLE_NAME,comment="商品售卖纪录")
public class GoodsSellLogBo extends Model<GoodsSellLogBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        goodsId("goods_id"),
        memberId("member_id"),
        memberName("member_name"),
        number("number"),
        orderId("order_id"),
        price("price"),
        
        remark("remark"),
        skuId("sku_id"),
        skuName("sku_name"),
        specInfo("spec_info");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:goods_sell_log*/
    public static final String TABLE_NAME = "goods_sell_log";
    private static final long serialVersionUID = 1736647657L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="销售日志ID")
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

    @TableField("member_id")
    @DDLColumn(name="member_id",comment="会员ID")
    private Long memberId;

    @TableField("member_name")
    @DDLColumn(name="member_name",comment="会员名称",length=255)
    private String memberName;

    @TableField("number")
    @DDLColumn(name="number",comment="购买数量")
    private Integer number;

    @TableField("order_id")
    @DDLColumn(name="order_id",comment="订单ID")
    private Long orderId;

    @TableField("price")
    @DDLColumn(name="price",comment="价格")
    private Double price;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("sku_id")
    @DDLColumn(name="sku_id",comment="SKUID")
    private Long skuId;

    @TableField("sku_name")
    @DDLColumn(name="sku_name",comment="SKU名称",length=255)
    private String skuName;

    @TableField("spec_info")
    @DDLColumn(name="spec_info",comment="规格信息",length=1023)
    private String specInfo;
    /** 销售日志ID*/
    public Long getId(){
        return id;
    }
    /** 销售日志ID*/
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
    /** 会员ID*/
    public Long getMemberId(){
        return memberId;
    }
    /** 会员ID*/
    public void setMemberId(Long memberId){
        this.memberId =memberId;
    }
    /** 会员名称*/
    public String getMemberName(){
        return memberName;
    }
    /** 会员名称*/
    public void setMemberName(String memberName){
        this.memberName =memberName;
    }
    /** 购买数量*/
    public Integer getNumber(){
        return number;
    }
    /** 购买数量*/
    public void setNumber(Integer number){
        this.number =number;
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
    public Double getPrice(){
        return price;
    }
    /** 价格*/
    public void setPrice(Double price){
        this.price =price;
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
    /** SKU名称*/
    public String getSkuName(){
        return skuName;
    }
    /** SKU名称*/
    public void setSkuName(String skuName){
        this.skuName =skuName;
    }
    /** 规格信息*/
    public String getSpecInfo(){
        return specInfo;
    }
    /** 规格信息*/
    public void setSpecInfo(String specInfo){
        this.specInfo =specInfo;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}