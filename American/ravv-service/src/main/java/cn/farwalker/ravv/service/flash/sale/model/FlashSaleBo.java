package cn.farwalker.ravv.service.flash.sale.model;
import java.util.Date;
import java.util.List;

import cn.farwalker.ravv.common.constants.FlashSaleStatusEnum;
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
import cn.farwalker.ravv.service.flash.sale.biz.IFlashSaleBiz;

/**
 * 闪购/限时购
 * 
 * @author generateModel.java
 */
@TableName(FlashSaleBo.TABLE_NAME)
@DDLTable(name=FlashSaleBo.TABLE_NAME,comment="闪购/限时购")
public class FlashSaleBo extends Model<FlashSaleBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        endtime("endtime"),
        freezetime("freezetime"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        remark("remark"),
        starttime("starttime"),
        status("status"),
        title("title");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:flash_sale*/
    public static final String TABLE_NAME = "flash_sale";
    private static final long serialVersionUID = -1370643740L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="闪购ID")
    private Long id;

    @TableField("endtime")
    @DDLColumn(name="endtime",comment="")
    private Date endtime;

    @TableField("freezetime")
    @DDLColumn(name="freezetime",comment="冻结购买的开始时间")
    private Date freezetime;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("starttime")
    @DDLColumn(name="starttime",comment="")
    private Date starttime;

    @TableField("title")
    @DDLColumn(name="title",comment="",length=255)
    private String title;
    /** 闪购ID*/
    public Long getId(){
        return id;
    }
    /** 闪购ID*/
    public void setId(Long id){
        this.id =id;
    }
    /** */
    public Date getEndtime(){
        return endtime;
    }
    /** */
    public void setEndtime(Date endtime){
        this.endtime =endtime;
    }
    /** 冻结购买的开始时间*/
    public Date getFreezetime(){
        return freezetime;
    }
    /** 冻结购买的开始时间*/
    public void setFreezetime(Date freezetime){
        this.freezetime =freezetime;
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
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** */
    public Date getStarttime(){
        return starttime;
    }
    /** */
    public void setStarttime(Date starttime){
        this.starttime =starttime;
    }
    
    /** */
    public String getTitle(){
        return title;
    }
    /** */
    public void setTitle(String title){
        this.title =title;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}