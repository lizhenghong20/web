package cn.farwalker.ravv.service.goods.base.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by asus on 2018/11/23.
 */
@Data
public class GoodsListVo extends GoodsBo {
    //菜单项图片1
    String picture1;
    //菜单项图片2
    String picture2;
    //商品图片,取主图
    String imgUrl;
    //商品价格.最低价
    BigDecimal lowestPrice;
    //商品菜单路径
    String menuPath;
    //商品菜单id
    Long menuId;
    //菜单名字
    String title;
}
