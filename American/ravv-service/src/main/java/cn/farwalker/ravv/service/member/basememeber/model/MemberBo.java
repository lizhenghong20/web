package cn.farwalker.ravv.service.member.basememeber.model;

import cn.farwalker.ravv.service.member.basememeber.constants.ApplyStatusEnum;
import cn.farwalker.waka.constants.SexEnum;

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

import java.math.BigDecimal;
import java.util.Date;
/**import more*/
/**
 * member表
 * @author generateModel.java
 */
@TableName(MemberBo.TABLE_NAME)
@DDLTable(name=MemberBo.TABLE_NAME,comment="member表")
public class MemberBo extends Model<MemberBo> implements BaseBo{

    /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        address("address"),
        advance("advance"),
        advanceFreeze("advance_freeze"),
        area("area"),
        avator("avator"),
        birthday("birthday"),
        buyerLevelId("buyer_level_id"),
        email("email"),
        

        middleName("middle_name"),
        firstname("firstname"),
        lastname("lastname"),

        
        distributionTotal("distribution_total"),
        
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        
        loginCount("login_count"),
        
        mobile("mobile"),
        name("name"),
        nickName("nick_name"),
        orderNum("order_num"),
        
        payPassword("pay_password"),
        point("point"),
        pointFreeze("point_freeze"),
        qrcode("qrcode"),
        referralCode("referral_code"),
        referrerReferalCode("referrer_referal_code"),
        regIp("reg_ip"),
        registerTime("register_time"),
        remark("remark"),
        sellerLevelId("seller_level_id"),
        
        sex("sex"),
        status("status"),
        tel("tel"),
        zip("zip");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    private static final long serialVersionUID = -1993902406L;
    /**数据表名*/
    public static final String TABLE_NAME = "member";

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="会员ID")
    private  Long id;
    @TableField("address")
    @DDLColumn(name="address",comment="地址",length=1023)
    private  String address;
    @TableField("advance")
    @DDLColumn(name="advance",comment="账户余额")
    private BigDecimal advance;
    @TableField("distribution_total")
    @DDLColumn(name="distribution_total",comment="累计分销金额")
    private BigDecimal distributionTotal;
    @TableField("advance_freeze")
    @DDLColumn(name="advance_freeze",comment="冻结金额")
    private  BigDecimal advanceFreeze;
    @TableField("area")
    @DDLColumn(name="area",comment="所属地区")
    private  Long area;
    @TableField("avator")
    @DDLColumn(name="avator",comment="头像",length=1024)
    private  String avator;
    @TableField("birthday")
    @DDLColumn(name="birthday",comment="出生年月")
    private  Date birthday;
    @TableField("buyer_level_id")

    @DDLColumn(name="buyer_level_id",comment="买家等级")
    private  Long buyerLevelId;
    @TableField("email")
    @DDLColumn(name="email",comment="邮箱",length=512)
    private  String email;
    
    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private  Date gmtCreate;
    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private  Date gmtModified;
    
    @TableField("login_count")
    @DDLColumn(name="login_count",comment="登录次数")
    private  Integer loginCount;
    

    @TableField("firstname")
    @DDLColumn(name="firstname",comment="名字",length=255)
    private  String firstname;
    
    @TableField("lastname")
    @DDLColumn(name="lastname",comment="姓氏",length=255)
    private  String lastname;
    
    @TableField("middle_name")
    @DDLColumn(name="middle_name",comment="副名",length=255)
    private  String middleName;

    @TableField("mobile")
    @DDLColumn(name="mobile",comment="手机",length=127)
    private  String mobile;
    @TableField("name")
    @DDLColumn(name="name",comment="名称",length=255)
    private  String name;
    @TableField("nick_name")
    @DDLColumn(name="nick_name",comment="昵称",length=255)
    private  String nickName;
    @TableField("order_num")
    @DDLColumn(name="order_num",comment="订单数")
    private  Integer orderNum;
    @TableField("pay_password")
    @DDLColumn(name="pay_password",comment="支付密码")
    private  String payPassword;
    @TableField("point")
    @DDLColumn(name="point",comment="积分")
    private  Long point;
    @TableField("point_freeze")
    @DDLColumn(name="point_freeze",comment="冻结金额，暂时不用")
    private  Long pointFreeze;
    @TableField("qrcode")
    @DDLColumn(name="qrcode",comment="二维码",length=1024)
    private  String qrcode;
    @TableField("referral_code")
    @DDLColumn(name="referral_code",comment="推荐码",length=1024)
    private  String referralCode;
    @TableField("referrer_referal_code")
    @DDLColumn(name="referrer_referal_code",comment="推荐人的推荐码",length=1024)
    private  String referrerReferalCode;
    @TableField("reg_ip")
    @DDLColumn(name="reg_ip",comment="注册IP",length=255)
    private  String regIp;
    @TableField("register_time")
    @DDLColumn(name="register_time",comment="注册时间")
    private  Date registerTime;
    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private  String remark;
    @TableField("seller_level_id")

    @DDLColumn(name="seller_level_id",comment="等级ID，表示卖家登记")
    private  Long sellerLevelId;
    @TableField("sex")
    @DDLColumn(name="sex",comment="性别",length=63)
    private  SexEnum sex;
    @TableField("status")
    @DDLColumn(name="status",comment="审核状态",length=63)
    private  ApplyStatusEnum status;
    @TableField("tel")
    @DDLColumn(name="tel",comment="固定电话",length=127)
    private  String tel;
    @TableField("zip")
    @DDLColumn(name="zip",comment="邮编")
    private  String zip;

    /** 会员ID*/
    public Long getId(){
        return this.id;
    }

    /** 会员ID*/
    public void setId(Long v){
        this.id =v;
    }

    /** 地址*/
    public String getAddress(){
        return this.address;
    }

    /** 地址*/
    public void setAddress(String v){
        this.address =v;
    }

    /** 账户余额*/
    public BigDecimal getAdvance(){
        return this.advance;
    }

    /** 账户余额*/
    public void setAdvance(BigDecimal v){
        this.advance =v;
    }

    /** 冻结金额*/
    public BigDecimal getAdvanceFreeze(){
        return this.advanceFreeze;
    }

    /** 冻结金额*/
    public void setAdvanceFreeze(BigDecimal v){
        this.advanceFreeze =v;
    }

    /** 所属地区*/
    public Long getArea(){
        return this.area;
    }

    /** 所属地区*/
    public void setArea(Long v){
        this.area =v;
    }

    /** 头像*/
    public String getAvator(){
        return this.avator;
    }

    /** 头像*/
    public void setAvator(String v){
        this.avator =v;
    }

    /** 出生年月*/
    public Date getBirthday(){
        return this.birthday;
    }

    /** 出生年月*/
    public void setBirthday(Date v){
        this.birthday =v;
    }

    /** 买家等级*/
    public Long getBuyerLevelId(){
        return this.buyerLevelId;
    }

    /** 买家等级*/
    public void setBuyerLevelId(Long v){
        this.buyerLevelId =v;
    }

    /** 邮箱*/
    public String getEmail(){
        return this.email;
    }

    /** 邮箱*/
    public void setEmail(String v){
        this.email =v;
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

    /** 姓氏*/
    public String getLastname(){
        return this.lastname;
    }

    /** 姓氏*/
    public void setLastname(String v){
        this.lastname =v;
    }
    /** 名字*/
    public String getFirstname(){
        return this.firstname;
    }

    /** 名字*/
    public void setFirstname(String v){
        this.firstname =v;
    }

    /** 副名*/
    public String getMiddleName(){
        return this.middleName;
    }

    /** 副名*/
    public void setMiddleName(String v){
        this.middleName =v;
    }

    /** 登录次数*/
    public Integer getLoginCount(){
        return this.loginCount;
    }

    /** 登录次数*/
    public void setLoginCount(Integer v){
        this.loginCount =v;
    }
    /** 手机*/
    public String getMobile(){
        return this.mobile;
    }

    /** 手机*/
    public void setMobile(String v){
        this.mobile =v;
    }

    /** 名称*/
    public String getName(){
        return this.name;
    }

    /** 名称*/
    public void setName(String v){
        this.name =v;
    }

    /** 昵称*/
    public String getNickName(){
        return this.nickName;
    }

    /** 昵称*/
    public void setNickName(String v){
        this.nickName =v;
    }

    /** 订单数*/
    public Integer getOrderNum(){
        return this.orderNum;
    }

    /** 订单数*/
    public void setOrderNum(Integer v){
        this.orderNum =v;
    }

    /** 支付密码*/
    public String getPayPassword(){
        return this.payPassword;
    }

    /** 支付密码*/
    public void setPayPassword(String v){
        this.payPassword =v;
    }

    /** 积分*/
    public Long getPoint(){
        return this.point;
    }

    /** 积分*/
    public void setPoint(Long v){
        this.point =v;
    }

    /** 冻结金额，暂时不用*/
    public Long getPointFreeze(){
        return this.pointFreeze;
    }

    /** 冻结金额，暂时不用*/
    public void setPointFreeze(Long v){
        this.pointFreeze =v;
    }

    /** 二维码*/
    public String getQrcode(){
        return this.qrcode;
    }

    /** 二维码*/
    public void setQrcode(String v){
        this.qrcode =v;
    }

    /** 推荐码*/
    public String getReferralCode(){
        return this.referralCode;
    }

    /** 推荐码*/
    public void setReferralCode(String v){
        this.referralCode =v;
    }

    /** 推荐人的推荐码*/
    public String getReferrerReferalCode(){
        return this.referrerReferalCode;
    }

    /** 推荐人的推荐码*/
    public void setReferrerReferalCode(String v){
        this.referrerReferalCode =v;
    }

    /** 注册IP*/
    public String getRegIp(){
        return this.regIp;
    }

    /** 注册IP*/
    public void setRegIp(String v){
        this.regIp =v;
    }

    /** 注册时间*/
    public Date getRegisterTime(){
        return this.registerTime;
    }

    /** 注册时间*/
    public void setRegisterTime(Date v){
        this.registerTime =v;
    }

    /** 备注*/
    public String getRemark(){
        return this.remark;
    }

    /** 备注*/
    public void setRemark(String v){
        this.remark =v;
    }

    /** 等级ID，表示卖家登记*/
    public Long getSellerLevelId(){
        return this.sellerLevelId;
    }

    /** 等级ID，表示卖家登记*/
    public void setSellerLevelId(Long v){
        this.sellerLevelId =v;
    }

    /** 性别*/
    public SexEnum getSex(){
        return this.sex;
    }

    /** 性别*/
    public void setSex(SexEnum v){
        this.sex =v;
    }

    /** 审核状态*/
    public ApplyStatusEnum getStatus(){
        return this.status;
    }

    /** 审核状态*/
    public void setStatus(ApplyStatusEnum v){
        this.status =v;
    }

    /** 固定电话*/
    public String getTel(){
        return this.tel;
    }

    /** 固定电话*/
    public void setTel(String v){
        this.tel =v;
    }

    /** 邮编*/
    public String getZip(){
        return this.zip;
    }

    /** 邮编*/
    public void setZip(String v){
        this.zip =v;
    }
    @Override
    protected Serializable pkVal(){
        return this.id;
    }

	public BigDecimal getDistributionTotal() {
		return distributionTotal;
	}

	public void setDistributionTotal(BigDecimal distributionTotal) {
		this.distributionTotal = distributionTotal;
	}
}
