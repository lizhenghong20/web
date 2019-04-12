package cn.farwalker.ravv.service.merchant.qualification.model;
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
import cn.farwalker.ravv.service.merchant.qualification.biz.IBaseQualificationBiz;

/**
 * 用户资质图信息表
 * 
 * @author generateModel.java
 */
@TableName(BaseQualificationBo.TABLE_NAME)
@DDLTable(name=BaseQualificationBo.TABLE_NAME,comment="用户资质图信息表")
public class BaseQualificationBo extends Model<BaseQualificationBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        bizId("biz_id"),
        bizType("biz_type"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        ossPath("oss_path"),
        picName("pic_name"),
        remark("remark"),
        
        status("status"),
        type("type"),
        merchantId("merchant_id");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:base_qualification*/
    public static final String TABLE_NAME = "base_qualification";
    private static final long serialVersionUID = 1037091691L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="资质ID")
    private Long id;

    @TableField("biz_id")
    @DDLColumn(name="biz_id",comment="业务数据ID")
    private Long bizId;

    @TableField("biz_type")
    @DDLColumn(name="biz_type",comment="业务类别")
    private Integer bizType;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;


    @TableField("oss_path")
    @DDLColumn(name="oss_path",comment="存储路径",length=1024)
    private String ossPath;

    @TableField("pic_name")
    @DDLColumn(name="pic_name",comment="图片名称",length=255)
    private String picName;

    @TableField("remark")
    @DDLColumn(name="remark",comment="扩展字段",length=2000)
    private String remark;

    @TableField("status")
    @DDLColumn(name="status",comment="逻辑删除",length=63)
    private String status;

    @TableField("type")
    @DDLColumn(name="type",comment="资质数据类型")
    private Integer type;

    @TableField("merchant_id")
    @DDLColumn(name="merchant_id",comment="会员ID")
    private Long merchantId;
    /** 资质ID*/
    public Long getId(){
        return id;
    }
    /** 资质ID*/
    public void setId(Long id){
        this.id =id;
    }
    /** 业务数据ID*/
    public Long getBizId(){
        return bizId;
    }
    /** 业务数据ID*/
    public void setBizId(Long bizId){
        this.bizId =bizId;
    }
    /** 业务类别*/
    public Integer getBizType(){
        return bizType;
    }
    /** 业务类别*/
    public void setBizType(Integer bizType){
        this.bizType =bizType;
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
    /** 会员ID * /
    public Long getMemberId(){
        return memberId;
    }
    / ** 会员ID
    public void setMemberId(Long memberId){
        this.memberId =memberId;
    }*/
    /** 存储路径*/
    public String getOssPath(){
        return ossPath;
    }
    /** 存储路径*/
    public void setOssPath(String ossPath){
        this.ossPath =ossPath;
    }
    /** 图片名称*/
    public String getPicName(){
        return picName;
    }
    /** 图片名称*/
    public void setPicName(String picName){
        this.picName =picName;
    }
    /** 扩展字段*/
    public String getRemark(){
        return remark;
    }
    /** 扩展字段*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 逻辑删除*/
    public String getStatus(){
        return status;
    }
    /** 逻辑删除*/
    public void setStatus(String status){
        this.status =status;
    }
    /** 资质数据类型*/
    public Integer getType(){
        return type;
    }
    /** 资质数据类型*/
    public void setType(Integer type){
        this.type =type;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
    /** 会员ID*/
    public Long getMerchantId(){
        return merchantId;
    }
    /** 会员ID*/
    public void setMerchantId(Long merchantId){
        this.merchantId =merchantId;
    }
}