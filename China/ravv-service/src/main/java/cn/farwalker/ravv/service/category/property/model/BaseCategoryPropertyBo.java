package cn.farwalker.ravv.service.category.property.model;
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

import cn.farwalker.ravv.service.category.property.biz.IBaseCategoryPropertyBiz;
import cn.farwalker.ravv.service.category.property.constants.PropertyTypeEnum;
import cn.farwalker.waka.constants.StatusEnum;

/**
 * 类目与属性的关联
 * 
 * @author generateModel.java
 */
@TableName(BaseCategoryPropertyBo.TABLE_NAME)
@DDLTable(name=BaseCategoryPropertyBo.TABLE_NAME,comment="类目与属性的关联")
public class BaseCategoryPropertyBo extends Model<BaseCategoryPropertyBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        catId("cat_id"),
        feature("feature"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        isimage("isimage"),
        manuallyInput("manually_input"),
        multiSelect("multi_select"),
        options("options"),
        
        propertyName("property_name"),
        remark("remark"),
        required("required"),
        sequence("sequence"),
        status("status"),
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
    /** 数据表名:base_category_property*/
    public static final String TABLE_NAME = "base_category_property";
    private static final long serialVersionUID = -1435536015L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键")
    private Long id;

    @TableField("cat_id")
    @DDLColumn(name="cat_id",comment="分类ID")
    private Long catId;

    @TableField("feature")
    @DDLColumn(name="feature",comment="key:value扩展",length=2000)
    private String feature;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("isimage")
    @DDLColumn(name="isimage",comment="是否图片展示")
    private Boolean isimage;

    @TableField("manually_input")
    @DDLColumn(name="manually_input",comment="是否手工输入属性")
    private Boolean manuallyInput;

    @TableField("multi_select")
    @DDLColumn(name="multi_select",comment="是否多个属性值")
    private Boolean multiSelect;

    @TableField("options")
    @DDLColumn(name="options",comment="位扩展，第一位表示能否填写其他自定义值")
    private Long options;

    @TableField("property_name")
    @DDLColumn(name="property_name",comment="属性名称",length=255)
    private String propertyName;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("required")
    @DDLColumn(name="required",comment="是否必填属性")
    private Boolean required;

    @TableField("sequence")
    @DDLColumn(name="sequence",comment="排序")
    private Integer sequence;

    @TableField("status")
    @DDLColumn(name="status",comment="状态",length=63)
    private StatusEnum status;

    @TableField("type")
    @DDLColumn(name="type",comment="属性类别",length=63)
    private PropertyTypeEnum type;
    /** 主键*/
    public Long getId(){
        return id;
    }
    /** 主键*/
    public void setId(Long id){
        this.id =id;
    }
    /** 分类ID*/
    public Long getCatId(){
        return catId;
    }
    /** 分类ID*/
    public void setCatId(Long catId){
        this.catId =catId;
    }
    /** key:value扩展:value扩展*/
    public String getFeature(){
        return feature;
    }
    /** key:value扩展:value扩展*/
    public void setFeature(String feature){
        this.feature =feature;
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
    /** 是否图片展示*/
    public Boolean getIsimage(){
        return isimage;
    }
    /** 是否图片展示*/
    public void setIsimage(Boolean isimage){
        this.isimage =isimage;
    }
    /** 是否手工输入属性*/
    public Boolean getManuallyInput(){
        return manuallyInput;
    }
    /** 是否手工输入属性*/
    public void setManuallyInput(Boolean manuallyInput){
        this.manuallyInput =manuallyInput;
    }
    /** 是否多个属性值*/
    public Boolean getMultiSelect(){
        return multiSelect;
    }
    /** 是否多个属性值*/
    public void setMultiSelect(Boolean multiSelect){
        this.multiSelect =multiSelect;
    }
    /** 位扩展，第一位表示能否填写其他自定义值*/
    public Long getOptions(){
        return options;
    }
    /** 位扩展，第一位表示能否填写其他自定义值*/
    public void setOptions(Long options){
        this.options =options;
    }
    /** 属性名称*/
    public String getPropertyName(){
        return propertyName;
    }
    /** 属性名称*/
    public void setPropertyName(String propertyName){
        this.propertyName =propertyName;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 是否必填属性*/
    public Boolean getRequired(){
        return required;
    }
    /** 是否必填属性*/
    public void setRequired(Boolean required){
        this.required =required;
    }
    /** 排序*/
    public Integer getSequence(){
        return sequence;
    }
    /** 排序*/
    public void setSequence(Integer sequence){
        this.sequence =sequence;
    }
    /** 状态*/
    public StatusEnum getStatus(){
        return status;
    }
    /** 状态*/
    public void setStatus(StatusEnum status){
        this.status =status;
    }
    /** 属性类别*/
    public PropertyTypeEnum getType(){
        return type;
    }
    /** 属性类别*/
    public void setType(PropertyTypeEnum type){
        this.type =type;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}