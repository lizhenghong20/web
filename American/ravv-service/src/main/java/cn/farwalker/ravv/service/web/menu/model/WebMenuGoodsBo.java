package cn.farwalker.ravv.service.web.menu.model;
import java.util.Date;
import java.util.List;
import com.cangwu.frame.orm.core.BaseBo;
import com.cangwu.frame.orm.core.annotation.LoadJoinValue;
import com.cangwu.frame.orm.ddl.annotation.DDLColumn;
import com.cangwu.frame.orm.ddl.annotation.DDLTable;
import com.cangwu.frame.orm.ddl.annotation.NotColumn;
import com.cangwu.frame.orm.core.IFieldKey;
import java.io.Serializable;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;

import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.web.menu.biz.IWebMenuGoodsBiz;

/**
 * 菜单与商品的关系
 * 
 * @author generateModel.java
 */
@TableName(WebMenuGoodsBo.TABLE_NAME)
@DDLTable(name=WebMenuGoodsBo.TABLE_NAME,comment="菜单与商品的关系")
public class WebMenuGoodsBo extends Model<WebMenuGoodsBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        goodsId("goods_id"),
        menuId("menu_id"),
        picture("picture"),
        sequence("sequence"),
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
    /** 数据表名:web_menu_goods*/
    public static final String TABLE_NAME = "web_menu_goods";
    private static final long serialVersionUID = -1766410224L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="")
    private Long id;

    @TableField("goods_id")
    @DDLColumn(name="goods_id",comment="")
    private Long goodsId;

    @TableField("menu_id")
    @DDLColumn(name="menu_id",comment="")
    private Long menuId;

    @TableField("picture")
    @DDLColumn(name="picture",comment="保留图片",length=1024)
    private String picture;

    @TableField("sequence")
    @DDLColumn(name="sequence",comment="")
    private Integer sequence;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="")
    private Date gmtModified;
    
    /** null*/
    public Long getId(){
        return id;
    }
    /** null*/
    public void setId(Long id){
        this.id =id;
    }
    /** null*/
    public Long getGoodsId(){
        return goodsId;
    }
    /** null*/
    public void setGoodsId(Long goodsId){
        this.goodsId =goodsId;
    }
    /** null*/
    public Long getMenuId(){
        return menuId;
    }
    /** null*/
    public void setMenuId(Long menuId){
        this.menuId =menuId;
    }
    /** 保留图片*/
    public String getPicture(){
        return picture;
    }
    /** 保留图片*/
    public void setPicture(String picture){
        this.picture =picture;
    }
    /** null*/
    public Integer getSequence(){
        return sequence;
    }
    /** null*/
    public void setSequence(Integer sequence){
        this.sequence =sequence;
    }
    @Override
    protected Serializable pkVal(){
        return id;
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
   
}