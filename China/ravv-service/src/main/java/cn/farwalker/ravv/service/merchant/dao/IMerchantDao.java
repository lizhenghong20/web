package cn.farwalker.ravv.service.merchant.dao;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.farwalker.ravv.service.merchant.model.MerchantBo;
import cn.farwalker.ravv.service.merchant.model.MerchantOrderVo;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 供应商<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IMerchantDao extends BaseMapper<MerchantBo>{
	/**订单数(有支付的都有效)*/
	public Integer getOrderCount(@Param("merchantId")Long merchantId
			,@Param("startdate") Date startdate,@Param("enddate") Date enddate);
	
	/**订单商品数量(有支付的都有效)*/
	public List<MerchantOrderVo> getGoodsCount(@Param("merchantIds") List<Long> merchantIds
			,@Param("startdate") Date startdate,@Param("enddate") Date enddate);
	
	/**完成订单数量（退一件都不统计）*/
	public Integer getOrderFinish(@Param("merchantId")Long merchantId
			,@Param("startdate") Date startdate,@Param("enddate") Date enddate);
	
	/**取消订单数量（没有发货，取消的订单）*/
	public Integer getOrderCancel(@Param("merchantId")Long merchantId
			,@Param("startdate") Date startdate,@Param("enddate") Date enddate);
	
	/**退货/换货商品数量*/
	public List<MerchantOrderVo> getGoodsReturn(@Param("merchantIds")List<Long> merchantIds
			,@Param("startdate") Date startdate,@Param("enddate") Date enddate);

	/**订单关闭后的有效金额-订单总额（商品数量*单价）*/
	public List<MerchantOrderVo> getTotalOrderAmt(@Param("merchantIds")List<Long> merchantIds
			,@Param("startdate") Date startdate,@Param("enddate") Date enddate);
	
	/**订单关闭后的有效金额-退货总额（商品数量*单价）*/
	public List<MerchantOrderVo> getTotalReturnAmt(@Param("merchantIds")List<Long> merchantIds
			,@Param("startdate") Date startdate,@Param("enddate") Date enddate);
	
}