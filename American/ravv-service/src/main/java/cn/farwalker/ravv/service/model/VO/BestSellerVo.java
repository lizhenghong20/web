package cn.farwalker.ravv.service.model.VO;

import cn.farwalker.ravv.service.model.bestsellers.activity.model.BestSellersActivityBo;
import cn.farwalker.ravv.service.model.bestsellers.goods.model.BestSellersGoodsBo;
import lombok.Data;

import java.util.List;

@Data
public class BestSellerVo {
    List<BestSellersGoodsBo> sellersGoodsList;
    List<BestSellersActivityBo> sellersActivityList;
}
