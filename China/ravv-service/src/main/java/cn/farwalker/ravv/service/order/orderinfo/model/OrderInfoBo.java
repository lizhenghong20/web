package cn.farwalker.ravv.service.order.orderinfo.model;
import java.io.Serializable;
import java.util.Date;

import cn.farwalker.ravv.service.order.orderinfo.constants.OrderStatusEnum;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderTypeEnum;

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
 * 订单信息
 * 
 * @author generateModel.java
 */
@TableName(OrderInfoBo.TABLE_NAME)
@DDLTable(name=OrderInfoBo.TABLE_NAME,comment="订单信息")
public class OrderInfoBo extends Model<OrderInfoBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        buyerId("buyer_id"),
        buyerMessage("buyer_message"),
        buyerNick("buyer_nick"),
        closedReason("closed_reason"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        orderCode("order_code"),
        orderFinishedTime("order_finished_time"),
        
        orderStatus("order_status"),
        orderType("order_type"),
        //outTradeNo("out_trade_no"),
        remark("remark"),
        //sellerId("seller_id"),
        sellerMessage("seller_message"),
        //sellerNick("seller_nick"),
        timeoutDate("timeout_date"),
        //userSource("user_source"),
        storehouseId("storehouse_id"),
        
        pid("pid"),
        shipstationOrderId("shipstation_order_id");

        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:order_info*/
    public static final String TABLE_NAME = "order_info";
    private static final long serialVersionUID = -1288707479L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="订单ID")
    private Long id;

    @TableField("buyer_id")
    @DDLColumn(name="buyer_id",comment="买家ID")
    private Long buyerId;

    @TableField("buyer_message")
    @DDLColumn(name="buyer_message",comment="买家留言",length=1023)
    private String buyerMessage;

    @TableField("buyer_nick")
    @DDLColumn(name="buyer_nick",comment="买家名称",length=255)
    private String buyerNick;

    @TableField("closed_reason")
    @DDLColumn(name="closed_reason",comment="关闭原因",length=511)
    private String closedReason;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("order_code")
    @DDLColumn(name="order_code",comment="订单编号")
    private String orderCode;

    @TableField("order_finished_time")
    @DDLColumn(name="order_finished_time",comment="订单完成时间")
    private Date orderFinishedTime;

    @TableField("order_status")
    @DDLColumn(name="order_status",comment="订单状态",length=63)
    private OrderStatusEnum orderStatus;

    @TableField("order_type")
    @DDLColumn(name="order_type",comment="订单类型")
    private OrderTypeEnum orderType;

	/*@TableField("out_trade_no")
    @DDLColumn(name="out_trade_no",comment="֧外部交易编号")
    private String outTradeNo;*/

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    /*
    @TableField("seller_id")
    @DDLColumn(name="seller_id",comment="卖家ID")
    private Long sellerId;*/

    @TableField("seller_message")
    @DDLColumn(name="seller_message",comment="卖家留言",length=1023)
    private String sellerMessage;

    @TableField("timeout_date")
    @DDLColumn(name="timeout_date",comment="过期时间(支付前保留时间)")
    private Date timeoutDate;
    
    /*
    @TableField("seller_nick")
    @DDLColumn(name="seller_nick",comment="卖家昵称",length=255)
    private String sellerNick;


    @TableField("user_source")
    @DDLColumn(name="user_source",comment="用户来源")
    private Integer userSource;
*/
    @TableField("storehouse_id")
    @DDLColumn(name="storehouse_id",comment="仓库id")
    private Long storehouseId;

    @TableField("pid")
    @DDLColumn(name="pid",comment="拆单前订单ID")
    private Long pid;

    @TableField("shipstation_order_id")
    @DDLColumn(name="shipstation_order_id",comment="shipstation订单id")
    private int shipstationOrderId;
    /** 订单ID*/
    public Long getId(){
        return id;
    }
    /** 订单ID*/
    public void setId(Long id){
        this.id =id;
    }
    /** 买家ID*/
    public Long getBuyerId(){
        return buyerId;
    }
    /** 买家ID*/
    public void setBuyerId(Long buyerId){
        this.buyerId =buyerId;
    }
    /** 买家留言*/
    public String getBuyerMessage(){
        return buyerMessage;
    }
    /** 买家留言*/
    public void setBuyerMessage(String buyerMessage){
        this.buyerMessage =buyerMessage;
    }
    /** 买家名称*/
    public String getBuyerNick(){
        return buyerNick;
    }
    /** 买家名称*/
    public void setBuyerNick(String buyerNick){
        this.buyerNick =buyerNick;
    }
    /** 关闭原因*/
    public String getClosedReason(){
        return closedReason;
    }
    /** 关闭原因*/
    public void setClosedReason(String closedReason){
        this.closedReason =closedReason;
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
    /** 订单编号*/
    public String getOrderCode(){
        return orderCode;
    }
    /** 订单编号*/
    public void setOrderCode(String orderCode){
        this.orderCode =orderCode;
    }
    /** 订单完成时间*/
    public Date getOrderFinishedTime(){
        return orderFinishedTime;
    }
    /** 订单完成时间*/
    public void setOrderFinishedTime(Date orderFinishedTime){
        this.orderFinishedTime =orderFinishedTime;
    }
    /** 订单状态*/
    public OrderStatusEnum getOrderStatus(){
        return orderStatus;
    }
    /** 订单状态*/
    public void setOrderStatus(OrderStatusEnum orderStatus){
        this.orderStatus =orderStatus;
    }
    /** 订单类型*/
    public OrderTypeEnum getOrderType(){
        return orderType;
    }
     /** 订单类型*/ 
    public void setOrderType(OrderTypeEnum orderType){
        this.orderType =orderType;
    }
    /** 外部交易编号 
    public String getOutTradeNo(){
        return outTradeNo;
    }
	外部交易编号
    public void setOutTradeNo(String outTradeNo){
        this.outTradeNo =outTradeNo;
    }*/
    
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 卖家ID 
    public Long getSellerId(){
        return sellerId;
    }
    卖家ID
    public void setSellerId(Long sellerId){
        this.sellerId =sellerId;
    }*/
    
    /** 卖家留言*/
    public String getSellerMessage(){
        return sellerMessage;
    }
    /** 卖家留言*/
    public void setSellerMessage(String sellerMessage){
        this.sellerMessage =sellerMessage;
    }
    /** 卖家昵称 
    public String getSellerNick(){
        return sellerNick;
    }
      卖家昵称
    public void setSellerNick(String sellerNick){
        this.sellerNick =sellerNick;
    }*/
    /** 过期时间(支付前保留时间)*/
    public Date getTimeoutDate(){
        return timeoutDate;
    }
    /** 过期时间(支付前保留时间)*/
    public void setTimeoutDate(Date timeoutDate){
        this.timeoutDate =timeoutDate;
    }
    /** 用户来源 
    public Integer getUserSource(){
        return userSource;
    }
      用户来源 
    public void setUserSource(Integer userSource){
        this.userSource =userSource;
    }*/
    @Override
    protected Serializable pkVal(){
        return id;
    }
    /** 仓库id*/
    public Long getStorehouseId(){
        return storehouseId;
    }
    /** 仓库id*/
    public void setStorehouseId(Long storehouseId){
        this.storehouseId =storehouseId;
    }
    
    /** 拆单前订单ID(0表示没有拆单,-n表示有n个子单)*/
    public Long getPid(){
        return pid;
    }
    /** 拆单前订单ID(0表示没有拆单,-n表示有n个子单)*/
    public void setPid(Long pid){
        this.pid =pid;
    }

    public int getShipstationOrderId() {
        return shipstationOrderId;
    }

    public void setShipstationOrderId(int shipstationOrderId) {
        this.shipstationOrderId = shipstationOrderId;
    }
}