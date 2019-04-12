package cn.farwalker.ravv.service.base.storehouse.model;
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
import cn.farwalker.ravv.service.base.storehouse.biz.IStorehouseUserBiz;

/**
 * 仓库的操作用户
 * 
 * @author generateModel.java
 */
@TableName(StorehouseUserBo.TABLE_NAME)
@DDLTable(name=StorehouseUserBo.TABLE_NAME,comment="仓库的操作用户")
public class StorehouseUserBo extends Model<StorehouseUserBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        gmtCreate("gmt_create"),
        gmtModifield("gmt_modifield"),
        storeId("store_id"),
        userId("user_id"),
        userName("user_name");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:storehouse_user*/
    public static final String TABLE_NAME = "storehouse_user";
    private static final long serialVersionUID = 1424272727L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="ID")
    private Long id;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField("gmt_modifield")
    @DDLColumn(name="gmt_modifield",comment="修改时间")
    private Date gmtModifield;

    @TableField("store_id")
    @DDLColumn(name="store_id",comment="仓库ID")
    private Long storeId;

    @TableField("user_id")
    @DDLColumn(name="user_id",comment="用户id")
    private Long userId;

    @TableField("user_name")
    @DDLColumn(name="user_name",comment="冗余用户名",length=255)
    private String userName;
    /** ID*/
    public Long getId(){
        return id;
    }
    /** ID*/
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
    public Date getGmtModifield(){
        return gmtModifield;
    }
    /** 修改时间*/
    public void setGmtModifield(Date gmtModifield){
        this.gmtModifield =gmtModifield;
    }
    /** 仓库ID*/
    public Long getStoreId(){
        return storeId;
    }
    /** 仓库ID*/
    public void setStoreId(Long storeId){
        this.storeId =storeId;
    }
    /** 用户id*/
    public Long getUserId(){
        return userId;
    }
    /** 用户id*/
    public void setUserId(Long userId){
        this.userId =userId;
    }
    /** 冗余用户名*/
    public String getUserName(){
        return userName;
    }
    /** 冗余用户名*/
    public void setUserName(String userName){
        this.userName =userName;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}