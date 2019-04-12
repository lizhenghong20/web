package cn.farwalker.ravv.service.base.storehouse.model;
import java.io.Serializable;
import java.util.Date;

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

import cn.farwalker.ravv.service.base.area.constant.CountryCodeEnum;
import cn.farwalker.waka.util.Tools;

/**
 * 仓库
 * 
 * @author generateModel.java
 */
@TableName(StorehouseBo.TABLE_NAME)
@DDLTable(name=StorehouseBo.TABLE_NAME,comment="仓库")
public class StorehouseBo extends Model<StorehouseBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        address("address"),
        countryCode("country_code"),
        areaid("areaid"),
        areaname("areaname"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        idcard("idcard"),
        linkman("linkman"),
        
        phone("phone"),
        remark("remark"),
        status("status"),
        storename("storename"),
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
    /** 数据表名:storehouse*/
    public static final String TABLE_NAME = "storehouse";
    private static final long serialVersionUID = 637162412L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="id")
    private Long id;

    @TableField("address")
    @DDLColumn(name="address",comment="详细地址",length=127)
    private String address;
    
    @TableField("country_code")
    @DDLColumn(name="country_code",comment="国家编码")
    private CountryCodeEnum countryCode;

    @TableField("areaid")
    @DDLColumn(name="areaid",comment="所在区域id(上下级id拼接)")
    private String areaid;

    @TableField("areaname")
    @DDLColumn(name="areaname",comment="所在区域",length=255)
    private String areaname;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("idcard")
    @DDLColumn(name="idcard",comment="身份证",length=127)
    private String idcard;

    @TableField("linkman")
    @DDLColumn(name="linkman",comment="联系人",length=255)
    private String linkman;

    @TableField("phone")
    @DDLColumn(name="phone",comment="电话",length=50)
    private String phone;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=127)
    private String remark;

    @TableField("status")
    @DDLColumn(name="status",comment="有效状态")
    private Boolean status;

    @TableField("storename")
    @DDLColumn(name="storename",comment="仓库名称",length=255)
    private String storename;

    @TableField("zip")
    @DDLColumn(name="zip",comment="邮政编码(美国计算税费)")
    private String zip;
    /** id*/
    public Long getId(){
        return id;
    }
    /** id*/
    public void setId(Long id){
        this.id =id;
    }
    /** 详细地址*/
    public String getAddress(){
        return address;
    }
    /** 详细地址*/
    public void setAddress(String address){
        this.address =address;
    }
    
    /** 所在区域id(上下级id拼接)*/
    public static String[] getAreaids(StorehouseBo bo){
    	String s = bo.getAreaid();
    	if(Tools.string.isEmpty(s)){
    		return new String[]{};
    	}
    	else{
    		return s.split("/");
    	}
    }
    
    /** 所在区域id(上下级id拼接)*/
    public String getAreaid(){
        return areaid;
    }
    /** 所在区域id(上下级id拼接)*/
    public void setAreaid(String areaid){
        this.areaid =areaid;
    }
    /** 所在区域*/
    public String getAreaname(){
        return areaname;
    }
    /** 所在区域*/
    public void setAreaname(String areaname){
        this.areaname =areaname;
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
    
    /** 身份证*/
    public String getIdcard(){
        return idcard;
    }
    /** 身份证*/
    public void setIdcard(String idcard){
        this.idcard =idcard;
    }
    /** 联系人*/
    public String getLinkman(){
        return linkman;
    }
    /** 联系人*/
    public void setLinkman(String linkman){
        this.linkman =linkman;
    }
    /** 电话*/
    public String getPhone(){
        return phone;
    }
    /** 电话*/
    public void setPhone(String phone){
        this.phone =phone;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 有效状态*/
    public Boolean getStatus(){
        return status;
    }
    /** 有效状态*/
    public void setStatus(Boolean status){
        this.status =status;
    }
    /** 仓库名称*/
    public String getStorename(){
        return storename;
    }
    /** 仓库名称*/
    public void setStorename(String storename){
        this.storename =storename;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
	
    /** 国家编码*/
    public CountryCodeEnum getCountryCode(){
		return countryCode;
	}
	
    /** 国家编码*/
    public void setCountryCode(CountryCodeEnum countryCode){
		this.countryCode = countryCode;
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