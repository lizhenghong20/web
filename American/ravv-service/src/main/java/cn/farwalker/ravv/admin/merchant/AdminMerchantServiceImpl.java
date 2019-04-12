package cn.farwalker.ravv.admin.merchant;


import cn.farwalker.ravv.service.merchant.biz.IMerchantBiz;
import cn.farwalker.ravv.service.merchant.biz.IMerchantService;
import cn.farwalker.ravv.service.merchant.model.MerchantBo;
import cn.farwalker.ravv.service.merchant.model.MerchantOrderVo;
import cn.farwalker.ravv.service.merchant.model.MerchantSalesVo;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.core.base.controller.ControllerUtils;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.orm.core.annotation.LoadJoinValueImpl;
import com.cangwu.frame.web.crud.QueryFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class AdminMerchantServiceImpl implements AdminMerchantService {
    @Resource
    private IMerchantBiz merchantBiz;

    @Resource
    private IMerchantService merchantService;


    protected IMerchantBiz getBiz() {
        return merchantBiz;
    }


    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean create(MerchantBo vo) {
        vo.setLogo(QiniuUtil.getRelativePath(vo.getLogo()));
        vo.setLicense(QiniuUtil.getRelativePath(vo.getLicense()));
        Boolean rs = getBiz().insert(vo);
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        Boolean rs = getBiz().deleteById(id);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.DELETE_ERROR);
        }
        return rs;
    }

    @Override
    public MerchantBo getOne(Long id) {
        MerchantBo rs = getBiz().selectById(id);
        rs.setLogo(QiniuUtil.getFullPath(rs.getLogo()));
        rs.setLicense(QiniuUtil.getFullPath(rs.getLicense()));
        return rs;
    }

    @Override
    public Page<MerchantBo> getList(List<QueryFilter> query, Integer start, Integer size, String sortfield) {
        // createMethodSinge创建方法
        Page<MerchantBo> page = ControllerUtils.getPage(start, size, sortfield);
        Wrapper<MerchantBo> wrap = ControllerUtils.getWrapper(query);
        Page<MerchantBo> rs = getBiz().selectPage(page, wrap);
        List<MerchantBo> merchantList = rs.getRecords();
        Integer length = merchantList.size();
        for (Integer i = 0; i < length; i++) {
            MerchantBo vo = merchantList.get(i);
            vo.setLogo(QiniuUtil.getFullPath(vo.getLogo()));
            vo.setLicense(QiniuUtil.getFullPath(vo.getLicense()));
        }
        return rs;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Boolean update(MerchantBo vo) {
        vo.setLogo(QiniuUtil.getRelativePath(vo.getLogo()));
        vo.setLicense(QiniuUtil.getRelativePath(vo.getLicense()));
        Boolean rs = getBiz().updateById(vo);
        if(!rs){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return rs;
    }

    @Override
    public MerchantOrderVo getOrderStats(Long merchantId, Date startdate, Date enddate) {
        Calendar today = Calendar.getInstance();
        if(enddate == null){
            enddate = today.getTime();
        }
        else{
            today.setTime(enddate);
        }
        if(startdate == null){//半年数据
            today.add(Calendar.MONTH, -6);
            startdate = today.getTime();
        }

        List<Long> merchantIds = Collections.singletonList(merchantId);
        List<MerchantOrderVo> rds = merchantService.getMerchantOrderStats(merchantIds, startdate, enddate);//getBiz().selectById(merchantId);
        if(Tools.collection.isEmpty(rds)){
            MerchantOrderVo vo = new MerchantOrderVo();
            vo.setMerchantId(merchantId);
            vo.setStartdate(startdate);
            vo.setEnddate(enddate);
            LoadJoinValueImpl.load(merchantBiz, vo);
            return vo;
        }
        else{
            LoadJoinValueImpl.load(merchantBiz, rds);
            return rds.get(0);
        }
    }

    @Override
    public MerchantSalesVo getSalesStatistics(Long merchantId, Date startdate, Date enddate) {
        Calendar today = Calendar.getInstance();
        if(enddate == null){
            enddate = today.getTime();
        }
        else{
            today.setTime(enddate);
        }
        if(startdate == null){//半年数据
            today.add(Calendar.MONTH, -6);
            startdate = today.getTime();
        }

        MerchantSalesVo salesVo = merchantService.getSalesStatistics(merchantId, startdate, enddate);

        return salesVo;
    }
}
