package cn.farwalker.ravv.service.payment.paymenterror.model;
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
import cn.farwalker.ravv.service.payment.paymenterror.biz.IMemberPaymentErrorBiz;

/**
 * 会员支付错误记录
 * 
 * @author generateModel.java
 */
@TableName(MemberPaymentErrorBo.TABLE_NAME)
@DDLTable(name=MemberPaymentErrorBo.TABLE_NAME,comment="会员支付错误记录")
public class MemberPaymentErrorBo extends Model<MemberPaymentErrorBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        errorNum("error_num"),
        errorTime("error_time"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        memberId("member_id"),
        paymentId("payment_id"),
        remark("remark"),
        type("type");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:member_payment_error*/
    public static final String TABLE_NAME = "member_payment_error";
    private static final long serialVersionUID = -1777164055L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键")
    private Long id;

    @TableField("error_num")
    @DDLColumn(name="error_num",comment="错误计数")
    private Integer errorNum;

    @TableField("error_time")
    @DDLColumn(name="error_time",comment="错误时间")
    private Date errorTime;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("member_id")
    @DDLColumn(name="member_id",comment="会员ID")
    private Long memberId;

    @TableField("payment_id")
    @DDLColumn(name="payment_id",comment="会员支付ID")
    private Long paymentId;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("type")
    @DDLColumn(name="type",comment="错误类型")
    private Integer type;
    /** 主键*/
    public Long getId(){
        return id;
    }
    /** 主键*/
    public void setId(Long id){
        this.id =id;
    }
    /** 错误计数*/
    public Integer getErrorNum(){
        return errorNum;
    }
    /** 错误计数*/
    public void setErrorNum(Integer errorNum){
        this.errorNum =errorNum;
    }
    /** 错误时间*/
    public Date getErrorTime(){
        return errorTime;
    }
    /** 错误时间*/
    public void setErrorTime(Date errorTime){
        this.errorTime =errorTime;
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
    /** 会员支付ID*/
    public Long getPaymentId(){
        return paymentId;
    }
    /** 会员支付ID*/
    public void setPaymentId(Long paymentId){
        this.paymentId =paymentId;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 错误类型*/
    public Integer getType(){
        return type;
    }
    /** 错误类型*/
    public void setType(Integer type){
        this.type =type;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}