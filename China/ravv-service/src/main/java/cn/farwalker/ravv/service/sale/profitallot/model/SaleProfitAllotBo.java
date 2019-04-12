package cn.farwalker.ravv.service.sale.profitallot.model;
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

import cn.farwalker.ravv.service.sale.profitallot.constants.DistStatusEnum;

/**
 * 订单利润分配
 * 
 * @author generateModel.java
 */
@TableName(SaleProfitAllotBo.TABLE_NAME)
@DDLTable(name=SaleProfitAllotBo.TABLE_NAME,comment="订单利润分配")
public class SaleProfitAllotBo extends Model<SaleProfitAllotBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        allotLevel("allot_level"),
        allotType("allot_type"),
        amt("amt"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        memberId("member_id"),
        orderId("order_id"),
        status("status"),
        subordinateId("subordinate_id"),
        
        proportion("proportion");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:sale_profit_allot*/
    public static final String TABLE_NAME = "sale_profit_allot";
    private static final long serialVersionUID = -1914432984L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="")
    private Long id;

    @TableField("allot_level")
    @DDLColumn(name="allot_level",comment="分配等级",length=63)
    private String allotLevel;

    @TableField("allot_type")
    @DDLColumn(name="allot_type",comment="类型(会员推荐或者直播推荐)",length=63)
    private String allotType;

    @TableField("amt")
    @DDLColumn(name="amt",comment="分配金额")
    private BigDecimal amt;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("member_id")
    @DDLColumn(name="member_id",comment="会员id")
    private Long memberId;

    @TableField("order_id")
    @DDLColumn(name="order_id",comment="订单id")
    private Long orderId;

    @TableField("status")
    @DDLColumn(name="status",comment="待返现、已返现、已取消")
    private DistStatusEnum status;

    @TableField("subordinate_id")
    @DDLColumn(name="subordinate_id",comment="下级会员id")
    private Long subordinateId;

    @TableField("proportion")
    @DDLColumn(name="proportion",comment="分销比例")
    private BigDecimal proportion;
    /** null*/
    public Long getId(){
        return id;
    }
    /** null*/
    public void setId(Long id){
        this.id =id;
    }
    /** 分配等级*/
    public String getAllotLevel(){
        return allotLevel;
    }
    /** 分配等级*/
    public void setAllotLevel(String allotLevel){
        this.allotLevel =allotLevel;
    }
    /** 类型(会员推荐或者直播推荐)*/
    public String getAllotType(){
        return allotType;
    }
    /** 类型(会员推荐或者直播推荐)*/
    public void setAllotType(String allotType){
        this.allotType =allotType;
    }
    /** 分配金额*/
    public BigDecimal getAmt(){
        return amt;
    }
    /** 分配金额*/
    public void setAmt(BigDecimal amt){
        this.amt =amt;
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
    /** 会员id*/
    public Long getMemberId(){
        return memberId;
    }
    /** 会员id*/
    public void setMemberId(Long memberId){
        this.memberId =memberId;
    }
    /** 订单id*/
    public Long getOrderId(){
        return orderId;
    }
    /** 订单id*/
    public void setOrderId(Long orderId){
        this.orderId =orderId;
    }
    /** 已分配、未分配*/
    public DistStatusEnum getStatus(){
        return status;
    }
    /** 已分配、未分配*/
    public void setStatus(DistStatusEnum status){
        this.status =status;
    }
    public Long getSubordinateId() {
        return subordinateId;
    }

    public void setSubordinateId(Long subordinateId) {
        this.subordinateId = subordinateId;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
    /** 分销比例*/
    public BigDecimal getProportion(){
        return proportion;
    }
    /** 分销比例*/
    public void setProportion(BigDecimal proportion){
        this.proportion =proportion;
    }
}