package cn.farwalker.ravv.service.goodsext.comment.model;
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
 * 商品评论
 * 
 * @author generateModel.java
 */
@TableName(GoodsCommentBo.TABLE_NAME)
@DDLTable(name=GoodsCommentBo.TABLE_NAME,comment="商品评论")
public class GoodsCommentBo extends Model<GoodsCommentBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        author("author"),
        authorId("author_id"),
        comment("comment"),
        commentTime("comment_time"),
        display("display"),
        forCommentId("for_comment_id"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        
        goodsId("goods_id"),
        ip("ip"),
        lastReply("last_reply"),
        orderId("order_id"),
        remark("remark"),
        replyName("reply_name"),
        skuId("sku_id"),
        images("images"),
        appendId("append_id"),
        videos("videos"),
        shopReply("shop_reply") ;
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:goods_comment*/
    public static final String TABLE_NAME = "goods_comment";
    private static final long serialVersionUID = 1778585718L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="评论ID")
    private Long id;

    @TableField("author")
    @DDLColumn(name="author",comment="作者",length=255)
    private String author;

    @TableField("author_id")
    @DDLColumn(name="author_id",comment="作者ID")
    private Long authorId;

    @TableField("comment")
    @DDLColumn(name="comment",comment="内容",length=65535)
    private String comment;

    @TableField("comment_time")
    @DDLColumn(name="comment_time",comment="发布时间")
    private Date commentTime;

    @TableField("display")
    @DDLColumn(name="display",comment="前台是否显示")
    private Integer display;

    @TableField("for_comment_id")
    @DDLColumn(name="for_comment_id",comment="回复的ID")
    private Long forCommentId;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("goods_id")
    @DDLColumn(name="goods_id",comment="商品ID")
    private Long goodsId;

    @TableField("ip")
    @DDLColumn(name="ip",comment="ip地址",length=127)
    private String ip;

    @TableField("last_reply")
    @DDLColumn(name="last_reply",comment="最后回复时间")
    private Date lastReply;

    @TableField("order_id")
    @DDLColumn(name="order_id",comment="订单ID")
    private Long orderId;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("reply_name")
    @DDLColumn(name="reply_name",comment="回复名称",length=255)
    private String replyName;

    @TableField("sku_id")
    @DDLColumn(name="sku_id",comment="SKU_ID")
    private Long skuId;

    @TableField("images")
    @DDLColumn(name="images",comment="图片-多个用逗号分隔",length=1024)
    private String images;

    @TableField("append_id")
    @DDLColumn(name="append_id",comment="追评id")
    private Long appendId;

    @TableField("videos")
    @DDLColumn(name="videos",comment="视频-多个用逗号分隔",length=512)
    private String videos;

    @TableField("shop_reply")
    @DDLColumn(name="shop_reply",comment="是否商家回复")
    private Integer shopReply;
    /** 评论ID*/
    public Long getId(){
        return id;
    }
    /** 评论ID*/
    public void setId(Long id){
        this.id =id;
    }
    /** 作者*/
    public String getAuthor(){
        return author;
    }
    /** 作者*/
    public void setAuthor(String author){
        this.author =author;
    }
    /** 作者ID*/
    public Long getAuthorId(){
        return authorId;
    }
    /** 作者ID*/
    public void setAuthorId(Long authorId){
        this.authorId =authorId;
    }
    /** 内容*/
    public String getComment(){
        return comment;
    }
    /** 内容*/
    public void setComment(String comment){
        this.comment =comment;
    }
    /** 发布时间*/
    public Date getCommentTime(){
        return commentTime;
    }
    /** 发布时间*/
    public void setCommentTime(Date commentTime){
        this.commentTime =commentTime;
    }
    /** 前台是否显示*/
    public Integer getDisplay(){
        return display;
    }
    /** 前台是否显示*/
    public void setDisplay(Integer display){
        this.display =display;
    }
    /** 回复的ID*/
    public Long getForCommentId(){
        return forCommentId;
    }
    /** 回复的ID*/
    public void setForCommentId(Long forCommentId){
        this.forCommentId =forCommentId;
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
    /** 商品ID*/
    public Long getGoodsId(){
        return goodsId;
    }
    /** 商品ID*/
    public void setGoodsId(Long goodsId){
        this.goodsId =goodsId;
    }
    /** ip地址*/
    public String getIp(){
        return ip;
    }
    /** ip地址*/
    public void setIp(String ip){
        this.ip =ip;
    }
    /** 最后回复时间*/
    public Date getLastReply(){
        return lastReply;
    }
    /** 最后回复时间*/
    public void setLastReply(Date lastReply){
        this.lastReply =lastReply;
    }
    /** 订单ID*/
    public Long getOrderId(){
        return orderId;
    }
    /** 订单ID*/
    public void setOrderId(Long orderId){
        this.orderId =orderId;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 回复名称*/
    public String getReplyName(){
        return replyName;
    }
    /** 回复名称*/
    public void setReplyName(String replyName){
        this.replyName =replyName;
    }
    /** SKU_ID*/
    public Long getSkuId(){
        return skuId;
    }
    /** SKU_ID*/
    public void setSkuId(Long skuId){
        this.skuId =skuId;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
    /** 图片-多个用逗号分隔*/
    public String getImages(){
        return images;
    }
    /** 图片-多个用逗号分隔*/
    public void setImages(String images){
        this.images =images;
    }
    /** 追评id*/
    public Long getAppendId(){
        return appendId;
    }
    /** 追评id*/
    public void setAppendId(Long appendId){
        this.appendId =appendId;
    }
    /** 视频-多个用逗号分隔*/
    public String getVideos(){
        return videos;
    }
    /** 视频-多个用逗号分隔*/
    public void setVideos(String videos){
        this.videos =videos;
    }
    public Integer getShopReply() {
        return shopReply;
    }

    public void setShopReply(Integer shopReply) {
        this.shopReply = shopReply;
    }
}