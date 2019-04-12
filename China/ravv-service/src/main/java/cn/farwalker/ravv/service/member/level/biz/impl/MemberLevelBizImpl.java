package cn.farwalker.ravv.service.member.level.biz.impl;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

import org.hibernate.validator.cfg.context.ReturnValueConstraintMappingContext;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitterReturnValueHandler;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import cn.farwalker.ravv.service.member.level.model.MemberLevelBo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.ravv.service.member.level.dao.IMemberLevelDao;
import cn.farwalker.ravv.service.category.basecategory.model.BaseCategoryBo;
import cn.farwalker.ravv.service.member.level.biz.IMemberLevelBiz;

/**
 * 会员级别<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class MemberLevelBizImpl extends ServiceImpl<IMemberLevelDao,MemberLevelBo> implements IMemberLevelBiz{

	 @Resource
	    private IMemberLevelBiz memberLevelBiz;
	
	@Override
	public MemberLevelBo getSameByDefaultLevel() {
		  EntityWrapper<MemberLevelBo> wrapper = new EntityWrapper<>();	
		  
		  wrapper.eq(MemberLevelBo.Key.defaultLevel.toString(),true);
		  	
		  return memberLevelBiz.selectOne(wrapper);
	}

}