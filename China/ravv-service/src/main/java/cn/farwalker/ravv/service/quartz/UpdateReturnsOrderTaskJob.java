package cn.farwalker.ravv.service.quartz;

import cn.farwalker.ravv.service.order.operationlog.biz.IOrderLogService;
import cn.farwalker.ravv.service.order.ordergoods.biz.IOrderGoodsService;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoBiz;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderStatusEnum;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnLogService;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsBiz;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsDetailBiz;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsGoodsStatusEnum;
import cn.farwalker.ravv.service.order.returns.constants.ReturnsTypeEnum;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsBo;
import cn.farwalker.ravv.service.sale.profitallot.biz.ISaleProfitAllotBiz;
import cn.farwalker.waka.util.Tools;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class UpdateReturnsOrderTaskJob extends QuartzJobBean {

    @Autowired
    private IOrderReturnLogService orderReturnLogService;

    @Autowired
    private IOrderReturnsBiz returnsBiz;

    private static final String OperatorText = "系统自动关闭";

    //这里只执行换货逻辑
    private void updateReturnsOrderStatus(Long returnsId){
        OrderReturnsBo bo = returnsBiz.selectById(returnsId);
        ReturnsGoodsStatusEnum status = bo.getStatus();
        if (status == ReturnsGoodsStatusEnum.buyerWaitReceived) {// 待买家收货
            bo.setStatus(ReturnsGoodsStatusEnum.finish);
            bo.setFinishTime(new Date());
            // 添加对应日志
            orderReturnLogService.createLog(bo.getOrderId(), null, "system",
                    ReturnsGoodsStatusEnum.buyerWaitReceived.getLabel(), "换货成功", true, bo.getId(),
                    ReturnsGoodsStatusEnum.buyerWaitReceived, "");
            orderReturnLogService.createLog(bo.getOrderId(), null, "system", ReturnsGoodsStatusEnum.finish.getLabel(),
                    "交易完成", true, bo.getId(), ReturnsGoodsStatusEnum.finish, "");
            returnsBiz.updateById(bo);

        }
        else if(status == ReturnsGoodsStatusEnum.finish) {
            log.info("退货已完成");
        }

    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap paramMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String stringreturns = paramMap.getString("returnsId");
        Long returnsId = Long.valueOf(stringreturns);
        try {
            updateReturnsOrderStatus(returnsId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
