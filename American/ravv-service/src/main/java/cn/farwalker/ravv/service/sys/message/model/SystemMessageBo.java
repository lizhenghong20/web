package cn.farwalker.ravv.service.sys.message.model;
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
import cn.farwalker.ravv.service.sys.message.biz.ISystemMessageBiz;

/**
 * 消息推送
 * 
 * @author generateModel.java
 */
@TableName(SystemMessageBo.TABLE_NAME)
@DDLTable(name=SystemMessageBo.TABLE_NAME,comment="消息推送")
public class SystemMessageBo extends Model<SystemMessageBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        content("content"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        icon("icon"),
        haveRead("have_read"),
        previewContent("preview_content"),
        publishMemberId("publish_member_id"),
        publishTime("publish_time"),
        
        recieveMemberId("recieve_member_id"),
        type("type"),
        consultId("consult_id"),
        linkUrl("link_url"),
        qaType("qa_type"),
        logisticsNo("logistics_no"),
        remark("remark"),
        title("title"),
        orderId("order_id");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:system_message*/
    public static final String TABLE_NAME = "system_message";
    private static final long serialVersionUID = 1752141093L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="id")
    private Long id;

    @TableField("content")
    @DDLColumn(name="content",comment="消息内容",length=1023)
    private String content;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("icon")
    @DDLColumn(name="icon",comment="消息的图标",length=1024)
    private String icon;

    @TableField("have_read")
    @DDLColumn(name="have_read",comment="是否已读")
    private Boolean haveRead;

    @TableField("preview_content")
    @DDLColumn(name="preview_content",comment="预览的内容",length=1023)
    private String previewContent;

    @TableField("publish_member_id")
    @DDLColumn(name="publish_member_id",comment="消息发布者的member_id")
    private Long publishMemberId;

    @TableField("publish_time")
    @DDLColumn(name="publish_time",comment="发布时间")
    private Date publishTime;

    @TableField("recieve_member_id")
    @DDLColumn(name="recieve_member_id",comment="消息接受者的member_id")
    private Long recieveMemberId;

    @TableField("type")
    @DDLColumn(name="type",comment="消息类型",length=127)
    private String type;

    @TableField("consult_id")
    @DDLColumn(name="consult_id",comment="问答id")
    private Long consultId;

    @TableField("link_url")
    @DDLColumn(name="link_url",comment="链接的网址",length=255)
    private String linkUrl;

    @TableField("qa_type")
    @DDLColumn(name="qa_type",comment="问答类型（问题、答案）",length=255)
    private String qaType;

    @TableField("logistics_no")
    @DDLColumn(name="logistics_no",comment="消息发布者的logistics_no",length=64)
    private String logisticsNo;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=255)
    private String remark;

    @TableField("title")
    @DDLColumn(name="title",comment="消息的标题",length=255)
    private String title;

    @TableField("order_id")
    @DDLColumn(name="order_id",comment="订单id")
    private Long orderId;

    @TableField("returns_order_id")
    @DDLColumn(name="returns_order_id",comment="退单id")
    private Long returnsOrderId;
    /** id*/
    public Long getId(){
        return id;
    }
    /** id*/
    public void setId(Long id){
        this.id =id;
    }
    /** 消息内容*/
    public String getContent(){
        return content;
    }
    /** 消息内容*/
    public void setContent(String content){
        this.content =content;
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
    /** 消息的图标*/
    public String getIcon(){
        return icon;
    }
    /** 消息的图标*/
    public void setIcon(String icon){
        this.icon =icon;
    }
    /** 是否已读*/
    public Boolean getHaveRead(){
        return haveRead;
    }
    /** 是否已读*/
    public void setHaveRead(Boolean haveRead){
        this.haveRead =haveRead;
    }
    /** 预览的内容*/
    public String getPreviewContent(){
        return previewContent;
    }
    /** 预览的内容*/
    public void setPreviewContent(String previewContent){
        this.previewContent =previewContent;
    }
    /** 消息发布者的member_id*/
    public Long getPublishMemberId(){
        return publishMemberId;
    }
    /** 消息发布者的member_id*/
    public void setPublishMemberId(Long publishMemberId){
        this.publishMemberId =publishMemberId;
    }
    /** 发布时间*/
    public Date getPublishTime(){
        return publishTime;
    }
    /** 发布时间*/
    public void setPublishTime(Date publishTime){
        this.publishTime =publishTime;
    }
    /** 消息接受者的member_id*/
    public Long getRecieveMemberId(){
        return recieveMemberId;
    }
    /** 消息接受者的member_id*/
    public void setRecieveMemberId(Long recieveMemberId){
        this.recieveMemberId =recieveMemberId;
    }
    /** 消息类型*/
    public String getType(){
        return type;
    }
    /** 消息类型*/
    public void setType(String type){
        this.type =type;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
    /** 问答id*/
    public Long getConsultId(){
        return consultId;
    }
    /** 问答id*/
    public void setConsultId(Long consultId){
        this.consultId =consultId;
    }
    /** 链接的网址*/
    public String getLinkUrl(){
        return linkUrl;
    }
    /** 链接的网址*/
    public void setLinkUrl(String linkUrl){
        this.linkUrl =linkUrl;
    }
    /** 问答类型（问题、答案）*/
    public String getLogisticsNo(){
        return logisticsNo;
    }
    /** 问答类型（问题、答案）*/
    public void setLogisticsNo(String logisticsNo){
        this.logisticsNo =logisticsNo;
    }
    /** 问答类型（问题、答案）*/
    public String getQaType(){
        return qaType;
    }
    /** 问答类型（问题、答案）*/
    public void setQaType(String qaType){
        this.qaType =qaType;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 消息的标题*/
    public String getTitle(){
        return title;
    }
    /** 消息的标题*/
    public void setTitle(String title){
        this.title =title;
    }
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getReturnsOrderId() {
        return returnsOrderId;
    }

    public void setReturnsOrderId(Long returnsOrderId) {
        this.returnsOrderId = returnsOrderId;
    }
}