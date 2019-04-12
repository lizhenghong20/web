package cn.farwalker.ravv.service.sys.user.model;
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
import cn.farwalker.ravv.service.sys.user.biz.ISysUserBiz;

/**
 * 管理员表
 * 
 * @author generateModel.java
 */
@TableName(SysUserBo.TABLE_NAME)
@DDLTable(name=SysUserBo.TABLE_NAME,comment="管理员表")
public class SysUserBo extends Model<SysUserBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        account("account"),
        avatar("avatar"),
        birthday("birthday"),
        deptid("deptid"),
        email("email"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        name("name"),
        
        password("password"),
        phone("phone"),
        remark("remark"),
        roleid("roleid"),
        salt("salt"),
        sex("sex"),
        status("status");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:sys_user*/
    public static final String TABLE_NAME = "sys_user";
    private static final long serialVersionUID = -2036667771L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键id")
    private Long id;

    @TableField("account")
    @DDLColumn(name="account",comment="账号",length=255)
    private String account;

    @TableField("avatar")
    @DDLColumn(name="avatar",comment="头像",length=1024)
    private String avatar;

    @TableField("birthday")
    @DDLColumn(name="birthday",comment="生日")
    private Date birthday;

    @TableField("deptid")
    @DDLColumn(name="deptid",comment="部门id")
    private Long deptid;

    @TableField("email")
    @DDLColumn(name="email",comment="电子邮件",length=512)
    private String email;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("name")
    @DDLColumn(name="name",comment="名字",length=255)
    private String name;

    @TableField("password")
    @DDLColumn(name="password",comment="密码")
    private String password;

    @TableField("phone")
    @DDLColumn(name="phone",comment="电话",length=50)
    private String phone;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("roleid")
    @DDLColumn(name="roleid",comment="角色",length=255)
    private String roleid;

    @TableField("salt")
    @DDLColumn(name="salt",comment="md5密码盐",length=127)
    private String salt;

    @TableField("sex")
    @DDLColumn(name="sex",comment="性别",length=63)
    private String sex;

    @TableField("status")
    @DDLColumn(name="status",comment="状态")
    private Integer status;
    /** 主键id*/
    public Long getId(){
        return id;
    }
    /** 主键id*/
    public void setId(Long id){
        this.id =id;
    }
    /** 账号*/
    public String getAccount(){
        return account;
    }
    /** 账号*/
    public void setAccount(String account){
        this.account =account;
    }
    /** 头像*/
    public String getAvatar(){
        return avatar;
    }
    /** 头像*/
    public void setAvatar(String avatar){
        this.avatar =avatar;
    }
    /** 生日*/
    public Date getBirthday(){
        return birthday;
    }
    /** 生日*/
    public void setBirthday(Date birthday){
        this.birthday =birthday;
    }
    /** 部门id*/
    public Long getDeptid(){
        return deptid;
    }
    /** 部门id*/
    public void setDeptid(Long deptid){
        this.deptid =deptid;
    }
    /** 电子邮件*/
    public String getEmail(){
        return email;
    }
    /** 电子邮件*/
    public void setEmail(String email){
        this.email =email;
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
    /** 名字*/
    public String getName(){
        return name;
    }
    /** 名字*/
    public void setName(String name){
        this.name =name;
    }
    /** 密码*/
    public String getPassword(){
        return password;
    }
    /** 密码*/
    public void setPassword(String password){
        this.password =password;
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
    /** 角色*/
    public String getRoleid(){
        return roleid;
    }
    /** 角色*/
    public void setRoleid(String roleid){
        this.roleid =roleid;
    }
    /** md5密码盐*/
    public String getSalt(){
        return salt;
    }
    /** md5密码盐*/
    public void setSalt(String salt){
        this.salt =salt;
    }
    /** 性别*/
    public String getSex(){
        return sex;
    }
    /** 性别*/
    public void setSex(String sex){
        this.sex =sex;
    }
    /** 状态*/
    public Integer getStatus(){
        return status;
    }
    /** 状态*/
    public void setStatus(Integer status){
        this.status =status;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}