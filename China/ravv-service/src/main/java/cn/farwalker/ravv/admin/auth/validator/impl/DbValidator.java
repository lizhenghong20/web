package cn.farwalker.ravv.admin.auth.validator.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.farwalker.ravv.service.sys.user.biz.ISysUserBiz;
import cn.farwalker.ravv.service.sys.user.model.SysUserBo;
import cn.farwalker.waka.auth.validator.IReqValidator;
import cn.farwalker.waka.auth.validator.dto.Credence;
import cn.farwalker.waka.util.MD5PasswordUtil;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * 账号密码验证
 *
 * @author Jason Chen
 * @date 2018-02-13 12:34
 */
@Service
public class DbValidator implements IReqValidator {

    @Autowired
    private ISysUserBiz userBiz;

    @Override
    public Object validate(Credence credence) {
    	//User userExist = new User();
    	Wrapper<SysUserBo> query = new EntityWrapper<>();
    	query.eq(SysUserBo.Key.account.toString(), credence.getCredenceName());
    	query.eq(SysUserBo.Key.password.toString(), credence.getCredenceCode());
    	
    	SysUserBo user = userBiz.selectOne(query);
        if (user != null) {
        	String salt = user.getSalt();
        	String dbEncryptedPassword = user.getPassword();
        	if(MD5PasswordUtil.verify(credence.getCredenceCode(), salt, dbEncryptedPassword)) {
        		return true;
        	}
            return false;
        } else {
            return false;
        }
    }
}
