package cn.farwalker.standard.modular.system.factory;

import org.springframework.beans.BeanUtils;

import cn.farwalker.ravv.service.sys.user.model.SysUserBo;
import cn.farwalker.standard.modular.system.transfer.UserDto;

/**
 * 用户创建工厂
 *
 * @author Jason Chen
 * @date 2017-11-05 22:43
 */
public class UserFactory {

    public static SysUserBo createUser(UserDto userDto){
        if(userDto == null){
            return null;
        }else{
        	SysUserBo user = new SysUserBo();
            BeanUtils.copyProperties(userDto,user);
            return user;
        }
    }
}
