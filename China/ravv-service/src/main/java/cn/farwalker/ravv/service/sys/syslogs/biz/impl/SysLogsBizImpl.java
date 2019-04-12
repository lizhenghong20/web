package cn.farwalker.ravv.service.sys.syslogs.biz.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.farwalker.ravv.service.sys.syslogs.biz.ISysLogsBiz;
import cn.farwalker.ravv.service.sys.syslogs.dao.ISysLogsDao;
import cn.farwalker.ravv.service.sys.syslogs.model.SysLogsBo;

/**
 * 系统操作日志<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
@Service
public class SysLogsBizImpl extends ServiceImpl<ISysLogsDao, SysLogsBo> implements ISysLogsBiz {

	@Resource
	private ISysLogsBiz sysLogsBiz;

	@Override
	public Boolean checkReReferalCode(String sourceid) {
		Wrapper<SysLogsBo> wrapper = new EntityWrapper<>();
		wrapper.eq(SysLogsBo.Key.sourcetable.toString(), "member");
		wrapper.eq(SysLogsBo.Key.sourcefield.toString(), "referrer_referal_code");
		wrapper.eq(SysLogsBo.Key.sourceid.toString(), sourceid);

		SysLogsBo sysLogsBo = sysLogsBiz.selectOne(wrapper);
		Boolean rs = false;
		if (null != sysLogsBo) {
			rs = true;
		}

		return rs;
	}
}