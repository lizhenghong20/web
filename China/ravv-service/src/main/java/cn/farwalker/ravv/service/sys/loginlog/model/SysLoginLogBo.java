package cn.farwalker.ravv.service.sys.loginlog.model;
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
import cn.farwalker.ravv.service.sys.loginlog.biz.ISysLoginLogBiz;

/**
 * 登录记录
 * 
 * @author generateModel.java
 */
@TableName(SysLoginLogBo.TABLE_NAME)
@DDLTable(name=SysLoginLogBo.TABLE_NAME,comment="登录记录")
public class SysLoginLogBo extends Model<SysLoginLogBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        ip("ip"),
        logname("logname"),
        message("message"),
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
    /** 数据表名:sys_login_log*/
    public static final String TABLE_NAME = "sys_login_log";
    private static final long serialVersionUID = -406342827L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键")
    private Long id;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("ip")
    @DDLColumn(name="ip",comment="登录ip",length=63)
    private String ip;

    @TableField("logname")
    @DDLColumn(name="logname",comment="日志名称",length=255)
    private String logname;

    @TableField("message")
    @DDLColumn(name="message",comment="具体消息",length=65535)
    private String message;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("succeed")
    @DDLColumn(name="succeed",comment="是否执行成功",length=63)
    private String succeed;

    @TableField("userid")
    @DDLColumn(name="userid",comment="用户id")
    private Long userid;
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
    /** 登录ip*/
    public String getIp(){
        return ip;
    }
    /** 登录ip*/
    public void setIp(String ip){
        this.ip =ip;
    }
    /** 日志名称*/
    public String getLogname(){
        return logname;
    }
    /** 日志名称*/
    public void setLogname(String logname){
        this.logname =logname;
    }
    /** 具体消息*/
    public String getMessage(){
        return message;
    }
    /** 具体消息*/
    public void setMessage(String message){
        this.message =message;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 是否执行成功*/
    public String getSucceed(){
        return succeed;
    }
    /** 是否执行成功*/
    public void setSucceed(String succeed){
        this.succeed =succeed;
    }
    /** 用户id*/
    public Long getUserid(){
        return userid;
    }
    /** 用户id*/
    public void setUserid(Long userid){
        this.userid =userid;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}