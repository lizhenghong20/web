package cn.farwalker.ravv.service.sys.loginlog.biz.impl;
import java.util.List;
import javax.annotation.Resource;

import cn.farwalker.waka.core.WakaException;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import cn.farwalker.ravv.service.sys.loginlog.model.SysLoginLogBo;
import cn.farwalker.ravv.service.sys.user.biz.ISysUserBiz;
import cn.farwalker.ravv.service.sys.user.model.SysUserBo;

import cn.farwalker.ravv.service.sys.loginlog.dao.ISysLoginLogDao;
import cn.farwalker.ravv.service.sys.loginlog.biz.ISysLoginLogBiz;
import org.springframework.transaction.annotation.Transactional;

/**
 * 登录记录<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class SysLoginLogBizImpl extends ServiceImpl<ISysLoginLogDao,SysLoginLogBo> implements ISysLoginLogBiz{


	@Resource
	private ISysLoginLogBiz sysLoginLogBiz;
	
	@Resource
	private ISysUserBiz sysUserBiz;
	
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Boolean saveAdminLoginLog(String account, String ip, String succeed) {
		//获取管理员信息
		SysUserBo user =  sysUserBiz.getUserInfoByAccount(account);
		if(null == user) {
			throw new WakaException("账号不能为空");
		}
		//存储登录日志
		SysLoginLogBo sysLoginLog = new SysLoginLogBo();
		sysLoginLog.setUserid(user.getId());
		sysLoginLog.setLogname(user.getAccount());
		sysLoginLog.setSucceed(succeed);
		sysLoginLog.setMessage(null);
		sysLoginLog.setIp(ip);
		
		Boolean rs = sysLoginLogBiz.insert(sysLoginLog);
		
		return rs;
	}
}