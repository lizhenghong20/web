package cn.farwalker.ravv.service.shipment.biz.impl;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
 

import cn.farwalker.ravv.service.shipment.biz.IShipmentBiz;
import cn.farwalker.ravv.service.shipment.biz.IShipmentService;
import cn.farwalker.ravv.service.shipment.constants.ShipmentTypeEnum;
import cn.farwalker.ravv.service.shipment.model.ShipmentBo;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * 运费模板(美国的方式)<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class ShipmentServiceImpl implements IShipmentService{
	@Resource
	private IShipmentBiz shipmentBiz;
	
	@Override
	public List<ShipmentBo> getGeneralList() {
		Wrapper<ShipmentBo> wp = new EntityWrapper<>();
		wp.eq(ShipmentBo.Key.shipmentType.toString(), ShipmentTypeEnum.General);
		List<ShipmentBo> rs  = shipmentBiz.selectList(wp);
		return rs;
	}
}