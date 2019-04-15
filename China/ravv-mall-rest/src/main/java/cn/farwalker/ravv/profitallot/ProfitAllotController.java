package cn.farwalker.ravv.profitallot;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import cn.farwalker.waka.core.HttpKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;

import cn.farwalker.ravv.order.CreateOrderController;
import cn.farwalker.ravv.profitallot.dto.MemberPromoteInfoVo;
import cn.farwalker.ravv.profitallot.dto.MemberRelativeTreeVo;
import cn.farwalker.ravv.profitallot.dto.RebatedMonthVo;
import cn.farwalker.ravv.service.member.basememeber.biz.IMemberBiz;
import cn.farwalker.ravv.service.member.basememeber.constants.MemberSubordinate;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.member.basememeber.model.MemberSimpleInfoVo;
import cn.farwalker.ravv.service.sale.profitallot.biz.ISaleProfitAllotBiz;
import cn.farwalker.ravv.service.sale.profitallot.constants.DistStatusEnum;
import cn.farwalker.ravv.service.sale.profitallot.model.ProfitAllotInfoVo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;

/**
 * 分销相关接口<br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@RestController
@RequestMapping("/profitallot")
public class ProfitAllotController {
    private final static  Logger log =LoggerFactory.getLogger(ProfitAllotController.class);
    @Resource
    private ISaleProfitAllotBiz saleProfitAllotBiz;
    protected ISaleProfitAllotBiz getBiz(){
        return saleProfitAllotBiz;
    }
    
    @Resource
    private IMemberBiz memberBiz;
    
    /**
     * 获取会员分销基本信息和统计数据
     * @return
     */
    @RequestMapping("/getpromoteInfo")
    public JsonResult<MemberPromoteInfoVo> getPromoteInfo(){
    	HttpSession sin = HttpKit.getRequest().getSession();
    	Long memberId = (Long)sin.getAttribute(CreateOrderController.K_MemberId);
    	
    	//获取会员信息
    	MemberBo member = memberBiz.selectById(memberId);
    	if(null == member) {
    		return JsonResult.newFail("找不到该会员信息，确认是否登录");
    	}
    	MemberPromoteInfoVo promoteInfo = new MemberPromoteInfoVo();
    	Tools.bean.copyProperties(member, promoteInfo);
    	
    	//补全头像路径
    	promoteInfo.setAvator(QiniuUtil.getFullPath(promoteInfo.getAvator()));
    	
    	return JsonResult.newSuccess(promoteInfo);
    }
    
    /**
     * 获取会员分销关系（上级和下三级信息）
     * @param start 页码
     * @param size 每页行数
     * @return
     */
    @RequestMapping("/getrelativetree")
    public JsonResult<MemberRelativeTreeVo> getRelativeTree(Integer start, Integer size){
    	HttpSession sin =HttpKit.getRequest().getSession();
    	Long memberId = (Long)sin.getAttribute(CreateOrderController.K_MemberId);
    	
    	//获取会员信息
    	MemberBo member = memberBiz.selectById(memberId);
    	if(null == member) {
    		return JsonResult.newFail("找不到该会员信息，确认是否登录");
    	}
    	if(null == member.getReferralCode()) {
    		return JsonResult.newFail("该会员没有推荐码，请联系管理员");
    	}
    	
    	MemberRelativeTreeVo MemberRelativeTree = new MemberRelativeTreeVo();
    	//获取会员上级推荐人
    	MemberSimpleInfoVo supurior = memberBiz.getMemberSupurior(memberId);
    	MemberRelativeTree.setSupurior(supurior);
    	
    	//获取第一级下级推荐人
    	Page<MemberSimpleInfoVo> fristSubordinate = memberBiz.subordinateList(member.getReferralCode(), MemberSubordinate.Frist_Subordinate, start, size);
    	MemberRelativeTree.setFristSubordinate(fristSubordinate);
    	
    	//获取第二级下级推荐人
    	Page<MemberSimpleInfoVo> secondSubordinate = memberBiz.subordinateList(member.getReferralCode(), MemberSubordinate.Second_Subordinate, start, size);
    	MemberRelativeTree.setSecondSubordinate(secondSubordinate);
    	
    	//获取第三级下级推荐人
    	Page<MemberSimpleInfoVo> thirdSubordinate = memberBiz.subordinateList(member.getReferralCode(), MemberSubordinate.Third_Subordinate, start, size);
    	MemberRelativeTree.setThirdSubordinate(thirdSubordinate);
    	
    	return JsonResult.newSuccess(MemberRelativeTree);
    }
    
    /**
     * 获取会员分销关系(下级任意一级)
     * @param start 页码
     * @param size 每页行数
     * @return
     */
    @RequestMapping("/getrelativelevel")
    public JsonResult<Page<MemberSimpleInfoVo>> getRelativeByLevel(MemberSubordinate subordinate, Integer start, Integer size){
    	HttpSession sin =HttpKit.getRequest().getSession();
    	Long memberId = (Long)sin.getAttribute(CreateOrderController.K_MemberId);
    	
    	if(null == subordinate) {
    		return JsonResult.newFail("请选择下级推荐人等级");
    	}
    	
    	//获取会员信息
    	MemberBo member = memberBiz.selectById(memberId);
    	if(null == member) {
    		return JsonResult.newFail("找不到该会员信息，确认是否登录");
    	}
		
    	Page<MemberSimpleInfoVo> relativePage = memberBiz.subordinateList(member.getReferralCode(), subordinate, start, size);
    	
    	return JsonResult.newSuccess(relativePage);
    }
    
    /**
     * 获取某一分销状态的分销列表
     * @param status 分销状态
     * @return
     */
    @RequestMapping("/rebatedlist_bystatus")
    public JsonResult<Page<ProfitAllotInfoVo>> getRebatedListByStatus(DistStatusEnum status, Integer start, Integer size){
    	HttpSession sin =HttpKit.getRequest().getSession();
    	Long memberId = (Long)sin.getAttribute(CreateOrderController.K_MemberId);
    	
    	if(null == status) {
    		return JsonResult.newFail("分润状态不能为空");
    	}
    	
    	//获取会员信息
    	MemberBo member = memberBiz.selectById(memberId);
    	if(null == member) {
    		return JsonResult.newFail("找不到该会员信息，确认是否登录");
    	}
    	
    	Page<ProfitAllotInfoVo> profitAllotInfoPage = saleProfitAllotBiz.profitAllotInfoPage(memberId, status, start, size);
    	
    	return JsonResult.newSuccess(profitAllotInfoPage);
    }
    
    /**
     * 获取会员已返现的分销记录列表（按月份分类和统计）
     * @return
     */
    @RequestMapping("/getrebatedlist")
    public JsonResult<Page<RebatedMonthVo>> getRebatedList(Integer start, Integer size){
    	HttpSession sin =HttpKit.getRequest().getSession();
    	Long memberId = (Long)sin.getAttribute(CreateOrderController.K_MemberId);
		Page<RebatedMonthVo> rebatedMonthVoPage = new Page<>();
       	//获取会员信息
    	MemberBo member = memberBiz.selectById(memberId);
    	if(null == member) {
    		return JsonResult.newFail("找不到该会员信息，确认是否登录");
    	}
    	
    	//获取已返现分销记录
    	Page<ProfitAllotInfoVo> profitAllotInfoList = saleProfitAllotBiz.profitAllotInfoPage(memberId, DistStatusEnum.Returned, start, size);
    	if(Tools.collection.isEmpty(profitAllotInfoList.getRecords())){
    		return JsonResult.newSuccess(rebatedMonthVoPage);
    	}
        
        //按月份排序
    	List<RebatedMonthVo> rebatedMonthVoList = this.rebatedSort(profitAllotInfoList.getRecords());
    	
    	//转换为分页格式，方便前端做分页，分页仍依据分销记录

    	rebatedMonthVoPage.setCurrent(profitAllotInfoList.getCurrent());
    	rebatedMonthVoPage.setSize(profitAllotInfoList.getSize());
    	rebatedMonthVoPage.setTotal(profitAllotInfoList.getTotal());
    	rebatedMonthVoPage.setRecords(rebatedMonthVoList);
        
    	return JsonResult.newSuccess(rebatedMonthVoPage);
    }
    
    /**
     * 获取某月已返现分销列表
     * @param month 所选择月份
     * @return
     */
    @RequestMapping("/month_rebatedlist")
    public JsonResult<Page<RebatedMonthVo>> getMonthRebatedlist(Date month, Integer start, Integer size){
    	HttpSession sin =HttpKit.getRequest().getSession();
    	Long memberId = (Long)sin.getAttribute(CreateOrderController.K_MemberId);
    	
    	if(null == month) {
    		return JsonResult.newFail("请选择月份");
    	}
    	
       	//获取会员信息
    	MemberBo member = memberBiz.selectById(memberId);
    	if(null == member) {
    		return JsonResult.newFail("找不到该会员信息，确认是否登录");
    	}
		
		//获取所选月份的分销记录
		Page<ProfitAllotInfoVo> allotInfoVoList = saleProfitAllotBiz.getMonthRebatedPage(memberId, month, DistStatusEnum.Returned, start, size);
		
		//返回所选月份的分销信息列表
		RebatedMonthVo rebatedMonthVo = new RebatedMonthVo();
		if(Tools.collection.isNotEmpty(allotInfoVoList.getRecords())) {
			BigDecimal amtMonthTotal = new BigDecimal(0.00);
			String date = null;
			for(ProfitAllotInfoVo allotInfoVo : allotInfoVoList.getRecords()) {        		
				date = allotInfoVo.getReturnedTime();
				allotInfoVo.setAvator(QiniuUtil.getFullPath(allotInfoVo.getAvator()));
				amtMonthTotal = amtMonthTotal.add(allotInfoVo.getAmt());
			}
			
        	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            try {
				rebatedMonthVo.setDate(dateFormat.parse(date));
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}   
			rebatedMonthVo.setAmtMonthTotal(amtMonthTotal);
			rebatedMonthVo.setProfitAllotInfoList(allotInfoVoList.getRecords());
		}
		
    	//转换为分页格式，方便前端做分页，分页仍依据分销记录
    	Page<RebatedMonthVo> rebatedMonthVoPage = new Page<>(allotInfoVoList.getCurrent(),allotInfoVoList.getSize());
    	rebatedMonthVoPage.setTotal(allotInfoVoList.getTotal());
    	List<RebatedMonthVo> rebatedMonthVoList = new ArrayList<>();
    	rebatedMonthVoList.add(rebatedMonthVo);
    	rebatedMonthVoPage.setRecords(rebatedMonthVoList);
    	
    	return JsonResult.newSuccess(rebatedMonthVoPage);
    }
    
    /**
     * 按月份分组排序
     * @param  分销按月分类列表信息
     */
    private List<RebatedMonthVo> rebatedSort(List<ProfitAllotInfoVo> profitAllotInfoList) {
    	//按月份分组
        Map<String,List<ProfitAllotInfoVo>> profitAllotInfoVo = profitAllotInfoList.stream().collect(Collectors.groupingBy(ProfitAllotInfoVo::getReturnedTime));
        
        List<RebatedMonthVo> rebatedMonthVoList = new ArrayList<>();
        for(List<ProfitAllotInfoVo> allotInfoVoList : profitAllotInfoVo.values()){
        	BigDecimal amtMonthTotal = new BigDecimal(0.00);
        	String date = null;
        	for(ProfitAllotInfoVo allotInfoVo : allotInfoVoList) {
        		date = allotInfoVo.getReturnedTime();
        		allotInfoVo.setAvator(QiniuUtil.getFullPath(allotInfoVo.getAvator()));
        		amtMonthTotal = amtMonthTotal.add(allotInfoVo.getAmt());
        	}
        	
        	RebatedMonthVo rebatedMonthVo = new RebatedMonthVo();
        	
        	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            try {
				rebatedMonthVo.setDate(dateFormat.parse(date));
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}   
            
        	rebatedMonthVo.setAmtMonthTotal(amtMonthTotal);
        	rebatedMonthVo.setProfitAllotInfoList(allotInfoVoList);
        	
        	rebatedMonthVoList.add(rebatedMonthVo);
        }
    	
        Collections.sort(rebatedMonthVoList, new Comparator<RebatedMonthVo>() {
            public int compare(RebatedMonthVo o1, RebatedMonthVo o2) {
                Date first = o1.getDate();
                Date second = o2.getDate();
                // 升序
//                return first.compareTo(second);
 
                // 降序
                 return second.compareTo(first);
            }
        });
        
        return rebatedMonthVoList;
    }
    
}