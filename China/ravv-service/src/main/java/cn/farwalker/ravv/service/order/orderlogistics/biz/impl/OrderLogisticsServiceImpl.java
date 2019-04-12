package cn.farwalker.ravv.service.order.orderlogistics.biz.impl;

import cn.farwalker.ravv.service.order.orderlogistics.biz.IOrderLogisticsBiz;
import cn.farwalker.ravv.service.order.orderlogistics.biz.IOrderLogisticsService;
import cn.farwalker.ravv.service.order.orderlogistics.biz.KdniaoTrackQueryAPI;
import cn.farwalker.ravv.service.order.orderlogistics.model.LogisticsTraceVo;
import cn.farwalker.ravv.service.order.orderlogistics.model.OrderLogisticsBo;
import cn.farwalker.ravv.service.shipment.biz.IShipmentBiz;
import cn.farwalker.ravv.service.shipment.model.ShipmentBo;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.Condition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class OrderLogisticsServiceImpl implements IOrderLogisticsService {

    @Autowired
    private IOrderLogisticsBiz orderLogisticsBiz;

    @Autowired
    private IShipmentBiz shipmentBiz;

    @Autowired
    private KdniaoTrackQueryAPI kdniaoTrackQueryAPI;

    @Override
    public List<LogisticsTraceVo> getLogisticsTrace(Long orderId) throws Exception {
        List<LogisticsTraceVo> result = new ArrayList<>();
        //根据orderId查出订单号，物流模板
        OrderLogisticsBo orderLogisticsBo = orderLogisticsBiz.selectOne(Condition.create()
                                                .eq(OrderLogisticsBo.Key.orderId.toString(), orderId));
        if(orderLogisticsBo == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        ShipmentBo shipmentBo = shipmentBiz.selectById(orderLogisticsBo.getShipmentId());
        String trace = kdniaoTrackQueryAPI.getOrderTracesByJson(shipmentBo.getExpCode(), orderLogisticsBo.getLogisticsNo());
        JSONObject traceJson = JSONObject.parseObject(trace);
        boolean successFlag = traceJson.getBoolean("Success");
        if(successFlag){
            JSONArray jsonArray = traceJson.getJSONArray("Traces");
            if(jsonArray != null && jsonArray.size() > 0){
                for(int i = 0; i < jsonArray.size(); i++){
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    LogisticsTraceVo logisticsTraceVo = new LogisticsTraceVo();
                    logisticsTraceVo.setAcceptStation(jsonObject.getString("AcceptStation"));
                    logisticsTraceVo.setAcceptTime(jsonObject.getString("AcceptTime"));
                    logisticsTraceVo.setRemark(jsonObject.getString("Remark"));
                    result.add(logisticsTraceVo);
                }
            }
        } else{
            throw new WakaException("物流查询失败");
        }
        Collections.reverse(result);
        return result;
    }

    @Override
    public List<LogisticsTraceVo> test(String expCode, String expNo) throws Exception {
        List<LogisticsTraceVo> result = new ArrayList<>();
        log.info("test");
        String trace = kdniaoTrackQueryAPI.getOrderTracesByJson(expCode, expNo);
        JSONObject traceJson = JSONObject.parseObject(trace);
        boolean successFlag = traceJson.getBoolean("Success");
        if(successFlag){
            JSONArray jsonArray = traceJson.getJSONArray("Traces");
            if(jsonArray != null && jsonArray.size() > 0){
                for(int i = 0; i < jsonArray.size(); i++){
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    LogisticsTraceVo logisticsTraceVo = new LogisticsTraceVo();
                    logisticsTraceVo.setAcceptStation(jsonObject.getString("AcceptStation"));
                    logisticsTraceVo.setAcceptTime(jsonObject.getString("AcceptTime"));
                    logisticsTraceVo.setRemark(jsonObject.getString("Remark"));
                    result.add(logisticsTraceVo);
                }
            }
        } else{
            throw new WakaException("物流查询失败");
        }
        Collections.reverse(result);
        log.info("==============================={}", result);
        return result;
    }
}
