package cn.farwalker.ravv.service.sys.role.model;
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
import cn.farwalker.ravv.service.sys.role.biz.ISysRoleBiz;

/**
 * 角色表
 * 
 * @author generateModel.java
 */
@TableName(SysRoleBo.TABLE_NAME)
@DDLTable(name=SysRoleBo.TABLE_NAME,comment="角色表")
public class SysRoleBo extends Model<SysRoleBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        deptid("deptid"),
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
    /** 数据表名:sys_role*/
    public static final String TABLE_NAME = "sys_role";
    private static final long serialVersionUID = -2126053264L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键id")
    private Long id;

    @TableField("deptid")
    @DDLColumn(name="deptid",comment="部门名称")
    private Long deptid;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("name")
    @DDLColumn(name="name",comment="角色名称",length=255)
    private String name;

    @TableField("num")
    @DDLColumn(name="num",comment="序号")
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
    /** 部门名称*/
    public Long getDeptid(){
        return deptid;
    }
    /** 部门名称*/
    public void setDeptid(Long deptid){
        this.deptid =deptid;
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
    /** 角色名称*/
    public String getName(){
        return name;
    }
    /** 角色名称*/
    public void setName(String name){
        this.name =name;
    }
    /** 序号*/
    public Integer getNum(){
        return num;
    }
    /** 序号*/
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