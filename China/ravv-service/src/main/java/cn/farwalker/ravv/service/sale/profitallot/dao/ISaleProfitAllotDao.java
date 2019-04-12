package cn.farwalker.ravv.service.sale.profitallot.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import cn.farwalker.ravv.service.sale.profitallot.model.ProfitAllotInfoVo;
import cn.farwalker.ravv.service.sale.profitallot.model.SaleProfitAllotBo;

/**
 * 订单利润分配<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
public interface ISaleProfitAllotDao extends BaseMapper<SaleProfitAllotBo> {

	/**
	 * 获取某一状态会员分润信息列表
	 * 
	 * @param memberId 会员id
	 * @param status 分销状态
	 * @param allotInfoVoPage 分页信息
	 * @return
	 */
	List<ProfitAllotInfoVo> profitAllotInfoList(Pagination allotInfoPage, @Param("memberId") Long memberId, @Param("status") String status);

	/**
	 * 获取某一分销状态会员某月分润信息列表
	 * @param memberId
	 * @param status 分销状态
	 * @param firstDay 所选月份第一天
	 * @param lastDay 所选月份最后一天
	 * @param allotInfoVoPage 分页信息
	 * @return
	 */
	List<ProfitAllotInfoVo> getMonthRebatedlist(Pagination allotInfoVoPage, @Param("memberId") Long memberId, @Param("status") String status,
			@Param("firstDay") Date firstDay, @Param("lastDay") Date lastDay);

}