package cn.farwalker.ravv.service.flash.goods.model;
import java.io.Serializable;
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
 * 闪购的商品(与flash_goods_sku没有直接关系)
 * 
 * @author generateModel.java
 */
@TableName(FlashGoodsBo.TABLE_NAME)
@DDLTable(name=FlashGoodsBo.TABLE_NAME,comment="闪购的商品(与flash_goods_sku没有直接关系)")
public class FlashGoodsBo extends Model<FlashGoodsBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        flashSaleId("flash_sale_id"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        goodsId("goods_id"),
        sequence("sequence");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:flash_goods*/
    public static final String TABLE_NAME = "flash_goods";
    private static final long serialVersionUID = -1196797869L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="")
    private Long id;

    @TableField("flash_sale_id")
    @DDLColumn(name="flash_sale_id",comment="")
    private Long flashSaleId;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("goods_id")
    @DDLColumn(name="goods_id",comment="")
    private Long goodsId;

    @TableField("sequence")
    @DDLColumn(name="sequence",comment="顺序")
    private Integer sequence;
    /** null*/
    public Long getId(){
        return id;
    }
    /** null*/
    public void setId(Long id){
        this.id =id;
    }
    /** null*/
    public Long getFlashSaleId(){
        return flashSaleId;
    }
    /** null*/
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
    /** null*/
    public Long getGoodsId(){
        return goodsId;
    }
    /** null*/
    public void setGoodsId(Long goodsId){
        this.goodsId =goodsId;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
    /** 顺序*/
    public Integer getSequence(){
        return sequence;
    }
    /** 顺序*/
    public void setSequence(Integer sequence){
        this.sequence =sequence;
    }
}