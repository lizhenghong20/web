package cn.farwalker.ravv.service.shipment.biz;

import java.util.List;

import cn.farwalker.ravv.service.shipment.model.ShipmentBo;

public interface IShipmentService {
	/**取得通用的物流方式*/
	public List<ShipmentBo> getGeneralList();
}
