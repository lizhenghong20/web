package cn.farwalker.ravv.service.member.accountflow.model;
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

import cn.farwalker.ravv.service.member.accountflow.constants.ChargeSourceEnum;
import cn.farwalker.ravv.service.member.accountflow.constants.ChargeTypeEnum;

/**
 * 余额流水表
 * 
 * @author generateModel.java
 */
@TableName(MemberAccountFlowBo.TABLE_NAME)
@DDLTable(name=MemberAccountFlowBo.TABLE_NAME,comment="余额流水表")
public class MemberAccountFlowBo extends Model<MemberAccountFlowBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        amt("amt"),
        auditDate("audit_date"),
        auditorId("auditor_id"),
        chargesource("chargesource"),
        chargetype("chargetype"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        memberId("member_id"),
        
        notifykey("notifykey"),
        operator("operator"),
        operatorId("operator_id"),
        remark("remark"),
        sources("sources");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:member_account_flow*/
    public static final String TABLE_NAME = "member_account_flow";
    private static final long serialVersionUID = 159501838L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="id")
    private Long id;

    @TableField("amt")
    @DDLColumn(name="amt",comment="总额")
    private BigDecimal amt;

    @TableField("audit_date")
    @DDLColumn(name="audit_date",comment="审核时间")
    private Date auditDate;

    @TableField("auditor_id")
    @DDLColumn(name="auditor_id",comment="审核人id")
    private Long auditorId;

    @TableField("chargesource")
    @DDLColumn(name="chargesource",comment="充值来源,1手工充值、2注册充值、3订单,4提现,5分销")
    private ChargeSourceEnum chargesource;

    @TableField("chargetype")
    @DDLColumn(name="chargetype",comment="充值类型:1现金(支付宝或银联等)、2现金券(总额)、3返现(总额),4提现,5分销")
    private ChargeTypeEnum chargetype;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="创建时间")
    private Date gmtModified;

    @TableField("member_id")
    @DDLColumn(name="member_id",comment="会员ID")
    private Long memberId;

    @TableField("notifykey")
    @DDLColumn(name="notifykey",comment="回调凭证号",length=50)
    private String notifykey;

    @TableField("operator")
    @DDLColumn(name="operator",comment="操作人名称",length=50)
    private String operator;

    @TableField("operator_id")
    @DDLColumn(name="operator_id",comment="操作人id(后台手工充值)")
    private Long operatorId;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=250)
    private String remark;

    @TableField("sources")
    @DDLColumn(name="sources",comment="来源信息:如果是订单则填写订单号，无来源则留空",length=250)
    private String sources;
    /** id*/
    public Long getId(){
        return id;
    }
    /** id*/
    public void setId(Long id){
        this.id =id;
    }
    /** 总额*/
    public BigDecimal getAmt(){
        return amt;
    }
    /** 总额*/
    public void setAmt(BigDecimal amt){
        this.amt =amt;
    }
    /** 审核时间*/
    public Date getAuditDate(){
        return auditDate;
    }
    /** 审核时间*/
    public void setAuditDate(Date auditDate){
        this.auditDate =auditDate;
    }
    /** 审核人id*/
    public Long getAuditorId(){
        return auditorId;
    }
    /** 审核人id*/
    public void setAuditorId(Long auditorId){
        this.auditorId =auditorId;
    }
    /** 充值来源,1手工充值、2注册充值、3订单,4提现*/
    public ChargeSourceEnum getChargesource(){
        return chargesource;
    }
    /** 充值来源,1手工充值、2注册充值、3订单,4提现*/
    public void setChargesource(ChargeSourceEnum chargesource){
        this.chargesource =chargesource;
    }
    /** 充值类型:1现金(支付宝或银联等)、2现金券(总额)、3反现(总额),4提现*/
    public ChargeTypeEnum getChargetype(){
        return chargetype;
    }
    /** 充值类型:1现金(支付宝或银联等)、2现金券(总额)、3反现(总额),4提现*/
    public void setChargetype(ChargeTypeEnum chargetype){
        this.chargetype =chargetype;
    }
    /** 创建时间*/
    public Date getGmtCreate(){
        return gmtCreate;
    }
    /** 创建时间*/
    public void setGmtCreate(Date gmtCreate){
        this.gmtCreate =gmtCreate;
    }
    /** 创建时间*/
    public Date getGmtModified(){
        return gmtModified;
    }
    /** 创建时间*/
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
    /** 回调凭证号*/
    public String getNotifykey(){
        return notifykey;
    }
    /** 回调凭证号*/
    public void setNotifykey(String notifykey){
        this.notifykey =notifykey;
    }
    /** 操作人名称*/
    public String getOperator(){
        return operator;
    }
    /** 操作人名称*/
    public void setOperator(String operator){
        this.operator =operator;
    }
    /** 操作人id(后台手工充值)*/
    public Long getOperatorId(){
        return operatorId;
    }
    /** 操作人id(后台手工充值)*/
    public void setOperatorId(Long operatorId){
        this.operatorId =operatorId;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 来源信息:如果是订单则填写订单号，无来源则留空*/
    public String getSources(){
        return sources;
    }
    /** 来源信息:如果是订单则填写订单号，无来源则留空*/
    public void setSources(String sources){
        this.sources =sources;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}