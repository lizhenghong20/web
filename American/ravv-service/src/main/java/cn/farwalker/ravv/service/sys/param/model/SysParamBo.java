package cn.farwalker.ravv.service.sys.param.model;
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

/**
 * 系统参数
 * 
 * @author generateModel.java
 */
@TableName(SysParamBo.TABLE_NAME)
@DDLTable(name=SysParamBo.TABLE_NAME,comment="系统参数")
public class SysParamBo extends Model<SysParamBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        code("code"),
        gmtCreate("gmt_create"),
        gmtModifield("gmt_modifield"),
        name("name"),
        pvalue("pvalue"),
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
    /** 数据表名:sys_param*/
    public static final String TABLE_NAME = "sys_param";
    private static final long serialVersionUID = 641472077L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="")
    private Long id;

    @TableField("code")
    @DDLColumn(name="code",comment="编码")
    private String code;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField("gmt_modifield")
    @DDLColumn(name="gmt_modifield",comment="修改时间")
    private Date gmtModifield;

    @TableField("name")
    @DDLColumn(name="name",comment="参数名称",length=255)
    private String name;

    @TableField("pvalue")
    @DDLColumn(name="pvalue",comment="参数值",length=1023)
    private String pvalue;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;
    /** */
    public Long getId(){
        return id;
    }
    /** */
    public void setId(Long id){
        this.id =id;
    }
    /** 编码*/
    public String getCode(){
        return code;
    }
    /** 编码*/
    public void setCode(String code){
        this.code =code;
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
    public Date getGmtModifield(){
        return gmtModifield;
    }
    /** 修改时间*/
    public void setGmtModifield(Date gmtModifield){
        this.gmtModifield =gmtModifield;
    }
    /** 参数名称*/
    public String getName(){
        return name;
    }
    /** 参数名称*/
    public void setName(String name){
        this.name =name;
    }
    /** 参数值*/
    public String getPvalue(){
        return pvalue;
    }
    /** 参数值*/
    public void setPvalue(String pvalue){
        this.pvalue =pvalue;
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