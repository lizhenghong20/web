package cn.farwalker.ravv.member;

import java.util.List;

import javax.annotation.Resource;

import cn.farwalker.ravv.admin.member.AdminMemberService;
import cn.farwalker.waka.core.RavvExceptionEnum;
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

import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.member.basememeber.model.MemberVo;
import cn.farwalker.ravv.service.member.level.biz.IMemberLevelBiz;
import cn.farwalker.ravv.service.member.level.model.MemberLevelBo;
import cn.farwalker.waka.core.JsonResult;


/**
 * 会员管理<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
@RestController
@RequestMapping("/member")
public class MemberController{
	private final static Logger log = LoggerFactory.getLogger(MemberController.class);

	@Resource
	private IMemberLevelBiz memberLevelBiz;

	@Autowired
	private AdminMemberService adminMemberService;

	/**
	 * 创建记录
	 * 
	 * @param vo
	 *            null<br/>
	 */
	@RequestMapping("/create")
	public JsonResult<Boolean> doCreate(@RequestBody MemberBo vo) {
		try{
			// createMethodSinge创建方法
			// 获取id 货币名称
			if (vo == null) {
				throw new WakaException("vo不能为空");
			}
			return JsonResult.newSuccess(adminMemberService.createMember(vo));
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
			// createMethodSinge创建方法
			// 获取id 货币名称
			if (id == null) {
				throw new WakaException("id不能为空");
			}
			return JsonResult.newSuccess(adminMemberService.deleteMemebr(id));
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
	public JsonResult<MemberVo> doGet(@RequestParam Long id) {
		try{
			// createMethodSinge创建方法
			// 获取id 货币名称
			if (id == null) {
				throw new WakaException("id不能为空");
			}
			return JsonResult.newSuccess(adminMemberService.getMember(id));
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
	 *            排序字段json:{字段名:1或2}<br/>
	 */
	@RequestMapping("/list")
	public JsonResult<Page<MemberVo>> doList(@RequestBody List<QueryFilter> query, Integer start, Integer size,
			String sortfield) {
		try{
			// createMethodSinge创建方法
			// 获取id 货币名称
//			if (Tools.collection.isEmpty(query) || start < 0 || size < 0 || Tools.string.isEmpty(sortfield)) {
//				throw new WakaException("id不能为空");
//			}
			return JsonResult.newSuccess(adminMemberService.getMemberList(query, start, size, sortfield));
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
	 * 
	 */
	@RequestMapping("/update")
	public JsonResult<Boolean> doUpdate(@RequestBody MemberBo vo, Long userId) {
		try{
			// createMethodSinge创建方法
			// 获取id 货币名称
			if (vo == null) {
				throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
			}
			return JsonResult.newSuccess(adminMemberService.updateMember(vo, userId));
		}
		catch(WakaException e){
			log.error("",e);
			return JsonResult.newFail(e.getCode(),e.getMessage());
		}catch(Exception e){
			log.error("",e);
			return JsonResult.newFail(e.getMessage());
		}

	}

	/** 取得用户等级定义 */
	@RequestMapping("/levels")
	public JsonResult<List<MemberLevelBo>> getLevels() {
		// createMethodSinge创建方法
		List<MemberLevelBo> rs = memberLevelBiz.selectList(null);
		return JsonResult.newSuccess(rs);
	}
}