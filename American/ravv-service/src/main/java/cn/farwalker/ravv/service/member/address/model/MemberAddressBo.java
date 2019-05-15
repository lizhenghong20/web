package cn.farwalker.ravv.service.member.address.model;
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
import cn.farwalker.ravv.service.member.address.biz.IMemberAddressBiz;

/**
 * 会员地址
 * 
 * @author generateModel.java
 */
@TableName(MemberAddressBo.TABLE_NAME)
@DDLTable(name=MemberAddressBo.TABLE_NAME,comment="会员地址")
public class MemberAddressBo extends Model<MemberAddressBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        address("address"),
        areaId("area_id"),
        areaName("area_name"),
        defaultAddr("default_addr"),
        deliveryDay("delivery_day"),
        deliveryTime("delivery_time"),
        /*
        middleName("middle_name"),
        firstname("firstname"),
        lastname("lastname"),
        */
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        memberId("member_id"),
        
        mobile("mobile"),
        name("name"),
        remark("remark"),
        zip("zip"),
        country("country") ;
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:member_address*/
    public static final String TABLE_NAME = "member_address";
    private static final long serialVersionUID = -708046393L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键")
    private Long id;

    @TableField("address")
    @DDLColumn(name="address",comment="详细地址",length=1023)
    private String address;

    @TableField("area_id")
    @DDLColumn(name="area_id",comment="地区ID")
    private Long areaId;

    @TableField("area_name")
    @DDLColumn(name="area_name",comment="地区名",length=255)
    private String areaName;

    @TableField("default_addr")
    @DDLColumn(name="default_addr",comment="是否默认地址")
    private Integer defaultAddr;

    @TableField("delivery_day")
    @DDLColumn(name="delivery_day",comment="送货日期")
    private Integer deliveryDay;

    @TableField("delivery_time")
    @DDLColumn(name="delivery_time",comment="送货时间")
    private Integer deliveryTime;



    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;



    @TableField("member_id")
    @DDLColumn(name="member_id",comment="会员ID")
    private Long memberId;

    /*
    @TableField("middle_name")
    @DDLColumn(name="middle_name",comment="父名",length=255)
    private String middleName;
    
    @TableField("firstname")
    @DDLColumn(name="firstname",comment="名字",length=255)
    private String firstname;
    
    @TableField("lastname")
    @DDLColumn(name="lastname",comment="姓",length=255)
    private String lastname;
*/
    @TableField("mobile")
    @DDLColumn(name="mobile",comment="联系电话",length=50)
    private String mobile;

    @TableField("name")
    @DDLColumn(name="name",comment="名称",length=255)
    private String name;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("zip")
    @DDLColumn(name="zip",comment="邮编")
    private String zip;

    @TableField("country")
    @DDLColumn(name="country",comment="国家",length=255)
    private String country;
    /** 主键*/
    public Long getId(){
        return id;
    }
    /** 主键*/
    public void setId(Long id){
        this.id =id;
    }
    /** 详细地址*/
    public String getAddress(){
        return address;
    }
    /** 详细地址*/
    public void setAddress(String address){
        this.address =address;
    }
    /** 地区ID*/
    public Long getAreaId(){
        return areaId;
    }
    /** 地区ID*/
    public void setAreaId(Long areaId){
        this.areaId =areaId;
    }
    /** 地区名*/
    public String getAreaName(){
        return areaName;
    }
    /** 地区名*/
    public void setAreaName(String areaName){
        this.areaName =areaName;
    }
    /** 是否默认地址*/
    public Integer getDefaultAddr(){
        return defaultAddr;
    }
    /** 是否默认地址*/
    public void setDefaultAddr(Integer defaultAddr){
        this.defaultAddr =defaultAddr;
    }
    /** 送货日期*/
    public Integer getDeliveryDay(){
        return deliveryDay;
    }
    /** 送货日期*/
    public void setDeliveryDay(Integer deliveryDay){
        this.deliveryDay =deliveryDay;
    }
    /** 送货时间*/
    public Integer getDeliveryTime(){
        return deliveryTime;
    }
    /** 送货时间*/
    public void setDeliveryTime(Integer deliveryTime){
        this.deliveryTime =deliveryTime;
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
    /** 姓* /
    public String getLastname(){
        return lastname;
    }
    / ** 姓* /
    public void setLastname(String lastname){
        this.lastname =lastname;
    }
    / ** 名字* /
    public String getFirstname(){
        return firstname;
    }
    / ** 名字* /
    public void setFirstname(String firstname){
        this.firstname =firstname;
    }
    / ** 父名* /
    public String getMiddleName(){
        return middleName;
    }
    / ** 父名* /
    public void setMiddleName(String middleName){
        this.middleName =middleName;
    }
    */
    /** 会员ID*/
    public Long getMemberId(){
        return memberId;
    }
    /** 会员ID*/
    public void setMemberId(Long memberId){
        this.memberId =memberId;
    }

    /** 联系电话*/
    public String getMobile(){
        return mobile;
    }
    /** 联系电话*/
    public void setMobile(String mobile){
        this.mobile =mobile;
    }
    /** 名称*/
    public String getName(){
        return name;
    }
    /** 名称*/
    public void setName(String name){
        this.name =name;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 邮编*/
    public String getZip(){
        return zip;
    }
    /** 邮编*/
    public void setZip(String zip){
        this.zip =zip;
    }
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}