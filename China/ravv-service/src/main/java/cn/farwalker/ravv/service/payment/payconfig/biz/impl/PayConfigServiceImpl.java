package cn.farwalker.ravv.service.payment.payconfig.biz.impl;

import cn.farwalker.ravv.common.constants.WechatLoginTypeEnum;
import cn.farwalker.ravv.service.payment.payconfig.biz.IPayConfigBiz;
import cn.farwalker.ravv.service.payment.payconfig.biz.IPayConfigService;
import cn.farwalker.ravv.service.payment.payconfig.model.PayConfigBo;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PayConfigServiceImpl implements IPayConfigService {

    @Autowired
    private IPayConfigBiz payConfigBiz;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String addPayConfig() {
        List<PayConfigBo> configBoList = new ArrayList<>();
        PayConfigBo appPay = new PayConfigBo();
        appPay.setApiKey("shoplive2019041771409102evilpohs");
        appPay.setAppId("wx474bf03c942287a7");
        appPay.setAppSecret("2e971612c9223c421be272c336967afd");
        appPay.setGmtCreate(new Date());
        appPay.setGmtModified(new Date());
        appPay.setMchId("1532204281");
        appPay.setPayType(WechatLoginTypeEnum.APP.toString());
        configBoList.add(appPay);

        PayConfigBo webPay = new PayConfigBo();
        webPay.setApiKey("shoplive2019041771409102evilpohs");
        webPay.setAppId("wxd6edafc887910c4d");
        webPay.setAppSecret("8020186adf819ca3b8afe0e6a08e67b3");
        webPay.setGmtCreate(new Date());
        webPay.setGmtModified(new Date());
        webPay.setMchId("1532204281");
        webPay.setPayType(WechatLoginTypeEnum.WEB.toString());
        configBoList.add(webPay);

        if(!payConfigBiz.insertBatch(configBoList)){
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }
        return "success";
    }
}
