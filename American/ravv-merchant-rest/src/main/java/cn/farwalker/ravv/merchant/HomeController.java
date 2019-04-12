package cn.farwalker.ravv.merchant;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cangwu.frame.orm.core.annotation.LoadJoinValueImpl;

import cn.farwalker.ravv.service.merchant.biz.IMerchantBiz;
import cn.farwalker.ravv.service.merchant.biz.IMerchantService;
import cn.farwalker.ravv.service.merchant.model.MerchantOrderVo;
import cn.farwalker.ravv.service.merchant.model.MerchantSalesVo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.util.Tools;

@RestController
@RequestMapping("/home")
public class HomeController {
	private final static Logger log = LoggerFactory.getLogger(HomeController.class);
	@Resource
	private IMerchantService merchantService;
	@Resource
	private IMerchantBiz merchantBiz;
	
	protected IMerchantBiz getBiz() {
		return merchantBiz;
	}
	/**
	 * 订单统计
	 * @param merchantId
	 * @param startdate
	 * @param enddate
	 * @return
	 */
	@RequestMapping("/stats")
	public JsonResult<MerchantOrderVo> getOrderStats(Date startdate,Date enddate) {
		Long merchantId = Tools.web.getOnlineUser();
		if (merchantId == null) {
			return JsonResult.newFail("id不能为空");
		}
		Calendar today = Calendar.getInstance();
		if(enddate == null){
			enddate = today.getTime();
		}
		else{
			today.setTime(enddate);
		}
		if(startdate == null){//半年数据
			today.add(Calendar.MONTH, -6);
			startdate = today.getTime();
		}
		
		List<Long> merchantIds = Collections.singletonList(merchantId);
		List<MerchantOrderVo> rds = merchantService.getMerchantOrderStats(merchantIds, startdate, enddate);//getBiz().selectById(merchantId);
		if(Tools.collection.isEmpty(rds)){
			MerchantOrderVo vo = new MerchantOrderVo();
			vo.setMerchantId(merchantId);
			vo.setStartdate(startdate);
			vo.setEnddate(enddate);
			LoadJoinValueImpl.load(merchantBiz, vo);
			return JsonResult.newSuccess(vo);
		}
		else{
			LoadJoinValueImpl.load(merchantBiz, rds);
			return JsonResult.newSuccess(rds.get(0));	
		}
		
	}
	
	/**
	 * 获取时间范围内的销售数量和销售额
	 * @param startdate
	 * @param enddate
	 * @return
	 */
	@RequestMapping("/sales_statistics")
	public JsonResult<MerchantSalesVo> getSalesStatistics(Date startdate,Date enddate) {
		Long merchantId = Tools.web.getOnlineUser();//获取供应商id
		if (merchantId == null) {
			return JsonResult.newFail("id不能为空");
		}
		Calendar today = Calendar.getInstance();
		if(enddate == null){
			enddate = today.getTime();
		}
		else{
			today.setTime(enddate);
		}
		if(startdate == null){//半年数据
			today.add(Calendar.MONTH, -6);
			startdate = today.getTime();
		}
		
		MerchantSalesVo salesVo = merchantService.getSalesStatistics(merchantId, startdate, enddate);
		
		return JsonResult.newSuccess(salesVo);
	}
}
