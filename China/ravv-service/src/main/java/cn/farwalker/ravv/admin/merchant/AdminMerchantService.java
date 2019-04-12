package cn.farwalker.ravv.admin.merchant;

import cn.farwalker.ravv.service.merchant.model.MerchantBo;
import cn.farwalker.ravv.service.merchant.model.MerchantOrderVo;
import cn.farwalker.ravv.service.merchant.model.MerchantSalesVo;
import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.web.crud.QueryFilter;

import java.util.Date;
import java.util.List;

public interface AdminMerchantService {
    Boolean create(MerchantBo vo);

    Boolean delete(Long id);

    MerchantBo getOne(Long id);

    Page<MerchantBo> getList(List<QueryFilter> query, Integer start, Integer size,
                                   String sortfield);

    Boolean update(MerchantBo vo);

    MerchantOrderVo getOrderStats(Long merchantId, Date startdate, Date enddate);

    MerchantSalesVo getSalesStatistics(Long merchantId, Date startdate, Date enddate);
}
