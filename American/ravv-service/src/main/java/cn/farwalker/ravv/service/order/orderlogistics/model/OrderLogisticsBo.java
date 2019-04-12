package cn.farwalker.ravv.service.order.orderlogistics.model;
import java.io.Serializable;
import java.util.Date;

import cn.farwalker.ravv.service.base.area.constant.CountryCodeEnum;
import cn.farwalker.ravv.service.order.orderlogistics.contants.LogisticsPaymentEnum;
import cn.farwalker.ravv.service.order.orderlogistics.contants.LogisticsStatusEnum;
import cn.farwalker.ravv.service.order.orderlogistics.contants.LogisticsTypeEnum;

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
 * 订单物流信息
 * 
 * @author generateModel.java
 */
@TableName(OrderLogisticsBo.TABLE_NAME)
@DDLTable(name=OrderLogisticsBo.TABLE_NAME,comment="订单物流信息")
public class OrderLogisticsBo extends Model<OrderLogisticsBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        logisticsCompany("logistics_company"),
        logisticsNo("logistics_no"),
        logisticsPayment("logistics_payment"),
        logisticsStatus("logistics_status"),
        logisticsType("logistics_type"),
        orderId("order_id"),
        
        receiverArea("receiver_area"),
        receiverCity("receiver_city"),
        receiverDetailAddress("receiver_detail_address"),
        receiverFullname("receiver_fullname"),
        receiverMobile("receiver_mobile"),
        receiverPostCode("receiver_post_code"),
        receiverProvince("receiver_province"),
        remark("remark"),
        sendGoodsTime("send_goods_time"),
        returnsId("returns_id"),
        
        receiverAreaId("receiver_area_id"),
        countryCode("country_code"),
        receiverTime("receiver_time"),
        shipmentId("shipment_id");
        
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:order_logistics*/
    public static final String TABLE_NAME = "order_logistics";
    private static final long serialVersionUID = 1394545074L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="物流ID")
    private Long id;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("logistics_company")
    @DDLColumn(name="logistics_company",comment="物流公司",length=255)
    private String logisticsCompany;

    @TableField("logistics_no")
    @DDLColumn(name="logistics_no",comment="物流单号")
    private String logisticsNo;

    @TableField("logistics_payment")
    @DDLColumn(name="logistics_payment",comment="邮费支付方式",length=63)
    private LogisticsPaymentEnum logisticsPayment;

    @TableField("logistics_status")
    @DDLColumn(name="logistics_status",comment="物流状态",length=63)
    private LogisticsStatusEnum logisticsStatus;

    @TableField("logistics_type")
    @DDLColumn(name="logistics_type",comment="物流方式",length=63)
    private LogisticsTypeEnum logisticsType;

    @TableField("order_id")
    @DDLColumn(name="order_id",comment="订单ID")
    private Long orderId;

    @TableField("receiver_area")
    @DDLColumn(name="receiver_area",comment="所在区文字")
    private String receiverArea;

    @TableField("receiver_city")
    @DDLColumn(name="receiver_city",comment="所在市文字")
    private String receiverCity;

    @TableField("receiver_detail_address")
    @DDLColumn(name="receiver_detail_address",comment="收件人详细地址",length=255)
    private String receiverDetailAddress;

    @TableField("receiver_fullname")
    @DDLColumn(name="receiver_fullname",comment="收件人全名",length=255)
    private String receiverFullname;

    @TableField("receiver_mobile")
    @DDLColumn(name="receiver_mobile",comment="收件人电话",length=50)
    private String receiverMobile;

    @TableField("receiver_post_code")
    @DDLColumn(name="receiver_post_code",comment="收件人邮编")
    private String receiverPostCode;

    @TableField("receiver_province")
    @DDLColumn(name="receiver_province",comment="所在省份文字")
    private String receiverProvince;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("send_goods_time")
    @DDLColumn(name="send_goods_time",comment="发货时间")
    private Date sendGoodsTime;

    @TableField("returns_id")
    @DDLColumn(name="returns_id",comment="退货id，默认值为0，如果不是0表示退货")
    private Long returnsId;

    @TableField("receiver_area_id")
    @DDLColumn(name="receiver_area_id",comment="地址id")
    private Long receiverAreaId;

    @TableField("country_code")
    @DDLColumn(name="country_code",comment="国家编码")
    private CountryCodeEnum countryCode;

    @TableField("receiver_time")
    @DDLColumn(name="receiver_time",comment="收货时间")
    private Date receiverTime;
    
    @TableField("shipment_id")
    @DDLColumn(name="shipment_id",comment="物流模板id")
    private Long shipmentId;
    
    /** 物流ID*/
    public Long getId(){
        return id;
    }
    /** 物流ID*/
    public void setId(Long id){
        this.id =id;
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
    /** 物流公司*/
    public String getLogisticsCompany(){
        return logisticsCompany;
    }
    /** 物流公司*/
    public void setLogisticsCompany(String logisticsCompany){
        this.logisticsCompany =logisticsCompany;
    }
    /** 物流单号*/
    public String getLogisticsNo(){
        return logisticsNo;
    }
    /** 物流单号*/
    public void setLogisticsNo(String logisticsNo){
        this.logisticsNo =logisticsNo;
    }
    /** 邮费支付方式*/
    public LogisticsPaymentEnum getLogisticsPayment(){
        return logisticsPayment;
    }
    /** 邮费支付方式*/
    public void setLogisticsPayment(LogisticsPaymentEnum v){
        this.logisticsPayment =v;
    }
    /** 物流状态*/
    public LogisticsStatusEnum getLogisticsStatus(){
        return logisticsStatus;
    }
    /** 物流状态*/
    public void setLogisticsStatus(LogisticsStatusEnum v){
        this.logisticsStatus =v;
    }
    /** 物流方式*/
    public LogisticsTypeEnum getLogisticsType(){
        return logisticsType;
    }
    /** 物流方式*/
    public void setLogisticsType(LogisticsTypeEnum v){
        this.logisticsType =v;
    }
    /** 订单ID*/
    public Long getOrderId(){
        return orderId;
    }
    /** 订单ID*/
    public void setOrderId(Long orderId){
        this.orderId =orderId;
    }
    /** 所在区文字*/
    public String getReceiverArea(){
        return receiverArea;
    }
    /** 所在区文字*/
    public void setReceiverArea(String receiverArea){
        this.receiverArea =receiverArea;
    }
    /** 所在市文字*/
    public String getReceiverCity(){
        return receiverCity;
    }
    /** 所在市文字*/
    public void setReceiverCity(String receiverCity){
        this.receiverCity =receiverCity;
    }
    /** 收件人详细地址*/
    public String getReceiverDetailAddress(){
        return receiverDetailAddress;
    }
    /** 收件人详细地址*/
    public void setReceiverDetailAddress(String receiverDetailAddress){
        this.receiverDetailAddress =receiverDetailAddress;
    }
    /** 收件人全名*/
    public String getReceiverFullname(){
        return receiverFullname;
    }
    /** 收件人全名*/
    public void setReceiverFullname(String receiverFullname){
        this.receiverFullname =receiverFullname;
    }
    /** 收件人电话*/
    public String getReceiverMobile(){
        return receiverMobile;
    }
    /** 收件人电话*/
    public void setReceiverMobile(String receiverMobile){
        this.receiverMobile =receiverMobile;
    }
    /** 收件人邮编*/
    public String getReceiverPostCode(){
        return receiverPostCode;
    }
    /** 收件人邮编*/
    public void setReceiverPostCode(String receiverPostCode){
        this.receiverPostCode =receiverPostCode;
    }
    /** 所在省份文字*/
    public String getReceiverProvince(){
        return receiverProvince;
    }
    /** 所在省份文字*/
    public void setReceiverProvince(String receiverProvince){
        this.receiverProvince =receiverProvince;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 发货时间*/
    public Date getSendGoodsTime(){
        return sendGoodsTime;
    }
    /** 发货时间*/
    public void setSendGoodsTime(Date sendGoodsTime){
        this.sendGoodsTime =sendGoodsTime;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
    /** 退货id，默认值为0，如果不是0表示退货*/
    public Long getReturnsId(){
        return returnsId;
    }
    /** 退货id，默认值为0，如果不是0表示退货*/
    public void setReturnsId(Long returnsId){
        this.returnsId =returnsId;
    }
    /** 地址id*/
    public Long getReceiverAreaId(){
        return receiverAreaId;
    }
    /** 地址id*/
    public void setReceiverAreaId(Long receiverAreaId){
        this.receiverAreaId =receiverAreaId;
    }
    /** 国家编码*/
    public CountryCodeEnum getCountryCode(){
        return countryCode;
    }
    /** 国家编码*/
    public void setCountryCode(CountryCodeEnum countryCode){
        this.countryCode =countryCode;
    }
    /** 收货时间*/
    public Date getReceiverTime(){
        return receiverTime;
    }
    /** 收货时间*/
    public void setReceiverTime(Date receiverTime){
        this.receiverTime =receiverTime;
    }
    /**物流模板id*/
	public Long getShipmentId() {
		return shipmentId;
	}
    /**物流模板id*/
	public void setShipmentId(Long shipmentId) {
		this.shipmentId = shipmentId;
	}
}