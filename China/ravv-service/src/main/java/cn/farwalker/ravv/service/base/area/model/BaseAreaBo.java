package cn.farwalker.ravv.service.base.area.model;
import java.io.Serializable;
import java.util.Date;

import cn.farwalker.ravv.service.base.area.constant.CountryCodeEnum;

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
 * 地区
 * 
 * @author generateModel.java
 */
@TableName(BaseAreaBo.TABLE_NAME)
@DDLTable(name=BaseAreaBo.TABLE_NAME,comment="地区")
public class BaseAreaBo extends Model<BaseAreaBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        code("code"),
        countryCode("country_code"),
        fullPath("full_path"),
        fullName("full_name"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        leaf("leaf"),
        level("level"),
        
        name("name"),
        nameCode("name_code"),
        pid("pid"),
        remark("remark"),
        shortFullName("short_full_name"),
        shortName("short_name"),
        unit("unit"),
        zip("zip");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:base_area*/
    public static final String TABLE_NAME = "base_area";
    private static final long serialVersionUID = 658151435L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="区域ID")
    private Long id;

    @TableField("code")
    @DDLColumn(name="code",comment="区域编码")
    private String code;

    @TableField("country_code")
    @DDLColumn(name="country_code",comment="国家编码")
    private CountryCodeEnum countryCode;
    
    @TableField("full_path")
    @DDLColumn(name="full_path",comment="地区全路径（区域上下级id拼接）",length=255)
    private String fullPath;

    @TableField("full_name")
    @DDLColumn(name="full_name",comment="地区全名",length=1023)
    private String fullName;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("leaf")
    @DDLColumn(name="leaf",comment="是否末级区域")
    private Boolean leaf;

    @TableField("level")
    @DDLColumn(name="level",comment="区域层级")
    private Integer level;

    @TableField("name")
    @DDLColumn(name="name",comment="区域名称",length=255)
    private String name;

    @TableField("name_code")
    @DDLColumn(name="name_code",comment="名称代码")
    private String nameCode;

    @TableField("pid")
    @DDLColumn(name="pid",comment="上级区域ID")
    private Long pid;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("short_full_name")
    @DDLColumn(name="short_full_name",comment="短名全称",length=1023)
    private String shortFullName;

    @TableField("short_name")
    @DDLColumn(name="short_name",comment="短名",length=255)
    private String shortName;

    @TableField("unit")
    @DDLColumn(name="unit",comment="区域单位")
    private String unit;

    @TableField("zip")
    @DDLColumn(name="zip",comment="邮政编码(美国计算税费)",length=255)
    private String zip;
    /** 区域ID*/
    public Long getId(){
        return id;
    }
    /** 区域ID*/
    public void setId(Long id){
        this.id =id;
    }
    /** 区域编码*/
    public String getCode(){
        return code;
    }
    /** 区域编码*/
    public void setCode(String code){
        this.code =code;
    }
    /** 国家编码*/
    public CountryCodeEnum getCountryCode(){
        return countryCode;
    }
    /** 国家编码*/
    public void setCountryCode(CountryCodeEnum countryCode){
        this.countryCode =countryCode;
    }
    /** 地区全名*/
    public String getFullName(){
        return fullName;
    }
    /** 地区全名*/
    public void setFullName(String fullName){
        this.fullName =fullName;
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
    /** 是否末级区域*/
    public Boolean getLeaf(){
        return leaf;
    }
    /** 是否末级区域*/
    public void setLeaf(Boolean leaf){
        this.leaf =leaf;
    }
    /** 区域层级*/
    public Integer getLevel(){
        return level;
    }
    /** 区域层级*/
    public void setLevel(Integer level){
        this.level =level;
    }
    /** 区域名称*/
    public String getName(){
        return name;
    }
    /** 区域名称*/
    public void setName(String name){
        this.name =name;
    }
    /** 名称代码*/
    public String getNameCode(){
        return nameCode;
    }
    /** 名称代码*/
    public void setNameCode(String nameCode){
        this.nameCode =nameCode;
    }
    /** 上级区域ID*/
    public Long getPid(){
        return pid;
    }
    /** 上级区域ID*/
    public void setPid(Long pid){
        this.pid =pid;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 短名全称*/
    public String getShortFullName(){
        return shortFullName;
    }
    /** 短名全称*/
    public void setShortFullName(String shortFullName){
        this.shortFullName =shortFullName;
    }
    /** 短名*/
    public String getShortName(){
        return shortName;
    }
    /** 短名*/
    public void setShortName(String shortName){
        this.shortName =shortName;
    }
    /** 区域单位*/
    public String getUnit(){
        return unit;
    }
    /** 区域单位*/
    public void setUnit(String unit){
        this.unit =unit;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
    /** 地区全路径（区域上下级id拼接）*/
	public String getFullPath(){
		return fullPath;
	}
	/** 地区全路径（区域上下级id拼接）*/
	public void setFullPath(String fullPath){
		this.fullPath = fullPath;
	}
    /** 邮政编码(美国计算税费)*/
    public String getZip(){
        return zip;
    }
    /** 邮政编码(美国计算税费)*/
    public void setZip(String zip){
        this.zip =zip;
    }
}