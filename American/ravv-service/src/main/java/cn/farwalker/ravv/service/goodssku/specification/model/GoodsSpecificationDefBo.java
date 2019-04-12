package cn.farwalker.ravv.service.goodssku.specification.model;
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
import cn.farwalker.ravv.service.goodssku.specification.biz.IGoodsSpecificationDefBiz;

/**
 * 商品的属性值定义(只保存了SKU的属性值，普通属性值没有保存在这里)
 * @author generateModel.java
 */
@TableName(GoodsSpecificationDefBo.TABLE_NAME)
@DDLTable(name=GoodsSpecificationDefBo.TABLE_NAME,comment="商品规格定义")
public class GoodsSpecificationDefBo extends Model<GoodsSpecificationDefBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        customValueName("custom_value_name"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        goodsId("goods_id"),
        imgUrl("img_url"),
        isimg("isimg"),
        remark("remark"),
        propertyValueId("property_value_id"),
        
        sequence("sequence"),
        //propertyId("property_id"),
        tmpPropertyName("tmp_property_name");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:goods_specification_def*/
    public static final String TABLE_NAME = "goods_specification_def";
    private static final long serialVersionUID = 1928845765L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键")
    private Long id;

    @TableField("custom_value_name")
    @DDLColumn(name="custom_value_name",comment="自定义属性值名称",length=128)
    private String customValueName;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("goods_id")
    @DDLColumn(name="goods_id",comment="商品ID")
    private Long goodsId;

    @TableField("img_url")
    @DDLColumn(name="img_url",comment="规格图片路径",length=1024)
    private String imgUrl;

    /**数据库不能是is_img,否则报错*/
    @TableField("isimg")
    @DDLColumn(name="isimg",comment="是否图片属性")
    private Boolean isimg;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=128)
    private String remark;


    @TableField("property_value_id")
    @DDLColumn(name="property_value_id",comment="属性与属性值关联ID")
    private Long propertyValueId;

    @TableField("sequence")
    @DDLColumn(name="sequence",comment="商品级的所有值排序(前端使用)")
    private Integer sequence;
    /*
    @TableField("property_id")
    @DDLColumn(name="property_id",comment="属性id")
    private Long propertyId;
    */

    @TableField("tmp_property_name")
    @DDLColumn(name="tmp_property_name",comment="冗余属性名")
    private String tmpPropertyName;
    /** 主键*/
    public Long getId(){
        return id;
    }
    /** 主键*/
    public void setId(Long id){
        this.id =id;
    }
    /** 自定义属性值名称*/
    public String getCustomValueName(){
        return customValueName;
    }
    /** 自定义属性值名称*/
    public void setCustomValueName(String customValueName){
        this.customValueName =customValueName;
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
    /** 规格图片路径*/
    public String getImgUrl(){
        return imgUrl;
    }
    /** 规格图片路径*/
    public void setImgUrl(String imgUrl){
        this.imgUrl =imgUrl;
    }
    /** 是否图片属性*/
    public Boolean getIsimg(){
        return isimg;
    }
    /** 是否图片属性*/
    public void setIsimg(Boolean isImg){
        this.isimg =isImg;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }

    @Override
    protected Serializable pkVal(){
        return id;
    }
    /** 属性与属性值关联ID*/
    public Long getPropertyValueId(){
        return propertyValueId;
    }
    /** 属性与属性值关联ID*/
    public void setPropertyValueId(Long propertyValueId){
        this.propertyValueId =propertyValueId;
    }
 
    /** 商品级的所有值排序(前端使用)*/
    public Integer getSequence(){
        return sequence;
    }
    /** 商品级的所有值排序(前端使用)*/
    public void setSequence(Integer sequence){
        this.sequence =sequence;
    }
    /** 属性id 
    public Long getPropertyId(){
        return propertyId;
    }
      属性id
    public void setPropertyId(Long propertyId){
        this.propertyId =propertyId;
    }*/
    /** 冗余属性名*/
    public String getTmpPropertyName(){
        return tmpPropertyName;
    }
    /** 冗余属性名*/
    public void setTmpPropertyName(String tmpPropertyName){
        this.tmpPropertyName =tmpPropertyName;
    }
}