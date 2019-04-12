package cn.farwalker.ravv.service.youtube.livegoods.model;
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
 * 直播商品
 * 
 * @author generateModel.java
 */
@TableName(YoutubeLiveGoodsBo.TABLE_NAME)
@DDLTable(name=YoutubeLiveGoodsBo.TABLE_NAME,comment="直播商品")
public class YoutubeLiveGoodsBo extends Model<YoutubeLiveGoodsBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        gmtCreate("gmt_create"),
        goodsId("goods_id"),
        gmtModified("gmt_modified"),
        videoId("video_id");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:youtube_live_goods*/
    public static final String TABLE_NAME = "youtube_live_goods";
    private static final long serialVersionUID = -637051724L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="id")
    private Long id;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField("goods_id")
    @DDLColumn(name="goods_id",comment="预览图默认")
    private Long goodsId;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="更新时间")
    private Date gmtModified;

    @TableField("video_id")
    @DDLColumn(name="video_id",comment="videId")
    private Long videoId;
    /** id*/
    public Long getId(){
        return id;
    }
    /** id*/
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
    /** 预览图默认*/
    public Long getGoodsId(){
        return goodsId;
    }
    /** 预览图默认*/
    public void setGoodsId(Long goodsId){
        this.goodsId =goodsId;
    }
    /** 更新时间*/
    public Date getGmtModified(){
        return gmtModified;
    }
    /** 更新时间*/
    public void setGmtModified(Date gmtModified){
        this.gmtModified =gmtModified;
    }
    /** videId*/
    public Long getVideoId(){
        return videoId;
    }
    /** videId*/
    public void setVideoId(Long videoId){
        this.videoId =videoId;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}