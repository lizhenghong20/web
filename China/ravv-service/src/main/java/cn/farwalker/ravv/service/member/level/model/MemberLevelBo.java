package cn.farwalker.ravv.service.member.level.model;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import com.cangwu.frame.orm.core.BaseBo;
import com.cangwu.frame.orm.core.IFieldKey;
import com.cangwu.frame.orm.ddl.annotation.DDLColumn;
import com.cangwu.frame.orm.ddl.annotation.DDLTable;

/**
 * 会员级别
 * 
 * @author generateModel.java
 */
@TableName(MemberLevelBo.TABLE_NAME)
@DDLTable(name=MemberLevelBo.TABLE_NAME,comment="会员级别")
public class MemberLevelBo extends Model<MemberLevelBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        defaultLevel("default_level"),
        discountRate("discount_rate"),
        enable("enable"),
        freeShipping("free_shipping"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        icon("icon"),
        name("name"),
        needPoint("need_point"),
        remark("remark"),
        level("level");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:member_level*/
    public static final String TABLE_NAME = "member_level";
    private static final long serialVersionUID = -660353321L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键")
    private Long id;

    @TableField("default_level")
    @DDLColumn(name="default_level",comment="默认等级")
    private Boolean defaultLevel;
 

    @TableField("discount_rate")
    @DDLColumn(name="discount_rate",comment="折扣率")
    private BigDecimal discountRate;

    @TableField("enable")
    @DDLColumn(name="enable",comment="是否启用")
    private Boolean enable;

    @TableField("free_shipping")
    @DDLColumn(name="free_shipping",comment="免邮费")
    private Boolean freeShipping;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("icon")
    @DDLColumn(name="icon",comment="图标",length=1024)
    private String icon;


    @TableField("name")
    @DDLColumn(name="name",comment="等级名称",length=255)
    private String name;

    @TableField("need_point")
    @DDLColumn(name="need_point",comment="所需积分")
    private Long needPoint;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("level")
    @DDLColumn(name="level",comment="等级(从小到大升级)")
    private Integer level;
    /** 主键*/
    public Long getId(){
        return id;
    }
    /** 主键*/
    public void setId(Long id){
        this.id =id;
    }
    /** 默认等级*/
    public Boolean getDefaultLevel(){
        return defaultLevel;
    }
    /** 默认等级*/
    public void setDefaultLevel(Boolean defaultLevel){
        this.defaultLevel =defaultLevel;
    }
 
    /** 折扣率*/
    public BigDecimal getDiscountRate(){
        return discountRate;
    }
    /** 折扣率*/
    public void setDiscountRate(BigDecimal discountRate){
        this.discountRate =discountRate;
    }
    /** 是否启用*/
    public Boolean getEnable(){
        return enable;
    }
    /** 是否启用*/
    public void setEnable(Boolean enable){
        this.enable =enable;
    }
    /** 免邮费*/
    public Boolean getFreeShipping(){
        return freeShipping;
    }
    /** 免邮费*/
    public void setFreeShipping(Boolean freeShipping){
        this.freeShipping =freeShipping;
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
    /** 图标*/
    public String getIcon(){
        return icon;
    }
    /** 图标*/
    public void setIcon(String icon){
        this.icon =icon;
    }

    /** 等级名称*/
    public String getName(){
        return name;
    }
    /** 等级名称*/
    public void setName(String name){
        this.name =name;
    }
    /** 所需积分*/
    public Long getNeedPoint(){
        return needPoint;
    }
    /** 所需积分*/
    public void setNeedPoint(Long needPoint){
        this.needPoint =needPoint;
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
    /** 等级(从小到大升级)*/
    public Integer getLevel(){
        return level;
    }
    /** 等级(从小到大升级)*/
    public void setLevel(Integer level){
        this.level =level;
    }
}