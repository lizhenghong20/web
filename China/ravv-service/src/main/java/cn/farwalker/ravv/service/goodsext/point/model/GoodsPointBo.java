package cn.farwalker.ravv.service.goodsext.point.model;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
 * 商品评分表
 * 
 * @author generateModel.java
 */
@TableName(GoodsPointBo.TABLE_NAME)
@DDLTable(name=GoodsPointBo.TABLE_NAME,comment="商品评分表")
public class GoodsPointBo extends Model<GoodsPointBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        commentId("comment_id"),
        display("display"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        goodsId("goods_id"),
        goodsPoint("goods_point"),
        memberId("member_id"),
        remark("remark"),
        
        skuId("sku_id"),
        type("type");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:goods_point*/
    public static final String TABLE_NAME = "goods_point";
    private static final long serialVersionUID = 2105590247L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="评分ID")
    private Long id;

    @TableField("comment_id")
    @DDLColumn(name="comment_id",comment="评论ID")
    private Long commentId;

    @TableField("display")
    @DDLColumn(name="display",comment="前台是否显示")
    private Integer display;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("goods_id")
    @DDLColumn(name="goods_id",comment="商品ID")
    private Long goodsId;

    @TableField("goods_point")
    @DDLColumn(name="goods_point",comment="商品评分")
    private BigDecimal goodsPoint;

    @TableField("member_id")
    @DDLColumn(name="member_id",comment="会员ID")
    private Long memberId;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("sku_id")
    @DDLColumn(name="sku_id",comment="SKUID")
    private Long skuId;

    @TableField("type")
    @DDLColumn(name="type",comment="评论类型")
    private Integer type;
    /** 评分ID*/
    public Long getId(){
        return id;
    }
    /** 评分ID*/
    public void setId(Long id){
        this.id =id;
    }
    /** 评论ID*/
    public Long getCommentId(){
        return commentId;
    }
    /** 评论ID*/
    public void setCommentId(Long commentId){
        this.commentId =commentId;
    }
    /** 前台是否显示*/
    public Integer getDisplay(){
        return display;
    }
    /** 前台是否显示*/
    public void setDisplay(Integer display){
        this.display =display;
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
    /** 商品评分*/
    public BigDecimal getGoodsPoint(){
        return goodsPoint;
    }
    /** 商品评分*/
    public void setGoodsPoint(BigDecimal goodsPoint){
        this.goodsPoint =goodsPoint;
    }
    /** 会员ID*/
    public Long getMemberId(){
        return memberId;
    }
    /** 会员ID*/
    public void setMemberId(Long memberId){
        this.memberId =memberId;
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
    /** 评论类型*/
    public Integer getType(){
        return type;
    }
    /** 评论类型*/
    public void setType(Integer type){
        this.type =type;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}