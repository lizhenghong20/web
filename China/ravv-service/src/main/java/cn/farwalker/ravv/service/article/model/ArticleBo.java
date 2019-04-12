package cn.farwalker.ravv.service.article.model;
import java.util.Date;
import java.util.List;
import com.cangwu.frame.orm.core.BaseBo;
import com.cangwu.frame.orm.core.annotation.LoadJoinValue;
import com.cangwu.frame.orm.ddl.annotation.DDLColumn;
import com.cangwu.frame.orm.ddl.annotation.DDLTable;

import cn.farwalker.ravv.service.article.biz.IArticleBiz;

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
 * 文章
 * 
 * @author generateModel.java
 */
@TableName(ArticleBo.TABLE_NAME)
@DDLTable(name=ArticleBo.TABLE_NAME,comment="文章")
public class ArticleBo extends Model<ArticleBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        category("category"),
        click("click"),
        content("content"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        remark("remark"),
        thumb("thumb"),
        thumbImageUrl("thumb_image_url"),
        
        title("title"),
        quan("quan");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:article*/
    public static final String TABLE_NAME = "article";
    private static final long serialVersionUID = -1731489853L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键")
    private Long id;

    @TableField("category")
    @DDLColumn(name="category",comment="分类")
    private Integer category;

    @TableField("click")
    @DDLColumn(name="click",comment="点击数")
    private Integer click;

    @TableField("content")
    @DDLColumn(name="content",comment="内容",length=65535)
    private String content;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("thumb")
    @DDLColumn(name="thumb",comment="缩略内容",length=255)
    private String thumb;

    @TableField("thumb_image_url")
    @DDLColumn(name="thumb_image_url",comment="缩略内容图片地址",length=1024)
    private String thumbImageUrl;

    @TableField("title")
    @DDLColumn(name="title",comment="标题",length=255)
    private String title;

    @TableField("quan")
    @DDLColumn(name="quan",comment="数量")
    private Integer quan;
    /** 主键*/
    public Long getId(){
        return id;
    }
    /** 主键*/
    public void setId(Long id){
        this.id =id;
    }
    /** 分类*/
    public Integer getCategory(){
        return category;
    }
    /** 分类*/
    public void setCategory(Integer category){
        this.category =category;
    }
    /** 点击数*/
    public Integer getClick(){
        return click;
    }
    /** 点击数*/
    public void setClick(Integer click){
        this.click =click;
    }
    /** 内容*/
    public String getContent(){
        return content;
    }
    /** 内容*/
    public void setContent(String content){
        this.content =content;
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
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 缩略内容*/
    public String getThumb(){
        return thumb;
    }
    /** 缩略内容*/
    public void setThumb(String thumb){
        this.thumb =thumb;
    }
    /** 缩略内容图片地址*/
    public String getThumbImageUrl(){
        return thumbImageUrl;
    }
    /** 缩略内容图片地址*/
    public void setThumbImageUrl(String thumbImageUrl){
        this.thumbImageUrl =thumbImageUrl;
    }
    /** 标题*/
    public String getTitle(){
        return title;
    }
    /** 标题*/
    public void setTitle(String title){
        this.title =title;
    }
    /** 数量*/
    public Integer getQuan(){
        return quan;
    }
    /** 数量*/
    public void setQuan(Integer quan){
        this.quan =quan;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}