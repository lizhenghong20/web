package cn.farwalker.ravv.service.member.pam.operationlog.model;
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
import cn.farwalker.ravv.service.member.pam.operationlog.biz.IPamOperationLogBiz;

/**
 * PAM_操作日志
 * 
 * @author generateModel.java
 */
@TableName(PamOperationLogBo.TABLE_NAME)
@DDLTable(name=PamOperationLogBo.TABLE_NAME,comment="PAM_操作日志")
public class PamOperationLogBo extends Model<PamOperationLogBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        classname("classname"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        logname("logname"),
        logtype("logtype"),
        memberId("member_id"),
        message("message"),
        method("method"),
        
        pamMemberId("pam_member_id"),
        remark("remark"),
        succeed("succeed");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:pam_operation_log*/
    public static final String TABLE_NAME = "pam_operation_log";
    private static final long serialVersionUID = 1570105638L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键")
    private Long id;

    @TableField("classname")
    @DDLColumn(name="classname",comment="类名称",length=511)
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
    @DDLColumn(name="logtype",comment="日志类型")
    private Integer logtype;

    @TableField("member_id")
    @DDLColumn(name="member_id",comment="会员ID")
    private Long memberId;

    @TableField("message")
    @DDLColumn(name="message",comment="具体内容",length=65535)
    private String message;

    @TableField("method")
    @DDLColumn(name="method",comment="方法名称",length=127)
    private String method;

    @TableField("pam_member_id")
    @DDLColumn(name="pam_member_id",comment="登录账号ID")
    private Long pamMemberId;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("succeed")
    @DDLColumn(name="succeed",comment="是否成功")
    private Integer succeed;
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
    public Integer getLogtype(){
        return logtype;
    }
    /** 日志类型*/
    public void setLogtype(Integer logtype){
        this.logtype =logtype;
    }
    /** 会员ID*/
    public Long getMemberId(){
        return memberId;
    }
    /** 会员ID*/
    public void setMemberId(Long memberId){
        this.memberId =memberId;
    }
    /** 具体内容*/
    public String getMessage(){
        return message;
    }
    /** 具体内容*/
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
    /** 登录账号ID*/
    public Long getPamMemberId(){
        return pamMemberId;
    }
    /** 登录账号ID*/
    public void setPamMemberId(Long pamMemberId){
        this.pamMemberId =pamMemberId;
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
    public Integer getSucceed(){
        return succeed;
    }
    /** 是否成功*/
    public void setSucceed(Integer succeed){
        this.succeed =succeed;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}