package cn.farwalker.ravv.service.test.model;
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
 * 测试
 * 
 * @author generateModel.java
 */
@TableName(BaseTestBo.TABLE_NAME)
@DDLTable(name=BaseTestBo.TABLE_NAME,comment="测试")
public class BaseTestBo extends Model<BaseTestBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        name("name"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        ramark("ramark");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:base_test*/
    public static final String TABLE_NAME = "base_test";
    private static final long serialVersionUID = 1190534864L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键",length=20)
    private Long id;

    @TableField("name")
    @DDLColumn(name="name",comment="测试名称",length=255)
    private String name;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("ramark")
    @DDLColumn(name="ramark",comment="备注",length=2000)
    private String ramark;
    /** 主键*/
    public Long getId(){
        return id;
    }
    /** 主键*/
    public void setId(Long id){
        this.id =id;
    }
    /** 测试名称*/
    public String getName(){
        return name;
    }
    /** 测试名称*/
    public void setName(String name){
        this.name =name;
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
    public String getRamark(){
        return ramark;
    }
    /** 备注*/
    public void setRamark(String ramark){
        this.ramark =ramark;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}