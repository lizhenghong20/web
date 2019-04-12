package cn.farwalker.ravv.service.web.menu.model;
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
 * 前端的分类标签
 * 
 * @author generateModel.java
 */
@TableName(WebMenuBo.TABLE_NAME)
@DDLTable(name=WebMenuBo.TABLE_NAME,comment="前端的分类标签")
public class WebMenuBo extends Model<WebMenuBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        level("level"),
        picture1("picture1"),
        picture2("picture2"),
        sequence("sequence"),
        title("title"),
        title2("title2"),
        visible("visible"),
        parentid("parentid"),
        
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        paths("paths"),
        leaf("leaf"),
        logo("logo");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:web_menu*/
    public static final String TABLE_NAME = "web_menu";
    private static final long serialVersionUID = 677595744L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="")
    private Long id;

    @TableField("level")
    @DDLColumn(name="level",comment="等级")
    private Integer level;

    @TableField("picture1")
    @DDLColumn(name="picture1",comment="正方形的图片",length=1024)
    private String picture1;

    @TableField("picture2")
    @DDLColumn(name="picture2",comment="长方形的图片",length=1024)
    private String picture2;

    @TableField("sequence")
    @DDLColumn(name="sequence",comment="顺序")
    private Integer sequence;

    @TableField("title")
    @DDLColumn(name="title",comment="标题",length=255)
    private String title;

    @TableField("title2")
    @DDLColumn(name="title2",comment="副标题",length=255)
    private String title2;

    @TableField("visible")
    @DDLColumn(name="visible",comment="是否显示")
    private Boolean visible;

    @TableField("parentid")
    @DDLColumn(name="parentid",comment="上级编码")
    private Long parentid;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="")
    private Date gmtModified;

    @TableField("paths")
    @DDLColumn(name="paths",comment="节点路径(相当于分级编码)",length=255)
    private String paths;

    @TableField("leaf")
    @DDLColumn(name="leaf",comment="是否叶子")
    private Boolean leaf;

    @TableField("logo")
    @DDLColumn(name="logo",comment="菜单logo",length=1024)
    private String logo;
    /** null*/
    public Long getId(){
        return id;
    }
    /** null*/
    public void setId(Long id){
        this.id =id;
    }
    /** 等级*/
    public Integer getLevel(){
        return level;
    }
    /** 等级*/
    public void setLevel(Integer level){
        this.level =level;
    }
    /** 正方形的图片*/
    public String getPicture1(){
        return picture1;
    }
    /** 正方形的图片*/
    public void setPicture1(String picture1){
        this.picture1 =picture1;
    }
    /** 长方形的图片*/
    public String getPicture2(){
        return picture2;
    }
    /** 长方形的图片*/
    public void setPicture2(String picture2){
        this.picture2 =picture2;
    }
    /** 顺序*/
    public Integer getSequence(){
        return sequence;
    }
    /** 顺序*/
    public void setSequence(Integer sequence){
        this.sequence =sequence;
    }
    /** 标题*/
    public String getTitle(){
        return title;
    }
    /** 标题*/
    public void setTitle(String title){
        this.title =title;
    }
    /** 副标题*/
    public String getTitle2(){
        return title2;
    }
    /** 副标题*/
    public void setTitle2(String title2){
        this.title2 =title2;
    }
    /** 是否显示*/
    public Boolean getVisible(){
        return visible;
    }
    /** 是否显示*/
    public void setVisible(Boolean visible){
        this.visible =visible;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
    /** 上级编码*/
    public Long getParentid(){
        return parentid;
    }
    /** 上级编码*/
    public void setParentid(Long parentid){
        this.parentid =parentid;
    }
    /** null*/
    public Date getGmtCreate(){
        return gmtCreate;
    }
    /** null*/
    public void setGmtCreate(Date gmtCreate){
        this.gmtCreate =gmtCreate;
    }
    /** null*/
    public Date getGmtModified(){
        return gmtModified;
    }
    /** null*/
    public void setGmtModified(Date gmtModified){
        this.gmtModified =gmtModified;
    }
    /** 节点路径(相当于分级编码)*/
    public String getPaths(){
        return paths;
    }
    /** 节点路径(相当于分级编码)*/
    public void setPaths(String paths){
        this.paths =paths;
    }
    /** 是否叶子*/
    public Boolean getLeaf(){
        return leaf;
    }
    /** 是否叶子*/
    public void setLeaf(Boolean leaf){
        this.leaf =leaf;
    }
    /** 菜单logo*/
    public String getLogo(){
        return logo;
    }
    /** 菜单logo*/
    public void setLogo(String logo){
        this.logo =logo;
    }
}