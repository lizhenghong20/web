package cn.farwalker.ravv.service.shipstation.biz;


public interface IShipStationService {
    boolean addShipStationOrder(Long orderId);

    boolean orderShipped(Long orderId);

    boolean cancelOrder(Long orderId);

    boolean listCarriers();

    boolean listService();
}
