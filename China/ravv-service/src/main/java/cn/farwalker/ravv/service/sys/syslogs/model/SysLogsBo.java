package cn.farwalker.ravv.service.sys.syslogs.model;
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
 * 系统操作日志（用于记录业务运营后台：admin-rest的logs；基本配置的系统后台：admin的logs写在sys_operation_log）
 * 
 * @author generateModel.java
 */
@TableName(SysLogsBo.TABLE_NAME)
@DDLTable(name=SysLogsBo.TABLE_NAME,comment="系统操作日志")
public class SysLogsBo extends Model<SysLogsBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        sourcetable("sourcetable"),
        sourcefield("sourcefield"),
        sourceid("sourceid"),
        content("content"),
        json("json"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:sys_logs*/
    public static final String TABLE_NAME = "sys_logs";
    private static final long serialVersionUID = 1997003625L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键",length=20)
    private Long id;

    @TableField("sourcetable")
    @DDLColumn(name="sourcetable",comment="来源表名",length=45)
    private String sourcetable;

    @TableField("sourcefield")
    @DDLColumn(name="sourcefield",comment="来源字段名",length=45)
    private String sourcefield;

    @TableField("sourceid")
    @DDLColumn(name="sourceid",comment="来源字段id")
    private String sourceid;

    @TableField("content")
    @DDLColumn(name="content",comment="日志内容",length=1024)
    private String content;

    @TableField("json")
    @DDLColumn(name="json",comment="",length=63)
    private String json;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;
    /** 主键*/
    public Long getId(){
        return id;
    }
    /** 主键*/
    public void setId(Long id){
        this.id =id;
    }
    /** 来源表名*/
    public String getSourcetable(){
        return sourcetable;
    }
    /** 来源表名*/
    public void setSourcetable(String sourcetable){
        this.sourcetable =sourcetable;
    }
    /** 来源字段名*/
    public String getSourcefield(){
        return sourcefield;
    }
    /** 来源字段名*/
    public void setSourcefield(String sourcefield){
        this.sourcefield =sourcefield;
    }
    /** 来源字段id*/
    public String getSourceid(){
        return sourceid;
    }
    /** 来源字段id*/
    public void setSourceid(String sourceid){
        this.sourceid =sourceid;
    }
    /** 日志内容*/
    public String getContent(){
        return content;
    }
    /** 日志内容*/
    public void setContent(String content){
        this.content =content;
    }
    /** */
    public String getJson(){
        return json;
    }
    /** */
    public void setJson(String json){
        this.json =json;
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
    @Override
    protected Serializable pkVal(){
        return id;
    }
}