package cn.farwalker.ravv.service.goodsext.recommended.model;
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
import cn.farwalker.ravv.service.goodsext.recommended.biz.IGoodsRecommendedBiz;

/**
 * 商品推荐表
 * 
 * @author generateModel.java
 */
@TableName(GoodsRecommendedBo.TABLE_NAME)
@DDLTable(name=GoodsRecommendedBo.TABLE_NAME,comment="商品推荐表")
public class GoodsRecommendedBo extends Model<GoodsRecommendedBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        primaryGoodsId("primary_goods_id"),
        remark("remark"),
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
    /** 数据表名:goods_recommended*/
    public static final String TABLE_NAME = "goods_recommended";
    private static final long serialVersionUID = -934728590L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="商品推荐ID")
    private Long id;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("primary_goods_id")
    @DDLColumn(name="primary_goods_id",comment="主商品ID")
    private Long primaryGoodsId;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("sequence")
    @DDLColumn(name="sequence",comment="排序")
    private Integer sequence;
    /** 商品推荐ID*/
    public Long getId(){
        return id;
    }
    /** 商品推荐ID*/
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
    /** 主商品ID*/
    public Long getPrimaryGoodsId(){
        return primaryGoodsId;
    }
    /** 主商品ID*/
    public void setPrimaryGoodsId(Long primaryGoodsId){
        this.primaryGoodsId =primaryGoodsId;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 排序*/
    public Integer getSequence(){
        return sequence;
    }
    /** 排序*/
    public void setSequence(Integer sequence){
        this.sequence =sequence;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}