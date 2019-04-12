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

import cn.farwalker.ravv.service.web.webmodel.constants.ShowTypeEnum;

/**
 * 模块商品
 * 
 * @author generateModel.java
 */
@TableName(WebModelGoodsBo.TABLE_NAME)
@DDLTable(name=WebModelGoodsBo.TABLE_NAME,comment="模块商品")
public class WebModelGoodsBo extends Model<WebModelGoodsBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        goodsId("goods_id"),
        menuId("menu_id"),
        modelCode("model_code"),
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
    /** 数据表名:web_model_goods*/
    public static final String TABLE_NAME = "web_model_goods";
    private static final long serialVersionUID = 34670638L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="")
    private Long id;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="")
    private Date gmtModified;

    @TableField("goods_id")
    @DDLColumn(name="goods_id",comment="")
    private Long goodsId;

    @TableField("menu_id")
    @DDLColumn(name="menu_id",comment="模块商品的归类(为新品界面的显示设计,相同id归组)")
    private Long menuId;

    @TableField("model_code")
    @DDLColumn(name="model_code",comment="模块编码")
    private String modelCode;

    @TableField("sequence")
    @DDLColumn(name="sequence",comment="顺序(基本为新品界面的显示设计)")
    private Integer sequence;

    @TableField("show_type")
    @DDLColumn(name="show_type",comment="显示类型(首页显示,某个页面显示,不显示-现阶段按boolean使用)",length=63)
    private ShowTypeEnum showType;
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
    /** */
    public Long getGoodsId(){
        return goodsId;
    }
    /** */
    public void setGoodsId(Long goodsId){
        this.goodsId =goodsId;
    }
    /** 模块商品的归类(为新品界面的显示设计,相同id归组)*/
    public Long getMenuId(){
        return menuId;
    }
    /** 模块商品的归类(为新品界面的显示设计,相同id归组)*/
    public void setMenuId(Long menuId){
        this.menuId =menuId;
    }
    /** 模块编码*/
    public String getModelCode(){
        return modelCode;
    }
    /** 模块编码*/
    public void setModelCode(String modelCode){
        this.modelCode =modelCode;
    }
    /** 顺序(基本为新品界面的显示设计)*/
    public Integer getSequence(){
        return sequence;
    }
    /** 顺序(基本为新品界面的显示设计)*/
    public void setSequence(Integer sequence){
        this.sequence =sequence;
    }
    /** 显示类型(首页显示,某个页面显示,不显示-现阶段按boolean使用)*/
    public ShowTypeEnum getShowType(){
        return showType;
    }
    /** 显示类型(首页显示,某个页面显示,不显示-现阶段按boolean使用)*/
    public void setShowType(ShowTypeEnum showType){
        this.showType =showType;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}