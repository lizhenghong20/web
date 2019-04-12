package cn.farwalker.ravv.profitallot;
import java.util.Date;

import cn.farwalker.ravv.admin.profitallot.AdminProfitallotService;
import cn.farwalker.waka.core.WakaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;

import cn.farwalker.ravv.admin.profitallot.dto.MemberPromoteInfoVo;
import cn.farwalker.ravv.admin.profitallot.dto.MemberRelativeTreeVo;
import cn.farwalker.ravv.admin.profitallot.dto.RebatedMonthVo;
import cn.farwalker.ravv.service.member.basememeber.constants.MemberSubordinate;
import cn.farwalker.ravv.service.member.basememeber.model.MemberSimpleInfoVo;
import cn.farwalker.ravv.service.sale.profitallot.constants.DistStatusEnum;
import cn.farwalker.ravv.service.sale.profitallot.model.ProfitAllotInfoVo;
import cn.farwalker.waka.core.JsonResult;

/**
 * 分销相关接口<br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@RestController
@RequestMapping("/profitallot")
public class ProfitAllotController{
    private final static  Logger log =LoggerFactory.getLogger(ProfitAllotController.class);

    @Autowired
	private AdminProfitallotService adminProfitallotService;
    
    /**
     * 获取会员分销基本信息和统计数据
     * @return
     */
    @RequestMapping("/getpromoteInfo")
    public JsonResult<MemberPromoteInfoVo> getPromoteInfo(Long memberId){
		try{
			if (memberId == null || memberId == 0) {
				throw new WakaException("id不能为空");
			}
			return JsonResult.newSuccess(adminProfitallotService.getPromoteInfo(memberId));
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
     * 获取会员分销关系（上级和下三级信息）
     * @param start 页码
     * @param size 每页行数
     * @return
     */
    @RequestMapping("/getrelativetree")
    public JsonResult<MemberRelativeTreeVo> getRelativeTree(Long memberId, Integer start, Integer size){
		try{
			if (memberId == null || memberId == 0) {
				throw new WakaException("memberId为空");
			}
			return JsonResult.newSuccess(adminProfitallotService.getRelativeTree(memberId, start, size));
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
     * 获取会员分销关系(下级任意一级)
     * @param start 页码
     * @param size 每页行数
     * @return
     */
    @RequestMapping("/getrelativelevel")
    public JsonResult<Page<MemberSimpleInfoVo>> getRelativeByLevel(Long memberId, MemberSubordinate subordinate,
																   Integer start, Integer size){
		try{
//			if (memberId == null || memberId == 0 || start < 0 || size < 0) {
//				throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
//			}
			if(null == subordinate) {
				throw new WakaException("请选择下级推荐人等级");
			}
			return JsonResult.newSuccess(adminProfitallotService.getRelativeByLevel(memberId, subordinate, start, size));
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
     * 获取某一分销状态的分销列表
     * @param status 分销状态
     * @return
     */
    @RequestMapping("/rebatedlist_bystatus")
    public JsonResult<Page<ProfitAllotInfoVo>> getRebatedListByStatus(Long memberId, DistStatusEnum status,
																	  Integer start, Integer size){
		try{
//			if (memberId == null || memberId == 0 || start < 0 || size < 0) {
//				throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
//			}
			if(null == status) {
				throw new WakaException("分润状态不能为空");
			}
			return JsonResult.newSuccess(adminProfitallotService.getRebatedListByStatus(memberId, status, start, size));
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
     * 获取会员已返现的分销记录列表（按月份分类和统计）
     * @return
     */
    @RequestMapping("/getrebatedlist")
    public JsonResult<Page<RebatedMonthVo>> getRebatedList(Long memberId, Integer start, Integer size){
		try{
//			if (memberId == null || memberId == 0 || start < 0 || size < 0) {
//				throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
//			}
			return JsonResult.newSuccess(adminProfitallotService.getRebatedList(memberId, start, size));
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
     * 获取某月已返现分销列表
     * @param month 所选择月份
     * @return
     */
    @RequestMapping("/month_rebatedlist")
    public JsonResult<Page<RebatedMonthVo>> getMonthRebatedlist(Long memberId, Date month, Integer start, Integer size){
		try{
//			if (memberId == null || memberId == 0 || start < 0 || size < 0) {
//				throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
//			}
			if(null == month) {
				throw new WakaException("请选择月份");
			}

			return JsonResult.newSuccess(adminProfitallotService.getMonthRebatedlist(memberId, month, start, size));
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