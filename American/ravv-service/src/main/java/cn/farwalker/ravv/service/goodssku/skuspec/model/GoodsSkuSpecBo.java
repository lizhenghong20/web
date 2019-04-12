package cn.farwalker.ravv.service.goodssku.skuspec.model;
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
import cn.farwalker.ravv.service.goodssku.skuspec.biz.IGoodsSkuSpecBiz;

/**
 * 商品sku与商品的属性值关联表
 * 
 * @author generateModel.java
 */
@TableName(GoodsSkuSpecBo.TABLE_NAME)
@DDLTable(name=GoodsSkuSpecBo.TABLE_NAME,comment="商品sku规格")
public class GoodsSkuSpecBo extends Model<GoodsSkuSpecBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        goodsId("goods_id"),
        remark("remark"),
        sequence("sequence"),
        skuId("sku_id"),
        specId("spec_id");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:goods_sku_spec*/
    public static final String TABLE_NAME = "goods_sku_spec";
    private static final long serialVersionUID = -1976577457L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="SKU规格关联ID")
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

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("sequence")
    @DDLColumn(name="sequence",comment="顺序")
    private Integer sequence;

    @TableField("sku_id")
    @DDLColumn(name="sku_id",comment="SKUID")
    private Long skuId;

    @TableField("spec_id")
    @DDLColumn(name="spec_id",comment="商品规格ID")
    private Long specId;
    /** SKU规格关联ID*/
    public Long getId(){
        return id;
    }
    /** SKU规格关联ID*/
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
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 顺序*/
    public Integer getSequence(){
        return sequence;
    }
    /** 顺序*/
    public void setSequence(Integer sequence){
        this.sequence =sequence;
    }
    /** SKUID*/
    public Long getSkuId(){
        return skuId;
    }
    /** SKUID*/
    public void setSkuId(Long skuId){
        this.skuId =skuId;
    }
    /** 商品规格ID*/
    public Long getSpecId(){
        return specId;
    }
    /** 商品规格ID*/
    public void setSpecId(Long specId){
        this.specId =specId;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}