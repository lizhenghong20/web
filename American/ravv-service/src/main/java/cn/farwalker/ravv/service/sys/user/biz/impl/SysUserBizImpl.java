package cn.farwalker.ravv.service.sys.user.biz.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import cn.farwalker.ravv.service.sys.user.model.SysUserBo;
import cn.farwalker.waka.components.wechatpay.common.util.StringUtils;
import cn.farwalker.ravv.service.sys.user.dao.ISysUserDao;
import cn.farwalker.ravv.service.sys.user.biz.ISysUserBiz;

/**
 * 管理员表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class SysUserBizImpl extends ServiceImpl<ISysUserDao,SysUserBo> implements ISysUserBiz{
	
	@Resource
	private ISysUserBiz sysUserBiz;

	@Override
	public SysUserBo getUserInfoByAccount(String account) {
		if(StringUtils.isEmpty(account)) {
			return null;
		}
		Wrapper<SysUserBo> wrapper = new EntityWrapper<>();
		wrapper.eq(SysUserBo.Key.account.toString(), account);
		
		return sysUserBiz.selectOne(wrapper);
	}
}