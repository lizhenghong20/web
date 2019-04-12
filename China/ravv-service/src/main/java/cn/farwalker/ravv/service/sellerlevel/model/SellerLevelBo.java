package cn.farwalker.ravv.service.sellerlevel.model;

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
 * 会员级别
 * @author generateModel.java
 */
@TableName(SellerLevelBo.TABLE_NAME)
@DDLTable(name=SellerLevelBo.TABLE_NAME,comment="会员级别")
public class SellerLevelBo extends Model<SellerLevelBo> implements BaseBo{

    /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        discountRate("discount_rate"),
        enable("enable"),
        freeShipping("free_shipping"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        icon("icon"),
        name("name"),
        needPoint("need_point"),
        
        pid("pid"),
        remark("remark"),
        sequence("sequence");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    private static final long serialVersionUID = -1397105883L;
    /**数据表名*/
    public static final String TABLE_NAME = "seller_level";

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键")
    private  Long id;
    @TableField("discount_rate")
    @DDLColumn(name="discount_rate",comment="折扣率")
    private BigDecimal discountRate;
    @TableField("enable")
    @DDLColumn(name="enable",comment="是否启用")
    private  Integer enable;
    @TableField("free_shipping")
    @DDLColumn(name="free_shipping",comment="免邮费")
    private  Integer freeShipping;
    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private  Date gmtCreate;
    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private  Date gmtModified;
    @TableField("icon")
    @DDLColumn(name="icon",comment="图标",length=1024)
    private  String icon;
    @TableField("name")
    @DDLColumn(name="name",comment="等级名称",length=255)
    private  String name;
    @TableField("need_point")
    @DDLColumn(name="need_point",comment="所需积分")
    private  Long needPoint;
    @TableField("pid")

    @DDLColumn(name="pid",comment="上一级ID")
    private  Long pid;
    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private  String remark;
    @TableField("sequence")
    @DDLColumn(name="sequence",comment="排序")
    private  Integer sequence;

    /** 主键*/
    public Long getId(){
        return this.id;
    }

    /** 主键*/
    public void setId(Long v){
        this.id =v;
    }

    /** 折扣率*/
    public BigDecimal getDiscountRate(){
        return this.discountRate;
    }

    /** 折扣率*/
    public void setDiscountRate(BigDecimal v){
        this.discountRate =v;
    }

    /** 是否启用*/
    public Integer getEnable(){
        return this.enable;
    }

    /** 是否启用*/
    public void setEnable(Integer v){
        this.enable =v;
    }

    /** 免邮费*/
    public Integer getFreeShipping(){
        return this.freeShipping;
    }

    /** 免邮费*/
    public void setFreeShipping(Integer v){
        this.freeShipping =v;
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

    /** 图标*/
    public String getIcon(){
        return this.icon;
    }

    /** 图标*/
    public void setIcon(String v){
        this.icon =v;
    }

    /** 等级名称*/
    public String getName(){
        return this.name;
    }

    /** 等级名称*/
    public void setName(String v){
        this.name =v;
    }

    /** 所需积分*/
    public Long getNeedPoint(){
        return this.needPoint;
    }

    /** 所需积分*/
    public void setNeedPoint(Long v){
        this.needPoint =v;
    }

    /** 上一级ID*/
    public Long getPid(){
        return this.pid;
    }

    /** 上一级ID*/
    public void setPid(Long v){
        this.pid =v;
    }

    /** 备注*/
    public String getRemark(){
        return this.remark;
    }

    /** 备注*/
    public void setRemark(String v){
        this.remark =v;
    }

    /** 排序*/
    public Integer getSequence(){
        return this.sequence;
    }

    /** 排序*/
    public void setSequence(Integer v){
        this.sequence =v;
    }
    @Override
    protected Serializable pkVal(){
        return this.id;
    }
}
