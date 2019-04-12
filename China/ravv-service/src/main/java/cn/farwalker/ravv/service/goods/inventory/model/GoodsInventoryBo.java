package cn.farwalker.ravv.service.goods.inventory.model;
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
import cn.farwalker.ravv.service.goods.inventory.biz.IGoodsInventoryBiz;

/**
 * 商品库存
 * 
 * @author generateModel.java
 */
@TableName(GoodsInventoryBo.TABLE_NAME)
@DDLTable(name=GoodsInventoryBo.TABLE_NAME,comment="商品库存")
public class GoodsInventoryBo extends Model<GoodsInventoryBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        alarmStockNum("alarm_stock_num"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        goodsId("goods_id"),
        remark("remark"),
        saleStockNum("sale_stock_num"),
        skuId("sku_id"),
        freeze("freeze");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:goods_inventory*/
    public static final String TABLE_NAME = "goods_inventory";
    private static final long serialVersionUID = 1544994227L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="库存ID")
    private Long id;

    @TableField("alarm_stock_num")
    @DDLColumn(name="alarm_stock_num",comment="预警库存数量(低于这个值时报警，0不报警)")
    private Integer alarmStockNum;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("goods_id")
    @DDLColumn(name="goods_id",comment="会员ID")
    private Long goodsId;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;

    @TableField("sale_stock_num")
    @DDLColumn(name="sale_stock_num",comment="销售库存数量")
    private Integer saleStockNum;

    @TableField("sku_id")
    @DDLColumn(name="sku_id",comment="SKUID")
    private Long skuId;

    @TableField("freeze")
    @DDLColumn(name="freeze",comment="冻结数量")
    private Integer freeze;
    /** 库存ID*/
    public Long getId(){
        return id;
    }
    /** 库存ID*/
    public void setId(Long id){
        this.id =id;
    }
    /** 预警库存数量(低于这个值时报警，0不报警)*/
    public Integer getAlarmStockNum(){
        return alarmStockNum;
    }
    /** 预警库存数量(低于这个值时报警，0不报警)*/
    public void setAlarmStockNum(Integer alarmStockNum){
        this.alarmStockNum =alarmStockNum;
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
    /** 会员ID*/
    public Long getGoodsId(){
        return goodsId;
    }
    /** 会员ID*/
    public void setGoodsId(Long goodsId){
        this.goodsId =goodsId;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 销售库存数量*/
    public Integer getSaleStockNum(){
        return saleStockNum;
    }
    /** 销售库存数量*/
    public void setSaleStockNum(Integer saleStockNum){
        this.saleStockNum =saleStockNum;
    }
    /** SKUID*/
    public Long getSkuId(){
        return skuId;
    }
    /** SKUID*/
    public void setSkuId(Long skuId){
        this.skuId =skuId;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
    /** 冻结数量*/
    public Integer getFreeze(){
        return freeze;
    }
    /** 冻结数量*/
    public void setFreeze(Integer freeze){
        this.freeze =freeze;
    }
}