package cn.farwalker.ravv.service.web.slider.biz.impl;
import java.util.ArrayList;
import java.util.List;

import cn.farwalker.waka.core.WakaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

import cn.farwalker.ravv.service.web.slider.biz.IWebSliderBiz;
import cn.farwalker.ravv.service.web.slider.biz.IWebSliderService;
import cn.farwalker.ravv.service.web.slider.model.WebSliderBo;
import cn.farwalker.ravv.service.web.slider.model.WebSliderVo;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;

/**
 * Created by asus on 2018/11/20.
 */
@Service
public class WebSliderServiceImpl implements IWebSliderService {
    @Autowired
    private IWebSliderBiz iWebSliderBiz;

    public List<WebSliderVo> getSliderImage(String pageName){
        Wrapper<WebSliderBo> wrapper = new EntityWrapper<>();
        wrapper.eq(WebSliderBo.Key.status.toString(),true);
        wrapper.eq(WebSliderBo.Key.pageName.toString(),pageName);
        List<String> list  = new ArrayList<>();
        list.add(WebSliderBo.Key.sequence.toString());
        wrapper.orderDesc(list);
        List<WebSliderBo> boList = iWebSliderBiz.selectList(wrapper);
        if(boList == null)
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);

        List<WebSliderVo> voList = new ArrayList<>();
        for(WebSliderBo item : boList){
            WebSliderVo  newVo = new WebSliderVo();
            newVo.setPicture(QiniuUtil.getFullPath(item.getPicture()));
            Tools.bean.copyProperties(item,newVo);
            voList.add(newVo);
        }
        return voList;
    }


}
