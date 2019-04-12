package cn.farwalker.ravv.service.sys.operationlog.model;
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
import cn.farwalker.ravv.service.sys.operationlog.biz.ISysOperationLogBiz;

/**
 * 操作日志（用于记录基本配置的系统后台：admin的logs；业务运营后台：admin-rest的logs写在：sys_logs）
 * 
 * @author generateModel.java
 */
@TableName(SysOperationLogBo.TABLE_NAME)
@DDLTable(name=SysOperationLogBo.TABLE_NAME,comment="操作日志")
public class SysOperationLogBo extends Model<SysOperationLogBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        classname("classname"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        logname("logname"),
        logtype("logtype"),
        message("message"),
        method("method"),
        remark("remark"),
        
        succeed("succeed"),
        userid("userid");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:sys_operation_log*/
    public static final String TABLE_NAME = "sys_operation_log";
    private static final long serialVersionUID = -1872441961L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键")
    private Long id;

    @TableField("classname")
    @DDLColumn(name="classname",comment="类名称",length=255)
    private String classname;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("logname")
    @DDLColumn(name="logname",comment="日志名称",length=255)
    private String logname;

    @TableField("logtype")
    @DDLColumn(name="logtype",comment="日志类型",length=63)
    private String logtype;

    @TableField("message")
    @DDLColumn(name="message",comment="备注",length=65535)
    private String message;

    @TableField("method")
    @DDLColumn(name="method",comment="方法名称",length=65535)
    private String method;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("succeed")
    @DDLColumn(name="succeed",comment="是否成功",length=63)
    private String succeed;

    @TableField("userid")
    @DDLColumn(name="userid",comment="用户ID")
    private Long userid;
    /** 主键*/
    public Long getId(){
        return id;
    }
    /** 主键*/
    public void setId(Long id){
        this.id =id;
    }
    /** 类名称*/
    public String getClassname(){
        return classname;
    }
    /** 类名称*/
    public void setClassname(String classname){
        this.classname =classname;
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
    /** 日志名称*/
    public String getLogname(){
        return logname;
    }
    /** 日志名称*/
    public void setLogname(String logname){
        this.logname =logname;
    }
    /** 日志类型*/
    public String getLogtype(){
        return logtype;
    }
    /** 日志类型*/
    public void setLogtype(String logtype){
        this.logtype =logtype;
    }
    /** 备注*/
    public String getMessage(){
        return message;
    }
    /** 备注*/
    public void setMessage(String message){
        this.message =message;
    }
    /** 方法名称*/
    public String getMethod(){
        return method;
    }
    /** 方法名称*/
    public void setMethod(String method){
        this.method =method;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 是否成功*/
    public String getSucceed(){
        return succeed;
    }
    /** 是否成功*/
    public void setSucceed(String succeed){
        this.succeed =succeed;
    }
    /** 用户ID*/
    public Long getUserid(){
        return userid;
    }
    /** 用户ID*/
    public void setUserid(Long userid){
        this.userid =userid;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}