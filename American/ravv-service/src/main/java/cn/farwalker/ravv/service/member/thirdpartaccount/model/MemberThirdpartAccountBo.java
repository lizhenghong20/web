package cn.farwalker.ravv.service.member.thirdpartaccount.model;
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
import cn.farwalker.ravv.service.member.thirdpartaccount.biz.IMemberThirdpartAccountBiz;

/**
 * 会员第三方账号绑定
 * 
 * @author generateModel.java
 */
@TableName(MemberThirdpartAccountBo.TABLE_NAME)
@DDLTable(name=MemberThirdpartAccountBo.TABLE_NAME,comment="会员第三方账号绑定")
public class MemberThirdpartAccountBo extends Model<MemberThirdpartAccountBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        memberId("member_id"),
        userId("user_id"),
        accountType("account_type"),
        firstname("firstname"),
        lastname("lastname"),
        nickname("nickname"),
        avator("avator"),
        email("email"),
        
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        remark("remark");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:member_thirdpart_account*/
    public static final String TABLE_NAME = "member_thirdpart_account";
    private static final long serialVersionUID = 2040179450L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键")
    private Long id;

    @TableField("member_id")
    @DDLColumn(name="member_id",comment="会员ID")
    private Long memberId;

    @TableField("user_id")
    @DDLColumn(name="user_id",comment="第三方账号",length=255)
    private String userId;

    @TableField("account_type")
    @DDLColumn(name="account_type",comment="第三方账号类型:facebook账户，google账户。")
    private Integer accountType;

    @TableField("firstname")
    @DDLColumn(name="firstname",comment="名")
    private String firstname;

    @TableField("lastname")
    @DDLColumn(name="lastname",comment="姓")
    private String lastname;

    @TableField("nickname")
    @DDLColumn(name="nickname",comment="昵称")
    private String nickname;

    @TableField("avator")
    @DDLColumn(name="avator",comment="头像",length=255)
    private String avator;

    @TableField("email")
    @DDLColumn(name="email",comment="邮箱",length=512)
    private String email;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;
    /** 主键*/
    public Long getId(){
        return id;
    }
    /** 主键*/
    public void setId(Long id){
        this.id =id;
    }
    /** 会员ID*/
    public Long getMemberId(){
        return memberId;
    }
    /** 会员ID*/
    public void setMemberId(Long memberId){
        this.memberId =memberId;
    }
    /** 第三方账号*/
    public String getUserId(){
        return userId;
    }
    /** 第三方账号*/
    public void setUserId(String userId){
        this.userId =userId;
    }
    /** 第三方账号类型:facebook账户，google账户。*/
    public Integer getAccountType(){
        return accountType;
    }
    /** 第三方账号类型:facebook账户，google账户。*/
    public void setAccountType(Integer accountType){
        this.accountType =accountType;
    }
    /** 名*/
    public String getFirstname(){
        return firstname;
    }
    /** 名*/
    public void setFirstname(String firstname){
        this.firstname =firstname;
    }
    /** 姓*/
    public String getLastname(){
        return lastname;
    }
    /** 姓*/
    public void setLastname(String lastname){
        this.lastname =lastname;
    }
    /** 昵称*/
    public String getNickname(){
        return nickname;
    }
    /** 昵称*/
    public void setNickname(String nickname){
        this.nickname =nickname;
    }
    /** 头像*/
    public String getAvator(){
        return avator;
    }
    /** 头像*/
    public void setAvator(String avator){
        this.avator =avator;
    }
    /** 邮箱*/
    public String getEmail(){
        return email;
    }
    /** 邮箱*/
    public void setEmail(String email){
        this.email =email;
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
    @Override
    protected Serializable pkVal(){
        return id;
    }
}