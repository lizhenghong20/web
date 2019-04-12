package cn.farwalker.ravv.service.member.level.biz;
import com.baomidou.mybatisplus.service.IService;

import cn.farwalker.ravv.service.category.basecategory.model.BaseCategoryBo;
import cn.farwalker.ravv.service.member.level.model.MemberLevelBo;

/**
 * 会员级别<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IMemberLevelBiz extends IService<MemberLevelBo>{
	
	/**
	 * 获取表中的会员默认等级
	 * @param  defaultLevel 默认等级
	 * @return
	 */
	MemberLevelBo getSameByDefaultLevel();
	
}