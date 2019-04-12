package cn.farwalker.ravv.service.goods.image.model;
import java.io.Serializable;
import java.util.Date;

import cn.farwalker.ravv.common.constants.ImagePositionEnum;

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
 * 商品图片
 * 
 * @author generateModel.java
 */
@TableName(GoodsImageBo.TABLE_NAME)
@DDLTable(name=GoodsImageBo.TABLE_NAME,comment="商品图片")
public class GoodsImageBo extends Model<GoodsImageBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        goodsId("goods_id"),
        imgPosition("img_position"),
        imgUrl("img_url"),
        remark("remark"),
        skuId("sku_id"),
        status("status"),
        
        isVideo("is_video"),
        //major("major"),
        sequence("sequence");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:goods_image*/
    public static final String TABLE_NAME = "goods_image";
    private static final long serialVersionUID = 123162354L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键")
    private Long id;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("goods_id")
    @DDLColumn(name="goods_id",comment="商品ID")
    private Long goodsId;

    @TableField("img_position")
    @DDLColumn(name="img_position",comment="图片位置")
    private ImagePositionEnum imgPosition;

    @TableField("img_url")
    @DDLColumn(name="img_url",comment="图片路径",length=1024)
    private String imgUrl;
/**  是否主图 由类型决定
    @TableField("major")
    @DDLColumn(name="major",comment="是否主图")
    private Boolean major;
*/
    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("sku_id")
    @DDLColumn(name="sku_id",comment="SKUID(作废)")
    private Long skuId;

    @TableField("status")
    @DDLColumn(name="status",comment="图片状态",length=63)
    private Boolean status;

    @TableField("is_video")
    @DDLColumn(name="is_video",comment="是否视频")
    private Boolean isvideo;

    /*
    @TableField("major")
    @DDLColumn(name="major",comment="是否主图")
    private Boolean major;
    */
    @TableField("sequence")
    @DDLColumn(name="sequence",comment="顺序")
    private Integer sequence;
    /** 主键*/
    public Long getId(){
        return id;
    }
    /** 主键*/
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
    /** 商品ID*/
    public Long getGoodsId(){
        return goodsId;
    }
    /** 商品ID*/
    public void setGoodsId(Long goodsId){
        this.goodsId =goodsId;
    }
    /** 图片位置*/
    public ImagePositionEnum getImgPosition(){
        return imgPosition;
    }
    /** 图片位置*/
    public void setImgPosition(ImagePositionEnum imgPosition){
        this.imgPosition =imgPosition;
    }
    /** 图片路径*/
    public String getImgUrl(){
        return imgUrl;
    }
    /** 图片路径*/
    public void setImgUrl(String imgUrl){
        this.imgUrl =imgUrl;
    }
    /** 是否主图
    public Boolean getMajor(){
        return major;
    }
     是否主图
    public void setMajor(Boolean major){
        this.major =major;
    }*/
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** SKUID(作废)*/
    public Long getSkuId(){
        return skuId;
    }
    /** SKUID(作废)*/
    public void setSkuId(Long skuId){
        this.skuId =skuId;
    }
    /** 图片状态*/
    public Boolean getStatus(){
        return status;
    }
    /** 图片状态*/
    public void setStatus(Boolean status){
        this.status =status;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
    /** 是否视频*/
    public Boolean getIsVideo(){
        return isvideo;
    }
    /** 是否视频*/
    public void setIsVideo(Boolean is_video){
        this.isvideo =is_video;
    }
    /** 是否主图 
    public Boolean getMajor(){
        return major;
    }
      是否主图
    public void setMajor(Boolean major){
        this.major =major;
    }*/
 
    /** 顺序*/
    public Integer getSequence(){
        return sequence;
    }
    /** 顺序*/
    public void setSequence(Integer sequence){
        this.sequence =sequence;
    }
}