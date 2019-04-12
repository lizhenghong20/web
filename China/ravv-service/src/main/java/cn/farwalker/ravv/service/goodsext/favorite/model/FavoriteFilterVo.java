package cn.farwalker.ravv.service.goodsext.favorite.model;

import lombok.Data;

import java.util.List;

@Data
public class FavoriteFilterVo {

    private String filterName;
    private int count;
    //可变参数（可以是一级菜单id，也可以是商品id，目前是一级菜单id）
    private Long id;
}
