package cn.farwalker.ravv.service.payment.paymentlog.model;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cn.farwalker.ravv.service.order.constants.PayStatusEnum;
import cn.farwalker.ravv.service.order.constants.PaymentPlatformEnum;
import cn.farwalker.ravv.service.payment.paymentlog.constants.PayMethodEnum;

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
 * 会员支付日志
 * 
 * @author generateModel.java
 */
@TableName(MemberPaymentLogBo.TABLE_NAME)
@DDLTable(name=MemberPaymentLogBo.TABLE_NAME,comment="会员支付日志")
public class MemberPaymentLogBo extends Model<MemberPaymentLogBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
         payeeEmail("payee_email"),
         payeeMerchantId("payee_merchant_id"),
        bank("bank"),
        currencyCode("currency_code"),
        disabled("disabled"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        ip("ip"),
        
        memberId("member_id"),
        memo("memo"),
        operatorId("operator_id"),
        orderId("order_id"),
        payAccount("pay_account"),
        payBeginTime("pay_begin_time"),
        payConfirmTime("pay_confirm_time"),
        payMethod("pay_method"),
        payType("pay_type"),
        
        paycost("paycost"),
         refundTime("refund_time"),
        payedTime("payed_time"),
        remark("remark"),
        returnUrl("return_url"),
        status("status"),
        tradeNo("trade_no"),
        payerId("payer_id"),
         payerEmail("payer_email"),
        paymentId("payment_id"),
         refundTotalAmount("refund_total_amount"),
        refundShipping("refund_shipping"),
        refundSubAmount("refund_sub_amount"),
        refundTax("refund_tax"),
        returnOrderId("return_order_id"),
        saleId("sale_id"),
        shipping("shipping"),
        subtotal("subtotal"),
        tax("tax"),
        totalAmount("total_amount"),
        stripePaymentId("stripe_payment_id") ;
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:member_payment_log*/
    public static final String TABLE_NAME = "member_payment_log";
    private static final long serialVersionUID = -1577607131L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="支付单号")
    private Long id;

    @TableField("payee_merchant_id")
    @DDLColumn(name="payee_merchant_id",comment="收款方的账号ID",length=255)
    private String payeeMerchantId;

    @TableField("payee_email")
    @DDLColumn(name="payee_email",comment="收款方的email账号",length=255)
    private String payeeEmail;

    @TableField("bank")
    @DDLColumn(name="bank",comment="银行",length=255)
    private String bank;

    @TableField("currency_code")
    @DDLColumn(name="currency_code",comment="货币编码，目前只用美元 USD")
    private String currencyCode;

    @TableField("disabled")
    @DDLColumn(name="disabled",comment="取消该笔支付")
    private Boolean disabled;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("ip")
    @DDLColumn(name="ip",comment="支付IP",length=127)
    private String ip;

    @TableField("member_id")
    @DDLColumn(name="member_id",comment="会员ID")
    private Long memberId;

    @TableField("memo")
    @DDLColumn(name="memo",comment="支付注释",length=255)
    private String memo;

    @TableField("operator_id")
    @DDLColumn(name="operator_id",comment="操作员")
    private Integer operatorId;

    @TableField("order_id")
    @DDLColumn(name="order_id",comment="订单ID")
    private Long orderId;

    @TableField("pay_account")
    @DDLColumn(name="pay_account",comment="支付账户",length=255)
    private String payAccount;

    @TableField("pay_begin_time")
    @DDLColumn(name="pay_begin_time",comment="支付开始时间")
    private Date payBeginTime;

    @TableField("pay_confirm_time")
    @DDLColumn(name="pay_confirm_time",comment="支付确认时间")
    private Date payConfirmTime;

    @TableField("pay_method")
    @DDLColumn(name="pay_method",comment="付款方式")
    private PayMethodEnum payMethod;

    @TableField("pay_type")
    @DDLColumn(name="pay_type",comment="支付类型")
    private PaymentPlatformEnum payType;

    @TableField("paycost")
    @DDLColumn(name="paycost",comment="手续费")
    private BigDecimal paycost;

    @TableField("payed_time")
    @DDLColumn(name="payed_time",comment="支付完成时间")
    private Date payedTime;

    @TableField("refund_time")
    @DDLColumn(name="refund_time",comment="退款完成的时间")
    private Date refundTime;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("return_url")
    @DDLColumn(name="return_url",comment="返回URL",length=1024)
    private String returnUrl;

    @TableField("status")
    @DDLColumn(name="status",comment="支付状态")
    private PayStatusEnum status;

    @TableField("trade_no")
    @DDLColumn(name="trade_no",comment="交易编号")
    private String tradeNo;

    @TableField("payer_id")
    @DDLColumn(name="payer_id",comment="paypal支付账户的 id")
    private String payerId;

    @TableField("payer_email")
    @DDLColumn(name="payer_email",comment="PayPal支付账户的email")
    private String payerEmail;

    @TableField("payment_id")
    @DDLColumn(name="payment_id",comment="paypal支付订单的id")
    private String paymentId;

    @TableField("refund_total_amount")
    @DDLColumn(name="refund_total_amount",comment="退款总额")
    private BigDecimal refundTotalAmount;

    @TableField("refund_shipping")
    @DDLColumn(name="refund_shipping",comment="退款中要退的运费")
    private BigDecimal refundShipping;

    @TableField("refund_sub_amount")
    @DDLColumn(name="refund_sub_amount",comment="退款要退的商品的金额")
    private BigDecimal refundSubAmount;

    @TableField("refund_tax")
    @DDLColumn(name="refund_tax",comment="退款中要退的税金")
    private BigDecimal refundTax;

    @TableField("return_order_id")
    @DDLColumn(name="return_order_id",comment="退货单的ID")
    private Long returnOrderId;

    @TableField("sale_id")
    @DDLColumn(name="sale_id",comment="交易成功后的交易id")
    private String saleId;

    @TableField("shipping")
    @DDLColumn(name="shipping",comment="运费")
    private BigDecimal shipping;

    @TableField("subtotal")
    @DDLColumn(name="subtotal",comment="订单商品的金额")
    private BigDecimal subtotal;

    @TableField("tax")
    @DDLColumn(name="tax",comment="税金")
    private BigDecimal tax;

    @TableField("total_amount")
    @DDLColumn(name="total_amount",comment="订单总金额")
    private BigDecimal totalAmount;

    @TableField("stripe_payment_id")
    @DDLColumn(name="stripe_payment_id",comment="stripe支付id,退款时使用",length=64)
    private String stripePaymentId;
    /** 支付单号*/
    public Long getId(){
        return id;
    }
    /** 支付单号*/
    public void setId(Long id){
        this.id =id;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }

    public String getPayeeEmail() {
        return payeeEmail;
    }

    public void setPayeeEmail(String payeeEmail) {
        this.payeeEmail = payeeEmail;
    }

    public String getPayeeMerchantId() {
        return payeeMerchantId;
    }

    public void setPayeeMerchantId(String payeeMerchantId) {
        this.payeeMerchantId = payeeMerchantId;
    }

    public BigDecimal getRefundTotalAmount() {return refundTotalAmount;}

    public void setRefundTotalAmount(BigDecimal refundTotalAmount) {this.refundTotalAmount = refundTotalAmount;}

    /** 银行*/
    public String getBank(){
        return bank;
    }
    /** 银行*/
    public void setBank(String bank){
        this.bank =bank;
    }

    /** 货币编码，目前只用美元 USD*/
    public String getCurrencyCode(){
        return currencyCode;
    }
    /** 货币编码，目前只用美元 USD*/
    public void setCurrencyCode(String currencyCode){
        this.currencyCode =currencyCode;
    }
    /** 取消该笔支付*/
    public Boolean getDisabled(){
        return disabled;
    }
    /** 取消该笔支付*/
    public void setDisabled(Boolean disabled){
        this.disabled =disabled;
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
    /** 支付IP*/
    public String getIp(){
        return ip;
    }
    /** 支付IP*/
    public void setIp(String ip){
        this.ip =ip;
    }
    /** 会员ID*/
    public Long getMemberId(){
        return memberId;
    }
    /** 会员ID*/
    public void setMemberId(Long memberId){
        this.memberId =memberId;
    }
    /** 支付注释*/
    public String getMemo(){
        return memo;
    }
    /** 支付注释*/
    public void setMemo(String memo){
        this.memo =memo;
    }
    /** 操作员*/
    public Integer getOperatorId(){
        return operatorId;
    }
    /** 操作员*/
    public void setOperatorId(Integer operatorId){
        this.operatorId =operatorId;
    }
    /** 订单ID*/
    public Long getOrderId(){
        return orderId;
    }
    /** 订单ID*/
    public void setOrderId(Long orderId){
        this.orderId =orderId;
    }
    /** 支付账户*/
    public String getPayAccount(){
        return payAccount;
    }
    /** 支付账户*/
    public void setPayAccount(String payAccount){
        this.payAccount =payAccount;
    }
    /** 支付开始时间*/
    public Date getPayBeginTime(){
        return payBeginTime;
    }
    /** 支付开始时间*/
    public void setPayBeginTime(Date payBeginTime){
        this.payBeginTime =payBeginTime;
    }
    /** 支付确认时间*/
    public Date getPayConfirmTime(){
        return payConfirmTime;
    }
    /** 支付确认时间*/
    public void setPayConfirmTime(Date payConfirmTime){
        this.payConfirmTime =payConfirmTime;
    }
    /** 付款方式*/
    public PayMethodEnum getPayMethod(){
        return payMethod;
    }
    /** 付款方式*/
    public void setPayMethod(PayMethodEnum payMethod){
        this.payMethod =payMethod;
    }
    /** 支付类型*/
    public PaymentPlatformEnum getPayType(){
        return payType;
    }
    /** 支付类型*/
    public void setPayType(PaymentPlatformEnum payType){
        this.payType =payType;
    }
    /** 手续费*/
    public BigDecimal getPaycost(){
        return paycost;
    }
    /** 手续费*/
    public void setPaycost(BigDecimal paycost){
        this.paycost =paycost;
    }
    /** 支付完成时间*/
    public Date getPayedTime(){
        return payedTime;
    }
    /** 支付完成时间*/
    public void setPayedTime(Date payedTime){
        this.payedTime =payedTime;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 返回URL*/
    public String getReturnUrl(){
        return returnUrl;
    }
    /** 返回URL*/
    public void setReturnUrl(String returnUrl){
        this.returnUrl =returnUrl;
    }
    /** 支付状态*/
    public PayStatusEnum getStatus(){
        return status;
    }
    /** 支付状态*/
    public void setStatus(PayStatusEnum status){
        this.status =status;
    }
    /** 交易编号*/
    public String getTradeNo(){
        return tradeNo;
    }
    /** 交易编号*/
    public void setTradeNo(String tradeNo){
        this.tradeNo =tradeNo;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
    /** 退款人的paypal id*/
    public String getPayerId(){
        return payerId;
    }
    /** 退款人的paypal id*/
    public void setPayerId(String payerId){
        this.payerId =payerId;
    }
    /** paypal支付订单的id*/
    public String getPaymentId(){
        return paymentId;
    }
    /** paypal支付订单的id*/
    public void setPaymentId(String paymentId){
        this.paymentId =paymentId;
    }
    /** 退款中要退的运费*/
    public BigDecimal getRefundShipping(){
        return refundShipping;
    }
    /** 退款中要退的运费*/
    public void setRefundShipping(BigDecimal refundShipping){
        this.refundShipping =refundShipping;
    }
    /** 退款要退的商品的金额*/
    public BigDecimal getRefundSubAmount(){
        return refundSubAmount;
    }
    /** 退款要退的商品的金额*/
    public void setRefundSubAmount(BigDecimal refundSubAmount){
        this.refundSubAmount =refundSubAmount;
    }
    /** 退款中要退的税金*/
    public BigDecimal getRefundTax(){
        return refundTax;
    }
    /** 退款中要退的税金*/
    public void setRefundTax(BigDecimal refundTax){
        this.refundTax =refundTax;
    }
    /** 退货单的ID*/
    public Long getReturnOrderId(){
        return returnOrderId;
    }
    /** 退货单的ID*/
    public void setReturnOrderId(Long returnOrderId){
        this.returnOrderId =returnOrderId;
    }
    /** 交易成功后的交易id*/
    public String getSaleId(){
        return saleId;
    }
    /** 交易成功后的交易id*/
    public void setSaleId(String saleId){
        this.saleId =saleId;
    }
    /** 运费*/
    public BigDecimal getShipping(){
        return shipping;
    }
    /** 运费*/
    public void setShipping(BigDecimal shipping){
        this.shipping =shipping;
    }
    /** 订单商品的金额*/
    public BigDecimal getSubtotal(){
        return subtotal;
    }
    /** 订单商品的金额*/
    public void setSubtotal(BigDecimal subtotal){
        this.subtotal =subtotal;
    }
    /** 税金*/
    public BigDecimal getTax(){
        return tax;
    }
    /** 税金*/
    public void setTax(BigDecimal tax){
        this.tax =tax;
    }
    /** 订单总金额*/
    public BigDecimal getTotalAmount(){
        return totalAmount;
    }
    /** 订单总金额*/
    public void setTotalAmount(BigDecimal totalAmount){
        this.totalAmount =totalAmount;
    }

    public Date getRefundTime() {return refundTime;}

    public void setRefundTime(Date refundTime) {this.refundTime = refundTime;}

    public String getStripePaymentId() {
        return stripePaymentId;
    }

    public void setStripePaymentId(String stripePaymentId) {
        this.stripePaymentId = stripePaymentId;
    }

}