package cn.farwalker.ravv.service.sys.notice.model;
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
import cn.farwalker.ravv.service.sys.notice.biz.ISysNoticeBiz;

/**
 * 通知表
 * 
 * @author generateModel.java
 */
@TableName(SysNoticeBo.TABLE_NAME)
@DDLTable(name=SysNoticeBo.TABLE_NAME,comment="通知表")
public class SysNoticeBo extends Model<SysNoticeBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        content("content"),
        creater("creater"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        remark("remark"),
        title("title"),
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
    /** 数据表名:sys_notice*/
    public static final String TABLE_NAME = "sys_notice";
    private static final long serialVersionUID = -1175324910L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键")
    private Long id;

    @TableField("content")
    @DDLColumn(name="content",comment="内容",length=65535)
    private String content;

    @TableField("creater")
    @DDLColumn(name="creater",comment="创建人id")
    private Long creater;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("title")
    @DDLColumn(name="title",comment="标题",length=255)
    private String title;

    @TableField("type")
    @DDLColumn(name="type",comment="类型",length=63)
    private String type;
    /** 主键*/
    public Long getId(){
        return id;
    }
    /** 主键*/
    public void setId(Long id){
        this.id =id;
    }
    /** 内容*/
    public String getContent(){
        return content;
    }
    /** 内容*/
    public void setContent(String content){
        this.content =content;
    }
    /** 创建人id*/
    public Long getCreater(){
        return creater;
    }
    /** 创建人id*/
    public void setCreater(Long creater){
        this.creater =creater;
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
    /** 标题*/
    public String getTitle(){
        return title;
    }
    /** 标题*/
    public void setTitle(String title){
        this.title =title;
    }
    /** 类型*/
    public String getType(){
        return type;
    }
    /** 类型*/
    public void setType(String type){
        this.type =type;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}