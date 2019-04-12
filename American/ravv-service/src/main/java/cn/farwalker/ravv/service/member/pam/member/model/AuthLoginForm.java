package cn.farwalker.ravv.service.member.pam.member.model;

import lombok.Data;

/**
 * Created by asus on 2018/11/7.
 */
@Data
public class AuthLoginForm {
    private String account;
    private String password;
    private int type;
}
