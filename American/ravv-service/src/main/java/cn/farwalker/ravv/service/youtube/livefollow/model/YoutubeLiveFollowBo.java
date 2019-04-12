package cn.farwalker.ravv.service.youtube.livefollow.model;
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
 * 直播收藏
 * 
 * @author generateModel.java
 */
@TableName(YoutubeLiveFollowBo.TABLE_NAME)
@DDLTable(name=YoutubeLiveFollowBo.TABLE_NAME,comment="直播收藏")
public class YoutubeLiveFollowBo extends Model<YoutubeLiveFollowBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        anchorMemberId("anchor_member_id"),
        gmtCreate("gmt_create"),
        fansMemberId("fans_member_id"),
        gmtModified("gmt_modified");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:youtube_live_follow*/
    public static final String TABLE_NAME = "youtube_live_follow";
    private static final long serialVersionUID = -95146099L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="id")
    private Long id;

    @TableField("anchor_member_id")
    @DDLColumn(name="anchor_member_id",comment="主播ID")
    private Long anchorMemberId;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField("fans_member_id")
    @DDLColumn(name="fans_member_id",comment="粉丝ID")
    private Long fansMemberId;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="更新时间")
    private Date gmtModified;
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
    /** 粉丝ID*/
    public Long getFansMemberId(){
        return fansMemberId;
    }
    /** 粉丝ID*/
    public void setFansMemberId(Long fansMemberId){
        this.fansMemberId =fansMemberId;
    }
    /** 更新时间*/
    public Date getGmtModified(){
        return gmtModified;
    }
    /** 更新时间*/
    public void setGmtModified(Date gmtModified){
        this.gmtModified =gmtModified;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}