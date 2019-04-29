package cn.farwalker.ravv.service.base.config.model;
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
import cn.farwalker.ravv.service.base.config.biz.IBaseConfigBiz;

/**
 * auth配置类
 * 
 * @author generateModel.java
 */
@TableName(BaseConfigBo.TABLE_NAME)
@DDLTable(name=BaseConfigBo.TABLE_NAME,comment="auth配置类")
public class BaseConfigBo extends Model<BaseConfigBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        authId("auth_id"),
        authToken("auth_token"),
        configType("config_type"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        remark("remark");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:base_config*/
    public static final String TABLE_NAME = "base_config";
    private static final long serialVersionUID = -2085084864L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键",length=20)
    private Long id;

    @TableField("auth_id")
    @DDLColumn(name="auth_id",comment="authId",length=255)
    private String authId;

    @TableField("auth_token")
    @DDLColumn(name="auth_token",comment="authToken",length=255)
    private String authToken;

    @TableField("config_type")
    @DDLColumn(name="config_type",comment="config类型",length=255)
    private String configType;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;
    /** 主键*/
    public Long getId(){
        return id;
    }
    /** 主键*/
    public void setId(Long id){
        this.id =id;
    }
    /** authId*/
    public String getAuthId(){
        return authId;
    }
    /** authId*/
    public void setAuthId(String authId){
        this.authId =authId;
    }
    /** authToken*/
    public String getAuthToken(){
        return authToken;
    }
    /** authToken*/
    public void setAuthToken(String authToken){
        this.authToken =authToken;
    }
    /** config类型*/
    public String getConfigType(){
        return configType;
    }
    /** config类型*/
    public void setConfigType(String configType){
        this.configType =configType;
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
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}