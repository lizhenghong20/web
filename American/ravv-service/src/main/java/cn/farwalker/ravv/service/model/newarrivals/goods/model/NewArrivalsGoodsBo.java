package cn.farwalker.ravv.service.model.newarrivals.goods.model;
import java.math.BigDecimal;
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
import cn.farwalker.ravv.service.model.newarrivals.goods.biz.INewArrivalsGoodsBiz;

/**
 * 新品到达商品
 * 
 * @author generateModel.java
 */
@TableName(NewArrivalsGoodsBo.TABLE_NAME)
@DDLTable(name=NewArrivalsGoodsBo.TABLE_NAME,comment="新品到达商品")
public class NewArrivalsGoodsBo extends Model<NewArrivalsGoodsBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        goodsId("goods_id"),
        skuId("sku_id"),
        imgUrl("img_url"),
        price("price"),
        display("display"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        remark("remark"),
        
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
    /** 数据表名:new_arrivals_goods*/
    public static final String TABLE_NAME = "new_arrivals_goods";
    private static final long serialVersionUID = 768152489L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键")
    private Long id;

    @TableField("goods_id")
    @DDLColumn(name="goods_id",comment="商品id")
    private Long goodsId;

    @TableField("sku_id")
    @DDLColumn(name="sku_id",comment="skuid")
    private Long skuId;

    @TableField("img_url")
    @DDLColumn(name="img_url",comment="图片地址",length=1024)
    private String imgUrl;

    @TableField("price")
    @DDLColumn(name="price",comment="商品价格")
    private BigDecimal price;

    @TableField("display")
    @DDLColumn(name="display",comment="是否展示到首页")
    private Integer display;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

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
    /** skuid*/
    public Long getSkuId(){
        return skuId;
    }
    /** skuid*/
    public void setSkuId(Long skuId){
        this.skuId =skuId;
    }
    /** 图片地址*/
    public String getImgUrl(){
        return imgUrl;
    }
    /** 图片地址*/
    public void setImgUrl(String imgUrl){
        this.imgUrl =imgUrl;
    }
    /** 商品价格*/
    public BigDecimal getPrice(){
        return price;
    }
    /** 商品价格*/
    public void setPrice(BigDecimal price){
        this.price =price;
    }
    /** 是否展示到首页*/
    public Integer getDisplay(){
        return display;
    }
    /** 是否展示到首页*/
    public void setDisplay(Integer display){
        this.display =display;
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
    /** 顺序*/
    public Integer getSequence(){
        return sequence;
    }
    /** 顺序*/
    public void setSequence(Integer sequence){
        this.sequence =sequence;
    }
}