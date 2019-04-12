package cn.farwalker.ravv.service.quartz;

import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInventoryService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UpdateOrderUnfreezeTaskJob extends QuartzJobBean {

    @Autowired
    private IOrderInventoryService orderInfoService;

    private void updateOrderUnfreeze(Long orderId){
        log.info("======================================执行取消订单任务:{}", orderId);
        orderInfoService.updateOrderUnfreeze(orderId);
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap paramMap = context.getJobDetail().getJobDataMap();
        Long orderId = paramMap.getLongValue("orderId");
        try {
            updateOrderUnfreeze(orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
