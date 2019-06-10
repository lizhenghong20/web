package cn.farwalker.ravv.service.model.VO;

import cn.farwalker.ravv.service.model.newarrivals.activity.model.NewArrivalsActivityBo;
import cn.farwalker.ravv.service.model.newarrivals.goods.model.NewArrivalsGoodsBo;
import lombok.Data;

import java.util.List;

@Data
public class NewArrivalsVo {
    List<NewArrivalsGoodsBo> sellersGoodsList;
    List<NewArrivalsActivityBo> sellersActivityList;
}
