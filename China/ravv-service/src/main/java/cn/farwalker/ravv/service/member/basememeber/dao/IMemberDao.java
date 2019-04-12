package cn.farwalker.ravv.service.member.basememeber.dao;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.member.basememeber.model.MemberSimpleInfoVo;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * member表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Repository
public interface IMemberDao extends BaseMapper<MemberBo>{
	 
	/**
	 * 获取第一级下级推荐人
	 * @param referrerReferalCode
	 * @return
	 */
	List<MemberSimpleInfoVo> getFristSubordinate(Pagination relativePage, @Param("referrerReferalCode")String referrerReferalCode);
	
	/**
	 * 获取第二级下级推荐人
	 * @param referrerReferalCode
	 * @return
	 */
	List<MemberSimpleInfoVo> getSecondSubordinate(Pagination relativePage, @Param("referrerReferalCode")String referrerReferalCode);
	
	/**
	 * 获取第三级下级推荐人
	 * @param referrerReferalCode
	 * @return
	 */
	List<MemberSimpleInfoVo> getThirdSubordinate(Pagination relativePage, @Param("referrerReferalCode")String referrerReferalCode);
}