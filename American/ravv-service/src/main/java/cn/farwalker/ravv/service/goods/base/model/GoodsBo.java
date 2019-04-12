package cn.farwalker.ravv.service.goods.base.model;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cn.farwalker.ravv.service.goods.constants.GoodsStatusEnum;
import cn.farwalker.ravv.service.goods.constants.SupportTradeEnum;
import cn.farwalker.ravv.service.merchant.model.MerchantBo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import com.cangwu.frame.orm.core.BaseBo;
import com.cangwu.frame.orm.core.IFieldKey;
import com.cangwu.frame.orm.core.annotation.LoadJoinValue;
import com.cangwu.frame.orm.ddl.annotation.DDLColumn;
import com.cangwu.frame.orm.ddl.annotation.DDLTable;
import com.cangwu.frame.orm.ddl.annotation.NotColumn;

/**
 * 商品基础信息表
 * 
 * @author generateModel.java
 */
@TableName(GoodsBo.TABLE_NAME)
@DDLTable(name=GoodsBo.TABLE_NAME,comment="商品基础信息表")
public class GoodsBo extends Model<GoodsBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        brandId("brand_id"),
        brandName("brand_name"),
        categoryPath("category_path"),
        customProps("custom_props"),
        favoriteCount("favorite_count"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        goodsCode("goods_code"),
        
        goodsModelNum("goods_model_num"),
        goodsName("goods_name"),
        goodsNotes("goods_notes"),
        goodsPoint("goods_point"),
        goodsSellPoints("goods_sell_points"),
        goodsStatus("goods_status"),
        leafCategoryId("leaf_category_id"),
        leafCategoryName("leaf_category_name"),
        logisticsPoint("logistics_point"),
        point("point"),
        
        postId("post_id"),
        props("props"),
        relaGoodsId("rela_goods_id"),
        remark("remark"),
        saleCount("sale_count"),
        servicePoint("service_point"),
        supportRetDay("support_ret_day"),
        supportTrade("support_trade"),
        merchantId("merchant_id"),
        keyword("keyword"),
        
        viewCount("view_count"),
        storehouseId("storehouse_id"),
        origPrice("orig_price"),
        products("products");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:goods*/
    public static final String TABLE_NAME = "goods";
    private static final long serialVersionUID = 1871690275L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="商品id主键")
    private Long id;

    @TableField("brand_id")
    @DDLColumn(name="brand_id",comment="商品品牌ID")
    private Long brandId;

    @TableField("brand_name")
    @DDLColumn(name="brand_name",comment="商品品牌名称")
    private String brandName;

    @TableField("category_path")
    @DDLColumn(name="category_path",comment="类目路径",length=127)
    private String categoryPath;

    @TableField("custom_props")
    @DDLColumn(name="custom_props",comment="自定义属性",length=2000)
    private String customProps;

    @TableField("favorite_count")
    @DDLColumn(name="favorite_count",comment="被收藏数量")
    private Integer favoriteCount;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("goods_code")
    @DDLColumn(name="goods_code",comment="商品编码")
    private String goodsCode;

    

    @TableField("goods_model_num")
    @DDLColumn(name="goods_model_num",comment="商品货号(废)",length=127)
    private String goodsModelNum;

    @TableField("goods_name")
    @DDLColumn(name="goods_name",comment="商品名称",length=255)
    private String goodsName;

    @TableField("goods_notes")
    @DDLColumn(name="goods_notes",comment="注意事项",length=255)
    private String goodsNotes;

    @TableField("goods_point")
    @DDLColumn(name="goods_point",comment="商品评价分数")
    private BigDecimal goodsPoint;

    @TableField("goods_sell_points")
    @DDLColumn(name="goods_sell_points",comment="卖点描述",length=1024)
    private String goodsSellPoints;

    @TableField("goods_status")
    @DDLColumn(name="goods_status",comment="商品状态")
    //private String goodsStatus;
    private GoodsStatusEnum goodsStatus;

    @TableField("leaf_category_id")
    @DDLColumn(name="leaf_category_id",comment="叶子类目ID")
    private Long leafCategoryId;

    @TableField("leaf_category_name")
    @DDLColumn(name="leaf_category_name",comment="叶子类目名称")
    private String leafCategoryName;

    @TableField("logistics_point")
    @DDLColumn(name="logistics_point",comment="物流评价分数")
    private BigDecimal logisticsPoint;


    @TableField("point")
    @DDLColumn(name="point",comment="商品总分")
    private BigDecimal point;

    @TableField("post_id")
    @DDLColumn(name="post_id",comment="运费模板ID")
    private Long postId;

    @TableField("props")
    @DDLColumn(name="props",comment="普通属性",length=2000)
    private String props;

    @TableField("rela_goods_id")
    @DDLColumn(name="rela_goods_id",comment="关联商品ID")
    private Long relaGoodsId;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=1024)
    private String remark;

    @TableField("sale_count")
    @DDLColumn(name="sale_count",comment="销售数量")
    private Integer saleCount;

    @TableField("service_point")
    @DDLColumn(name="service_point",comment="服务评价分数")
    private BigDecimal servicePoint;

    @TableField("support_ret_day")
    @DDLColumn(name="support_ret_day",comment="无理由退换货天数")
    private Integer supportRetDay;

    @TableField("support_trade")
    @DDLColumn(name="support_trade",comment="支持的交易模式")
    private SupportTradeEnum supportTrade;

    @TableField("merchant_id")
    @DDLColumn(name="merchant_id",comment="供应商ID")
    private Long merchantId;
    
    @TableField(exist=false)
    @NotColumn
    private String merchantName;
    
    @TableField("keyword")
    @DDLColumn(name="keyword",comment="关键字")
    private String keyword;
/*
    @TableField("goods_desc_url")
    @DDLColumn(name="goods_desc_url",comment="商品描述路径")
    private String goodsDescUrl;*/

    @TableField("view_count")
    @DDLColumn(name="view_count",comment="查看次数")
    private Integer viewCount;

    @TableField("storehouse_id")
    @DDLColumn(name="storehouse_id",comment="商品归属的仓库id")
    private Long storehouseId;

    @TableField("orig_price")
    @DDLColumn(name="orig_price",comment="原价(显示用,不是销售价)")
    private BigDecimal origPrice;

    @TableField("products")
    @DDLColumn(name="products",comment="产品参数(json格式)",length=2000)
    private String products;
    
    //private String supportTrade;
    /** 商品id主键*/
    public Long getId(){
        return id;
    }
    /** 商品id主键*/
    public void setId(Long id){
        this.id =id;
    }
    /** 商品品牌ID*/
    public Long getBrandId(){
        return brandId;
    }
    /** 商品品牌ID*/
    public void setBrandId(Long brandId){
        this.brandId =brandId;
    }
    /** 商品品牌名称*/
    public String getBrandName(){
        return brandName;
    }
    /** 商品品牌名称*/
    public void setBrandName(String brandName){
        this.brandName =brandName;
    }
    /** 类目路径*/
    public String getCategoryPath(){
        return categoryPath;
    }
    /** 类目路径*/
    public void setCategoryPath(String categoryPath){
        this.categoryPath =categoryPath;
    }
    /** 自定义属性*/
    public String getCustomProps(){
        return customProps;
    }
    /** 自定义属性*/
    public void setCustomProps(String customProps){
        this.customProps =customProps;
    }
    /** 被收藏数量*/
    public Integer getFavoriteCount(){
        return favoriteCount;
    }
    /** 被收藏数量*/
    public void setFavoriteCount(Integer favoriteCount){
        this.favoriteCount =favoriteCount;
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
    /** 商品编码*/
    public String getGoodsCode(){
        return goodsCode;
    }
    /** 商品编码*/
    public void setGoodsCode(String goodsCode){
        this.goodsCode =goodsCode;
    }

    /** 商品货号(废)*/
    public String getGoodsModelNum(){
        return goodsModelNum;
    }
    /** 商品货号(废)*/
    public void setGoodsModelNum(String goodsModelNum){
        this.goodsModelNum =goodsModelNum;
    }
    /** 商品名称*/
    public String getGoodsName(){
        return goodsName;
    }
    /** 商品名称*/
    public void setGoodsName(String goodsName){
        this.goodsName =goodsName;
    }
    /** 注意事项*/
    public String getGoodsNotes(){
        return goodsNotes;
    }
    /** 注意事项*/
    public void setGoodsNotes(String goodsNotes){
        this.goodsNotes =goodsNotes;
    }
    /** 商品评价分数*/
    public BigDecimal getGoodsPoint(){
        return goodsPoint;
    }
    /** 商品评价分数*/
    public void setGoodsPoint(BigDecimal goodsPoint){
        this.goodsPoint =goodsPoint;
    }
    /** 卖点描述*/
    public String getGoodsSellPoints(){
        return goodsSellPoints;
    }
    /** 卖点描述*/
    public void setGoodsSellPoints(String goodsSellPoints){
        this.goodsSellPoints =goodsSellPoints;
    }
    /** 商品状态*/
    public GoodsStatusEnum getGoodsStatus(){
        return goodsStatus;
    }
    /** 商品状态*/
    public void setGoodsStatus(GoodsStatusEnum goodsStatus){
        this.goodsStatus =goodsStatus;
    }
    /** 叶子类目ID*/
    public Long getLeafCategoryId(){
        return leafCategoryId;
    }
    /** 叶子类目ID*/
    public void setLeafCategoryId(Long leafCategoryId){
        this.leafCategoryId =leafCategoryId;
    }
    /** 叶子类目名称*/
    public String getLeafCategoryName(){
        return leafCategoryName;
    }
    /** 叶子类目名称*/
    public void setLeafCategoryName(String leafCategoryName){
        this.leafCategoryName =leafCategoryName;
    }
    /** 物流评价分数*/
    public BigDecimal getLogisticsPoint(){
        return logisticsPoint;
    }
    /** 物流评价分数*/
    public void setLogisticsPoint(BigDecimal logisticsPoint){
        this.logisticsPoint =logisticsPoint;
    }

    /** 商品总分*/
    public BigDecimal getPoint(){
        return point;
    }
    /** 商品总分*/
    public void setPoint(BigDecimal point){
        this.point =point;
    }
    /** 运费模板ID*/
    public Long getPostId(){
        return postId;
    }
    /** 运费模板ID*/
    public void setPostId(Long postId){
        this.postId =postId;
    }
    /** 普通属性*/
    public String getProps(){
        return props;
    }
    /** 普通属性*/
    public void setProps(String props){
        this.props =props;
    }
    /** 关联商品ID*/
    public Long getRelaGoodsId(){
        return relaGoodsId;
    }
    /** 关联商品ID*/
    public void setRelaGoodsId(Long relaGoodsId){
        this.relaGoodsId =relaGoodsId;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    /** 销售数量*/
    public Integer getSaleCount(){
        return saleCount;
    }
    /** 销售数量*/
    public void setSaleCount(Integer saleCount){
        this.saleCount =saleCount;
    }
    /** 服务评价分数*/
    public BigDecimal getServicePoint(){
        return servicePoint;
    }
    /** 服务评价分数*/
    public void setServicePoint(BigDecimal servicePoint){
        this.servicePoint =servicePoint;
    }
    /** 无理由退换货天数*/
    public Integer getSupportRetDay(){
        return supportRetDay;
    }
    /** 无理由退换货天数*/
    public void setSupportRetDay(Integer supportRetDay){
        this.supportRetDay =supportRetDay;
    }
    /** 支持的交易模式*/
    public SupportTradeEnum getSupportTrade(){
        return supportTrade;
    }
    /** 支持的交易模式*/
    public void setSupportTrade(SupportTradeEnum supportTrade){
        this.supportTrade =supportTrade;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
    /** 供应商ID*/
    public Long getMerchantId(){
        return merchantId;
    }
    /** 供应商ID*/
    public void setMerchantId(Long merchantId){
        this.merchantId =merchantId;
    }
    /** 关键字*/
    public String getKeyword(){
        return keyword;
    }
    /** 关键字*/
    public void setKeyword(String keyword){
        this.keyword =keyword;
    }
    /** 商品描述路径
    public String getGoodsDescUrl(){
        return goodsDescUrl;
    }
     商品描述路径
    public void setGoodsDescUrl(String goodsDescUrl){
        this.goodsDescUrl =goodsDescUrl;
    }*/
    /** 查看次数*/
    public Integer getViewCount(){
        return viewCount;
    }
    /** 查看次数*/
    public void setViewCount(Integer viewCount){
        this.viewCount =viewCount;
    }
    /** 商品归属的仓库id*/
    public Long getStorehouseId(){
        return storehouseId;
    }
    /** 商品归属的仓库id*/
    public void setStorehouseId(Long storehouseId){
        this.storehouseId =storehouseId;
    }
    /** 原价(显示用,不是销售价)*/
    public BigDecimal getOrigPrice(){
        return origPrice;
    }
    /** 原价(显示用,不是销售价)*/
    public void setOrigPrice(BigDecimal origPrice){
        this.origPrice =origPrice;
    }
    /** 产品参数(json格式)*/
    public String getProducts(){
        return products;
    }
    /** 产品参数(json格式)*/
    public void setProducts(String products){
        this.products =products;
    }
    /**供应商名称*/
	public String getMerchantName() {
		return merchantName;
	}
    /**供应商名称*/
	@LoadJoinValue(by="merchantId",table=MerchantBo.TABLE_NAME,joinfield="id",returnfield="name")
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
}