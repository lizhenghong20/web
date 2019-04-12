package cn.farwalker.ravv.service.sys.relation.model;
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
import cn.farwalker.ravv.service.sys.relation.biz.ISysRelationBiz;

/**
 * 角色和菜单关联表
 * 
 * @author generateModel.java
 */
@TableName(SysRelationBo.TABLE_NAME)
@DDLTable(name=SysRelationBo.TABLE_NAME,comment="角色和菜单关联表")
public class SysRelationBo extends Model<SysRelationBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        menuid("menuid"),
        remark("remark"),
        roleid("roleid");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:sys_relation*/
    public static final String TABLE_NAME = "sys_relation";
    private static final long serialVersionUID = 1121837558L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键")
    private Long id;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("menuid")
    @DDLColumn(name="menuid",comment="菜单id")
    private Long menuid;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("roleid")
    @DDLColumn(name="roleid",comment="角色id")
    private Long roleid;
    /** 主键*/
    public Long getId(){
        return id;
    }
    /** 主键*/
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
    /** 菜单id*/
    public Long getMenuid(){
        return menuid;
    }
    /** 菜单id*/
    public void setMenuid(Long menuid){
        this.menuid =menuid;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 角色id*/
    public Long getRoleid(){
        return roleid;
    }
    /** 角色id*/
    public void setRoleid(Long roleid){
        this.roleid =roleid;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}