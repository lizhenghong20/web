package cn.farwalker.ravv.service.sys.param.biz;
import com.baomidou.mybatisplus.service.IService;
import cn.farwalker.ravv.service.sys.param.model.SysParamBo;

/**
 * 系统参数<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface ISysParamBiz extends IService<SysParamBo>{
	
	/**
	 * 检查是否有相同的分销层级
	 * @param paramId 系统参数id
	 * @param code 系统参数编码
	 * @return
	 */
	Boolean checkSameDistLevel(Long paramId, String code);
	
	/**
	 * 根据系统配置编码获取系统配置信息
	 * @param code
	 * @return
	 */
	SysParamBo getParamByCode(String code);
}