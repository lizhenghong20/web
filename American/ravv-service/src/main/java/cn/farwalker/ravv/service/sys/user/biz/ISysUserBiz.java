package cn.farwalker.ravv.service.sys.user.biz;
import com.baomidou.mybatisplus.service.IService;
import cn.farwalker.ravv.service.sys.user.model.SysUserBo;

/**
 * 管理员表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface ISysUserBiz extends IService<SysUserBo>{
	
	/**
	 * 根据账号获取后台管理员信息
	 * @param account
	 * @return
	 */
	SysUserBo getUserInfoByAccount(String account);
}