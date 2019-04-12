package cn.farwalker.ravv.service.sys.syslogs.biz;
import com.baomidou.mybatisplus.service.IService;

import cn.farwalker.ravv.service.sys.syslogs.model.SysLogsBo;

/**
 * 系统操作日志<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface ISysLogsBiz extends IService<SysLogsBo>{
	
	/**
	 * 判断会员推荐人推荐码是否已修改过
	 * @param sourceid 来源id(字段内容)
	 * @return
	 */
	Boolean checkReReferalCode(String sourceid);
	
}