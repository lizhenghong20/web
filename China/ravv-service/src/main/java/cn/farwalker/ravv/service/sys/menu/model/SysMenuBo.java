package cn.farwalker.ravv.service.sys.menu.model;
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
import cn.farwalker.ravv.service.sys.menu.biz.ISysMenuBiz;

/**
 * 菜单表
 * 
 * @author generateModel.java
 */
@TableName(SysMenuBo.TABLE_NAME)
@DDLTable(name=SysMenuBo.TABLE_NAME,comment="菜单表")
public class SysMenuBo extends Model<SysMenuBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        code("code"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        icon("icon"),
        ismenu("ismenu"),
        isopen("isopen"),
        levels("levels"),
        menutype("menutype"),
        
        name("name"),
        num("num"),
        pcode("pcode"),
        pcodes("pcodes"),
        remark("remark"),
        status("status"),
        tips("tips"),
        url("url");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:sys_menu*/
    public static final String TABLE_NAME = "sys_menu";
    private static final long serialVersionUID = 2016608025L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键id")
    private Long id;

    @TableField("code")
    @DDLColumn(name="code",comment="菜单编号",length=255)
    private String code;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("icon")
    @DDLColumn(name="icon",comment="菜单图标",length=255)
    private String icon;

    @TableField("ismenu")
    @DDLColumn(name="ismenu",comment="是否是菜单")
    private Integer ismenu;

    @TableField("isopen")
    @DDLColumn(name="isopen",comment="是否打开")
    private Integer isopen;

    @TableField("levels")
    @DDLColumn(name="levels",comment="菜单层级")
    private Integer levels;

    @TableField("menutype")
    @DDLColumn(name="menutype",comment="菜单类型",length=63)
    private String menutype;

    @TableField("name")
    @DDLColumn(name="name",comment="菜单名称",length=255)
    private String name;

    @TableField("num")
    @DDLColumn(name="num",comment="菜单排序号")
    private Integer num;

    @TableField("pcode")
    @DDLColumn(name="pcode",comment="菜单父编号",length=255)
    private String pcode;

    @TableField("pcodes")
    @DDLColumn(name="pcodes",comment="当前菜单的所有父菜单编号",length=255)
    private String pcodes;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("status")
    @DDLColumn(name="status",comment="菜单状态")
    private Integer status;

    @TableField("tips")
    @DDLColumn(name="tips",comment="备注",length=255)
    private String tips;

    @TableField("url")
    @DDLColumn(name="url",comment="url地址",length=1024)
    private String url;
    /** 主键id*/
    public Long getId(){
        return id;
    }
    /** 主键id*/
    public void setId(Long id){
        this.id =id;
    }
    /** 菜单编号*/
    public String getCode(){
        return code;
    }
    /** 菜单编号*/
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
    public Date getGmtModified(){
        return gmtModified;
    }
    /** 修改时间*/
    public void setGmtModified(Date gmtModified){
        this.gmtModified =gmtModified;
    }
    /** 菜单图标*/
    public String getIcon(){
        return icon;
    }
    /** 菜单图标*/
    public void setIcon(String icon){
        this.icon =icon;
    }
    /** 是否是菜单*/
    public Integer getIsmenu(){
        return ismenu;
    }
    /** 是否是菜单*/
    public void setIsmenu(Integer ismenu){
        this.ismenu =ismenu;
    }
    /** 是否打开*/
    public Integer getIsopen(){
        return isopen;
    }
    /** 是否打开*/
    public void setIsopen(Integer isopen){
        this.isopen =isopen;
    }
    /** 菜单层级*/
    public Integer getLevels(){
        return levels;
    }
    /** 菜单层级*/
    public void setLevels(Integer levels){
        this.levels =levels;
    }
    /** 菜单类型*/
    public String getMenutype(){
        return menutype;
    }
    /** 菜单类型*/
    public void setMenutype(String menutype){
        this.menutype =menutype;
    }
    /** 菜单名称*/
    public String getName(){
        return name;
    }
    /** 菜单名称*/
    public void setName(String name){
        this.name =name;
    }
    /** 菜单排序号*/
    public Integer getNum(){
        return num;
    }
    /** 菜单排序号*/
    public void setNum(Integer num){
        this.num =num;
    }
    /** 菜单父编号*/
    public String getPcode(){
        return pcode;
    }
    /** 菜单父编号*/
    public void setPcode(String pcode){
        this.pcode =pcode;
    }
    /** 当前菜单的所有父菜单编号*/
    public String getPcodes(){
        return pcodes;
    }
    /** 当前菜单的所有父菜单编号*/
    public void setPcodes(String pcodes){
        this.pcodes =pcodes;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 菜单状态*/
    public Integer getStatus(){
        return status;
    }
    /** 菜单状态*/
    public void setStatus(Integer status){
        this.status =status;
    }
    /** 备注*/
    public String getTips(){
        return tips;
    }
    /** 备注*/
    public void setTips(String tips){
        this.tips =tips;
    }
    /** url地址*/
    public String getUrl(){
        return url;
    }
    /** url地址*/
    public void setUrl(String url){
        this.url =url;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
    
}