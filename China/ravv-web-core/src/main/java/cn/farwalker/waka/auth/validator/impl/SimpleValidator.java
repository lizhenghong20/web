package cn.farwalker.waka.auth.validator.impl;

import org.springframework.stereotype.Service;

import cn.farwalker.waka.auth.validator.IReqValidator;
import cn.farwalker.waka.auth.validator.dto.Credence;

/**
 * 直接验证账号密码是不是admin
 *
 * @author Jason Chen
 * @date 2018-02-13 12:34
 */
@Service
public class SimpleValidator implements IReqValidator {

    private static String USER_NAME = "admin";

    private static String PASSWORD = "admin";

    @Override
    public Object validate(Credence credence) {

        String userName = credence.getCredenceName();
        String password = credence.getCredenceCode();

        if (USER_NAME.equals(userName) && PASSWORD.equals(password)) {
            return new Object();
        } else {
            return null;
        }
    }
}
