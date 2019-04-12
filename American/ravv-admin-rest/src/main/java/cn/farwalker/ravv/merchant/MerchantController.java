package cn.farwalker.ravv.merchant;

import java.util.Date;
import java.util.List;


import cn.farwalker.ravv.admin.merchant.AdminMerchantService;
import cn.farwalker.waka.core.WakaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import cn.farwalker.waka.core.JsonResult;
import com.cangwu.frame.web.crud.QueryFilter;
import cn.farwalker.ravv.service.merchant.model.MerchantBo;
import cn.farwalker.ravv.service.merchant.model.MerchantOrderVo;
import cn.farwalker.ravv.service.merchant.model.MerchantSalesVo;


/**
 * 供应商<br/>
 * 供应商 controller<br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
@RestController
@RequestMapping("/merchant")
public class MerchantController{
	private final static Logger log = LoggerFactory.getLogger(MerchantController.class);

	@Autowired
	private AdminMerchantService adminMerchantService;

	/**
	 * 创建记录
	 * 
	 * @param vo
	 *            null<br/>
	 */
	@RequestMapping("/create")
	@Transactional
	public JsonResult<?> doCreate(@RequestBody MerchantBo vo) {
		try{
			if (vo == null) {
				throw new WakaException("vo不能为空");
			}
			return JsonResult.newSuccess(adminMerchantService.create(vo));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}

	}

	/**
	 * 删除记录
	 * 
	 * @param id
	 *            null<br/>
	 */
	@RequestMapping("/delete")
	public JsonResult<Boolean> doDelete(@RequestParam Long id) {
		try{
			if (id == null) {
				throw new WakaException("id不能为空");
			}

			return JsonResult.newSuccess(adminMerchantService.delete(id));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}
	}

	/**
	 * 取得单条记录
	 * 
	 * @param id
	 *            null<br/>
	 */
	@RequestMapping("/get")
	public JsonResult<MerchantBo> doGet(@RequestParam Long id) {
		try{
			if (id == null) {
				throw new WakaException("vo不能为空");
			}

			return JsonResult.newSuccess(adminMerchantService.getOne(id));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}

	}

	/**
	 * 列表记录
	 * 
	 * @param query
	 *            null<br/>
	 * @param start
	 *            开始行号<br/>
	 * @param size
	 *            记录数<br/>
	 * @param sortfield
	 *            排序字段(+/-表示升序或者降序) 格式: +id,-name,+fsdate <br/>
	 */
	@RequestMapping("/list")
	public JsonResult<Page<MerchantBo>> doList(@RequestBody List<QueryFilter> query, Integer start, Integer size,
			String sortfield) {
		try{
//			if (Tools.collection.isEmpty(query) || start < 0 || size < 0 || Tools.string.isEmpty(sortfield)) {
//				throw new WakaException("id不能为空");
//			}
			return JsonResult.newSuccess(adminMerchantService.getList(query, start, size, sortfield));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}
	}

	/**
	 * 修改记录
	 * 
	 * @param vo
	 *            null<br/>
	 */
	@RequestMapping("/update")
	public JsonResult<?> doUpdate(@RequestBody MerchantBo vo) {
		try{
			if (vo == null) {
				throw new WakaException("vo不能为空");
			}
			return JsonResult.newSuccess(adminMerchantService.update(vo));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}

	}
	
	/**
	 * 供应商订单统计
	 * @param merchantId
	 * @param startdate
	 * @param enddate
	 * @return
	 */
	@RequestMapping("/stats")
	public JsonResult<MerchantOrderVo> getOrderStats(Long merchantId, Date startdate,Date enddate) {
		try{
			if (merchantId == null) {
				throw new WakaException("供应商id不能为空");
			}
			return JsonResult.newSuccess(adminMerchantService.getOrderStats(merchantId, startdate, enddate));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}

		
	}
	
	/**
	 * 获取供应商某时间范围内的销售数量和销售额
	 * @param startdate
	 * @param enddate
	 * @return
	 */
	@RequestMapping("/sales_statistics")
	public JsonResult<MerchantSalesVo> getSalesStatistics(Long merchantId, Date startdate,Date enddate) {
		try{
			if (merchantId == null) {
				throw new WakaException("供应商id不能为空");
			}
			return JsonResult.newSuccess(adminMerchantService.getSalesStatistics(merchantId, startdate, enddate));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}

	}
}