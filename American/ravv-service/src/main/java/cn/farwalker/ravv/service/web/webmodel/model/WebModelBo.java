package cn.farwalker.ravv.service.web.webmodel.model;
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

import cn.farwalker.ravv.service.web.webmodel.constants.ModelShowTypeEnum;

/**
 * 首页模块
 * 
 * @author generateModel.java
 */
@TableName(WebModelBo.TABLE_NAME)
@DDLTable(name=WebModelBo.TABLE_NAME,comment="首页模块")
public class WebModelBo extends Model<WebModelBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        modelCode("model_code"),
        modelName("model_name"),
        sequence("sequence"),
        showType("show_type");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:web_model*/
    public static final String TABLE_NAME = "web_model";
    private static final long serialVersionUID = -192749694L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="")
    private Long id;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="")
    private Date gmtModified;

    @TableField("model_code")
    @DDLColumn(name="model_code",comment="模块编码(代替id)")
    private String modelCode;

    @TableField("model_name")
    @DDLColumn(name="model_name",comment="模块名(新品、热销、推荐....)",length=255)
    private String modelName;

    @TableField("sequence")
    @DDLColumn(name="sequence",comment="顺序")
    private Integer sequence;

    @TableField("show_type")
    @DDLColumn(name="show_type",comment="显示类型(首页显示,某个页面显示,不显示)",length=63)
    private ModelShowTypeEnum showType;
    /** */
    public Long getId(){
        return id;
    }
    /** */
    public void setId(Long id){
        this.id =id;
    }
    /** */
    public Date getGmtCreate(){
        return gmtCreate;
    }
    /** */
    public void setGmtCreate(Date gmtCreate){
        this.gmtCreate =gmtCreate;
    }
    /** */
    public Date getGmtModified(){
        return gmtModified;
    }
    /** */
    public void setGmtModified(Date gmtModified){
        this.gmtModified =gmtModified;
    }
    /** 模块编码(代替id)*/
    public String getModelCode(){
        return modelCode;
    }
    /** 模块编码(代替id)*/
    public void setModelCode(String modelCode){
        this.modelCode =modelCode;
    }
    /** 模块名(新品、热销、推荐....)*/
    public String getModelName(){
        return modelName;
    }
    /** 模块名(新品、热销、推荐....)*/
    public void setModelName(String modelName){
        this.modelName =modelName;
    }
    /** 顺序*/
    public Integer getSequence(){
        return sequence;
    }
    /** 顺序*/
    public void setSequence(Integer sequence){
        this.sequence =sequence;
    }
    /** 显示类型(首页显示,某个页面显示,不显示)*/
    public ModelShowTypeEnum getShowType(){
        return showType;
    }
    /** 显示类型(首页显示,某个页面显示,不显示)*/
    public void setShowType(ModelShowTypeEnum showType){
        this.showType =showType;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}