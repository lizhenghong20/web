package cn.farwalker.ravv.service.payment.withdrawapply.model;
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

import cn.farwalker.ravv.service.payment.withdrawapply.constants.WithdrawStatusEnum;

/**
 * 提现申请
 * 
 * @author generateModel.java
 */
@TableName(MemberWithdrawApplyBo.TABLE_NAME)
@DDLTable(name=MemberWithdrawApplyBo.TABLE_NAME,comment="提现申请")
public class MemberWithdrawApplyBo extends Model<MemberWithdrawApplyBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        applyFee("apply_fee"),
        applyNo("apply_no"),
        applyTime("apply_time"),
        bankId("bank_id"),
        bankName("bank_name"),
        bankType("bank_type"),
        cardNumber("card_number"),
        checkTime("check_time"),
        
        checkerId("checker_id"),
        checkerName("checker_name"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        memberId("member_id"),
        paycost("paycost"),
        percentage("percentage"),
        remark("remark"),
        withdrawTime("withdraw_time"),
        withdrawStatus("withdraw_status");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:member_withdraw_apply*/
    public static final String TABLE_NAME = "member_withdraw_apply";
    private static final long serialVersionUID = 1122044887L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="提现申请ID")
    private Long id;

    @TableField("apply_fee")
    @DDLColumn(name="apply_fee",comment="申请提现金额")
    private BigDecimal applyFee;

    @TableField("apply_no")
    @DDLColumn(name="apply_no",comment="申请编号")
    private String applyNo;

    @TableField("apply_time")
    @DDLColumn(name="apply_time",comment="申请时间")
    private Date applyTime;

    @TableField("bank_id")
    @DDLColumn(name="bank_id",comment="银行卡ID")
    private Long bankId;

    @TableField("bank_name")
    @DDLColumn(name="bank_name",comment="银行名称",length=255)
    private String bankName;

    @TableField("bank_type")
    @DDLColumn(name="bank_type",comment="银行卡类型")
    private Integer bankType;

    @TableField("card_number")
    @DDLColumn(name="card_number",comment="银行卡号")
    private String cardNumber;

    @TableField("check_time")
    @DDLColumn(name="check_time",comment="审核时间")
    private Date checkTime;

    @TableField("checker_id")
    @DDLColumn(name="checker_id",comment="审核人ID")
    private Long checkerId;

    @TableField("checker_name")
    @DDLColumn(name="checker_name",comment="审核人姓名",length=255)
    private String checkerName;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("member_id")
    @DDLColumn(name="member_id",comment="会员ID")
    private Long memberId;

    @TableField("paycost")
    @DDLColumn(name="paycost",comment="手续费")
    private BigDecimal paycost;

    @TableField("percentage")
    @DDLColumn(name="percentage",comment="手续费费率")
    private BigDecimal percentage;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("withdraw_time")
    @DDLColumn(name="withdraw_time",comment="完成提现时间")
    private Date withdrawTime;
    
    @TableField("withdraw_status")
    @DDLColumn(name="withdraw_status",comment="提现状态")
    private WithdrawStatusEnum withdrawStatus;
    
    /** 提现申请ID*/
    public Long getId(){
        return id;
    }
    /** 提现申请ID*/
    public void setId(Long id){
        this.id =id;
    }
    /** 申请提现金额*/
    public BigDecimal getApplyFee(){
        return applyFee;
    }
    /** 申请提现金额*/
    public void setApplyFee(BigDecimal applyFee){
        this.applyFee =applyFee;
    }
    /** 申请编号*/
    public String getApplyNo(){
        return applyNo;
    }
    /** 申请编号*/
    public void setApplyNo(String applyNo){
        this.applyNo =applyNo;
    }
    /** 申请时间*/
    public Date getApplyTime(){
        return applyTime;
    }
    /** 申请时间*/
    public void setApplyTime(Date applyTime){
        this.applyTime =applyTime;
    }
    /** 银行卡ID*/
    public Long getBankId(){
        return bankId;
    }
    /** 银行卡ID*/
    public void setBankId(Long bankId){
        this.bankId =bankId;
    }
    /** 银行名称*/
    public String getBankName(){
        return bankName;
    }
    /** 银行名称*/
    public void setBankName(String bankName){
        this.bankName =bankName;
    }
    /** 银行卡类型*/
    public Integer getBankType(){
        return bankType;
    }
    /** 银行卡类型*/
    public void setBankType(Integer bankType){
        this.bankType =bankType;
    }
    /** 银行卡号*/
    public String getCardNumber(){
        return cardNumber;
    }
    /** 银行卡号*/
    public void setCardNumber(String cardNumber){
        this.cardNumber =cardNumber;
    }
    /** 审核时间*/
    public Date getCheckTime(){
        return checkTime;
    }
    /** 审核时间*/
    public void setCheckTime(Date checkTime){
        this.checkTime =checkTime;
    }
    /** 审核人ID*/
    public Long getCheckerId(){
        return checkerId;
    }
    /** 审核人ID*/
    public void setCheckerId(Long checkerId){
        this.checkerId =checkerId;
    }
    /** 审核人姓名*/
    public String getCheckerName(){
        return checkerName;
    }
    /** 审核人姓名*/
    public void setCheckerName(String checkerName){
        this.checkerName =checkerName;
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
    /** 手续费*/
    public BigDecimal getPaycost(){
        return paycost;
    }
    /** 手续费*/
    public void setPaycost(BigDecimal paycost){
        this.paycost =paycost;
    }
    /** 手续费百分比*/
    public BigDecimal getPercentage(){
        return percentage;
    }
    /** 手续费百分比*/
    public void setPercentage(BigDecimal percentage){
        this.percentage =percentage;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 完成提现时间*/
    public Date getWithdrawTime(){
        return withdrawTime;
    }
    /** 完成提现时间*/
    public void setWithdrawTime(Date withdrawTime){
        this.withdrawTime =withdrawTime;
    }
    /** 提现状态 */
	public WithdrawStatusEnum getWithdrawStatus() {
		return withdrawStatus;
	}
	/** 提现状态 */
	public void setWithdrawStatus(WithdrawStatusEnum withdrawStatus) {
		this.withdrawStatus = withdrawStatus;
	}
    @Override
    protected Serializable pkVal(){
        return id;
    }
}