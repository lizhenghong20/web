package cn.farwalker.ravv.service.model.VO;

import cn.farwalker.ravv.service.goods.base.model.GoodsDetailsVo;
import cn.farwalker.ravv.service.model.bestsellers.activity.model.BestSellersActivityBo;
import cn.farwalker.ravv.service.model.newarrivals.activity.model.NewArrivalsActivityBo;
import lombok.Data;

import java.util.List;

@Data
public class HomeModelVo {

    List<GoodsDetailsVo> newArrivalsGoodsList;
    List<NewArrivalsActivityBo> newArrivalsActivityList;
    List<GoodsDetailsVo> bestSellersGoodsList;
    List<BestSellersActivityBo> bestSellersActivityList;
}
