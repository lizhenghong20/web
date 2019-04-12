package cn.farwalker.ravv.service.order.returns.biz.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import cn.farwalker.ravv.service.order.returns.model.OrderReturnsDetailBo;
import cn.farwalker.waka.util.Tools;
import cn.farwalker.ravv.service.order.returns.dao.IOrderReturnsDetailDao;
import cn.farwalker.ravv.service.order.returns.biz.IOrderReturnsDetailBiz;

/**
 * 订单退货详情<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class OrderReturnsDetailBizImpl extends ServiceImpl<IOrderReturnsDetailDao,OrderReturnsDetailBo> implements IOrderReturnsDetailBiz{

	@Resource
	private IOrderReturnsDetailBiz orderReturnsDetailBiz;
	
	@Resource
	private IOrderReturnsDetailDao orderReturnsDetailDao;
	
	@Override
	public Integer succeedReGoodsNum(Long returnsId) {
		Integer succeedReGoodsNum = 0;
		
		if(returnsId == null) {
			return succeedReGoodsNum;
		}
		
		Wrapper<OrderReturnsDetailBo> wrapper = new EntityWrapper<>();
		wrapper.eq(OrderReturnsDetailBo.Key.returnsId.toString(), returnsId);
		
		List<OrderReturnsDetailBo> detailBos = orderReturnsDetailBiz.selectList(wrapper);
		
		if(Tools.collection.isEmpty(detailBos)) {
			return succeedReGoodsNum;
		}
		
		for(OrderReturnsDetailBo detail : detailBos) {
			succeedReGoodsNum += detail.getActualRefundQty();
		}
		
		return succeedReGoodsNum;
	}

	@Override
	public List<OrderReturnsDetailBo> getReturnsDetailList(Long orderId, Long goodsId) {
		if(null == orderId || null == goodsId) {
			return null;
		}
		
		return orderReturnsDetailDao.getReturnsDetailList(orderId, goodsId);
	}
}