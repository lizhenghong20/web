package cn.farwalker.ravv.service.web.slider.biz;

import cn.farwalker.ravv.service.web.slider.model.WebSliderVo;

import java.util.List;

/**
 * Created by asus on 2018/11/20.
 */
public interface IWebSliderService {
    public List<WebSliderVo> getSliderImage(String pageName);
}
