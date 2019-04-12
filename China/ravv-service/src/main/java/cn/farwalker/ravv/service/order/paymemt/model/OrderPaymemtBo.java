package cn.farwalker.ravv.service.order.paymemt.model;
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

import cn.farwalker.ravv.service.order.constants.PayStatusEnum;
import cn.farwalker.ravv.service.order.constants.PaymentModeEnum;
import cn.farwalker.ravv.service.order.constants.PaymentPlatformEnum;
import cn.farwalker.waka.util.Tools;

/**
 * 订单支付信息
 * 应付款总价=商品总价-优惠金额+物流费用+调整费用
 * @author generateModel.java
 */
@TableName(OrderPaymemtBo.TABLE_NAME)
@DDLTable(name=OrderPaymemtBo.TABLE_NAME,comment="订单支付信息")
public class OrderPaymemtBo extends Model<OrderPaymemtBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        adjustFee("adjust_fee"),
        buyerPaymentNo("buyer_payment_no"),
        buyerPaymentType("buyer_payment_type"),
        discountFee("discount_fee"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        goodsTotalFee("goods_total_fee"),
        orderId("order_id"),
        
        payMode("pay_mode"),
        payStatus("pay_status"),
        payTime("pay_time"),
        postFee("post_fee"),
        remark("remark"),
        shouldPayTotalFee("should_pay_total_fee"),
        taxFee("tax_fee");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:order_paymemt*/
    public static final String TABLE_NAME = "order_paymemt";
    private static final long serialVersionUID = -463005434L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="订单支付ID")
    private Long id;

    @TableField("adjust_fee")
    @DDLColumn(name="adjust_fee",comment="调整费用")
    private BigDecimal adjustFee;

    @TableField("buyer_payment_no")
    @DDLColumn(name="buyer_payment_no",comment="买家支付账号",length=255)
    private String buyerPaymentNo;

    @TableField("buyer_payment_type")
    @DDLColumn(name="buyer_payment_type",comment="买家支付账号类型(微信、支付宝)",length=63)
    private PaymentPlatformEnum buyerPaymentType;

    @TableField("discount_fee")
    @DDLColumn(name="discount_fee",comment="优惠金额(正数)")
    private BigDecimal discountFee;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("goods_total_fee")
    @DDLColumn(name="goods_total_fee",comment="商品总价")
    private BigDecimal goodsTotalFee;
    
    @TableField("order_id")
    @DDLColumn(name="order_id",comment="订单ID")
    private Long orderId;

    @TableField("pay_mode")
    @DDLColumn(name="pay_mode",comment="支付模式(在线、到付)",length=63)
    private PaymentModeEnum payMode;

    @TableField("pay_status")
    @DDLColumn(name="pay_status",comment="支付状态",length=63)
    private PayStatusEnum payStatus;

    @TableField("pay_time")
    @DDLColumn(name="pay_time",comment="支付时间")
    private Date payTime;

    @TableField("post_fee")
    @DDLColumn(name="post_fee",comment="物流费用")
    private BigDecimal postFee;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("should_pay_total_fee")
    @DDLColumn(name="should_pay_total_fee",comment="应付款总价")
    private BigDecimal shouldPayTotalFee;

    @TableField("tax_fee")
    @DDLColumn(name="tax_fee",comment="税费")
    private BigDecimal taxFee;
    /** 订单支付ID*/
    public Long getId(){
        return id;
    }
    /** 订单支付ID*/
    public void setId(Long id){
        this.id =id;
    }
    /** 调整费用*/
    public BigDecimal getAdjustFee(){
        return adjustFee;
    }
    /** 调整费用*/
    public void setAdjustFee(BigDecimal adjustFee){
        this.adjustFee =adjustFee;
    }
    /** 买家支付账号*/
    public String getBuyerPaymentNo(){
        return buyerPaymentNo;
    }
    /** 买家支付账号*/
    public void setBuyerPaymentNo(String buyerPaymentNo){
        this.buyerPaymentNo =buyerPaymentNo;
    }
    /** 买家支付账号类型(微信、支付宝)*/
    public PaymentPlatformEnum getBuyerPaymentType(){
        return buyerPaymentType;
    }
    /** 买家支付账号类型(微信、支付宝)*/
    public void setBuyerPaymentType(PaymentPlatformEnum buyerPaymentType){
        this.buyerPaymentType =buyerPaymentType;
    }
    /** 优惠金额*/
    public BigDecimal getDiscountFee(){
        return discountFee;
    }
    /** 优惠金额*/
    public void setDiscountFee(BigDecimal discountFee){
        this.discountFee =discountFee;
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
    /** 商品总价*/
    public BigDecimal getGoodsTotalFee(){
        return goodsTotalFee;
    }
    /** 商品总价*/
    public void setGoodsTotalFee(BigDecimal goodsTotalFee){
        this.goodsTotalFee =goodsTotalFee;
    }
    /** 订单ID*/
    public Long getOrderId(){
        return orderId;
    }
    /** 订单ID*/
    public void setOrderId(Long orderId){
        this.orderId =orderId;
    }
    /** 支付模式(在线、到付)*/
    public PaymentModeEnum getPayMode(){
        return payMode;
    }
    /** 支付模式(在线、到付)*/
    public void setPayMode(PaymentModeEnum payMode){
        this.payMode =payMode;
    }
    /** 支付状态*/
    public PayStatusEnum getPayStatus(){
        return payStatus;
    }
    /** 支付状态*/
    public void setPayStatus(PayStatusEnum payStatus){
        this.payStatus =payStatus;
    }
    /** 支付时间*/
    public Date getPayTime(){
        return payTime;
    }
    /** 支付时间*/
    public void setPayTime(Date payTime){
        this.payTime =payTime;
    }
    /** 物流费用*/
    public BigDecimal getPostFee(){
        return postFee;
    }
    /** 物流费用*/
    public void setPostFee(BigDecimal postFee){
        this.postFee =postFee;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 应付款总价*/
    public BigDecimal getShouldPayTotalFee(){
        return shouldPayTotalFee;
    }
    /**统计应付款总价: pbo.goodsTotalFee + pbo.adjustFee + pbo.postFee + pbo.taxFee - pbo.discountFee ;
     */
    public static BigDecimal getPayTotalFee(OrderPaymemtBo pbo){
    	BigDecimal payfee = Tools.bigDecimal.add(pbo.goodsTotalFee,pbo.adjustFee,pbo.postFee,pbo.taxFee) ;
    	if(pbo.discountFee!=null) {
    		BigDecimal df =pbo.discountFee.abs();
    		payfee = Tools.bigDecimal.sub(payfee,df);
    	} 
    	return payfee;
    }

    /** 应付款总价 {@link #getPayTotalFee(OrderPaymemtBo)}*/
    public void setShouldPayTotalFee(BigDecimal shouldPayTotalFee){
        this.shouldPayTotalFee =shouldPayTotalFee;
    }
    
    @Override
    protected Serializable pkVal(){
        return id;
    }
    /** 税费*/
    public BigDecimal getTaxFee(){
        return taxFee;
    }
    /** 税费*/
    public void setTaxFee(BigDecimal taxFee){
        this.taxFee =taxFee;
    }
}