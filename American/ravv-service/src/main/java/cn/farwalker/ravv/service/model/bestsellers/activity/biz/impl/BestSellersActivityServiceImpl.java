package cn.farwalker.ravv.service.model.bestsellers.activity.biz.impl;

import cn.farwalker.ravv.service.model.bestsellers.activity.biz.IBestSellersActivityBiz;
import cn.farwalker.ravv.service.model.bestsellers.activity.biz.IBestSellersActivityService;
import cn.farwalker.ravv.service.model.bestsellers.activity.model.BestSellersActivityBo;
import cn.farwalker.ravv.service.model.newarrivals.activity.model.NewArrivalsActivityBo;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import com.baomidou.mybatisplus.mapper.Condition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BestSellersActivityServiceImpl implements IBestSellersActivityService {

    @Autowired
    private IBestSellersActivityBiz activityBiz;

    @Override
    public List<BestSellersActivityBo> getActivity() {
        List<BestSellersActivityBo> allActivity = activityBiz.selectList(Condition.create()
                .isNotNull(BestSellersActivityBo.Key.imgUrl.toString())
                .orderBy(BestSellersActivityBo.Key.sequence.toString(), true));
        allActivity.forEach(s-> s.setImgUrl(QiniuUtil.getFullPath(s.getImgUrl())));

//        if(allActivity.size() == 0){
//            throw new WakaException("there are not any thing");
//        }
        return allActivity;
    }

    
}
