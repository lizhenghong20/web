package cn.farwalker.ravv.service.quartz;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class UpdateOrderStatusTaskJob extends QuartzJobBean {

    @Autowired
    private UpdateOrderWihthoutReturns updateOrderWihthoutReturns;

    private void updateOrderClosed(Long orderId){
        updateOrderWihthoutReturns.updateOrderStatus(orderId);
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap paramMap = jobExecutionContext.getJobDetail().getJobDataMap();
        Long orderId = paramMap.getLongValue("orderId");
        try {
            updateOrderClosed(orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
