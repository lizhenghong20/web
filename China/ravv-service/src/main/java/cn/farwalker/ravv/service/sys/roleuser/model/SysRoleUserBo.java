package cn.farwalker.ravv.service.sys.roleuser.model;
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
 * 角色和菜单关联表
 * 
 * @author generateModel.java
 */
@TableName(SysRoleUserBo.TABLE_NAME)
@DDLTable(name=SysRoleUserBo.TABLE_NAME,comment="角色和菜单关联表")
public class SysRoleUserBo extends Model<SysRoleUserBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        roleid("roleid"),
        userid("userid"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:sys_role_user*/
    public static final String TABLE_NAME = "sys_role_user";
    private static final long serialVersionUID = -1396352997L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键")
    private Long id;

    @TableField("roleid")
    @DDLColumn(name="roleid",comment="角色id")
    private Long roleid;

    @TableField("userid")
    @DDLColumn(name="userid",comment="菜单id")
    private Long userid;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;
    /** 主键*/
    public Long getId(){
        return id;
    }
    /** 主键*/
    public void setId(Long id){
        this.id =id;
    }
    /** 角色id*/
    public Long getRoleid(){
        return roleid;
    }
    /** 角色id*/
    public void setRoleid(Long roleid){
        this.roleid =roleid;
    }
    /** 菜单id*/
    public Long getUserid(){
        return userid;
    }
    /** 菜单id*/
    public void setUserid(Long userid){
        this.userid =userid;
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
    @Override
    protected Serializable pkVal(){
        return id;
    }
}