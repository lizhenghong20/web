package cn.farwalker.ravv.service.payment.bank.model;
import java.io.Serializable;
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
 * 会员银行卡
 * 
 * @author generateModel.java
 */
@TableName(MemberBankBo.TABLE_NAME)
@DDLTable(name=MemberBankBo.TABLE_NAME,comment="会员银行卡")
public class MemberBankBo extends Model<MemberBankBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        bankName("bank_name"),
        bankType("bank_type"),
        bindTime("bind_time"),
        cardNumber("card_number"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        memberId("member_id"),
        remark("remark"),
        cardHolder("card_holder"),
        phone("phone"),
        region("region"),
        
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
    /** 数据表名:member_bank*/
    public static final String TABLE_NAME = "member_bank";
    private static final long serialVersionUID = -727163133L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="银行卡ID")
    private Long id;

    @TableField("bank_name")
    @DDLColumn(name="bank_name",comment="银行名称",length=255)
    private String bankName;

    @TableField("bank_type")
    @DDLColumn(name="bank_type",comment="银行卡类型")
    private Integer bankType;

    @TableField("bind_time")
    @DDLColumn(name="bind_time",comment="绑定时间")
    private Date bindTime;

    @TableField("card_number")
    @DDLColumn(name="card_number",comment="银行卡号")
    private String cardNumber;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("member_id")
    @DDLColumn(name="member_id",comment="会员ID")
    private Long memberId;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("sequence")
    @DDLColumn(name="sequence",comment="排序")
    private Integer sequence;
    
    @TableField("card_holder")
    @DDLColumn(name="card_holder",comment="持卡人姓名",length=255)
    private String cardHolder;
    
    @TableField("phone")
    @DDLColumn(name="phone",comment="持卡人手机号",length=64)
    private String phone;
    
    @TableField("region")
    @DDLColumn(name="region",comment="银行卡所属区域",length=255)
    private String region;
    
    /** 银行卡ID*/
    public Long getId(){
        return id;
    }
    /** 银行卡ID*/
    public void setId(Long id){
        this.id =id;
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
    /** 绑定时间*/
    public Date getBindTime(){
        return bindTime;
    }
    /** 绑定时间*/
    public void setBindTime(Date bindTime){
        this.bindTime =bindTime;
    }
    /** 银行卡号*/
    public String getCardNumber(){
        return cardNumber;
    }
    /** 银行卡号*/
    public void setCardNumber(String cardNumber){
        this.cardNumber =cardNumber;
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
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 排序*/
    public Integer getSequence(){
        return sequence;
    }
    /** 排序*/
    public void setSequence(Integer sequence){
        this.sequence =sequence;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
	public String getCardHolder() {
		return cardHolder;
	}
	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
}