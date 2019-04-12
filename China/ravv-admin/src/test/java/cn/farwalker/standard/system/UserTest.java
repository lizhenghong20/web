package cn.farwalker.standard.system;

import javax.annotation.Resource;

import org.junit.Test;

import cn.farwalker.ravv.service.sys.user.dao.ISysUserDao;
import cn.farwalker.ravv.service.sys.user.dao.ISysUserMgrDao;
import cn.farwalker.standard.base.BaseJunit;

/**
 * 用户测试
 *
 * @author Jason Chen
 * @date 2017-04-27 17:05
 */
public class UserTest extends BaseJunit {

    @Resource
    ISysUserMgrDao userMgrDao;

    @Resource
    ISysUserDao userMapper;

    @Test
    public void userTest() throws Exception {

    }

}
