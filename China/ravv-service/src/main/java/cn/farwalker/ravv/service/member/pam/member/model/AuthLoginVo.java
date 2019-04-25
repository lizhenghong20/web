package cn.farwalker.ravv.service.member.pam.member.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by asus on 2018/11/12.
 */
@Data
public class AuthLoginVo implements Serializable{
    private static final long serialVersionUID = 1L;
    private String token;
    private String account;
    private String loginType;
    private String randomKey;
    private String avator;
    private Boolean bindPhone;
}
