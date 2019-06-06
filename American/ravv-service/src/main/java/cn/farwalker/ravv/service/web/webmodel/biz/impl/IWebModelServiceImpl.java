package cn.farwalker.ravv.service.web.webmodel.biz.impl;

import cn.farwalker.ravv.service.web.webmodel.biz.IWebModelBiz;
import cn.farwalker.ravv.service.web.webmodel.biz.IWebModelService;
import cn.farwalker.ravv.service.web.webmodel.model.WebModelBo;
import com.baomidou.mybatisplus.mapper.Condition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class IWebModelServiceImpl implements IWebModelService {

    @Autowired
    private IWebModelBiz webModelBiz;

    @Override
    public List<WebModelBo> getAllModel() {
        List<WebModelBo> allModel = webModelBiz.selectList(Condition.create()
                                    .orderBy(WebModelBo.Key.sequence.toString(), true));
        return allModel;
    }
}
