package cn.farwalker.ravv.service.member.pam.loginlog.model;
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
import cn.farwalker.ravv.service.member.pam.loginlog.biz.IPamLoginLogBiz;

/**
 * PAM_会员登录日志
 * 
 * @author generateModel.java
 */
@TableName(PamLoginLogBo.TABLE_NAME)
@DDLTable(name=PamLoginLogBo.TABLE_NAME,comment="PAM_会员登录日志")
public class PamLoginLogBo extends Model<PamLoginLogBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        ip("ip"),
        logname("logname"),
        memberId("member_id"),
        message("message"),
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
    /** 数据表名:pam_login_log*/
    public static final String TABLE_NAME = "pam_login_log";
    private static final long serialVersionUID = -432345500L;

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
    @DDLColumn(name="ip",comment="登录ip",length=127)
    private String ip;

    @TableField("logname")
    @DDLColumn(name="logname",comment="日志名称",length=255)
    private String logname;

    @TableField("member_id")
    @DDLColumn(name="member_id",comment="会员ID")
    private Long memberId;

    @TableField("message")
    @DDLColumn(name="message",comment="具体消息",length=65535)
    private String message;

    @TableField("pam_member_id")
    @DDLColumn(name="pam_member_id",comment="登录账号ID")
    private Long pamMemberId;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("succeed")
    @DDLColumn(name="succeed",comment="是否执行成功")
    private Integer succeed;
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
    /** 会员ID*/
    public Long getMemberId(){
        return memberId;
    }
    /** 会员ID*/
    public void setMemberId(Long memberId){
        this.memberId =memberId;
    }
    /** 具体消息*/
    public String getMessage(){
        return message;
    }
    /** 具体消息*/
    public void setMessage(String message){
        this.message =message;
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
    /** 是否执行成功*/
    public Integer getSucceed(){
        return succeed;
    }
    /** 是否执行成功*/
    public void setSucceed(Integer succeed){
        this.succeed =succeed;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}