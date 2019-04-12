package cn.farwalker.ravv.service.sys.param.biz.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import cn.farwalker.ravv.service.sys.param.model.SysParamBo;
import cn.farwalker.ravv.service.sys.param.dao.ISysParamDao;
import cn.farwalker.ravv.service.sys.param.biz.ISysParamBiz;

/**
 * 系统参数<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class SysParamBizImpl extends ServiceImpl<ISysParamDao,SysParamBo> implements ISysParamBiz{

	@Resource
	private ISysParamBiz sysParamBiz;
	
	@Override
	public Boolean checkSameDistLevel(Long paramId, String code) {
		Wrapper<SysParamBo> wrapper = new EntityWrapper<>();
		wrapper.eq(SysParamBo.Key.code.toString(), code);
		
		SysParamBo sysParam = sysParamBiz.selectOne(wrapper);
		
		Boolean rs = false;
		if(null != sysParam) {
			if(!sysParam.getId().equals(paramId)) {
				rs = true;
			}
		}
		
		return rs;
	}

	@Override
	public SysParamBo getParamByCode(String code) {
		Wrapper<SysParamBo> wrapper = new EntityWrapper<>();
		wrapper.eq(SysParamBo.Key.code.toString(), code);
		
		return sysParamBiz.selectOne(wrapper);
	}
}