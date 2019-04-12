package cn.farwalker.ravv.service.goodsext.consult.model;
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
import cn.farwalker.ravv.service.goodsext.consult.biz.IGoodsConsultBiz;

/**
 * 商品咨询
 * 
 * @author generateModel.java
 */
@TableName(GoodsConsultBo.TABLE_NAME)
@DDLTable(name=GoodsConsultBo.TABLE_NAME,comment="商品咨询")
public class GoodsConsultBo extends Model<GoodsConsultBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        consultContent("consult_content"),
        consultTime("consult_time"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        goodsId("goods_id"),
        memberId("member_id"),
        remark("remark"),
        replyContent("reply_content"),
        
        replyId("reply_id"),
        replyTime("reply_time"),
        authorName("author_name");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:goods_consult*/
    public static final String TABLE_NAME = "goods_consult";
    private static final long serialVersionUID = -1442386237L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="商品咨询ID")
    private Long id;

    @TableField("consult_content")
    @DDLColumn(name="consult_content",comment="咨询内容",length=65535)
    private String consultContent;

    @TableField("consult_time")
    @DDLColumn(name="consult_time",comment="咨询时间")
    private Date consultTime;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("goods_id")
    @DDLColumn(name="goods_id",comment="商品ID")
    private Long goodsId;

    @TableField("member_id")
    @DDLColumn(name="member_id",comment="会员ID")
    private Long memberId;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("reply_content")
    @DDLColumn(name="reply_content",comment="回复内容",length=65535)
    private String replyContent;

    @TableField("reply_id")
    @DDLColumn(name="reply_id",comment="回复的咨询ID")
    private Long replyId;

    @TableField("reply_time")
    @DDLColumn(name="reply_time",comment="回复时间")
    private Date replyTime;

    @TableField("author_name")
    @DDLColumn(name="author_name",comment="回复人昵称")
    private String authorName;
    /** 商品咨询ID*/
    public Long getId(){
        return id;
    }
    /** 商品咨询ID*/
    public void setId(Long id){
        this.id =id;
    }
    /** 咨询内容*/
    public String getConsultContent(){
        return consultContent;
    }
    /** 咨询内容*/
    public void setConsultContent(String consultContent){
        this.consultContent =consultContent;
    }
    /** 咨询时间*/
    public Date getConsultTime(){
        return consultTime;
    }
    /** 咨询时间*/
    public void setConsultTime(Date consultTime){
        this.consultTime =consultTime;
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
    /** 回复内容*/
    public String getReplyContent(){
        return replyContent;
    }
    /** 回复内容*/
    public void setReplyContent(String replyContent){
        this.replyContent =replyContent;
    }
    /** 回复的咨询ID*/
    public Long getReplyId(){
        return replyId;
    }
    /** 回复的咨询ID*/
    public void setReplyId(Long replyId){
        this.replyId =replyId;
    }
    /** 回复时间*/
    public Date getReplyTime(){
        return replyTime;
    }
    /** 回复时间*/
    public void setReplyTime(Date replyTime){
        this.replyTime =replyTime;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
    /** 回复人昵称*/
    public String getAuthorName(){
        return authorName;
    }
    /** 回复人昵称*/
    public void setAuthorName(String authorName){
        this.authorName =authorName;
    }
}