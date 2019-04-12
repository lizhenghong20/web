package cn.farwalker.ravv.service.base.brand.model;
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
import cn.farwalker.ravv.service.base.brand.biz.IBaseBrandBiz;
import cn.farwalker.waka.constants.StatusEnum;

/**
 * 品牌基础信息
 * 
 * @author generateModel.java
 */
@TableName(BaseBrandBo.TABLE_NAME)
@DDLTable(name=BaseBrandBo.TABLE_NAME,comment="品牌基础信息")
public class BaseBrandBo extends Model<BaseBrandBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        brandDescri("brand_descri"),
        brandEnName("brand_en_name"),
        brandName("brand_name"),
        brandRegisterNum("brand_register_num"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        logoUrl("logo_url"),
        remark("remark"),
        
        status("status");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:base_brand*/
    public static final String TABLE_NAME = "base_brand";
    private static final long serialVersionUID = -187913725L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="品牌ID")
    private Long id;

    @TableField("brand_descri")
    @DDLColumn(name="brand_descri",comment="品牌描述",length=1023)
    private String brandDescri;

    @TableField("brand_en_name")
    @DDLColumn(name="brand_en_name",comment="英文品牌名",length=255)
    private String brandEnName;

    @TableField("brand_name")
    @DDLColumn(name="brand_name",comment="品牌名",length=255)
    private String brandName;

    @TableField("brand_register_num")
    @DDLColumn(name="brand_register_num",comment="商标注册号")
    private String brandRegisterNum;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("logo_url")
    @DDLColumn(name="logo_url",comment="品牌logo url",length=1024)
    private String logoUrl;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("status")
    @DDLColumn(name="status",comment="状态",length=63)
    private StatusEnum status;
    /** 品牌ID*/
    public Long getId(){
        return id;
    }
    /** 品牌ID*/
    public void setId(Long id){
        this.id =id;
    }
    /** 品牌描述*/
    public String getBrandDescri(){
        return brandDescri;
    }
    /** 品牌描述*/
    public void setBrandDescri(String brandDescri){
        this.brandDescri =brandDescri;
    }
    /** 英文品牌名*/
    public String getBrandEnName(){
        return brandEnName;
    }
    /** 英文品牌名*/
    public void setBrandEnName(String brandEnName){
        this.brandEnName =brandEnName;
    }
    /** 品牌名*/
    public String getBrandName(){
        return brandName;
    }
    /** 品牌名*/
    public void setBrandName(String brandName){
        this.brandName =brandName;
    }
    /** 商标注册号*/
    public String getBrandRegisterNum(){
        return brandRegisterNum;
    }
    /** 商标注册号*/
    public void setBrandRegisterNum(String brandRegisterNum){
        this.brandRegisterNum =brandRegisterNum;
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
    /** 品牌logo url*/
    public String getLogoUrl(){
        return logoUrl;
    }
    /** 品牌logo url*/
    public void setLogoUrl(String logoUrl){
        this.logoUrl =logoUrl;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 状态*/
    public StatusEnum getStatus(){
        return status;
    }
    /** 状态*/
    public void setStatus(StatusEnum status){
        this.status =status;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}