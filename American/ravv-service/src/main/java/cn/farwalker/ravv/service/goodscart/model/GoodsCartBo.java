package cn.farwalker.ravv.service.goodscart.model;
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
import cn.farwalker.ravv.service.goodscart.biz.IGoodsCartBiz;

/**
 * 购物车
 * 
 * @author generateModel.java
 */
@TableName(GoodsCartBo.TABLE_NAME)
@DDLTable(name=GoodsCartBo.TABLE_NAME,comment="购物车")
public class GoodsCartBo extends Model<GoodsCartBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        addTime("add_time"),
        //dictId("dict_id"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        goodsId("goods_id"),
        memberId("member_id"),
        memberIdent("member_ident"),
        params("params"),
        
        quantity("quantity"),
        remark("remark"),
        sellerId("seller_id"),
        skuId("sku_id");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:goods_cart*/
    public static final String TABLE_NAME = "goods_cart";
    private static final long serialVersionUID = 1345654371L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="购物车ID")
    private Long id;

    @TableField("add_time")
    @DDLColumn(name="add_time",comment="添加时间")
    private Date addTime;

    /*@TableField("dict_id")
    @DDLColumn(name="dict_id",comment="商品类型")
    private Integer dictId;*/

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

    @TableField("member_ident")
    @DDLColumn(name="member_ident",comment="会员标识",length=255)
    private String memberIdent;

    @TableField("params")
    @DDLColumn(name="params",comment="参数",length=1023)
    private String params;

    @TableField("quantity")
    @DDLColumn(name="quantity",comment="数量")
    private Integer quantity;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("seller_id")
    @DDLColumn(name="seller_id",comment="卖家ID")
    private Long sellerId;

    @TableField("sku_id")
    @DDLColumn(name="sku_id",comment="SKUID")
    private Long skuId;
    /** 购物车ID*/
    public Long getId(){
        return id;
    }
    /** 购物车ID*/
    public void setId(Long id){
        this.id =id;
    }
    /** 添加时间*/
    public Date getAddTime(){
        return addTime;
    }
    /** 添加时间*/
    public void setAddTime(Date addTime){
        this.addTime =addTime;
    }
    /** 商品类型* /
    public Integer getDictId(){
        return dictId;
    }
    / ** 商品类型
    public void setDictId(Integer dictId){
        this.dictId =dictId;
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
    /** 会员ID*/
    public Long getMemberId(){
        return memberId;
    }
    /** 会员ID*/
    public void setMemberId(Long memberId){
        this.memberId =memberId;
    }
    /** 会员标识*/
    public String getMemberIdent(){
        return memberIdent;
    }
    /** 会员标识*/
    public void setMemberIdent(String memberIdent){
        this.memberIdent =memberIdent;
    }
    /** 参数*/
    public String getParams(){
        return params;
    }
    /** 参数*/
    public void setParams(String params){
        this.params =params;
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
    /** 卖家ID*/
    public Long getSellerId(){
        return sellerId;
    }
    /** 卖家ID*/
    public void setSellerId(Long sellerId){
        this.sellerId =sellerId;
    }
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
}