package cn.farwalker.ravv.service.member.pam.member.model;
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
import cn.farwalker.ravv.service.member.pam.member.biz.IPamMemberBiz;

/**
 * PAM_会员登录账号
 * 
 * @author generateModel.java
 */
@TableName(PamMemberBo.TABLE_NAME)
@DDLTable(name=PamMemberBo.TABLE_NAME,comment="PAM_会员登录账号")
public class PamMemberBo extends Model<PamMemberBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        appendAccount("append_account"),
        emailAccount("email_account"),
        enabled("enabled"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        memberId("member_id"),
        password("password"),
        remark("remark"),
        
        salt("salt"),
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
    /** 数据表名:pam_member*/
    public static final String TABLE_NAME = "pam_member";
    private static final long serialVersionUID = 362001699L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键")
    private Long id;

    @TableField("append_account")
    @DDLColumn(name="append_account",comment="附加账号",length=255)
    private String appendAccount;

    @TableField("email_account")
    @DDLColumn(name="email_account",comment="邮箱账号",length=512)
    private String emailAccount;

    @TableField("enabled")
    @DDLColumn(name="enabled",comment="启用状态")
    private Boolean enabled;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("member_id")
    @DDLColumn(name="member_id",comment="会员ID")
    private Long memberId;

    @TableField("password")
    @DDLColumn(name="password",comment="登录密码")
    private String password;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("salt")
    @DDLColumn(name="salt",comment="密码盐")
    private String salt;

    @TableField("type")
    @DDLColumn(name="type",comment="账户类型,商户型，非商户型")
    private Integer type;
    /** 主键*/
    public Long getId(){
        return id;
    }
    /** 主键*/
    public void setId(Long id){
        this.id =id;
    }
    /** 附加账号*/
    public String getAppendAccount(){
        return appendAccount;
    }
    /** 附加账号*/
    public void setAppendAccount(String appendAccount){
        this.appendAccount =appendAccount;
    }
    /** 邮箱账号*/
    public String getEmailAccount(){
        return emailAccount;
    }
    /** 邮箱账号*/
    public void setEmailAccount(String emailAccount){
        this.emailAccount =emailAccount;
    }
    /** 启用状态*/
    public Boolean getEnabled(){
        return enabled;
    }
    /** 启用状态*/
    public void setEnabled(Boolean enabled){
        this.enabled =enabled;
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
    /** 会员ID*/
    public Long getMemberId(){
        return memberId;
    }
    /** 会员ID*/
    public void setMemberId(Long memberId){
        this.memberId =memberId;
    }
    /** 登录密码*/
    public String getPassword(){
        return password;
    }
    /** 登录密码*/
    public void setPassword(String password){
        this.password =password;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 密码盐*/
    public String getSalt(){
        return salt;
    }
    /** 密码盐*/
    public void setSalt(String salt){
        this.salt =salt;
    }
    /** 账户类型,商户型，非商户型*/
    public Integer getType(){
        return type;
    }
    /** 账户类型,商户型，非商户型*/
    public void setType(Integer type){
        this.type =type;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}