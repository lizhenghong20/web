package cn.farwalker.ravv.service.web.menu.model;

import cn.farwalker.ravv.service.goods.base.model.GoodsListVo;
import lombok.Data;

import java.util.List;

@Data
public class WebMenuFrontVo extends WebMenuBo{
    WebMenuBo secondWebMenuBo;
    List<WebMenuBo> LowLevelMenu;
    List<GoodsListVo> goodsListVos;

}
