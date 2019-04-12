package cn.farwalker.ravv.service.merchant.model;
import java.util.Date;
import java.util.List;
import com.cangwu.frame.orm.core.BaseBo;
import com.cangwu.frame.orm.core.annotation.LoadJoinValue;
import com.cangwu.frame.orm.ddl.annotation.DDLColumn;
import com.cangwu.frame.orm.ddl.annotation.DDLTable;

import cn.farwalker.ravv.service.merchant.biz.IMerchantBiz;

import com.cangwu.frame.orm.core.IFieldKey;
import java.io.Serializable;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import cn.farwalker.waka.constants.StatusEnum;

/**
 * 供应商
 * 
 * @author generateModel.java
 */
@TableName(MerchantBo.TABLE_NAME)
@DDLTable(name=MerchantBo.TABLE_NAME,comment="供应商")
public class MerchantBo extends Model<MerchantBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        account("account"),
        bindEmail("bind_email"),
        bindMobile("bind_mobile"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        lastLoginIp("last_login_ip"),
        lastLoginTime("last_login_time"),
        loginPassword("login_password"),
        
        memberId("member_id"),
        name("name"),
        remark("remark"),
        status("status"),
        address("address"),
        areaid("areaid"),
        idcard("idcard"),
        license("license"),
        linkman("linkman"),
        logo("logo"),
        
        phone("phone"),
        bankno("bankno"),
        bankname("bankname");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:merchant*/
    public static final String TABLE_NAME = "merchant";
    private static final long serialVersionUID = 1734892629L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="供应商ID")
    private Long id;

    @TableField("account")
    @DDLColumn(name="account",comment="账号",length=255)
    private String account;

    @TableField("bind_email")
    @DDLColumn(name="bind_email",comment="绑定邮箱",length=512)
    private String bindEmail;

    @TableField("bind_mobile")
    @DDLColumn(name="bind_mobile",comment="绑定手机",length=50)
    private String bindMobile;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("last_login_ip")
    @DDLColumn(name="last_login_ip",comment="最近登录IP",length=127)
    private String lastLoginIp;

    @TableField("last_login_time")
    @DDLColumn(name="last_login_time",comment="最近登录时间")
    private Date lastLoginTime;

    @TableField("login_password")
    @DDLColumn(name="login_password",comment="登录密码")
    private String loginPassword;

    @TableField("member_id")
    @DDLColumn(name="member_id",comment="会员ID-作废")
    private Long memberId;

    @TableField("name")
    @DDLColumn(name="name",comment="姓名",length=255)
    private String name;

    @TableField("remark")
    @DDLColumn(name="remark",comment="扩展字段",length=2000)
    private String remark;

    @TableField("status")
    @DDLColumn(name="status",comment="状态",length=63)
    private StatusEnum status;

    @TableField("address")
    @DDLColumn(name="address",comment="详细地址",length=127)
    private String address;

    @TableField("areaid")
    @DDLColumn(name="areaid",comment="地区id")
    private Long areaid;

    @TableField("idcard")
    @DDLColumn(name="idcard",comment="身份证")
    private String idcard;

    @TableField("license")
    @DDLColumn(name="license",comment="营业执照",length=1024)
    private String license;

    @TableField("linkman")
    @DDLColumn(name="linkman",comment="联系人",length=255)
    private String linkman;

    @TableField("logo")
    @DDLColumn(name="logo",comment="logo",length=1024)
    private String logo;

    @TableField("phone")
    @DDLColumn(name="phone",comment="电话",length=50)
    private String phone;

    @TableField("bankno")
    @DDLColumn(name="bankno",comment="银行账号")
    private String bankno;

    @TableField("bankname")
    @DDLColumn(name="bankname",comment="银行名称")
    private String bankname;
    /** 供应商ID*/
    public Long getId(){
        return id;
    }
    /** 供应商ID*/
    public void setId(Long id){
        this.id =id;
    }
    /** 账号*/
    public String getAccount(){
        return account;
    }
    /** 账号*/
    public void setAccount(String account){
        this.account =account;
    }
    /** 绑定邮箱*/
    public String getBindEmail(){
        return bindEmail;
    }
    /** 绑定邮箱*/
    public void setBindEmail(String bindEmail){
        this.bindEmail =bindEmail;
    }
    /** 绑定手机*/
    public String getBindMobile(){
        return bindMobile;
    }
    /** 绑定手机*/
    public void setBindMobile(String bindMobile){
        this.bindMobile =bindMobile;
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
    /** 最近登录IP*/
    public String getLastLoginIp(){
        return lastLoginIp;
    }
    /** 最近登录IP*/
    public void setLastLoginIp(String lastLoginIp){
        this.lastLoginIp =lastLoginIp;
    }
    /** 最近登录时间*/
    public Date getLastLoginTime(){
        return lastLoginTime;
    }
    /** 最近登录时间*/
    public void setLastLoginTime(Date lastLoginTime){
        this.lastLoginTime =lastLoginTime;
    }
    /** 登录密码*/
    public String getLoginPassword(){
        return loginPassword;
    }
    /** 登录密码*/
    public void setLoginPassword(String loginPassword){
        this.loginPassword =loginPassword;
    }
    /** 会员ID-作废*/
    public Long getMemberId(){
        return memberId;
    }
    /** 会员ID-作废*/
    public void setMemberId(Long memberId){
        this.memberId =memberId;
    }
    /** 姓名*/
    public String getName(){
        return name;
    }
    /** 姓名*/
    public void setName(String name){
        this.name =name;
    }
    /** 扩展字段*/
    public String getRemark(){
        return remark;
    }
    /** 扩展字段*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 状态*/
    public StatusEnum getStatus(){
        return status;
    }
    /** 状态*/
    public void setStatus(StatusEnum status){
        this.status =status;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
    /** 详细地址*/
    public String getAddress(){
        return address;
    }
    /** 详细地址*/
    public void setAddress(String address){
        this.address =address;
    }
    /** 地区id*/
    public Long getAreaid(){
        return areaid;
    }
    /** 地区id*/
    public void setAreaid(Long areaid){
        this.areaid =areaid;
    }
    /** 身份证*/
    public String getIdcard(){
        return idcard;
    }
    /** 身份证*/
    public void setIdcard(String idcard){
        this.idcard =idcard;
    }
    /** 营业执照*/
    public String getLicense(){
        return license;
    }
    /** 营业执照*/
    public void setLicense(String license){
        this.license =license;
    }
    /** 联系人*/
    public String getLinkman(){
        return linkman;
    }
    /** 联系人*/
    public void setLinkman(String linkman){
        this.linkman =linkman;
    }
    /** logo*/
    public String getLogo(){
        return logo;
    }
    /** logo*/
    public void setLogo(String logo){
        this.logo =logo;
    }
    /** 电话*/
    public String getPhone(){
        return phone;
    }
    /** 电话*/
    public void setPhone(String phone){
        this.phone =phone;
    }
    /** 银行账号*/
    public String getBankno(){
        return bankno;
    }
    /** 银行账号*/
    public void setBankno(String bankno){
        this.bankno =bankno;
    }
    /** 银行名称*/
    public String getBankname(){
        return bankname;
    }
    /** 银行名称*/
    public void setBankname(String bankname){
        this.bankname =bankname;
    }
}