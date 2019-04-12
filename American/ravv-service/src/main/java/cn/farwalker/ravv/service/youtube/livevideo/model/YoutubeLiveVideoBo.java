package cn.farwalker.ravv.service.youtube.livevideo.model;
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
 * 直播视频
 * 
 * @author generateModel.java
 */
@TableName(YoutubeLiveVideoBo.TABLE_NAME)
@DDLTable(name=YoutubeLiveVideoBo.TABLE_NAME,comment="直播视频")
public class YoutubeLiveVideoBo extends Model<YoutubeLiveVideoBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        anchorMemberId("anchor_member_id"),
        gmtCreate("gmt_create"),
        description("description"),
        previewDefaultUrl("preview_default_url"),
        previewHighUrl("preview_high_url"),
        previewMediumUrl("preview_medium_url"),
        publishTime("publish_time"),
        title("title"),
        startTime("start_time"),
         endTime("end_time"),
        gmtModified("gmt_modified"),
        videoUrl("video_url"),
        youtubeVideoId("youtube_video_id");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:youtube_live_video*/
    public static final String TABLE_NAME = "youtube_live_video";
    private static final long serialVersionUID = -391306087L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="")
    private Long id;

    @TableField("anchor_member_id")
    @DDLColumn(name="anchor_member_id",comment="主播ID")
    private Long anchorMemberId;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField("description")
    @DDLColumn(name="description",comment="描述",length=1023)
    private String description;

    @TableField("preview_default_url")
    @DDLColumn(name="preview_default_url",comment="预览图默认",length=1024)
    private String previewDefaultUrl;

    @TableField("preview_high_url")
    @DDLColumn(name="preview_high_url",comment="预览图 高清",length=1024)
    private String previewHighUrl;

    @TableField("preview_medium_url")
    @DDLColumn(name="preview_medium_url",comment="预览图 中等",length=1024)
    private String previewMediumUrl;

    @TableField("publish_time")
    @DDLColumn(name="publish_time",comment="发布时间")
    private Date publishTime;

    @TableField("start_time")
    @DDLColumn(name="start_time",comment="直播开始时间")
    private Date startTime;

    @TableField("end_time")
    @DDLColumn(name="end_time",comment="直播结束时间")
    private Date endTime;


    @TableField("title")
    @DDLColumn(name="title",comment="标题",length=127)
    private String title;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="更新时间")
    private Date gmtModified;

    @TableField("video_url")
    @DDLColumn(name="video_url",comment="视频地址",length=1024)
    private String videoUrl;

    @TableField("youtube_video_id")
    @DDLColumn(name="youtube_video_id",comment="video_url 中包含的ID",length=127)
    private String youtubeVideoId;
    /** */
    public Long getId(){
        return id;
    }
    /** */
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
    /** 描述*/
    public String getDescription(){
        return description;
    }
    /** 描述*/
    public void setDescription(String description){
        this.description =description;
    }
    /** 预览图默认*/
    public String getPreviewDefaultUrl(){
        return previewDefaultUrl;
    }
    /** 预览图默认*/
    public void setPreviewDefaultUrl(String previewDefaultUrl){
        this.previewDefaultUrl =previewDefaultUrl;
    }
    /** 预览图 高清*/
    public String getPreviewHighUrl(){
        return previewHighUrl;
    }
    /** 预览图 高清*/
    public void setPreviewHighUrl(String previewHighUrl){
        this.previewHighUrl =previewHighUrl;
    }
    /** 预览图 中等*/
    public String getPreviewMediumUrl(){
        return previewMediumUrl;
    }
    /** 预览图 中等*/
    public void setPreviewMediumUrl(String previewMediumUrl){
        this.previewMediumUrl =previewMediumUrl;
    }
    /** 发布时间*/
    public Date getPublishTime(){
        return publishTime;
    }
    /** 发布时间*/
    public void setPublishTime(Date publishTime){
        this.publishTime =publishTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /** 标题*/
    public String getTitle(){
        return title;
    }
    /** 标题*/
    public void setTitle(String title){
        this.title =title;
    }
    /** 更新时间*/
    public Date getGmtModified(){
        return gmtModified;
    }
    /** 更新时间*/
    public void setGmtModified(Date gmtModified){
        this.gmtModified =gmtModified;
    }
    /** 视频地址*/
    public String getVideoUrl(){
        return videoUrl;
    }
    /** 视频地址*/
    public void setVideoUrl(String videoUrl){
        this.videoUrl =videoUrl;
    }
    /** video_url 中包含的ID*/
    public String getYoutubeVideoId(){
        return youtubeVideoId;
    }
    /** video_url 中包含的ID*/
    public void setYoutubeVideoId(String youtubeVideoId){
        this.youtubeVideoId =youtubeVideoId;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}