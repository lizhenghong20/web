package cn.farwalker.ravv.service.category.value.model;
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
import cn.farwalker.ravv.service.category.value.biz.IBaseCategoryPropertyValueBiz;
import cn.farwalker.waka.constants.StatusEnum;

/**
 * 叶子类目的属性与属性值的关联表
 * 
 * @author generateModel.java
 */
@TableName(BaseCategoryPropertyValueBo.TABLE_NAME)
@DDLTable(name=BaseCategoryPropertyValueBo.TABLE_NAME,comment="叶子类目的属性与属性值的关联表")
public class BaseCategoryPropertyValueBo extends Model<BaseCategoryPropertyValueBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        propertyId("property_id"),
        remark("remark"),
        status("status"),
        tmpCategoryId("tmp_category_id"),
        tmpPropertyName("tmp_property_name"),
        valueName("value_name");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:base_category_property_value*/
    public static final String TABLE_NAME = "base_category_property_value";
    private static final long serialVersionUID = -83448070L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键")
    private Long id;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("property_id")
    @DDLColumn(name="property_id",comment="属性ID")
    private Long propertyId;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("status")
    @DDLColumn(name="status",comment="状态",length=63)
    private StatusEnum status;

    @TableField("tmp_category_id")
    @DDLColumn(name="tmp_category_id",comment="分类ID")
    private Long tmpCategoryId;

    @TableField("tmp_property_name")
    @DDLColumn(name="tmp_property_name",comment="属性名称",length=255)
    private String tmpPropertyName;

    @TableField("value_name")
    @DDLColumn(name="value_name",comment="值名称",length=255)
    private String valueName;
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
    /** 属性ID*/
    public Long getPropertyId(){
        return propertyId;
    }
    /** 属性ID*/
    public void setPropertyId(Long propertyId){
        this.propertyId =propertyId;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 状态*/
    public StatusEnum getStatus(){
        return status;
    }
    /** 状态*/
    public void setStatus(StatusEnum status){
        this.status =status;
    }
    /** 分类ID*/
    public Long getTmpCategoryId(){
        return tmpCategoryId;
    }
    /** 分类ID*/
    public void setTmpCategoryId(Long tmpCategoryId){
        this.tmpCategoryId =tmpCategoryId;
    }
    /** 属性名称*/
    public String getTmpPropertyName(){
        return tmpPropertyName;
    }
    /** 属性名称*/
    public void setTmpPropertyName(String tmpPropertyName){
        this.tmpPropertyName =tmpPropertyName;
    }
    /** 值名称*/
    public String getValueName(){
        return valueName;
    }
    /** 值名称*/
    public void setValueName(String valueName){
        this.valueName =valueName;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}