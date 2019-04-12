package cn.farwalker.ravv.service.web.slider.model;
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
import cn.farwalker.ravv.service.web.slider.biz.IWebSliderBiz;
import cn.farwalker.ravv.service.web.slider.constants.JumpTypeEnum;
import cn.farwalker.ravv.service.web.slider.constants.PageNameEnum;

/**
 * 首页及其他页面的轮播
 * 
 * @author generateModel.java
 */
@TableName(WebSliderBo.TABLE_NAME)
@DDLTable(name=WebSliderBo.TABLE_NAME,comment="首页及其他页面的轮播")
public class WebSliderBo extends Model<WebSliderBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        jumpParams("jump_params"),
        jumpType("jump_type"),
        pageName("page_name"),
        picture("picture"),
        remark("remark"),
        sequence("sequence"),
        
        status("status"),
        title("title");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:web_slider*/
    public static final String TABLE_NAME = "web_slider";
    private static final long serialVersionUID = 1961168994L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="id")
    private Long id;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("jump_params")
    @DDLColumn(name="jump_params",comment="跳转参数(根据跳转类型约定)",length=127)
    private String jumpParams;

    @TableField("jump_type")
    @DDLColumn(name="jump_type",comment="跳转到那个页面枚举",length=63)
    private JumpTypeEnum jumpType;

    @TableField("page_name")
    @DDLColumn(name="page_name",comment="页面名称(首页、分类等)",length=63)
    private PageNameEnum pageName;

    @TableField("picture")
    @DDLColumn(name="picture",comment="图片",length=1024)
    private String picture;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("sequence")
    @DDLColumn(name="sequence",comment="顺序")
    private Integer sequence;

    @TableField("status")
    @DDLColumn(name="status",comment="状态(是否有效)")
    private Boolean status;

    @TableField("title")
    @DDLColumn(name="title",comment="标题",length=255)
    private String title;
    /** id*/
    public Long getId(){
        return id;
    }
    /** id*/
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
    /** 跳转参数(根据跳转类型约定)*/
    public String getJumpParams(){
        return jumpParams;
    }
    /** 跳转参数(根据跳转类型约定)*/
    public void setJumpParams(String jumpParams){
        this.jumpParams =jumpParams;
    }
    /** 跳转到那个页面枚举*/
    public JumpTypeEnum getJumpType(){
        return jumpType;
    }
    /** 跳转到那个页面枚举*/
    public void setJumpType(JumpTypeEnum jumpType){
        this.jumpType =jumpType;
    }
    /** 页面名称(首页、分类等)*/
    public PageNameEnum getPageName(){
        return pageName;
    }
    /** 页面名称(首页、分类等)*/
    public void setPageName(PageNameEnum pageName){
        this.pageName =pageName;
    }
    /** 图片*/
    public String getPicture(){
        return picture;
    }
    /** 图片*/
    public void setPicture(String picture){
        this.picture =picture;
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
    /** 状态(是否有效)*/
    public Boolean getStatus(){
        return status;
    }
    /** 状态(是否有效)*/
    public void setStatus(Boolean status){
        this.status =status;
    }
    /** 标题*/
    public String getTitle(){
        return title;
    }
    /** 标题*/
    public void setTitle(String title){
        this.title =title;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}