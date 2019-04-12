package cn.farwalker.ravv.service.sys.loginlog.biz;
import com.baomidou.mybatisplus.service.IService;
import cn.farwalker.ravv.service.sys.loginlog.model.SysLoginLogBo;

/**
 * 登录记录<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface ISysLoginLogBiz extends IService<SysLoginLogBo>{
	
	/**
	 * 运营后台管理员登录日志
	 * @param account 登录账号
	 * @param ip 登录地址IP 
	 * @param succeed 是否登录成功
	 * @return
	 */
	Boolean saveAdminLoginLog(String account, String ip, String succeed);
	
}