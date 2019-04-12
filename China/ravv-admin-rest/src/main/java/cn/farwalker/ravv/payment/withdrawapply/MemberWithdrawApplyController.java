package cn.farwalker.ravv.payment.withdrawapply;
import java.util.List;

import javax.annotation.Resource;

import cn.farwalker.ravv.admin.payment.withdrawapply.AdminWithdrawApplyService;
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

import cn.farwalker.ravv.admin.payment.withdrawapply.dto.MemberWithdrawApplyVo;
import cn.farwalker.ravv.service.payment.withdrawapply.biz.IMemberWithdrawApplyBiz;
import cn.farwalker.ravv.service.payment.withdrawapply.constants.WithdrawStatusEnum;
import cn.farwalker.ravv.service.payment.withdrawapply.model.MemberWithdrawApplyBo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.util.Tools;

/**
 * 用户提现申请<br/>
 * 用户提现申请controller<br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@RestController
@RequestMapping("/payment/withdrawapply")
public class MemberWithdrawApplyController{
    private final static  Logger log =LoggerFactory.getLogger(MemberWithdrawApplyController.class);
    @Resource
    private IMemberWithdrawApplyBiz memberWithdrawApplyBiz;
    protected IMemberWithdrawApplyBiz getBiz(){
        return memberWithdrawApplyBiz;
    }

    @Autowired
    private AdminWithdrawApplyService adminWithdrawApplyService;
    
    /**创建记录
     * @param vo null<br/>
     */
    @RequestMapping("/create")
    public JsonResult<?> doCreate(@RequestBody MemberWithdrawApplyBo vo){
        try{
            if (vo == null) {
                throw new WakaException("vo不能为空");
            }
            return JsonResult.newSuccess(adminWithdrawApplyService.create(vo));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }

    }
    /**删除记录
     * @param id null<br/>
     */
    @RequestMapping("/delete")
    public JsonResult<Boolean> doDelete(@RequestParam Long id){
        try{
            if (id == null) {
                throw new WakaException("id不能为空");
            }

            return JsonResult.newSuccess(adminWithdrawApplyService.delete(id));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }

    }
    /**取得单条记录
     * @param id null<br/>
     */
    @RequestMapping("/get")
    public JsonResult<MemberWithdrawApplyVo> doGet(@RequestParam Long id){
        try{
            if (id == null) {
                throw new WakaException("id不能为空");
            }

            return JsonResult.newSuccess(adminWithdrawApplyService.getOne(id));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }

    }
    /**列表记录
     * @param query 查询条件<br/>
     * @param start 开始行号<br/>
     * @param size 记录数<br/>
     * @param sortfield 排序(+字段1,-字段名2)<br/>
     */
    @RequestMapping("/list")
    public JsonResult<Page<MemberWithdrawApplyVo>> doList(@RequestBody List<QueryFilter> query,Integer start,Integer size,String sortfield){
        try{
//            if (Tools.collection.isEmpty(query) || start < 0 || size < 0 || Tools.string.isEmpty(sortfield)) {
//                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
//            }
            return JsonResult.newSuccess(adminWithdrawApplyService.getList(query, start, size, sortfield));
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
     * 提现记录审核
     * @param vo
     */
    @RequestMapping("/update")
    public JsonResult<Boolean> doUpdate(@RequestBody MemberWithdrawApplyBo vo, Long sysUserId){
    	 try{
	    	if(vo == null){
                throw new WakaException("vo不能为空");
	        }
	        if(null == vo.getWithdrawStatus()) {
                throw new WakaException("审核状态不能为空");
	        }
	        if(vo.getWithdrawStatus() == WithdrawStatusEnum.Withdraw_Fail && Tools.string.isEmpty(vo.getRemark())) {
                throw new WakaException("拒绝提现，需要填写拒绝原因");
	        }
	        
	        Boolean rs = memberWithdrawApplyBiz.updateWithdrawApply(vo, sysUserId);
	        
	        return JsonResult.newSuccess(rs);
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