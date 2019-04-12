package cn.farwalker.ravv.service.sys.param.biz.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

import cn.farwalker.ravv.service.sys.param.biz.ISysParamBiz;
import cn.farwalker.ravv.service.sys.param.biz.ISysParamService;
import cn.farwalker.ravv.service.sys.param.model.SysParamBo;
import cn.farwalker.waka.util.Tools;

@Service
public class SysParamService implements ISysParamService{
	private static final String OrderCloseDay="order_close_day";
	
	@Resource
	private ISysParamBiz sysparamBiz;

	/** 取没有支付的订单解除冻结时间(分钟)*/
	private static final String OrderUnfreezeTime="order_unfreeze_time";
	
	/** 取没有支付的订单解除冻结时间(分钟)*/
	@Override
	public Integer getOrderUnfreezeTime(){
		String value = getBoValue(OrderUnfreezeTime);
		if(Tools.number.isInteger(value)){
			return Integer.valueOf(value);
		}
		else{
			return null;
		}
	}

	private String getBoValue(String key){
		Wrapper<SysParamBo> wp = new EntityWrapper<>();
		wp.eq(SysParamBo.Key.code.toString(), key);
		wp.last("limit 1");
		SysParamBo bo = sysparamBiz.selectOne(wp);
		return (bo == null ? null : bo.getPvalue());
	}

	@Override
	public Integer getOrderCloseDay() {
		String value = getBoValue(OrderCloseDay);
		if(Tools.number.isInteger(value)){
			return Integer.valueOf(value);
		}
		else{
			return null;
		}
	}

}
