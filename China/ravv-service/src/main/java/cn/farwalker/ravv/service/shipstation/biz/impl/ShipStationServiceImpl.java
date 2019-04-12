package cn.farwalker.ravv.service.shipstation.biz.impl;

import cn.farwalker.ravv.service.base.area.biz.IBaseAreaBiz;
import cn.farwalker.ravv.service.base.area.model.BaseAreaBo;
import cn.farwalker.ravv.service.member.basememeber.biz.IMemberBiz;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.order.ordergoods.biz.IOrderGoodsBiz;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoBiz;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.order.orderlogistics.biz.IOrderLogisticsBiz;
import cn.farwalker.ravv.service.order.orderlogistics.model.OrderLogisticsBo;
import cn.farwalker.ravv.service.shipment.biz.IShipmentBiz;
import cn.farwalker.ravv.service.shipment.model.ShipmentBo;
import cn.farwalker.ravv.service.shipstation.biz.IShipStationService;
import cn.farwalker.ravv.service.shipstation.constants.ShipStationOrderStatusEnum;
import cn.farwalker.ravv.service.shipstation.model.*;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.Condition;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ShipStationServiceImpl implements IShipStationService {

    @Autowired
    private IOrderInfoBiz iOrderInfoBiz;

    @Autowired
    private IOrderGoodsBiz iOrderGoodsBiz;

    @Autowired
    private IMemberBiz iMemberBiz;

    @Autowired
    private IBaseAreaBiz iBaseAreaBiz;

    @Autowired
    private IOrderLogisticsBiz iOrderLogisticsBiz;

    @Autowired
    private IShipmentBiz iShipmentBiz;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private final static String authorization = "Basic OTlmZmZkYTM0ODlhNDBjMjgzMjRhYTFjZWJkZDFkZmI6OTk0NDRhN2Q5MjcwNDc5ZmFmN2Y0NjFiNDQ5ODE2ZTQ=";

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean addShipStationOrder(Long orderId) {
        //因为物流模板还未上线，目前先屏蔽此处代码
//        ShipmentBo shipmentBo = getShipment(orderId);
        //根据orderId查出有无子单，memberId，收货地址
        List<OrderInfoBo> orderList = iOrderInfoBiz.selectList(Condition.create()
                                        .eq(OrderInfoBo.Key.pid.toString(), orderId));
        if(orderList.size() == 0){
            //这里处理未分单的情况
            return updateAndCreateOrder(orderId /**, shipmentBo*/);
        }
        else {
            return updateAndCreateMultipleOrder(orderList /**, shipmentBo*/);
        }
    }

    @Override
    public boolean orderShipped(Long orderId) {
        ShippedOrder shippedOrder = new ShippedOrder();
        //根据orderId查出shipstationid,运单id
        OrderInfoBo orderInfoBo = iOrderInfoBiz.selectById(orderId);
        if(orderInfoBo == null){
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR + "this order is not exist!");
        }
        int shipstationOrderId = orderInfoBo.getShipstationOrderId();
        //根据orderId查出物流信息
        OrderLogisticsBo orderLogisticsBo = iOrderLogisticsBiz.selectOne(Condition.create()
                                                                .eq(OrderLogisticsBo.Key.orderId.toString(), orderId));
        if(orderLogisticsBo == null)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        String trackingNumber = orderLogisticsBo.getLogisticsNo();
        ShipmentBo shipmentBo = iShipmentBiz.selectById(orderLogisticsBo.getShipmentId());
        if(shipmentBo == null){
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR + "shipment is not exist!");
        }
        String carrierCode = shipmentBo.getCarrierCode();
        shippedOrder.setOrderId(shipstationOrderId);
        shippedOrder.setCarrierCode(carrierCode);
        shippedOrder.setTrackingNumber(trackingNumber);
        shippedOrder.setShipDate(sdf.format(new Date()));
        shippedOrder.setNotifyCustomer(true);

        return shipped(shippedOrder);
    }

    @Override
    public boolean cancelOrder(Long orderId) {

        return cancel(orderId);
    }


    @Override
    public boolean listCarriers() {
        Client client = ClientBuilder.newClient();
        Response response = client.target("https://ssapi.shipstation.com/carriers")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", authorization)
                .get();
        log.info("======================response.getStatus:{}", response.getStatus());
        log.info("======================response.getHeaders:{}", response.getHeaders());
        log.info("======================response.body:{}", response.readEntity(String.class));
        if(response.getStatus() != 200)
            return false;
        else
            return true;
    }

    @Override
    public boolean listService() {
        Client client = ClientBuilder.newClient();
        Response response = client.target("https://ssapi.shipstation.com/carriers/listservices?carrierCode=ups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", authorization)
                .get();
        log.info("======================response.getStatus:{}", response.getStatus());
        log.info("======================response.getHeaders:{}", response.getHeaders());
        log.info("======================response.body:{}", response.readEntity(String.class));
        if(response.getStatus() != 200)
            return false;
        else
            return true;
    }


    /**
     * @description: 处理未分单的订单
     * @param:
     * @return boolean
     * @author Mr.Simple
     * @date 2019/2/15 9:27
     */
    private boolean updateAndCreateOrder(Long orderId /**, Shipment shipment*/){
        //根据orderId查出orderCode
        OrderInfoBo orderInfoBo = iOrderInfoBiz.selectById(orderId);
        if(orderInfoBo == null)
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR + "订单：" + orderId + "不存在");
        Order shipstationOrder = getShipStationOrder(orderInfoBo /**, shipment*/);
        log.info("======================shipstationOrder.getShipTo:{}", shipstationOrder.getShipTo());
        Client client = ClientBuilder.newClient();
        Entity payload = Entity.json(Tools.json.toJson(shipstationOrder));
        log.info("======================payload:{}", payload);
        Response response = client.target("https://ssapi.shipstation.com/orders/createorder")
                                    .request(MediaType.APPLICATION_JSON_TYPE)
                                    .header("Authorization", authorization)
                                    .post(payload);

//        ReceiveOrder receiveOrder = Tools.json.toObject(response.readEntity(String.class), ReceiveOrder.class);
        log.info("======================response.getStatus:{}", response.getStatus());
        log.info("======================response.getHeaders:{}", response.getHeaders());
        if(response.getStatus() != 200)
            return false;
        else{
            //对返回数据进行处理
            ReceiveOrder o = response.readEntity(ReceiveOrder.class);
            log.info("======================response.body:{}", o);
            orderInfoBo.setShipstationOrderId(o.getOrderId());
            if(!iOrderInfoBiz.updateById(orderInfoBo)){
                return false;
            }
            return true;
        }

    }

    /**
     * @description: 处理有子单的订单
     * @param:
     * @return boolean
     * @author Mr.Simple
     * @date 2019/2/15 9:27
     */
    private boolean updateAndCreateMultipleOrder(List<OrderInfoBo> orderInfoList /**, Shipment shipment*/){
//        List<Order> shipstationOrder = new ArrayList<>();
        for (OrderInfoBo orderInfoBo : orderInfoList){
            if(!updateAndCreateOrder(orderInfoBo.getId() /**, shipment*/)){
                return false;
            }
            //使用单次添加订单即可，无需下面操作
//            Order shipOrder = getShipStationOrder(orderInfoBo /**, shipment*/);
//            shipstationOrder.add(shipOrder);
        }

        return true;
    }

    /**
     * @description: 将订单标记为发货
     * @param:
     * @return boolean
     * @author Mr.Simple
     * @date 2019/2/18 10:30
     */
    private boolean shipped(ShippedOrder shippedOrder){
        Client client = ClientBuilder.newClient();
        Entity payload = Entity.json(Tools.json.toJson(shippedOrder));
        Response response = client.target("https://ssapi.shipstation.com/orders/markasshipped")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", authorization)
                .post(payload);
        log.info("======================response.getStatus:{}", response.getStatus());
        log.info("======================response.getHeaders:{}", response.getHeaders());
        log.info("======================response.body:{}", response.readEntity(String.class));
        if(response.getStatus() != 200)
            return false;

        return true;

    }

    /**
     * @description: 取消订单
     * @param:
     * @return boolean
     * @author Mr.Simple
     * @date 2019/2/20 9:49
     */
    private boolean cancel(Long orderId){
        OrderInfoBo orderInfoBo = iOrderInfoBiz.selectById(orderId);
        ShipmentBo shipmentBo = getShipment(orderId);
        Order shipstationOrder = getShipStationOrder(orderInfoBo/**, shipmentBo*/);
        shipstationOrder.setOrderStatus(ShipStationOrderStatusEnum.CANCELLED.getKey());
        Client client = ClientBuilder.newClient();
        Entity payload = Entity.json(Tools.json.toJson(shipstationOrder));
        log.info("======================payload:{}", payload);
        Response response = client.target("https://ssapi.shipstation.com/orders/createorder")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", authorization)
                .post(payload);
        log.info("======================response.getStatus:{}", response.getStatus());
        log.info("======================response.getHeaders:{}", response.getHeaders());
        log.info("======================response.body:{}", response.readEntity(String.class));
        if(response.getStatus() != 200)
            return false;
        return true;
    }


    private ShipmentBo getShipment(Long orderId){
        //根据orderId查出运费模板信息
        OrderLogisticsBo orderLogisticsBo = iOrderLogisticsBiz.selectOne(Condition.create()
                                                                .eq(OrderLogisticsBo.Key.orderId.toString(), orderId));
        if(orderLogisticsBo == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        //根据shipmentId查出运费模板
        ShipmentBo shipmentBo = iShipmentBiz.selectById(orderLogisticsBo.getShipmentId());
        return shipmentBo;
    }

    private Order getShipStationOrder(OrderInfoBo orderInfoBo /**, Shipment shipment*/){

        Order shipstationOrder = new Order();
        String orderCode = orderInfoBo.getOrderCode();
        //根据memberId查出买家邮箱
        MemberBo memberBo = iMemberBiz.selectById(orderInfoBo.getBuyerId());
        List<OrderItem> orderItemList = new ArrayList<>();
        double allAmount = 0.0;
        //根据订单id查出订单商品详情，收货地址
        List<OrderGoodsBo> orderGoodsList = iOrderGoodsBiz.selectList(Condition.create()
                .eq(OrderGoodsBo.Key.orderId.toString(), orderInfoBo.getId()));
        if(orderGoodsList.size() == 0){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        //遍历订单下的所有商品
        for(OrderGoodsBo item: orderGoodsList){
            OrderItem orderItem = new OrderItem();
            orderItem.setLineItemKey(item.getId().toString());
//            orderItem.setOrderItemId(123);
            orderItem.setName(item.getGoodsName());
            if(Tools.string.isNotEmpty(item.getImgSku()))
                orderItem.setImageUrl(QiniuUtil.getFullPath(item.getImgSku()));
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUnitPrice(item.getPrice().doubleValue());
//            orderItem.setProductId(123);
            orderItem.setCreateDate(sdf.format(new Date()));
            orderItem.setModifyDate(sdf.format(new Date()));
            orderItemList.add(orderItem);
            allAmount += item.getGoodsfee().doubleValue();
        }
        //查出收货地址
        Address shipstationAddress = getReceiveAddress(orderInfoBo.getId());
//        log.info("======================shipstationAddress.getStreet1:{}", shipstationAddress.getStreet1());
//        shipstationOrder.setOrderId(25668402);
        shipstationOrder.setOrderNumber(orderInfoBo.getId().toString());
        shipstationOrder.setOrderKey(orderCode);
        shipstationOrder.setOrderDate(sdf.format(orderInfoBo.getGmtCreate()));
        shipstationOrder.setOrderStatus(ShipStationOrderStatusEnum.AWAITINGSHIPMENT.getKey());
        shipstationOrder.setCustomerEmail(memberBo.getEmail());
        shipstationOrder.setItems(orderItemList);
        shipstationOrder.setCarrierCode("fedex");
        shipstationOrder.setServiceCode("fedex_2day");
//        shipstationOrder.setCarrierCode(shipment.getCarrierCode());
//        shipstationOrder.setServiceCode(shipment.getServiceCode());
        shipstationOrder.setAmountPaid(allAmount);
        shipstationOrder.setShipTo(shipstationAddress);
        shipstationOrder.setBillTo(shipstationAddress);
        return shipstationOrder;
    }

    /**
     * @description: 获取收货地址转换为shipstationAddress
     * @param:
     * @return Address
     * @author Mr.Simple
     * @date 2019/2/15 11:53
     */
    private Address getReceiveAddress(Long orderId){
        Address shipstationAddress = new Address();
        //根据orderId查出收货地址
        OrderLogisticsBo orderLogisticsBo = iOrderLogisticsBiz.selectOne(Condition.create()
                                            .eq(OrderLogisticsBo.Key.orderId.toString(), orderId));

        if(orderLogisticsBo == null)
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR + "物流查询失败");
        //通过province找出短名
        BaseAreaBo baseAreaBo = iBaseAreaBiz.selectOne(Condition.create()
                                        .eq(BaseAreaBo.Key.name.toString(), orderLogisticsBo.getReceiverProvince())
                                        .eq(BaseAreaBo.Key.pid.toString(), 0));
        if(baseAreaBo == null){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR + "收货地区不存在");
        }
        shipstationAddress.setCountry("US");
        shipstationAddress.setName(orderLogisticsBo.getReceiverFullname());
        shipstationAddress.setPhone(orderLogisticsBo.getReceiverMobile());
        shipstationAddress.setPostalCode(orderLogisticsBo.getReceiverPostCode());
        shipstationAddress.setCity(orderLogisticsBo.getReceiverCity());
        shipstationAddress.setState(baseAreaBo.getShortName());
        shipstationAddress.setResidential(true);
        shipstationAddress.setStreet1(orderLogisticsBo.getReceiverDetailAddress());
        log.info("=====================orderLogisticsBo.getReceiverDetailAddress:{}",orderLogisticsBo.getReceiverDetailAddress());
        return shipstationAddress;
    }


}
