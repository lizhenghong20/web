package cn.farwalker.ravv.youtube.liveanchor;

import java.util.Date;
import java.util.List;

import cn.farwalker.ravv.admin.youtube.AdminYoutubeService;
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

import cn.farwalker.ravv.service.youtube.liveanchor.model.YoutubeLiveAnchorBo;
import cn.farwalker.ravv.admin.youtube.dto.LiveAnchorVO;
import cn.farwalker.waka.core.JsonResult;


/**
 * YouTube 主播表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
@RestController
@RequestMapping("/youtube/live_anchor")
public class LiveAnchorController{
	private final static Logger log = LoggerFactory.getLogger(LiveAnchorController.class);

	@Autowired
	private AdminYoutubeService adminYoutubeService;

	/**
	 * 创建记录
	 * 
	 * @param vo
	 */
	@RequestMapping("/create")
	public JsonResult<Boolean> doCreate(@RequestBody YoutubeLiveAnchorBo vo) {
		try {
			if (vo == null) {
				return JsonResult.newFail("vo不能为空");
			}
			return JsonResult.newSuccess(adminYoutubeService.createLiveAnchor(vo));
		} catch (WakaException e) {
			log.error("", e);
			return JsonResult.newFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());

		}
	}

	/**
	 * 删除记录
	 * 
	 * @param id
	 */
	@RequestMapping("/delete")
	public JsonResult<Boolean> doDelete(@RequestParam Long id) {
		try{
			if (id == null) {
				throw new WakaException("id不能为空");
			}
			return JsonResult.newSuccess(adminYoutubeService.deleteLiveAnchor(id));
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
	 */
	@RequestMapping("/get")
	public JsonResult<YoutubeLiveAnchorBo> doGet(@RequestParam Long id) {
		try{
			if (id == null) {
				throw new WakaException("vo不能为空");
			}
			return JsonResult.newSuccess(adminYoutubeService.getOneLiveAnchor(id));
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
	 * @param query 查询条件<br/>
	 * @param start 开始行号<br/>
	 * @param size 记录数<br/>
	 * @param sortfield 排序(+字段1,-字段名2)<br/>
	 */
	@RequestMapping("/list")
	public JsonResult<Page<LiveAnchorVO>> doList(@RequestBody List<QueryFilter> query, Integer start, Integer size,
			String sortfield) {
		try{
//			if (Tools.collection.isEmpty(query) || start < 0 || size < 0 || Tools.string.isEmpty(sortfield)) {
//				throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
//			}
			return JsonResult.newSuccess(adminYoutubeService.getListLiveAnchor(query, start, size, sortfield));
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
	 */
	@RequestMapping("/update")
	public JsonResult<Boolean> doUpdate(@RequestBody YoutubeLiveAnchorBo vo) {
		try {
			if (vo == null) {
				return JsonResult.newFail("vo不能为空");
			}
			return JsonResult.newSuccess(adminYoutubeService.updateLiveAnchor(vo));
		} catch (WakaException e) {
			log.error("", e);
			return JsonResult.newFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());

		}
	}

	/**
	 * 查看主播某月直播时长
	 * 
	 * @param anchorMemberId 主播会员id
	 * @param month 查看的月份
	 * @return
	 */
	@RequestMapping("/month_live_duration")
	public JsonResult<Integer> getMonthLiveDuration(Long anchorMemberId, Date month) {
		try {
			if (null == anchorMemberId) {
				return JsonResult.newFail("id不能为空");
			}
			if (null == month) {
				return JsonResult.newFail("请选择月份");
			}
			return JsonResult.newSuccess(adminYoutubeService.getMonthLiveDuration(anchorMemberId, month));
		} catch (WakaException e) {
			log.error("", e);
			return JsonResult.newFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());

		}

	}

	/**
	 * 冻结主播账号
	 * @param vo 
	 * @return
	 */
	@RequestMapping("/freeze_anchor")
	public JsonResult<Boolean> freezeAnchor(@RequestBody YoutubeLiveAnchorBo vo) {
		try {
			if (vo == null) {
				throw new WakaException("vo不能为空");
			}
			if (null == vo.getId()) {
				throw new WakaException("主播id不能为空");
			}
			if (null == vo.getFrozenStart() || null == vo.getFrozenEnd()) {
				throw new WakaException("冻结开始时间和冻结结束时间不能为空");
			}
			if (null == vo.getFreezeReason()) {
				throw new WakaException("冻结账号原因不能为空");
			}

			return JsonResult.newSuccess(adminYoutubeService.freezeAnchor(vo));
		} catch (WakaException e) {
			log.error("", e);
			return JsonResult.newFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());
		}
	}

	/**
	 * 主播解除冻结
	 * 
	 * @param anchorId 主播id
	 * @param sysUserId 管理员id
	 * @param unfreezeReason 解除冻结原因
	 * @return
	 */
	@RequestMapping("/unfreeze_anchor")
	public JsonResult<Boolean> unfreezeAnchor(Long anchorId, Long sysUserId, String unfreezeReason) {
		try {
			if (null == anchorId) {
				throw new WakaException("主播id不能为空");
			}
			if (null == sysUserId) {
				throw new WakaException("管理员id不能为空");
			}
			if (null == unfreezeReason) {
				throw new WakaException("解封原因不能为空");
			}
			return JsonResult.newSuccess(adminYoutubeService.unfreezeAnchor(anchorId, sysUserId, unfreezeReason));
		} catch (WakaException e) {
			log.error("", e);
			return JsonResult.newFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("", e);
			return JsonResult.newFail(e.getMessage());
		}

	}
}