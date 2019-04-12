package cn.farwalker.ravv.flash;

import java.util.List;

import cn.farwalker.ravv.admin.flash.AdminFlashService;
import cn.farwalker.waka.core.WakaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.web.crud.QueryFilter;

import cn.farwalker.ravv.service.flash.sale.model.FlashSaleBo;
import cn.farwalker.waka.core.JsonResult;

/**
 * 限时购<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
@RestController
@RequestMapping("/flashSale")
public class FlashSaleController{
	private final static Logger log = LoggerFactory.getLogger(FlashSaleController.class);

	@Autowired
	private AdminFlashService adminFlashService;

	/**
	 * 创建记录
	 * 
	 * @param vo
	 *            vo<br/>
	 */
	@RequestMapping("/create")
	public JsonResult<Boolean> doCreate(@RequestBody FlashSaleBo vo) {
		try{
			// createMethodSinge创建方法
			if (vo == null) {
				throw new WakaException("vo不能为空");
			}
			return JsonResult.newSuccess(adminFlashService.create(vo));
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
	 *            限时购id<br/>
	 */
	@RequestMapping("/delete")
	public JsonResult<Boolean> doDelete(@RequestParam Long id) {
		try{
			// createMethodSinge创建方法
			if (id == null) {
				throw new WakaException("id不能为空");
			}
			return JsonResult.newSuccess(adminFlashService.delete(id));
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
	 *            限时购id<br/>
	 */
	@RequestMapping("/get")
	public JsonResult<FlashSaleBo> doGet(@RequestParam Long id) {
		try{
			// createMethodSinge创建方法
			if (id == null) {
				throw new WakaException("id不能为空");
			}
			return JsonResult.newSuccess(adminFlashService.getOne(id));
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
	 *            查询条件<br/>
	 * @param start
	 *            开始行号<br/>
	 * @param size
	 *            记录数<br/>
	 * @param sortfield
	 *            排序(+字段1,-字段名2)<br/>
	 */
	@RequestMapping("/list")
	public JsonResult<Page<FlashSaleBo>> doList(@RequestBody List<QueryFilter> query, Integer start, Integer size,
			String sortfield) {
		try{
			// createMethodSinge创建方法
//			if (Tools.collection.isEmpty(query) || start < 0 || size < 0 || Tools.string.isEmpty(sortfield)) {
//				throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
//			}
			return JsonResult.newSuccess(adminFlashService.getList(query, start, size, sortfield));
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
	 *            vo<br/>
	 */
	@RequestMapping("/update")
	public JsonResult<Boolean> doUpdate(@RequestBody FlashSaleBo vo) {
		try{
			// createMethodSinge创建方法
			if (vo == null) {
				throw new WakaException("vo不能为空");
			}
			return JsonResult.newSuccess(adminFlashService.update(vo));
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