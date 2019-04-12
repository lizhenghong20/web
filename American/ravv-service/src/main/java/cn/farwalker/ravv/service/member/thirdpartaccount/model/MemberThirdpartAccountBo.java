package cn.farwalker.ravv.service.member.thirdpartaccount.model;

import com.cangwu.frame.orm.core.BaseBo;
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
import java.util.Date;
/**import more*/
/**
 * 会员第三方账号绑定
 * @author generateModel.java
 */
@TableName(MemberThirdpartAccountBo.TABLE_NAME)
@DDLTable(name=MemberThirdpartAccountBo.TABLE_NAME,comment="会员第三方账号绑定")
public class MemberThirdpartAccountBo extends Model<MemberThirdpartAccountBo> implements BaseBo{

    /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        account("account"),
        accountInformation("account_information"),
        accountType("account_type"),
        bindTime("bind_time"),
        email("email"),
        enable("enable"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        
        memberId("member_id"),
        remark("remark"),
        unbindTime("unbind_time"),
        validateStatus("validate_status");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    private static final long serialVersionUID = -1928601139L;
    /**数据表名*/
    public static final String TABLE_NAME = "member_thirdpart_account";

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键")
    private  Long id;
    @TableField("account")
    @DDLColumn(name="account",comment="第三方账号",length=255)
    private  String account;
    @TableField("account_information")
    @DDLColumn(name="account_information",comment="第三方账号昵称",length=65535)
    private  String accountInformation;
    @TableField("account_type")
    @DDLColumn(name="account_type",comment="第三方账号类型:facebook账户，google账户。")
    private  Integer accountType;
    @TableField("bind_time")
    @DDLColumn(name="bind_time",comment="绑定时间")
    private  Date bindTime;
    @TableField("email")
    @DDLColumn(name="email",comment="邮箱",length=512)
    private  String email;
    @TableField("enable")
    @DDLColumn(name="enable",comment="是否启用")
    private  Integer enable;
    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private  Date gmtCreate;
    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private  Date gmtModified;
    @TableField("member_id")

    @DDLColumn(name="member_id",comment="会员ID")
    private  Long memberId;
    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private  String remark;
    @TableField("unbind_time")
    @DDLColumn(name="unbind_time",comment="解除绑定时间")
    private  Date unbindTime;
    @TableField("validate_status")
    @DDLColumn(name="validate_status",comment="验证状态",length=63)
    private  String validateStatus;

    /** 主键*/
    public Long getId(){
        return this.id;
    }

    /** 主键*/
    public void setId(Long v){
        this.id =v;
    }

    /** 第三方账号*/
    public String getAccount(){
        return this.account;
    }

    /** 第三方账号*/
    public void setAccount(String v){
        this.account =v;
    }

    /** 第三方账号昵称*/
    public String getAccountInformation(){
        return this.accountInformation;
    }

    /** 第三方账号昵称*/
    public void setAccountInformation(String v){
        this.accountInformation =v;
    }

    /** 第三方账号类型:facebook账户，google账户。:facebook账户，google账户。*/
    public Integer getAccountType(){
        return this.accountType;
    }

    /** 第三方账号类型:facebook账户，google账户。:facebook账户，google账户。*/
    public void setAccountType(Integer v){
        this.accountType =v;
    }

    /** 绑定时间*/
    public Date getBindTime(){
        return this.bindTime;
    }

    /** 绑定时间*/
    public void setBindTime(Date v){
        this.bindTime =v;
    }

    /** 邮箱*/
    public String getEmail(){
        return this.email;
    }

    /** 邮箱*/
    public void setEmail(String v){
        this.email =v;
    }

    /** 是否启用*/
    public Integer getEnable(){
        return this.enable;
    }

    /** 是否启用*/
    public void setEnable(Integer v){
        this.enable =v;
    }

    /** 创建时间*/
    public Date getGmtCreate(){
        return this.gmtCreate;
    }

    /** 创建时间*/
    public void setGmtCreate(Date v){
        this.gmtCreate =v;
    }

    /** 修改时间*/
    public Date getGmtModified(){
        return this.gmtModified;
    }

    /** 修改时间*/
    public void setGmtModified(Date v){
        this.gmtModified =v;
    }

    /** 会员ID*/
    public Long getMemberId(){
        return this.memberId;
    }

    /** 会员ID*/
    public void setMemberId(Long v){
        this.memberId =v;
    }

    /** 备注*/
    public String getRemark(){
        return this.remark;
    }

    /** 备注*/
    public void setRemark(String v){
        this.remark =v;
    }

    /** 解除绑定时间*/
    public Date getUnbindTime(){
        return this.unbindTime;
    }

    /** 解除绑定时间*/
    public void setUnbindTime(Date v){
        this.unbindTime =v;
    }

    /** 验证状态*/
    public String getValidateStatus(){
        return this.validateStatus;
    }

    /** 验证状态*/
    public void setValidateStatus(String v){
        this.validateStatus =v;
    }
    @Override
    protected Serializable pkVal(){
        return this.id;
    }
}
