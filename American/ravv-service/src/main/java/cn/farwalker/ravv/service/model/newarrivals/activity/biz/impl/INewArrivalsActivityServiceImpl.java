package cn.farwalker.ravv.service.model.newarrivals.activity.biz.impl;

import cn.farwalker.ravv.service.model.newarrivals.activity.biz.INewArrivalsActivityBiz;
import cn.farwalker.ravv.service.model.newarrivals.activity.biz.INewArrivalsActivityService;
import cn.farwalker.ravv.service.model.newarrivals.activity.model.NewArrivalsActivityBo;
import cn.farwalker.ravv.service.model.newarrivals.goods.biz.INewArrivalsGoodsBiz;
import cn.farwalker.ravv.service.model.newarrivals.goods.model.NewArrivalsGoodsBo;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import com.baomidou.mybatisplus.mapper.Condition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class INewArrivalsActivityServiceImpl implements INewArrivalsActivityService {

    @Autowired
    private INewArrivalsActivityBiz activityBiz;



    @Override
    public List<NewArrivalsActivityBo> getActivity() {
        List<NewArrivalsActivityBo> allActivity = activityBiz.selectList(Condition.create()
                                    .isNotNull(NewArrivalsActivityBo.Key.imgUrl.toString())
                                    .orderBy(NewArrivalsActivityBo.Key.sequence.toString(), true));
        allActivity.forEach(s-> s.setImgUrl(QiniuUtil.getFullPath(s.getImgUrl())));
//        if(allActivity.size() == 0){
//            throw new WakaException("there are not any thing");
//        }
        return allActivity;
    }


}
