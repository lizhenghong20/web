package cn.farwalker.ravv.service.category.basecategory.model;
import java.util.Date;


import cn.farwalker.waka.constants.StatusEnum;
import com.cangwu.frame.orm.core.BaseBo;
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


/**
 * 类目基础信息
 * 
 * @author generateModel.java
 */
@TableName(BaseCategoryBo.TABLE_NAME)
@DDLTable(name=BaseCategoryBo.TABLE_NAME,comment="类目基础信息")
public class BaseCategoryBo extends Model<BaseCategoryBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        catName("cat_name"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        leaf("leaf"),
        pid("pid"),
        remark("remark"),
        sequence("sequence"),
        status("status"),
        
        paths("paths");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:base_category*/
    public static final String TABLE_NAME = "base_category";
    private static final long serialVersionUID = -1846240324L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="类目id主键")
    private Long id;

    @TableField("cat_name")
    @DDLColumn(name="cat_name",comment="类目名称",length=255)
    private String catName;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("leaf")
    @DDLColumn(name="leaf",comment="是否叶子类目")
    private Boolean leaf;

    @TableField("pid")
    @DDLColumn(name="pid",comment="父级ID")
    private Long pid;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("sequence")
    @DDLColumn(name="sequence",comment="排序")
    private Integer sequence;

    @TableField("status")
    @DDLColumn(name="status",comment="状态")
    private StatusEnum status;

    @TableField("paths")
    @DDLColumn(name="paths",comment="节点路径(相当于分级编码)",length=255)
    private String paths;
    /** 类目id主键*/
    public Long getId(){
        return id;
    }
    /** 类目id主键*/
    public void setId(Long id){
        this.id =id;
    }
    /** 类目名称*/
    public String getCatName(){
        return catName;
    }
    /** 类目名称*/
    public void setCatName(String catName){
        this.catName =catName;
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
    /** 是否叶子类目*/
    public Boolean getLeaf(){
        return leaf;
    }
    /** 是否叶子类目*/
    public void setLeaf(Boolean leaf){
        this.leaf =leaf;
    }
    /** 父级ID*/
    public Long getPid(){
        return pid;
    }
    /** 父级ID*/
    public void setPid(Long pid){
        this.pid =pid;
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
    /** 状态*/
    public StatusEnum getStatus(){
        return status;
    }
    /** 状态*/
    public void setStatus(StatusEnum status){
        this.status =status;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
    /** 节点路径(相当于分级编码,包含末级)*/
    public String getPaths(){
        return paths;
    }
    /** 节点路径(相当于分级编码,包含末级)*/
    public void setPaths(String paths){
        this.paths =paths;
    }
}