package cn.farwalker.ravv.service.base.area.model;
import java.util.Date;
import java.util.List;

import cn.farwalker.ravv.service.base.area.constant.CountryCodeEnum;
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
import cn.farwalker.ravv.service.base.area.biz.IBaseAreaBiz;

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
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        name("name"),
        pid("pid"),
        remark("remark"),
        countryCode("country_code"),
        fullPath("full_path"),
        controlCity("control_city");
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

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("name")
    @DDLColumn(name="name",comment="区域名称",length=255)
    private String name;

    @TableField("pid")
    @DDLColumn(name="pid",comment="上级区域ID")
    private Long pid;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("country_code")
    @DDLColumn(name="country_code",comment="国家编码")
    private CountryCodeEnum countryCode;

    @TableField("full_path")
    @DDLColumn(name="full_path",comment="地区全路径（区域上下级id拼接）",length=255)
    private String fullPath;

    @TableField("control_city")
    @DDLColumn(name="control_city",comment="是否是直辖市",length=11)
    private Boolean controlCity;
    /** 区域ID*/
    public Long getId(){
        return id;
    }
    /** 区域ID*/
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
    /** 区域名称*/
    public String getName(){
        return name;
    }
    /** 区域名称*/
    public void setName(String name){
        this.name =name;
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
    /** 国家编码*/
    public CountryCodeEnum getCountryCode(){
        return countryCode;
    }
    /** 国家编码*/
    public void setCountryCode(CountryCodeEnum countryCode){
        this.countryCode =countryCode;
    }
    /** 地区全路径（区域上下级id拼接）*/
    public String getFullPath(){
        return fullPath;
    }
    /** 地区全路径（区域上下级id拼接）*/
    public void setFullPath(String fullPath){
        this.fullPath =fullPath;
    }
    /** 是否是直辖市*/
    public Boolean getControlCity(){
        return controlCity;
    }
    /** 是否是直辖市*/
    public void setControlCity(Boolean controlCity){
        this.controlCity =controlCity;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}