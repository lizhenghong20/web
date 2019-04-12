package cn.farwalker.ravv.service.sys.dict.model;
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
import cn.farwalker.ravv.service.sys.dict.biz.ISysDictBiz;

/**
 * 字典表
 * 
 * @author generateModel.java
 */
@TableName(SysDictBo.TABLE_NAME)
@DDLTable(name=SysDictBo.TABLE_NAME,comment="字典表")
public class SysDictBo extends Model<SysDictBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        name("name"),
        num("num"),
        pid("pid"),
        remark("remark"),
        tips("tips");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:sys_dict*/
    public static final String TABLE_NAME = "sys_dict";
    private static final long serialVersionUID = 1762311088L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键id")
    private Long id;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("name")
    @DDLColumn(name="name",comment="名称",length=255)
    private String name;

    @TableField("num")
    @DDLColumn(name="num",comment="排序")
    private Integer num;

    @TableField("pid")
    @DDLColumn(name="pid",comment="上级id")
    private Long pid;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("tips")
    @DDLColumn(name="tips",comment="提示",length=255)
    private String tips;
    /** 主键id*/
    public Long getId(){
        return id;
    }
    /** 主键id*/
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
    /** 名称*/
    public String getName(){
        return name;
    }
    /** 名称*/
    public void setName(String name){
        this.name =name;
    }
    /** 排序*/
    public Integer getNum(){
        return num;
    }
    /** 排序*/
    public void setNum(Integer num){
        this.num =num;
    }
    /** 上级id*/
    public Long getPid(){
        return pid;
    }
    /** 上级id*/
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
    /** 提示*/
    public String getTips(){
        return tips;
    }
    /** 提示*/
    public void setTips(String tips){
        this.tips =tips;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}