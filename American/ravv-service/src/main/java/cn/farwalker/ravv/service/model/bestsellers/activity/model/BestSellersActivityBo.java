package cn.farwalker.ravv.service.model.bestsellers.activity.model;
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
import cn.farwalker.ravv.service.model.bestsellers.activity.biz.IBestSellersActivityBiz;

/**
 * bestseller活动
 * 
 * @author generateModel.java
 */
@TableName(BestSellersActivityBo.TABLE_NAME)
@DDLTable(name=BestSellersActivityBo.TABLE_NAME,comment="bestseller活动")
public class BestSellersActivityBo extends Model<BestSellersActivityBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        goodsId("goods_id"),
        imgUrl("img_url"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        remark("remark"),
        skuId("sku_id"),
        menuId("menu_id"),
        title("title"),
        
        content("content"),
        jumpTo("jump_to"),
        sequence("sequence");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:best_sellers_activity*/
    public static final String TABLE_NAME = "best_sellers_activity";
    private static final long serialVersionUID = -903514612L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键")
    private Long id;

    @TableField("goods_id")
    @DDLColumn(name="goods_id",comment="商品id")
    private Long goodsId;

    @TableField("img_url")
    @DDLColumn(name="img_url",comment="图片地址",length=1024)
    private String imgUrl;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("sku_id")
    @DDLColumn(name="sku_id",comment="skuid")
    private Long skuId;

    @TableField("menu_id")
    @DDLColumn(name="menu_id",comment="菜单id")
    private Long menuId;

    @TableField("title")
    @DDLColumn(name="title",comment="活动标题",length=2000)
    private String title;

    @TableField("content")
    @DDLColumn(name="content",comment="活动内容",length=2000)
    private String content;

    @TableField("jump_to")
    @DDLColumn(name="jump_to",comment="活动链接",length=2000)
    private String jumpTo;

    @TableField("sequence")
    @DDLColumn(name="sequence",comment="顺序")
    private Integer sequence;
    /** 主键*/
    public Long getId(){
        return id;
    }
    /** 主键*/
    public void setId(Long id){
        this.id =id;
    }
    /** 商品id*/
    public Long getGoodsId(){
        return goodsId;
    }
    /** 商品id*/
    public void setGoodsId(Long goodsId){
        this.goodsId =goodsId;
    }
    /** 图片地址*/
    public String getImgUrl(){
        return imgUrl;
    }
    /** 图片地址*/
    public void setImgUrl(String imgUrl){
        this.imgUrl =imgUrl;
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
    @Override
    protected Serializable pkVal(){
        return id;
    }
    /** skuid*/
    public Long getSkuId(){
        return skuId;
    }
    /** skuid*/
    public void setSkuId(Long skuId){
        this.skuId =skuId;
    }
    /** 菜单id*/
    public Long getMenuId(){
        return menuId;
    }
    /** 菜单id*/
    public void setMenuId(Long menuId){
        this.menuId =menuId;
    }
    /** 活动标题*/
    public String getTitle(){
        return title;
    }
    /** 活动标题*/
    public void setTitle(String title){
        this.title =title;
    }
    /** 活动内容*/
    public String getContent(){
        return content;
    }
    /** 活动内容*/
    public void setContent(String content){
        this.content =content;
    }
    /** 活动链接*/
    public String getJumpTo(){
        return jumpTo;
    }
    /** 活动链接*/
    public void setJumpTo(String jumpTo){
        this.jumpTo =jumpTo;
    }
    /** 顺序*/
    public Integer getSequence(){
        return sequence;
    }
    /** 顺序*/
    public void setSequence(Integer sequence){
        this.sequence =sequence;
    }
}