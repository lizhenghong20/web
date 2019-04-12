package cn.farwalker.ravv.service.sys.dept.model;
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
import cn.farwalker.ravv.service.sys.dept.biz.ISysDeptBiz;

/**
 * 部门表
 * 
 * @author generateModel.java
 */
@TableName(SysDeptBo.TABLE_NAME)
@DDLTable(name=SysDeptBo.TABLE_NAME,comment="部门表")
public class SysDeptBo extends Model<SysDeptBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        fullname("fullname"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        num("num"),
        pid("pid"),
        pids("pids"),
        remark("remark"),
        simplename("simplename"),
        
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
    /** 数据表名:sys_dept*/
    public static final String TABLE_NAME = "sys_dept";
    private static final long serialVersionUID = 1759004287L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键id")
    private Long id;

    @TableField("fullname")
    @DDLColumn(name="fullname",comment="全称",length=255)
    private String fullname;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("num")
    @DDLColumn(name="num",comment="排序")
    private Integer num;

    @TableField("pid")
    @DDLColumn(name="pid",comment="父级id")
    private Long pid;

    @TableField("pids")
    @DDLColumn(name="pids",comment="父级ids",length=255)
    private String pids;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("simplename")
    @DDLColumn(name="simplename",comment="简称",length=255)
    private String simplename;

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
    /** 全称*/
    public String getFullname(){
        return fullname;
    }
    /** 全称*/
    public void setFullname(String fullname){
        this.fullname =fullname;
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
    /** 排序*/
    public Integer getNum(){
        return num;
    }
    /** 排序*/
    public void setNum(Integer num){
        this.num =num;
    }
    /** 父级id*/
    public Long getPid(){
        return pid;
    }
    /** 父级id*/
    public void setPid(Long pid){
        this.pid =pid;
    }
    /** 父级ids*/
    public String getPids(){
        return pids;
    }
    /** 父级ids*/
    public void setPids(String pids){
        this.pids =pids;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 简称*/
    public String getSimplename(){
        return simplename;
    }
    /** 简称*/
    public void setSimplename(String simplename){
        this.simplename =simplename;
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