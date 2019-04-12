package cn.farwalker.ravv.service.youtube.liveanchor.model;
import java.util.Date;

import com.cangwu.frame.orm.core.BaseBo;
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

/**
 * YouTube 主播表
 * 
 * @author generateModel.java
 */
@TableName(YoutubeLiveAnchorBo.TABLE_NAME)
@DDLTable(name=YoutubeLiveAnchorBo.TABLE_NAME,comment="YouTube 主播表")
public class YoutubeLiveAnchorBo extends Model<YoutubeLiveAnchorBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        anchorMemberId("anchor_member_id"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        youtubeChannelId("youtube_channel_id"),
        frozenStart("frozenStart"),
        frozenEnd("frozenEnd"),
        sequence("sequence"),
        freezeReason("freeze_reason"),
        unfreezeReason("unfreeze_reason");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:youtube_live_anchor*/
    public static final String TABLE_NAME = "youtube_live_anchor";
    private static final long serialVersionUID = -1368128111L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="id")
    private Long id;

    @TableField("anchor_member_id")
    @DDLColumn(name="anchor_member_id",comment="主播ID")
    private Long anchorMemberId;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="更新时间")
    private Date gmtModified;

    @TableField("youtube_channel_id")
    @DDLColumn(name="youtube_channel_id",comment="YouTube 的频道Id",length=127)
    private String youtubeChannelId;

    @TableField("frozen_start")
    @DDLColumn(name="frozen_start",comment="主播冻结开始的时间")
    private Date frozenStart;

    @TableField("frozen_end")
    @DDLColumn(name="frozen_end",comment="主播冻结结束的时间")
    private Date frozenEnd;

    @TableId(value="sequence")
    @DDLColumn(name="sequence",comment="主播排序字段")
    private Long sequence;
    
    @TableId(value="freeze_reason")
    @DDLColumn(name="freeze_reason",comment="冻结原因")
    private String freezeReason;
    
    @TableId(value="unfreeze_reason")
    @DDLColumn(name="unfreeze_reason",comment="解除冻结原因")
    private String unfreezeReason;



    /** id*/
    public Long getId(){
        return id;
    }
    /** id*/
    public void setId(Long id){
        this.id =id;
    }
    /** 主播ID*/
    public Long getAnchorMemberId(){
        return anchorMemberId;
    }
    /** 主播ID*/
    public void setAnchorMemberId(Long anchorMemberId){
        this.anchorMemberId =anchorMemberId;
    }
    /** 创建时间*/
    public Date getGmtCreate(){
        return gmtCreate;
    }
    /** 创建时间*/
    public void setGmtCreate(Date gmtCreate){
        this.gmtCreate =gmtCreate;
    }
    /** 更新时间*/
    public Date getGmtModified(){
        return gmtModified;
    }
    /** 更新时间*/
    public void setGmtModified(Date gmtModified){
        this.gmtModified =gmtModified;
    }
    /** YouTube 的频道Id*/
    public String getYoutubeChannelId(){
        return youtubeChannelId;
    }
    /** YouTube 的频道Id*/
    public void setYoutubeChannelId(String youtubeChannelId){
        this.youtubeChannelId =youtubeChannelId;
    }

    public Date getFrozenEnd() {
        return frozenEnd;
    }

    public void setFrozenEnd(Date frozenEnd) {
        this.frozenEnd = frozenEnd;
    }

    public Date getFrozenStart() {
        return frozenStart;
    }

    public void setFrozenStart(Date frozenStart) {
        this.frozenStart = frozenStart;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }
    
	public String getFreezeReason() {
		return freezeReason;
	}
	
	public void setFreezeReason(String freezeReason) {
		this.freezeReason = freezeReason;
	}
	
	public String getUnfreezeReason() {
		return unfreezeReason;
	}
	
	public void setUnfreezeReason(String unfreezeReason) {
		this.unfreezeReason = unfreezeReason;
	}

    @Override
    protected Serializable pkVal(){
        return id;
    }

}